package StudyShare.src.services.microservices.filehandling;

import java.nio.Files;
import java.nio.Paths;
import StudyShare.src.services.microservices.filehandling.CustomFilePool;

public class CustomFile {
	private String path;
	private byte[] fileContents;
	private static CustomFilePool customFilePool;
	/* Sharing an instance of the file pool to prevent the main thread from being blocked*/
	static{
		CustomFile.customFilePool=new CustomFilePool();
	}

	public CustomFile(String path){
		this.path=path;
		this.readFile();
	}
	/*Reads the file in a thread and adds it to the file pool */
	private void readFile(){
		new Thread(new Runnable(){
			
			@Override
			public void run(){
				CustomFile.this.fileContents=Files.readAllBytes(Paths.get(this.path));
				CustomFile.customFilePool.add(CustomFile.this);
			}
		}).start();
	}

	public byte[] getContents(){
		return this.fileContents;
	}

	public CustomFilePool getPool(){
		return CustomFile.customFilePool;
	}
	


}