import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import interfaces.*;
/*
@author Pratik
Comms is the main client communicating thread, this thread performs 3 tasks in this scenario
    1. Sends Login Details of a client to the server and waits until server's response is true or false (true means authenticated and false means otherwise).
    2. Requests the Server for the accessible file paths once authenticated by passing the ServerRequestConstant LIST_REQUEST.
    3. Once the user has submitted the files (through SENDING_REQUEST) to be requested list this threads downloads the files to User.dir/StudyShareDownload folder in the client machine.

This class makes the use of 2 interfaces ServerRequestMessages for LIST_REQUEST and SENDING_LIST constants and CommsMessages for the internal working of comms and the application class

FOR MAKING THE COMMS OBJECT PASS THE SOCKET'S STREAMS
APPLICATION CLASS SHOULD IMMEDIATELY CALL THE COMMS PROPERTY GETTERS FOR BIDIRECTIONAL BINDING AND SHOULD IMPLEMENT CHANGE LISTENER FOR THE RESPECTIVE PROPERTY OBJECTS
THE APPLICATION CLASS SHOULD MAKE AND FOLLOW THE FOLLOWING BIND MAP
APPLICATIONS'S COMMAND PROPERTY -----------BIDIRECTIONALLY BOUND WITH--------COMMS'S COMMAND PROPERTY
APPLICATIONS'S COMMAND PROPERTY -----------BIDIRECTIONALLY BOUND WITH--------COMMS'S ACCESSFILESRECIEVED
 */
public class Comms implements Runnable{

    private String serverRequest;
    private StringProperty commandProperty;
    private OutputStream serverWriterStream;
    private PrintWriter serverWriter;
    private boolean readyToRead;
    private InputStream serverReaderStream;
    private BufferedReader serverReader;
    private BooleanProperty accessibleFilePathsRecieved;
    private List<String> filesListToBeRequested;
    private DataInputStream downloadStream;
    private ArrayList<ProgressBar> pgbList;

    public Comms(OutputStream clientOutputStream,InputStream clientInputStream){
        serverRequest="";
        this.commandProperty=new SimpleStringProperty();
        this.serverWriterStream=clientOutputStream;
        this.serverReaderStream=clientInputStream;
        this.readyToRead=false;
        this.accessibleFilePathsRecieved= new SimpleBooleanProperty(false);
    }
    @Override
    public void run() {
        this.serverWriter=new PrintWriter(serverWriterStream,true);
        this.serverReader = new BufferedReader(new InputStreamReader(serverReaderStream));
        try {
            while (true) {
                if (!this.serverRequest.equals("")) {
                    this.serverWriter.println(this.serverRequest); // FOR PROPER EXECUTION CALL THE setserverRequest() with the proper ServerRequestConstants
                    this.readyToRead = true;//Once the request is performed the code is ready to run the sub loop is ready to read the server's response
                    while (this.readyToRead) {
                            String serverResponse = serverReader.readLine();
                            if (serverResponse != null && this.serverRequest.equals(ServerRequestConstants.LIST_REQUEST)) { //if the server response is not null and the call to setserverRequest() was done with LIST_REQUEST
                                this.commandProperty.setValue(serverResponse); // Set the command property to the string tree returned as the server response, this triggers the change event callback in the application class
                                this.accessibleFilePathsRecieved.setValue(true);// This instruction sets the bound accessibleFilePathsRevieved value to true, the boolean property needs to be checked if is true make the tree with the value of the command Property of the application class
                            } else if (this.serverRequest.equals(ServerRequestConstants.SENDING_LIST)) { //if the string to be written is SENDING_LIST the list object is sent to the user by object output stream
                                ObjectOutputStream filesListWriter = new ObjectOutputStream(this.serverWriterStream);
                                filesListWriter.writeObject(this.filesListToBeRequested);
                                this.commandProperty.setValue(CommsMessages.START_DOWNLOADING);// this value setting needs to be checked in the change event call back to generate the downloading, BUT BEFORE MAKING THE DOWNLOAD SCENE INITIALIZE THE PROGRESS BAR LISTS
                            }
                            this.readyToRead = false;
                        this.serverRequest = "";
                    }
                }
                else if(this.commandProperty.getValue().equals(CommsMessages.START_DOWNLOADING)){
                    File directory=new File(System.getProperty("user.dir")+"/StudyShareDownloads");
                    if(!directory.exists())
                        directory.mkdir();
                    this.downloadStream=new DataInputStream(serverReaderStream);
                    int readData;
                    int i=0;
                    while(i<filesListToBeRequested.size()) {
                        String path=filesListToBeRequested.get(i);
                        File newFile = new File(directory+"\\"+path.substring(path.lastIndexOf("/"),path.length()-1)+".zip");
                        FileOutputStream fos=new FileOutputStream(newFile);
                        while ((readData = downloadStream.read()) != -1) {
                            fos.write(readData);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //CODE TO UPDATE PROGRESS BARS
                                }
                            });
                        }
                        i++;
                    }
                    filesListToBeRequested=null;
                    this.commandProperty.setValue(CommsMessages.DOWNLOAD_COMPLETED); //CHECK FOR THIS VALUE IN THE changedEvent CALLBACK AND ROLLBACK THE CLIENT TO GET MORE FILES TO BE DOWNLOADER
                }
            }
        }
        catch(Exception e){
            return;
        }
    }
    public void setServerRequest(String serverRequest){
        this.serverRequest=serverRequest;
    }
    public void setSelectedFilesListToBeRequested(List<String> filesListToBeRequested){
        this.filesListToBeRequested=filesListToBeRequested;
    }
    public void setProgressBar(ArrayList<ProgressBar> pgb){
        pgbList=pgb;
    }
    public final String getServerRequest(){
        return this.serverRequest;
    }
    //THE PROPERTY GETTERS ARE USED FOR BINDING THE COMMS CLASS PROPERTIES AND APPLICATION CLASS PROPERTIES
    public BooleanProperty getAccessibleFilePathsRecievedProperty(){
        return this.accessibleFilePathsRecieved;
    }
    public StringProperty getCommandProperty() {
        return this.commandProperty;
    }
    public void checkAuthentication(String username,String password){
        //Perform the reading and writing once the UI thread is donw with all the processings
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                try {
                    String jsonCombo = "username:" + username + "password:" + password;
                    serverWriter.println(jsonCombo);
                    if (serverReader.readLine().equals(jsonCombo)) {
                        new Thread(Comms.this).start(); //MAKE THE THREAD ONLY IF THE CLIENT IS AUTHENTICATED
                    }
                }
                catch(IOException i){
                }
            }
        });
    }
}