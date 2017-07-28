/* To be used by SERVER ONLY. Will be placed in authenticated Que.*/
package src.services.microservices;
import java.util.ArrayList;
import java.net.Socket;

public class ClientsToBeServed{
	//acessibleFilePaths will be obtained from the authenticator thread.
	private ArrayList <String>accesibleFilePaths;

	private Socket socket;	
	public ClientsToBeServed(final Socket sock,final ArrayList accessible){

		this.socket=sock;
		this.accesibleFilePaths=accessible;

	}


	public ArrayList<String> getAccessibleFiles(){
		return this.accesibleFilePaths;
	}

	public OutputStream getOutputStream(){
		return this.socket.getOutputStream();
	}
	public InputStream getInputStream(){
		return this.socket.getInputStream();
	}

	



}