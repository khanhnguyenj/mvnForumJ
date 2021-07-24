/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/work/ResultCollector.java,v 1.3 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.work;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import commonj.work.WorkEvent;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

/**
 * This class collects the results from workers.
 * 
 * @author Andreas Keldenich
 * @version 1.0
 */
public final class ResultCollector {
    
    private int workCount = 0;
    private int expectedWorkCount = 0;
    private long timeout;
    private List workItems = new ArrayList();

    /**
     * Creates a new ResultCollector.
     * @param timeoutMs timeout in millis
     */
    public ResultCollector(long timeoutMs) {
        this.timeout = timeoutMs;
    }

    /**
     * Wait for workers/agents to complete. The <code>Executor</code> calls
     * this method which makes him wait while the workers run.
     * 
     * @return <code>true</code> if all items are done in time.
     * @throws InterruptedException If work got interrupted
     */
    public synchronized boolean waitForAll() throws InterruptedException {
        boolean timedOut = false;
        long endTime = 0;
        
        if (timeout == WorkManager.IMMEDIATE) {
            return allItemsDone();
        }
        if (timeout == WorkManager.INDEFINITE) {
            endTime = timeout;
        }
        else {
            endTime = System.currentTimeMillis() + timeout;
        }
        
        // is work already completed?
        if (allItemsDone()) {
            return true;
        }
        
        // wait for work
        while (workCount < expectedWorkCount) {
            try {
                // wait 10 ms or to be notified.
                wait(10); 
            }
            catch (InterruptedException e) { 
                // We got interrupted - bad luck... rethrow it.
                throw e;
            }
            
            // check for timeout
            if (System.currentTimeMillis() >= endTime) {
                timedOut = true;
                break;
            }
            
            // are we done?
            if (allItemsDone()) {
                break;
            }
        }

        notifyAll();
        return ! timedOut;
    }

    /*
     * Check if all items are done.
     */
    private boolean allItemsDone() {
        for (Iterator iter = workItems.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            if (workItem.getStatus() != WorkEvent.WORK_COMPLETED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Wait for workers/agents to complete. The <code>Executor</code> calls
     * this method which makes him wait while the workers run.
     * 
     * @return true if the execution timed out
     * @throws InterruptedException If work got interrupted
     */
    public synchronized Collection waitForAny() throws InterruptedException {
        long endTime = 0;
        
        if (timeout == WorkManager.IMMEDIATE) {
            return itemsDone();
        }
        if (timeout == WorkManager.INDEFINITE) {
            endTime = timeout;
        }
        else {
            endTime = System.currentTimeMillis() + timeout;
        }
        
        while (workCount == 0) {
            try {
                // wait 10 ms or to be notified.
                wait(10); 
            }
            catch (InterruptedException e) { 
                // We got interrupted - bad luck... rethrow it.
                throw e;
            }
            
            // check for timeout
            if (System.currentTimeMillis() >= endTime) {
                break;
            }
            
            // are we done?
            if (! itemsDone().isEmpty()) {
                break;
            }
        }

        notifyAll();
        return itemsDone();
    }

    /*
     * return the work items that are done.
     */
    private Collection itemsDone() {
        Collection items = new ArrayList();
        for (Iterator iter = workItems.iterator(); iter.hasNext();) {
            WorkItem workItem = (WorkItem) iter.next();
            if (workItem.getStatus() == WorkEvent.WORK_COMPLETED) {
                items.add(workItem);
            }
        }
        return items;
    }

    /**
     * Let the collector know that this work is finished.
     */
    public synchronized void workDone() {
        workCount++;
        notifyAll();
    }

    /**
     * Add a workItem.
     */
    public void addWorkItem(WorkItem workItem) {
        workItems.add(workItem);
        if (workItem instanceof FooWorkItem) {
            FooWorkItem fwi = (FooWorkItem) workItem;
            fwi.setResultCollector(this);
        }
        expectedWorkCount++;
    }
    

    /**
     * @return Returns the timeout.
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout The timeout to set.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
