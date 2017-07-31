package services.microservices.filehandling;
import services.microservices.filehandling.customfile.CustomFile;

import java.io.*;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.*;
public class Zipper {
    public static byte[] send(ArrayList<CustomFile> inputFiles, OutputStream clientOutputStream) throws IOException
    {
        File f = new File("d:\\"+System.currentTimeMillis()+".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        inputFiles.trimToSize();
        for(int i=0;i<inputFiles.size();i++) {
            ZipEntry e = new ZipEntry(inputFiles.get(i).getFileName());
            out.putNextEntry(e);
            byte[] data = inputFiles.get(i).getContents();
            out.write(data, 0, data.length);
        }
        out.closeEntry();
        out.close();
        byte output[]=Files.readAllBytes(f.toPath());
        f.delete();
        return output;
    }
}
