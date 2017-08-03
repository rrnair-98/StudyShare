package services.microservices.filehandling;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
//import static java.nio.file.StandardWatchEventKinds.ENTRY_OVERFLOW;
import services.microservices.filehandling.customfile.CustomFile;
import services.microservices.filehandling.customfile.CustomFilePool;
import services.microservices.filehandling.callback.ArrayListCallback;
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
*NOTE: FileWatcher's setFilePool and set filesChanged must be called atleast once
* */

public class FileWatcher extends Thread
{
	private static CustomFilePool cfp;//reference can be obtained from FileReaderRunnables.
	private String folderToWatch;
	private WatchService watcher;
	private static ArrayListCallback arrayListCallback;


	/* to be called from within FileReaderRunnable*/
	public static void setFilePool(CustomFilePool customFilePool){
		FileWatcher.cfp=customFilePool;
	}

	/* to be called from within Server class*/
	public static void setArrayListCallback(ArrayListCallback callback){
		FileWatcher.arrayListCallback=callback;
	}

	public FileWatcher(String path)
	{
		try{
		this.folderToWatch=path;
		this.watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(this.folderToWatch);
        dir.register(watcher,ENTRY_MODIFY,ENTRY_CREATE,ENTRY_DELETE); //ONLY THE REQUIRED EVENT IS REGISTERED.
		this.start();
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

						WatchEvent<Path> pev = (WatchEvent<Path>) e;

						Path p = pev.context();

						if(kind==ENTRY_MODIFY)
						{
							FileWatcher.cfp.replace(p.toString(), new CustomFile(p.toString(), Files.readAllBytes(p)));
							Logger.i("replacing "+p.toString()+" To pool");
							if(FileWatcher.arrayListCallback!=null)
								FileWatcher.arrayListCallback.onAdd(p.toString());
						}
						else if(kind==ENTRY_CREATE)
						{
							Logger.i("adding "+p.toString()+" To pool");
							FileWatcher.cfp.add(p.toString(), new CustomFile(p.toString(),Files.readAllBytes(p)));
							if(FileWatcher.arrayListCallback!=null)
								FileWatcher.arrayListCallback.onAdd(p.toString());
						}
						else if(kind==ENTRY_DELETE)
						{

							FileWatcher.cfp.remove(p.toString());
							Logger.i("removing "+p.toString()+" from pool");
							if (FileWatcher.arrayListCallback!=null)
								FileWatcher.arrayListCallback.onRemove(p.toString());
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