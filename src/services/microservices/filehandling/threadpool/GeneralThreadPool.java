package services.microservices.filehandling.threadpool;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/*
* @author Pratik
* This class uses the ScheduledThreadPoolExecutor to schedule jobs.
* This class provides the following methods
*   1. add
*       to be used for executing tasks
*   2. shutdownPool
*       to be used for shutting down the pool
* Since the class uses a scheduledThreadPool it increases its pools size dynamically as and when required.
* */
public class GeneralThreadPool implements GeneralThreadPoolConstants{
    private ScheduledThreadPoolExecutor readerPool;
    //to be used to dynamically grow the size of the pool
    private int poolSize;
    public GeneralThreadPool(int poolSize){
        this.poolSize=poolSize;
        this.readerPool=new ScheduledThreadPoolExecutor(poolSize);
    }
    public GeneralThreadPool(){
        this(GeneralThreadPoolConstants.CORE_POOL_SIZE);
    }


    public  void add(Runnable frt)
    {
        this.readerPool.execute(frt);
    }
    public  ScheduledThreadPoolExecutor getReaderPool()
    {
        return this.readerPool;
    }

    public void shutdownPool(){
        this.readerPool.shutdown();
    }

}
interface GeneralThreadPoolConstants{
    public static int CORE_POOL_SIZE=5;
}