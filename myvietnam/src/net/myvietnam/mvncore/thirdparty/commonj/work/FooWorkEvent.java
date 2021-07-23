/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/work/FooWorkEvent.java,v 1.3 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.work;

import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;

/**
 * Implementation of a work event.
 *
 * @author  Andreas Keldenich
 * @version 1.0
 */
public class FooWorkEvent implements WorkEvent {

    private int type = 0;
    private WorkItem wi = null;
    private WorkException ex = null;
    
    /**
     * Creates a new instance of FooWorkEvent.
     * @param wi the work item
     * @param type type of the event.
     */
    public FooWorkEvent(WorkItem wi, int type) {
        this.wi = wi;
        this.type = type;
    }

    /**
     * Get the event type.
     * @see commonj.work.WorkEvent#getType()
     */
    public int getType() {
        return type;
    }

    /** 
     * Get the work item.
     * @see commonj.work.WorkEvent#getWorkItem()
     */
    public WorkItem getWorkItem() {
        return wi;
    }

    /**
     * Get the Exception if one was thorwn by the work.
     * @see commonj.work.WorkEvent#getException()
     */
    public WorkException getException() {
        return ex;
    }
    
    /**
     * Set Exception
     * @param th thorwable
     */
    public void setException(Throwable th) {
        this.ex = new WorkException("Error Executing work: " + th.toString(), th);
    }

}
