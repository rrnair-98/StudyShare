package services.microservices.filehandling;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
//import static java.nio.file.StandardWatchEventKinds.ENTRY_OVERFLOW;
import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.filehandling.customfile.CustomFilePool;
import services.microservices.utilities.logger.Logger;

/*
* @author Pratik
* Saves changes to the file pool if some file gets modified in the watched directory.The full path of the subdirectory
* should be provided if any. This class checks for the following kinds of events
* 	1.ENTRY_MODIFY
* 			will replace the file from the pool
* 	2.ENTRY_CREATE
* 			will add a file to filepool
* 	3.ENTRY_DELETE
* 			will remove a file from the pool
* */

public class FileWatcher extends Thread
{
	private CustomFilePool cfp;//reference can be obtained from FileReaderRunnables.
	private String path;
	private WatchService watcher;
	FileWatcher(String path,CustomFilePool cfp)
	{
		try{
		this.path=path;
		this.cfp=cfp;
		this.watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(path);
        dir.register(watcher,ENTRY_MODIFY,ENTRY_CREATE,ENTRY_DELETE); //ONLY THE REQUIRED EVENT IS REGISTERED.
		start();
		}
		catch(IOException e){
			Logger.wtf(e.toString());
		}
	}
	public void run()
	{
		try {
			while (true) {
				try {
					WatchKey key = watcher.take();
					for (WatchEvent<?> e : key.pollEvents()) {
						WatchEvent.Kind<?> kind = e.kind();
						if(kind==ENTRY_MODIFY)
						{
							WatchEvent<Path> pev = (WatchEvent<Path>) e;
							Path p = pev.context();
							this.cfp.replace(p.toString(), new CustomFile(p.toString(), Files.readAllBytes(p)));
							Logger.i("replacing "+p.toString()+" To pool");
						}
						else if(kind==ENTRY_CREATE)
						{
							WatchEvent<Path> pev = (WatchEvent<Path>) e;
							Path p = pev.context();
							Logger.i("adding "+p.toString()+" To pool");
							this.cfp.add(p.toString(), new CustomFile(p.toString(),Files.readAllBytes(p)));
						}
						else if(kind==ENTRY_DELETE)
						{
							WatchEvent<Path> pev = (WatchEvent<Path>) e;
							Path p = pev.context();
							this.cfp.remove(p.toString());
							Logger.i("removing "+p.toString()+" from pool");
						}

					}
					if (!key.reset() )
						break;
				} catch (IOException e) {
					Logger.wtf(e.toString());
				}
			}
		}
		catch(InterruptedException ie)
		{
				Logger.wtf(ie.toString());
		}
	}
}