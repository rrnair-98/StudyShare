package services.microservices.utilities;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
/*import org.json.JSONException;
import org.json.JSONObject;*/

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.utilities.logger.Logger;

/*
@author Rohan,Pratik
Manages the small utility tasks such as getting compressed bytes of a file and low level sending jobs to some end
*/
public class Housekeeper{

    public static byte[] getCompressedBytes(String inputfilepath)
    {
        String fileName = inputfilepath.substring(inputfilepath.lastIndexOf("//"+2),inputfilepath.length());
        File f = new File("d:\\" + fileName + ".zip");
        byte output[] = {0};
        try {

            if (f.exists() == false) {
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
        }
        catch (IOException ie)
        {
            Logger.wtf(ie.toString());
        }
        return output;
    }
    public static void writeJob(OutputStream os, CustomFile file, ProgressBar pgb)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream dos = new DataOutputStream(os);
                    byte data[] = file.getContents();
                    for (int i = 0; i < data.length; i++) {

                        dos.write(data[i]);
                        pgb.setProgress(i);
                    }
                    dos.close();
                }
                catch (IOException e)
                {
                    Logger.wtf(e.toString());
                }
            }
        });
    }
}