package services.microservices;

import services.microservices.filehandling.customfile.CustomFilePool;
import services.microservices.filehandling.customfile.CustomFile;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.concurrent.Task;
import java.nio.file.Files;
import java.nio.file.Paths;
/*
* @author Rohan
* This class does the following:
*	1.Schedules files to be read
*	2.Stores them onto a pool
*	3.Override the interface which updates a ProgressBar
* */
public class FileReaderRunnables extends Task<Boolean>{
	private static CustomFilePool pool;
	private String filePath;
	static{
		FileReaderRunnables.pool = new CustomFilePool(); 
	}

	public FileReaderRunnables(String filePath)throws FileNotFoundException{
		File file=new File(filePath);
		if(!file.exists())
			throw new FileNotFoundException();
		this.filePath=filePath;


	}



	public static CustomFilePool getPool(){
		return FileReaderRunnables.pool;
	}

	@Override
	public Boolean call(){

		try{

				byte fileBytes[]=Files.readAllBytes(Paths.get(this.filePath));

				FileReaderRunnables.pool.add(this.filePath,new CustomFile(this.filePath,fileBytes));
				return true;

		}catch (Exception exception){
			exception.printStackTrace();
		}


		return false;
	}


}
