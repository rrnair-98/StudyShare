package services.microservices.filehandling;

import services.microservices.filehandling.customfile.CustomFilePool;
import services.microservices.filehandling.customfile.CustomFile;

import java.io.File;
import java.io.FileNotFoundException;

import services.microservices.threadpool.GeneralThreadPool;
import services.microservices.threadpool.SequenceConvulsion;
import services.microservices.utilities.Housekeeper;
import services.microservices.utilities.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;

/*
* @author Rohan
* This class reads files.
* This class does the following:
*	1.Schedules files to be read
*	2.Stores them onto a pool
*	3.Override the interface which updates a ProgressBar?
* */
public class FileReaderRunnable implements Runnable,FileReaderRunnableConstants{

	private static CustomFilePool pool;
	private static GeneralThreadPool fileReaderRunnableThreadPool;
	private String filePath;
	private static SequenceConvulsion sequenceConvulsion;

	private static long fileLength;
	//redundant code ... Sharing reference of main list everywhere. Not required as of now.
	//private static ArrayList<String> filesToBeRead;

	static{
		FileReaderRunnable.pool=new CustomFilePool();
		FileWatcher.setFilePool(FileReaderRunnable.pool);
		//FileReaderRunnable.filesToBeRead=new ArrayList<String>();
	}

	public FileReaderRunnable(String filePath)throws FileNotFoundException{
		File file=new File(filePath);
		if(!file.exists())
			throw new FileNotFoundException();
		this.filePath=filePath;
		FileReaderRunnable.fileReaderRunnableThreadPool.add(FileReaderRunnable.this);
	}

	/* FUNCTION MUST BE DELETED */
	public static void printPool(){
		System.out.println(FileReaderRunnable.pool.toString());
	}
	public static void setSequenceConvulsion(SequenceConvulsion sequenceConvulsion){
		FileReaderRunnable.sequenceConvulsion=sequenceConvulsion;
		FileReaderRunnable.fileReaderRunnableThreadPool=new GeneralThreadPool(FileReaderRunnable.MAX_QUE_SIZE,FileReaderRunnable.sequenceConvulsion);


	}
	public static void reinitThreadPool(){
		FileReaderRunnable.fileReaderRunnableThreadPool=new GeneralThreadPool(FileReaderRunnable.MAX_QUE_SIZE);

	}


	/*public static void setFilesToBeRead(ArrayList<String> filesToBeRead){
		FileReaderRunnable.filesToBeRead=filesToBeRead;
	}*/


	public static CustomFilePool getPool(){
		return FileReaderRunnable.pool;
	}


	public static GeneralThreadPool getThreadPool(){
		return FileReaderRunnable.fileReaderRunnableThreadPool;
	}

	@Override
	public void run(){

		try{
			if(this.filePath.contains(".DS"))
				return;
			System.out.println("about to read "+this.filePath );
					CustomFile customFile=new CustomFile(this.filePath, Housekeeper.getCompressedBytes(this.filePath));
			System.out.println("ASDASD");

						FileReaderRunnable.pool.add(this.filePath, customFile);

					System.out.println("ASDASD DONE");
		}catch (IOException exception){
			Logger.wtf(exception.toString());
		}


	}


}
interface FileReaderRunnableConstants{
	public static final int MAX_QUE_SIZE=3;
}