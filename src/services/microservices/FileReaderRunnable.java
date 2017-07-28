package src.services.microservices;

import src.services.microservices.filehandling.customfile.CustomFilePool;
import src.services.microservices.filehandling.customfile.CustomFile;
public class FileReaderRunnables extends Task<Boolean>{
	private CustomFilePool pool; 
	private FilePath filePath;
	static{
		FileReaderRunnables.pool = new CustomFilePool(); 
	}

	public FileReaderRunnables(String filePath){
		this.filePath=filePath;
	}



	public static CustomFilePool getPool(){
		return FileReaderRunnables.pool;
	}

	@Override
	public Boolean call(){





	}


}
