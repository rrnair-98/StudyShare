package services;

import java.util.concurrent.ScheduledThreadPoolExecutor;
/*
@author Pratik
Adds the ClientsToBeServed Threads to the pool for their future execution.
*/
public class AuthenticatedPool {
    private static ScheduledThreadPoolExecutor authenticatedPool;

    static {
        authenticatedPool = new ScheduledThreadPoolExecutor(3);
    }

    public static boolean add(ClientsToBeServed userThread) {
        if (userThread.isAuthenticated()) {
            authenticatedPool.execute(userThread);
            return true;
        }
        return false;
    }
}