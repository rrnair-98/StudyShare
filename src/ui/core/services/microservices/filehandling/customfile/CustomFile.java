package ui.core.services.microservices.filehandling.customfile;

import java.io.File;
import java.io.FileNotFoundException;
/*
@Author Rohan
TBD : add a function to obtain name of the file by substringing the path.
This class acts as a placeholder for the file name and its contents*/

public class CustomFile{
	private String path;
	private String fileName;
	private byte[] fileContents;
	public CustomFile(String path,byte[] bytes)throws FileNotFoundException{
		File f=new File(path);
		if(!f.exists()) throw new FileNotFoundException();

		this.fileName=f.getName();
		this.path=path;

		this.fileContents=bytes;
	}

	public byte[] getContents(){
		return this.fileContents;
	}
	
	public String getPath(){
		return this.path;
	}
	public String getFileName(){return this.fileName;}
	public int getFileSize(){return this.fileContents.length;}

	@Override
	public String toString(){
		return "{fileName:"+this.fileName+",path:"+this.path+",fileSize:"+this.getFileSize()+"}";
	}

	


}