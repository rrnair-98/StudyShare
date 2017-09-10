package ui.core.services.microservices.utilities.logger;

import sun.security.provider.MD5;

import javax.swing.text.DateFormatter;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private static boolean isInited=false;
    private Logger(){

    }



     synchronized public static void initLogger() {
        if(Logger.isInited)
            throw new IllegalStateException("Logger has already been inited");
        Logger.isInited=true;
        System.out.println("logger inited");
        String date = getDate();

        String arr[] = {LoggerDefaults.DEFAULT_DEBUG,LoggerDefaults.DEFAULT_I, LoggerDefaults.DEFAULT_WTF} ;
        File file = new File(System.getProperty("user.dir") + "/StudyShare/logs/");
        if(!file.exists());
            file.mkdirs();
        for (int i = 0; i < 3; i++) {
            Logger.isInited=false;
            try {
                System.out.println(file.getCanonicalPath());
                System.out.println(file.getCanonicalPath() + "/" + (arr[i]) + date + ".log");
                File f = new File(file.getCanonicalPath() + "/" + (arr[i]) + date + ".log");
                if (!f.exists()) {
                    try {
                        if(!f.getParentFile().exists())
                            f.getParentFile().mkdirs();
                        f.createNewFile();

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                Logger.printWriter[i] = new PrintWriter(new FileWriter(f,true), true);//append true

            } catch (IOException io) {
                io.printStackTrace();
            }

        }
        Logger.isInited=true;
    }


    private static String getDate(){
        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.now();
        return dtf.format(localDate);
    }

    private static void write(String stringToBeWritten,int whichWriter){
        Logger.printWriter[whichWriter].println(System.currentTimeMillis()+":\t"+stringToBeWritten);

    }

    /* calls for writing debug messages*/
    public static void d(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_DEBUG+"\t"+stringToBeWritten,LoggerDefaults.DEBUG_INDEX);
    }

    /* call for writing informative messagesGenerally about the user.*/
    public static void i(String toBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_I+"\t"+toBeWritten,LoggerDefaults.INFO_INDEX);
    }
    /*calls for writing a terrible failure message or exceptions
    * wtf=whatATerrribleFailure :)
    * */
    public static void wtf(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_WTF+"\t"+stringToBeWritten,LoggerDefaults.WTF_INDEX);
    }







}
interface LoggerDefaults{
    public static final String DEFAULT_DEBUG="DEBUG";
    public static final String DEFAULT_WTF="WTF";
    public static final String DEFAULT_I="INFO";
    public static final int DEBUG_INDEX=0, INFO_INDEX=1,WTF_INDEX=2;
}
