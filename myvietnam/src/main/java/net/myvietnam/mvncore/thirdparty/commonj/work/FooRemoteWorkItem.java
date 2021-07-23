/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/work/FooRemoteWorkItem.java,v 1.3 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.work;

import commonj.work.RemoteWorkItem;
import commonj.work.Work;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

/**
 * Implementation of the remote work item.
 * 
 * @author Andreas Keldenich
 * @version 1.0
 */
public final class FooRemoteWorkItem extends FooWorkItem implements
        RemoteWorkItem {

    private WorkManager wm;

    /**
     * Creates a new instance of FooRemoteWorkItem.
     * 
     * @param work the work
     * @param wl work listener
     * @param wm the "remote" work manger
     */
    public FooRemoteWorkItem(Work work, WorkListener wl, WorkManager wm) {
        super(work, wl);
        this.wm = wm;
    }

    /**
     * Calls the remote Work object's {@link Work#release()} method. It
     * instructs the Work associated with this RemoteWorkItem to 'stop'
     * voluntarily. If the work has already stopped then this has no effect. The
     * {@link commonj.work.WorkItem#getStatus()} can be used to determine
     * whether it has stopped or not but the Work can always stop just after
     * getStatus returns 'still working'. The best way to wait for such Works to
     * release is using the
     * {@link WorkManager#waitForAll(java.util.Collection, long)} API.
     * 
     * @see commonj.work.RemoteWorkItem#release()
     */
    public void release() {
        work.release();
    }

    /**
     * This returns a pinned WorkManager which represents the JVM that was used
     * to execute this Work. This allows subsequent remote Works to be sent to
     * the same JVM as the one that was used to execute this WorkItem. If the
     * remote JVM fails then subsequent schedule Works on this WorkManager will
     * fail with a rejected exception even if the remote JVM restarts. The
     * pinned WorkManager is associated with the JVM instance that was running
     * as opposed to any future JVM instance.
     * 
     * @return the WorkManager associated with the JVM that was used to execute
     *         this RemoteWorkItem
     * @see commonj.work.RemoteWorkItem#getPinnedWorkManager()
     */
    public WorkManager getPinnedWorkManager() {
        return wm;
    }

}
