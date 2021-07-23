/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/FixedDelayTimer.java,v 1.3 2008/05/21 07:13:13 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:13 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

import commonj.timers.TimerListener;

/**
 * Fixed delay timer.
 *
 * @author  Kelly
 * @version 1.0
 */
public final class FixedDelayTimer extends FooTimer {

    /**
     * Creates a new instance of FixedDelayTimer.
     * 
     * @param startTime start time
     * @param period execution period
     * @param listener the timer listener for this timer.
     */
    public FixedDelayTimer(long startTime, long period, TimerListener listener) {
        super(startTime, period, listener);
    }

    /**
     * Compute the next execution time.
     * 
     * @see net.myvietnam.mvncore.thirdparty.commonj.timers.FooTimer#computeNextExecutionTime()
     */
    protected void computeNextExecutionTime() {
        scheduledExcecutionTime = System.currentTimeMillis() + period;
    }

}
