package services.microservices.filehandling.customfile;

import java.util.HashMap;
import java.util.Set;

import services.microservices.filehandling.customfile.CustomFile;
public class CustomFilePool{
	private HashMap <String,CustomFile>pool;

	public CustomFilePool(){
		this.pool=new HashMap();
	}

	public void add(final String path,final CustomFile customFile){
		this.pool.put(path,customFile);
		System.out.println("added "+customFile.getFileName()+" PATH "+customFile.getPath());
	}

	public void remove(final String key){
		this.pool.remove(key);
	}

	public CustomFile get(final String key){
		return this.pool.get(key);
	}
	public boolean isPoolEmpty(){
		return this.pool.isEmpty();
	}
	public int getPoolSize(){
		return this.pool.size();
	}
	public void clearPool(){
		this.pool.clear();
	}
	public boolean containsKey(final String key){
		return this.pool.containsKey(key);
	}
	/* this function returns the paths that are stored in the list*/
	public Set<String> getKeySet(){
		return this.pool.keySet();
	}

	public void replace(final String key,CustomFile customFile){
		this.pool.replace(key,customFile) ;
	}
	
	@Override
	public String toString(){
		return this.pool.toString();
	}
	
}
