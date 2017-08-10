import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import services.Authenticator;
import services.microservices.Comms;
import services.microservices.database.Group;
import services.microservices.filehandling.FileReaderRunnable;
import services.microservices.ThreadNotifier;
import services.microservices.filehandling.FileWatcher;
import services.microservices.filehandling.callback.FileWatcherCallback;
import services.microservices.threadpool.SequenceConvulsion;
import services.microservices.utilities.Housekeeper;
import services.microservices.utilities.logger.Logger;

public class Server implements ThreadNotifier,ServerConstants,FileWatcherCallback,SequenceConvulsion{
	private Group someGroup;
	private ArrayList<String> listOfFiles;
	private ServerSocket serverSocket;
	private Authenticator authenticator;

	public Server(String groupname){

		this.listOfFiles=new ArrayList<>();

		/* Logger will have to be inited from the UI ... doing it here temporarily*/
		Logger.initLogger();

		FileReaderRunnable.setSequenceConvulsion(this);


		try {
			this.serverSocket = new ServerSocket(ServerConstants.DEFUALT_PORT, ServerConstants.MAX_QUEUE_SIZE,ServerConstants.ACTUAL_IP);
		}catch (IOException io){
			System.out.println("exception - couldnt create socket\n"+io.toString());
			Logger.wtf(io.toString()+" from Server ctor");
			/* TODO Throw an exception here for the ui guys */
		}


		//add arraylist of filePaths
		this.someGroup=new Group(groupname,this);

		this.authenticator=new Authenticator();


	}



	public void kickStart(){
		System.out.println("in KICKSTART");
		try {
			FileReaderRunnable.printPool();

			System.out.println("YOUR IP "+ServerConstants.ACTUAL_IP);
			Logger.i("IP address "+ServerConstants.ACTUAL_IP);
			while (true) {

				Socket clientSocket = serverSocket.accept();
				System.out.println("Someone connected");
				this.authenticator.addClient(clientSocket);
			}

		}catch (IOException ioe){
			ioe.printStackTrace();
			Logger.wtf(ioe.toString()+" from kickStart in Server");
		}

	}

	public Authenticator getAuthenticator() {
		return this.authenticator;
	}

	public static void main(String ar[]){
		
	}


	/* running on main thread since the files need to be read before the server can start running*/
	private void setFileReaderRunnables(ArrayList<String> listOfFiles){
		FileReaderRunnable fileReaderRunnable=null;

		/* Whenever a new file is obtained the file pool will have to be reiinited*/

		/* start the progress bar here*/
		for(String str:listOfFiles){
			try {
				fileReaderRunnable = new FileReaderRunnable(str);
			}catch (FileNotFoundException fnfe){
				Logger.wtf(fnfe.toString()+" from setFileReaderRunnable in Server");

				/* TBD code to notify server of the absence of certain files */

			}
		}
		/* shutting down pool and preventing anymore FileReaderRunnables from spawning*/
		FileReaderRunnable.getThreadPool().shutdown();
		/* This method recreates the threadpool and assigns the current class as the reference to the sequence convulsion
		* This also allows me to add more FileReaderRunnables since shutting down an executor wont let you add more runnables to it.
		* */
		FileReaderRunnable.reinitThreadPool();



		/* stop the progress bar here*/
		/* start the server here.*/

	}




	private void setFileWatchers(){
		FileWatcher.setfileWatcherCallbackk(this);

		for(String str:this.someGroup.getListOfFolders()){
			FileWatcher fileWatcher=new FileWatcher(str);
		}
	}







	/*
	* implementing ThreadNotifier Methods
	*Will be called by the FileLister ( preExecute-Before it starts doing something , postExecute-Once it obtains the main list of files)
	* */
	@Override
	public void onPreExecute(String something){
		//code to be written for preexecute...
	}

	@Override
	public void onPostExecute(Object []allFiles){
		this.listOfFiles=(ArrayList<String>)allFiles[0];
		Comms.setTreeString(allFiles[1].toString());
		Comms.setFileListerList(this.listOfFiles);
		System.out.println("Tree string is "+allFiles[1].toString() );

		this.setFileWatchers();
		this.setFileReaderRunnables(this.listOfFiles);
	}

	/*
	* implementing FileWatcherCallback methods
	*
	*
	* */
	@Override
	public void onAdd(String toBeAdded){
		this.listOfFiles.add(toBeAdded);
		/* TODO add file to treeString */
		try {
			FileReaderRunnable newFile = new FileReaderRunnable(toBeAdded);
		}catch (FileNotFoundException fnfe){
			Logger.wtf(fnfe.toString()+" from FileWatcherCallback in Server");
		}
	}
	@Override
	public void onRemove(String toBeRemoved){
		this.listOfFiles.remove(toBeRemoved);
		FileReaderRunnable.getPool().remove(toBeRemoved);

		/* TODO remove fileName from treeString*/

	}






}
interface ServerConstants{
	public final static int DEFUALT_PORT=44444;
	//could lead to a problem if the ip changes... ie if the user disconnects and then reconnects the user might be given a new IP(DHCP).
	public final static InetAddress ACTUAL_IP= Housekeeper.getIpAddress();
	public final static int MAX_QUEUE_SIZE=50;
}
