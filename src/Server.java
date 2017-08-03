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
import services.microservices.database.filelister.FilesLister;
import services.microservices.ThreadNotifier;
import services.microservices.filehandling.FileWatcher;
import services.microservices.filehandling.callback.ArrayListCallback;
import services.microservices.utilities.Housekeeper;
import services.microservices.utilities.logger.Logger;

public class Server implements ThreadNotifier,ServerConstants,ArrayListCallback{
	private Group someGroup;
	private ArrayList<String> listOfFiles;
	private ServerSocket serverSocket;
	private Authenticator authenticator;

	public Server(String groupname){

		this.listOfFiles=new ArrayList<>();
		//add arraylist of filePaths
		this.someGroup=new Group(groupname,this);

		try {
			this.serverSocket = new ServerSocket(ServerConstants.DEFUALT_PORT, ServerConstants.MAX_QUEUE_SIZE,ServerConstants.ACTUAL_IP);
		}catch (IOException io){
			System.out.println("exception - couldnt create socket\n"+io.toString());
			Logger.wtf(io.toString());
		}

		this.authenticator=new Authenticator();


	}

	public void startServer(){
		try {

			while (true) {

				Socket clientSocket = serverSocket.accept();
				this.authenticator.addClient(clientSocket);
			}

		}catch (IOException ioe){
			ioe.printStackTrace();
			Logger.wtf(ioe.toString());
		}

	}

	public Authenticator getAuthenticator() {
		return this.authenticator;
	}

	public static void main(String ar[]){
		
	}


	/* running on main thread since the files need to be read before the server can start running*/
	private void setFileReaderRunnables(){

		/* start the progress bar here*/

		for(String str:this.listOfFiles){
			try {
				FileReaderRunnable fileReaderRunnable = new FileReaderRunnable(str);
			}catch (FileNotFoundException fnfe){
				Logger.wtf(fnfe.toString());

				/* TBD code to notify server of the absence of certain files */

			}
		}

		/* stop the progress bar here*/


	}




	private void setFileWatchers(){
		FileWatcher.setArrayListCallback(this);

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

		this.setFileWatchers();
	}

	/*
	* implementing ArrayListCallback methods
	*
	*
	* */
	@Override
	public void onAdd(String toBeAdded){
		this.listOfFiles.add(toBeAdded);
	}
	@Override
	public void onRemove(String toBeRemoved){
		this.listOfFiles.remove(toBeRemoved);
	}



}
interface ServerConstants{
	public final static int DEFUALT_PORT=4444;
	//could lead to a problem if the ip changes... ie if the user disconnects and then reconnects the user might be given a new IP(DHCP).
	public final static InetAddress ACTUAL_IP= Housekeeper.getIpAddress();
	public final static int MAX_QUEUE_SIZE=50;
}
