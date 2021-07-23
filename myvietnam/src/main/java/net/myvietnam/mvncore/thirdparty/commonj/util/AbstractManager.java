/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/util/AbstractManager.java,v 1.3 2008/05/21 07:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:16 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.util;

/**
 * Abtract manger class - parent of all <code>TimerManager</code> and 
 * <code>WorkManagers</code>
 *
 * @author  Andreas Keldenich
 * @version 1.0
 */
public abstract class AbstractManager {

    protected ThreadPool pool;

    /**
     * Creates a new instance of AbstractManager.
     */
    public AbstractManager(ThreadPool pool) {
        this.pool = pool;
    }
    
    /**
     * Shutdown the thread pool.
     */
    public void shutdown() {
        // shutdown the thread pool
        pool.shutdown();
    }
    
    /**
     * Force shutdown the thread pool.
     */
    public void forceShutdown() {
        // shutdown the thread pool
        pool.forceShutdown();
    }
    

}
