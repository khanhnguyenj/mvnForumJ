/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/work/FooWorkManager.java,v 1.3 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import commonj.work.RemoteWorkItem;
import commonj.work.Work;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;
import commonj.work.WorkRejectedException;

import net.myvietnam.mvncore.thirdparty.commonj.util.AbstractManager;
import net.myvietnam.mvncore.thirdparty.commonj.util.ThreadPool;

/**
 * Implemetation of a WorkManager. The WorkManager is the abstraction for 
 * dispatching and monitoring asynchronous work and is a factory
 * for creating application short or long lived Works.
 * 
 * <p>
 * This WorkManager does not support remoting works. However,
 * applications that follow the programming model will work. 
 * Serializable Works will be executed within the local JVM but the 
 * <code>WorkItem</code> can savely be downcasted to a 
 * <code>RemoteWorkItem</code>.
 * 
 * <p>
 * If the scheduled <code>Work</code> is a daemon Work, then the life-cycle of 
 * that Work is tied to the application that scheduled it. If the application is 
 * stopped, the <code>Work.release()</code> method will be called.
 *  
 * @see commonj.work.WorkManager
 * 
 * @author Andreas Keldenich
 * @version 1.0
 */
public final class FooWorkManager extends AbstractManager implements WorkManager {

    private int maxDaemons = 10;
    private List daemons = new ArrayList();
    
    /**
     * Creates a new instance of FooWorkManager.
     * @param pool the thread pool to execute work in
     */
    public FooWorkManager(ThreadPool pool) {
        super(pool);
    }
    
    /**
     * Creates a new instance of FooWorkManager.
     * @param pool the thread pool to execute work in
     * @param maxDaemons max number of daemons to allow
     */
    public FooWorkManager(ThreadPool pool, int maxDaemons) {
        this(pool);
        this.maxDaemons = maxDaemons;
    }
    
    /**
     * Dispatches a Work asynchronously. The work is dispatched and the method
     * returns immediately. The J2EE context of the caller is used to execute
     * the Work.
     * 
     * <p>
     * At-most-once semantics are provided. If the server fails then the Work
     * will not be executed on restart.
     * 
     * <p>
     * If this FooWorkManager is a pinned one, i.e. one obtained using
     * {@link RemoteWorkItem#getPinnedWorkManager()} and that JVM that it
     * represents has failed then a {@link WorkRejectedException } will be
     * thrown even if the remote JVM restarts. The pinned WorkManager must be
     * refreshed by using a normal WorkManager and then acquiring a new pinned
     * WorkManager.
     * 
     * @param work the Work to execute.
     * @return the workitem representing the asynchronous work. If the Work is
     *         serializable then a RemoteWorkItem is always returned.
     * @throws WorkException If queuing this up results in an exception then 
     *         a WorkException is thrown.
     * @throws IllegalArgumentException thrown if work is a javax.ejb.EnterpriseBean.
     * 
     * @see commonj.work.WorkManager#schedule(commonj.work.Work)
     */
    public WorkItem schedule(Work work) throws WorkException,
            IllegalArgumentException {
        
        return schedule(work, null);
    }

    
    /**
     * Dispatches a Work asynchronously. The work is dispatched and the method
     * returns immediately. The J2EE context of the caller is used to execute
     * the Work.
     * 
     * <p>
     * At-most-once semantics are provided. If the server fails then the Work
     * will not be executed on restart.
     * 
     * <p>
     * The WorkListener methods are called using the J2EE context of the caller
     * as the Work progresses through processing.
     * 
     * <p>
     * If this FooWorkManager is a pinned one, i.e. one obtained using
     * {@link RemoteWorkItem#getPinnedWorkManager() } and that JVM that it
     * represents has failed then a {@link WorkRejectedException } will be
     * thrown even if the remote JVM restarts. The pinned WorkManager must be
     * refreshed by using a normal WorkManager and then acquiring a new pinned
     * WorkManager.
     * 
     * @param work the Work to execute.
     * @param wl can be null or a WorkListener which is used to inform the
     *        application of the progress of a Work.
     * @return The workitem representing the asynchronous work. If the Work is
     *         serializable then a RemoteWorkItem is always returned.
     * @throws WorkException If queuing this up results in an exception then a
     *         WorkException is thrown.
     * @throws IllegalArgumentException thrown if work is a
     *         javax.ejb.EnterpriseBean.
     * 
     * @see commonj.work.WorkManager#schedule(commonj.work.Work,
     *      commonj.work.WorkListener)
     */
    public WorkItem schedule(Work work, WorkListener wl) throws WorkException,
            IllegalArgumentException {
        
        checkWork(work);
        
        FooWorkItem workItem = null;
        if (work instanceof Serializable) {
            workItem = new FooRemoteWorkItem(work, wl, this);
        }
        else { 
            workItem = new FooWorkItem(work, wl);
        }
        
        try {
            if (work.isDaemon()) {
                // check for max # of daemons
                if (daemons.size() >= maxDaemons) {
                    throw new WorkException("Too many daemons running: " + maxDaemons);
                }
                
                // store daemon list
                daemons.add(workItem);
                workItem.setDaemons(daemons);
                
                // according to the spec we shouldn't take deamon threads in pool
                Thread thread = new Thread(workItem);
                thread.start();
            }
            else {
                pool.execute(workItem);
            }
        }
        catch (InterruptedException e) {
            throw new WorkException("Failed to execute work: " + e.getMessage(), e);
        }
        catch (RuntimeException e) {
            throw new WorkException("Failed to execute work: " + e.getMessage(), e);
        }
        
        return workItem;
    }

    /**
     * Wait for all WorkItems in the collection to finish successfully or
     * otherwise. WorkItems from different WorkManagers can be placed in a
     * single collection and waited on together.
     * 
     * <p>
     * The WorkItems collection should not be altered once submitted until the
     * method returns.
     * 
     * @param workItems the Collection of WorkItem objects to wait for.
     * @param timeoutMs the timout in milliseconds. If this is 0 then this
     *        method returns immediately.
     * @return <code>true</code> if all WorkItems have completed, 
     *            <code>false</code> if the timeout has expired.
     * @throws InterruptedException thrown if the wait is interrupted.
     * @throws IllegalArgumentException thrown if workItems is null, any of the
     *         objects in the collection are not WorkItems or the timeout_ms is
     *         negative.
     * 
     * @see commonj.work.WorkManager#waitForAll(java.util.Collection, long)
     */
    public boolean waitForAll(Collection workItems, long timeoutMs)
            throws InterruptedException, IllegalArgumentException {
        
        // perfrom checks
        checkTimeout(timeoutMs);
        if (workItems == null || workItems.isEmpty()) {
            throw new IllegalArgumentException("null or empty work items.");
        }
        
        // setup result collector
        ResultCollector rc = new ResultCollector(timeoutMs);
        for (Iterator iter = workItems.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            
            rc.addWorkItem(workItem);
        }
        
        return rc.waitForAll();
    }

    /**
     * Wait for any of the WorkItems in the collection to finish. If there are
     * no WorkItems in the list then it returns immediately indicating a
     * timeout. WorkItems from different WorkManagers can be placed in a single
     * collection and waited on together.
     * 
     * <p>
     * The WorkItems collection should not be altered once submitted until the
     * method returns.
     * 
     * @param workItems the Collection of WorkItem objects to wait for.
     * @param timeoutMs the timeout in ms. If this is 0 then the method returns
     *        immediately, i.e. does not block.
     * @return the WorkItems that have completed or an empty Collection if it
     *         time out expires before any finished.
     * @throws InterruptedException thrown if the wait is interrupted.
     * @throws IllegalArgumentException thrown if workItems is null, any of the
     *         objects in the collection are not WorkItems or the timeout_ms is
     *         negative.
     * 
     * @see commonj.work.WorkManager#waitForAny(java.util.Collection, long)
     */
    public Collection waitForAny(Collection workItems, long timeoutMs)
            throws InterruptedException, IllegalArgumentException {
        
        // perfrom checks
        checkTimeout(timeoutMs);
        if (workItems == null || workItems.isEmpty()) {
            throw new IllegalArgumentException("null or empty work items.");
        }
        
        // setup result collector
        ResultCollector rc = new ResultCollector(timeoutMs);
        for (Iterator iter = workItems.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            
            rc.addWorkItem(workItem);
        }
        
        return rc.waitForAny();
    }

    
    /*
     * Check timeout value
     */
    private void checkTimeout(long timeoutMs) throws IllegalArgumentException {
        if (timeoutMs < 0L) {
            throw new IllegalArgumentException("Invalid timeout: " + timeoutMs);
        }
    }
    
    /*
     * Check if work is valid
     */
    private void checkWork(Work work) throws IllegalArgumentException {
//        if (work instanceof EnterpriseBean) {
//            throw new IllegalArgumentException("Work must not be an EJB.");
//        }
    }


    /**
     * Shutdown the thread pool and daemons.
     */
    public void shutdown() {
        super.shutdown();
        
        // shutdown daemons 
        for (Iterator iter = daemons.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            try {
                Work work = workItem.getResult();
                work.release();
            }
            catch (WorkException e) { 
                // ignore
            }
        }
    }
    
    /**
     * Shutdown the thread pool and daemons.
     */
    public void forceShutdown() {
        super.forceShutdown();
        
        // shutdown daemons 
        for (Iterator iter = daemons.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            try {
                Work work = workItem.getResult();
                work.release();
            }
            catch (WorkException e) { 
                // ignore
            }
        }
    }
    
    /**
     * Clean up.
     */
    protected void finalize() throws Throwable {
        shutdown();
    }


    /**
     * Getter for maxDaemons
     * @return Returns the maxDaemons.
     */
    public int getMaxDaemons() {
        return maxDaemons;
    }

    /**
     * Setter for maxDaemons
     * @param maxDaemons The maxDaemons to set.
     */
    public void setMaxDaemons(int maxDaemons) {
        this.maxDaemons = maxDaemons;
    }
    
}
