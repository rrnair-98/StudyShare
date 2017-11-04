package ui.core;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import ui.core.interfaces.*;
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
APPLICATIONS'S ACCESSFILESRECIEVED PROPERTY -----------BIDIRECTIONALLY BOUND WITH--------COMMS'S ACCESSFILESRECIEVED
 */
public class Comms /*implements Runnable*/ {

    private volatile String serverRequest;
    private StringProperty commandProperty;
    private OutputStream serverWriterStream;
    private PrintWriter serverWriter;
    private boolean readyToRead;
    private InputStream serverReaderStream;
    private BufferedReader serverReader;
    private BooleanProperty accessibleFilePathsRecieved;
    private List<String> filesListToBeRequested;
    private DataInputStream downloadStream;
    private ProgressBar pgb;
    private boolean authenticationStatus;
    static boolean downloadStatus = false;

    /*private Comms(OutputStream clientOutputStream, InputStream clientInputStream) {
        serverRequest = "";
        this.commandProperty = new SimpleStringProperty("none");
        this.serverWriterStream = clientOutputStream;
        this.serverReaderStream = clientInputStream;
        this.readyToRead = false;
        this.accessibleFilePathsRecieved = new SimpleBooleanProperty(false);
        this.serverWriter = new PrintWriter(serverWriterStream, true);
        this.serverReader = new BufferedReader(new InputStreamReader(serverReaderStream));
    }*/
    public Comms(InetAddress serverIp) throws IOException
    {
        Socket user=new Socket(serverIp,44444);
        serverRequest = "";
        this.commandProperty = new SimpleStringProperty("none");
        this.serverWriterStream = user.getOutputStream();
        this.serverReaderStream = user.getInputStream();
        this.readyToRead = false;
        this.accessibleFilePathsRecieved = new SimpleBooleanProperty(false);
        this.serverWriter = new PrintWriter(serverWriterStream, true);
        this.serverReader = new BufferedReader(new InputStreamReader(serverReaderStream));
    }
    public void getOptions(String serverResponse) {
        try {
            //serverWriter.println(ServerRequestConstants.LIST_REQUEST);
            System.out.println(serverResponse + "\nEnter the String to request");
            String option=(new BufferedReader(new InputStreamReader(System.in))).readLine();
            ArrayList l1=new ArrayList();
            l1.add(option);
            this.setSelectedFilesListToBeRequested(l1);
            this.setServerRequest(ServerRequestConstants.SENDING_LIST);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //@Override
    public void run() {
        System.out.println("Thread made");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //System.out.println(serverRequest);
                        if (Comms.this.serverRequest.equals("") == false) {
                            Comms.this.serverWriter.println(Comms.this.serverRequest); // FOR PROPER EXECUTION CALL THE setserverRequest() with the proper ServerRequestConstants
                            System.out.println(serverRequest);
                            Comms.this.readyToRead = true;//Once the request is performed the code is ready to run the sub loop is ready to read the server's response
                            while (Comms.this.readyToRead) {
                                System.out.println(serverRequest + "123");
                                String serverResponse;
                                if (Comms.this.serverRequest.equals(ServerRequestConstants.LIST_REQUEST)) {
                                    serverResponse = Comms.this.serverReader.readLine();//if the server response is not null and the call to setserverRequest() was done with LIST_REQUEST
                                    System.out.println("Here");
                                    //Comms.this.getOptions(serverResponse);
                                    Comms.this.commandProperty.setValue(serverResponse);// Set the command property to the string tree returned as the server response, this triggers the change event callback in the application class
                                    Comms.this.accessibleFilePathsRecieved.setValue(true);// This instruction sets the bound accessibleFilePathsRevieved value to true, the boolean property needs to be checked if is true make the tree with the value of the command Property of the application class
                                } else if (Comms.this.serverRequest.equals(ServerRequestConstants.SENDING_LIST)) { //if the string to be written is SENDING_LIST the list object is sent to the user by object output stream
                                    System.out.println("Sending List");
                                    ObjectOutputStream filesListWriter = new ObjectOutputStream(Comms.this.serverWriterStream);
                                    filesListWriter.writeObject(Comms.this.filesListToBeRequested);
                                    filesListWriter.flush();
                                    Comms.this.commandProperty.setValue(CommsMessages.START_DOWNLOADING);// this value setting needs to be checked in the change event call back to generate the downloading, BUT BEFORE MAKING THE DOWNLOAD SCENE INITIALIZE THE PROGRESS BAR LISTS
                                }


                                Comms.this.readyToRead = false;
                            }
                            serverRequest = "";
                        } else if (Comms.this.commandProperty.getValue().equals(CommsMessages.START_DOWNLOADING)) {
                            File directory = new File(System.getProperty("user.dir") + "/StudyShareDownloads");
                            if (!directory.exists())
                                directory.mkdir();
                            Comms.this.downloadStream = new DataInputStream(serverReaderStream);
                            //int readData;
                            int i = 0;
                            //int lengthOfEachFile = 100 / filesListToBeRequested.size();
                            //while (i < filesListToBeRequested.size()) {
                            String path = filesListToBeRequested.get(i++);
                            System.out.println(path);
                            File newFile = new File(directory + File.separator + path.substring(path.lastIndexOf(File.separator), path.length()) + ".zip");
                            try {
                                FileOutputStream fos = new FileOutputStream(newFile);
                                DataOutputStream dos = new DataOutputStream(fos);
                                int perclength = Integer.parseInt(downloadStream.readUTF());
                                System.out.println(perclength);
                                int t[] = new int[1];
                                t[0] = 0;
                                System.out.println("Starting to download");

                                for (t[0] = 0; t[0] < perclength; t[0]++) {
                                    byte bytes = downloadStream.readByte();
                                    dos.write(bytes);
                                       /* Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                //CODE TO UPDATE PROGRESS BAR
                                                //pgb.setProgress(((t[0]++) / lengthOfEachFile) * 100);
                                            }
                                        });*/
                                }
                                dos.close();
                                fos.close();

                                System.out.println("Hellooooooo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            filesListToBeRequested = null;
                            Comms.this.commandProperty.setValue(CommsMessages.DOWNLOAD_COMPLETED); //CHECK FOR THIS VALUE IN THE changedEvent CALLBACK AND ROLLBACK THE CLIENT TO GET MORE FILES TO BE DOWNLOADE
                            downloadStatus = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void setServerRequest(String serverRequest) {
        System.out.println("Hello");
        this.serverRequest = serverRequest;
    }

    public void setSelectedFilesListToBeRequested(List<String> filesListToBeRequested) {
        this.filesListToBeRequested = filesListToBeRequested;
    }

    /*public void setProgressBar(ProgressBar pgb) {
        this.pgb = pgb;
    }*/

    public final String getServerRequest() {
        return this.serverRequest;
    }

    //THE PROPERTY GETTERS ARE USED FOR BINDING THE COMMS CLASS PROPERTIES AND APPLICATION CLASS PROPERTIES
    public BooleanProperty getAccessibleFilePathsRecievedProperty() {
        return this.accessibleFilePathsRecieved;
    }

    public StringProperty getCommandProperty() {
        return this.commandProperty;
    }

    public static Long getMd5(String strToBeHashed){
        try {
            byte []byteOfMsg=strToBeHashed.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte p[]=md.digest(byteOfMsg);

            return new BigInteger(p).longValue();
        }catch (NoSuchAlgorithmException ns){
            //Logger.wtf(ns.toString());
        }
        return null;
    }
    public boolean checkAuthentication(String username, String password) {
        if (!authenticationStatus) {
            try {
                Long hash=Comms.getMd5(password);
                String jsonCombo = "username:\"" + username + "\" , password:\"" + hash+ "\"";
                serverWriter.println(jsonCombo);
                System.out.println("Reading");
                String temp = serverReader.readLine();
                //System.out.println(temp);
                if (temp.equals("verified")) {
                    System.out.println(temp);
                    //System.out.println(serverReader.readLine());
                    Comms.this.run();//MAKE THE THREAD ONLY IF THE CLIENT IS AUTHENTICATE
                    authenticationStatus = true;
                    return true;
                }
            } catch (IOException i) {
            }
            authenticationStatus = false;
            return false;
        }
        return true;
    }
}