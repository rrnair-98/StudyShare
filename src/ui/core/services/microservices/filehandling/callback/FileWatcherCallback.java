package ui.core.services.microservices.filehandling.callback;
/*
* @author Rohan
* This interface is to be implemented by the classes that want to remove/add elements from a arraylist. This callback will be called
* whenever the FileWatcher detects changes in a particular directory.
* NOTE: filewatcher removes the entry from CustomFilePool so the only thing that needs to be done is remove the filePath from the main arraylist.
* */
public interface FileWatcherCallback {
    public void onAdd(String toBeAdded);
    public void onRemove(String toBeRemoved);
}
