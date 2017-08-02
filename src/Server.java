import java.net.InetAddress;
import java.util.ArrayList;

import services.microservices.Comms;
import services.microservices.filehandling.FileReaderRunnable;
import services.microservices.FilesLister;
import services.microservices.ThreadNotifier;
import services.microservices.utilities.Housekeeper;

public class Server implements ThreadNotifier,ServerConstants{
	private FilesLister filesLister;
	private ArrayList<String> listOfFiles;

	public Server(ArrayList<String> resultSetOfDatabase){

		this.listOfFiles=new ArrayList<>();
		//add arraylist of filePaths
		this.filesLister=new FilesLister(resultSetOfDatabase,this);

	}



	@Override
	public void onPreExecute(String something){
		//code to be written for preexecute...
	}

	@Override
	public void onPostExecute(Object []allFiles){
		this.listOfFiles=(ArrayList<String>)allFiles[0];
		FileReaderRunnable.setFilesToBeRead(this.listOfFiles);
		Comms.setTreeString(allFiles[1].toString());
		Comms.setFileListerList(this.listOfFiles);
	}



	public static void main(String ar[]){
		
	}

}
interface ServerConstants{
	public final static int DEFUALT_PORT=4444;
	//could lead to a problem if the ip changes... ie if the user disconnects and then reconnects the user might be given a new IP(DHCP).
	public final static InetAddress ACTUAL_IP= Housekeeper.getIpAddress();
}
