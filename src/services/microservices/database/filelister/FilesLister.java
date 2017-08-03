package services.microservices.database.filelister;

import services.microservices.ThreadNotifier;
import services.microservices.utilities.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
/*
* @author Rohan
* This class creates an arraylist of files which will be used by several classes.
* This class does the following
*   1.Receives a folderList from the database.
*   2.Calls preExecute and postExecute methods if set.Passes the arraylist of acutalFiles in the postExecute callback.
*   3.provides a method for accessing the files list
*
* An object of this class will have to be created again to update the list. Cant think of anything for the time being.
* */
public class FilesLister implements Runnable{

    private ArrayList<String> databaseFolderPaths,actualFilePaths;
    private Thread mThread;
    private ThreadNotifier threadNotifier;
    private StringBuilder stringBuilder;

    public FilesLister(ArrayList<String> files,ThreadNotifier threadNotifier){
        this.databaseFolderPaths=files;
        this.threadNotifier=threadNotifier;
        this.stringBuilder=new StringBuilder();
        this.mThread=new Thread(this);
        this.mThread.start();

    }
    @Override
    public void run(){
        if(this.threadNotifier!=null)
            this.threadNotifier.onPreExecute(null);
        /* TBD add files to a arraylist */
        StringBuilder stringBuilder=new StringBuilder();
        for(String str:databaseFolderPaths) {
            File file=new File(str);
            this.obtainFiles(file);
            FilesLister.makeFileTree(stringBuilder,file);

        }
        /* TBD code to construct a treeString for making the fileTree*/

        /* Passing the the arraylist to the implementer of the callback*/
        if(this.threadNotifier!=null)
            this.threadNotifier.onPostExecute(new Object[]{this.actualFilePaths,stringBuilder});
    }


    public ArrayList<String> getActualFilePaths(){return this.actualFilePaths;}

    private void obtainFiles(File root){
        try {
            if (root.exists()) {
                if (root.isDirectory()) {
                    for (File f : root.listFiles()) {
                        obtainFiles(f);
                    }
                } else
                    this.actualFilePaths.add(root.getCanonicalPath());
            }
        }catch (IOException io){
            Logger.wtf(io.toString());
        }
    }

    /* this IS REDUNDANT code ... but this will help me later.
    * makes changes to the reference of the strinbuilder that was passed to it.
    * */
    public static void makeFileTree(StringBuilder sb,File root){
        try {
            if (root.exists()) {
                if (root.isDirectory()) {
                    sb.append(root.getCanonicalPath()+":{");
                    for (File f : root.listFiles()) {
                        makeFileTree(sb,f);
                    }
                    sb.append("}");
                } else
                    sb.append(root.getName()+",");
            }
        }catch (IOException io){
            Logger.wtf(io.toString());
        }
    }



}
