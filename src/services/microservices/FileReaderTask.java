package services.microservices;

import services.microservices.filehandling.customfile.CustomFilePool;
import services.microservices.filehandling.customfile.CustomFile;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.concurrent.Task;
import services.microservices.utilities.logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/*
* @author Rohan
* This class does the following:
*	1.Schedules files to be read
*	2.Stores them onto a pool
*	3.Override the interface which updates a ProgressBar?
* */
public class FileReaderTask implements Runnable{
	private static CustomFilePool pool;
	private String filePath;
	static{
		FileReaderTask.pool = new CustomFilePool();
	}

	public FileReaderTask(String filePath)throws FileNotFoundException{
		File file=new File(filePath);
		if(!file.exists())
			throw new FileNotFoundException();
		this.filePath=filePath;


	}



	public static CustomFilePool getPool(){
		return FileReaderTask.pool;
	}

	@Override
	public void run(){

		try{
					byte fileBytes[] = Files.readAllBytes(Paths.get(this.filePath));

					FileReaderTask.pool.add(this.filePath, new CustomFile(this.filePath, fileBytes));


		}catch (IOException exception){
			Logger.wtf(exception.toString());
		}


	}


}
