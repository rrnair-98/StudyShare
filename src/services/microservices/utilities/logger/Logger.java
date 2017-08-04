package services.microservices.utilities.logger;

import sun.security.provider.MD5;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/*
*@author rohan
*This class is a logger. Will write to a local file.
*To be used only by the server.
*The function init must be called at least once before the file starts executing.
*Class provides 3 static functions
*   1.d
*       for writing debug messages
*   2.wtf
*       for writing exceptions or failures
*   3.i
*       for writing informative messages(usually about the user)
* */
public class Logger implements LoggerDefaults{
    private static PrintWriter printWriter[]=new PrintWriter[3];
    private Logger(){

    }



    public static void initLogger(){
        String temp=new Date().toString();
        String arr[]={LoggerDefaults.DEFAULT_I,LoggerDefaults.DEFAULT_WTF,LoggerDefaults.DEFAULT_DEBUG};
        for(int i=0;i<3;i++){
            File f=new File("StudyShare/logs/"+arr[i]+temp+".log");
            if(!f.exists())
                try {
                    f.createNewFile();
                    Logger.printWriter[i]=new PrintWriter(new FileOutputStream(f),true);//append true
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
        }

    }

    private static void write(String stringToBeWritten,int whichWriter){
        Logger.printWriter[whichWriter].println(System.currentTimeMillis()+":\t"+stringToBeWritten);

    }

    /* calls for writing debug messages*/
    public static void d(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_DEBUG+stringToBeWritten,LoggerDefaults.DEBUG_INDEX);
    }

    /* call for writing informative messagesGenerally about the user.*/
    public static void i(String toBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_I+toBeWritten,LoggerDefaults.INFO_INDEX);
    }
    /*calls for writing a terrible failure message or exceptions
    * wtf=whatATerrribleFailure :)
    * */
    public static void wtf(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_WTF+stringToBeWritten,LoggerDefaults.WTF_INDEX);
    }







}
interface LoggerDefaults{
    public static final String DEFAULT_DEBUG="DEBUG:\t";
    public static final String DEFAULT_WTF="WTF:\t";
    public static final String DEFAULT_I="INFO:\t";
    public static final int DEBUG_INDEX=0, INFO_INDEX=1,WTF_INDEX=2;
}
