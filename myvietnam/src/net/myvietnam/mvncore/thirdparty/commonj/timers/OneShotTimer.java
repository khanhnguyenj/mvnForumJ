/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/OneShotTimer.java,v 1.4 2008/05/21 07:13:15 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2008/05/21 07:13:15 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

import commonj.timers.TimerListener;

/**
 * A timer that expires once and stops execution.
 *
 * @author  Kelly
 * @version 1.0
 */
public final class OneShotTimer extends FooTimer {

    /**
     * Creates a new instance of OneShotTimer.
     * 
     * @param startTime start time
     * @param listener the timer listener
     */
    public OneShotTimer(long startTime, TimerListener listener) {
        super(startTime, 0L, listener);
    }

    /**
     * Compute the next execution time - never again.
     * 
     * @see net.myvietnam.mvncore.thirdparty.commonj.timers.FooTimer#computeNextExecutionTime()
     */
    protected void computeNextExecutionTime() {
        // empty
    }

}
