package src.services.microservices.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
/*TBD : add a function to obtain name of the file by substringing the path. 
This class acts as a placeholder for the file name and its contents*/
public class CustomFile{
	private String path;
	private String fName;
	private byte[] fileContents;
	private long fileSize;
	public CustomFile(String path,byte[] bytes)throws FileNotFoundException{
		File f=new File(path);
		if(!f.exists()) throw new FileNotFoundException();

		this.fName=f.getName();
		this.fileSize=f.length();
		this.path=path;

		this.fileContents=bytes;
	}

	public byte[] getContents(){
		return this.fileContents;
	}
	
	public String getPath(){
		return this.path;
	}
	

	@Override
	public String toString(){
		return "{fName:"+this.fName+",path:"+this.path+",fileSize:"+this.fileSize+"}";
	}

	


}