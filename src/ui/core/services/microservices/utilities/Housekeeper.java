package ui.core.services.microservices.utilities;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import java.io.*;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ui.core.services.microservices.filehandling.customfile.CustomFile;
import ui.core.services.microservices.utilities.logger.Logger;

/*
*@author Pratik
*This class contains methods that are going to be used frequently.
* Has several functions
* */

public class Housekeeper implements HouseKeeperConstants{

    public static byte[] getCompressedBytes(String inputfilepath) throws IOException
    {



        File inputfileobj = new File(inputfilepath);

        File newFile=new File(HouseKeeperConstants.PATH_TO_ZIP+"/"+inputfileobj.getName()+".zip");
        if(inputfilepath.contains(".zip") )
            return Files.readAllBytes(Paths.get(inputfilepath));
        else if(newFile.exists())
            return Files.readAllBytes(Paths.get(newFile.getCanonicalPath()));
        if(!newFile.getParentFile().exists())
            newFile.getParentFile().mkdirs();
        byte output[] = {0};
        try {
            newFile.createNewFile();

                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(newFile));

                ZipEntry e = new ZipEntry(inputfilepath);
                out.putNextEntry(e);
                byte[] data = Files.readAllBytes(inputfileobj.toPath());
                out.write(data, 0, data.length);
                out.closeEntry();
                out.close();
                output = Files.readAllBytes(inputfileobj.toPath());
                //f.delete();

        }catch (IOException ioe) {
            Logger.wtf(ioe.toString());
        }

        return output;
    }
    public static void writeJob(final OutputStream os, final CustomFile file, final ProgressBar pgb)
    {


                try {
                    DataOutputStream dos = new DataOutputStream(os);
                    byte data[] = file.getContents();
                    int i[]=new int[1];

                    //sending file size.
                    System.out.println("file size "+file.getFileSize());
                    dos.writeUTF(""+file.getFileSize());
                    for (i[0]= 0; i[0] < data.length; i[0]++) {

                        dos.write(data[i[0]]);
                        /* running this method on the ui thread since it could lead to an illegal state exception*/
                        if(pgb!=null)
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    pgb.setProgress(i[0]*1.0);

                                }
                            });
                    }
                    dos=null;
                }
                catch (IOException e)
                {
                    Logger.wtf(e.toString());
                }
                System.out.println("Done sending");


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
interface HouseKeeperConstants{
    public static final String PATH_TO_ZIP=System.getProperty("user.dir")+"/StudyShare/zips";
}