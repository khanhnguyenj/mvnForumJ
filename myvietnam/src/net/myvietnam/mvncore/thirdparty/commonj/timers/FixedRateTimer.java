/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/FixedRateTimer.java,v 1.3 2008/05/21 07:13:14 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/05/21 07:13:14 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

import commonj.timers.TimerListener;

/**
 * Fixed rate timer.
 *
 * @author  Kelly
 * @version 1.0
 */
public final class FixedRateTimer extends FooTimer {

    /**
     * Creates a new instance of FixedRateTimer.
     * 
     * @param startTime start time
     * @param period execution period
     * @param listener the timer listener for this timer.
     */
    public FixedRateTimer(long startTime, long period, TimerListener listener) {
        super(startTime, period, listener);
    }

    /**
     * Compute the next execution time.
     * 
     * @see net.myvietnam.mvncore.thirdparty.commonj.timers.FooTimer#computeNextExecutionTime()
     */
    protected void computeNextExecutionTime() {
        long currentTime = System.currentTimeMillis();
        long execTime = scheduledExcecutionTime + period;
        
        while (execTime <= currentTime) {
            execTime += period;
        }
        
        scheduledExcecutionTime = execTime;
    }

}
