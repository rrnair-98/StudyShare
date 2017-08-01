package services.microservices;

import services.microservices.FileReaderTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class FileReaderTaskPool {
    private static ScheduledThreadPoolExecutor readerPool;
    static{
        readerPool=new ScheduledThreadPoolExecutor(5);
    }
    public static void add(FileReaderTask frt)
    {
        readerPool.execute(frt);
    }
    public static ScheduledThreadPoolExecutor getReaderPool()
    {
        return readerPool;
    }
}
