package services.microservices;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import services.AuthenticatorQueMgr;
import services.microservices.filehandling.FileReaderRunnable;
import services.microservices.filehandling.ProgressBarUpdater;
import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.threadpool.GeneralThreadPool;
import services.microservices.utilities.Housekeeper;
import services.microservices.utilities.logger.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/*
* @author Rohan
* This is the class which will actually sends the file to the user.
* It essentially performs these functions
*   1.opens a stream to the user and serve reuest
*       a. LIST_REQUEST
*               will send the list of accessible files to the user.
*       b.
*   2.executes postexecute and preexecute if not null
*
* NOTE this class will be queued only when its thread notifier is set.
* */
/*TBD To override an interface called QueManager which has method add and remove.
remove will remove data from both the list and the database. Since DatabaseHelper has static function no need to
create an object
*/
public class Comms implements Runnable,ProgressBarUpdater,CommsConstants{
    //if isServer then  first write later read and then send else write first readLater;
    private UserInfo userInfo;
    private ThreadNotifier threadNotifier;
    private ProgressBar progressBar;
    private PrintWriter printWriter=null;

    //tbd need a reference of FilesLister

    //the thread pool is static since only 1 thraed pool is required for all file reader threads.
    private static GeneralThreadPool commsThreadPool;
    //this is the list of files that are to be sent to the user.

    /* the arraylist isnt being used ... reference isnt required as of now*/
    //this is going to be set as soon as the FileLister thread calls its callback
    /* this reference will be used whenever the FileWatcher notices a change in the dir*/
    private static ArrayList<String> fileListerList;




    //this is the string with which the fileTree will constructed

    private static String treeString;

    //to be used to remove clients once they send a disconnect request or once an exception occurs.
    private static AuthenticatorQueMgr queMgr;


    /* all these members are static since all Comms objs are going to share these and new memory
    * isnt required by them for all instances of this class.
    *
    * Also NOTE the members accessibleFiles and treeString are going to be set from withing Server class as soon
    * as the FilesLister completes obtaining the list of files.
    *
    * */
    static{
        Comms.commsThreadPool=new GeneralThreadPool();
        Comms.fileListerList=new ArrayList<>();
        Comms.treeString=new String();
    }


    public static void setQueMgr(AuthenticatorQueMgr authenticatorQueMgr){
        Comms.queMgr=authenticatorQueMgr;
    }



    //private boolean isServer;

    public Comms(final UserInfo userInfo){
        this.userInfo=userInfo;
    }


    public Comms(final UserInfo userInfo,final ThreadNotifier threadNotifier){
            this(userInfo);
            this.threadNotifier=threadNotifier;

    }


    /*public static void setAccessibleFiles(ArrayList<String> accessibleFiles){
        Comms.accessibleFiles=accessibleFiles;
    }*/
    public static void setTreeString(final String treeString){
        Comms.treeString=treeString;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setThreadNotifier(ThreadNotifier threadNotifier){
        this.threadNotifier=threadNotifier;
        //queuing the job
        this.addCurrentToQueue();
    }
    public static void setFileListerList(ArrayList<String> arr){
        Comms.fileListerList=arr;
    }

    @Override
    public void run(){
        BufferedReader bufferedReader=null;

        if(this.threadNotifier!=null)
            this.threadNotifier.onPreExecute(this.userInfo.getUsername());
        try {
            this.printWriter=new PrintWriter(this.userInfo.getOutputStream(),true);
            bufferedReader=new BufferedReader(new InputStreamReader(this.userInfo.getInputStream()));

            this.printWriter.println("{message: \""+this.userInfo.getUsername()+"sending requested files\"}");

            String s;
            while((s=bufferedReader.readLine())!=null){
                /*TBD send the arraylist of files to the user. Will be obtained from fileRunnables*/
                if(s.contains(CommsConstants.LIST_REQUEST))
                    /* this string is going to be used to construct a fileTree*/
                    this.printWriter.println(Comms.treeString);

                else if(s.contains(CommsConstants.RECEIVING_LIST)){

                    //code to heed to request and obtain a list of strings
                    ObjectInputStream objectInputStream=new ObjectInputStream(this.userInfo.getInputStream());
                    //cant cast to ArrayList since objectInputStream understands List for somereason
                    /*
                    * The client is expected to send a list of Strings ie List<String>
                    * */
                    try {
                        List<String> reqeustedFiles = (List<String>) objectInputStream.readObject();

                        objectInputStream.close();
                        //code to send files to users.
                        this.iterateAndSendList(reqeustedFiles);


                    }catch (ClassNotFoundException cfe){
                        Logger.wtf(cfe.toString()+ABORT_MESSAGE);
                        System.out.println(ABORT_MESSAGE);
                    }



                }
                else if(s.contains("disconnect")){
                    /* removing the user*/
                    Comms.queMgr.removeFromAuthenticator(Comms.this);
                    break;
                }

            }
            if(this.threadNotifier!=null)
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Comms.this.threadNotifier.onPostExecute(null);
                    }
                });



        }catch (IOException ioe){
            queMgr.removeFromAuthenticator(Comms.this);
            Logger.wtf(ioe.toString());
            ioe.printStackTrace();
        }finally {
            try{
                if(printWriter!=null)
                    printWriter.close();
                if(bufferedReader!=null)
                    bufferedReader.close();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }

        /* TBD
        * add code to remove the current user from the queue. ie when a disconnect request is sent
        * */
        this.addCurrentToQueue();
    }



    private void iterateAndSendList(List<String> requestedFiles){
        for(String fstr:requestedFiles){
            /*TBD
            * Length of progress bar will be the sum of the size of the entire list... Will have to set it manually.
            * */
            CustomFile customFile= FileReaderRunnable.getPool().get(fstr);
            if(customFile!=null)
                Housekeeper.writeJob(this.userInfo.getOutputStream(),customFile,this.progressBar);
            else
                this.writeErrorToStream(fstr);
        }
    }
    /* to be used only when the file cant be written to stream*/
    private void writeErrorToStream(String fName){
        if(this.printWriter!=null)
            this.printWriter.println(ERROR+"\tCouldnt write "+fName);

        Logger.i("Couldnt send "+fName+" to "+this.userInfo.getUsername());
    }


    @Override
    public void setProgress(final ProgressBar progress){
        this.progressBar=progress;
        //TBD code to add this thread to the executable queue.
       this.addCurrentToQueue();

    }

    private void addCurrentToQueue(){
        Comms.commsThreadPool.add(Comms.this);
    }



}

interface CommsConstants{
    public static final String ERROR="ERROR:";
    public static final String RECEIVING_LIST="sendingRequest";
    public static final String LIST_REQUEST="giveMeList";
    public static final String ABORT_MESSAGE=" \nABORTING SEND OPERATION COULDNT FIND CLASS";
}
