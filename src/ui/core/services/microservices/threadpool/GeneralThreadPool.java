package ui.core.services.microservices.threadpool;

import ui.core.services.microservices.filehandling.FileReaderRunnable;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
* @author Rohan
* This class uses the ScheduledThreadPoolExecutor to schedule jobs.
* This class provides the following methods
*   1. add
*       to be used for executing tasks calls submit internally
*   2. submit
*       the preferred way to add tasks to the pool
*   3. startListWatcher
*       the class has a thread called ListWatcher which constantly monitors the jobs that are being executed.
*       Once the pool finishes executing all tasks and is free the listWatcher thread calls a callback method
*       kickStart()[part of SequenceConvulsion] to start the server.
* the class uses a scheduledThreadPool.
* */
public class GeneralThreadPool extends ScheduledThreadPoolExecutor implements GeneralThreadPoolConstants{
    private Thread listWatcher;
    private ArrayList<Future> arrayList=new ArrayList();
    private boolean isFirst=false;
    private SequenceConvulsion sequenceConvulsion;

    public GeneralThreadPool(final int size){
        super(size);
        this.initListWatcher();

    }
    public GeneralThreadPool(){
        this(GeneralThreadPoolConstants.CORE_POOL_SIZE);
    }


    public GeneralThreadPool(final int size,final SequenceConvulsion sequenceConvulsion){
        this(size);
        this.sequenceConvulsion=sequenceConvulsion;
    }


    private void initListWatcher(){
        this.listWatcher=new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    int i=0;
					/* this works since arrayList.size is never going to be 0 whenver a task has been submitted*/

				/* not using for each since it leads to ConcurrentModificationException. JVM does that whenver new items are added to the iterator of Collection to maintain safe state*/
                        while(!GeneralThreadPool.this.isTerminated());
                    System.out.println("Done with watching");



                    //GeneralThreadPool.this.terminated();
                    if(GeneralThreadPool.this.sequenceConvulsion!=null)
                        GeneralThreadPool.this.sequenceConvulsion.kickStart();
				/* for cases when jobs are added later*/

				    GeneralThreadPool.this.terminated();
                    GeneralThreadPool.this.arrayList.clear();
                    GeneralThreadPool.this.isFirst=false;
				/* since threads cant be restarted */
                    GeneralThreadPool.this.initListWatcher();

                }catch(	Exception e){
                    e.printStackTrace();
                }
            }
        });
        this.listWatcher.setName("listWatcher");

    }
    @Override
    public void terminated(){
        super.terminated();
        System.out.println("terminated");
		/* Call callback here */
    }

    public void add(Runnable runnable){
        System.out.println("adding");
        this.submit(runnable);
    }

    @Override
    public Future submit(Runnable run){
        Future holder=(super.submit(run));
        this.arrayList.add(holder);

        //code to start internally

        if(!isFirst){
            isFirst=!isFirst;
            this.startWatching();

        }

        return holder;
    }

    /* or this to make submit better*/
    public void startWatching(){
        this.listWatcher.start();
    }

    public boolean isFirst(){return this.isFirst;}


}
interface GeneralThreadPoolConstants{
    public static int CORE_POOL_SIZE=5;
}