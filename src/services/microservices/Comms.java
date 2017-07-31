package services.microservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/*
* @author Rohan
* This is the class which will actually send the file to the user.
* It essentially performs these functions
*   1.opens a stream to the user and sends Files.
*   2.executes postexecute if not null
* */

public class Comms implements Runnable{
    //if isServer then  first write later read and then send else write first readLater;
    private UserInfo userInfo;
    private PostExecute postExecute;
    //private boolean isServer;
    public Comms(final UserInfo userInfo,final PostExecute postExecute){
            this.userInfo=userInfo;
            this.postExecute=postExecute;
    }


    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public void run(){
        PrintWriter printWriter=null;
        BufferedReader bufferedReader=null;
        try {
            printWriter=new PrintWriter(this.userInfo.getOutputStream(),true);
            bufferedReader=new BufferedReader(new InputStreamReader(this.userInfo.getInputStream()));

            printWriter.println("{message: \""+this.userInfo.getUsername()+"sendin requested files\"}");

            String s;
            while((s=bufferedReader.readLine())!=null && s.contains("giveMeSomeFiles")){
                /*TBD send the arraylist of files to the user. Will be obtained from fileRunnables*/
            }
            if(postExecute!=null)
                postExecute.onPostExecute();



        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
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
