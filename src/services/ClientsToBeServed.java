/* To be used by SERVER ONLY. Will be placed in authenticated Que.*/
package services;
import javafx.concurrent.Task;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import services.microservices.Comms;
import services.microservices.UserInfo;
import services.microservices.database.DatabaseHelper;

/*@author: Rohan
This class will be created for every user that hits the server. It does the following things
*  1.Authenticates the user.
*  2.Provides information about the user.(username and jointime)
*  3.Maintains a list of accessibleFiles.
*  4.Will communicate with the user once the user has been authenticated.
*
* */
public class ClientsToBeServed implements Runnable{
	//acessibleFilePaths will be obtained from the authenticator thread.
	private ArrayList <String>accessibleFilePaths;

	private AuthenticatorQueMgr authenticatorQueMgr=null;
	/*private static DatabaseHandler databaseHandler;
	static{
		ClientsToBeServed.databaseHandler=new DatabaseHandler();
	}*/
	private Socket socket;

	//will be set whenever run is executed
	private UserInfo userInfo;
	private boolean isAuthenticated;

	public ClientsToBeServed(final Socket sock,final AuthenticatorQueMgr authenticatorQueMgr){

		this.socket=sock;

		this.authenticatorQueMgr=authenticatorQueMgr;

	}


	public ArrayList<String> getAccessibleFiles(){
		return this.accessibleFilePaths;
	}

	public OutputStream getOutputStream()throws IOException{
		return this.socket.getOutputStream();
	}
	public InputStream getInputStream()throws IOException{
		return this.socket.getInputStream();
	}

	public void setUserInfo(UserInfo user){
		this.userInfo=user;
	}

	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	private void setIsAuthenticated(boolean b){
		this.isAuthenticated=b;
	}


	public boolean isAuthenticated(){return this.isAuthenticated;}

	@Override
	public void run(){
     /* TBD open streams and check the db for valid username password combo.
     * Set required vars (isAuth and userInfo)thereafter
     * */
		PrintWriter printWriter=null;BufferedReader bufferedReader=null;
		try{
			printWriter=new PrintWriter(this.getOutputStream());
			bufferedReader=new BufferedReader(new InputStreamReader(this.getInputStream()));

			String s;
			String arr[]=new String[2];
			while((s= bufferedReader.readLine())!=null){
				if(s.contains("username") && s.contains("password")){
					//extracting username and password
              /* The string is expected to be sent as a json string ie
              {
                 username: "helloWorld",
                 password: "somePassword"

              }*/
					Pattern pattern= Pattern.compile("\"(.*?)\"");
					Matcher matcher= pattern.matcher(s);
					int m=0;
					try {
						while (matcher.find())
							arr[m++] = matcher.group(1);

						//username and password extracted succesfully
						if(DatabaseHelper.verifyUser(arr[0],Long.parseLong(arr[1]))) {
							ClientsToBeServed.this.setUserInfo(new UserInfo(arr[0], System.currentTimeMillis(),this.getInputStream(),this.getOutputStream()));
							ClientsToBeServed.this.isAuthenticated=true;
							//TBD code to place in Authenticated queue ie place a CommsObject... Done by authenticator?adding an interface?
							this.authenticatorQueMgr.addToAuthenticator(new Comms(this.userInfo,null),this);

							printWriter.println(s);

							return;
						}

						printWriter.println("couldnt be authenticated");

					}catch (ArrayIndexOutOfBoundsException arrayIndex){
						//thrown only when a unformatted string is received.
						arrayIndex.printStackTrace();
					}
				}
			}




		}catch (IOException ioe){
			ioe.printStackTrace();
		}finally {
        /* Always executed gc :)*/
			try{
				if(printWriter!=null)
					printWriter.close();
				if(bufferedReader!=null)
					bufferedReader.close();
			}catch (IOException ioe){
				ioe.printStackTrace();
			}
		}

	}






}
