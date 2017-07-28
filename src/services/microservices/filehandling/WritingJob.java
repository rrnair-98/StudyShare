package StudyShare.src.services.microservices.filehandling;
import java.nio.file.Files;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class WritingJob
{
	User user;
	File file;
	DataOutputStream dos;
	WritingJob(File file,User user)
	{
		this.user=user;
		this.file=file;		
	}
	public void writeToUser() throws IOException
	{
		dos=user.getOutputStream();
		int progressInc=0;
		byte data[]=File.readAllBytes(file.toPath());
		progressInc=0;
		for(byte byt:data)
		{
			dos.write(byt);
			progressInc++;
			user.updateProgress(progressInc,data.length);
		}
		dos.close();
	}
}