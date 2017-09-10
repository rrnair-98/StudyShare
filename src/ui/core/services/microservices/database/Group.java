package ui.core.services.microservices.database;

import ui.core.services.microservices.ThreadNotifier;
import ui.core.services.microservices.database.filelister.FilesLister;

import java.util.ArrayList;

/*
* @author Rohan
* This class serves the purpose of obtaining group info from the database.
*  This class performs the following
*   1. Retreives the folders stored in the table with the provided groupName
*   2. starts a thread called filelister which searches and lists all files present in all directories
*   3. returns the list of folders for FileWatchers to be set on them
* */
public class Group {

    private String groupName;
    private FilesLister filesLister;
    private ArrayList<String> listOfFolders;
    /* Assuming the groupName is part of the table Roles. */
    public Group(String groupName, ThreadNotifier threadNotifier){
        this.groupName=groupName;
        this.listOfFolders=DatabaseHelper.getFilePaths(this.groupName);
        this.filesLister= new FilesLister(this.listOfFolders,threadNotifier);
    }

    public ArrayList<String> getListOfFiles(){
        return this.filesLister.getActualFilePaths();
    }


    /*
    * To be used to set FileWatchers
    * */
    public ArrayList<String> getListOfFolders() {
        return listOfFolders;
    }
}
