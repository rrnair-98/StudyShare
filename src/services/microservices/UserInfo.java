package services.microservices;


import services.microservices.utilities.logger.Logger;

import java.io.InputStream;
import java.io.OutputStream;
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
    private OutputStream outputStream;
    private InputStream inputStream;
    private ArrayList<String> filesUsed;

    public UserInfo(final String username,final long joinTime,final InputStream inputStream,final OutputStream outputStream){
        this.username=username;
        this.joinTime=joinTime;
        this.outputStream=outputStream;
        this.inputStream=inputStream;
        this.filesUsed=new ArrayList<>();
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
    public void setFilesUsed(ArrayList<String> filesUsed){
        this.filesUsed.addAll(filesUsed);
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }


    @Override
    public String toString(){
        return "{ user:\""+this.username+"\",joinTime:\""+this.joinTime+"\",leaveTime:\""+this.leaveTime+"\",filesUsed:\""+this.filesUsed.toString()+"\" }";
    }



}