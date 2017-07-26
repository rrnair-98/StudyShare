/* This thread is being used to queue file read requests so as to prevent the creation of multiple threads.
		Also this prevents the ui thread/main thread from being blocked or "lagging".
		Tasks can be added using the add task method.
	*/
package src.services.microservices;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import src.services.microservices.filehandling.CustomFile;
import src.services.microservices.filehandling.CustomFilePool;


public class FileReaderThread extends Thread{
		private ArrayList<String> filePaths;
		private int finalIndex,begIndex;
		private static CustomFilePool pool;

		private static final String THREAD_NAME="FileReaderThread";
		static{
			FileReaderThread.pool=new CustomFilePool();
		}


		FileReaderThread(ArrayList<String> filePaths,int begIndex,int end){
			super(THREAD_NAME);
			this.filePaths=filePaths;
			this.finalIndex=end;
		}

		private void loop(){
			for(int i=this.begIndex;i<this.finalIndex;i++){
				String path=this.filePaths.get(i);
				FileReaderThread.pool.add(FileReaderThread.readFile(path));

			}
		}
		//can be use whenever a single file needs to be read
		public static CustomFile readFile(final String path){
			byte[] bytes=Files.readAllBytes(Paths.getPath(path));
			return new CustomFile(path,bytes);
		}


		public static CustomFilePool getPool(){
			return FileReaderThread.pool;
		}

		@Override
		public void run(){
			this.loop();
		}
		
		




}