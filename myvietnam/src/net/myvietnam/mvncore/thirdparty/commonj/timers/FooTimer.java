/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/FooTimer.java,v 1.5 2008/05/21 07:13:14 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2008/05/21 07:13:14 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

import commonj.timers.StopTimerListener;
import commonj.timers.Timer;
import commonj.timers.TimerListener;

/**
 * A FooTimer is returned when a TimerListener is scheduled using one of 
 * the <code>FooTimerManager.schedule</code> methods.
 * It allows retrieving information about the scheduled TimerListener and
 * allows canceling it.
 *
 * @author  Kelly
 * @version 1.0
 */
public abstract class FooTimer implements Timer {
    
    protected boolean stopped = false;
    private boolean cancelled = false;
    protected long scheduledExcecutionTime;
    protected long period = 0L;
    private TimerListener listener;
    
    /**
     * Creates a new instance of FooTimer.
     * 
     * @param startTime start time
     * @param period execution period
     * @param listener the timer listener for this timer.
     */
    public FooTimer(long startTime, long period, TimerListener listener) {
        scheduledExcecutionTime = startTime;
        this.period = period;
        this.listener = listener;
    }
    
    /**
     * Compute the next execution time for this timer.
     */
    protected abstract void computeNextExecutionTime();
    
    
    /**
     * Execute the timer listener.
     */
    public void execute() {
        listener.timerExpired(this);
    }
    

    /**
     * This cancels the timer and all future TimerListener invocations and
     * may be called during the <code>TimerListener.timerExpired</code> 
     * method.
     * 
     * <p>
     * <code>CancelTimerListener.timerCancel</code> events may be 
     * called concurrently with any <code>TimerListener.timerExpired</code>
     * methods. Proper thread synchronization techniques must be employed to 
     * ensure consistency.
     * 
     * <p>
     * Once a Timer is canceled an application <b>must not</b> use
     * the Timer instance again.
     * 
     * @return <code>true</code> if this prevented the next execution 
     *             of this timer. <code>false</code> if this was already 
     *             canceled or had already expired in the one shot case.
     * @see commonj.timers.Timer#cancel()
     */
    public boolean cancel() {
        if (cancelled) {
            return false;
        }
        cancelled = true;
        return true;
    }

    /**
     * Returns the application-supplied TimerListener associated
     * with this Timer.
     * 
     * @return The TimerListener associated with the timer.
     * @throws IllegalStateException if the TimerManager has been stopped.
     * @see commonj.timers.Timer#getTimerListener()
     */
    public TimerListener getTimerListener() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Timer has already been stopped.");
        }
        return listener;
    }

    /**
     * Returns the next absolute <i>scheduled</i> execution time in 
     * milliseconds.
     * 
     * <p>
     * If invoked while a TimerListener is running, the return value is the 
     * <i>scheduled</i> execution time of the current TimerListener execution.
     * 
     * <p>
     * If the timer has been suspended, the time reflects the most 
     * recently-calculated execution time prior to being suspended.
     * 
     * @return the time in milliseconds at which the TimerListener is 
     *             scheduled to run next.
     * @throws IllegalStateException if the TimerManager has been stopped.
     * @see commonj.timers.Timer#getScheduledExecutionTime()
     */
    public long getScheduledExecutionTime() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Timer has already been stopped.");
        }
        return scheduledExcecutionTime;
    }

    /**
     * Return the period used to compute the time this timer will repeat. 
     * A value of zero indicates that the timer is non-repeating. 
     * 
     * @return the period in milliseconds between timer executions.
     * @see commonj.timers.Timer#getPeriod()
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Getter for canceled
     * @return Returns the canceled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        this.stopped = true;
        
        if (listener instanceof StopTimerListener) {
            StopTimerListener stl = (StopTimerListener) listener;
            stl.timerStop(this);
        }
    }

    /**
     * Check if this timer is expired and needs to be fired.
     * @return <code>true</code> if timer is expired.
     */
    public boolean isExpired() {
        // fix, change condition from >= to <=
        return scheduledExcecutionTime <= System.currentTimeMillis();
    }

}
