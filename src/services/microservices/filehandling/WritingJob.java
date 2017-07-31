package services.microservices.filehandling;
import java.nio.file.Files;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.ProgressBar;
//import src.services.microservices.ClientsToBeServed;
/*
Does all the low level writing jobs and implements ProgressBarUpdater interface 

	@author Pratik Tiwari
	
*/
public class WritingJob implements ProgressBarUpdater
{
	File file;
	DataOutputStream dos;
	ProgressBar pgb;
	WritingJob(File file,OutputStream dos)
	{
		this.dos=new DataOutputStream(dos);
		this.file=file;		
	}
	public void setProgress(ProgressBar pgb)
	{
		this.pgb=pgb;
	}
	public void writeJob() throws IOException
	{
		byte data[]=Files.readAllBytes(file.toPath());
		for(int i=0;i<data.length;i++)
		{
			dos.write(data[i]);
			pgb.setProgress(i);
		}
		dos.close();
	}
}