/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/util/ThreadPool.java,v 1.4 2008/03/28 03:38:40 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2008/03/28 03:38:40 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.util;

import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * Thread pool implementation to execute <code>Work</code> and 
 * <code>Timer</code>s.
 *
 * @author  Andreas Keldenich
 * @version 1.0
 */
public final class ThreadPool {

    public static final int DEFAULT_MIN_THREADS  = 2;
    public static final int DEFAULT_MAX_THREADS  = 10;
    public static final int DEFAULT_QUEUE_LENGTH = 10;
    
    private PooledExecutor pool;
    
    /**
     * Creates a new instance of ThreadPool.
     * 
     * @param minThreads minimum number of threads
     * @param maxThreads maximum number of threads
     * @param queueLength length of the execution queue
     */
    public ThreadPool(int minThreads, int maxThreads, int queueLength) {
        if (queueLength == 0) {
            pool = new PooledExecutor();
        }
        else {
            Channel queue = new BoundedBuffer(queueLength);
            pool = new PooledExecutor(queue);
        }
        pool.setMinimumPoolSize(minThreads);
        pool.setMaximumPoolSize(maxThreads);
        pool.createThreads(minThreads);
        pool.abortWhenBlocked();
    }

    /**
     * Arrange for the given command to be executed by a thread in this
     * pool. The method normally returns when the command has been
     * handed off for (possibly later) execution.
     * 
     * @param command command to execute
     * @throws InterruptedException if execution fails
     */
    public void execute(Runnable command) throws InterruptedException {
        pool.execute(command);
    }
    
    /**
     * Shutdown the pool after processing the currently queue tasks.
     */
    public void shutdown() {
        pool.shutdownAfterProcessingCurrentlyQueuedTasks();
    }

    /**
     * Force shutdown the pool immediately.
     */
    public void forceShutdown() {
        pool.shutdownNow();
    }

}
