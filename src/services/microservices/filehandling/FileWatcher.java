package 
import java.io.IOException;
import java.nio.WatchService;
import java.nio.WatchEvent;
import java.nio.WatchKey;
import java.nio.Path;
import java.nio.Paths;
import java.nio.FileSystems;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATED;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETED;
import static java.nio.file.StandardWatchEventKinds.ENTRY_OVERFLOW;
import src.services.microservices.filehandling.CustomFilePool;
import src.services.microservices.filehandling.CustomFile;

/*Saves changes to the file pool if some file gets modified in the watched directory. The full path of the subdirectory should be provided if any*/

public class FileWatcher extends Thread
{
	CustomFilePool cfp;
	String path;
	WatchService watcher;
	FileWatcher(String path,CustomFilePool cfp)
	{
		try{
		this.path=path;
		this.cfp=cfp;
		this.watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(path);
        dir.register(watcher,ENTRY_MODIFY,ENTRY_CREATED,ENTRY_DELETED); //ONLY THE REQUIRED EVENT IS REGISTERED.
		start();
		}
		catch(IOException e){}
	}
	public void run()
	{
		while(true){
		try{
			WatchKey key=watcher.take(); 
			for(WatchEvent<?> e:key.pollEvents())
			{
				WatchEvent.Kind<?> kind=e.kind();
				switch(kind)
				{
					case ENTRY_MODIFY:
						WatchEvent<Path> pev=(WatchEvent<Path>)e;
						Path p=e.context();
						cfp.add(p.toString,new CustomFile(p.toString()));
					break;
					case ENTRY_CREATED:
						WatchEvent<Path> pev=(WatchEvent<Path>)e;
						Path p=e.context();
						cfp.add(p.toString(),new CustomFile(p.toString()));
					break;
					case ENTRY_DELETED:
						WatchEvent<Path> pev=(WatchEvent<Path>)e;
						Path p=e.context();
						cfp.remove(p.toString());
					break;
				}
			}
			if(key.reset()==false)
				break;
		}
		catch(IOException e){}
		}
	}
}