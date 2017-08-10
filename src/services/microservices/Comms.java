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
import sun.rmi.rmic.iiop.Generator;

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

            /* to be removed.SHOULD BE ADDED TO QUEUE ONLY WHEN PGB REF IS SET */
            System.out.println("adding to queue");
            this.addCurrentToQueue();


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
    public void run() {
        BufferedReader bufferedReader = null;

        if (this.threadNotifier != null)
            this.threadNotifier.onPreExecute(this.userInfo.getUsername());
        try {
            this.printWriter = this.userInfo.getPrintWriter();
            bufferedReader = this.userInfo.getBufferedReader();
            String s;

            while ((s = bufferedReader.readLine()) != null) {
                System.out.println("RECEIVED MESSAGE " + s);
                /*TBD send the arraylist of files to the user. Will be obtained from fileRunnables*/
                if (s.contains(CommsConstants.LIST_REQUEST)) {
                    /* this string is going to be used to construct a fileTree*/
                    this.printWriter.println(Comms.treeString);
                    System.out.println("sending tree list");
                } else if (s.contains(CommsConstants.RECEIVING_LIST)) {

                    System.out.println("receving request");

                    //code to heed to request and obtain a list of strings
                    ObjectInputStream objectInputStream = new ObjectInputStream(this.userInfo.getInputStream());
                    //cant cast to ArrayList since objectInputStream understands List for somereason
                    /*
                    * The client is expected to send a list of Strings ie List<String>
                    * */


                    try {
                        ArrayList<String> reqeustedFiles = (ArrayList<String>) objectInputStream.readObject();

                        objectInputStream=null;
                        System.out.println(reqeustedFiles.toString());

                        //code to send files to users.
                        this.iterateAndSendList(reqeustedFiles);
                        this.userInfo.addToFilesUsed(reqeustedFiles);

                    } catch (ClassNotFoundException cfe) {
                        Logger.wtf(cfe.toString() + ABORT_MESSAGE);
                        System.out.println(ABORT_MESSAGE);
                    }


                } else if (s.contains(CommsConstants.DC)) {
                    /* removing the user*/
                    this.removeThisUser();
                    this.userInfo.close();
                    return;
                }

            }
            if (this.threadNotifier != null)
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Comms.this.threadNotifier.onPostExecute(null);
                    }
                });

        } catch (IOException | NullPointerException ioe) {
            this.removeThisUser();
            try {
                this.userInfo.close();
            } catch (IOException epp) {
                Logger.wtf(epp.toString());
            }
            Logger.wtf(ioe.toString());
            ioe.printStackTrace();
        } finally {
            /*try{
                if(printWriter!=null)
                    printWriter.flush();
                if(bufferedReader!=null)
                    bufferedReader.reset();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }*/



        /* TBD
        * add code to remove the current user from the queue. ie when a disconnect request is sent
        * */
            if (!this.userInfo.getIsClosed())
                this.addCurrentToQueue();
        }
    }



    private void removeThisUser(){
        queMgr.removeFromAuthenticator(Comms.this);

    }


    private void iterateAndSendList(ArrayList<String> requestedFiles)throws IOException{
        for(String fstr:requestedFiles){
            /*TBD
            * Length of progress bar will be the sum of the size of the entire list... Will have to set it manually.
            * */
            OutputStream outputStream=this.userInfo.getOutputStream();
            CustomFile customFile= FileReaderRunnable.getPool().get(fstr);
            if(customFile!=null) {
                System.out.println("sending "+fstr);
                Housekeeper.writeJob(outputStream, customFile, this.progressBar);
            }
            else {

                this.writeErrorToStream(fstr);
            }
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
    public static final String DC="disconnect";
}
