package services.microservices;

import services.microservices.FileReaderTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;

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
