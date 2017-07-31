package services.microservices.utilities.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/*
*@author rohan
*This class is a logger. Will write to a local file.
*To be used only by the server.
*The function init must be called at least once before the file starts executing.
*Class provides 2 static functions
*   1.d
*       for writing debug messages
*   2.wtf
*       for writing exceptions or failures
* */
public class Logger implements LoggerDefaults{
    private static PrintWriter printWriter;
    private Logger(){

    }


    public static void initLogger(final String logFileName){
        File file=new File("Studyshare/logs/"+new Date().toString()+".log");
        try {
            if (!file.exists())
                file.createNewFile();
            Logger.printWriter = new PrintWriter(new FileOutputStream(file), true);//append true

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private static void write(String stringToBeWritten){
        Logger.printWriter.println(System.currentTimeMillis()+":\t"+stringToBeWritten);

    }

    /* calls for writing debug messages*/
    public static void d(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_DEBUG+stringToBeWritten);
    }

    public static void i(String toBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_I+toBeWritten);
    }
    /*calls for writing a terrible failure message or exceptions
    * wtf=whatATerrribleFailure :)
    * */
    public static void wtf(String stringToBeWritten){
        Logger.write(LoggerDefaults.DEFAULT_WTF+stringToBeWritten);
    }




}
interface LoggerDefaults{
    public static final String DEFAULT_DEBUG="DEBUG:\t";
    public static final String DEFAULT_WTF="WTF:\t";
    public static final String DEFAULT_I="INFO:\t";
}
