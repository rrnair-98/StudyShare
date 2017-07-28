package src.services.microservices.filehandling;

import java.util.HashMap;
import src.services.microservices.filehandling.CustomFile;
public class CustomFilePool{
	private HashMap <String,CustomFile>pool;

	public CustomFilePool(){
		this.pool=new HashMap();
	}

	public void add(final String path,final CustomFile customFile){
		this.pool.put(path,customFile);
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
	public Set getKeySet(){
		return this.pool.keySet();
	}
	

	
}
