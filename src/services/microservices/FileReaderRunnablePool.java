package services.microservices;

import services.microservices.FileReaderTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/*
@author Pratik
Manages the File reading task threads
*/
public class FileReaderRunnablePool {
    private static ScheduledThreadPoolExecutor readerPool;
    static{
        readerPool=new ScheduledThreadPoolExecutor(5);
    }
    public  void add(FileReaderTask frt)
    {
        readerPool.execute(frt);
    }
    public  ScheduledThreadPoolExecutor getReaderPool()
    {
        return readerPool;
    }
}
