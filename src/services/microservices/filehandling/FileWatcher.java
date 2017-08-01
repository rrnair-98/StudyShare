package services.microservices.filehandling;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
//import static java.nio.file.StandardWatchEventKinds.ENTRY_OVERFLOW;
import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.filehandling.customfile.CustomFilePool;

/*
@author Pratik
Saves changes to the file pool if some file gets modified in the watched directory. The full path of the subdirectory should be provided if any
*/

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
        dir.register(watcher,ENTRY_MODIFY,ENTRY_CREATE,ENTRY_DELETE); //ONLY THE REQUIRED EVENT IS REGISTERED.
		start();
		}
		catch(IOException e){}
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
							cfp.add(p.toString(), new CustomFile(p.toString(), Files.readAllBytes(p)));
						}
						else if(kind==ENTRY_CREATE)
						{
							WatchEvent<Path> pev = (WatchEvent<Path>) e;
							Path p = pev.context();
							cfp.add(p.toString(), new CustomFile(p.toString(),Files.readAllBytes(p)));
						}
						else if(kind==ENTRY_DELETE)
						{
							WatchEvent<Path> pev = (WatchEvent<Path>) e;
							Path p = pev.context();
							cfp.remove(p.toString());
						}

					}
					if (key.reset() == false)
						break;
				} catch (IOException e) {
				}
			}
		}
		catch(InterruptedException ie)
		{

		}
	}
}