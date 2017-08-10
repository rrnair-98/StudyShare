package services.microservices;


import services.microservices.utilities.logger.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;

/*
* @author Rohan
* This class just holds certain readonly properties.
*
* */
public class UserInfo {
    private String username;
    private long joinTime,leaveTime;
    private Socket socket;
    private ArrayList<String> filesUsed;
    private boolean isClosed=false;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public UserInfo(final String username,final long joinTime,final Socket socket,final PrintWriter printWriter,final BufferedReader bufferedReader){
        this.username=username;
        this.joinTime=joinTime;
        this.socket=socket;
        this.filesUsed=new ArrayList<>();
        this.printWriter=printWriter;
        this.bufferedReader=bufferedReader;
    }

    public void setLeaveTime(Long systemTimeInMillis){
        this.leaveTime=systemTimeInMillis;
        //tbd call logger
        Logger.i(this.toString());
    }

    public String getUsername(){return this.username;}
    public long getJoinTime(){return this.joinTime;}

    public ArrayList<String> getFilesUsed() {
        return this.filesUsed;
    }
    public void addToFilesUsed(ArrayList<String> filesUsed){
        this.filesUsed.addAll(filesUsed);
    }

    public OutputStream getOutputStream()throws IOException {
        return this.socket.getOutputStream();
    }

    public boolean getIsClosed(){
        return this.isClosed;
    }

    public InputStream getInputStream() throws IOException{
        return this.socket.getInputStream();
    }


    public void close() throws IOException{
        System.out.println("CLOSING");
        this.isClosed=true;
        this.socket.close();
        this.setLeaveTime(System.currentTimeMillis());
    }

    public PrintWriter getPrintWriter() {
        return this.printWriter;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }

    @Override
    public String toString(){
        return "{ user:\""+this.username+"\",joinTime:\""+this.joinTime+"\",leaveTime:\""+this.leaveTime+"\",filesUsed:\""+this.filesUsed.toString()+"\" }";
    }



}