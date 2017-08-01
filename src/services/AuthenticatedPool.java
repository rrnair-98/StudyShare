package services;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AuthenticatedPool {
    private static ScheduledThreadPoolExecutor authenticatedPool;

    static {
        authenticatedPool = new ScheduledThreadPoolExecutor(3);
    }

    public static boolean add(ClientsToBeServed userThread)
    {
        if(userThread.isAuthenticated())
        {
            authenticatedPool.execute(userThread);
            return true;
        }
        return false;
    }