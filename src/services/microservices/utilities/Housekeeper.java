package services.microservices.utilities;

import javafx.scene.control.ProgressBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.utilities.logger.Logger;

<<<<<<< Updated upstream
/*
*@author Pratik
*This class contains methods that are going to be used frequently.
* Has several functions
* */
=======
>>>>>>> Stashed changes
public class Housekeeper{

    public static byte[] getCompressedBytes(String inputfilepath) throws IOException
    {
        String fileName = inputfilepath.substring(inputfilepath.length() - 1, inputfilepath.lastIndexOf("//"));
        File f = new File("d:\\" + fileName + ".zip");
<<<<<<< Updated upstream
        byte output[] = {0};
        try {

            if (!f.exists()) {
                File inputfileobj = new File(inputfilepath);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
                ZipEntry e = new ZipEntry(fileName);
                out.putNextEntry(e);
                byte[] data = Files.readAllBytes(inputfileobj.toPath());
                out.write(data, 0, data.length);
                out.closeEntry();
                out.close();
                output = Files.readAllBytes(f.toPath());
                //f.delete();
            } else {
                output = Files.readAllBytes(f.toPath());
            }
=======
        byte output[]={0};
        if(f.exists()==false) {
            File inputfileobj=new File(inputfilepath);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry e = new ZipEntry(fileName);
            out.putNextEntry(e);
            byte[] data = Files.readAllBytes(inputfileobj.toPath());
            out.write(data,0, data.length);
            out.closeEntry();
            out.close();
             output= Files.readAllBytes(f.toPath());
            //f.delete();
>>>>>>> Stashed changes
        }
        else
        {
            output=Files.readAllBytes(f.toPath());
        }
        return output;
    }
<<<<<<< Updated upstream
    public static void writeJob(final OutputStream os, final CustomFile file, final ProgressBar pgb)
    {


                try {
                    DataOutputStream dos = new DataOutputStream(os);
                    byte data[] = file.getContents();
                    int i[]=new int[1];

                    for (i[0]= 0; i[0] < data.length; i[0]++) {

                        dos.write(data[i[0]]);
                        /* running this method on the ui thread since it could lead to an illegal state exception*/
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                pgb.setProgress(i[0]*1.0);

                            }
                        });
                    }
                    dos.close();
                }
                catch (IOException e)
                {
                    Logger.wtf(e.toString());
                }


    }





    /*
    * @author Rohan
    * This function returns the ip address of the machine(provided its not a loopback).
    * If it is the localhost it returns null;
    * */

    public static InetAddress getIpAddress(){

        try{

            Enumeration<NetworkInterface> networkInterface=NetworkInterface.getNetworkInterfaces();
            while(networkInterface.hasMoreElements()){

                    NetworkInterface current=networkInterface.nextElement();
                    Enumeration<InetAddress> addresses=current.getInetAddresses();
                    while(addresses.hasMoreElements()){
                        InetAddress currentInterfaceIp=addresses.nextElement();
                        if(!currentInterfaceIp.isLinkLocalAddress() && !currentInterfaceIp.isLoopbackAddress() && currentInterfaceIp instanceof Inet4Address)
                            return currentInterfaceIp;
                    }

            }

        }catch (SocketException so){
            Logger.wtf(so.toString());
        }

        return null;


    }


    /*
    * @author Rohan
    * this function returns the md5 of a string object.
    * To be used while db insertion/searching etc
    * */

    public static Long getMd5(String strToBeHashed){
        try {
            byte []byteOfMsg=strToBeHashed.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte p[]=md.digest(byteOfMsg);

            return new BigInteger(p).longValue();
        }catch (NoSuchAlgorithmException ns){
            Logger.wtf(ns.toString());
        }
        return null;
    }



}
=======
    public static void writeJob(OutputStream os, CustomFile file, ProgressBar pgb) throws IOException
    {
        DataOutputStream dos=new DataOutputStream(os);
        byte data[]=file.getContents();
        for(int i=0;i<data.length;i++)
        {
            dos.write(data[i]);
            pgb.setProgress(i);
        }
        dos.close();
    }
}
>>>>>>> Stashed changes
