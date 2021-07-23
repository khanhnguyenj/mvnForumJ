/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/FooTimerManager.java,v 1.5 2008/05/21 02:52:44 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.5 $
 * $Date: 2008/05/21 02:52:44 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import commonj.timers.Timer;
import commonj.timers.TimerListener;
import commonj.timers.TimerManager;
import net.myvietnam.mvncore.thirdparty.commonj.util.ThreadPool;

/**
 * Implementation of the TimerManager.  
 * Each of the TimerManager schedule methods returns a Timer object.  
 * The returned Timer can then be queried and/or canceled. Applications 
 * are required to implement the TimerListener interface and may optionally 
 * implement one or both of the CancelTimerListener and StopTimerListener 
 * interfaces.
 * 
 * <p>
 * All Timers execute in the same JVM as the thread that created
 * the Timer and are transient. If the JVM fails then all Timers will be lost.
 * 
 * <p>
 * Recurring timers will execute their TimerListener multiple times.  
 * Invocations of the TimerListener are executed serially. That is, 
 * if a timer is scheduled to repeat every 5 seconds and a previous 
 * execution is still running, then the subsequent execution is delayed 
 * until the currently running execution completes.
 * 
 * <p>
 * A TimerListener scheduled multiple times, resulting in the return of 
 * multiple Timer instances, may execute concurrently depending on 
 * implementation. Proper thread safety techniques need to be employed.
 * 
 * <p> 
 * Two types of repeating timers are available:
 * <ul>
 * <li><strong>fixed-rate</strong> - 
 * TimerListeners are executed at a constant rate based on the 
 * initial scheduled execution time. Each subsequent execution time 
 * is based off of the period and the first execution time. 
 * This is appropriate for recurring activities that are sensitive to absolute 
 * time, such as ringing a chime every hour on the hour. 
 * 
 * <p>
 * If a fixed-rate execution is delayed for any reason (such as garbage collection, 
 * suspension, or other background activity), 
 * the late TimerListener will execute one time immediately, and then again at the next 
 * scheduled fixed-rate time.
 *
 * <p>
 * For example, if a fixed-rate timer was to execute every hour on the hour 
 * and missed 5 executions because the TimerManager was suspended for over 
 * 5 hours,  when the TimerManager is resumed, the TimerListener will 
 * run once immediately, and each execution thereafter will be on the hour.
 * 
 * <li><strong>fixed-delay</strong> - 
 * TimerListeners are executed at a constant delay based on the
 * actual execution time.  Each subsequent execution time is based off of the period 
 * and the end time of the previous execution.  The period is the delay in between 
 * TimerListener executions.
 * 
 * <p>
 * If a fixed-delay execution is delayed for any reason (such as garbage 
 * collection, suspension, or other background activity), subsequent executions 
 * will be delayed as well. In the long run, the frequency of execution will 
 * generally be slightly lower than the reciprocal of the specified period 
 * (assuming the system clock underlying Object.wait(long) is accurate). 
 * </ul>
 * 
 * @author Andreas Keldenich
 * @version 1.0
 * @see commonj.timers.TimerManager
 */
public final class FooTimerManager implements TimerManager, Runnable {

    private boolean stopped = false;
    private boolean stopping = false;
    private boolean suspended = false;
    private boolean suspending = false;

    private ThreadPool pool;
    private List timers = new ArrayList();
    private Thread timerThread = null;
    

    /**
     * Create a new TimerManager.
     * 
     * @param pool thread pool to execute timers in
     */
    public FooTimerManager(ThreadPool pool) {
        this.pool = pool;
        
        timerThread = new Thread(this);
        timerThread.start();
    }
    
    /**
     * Stop all timers and shutdown the thread pool.
     */
    public void shutdown() {
        // stop timers
        if (! stopped) {
            stop();
        }
        
        // shutdown pool
        pool.shutdown();
    }
    
    
    /**
     * Clean up.
     */
    protected void finalize() throws Throwable {
        shutdown();
    }

    
    /**
     * run method for the timer thread.
     * 
     * @see Runnable#run()
     */
    public void run() {
        
        // while we are not stopped...
        while (! stopped) {
            
            long nextTime = System.currentTimeMillis() + 1000;
            
            synchronized (this) {
                // iterate all timers and check if the need to be executed
                for (Iterator iter = timers.iterator(); iter.hasNext();) {
                    TimerExecutor timerExecutor = (TimerExecutor) iter.next();
                    FooTimer timer = timerExecutor.getTimer();
                    
                    // check if timer is expired
                    if (timer.isExpired() && ! timerExecutor.isRunning()) {
                        // execute timer if not suspended / suspending
                        if (! suspended && ! suspending) {
                            try {
                                timerExecutor.scheduleToRun();
                                pool.execute(timerExecutor);
                            }
                            catch (InterruptedException e) { // ignore
                            }
                        }
                        
                        // remove one shot timers after execution
                        if (timer instanceof OneShotTimer) {
                            iter.remove();
                        }
                    }
                    else {
                        // find the soonest execution time
                        long time = timer.getScheduledExecutionTime();
                        if (time < nextTime) {
                            nextTime = time;
                        }
                    }
                }
                
                // count running timers that are still running
                boolean running = false;
                for (Iterator iter = timers.iterator(); iter.hasNext();) {
                    TimerExecutor timerExecutor = (TimerExecutor) iter.next();
                    if (timerExecutor.isRunning()) {
                        running = true;
                    }
                }
                
                if (suspending && ! running) {
                    suspended = true;
                }
                
                if (stopping && ! running) {
                    for (Iterator iter = timers.iterator(); iter.hasNext();) {
                        TimerExecutor timerExecutor = (TimerExecutor) iter.next();
                        FooTimer timer = timerExecutor.getTimer();
                        timer.stop();
                    }
                    stopped = true;
                }
                
                // wait til next execution is due...
                long waitTime = nextTime - System.currentTimeMillis();
                if (waitTime > 0) {
                    try {
                        wait(waitTime);
                    }
                    catch (InterruptedException e) { // ignore
                    }
                }

            }
        }
        
    }
    

    /**
     * Returns <code>true</code> if all TimerListeners have completed 
     * following stop.
     *
     * @return <code>true</code> if all TimerListeners have completed 
     *         following stop
     * @see commonj.timers.TimerManager#isStopped()
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Returns <code>true</code> if this TimerManager is stopping or has 
     * been stopped.
     *
     * @return <code>true</code> if this TimerManager is stopping or has 
     *         been stopped
     * @see commonj.timers.TimerManager#isStopping()
     */
    public boolean isStopping() {
        return stopping;
    }

    /**
     * Returns <code>true</code> if this TimerManager is in the process
     * of or has completed suspending.
     *
     * @return <code>true</code> if this TimerManager has been suspended
     * @throws IllegalStateException if this TimerManager has been stopped.
     * @see commonj.timers.TimerManager#isSuspended()
     */
    public boolean isSuspended() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        return suspended;
    }

    /**
     * Returns <code>true</code> if this TimerManager is in the process
     * of or has completed suspending.
     *
     * @return <code>true</code> if this TimerManager has been suspended
     * @throws IllegalStateException if this TimerManager has been stopped.
     * @see commonj.timers.TimerManager#isSuspending()
     */
    public boolean isSuspending() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        return suspending;
    }

    /**
     * Resume the TimerManager. All scheduled timers that
     * have expired while suspended will execute immediately. All fixed-rate
     * timers will execute one time, and then again at the next 
     * scheduled fixed-rate time.
     * 
     * @throws IllegalStateException if this TimerManager has been stopped.
     * @see commonj.timers.TimerManager#resume()
     */
    public void resume() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        synchronized (this) {
            suspended = false;
            suspending = false;
            notifyAll();
        }
    }

    /**
     * Schedules a OneShotTimer to execute at a specified time. 
     * If the time is in the past, the TimerListener will execute immediately.
     *  
     * @param listener the TimerListener implementation to invoke when the timer expires.
     * @param time the time at which the timer will expire.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener or firstTime is null
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#schedule(commonj.timers.TimerListener, java.util.Date)
     * @see OneShotTimer
     */
    public Timer schedule(TimerListener listener, Date time)
            throws IllegalArgumentException, IllegalStateException {
        if (time == null) {
            throw new IllegalArgumentException("time is null.");
        }
        return scheduleOneShotTimer(listener, time.getTime());
    }

    /**
     * Schedules a TimerListener to execute once after the specified delay.
     * 
     * @param listener the TimerListener implementation to invoke when the Timer expires.
     * @param delay the number of milliseconds to wait before the Timer expires.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener is null or the delay is negative.
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#schedule(commonj.timers.TimerListener, long)
     */
    public Timer schedule(TimerListener listener, long delay)
            throws IllegalArgumentException, IllegalStateException {
        if (delay < 0) {
            throw new IllegalArgumentException("dealy is negative.");
        }
        return scheduleOneShotTimer(listener, System.currentTimeMillis() + delay);
    }

    /**
     * Schedules a TimerListener to execute repeatedly using <strong>fixed-delay</strong> 
     * execution after the <code>firstTime</code> elapses.
     * 
     * <p>
     * TimerListeners are executed at a constant delay based on the
     * actual execution time.  Each subsequent execution time is based off of the period 
     * and the end time of the previous execution.  The period is the delay in between 
     * TimerListener executions.
     * 
     * @param listener the TimerListener implementation to invoke when the Timer expires.
     * @param firstTime the time at which the first TimerListener will execute.
     * @param period the number of milliseconds between repeating expiration intervals.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener or firstTime is null or the period is negative.
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#schedule(commonj.timers.TimerListener, java.util.Date, long)
     */
    public Timer schedule(TimerListener listener, Date firstTime, long period)
            throws IllegalArgumentException, IllegalStateException {
        if (firstTime == null) {
            throw new IllegalArgumentException("firstTime is null.");
        }
        return scheduleFixedDelayTimer(listener, firstTime.getTime(), period);
    }

    /**
     * Schedules a TimerListener to execute repeatedly using <strong>fixed-delay</strong> 
     * execution after the specified delay.
     * 
     * <p>
     * TimerListeners are executed at a constant delay based on the
     * actual execution time. Each subsequent execution time is based off of the period 
     * and the end time of the previous execution. The period is the delay in between 
     * TimerListener executions.
     * 
     * @param listener the TimerListener implementation to invoke when the Timer expires.
     * @param delay the number of milliseconds to wait before the first Timer expires.
     * @param period the number of milliseconds between repeating expiration intervals.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener is null or the period or delay is negative.
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#schedule(commonj.timers.TimerListener, long, long)
     */
    public Timer schedule(TimerListener listener, long delay, long period)
            throws IllegalArgumentException, IllegalStateException {
        if (delay < 0) {
            throw new IllegalArgumentException("dealy is negative.");
        }
        return scheduleFixedDelayTimer(listener, System.currentTimeMillis() + delay, period);
    }

    /**
     * Schedules a TimerListener to execute repeatedly using <strong>fixed-rate</strong> 
     * execution after the <code>firstTime</code> elapses.
     * 
     * <p>
     * TimerListeners are executed at a constant rate based on the 
     * initial scheduled execution time.  Each subsequent execution time is based off of the period
     * and the first execution time.  This is appropriate for recurring 
     * activities that are sensitive to absolute time, such as ringing a chime
     * every hour on the hour. It is also appropriate for recurring activities 
     * where the total time to perform a fixed number of executions is important, such
     * as a count-down timer that ticks once every second for ten seconds.
     * 
     * @param listener the TimerListener implementation to invoke when the Timer expires.
     * @param firstTime the time at which the first timer will execute.
     * @param period the number of milliseconds between repeating expiration intervals.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener or firstTime is null or the period is negative.
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#scheduleAtFixedRate(commonj.timers.TimerListener, java.util.Date, long)
     */
    public Timer scheduleAtFixedRate(TimerListener listener, Date firstTime,
            long period) throws IllegalArgumentException, IllegalStateException {
        if (firstTime == null) {
            throw new IllegalArgumentException("firstTime is null.");
        }
        return scheduleFixedRateTimer(listener, firstTime.getTime(), period);
    }

    /**
     * Schedules a TimerListener to execute repeatedly using <strong>fixed-rate</strong> 
     * execution after the specified delay.
     * 
     * <p>
     * TimerListeners are executed at a constant rate based on the 
     * initial scheduled execution time. Each subsequent execution time is based off of the period
     * and the first execution time. This is appropriate for recurring 
     * activities that are sensitive to absolute time, such as ringing a chime
     * every hour on the hour. It is also appropriate for recurring activities 
     * where the total time to perform a fixed number of executions is important, such
     * as a count-down timer that ticks once every second for ten seconds.
     * 
     * @param listener the TimerListener implementation to invoke when the Timer expires.
     * @param delay the number of milliseconds to wait before the first Timer expires.
     * @param period the number of milliseconds between repeating expiration intervals.
     * @return the resulting Timer object.
     * @throws IllegalArgumentException thrown when the listener is null or the period or delay is negative.
     * @throws IllegalStateException thrown when the TimerManager has been stopped.
     * @see commonj.timers.TimerManager#scheduleAtFixedRate(commonj.timers.TimerListener, long, long)
     */
    public Timer scheduleAtFixedRate(TimerListener listener, long delay,
            long period) throws IllegalArgumentException, IllegalStateException {
        if (delay < 0) {
            throw new IllegalArgumentException("dealy is negative.");
        }
        return scheduleFixedRateTimer(listener, System.currentTimeMillis() + delay, period);
    }

    /**
     * Destroy the TimerManager. All Timers are stopped and all 
     * currently executing listeners are permitted to complete.
     * 
     * <p>
     * <code>StopTimerListener.stopTimer</code> events will be 
     * called serially with any <code>TimerListener.timerExpired</code>
     * methods.  They will not execute concurrently.  If a TimerListener
     * is executing, the <code>stopTimer</code> method will be called after the 
     * <code>timerExpired</code> method has completed.
     * 
     * <p>
     * <code>CancelTimerListener.timerCancel</code> events do not execute
     * as a result of this method.
     * 
     * <p>
     * Once the a timer manager has been stopped, it can never be re-started.
     * Attempts to schedule a TimerListener, and resume or suspend the TimerManager
     * will result in a thrown IllegalStateException.
     * 
     * @throws IllegalStateException If this TimerManager was already stopped.
     * @see commonj.timers.TimerManager#stop()
     */
    public void stop() throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        
        synchronized (this) {
            stopping = true;
            notifyAll();
        }
    }

    /**
     * Suspend the TimerManager.
     * 
     * <p>
     * Execution of listeners for timers that expire while the TimerManager 
     * is suspended is deferred until the TimerManager is resumed.
     * 
     * <p>
     * This method will return immediately.  Use the 
     * {@link TimerManager#isSuspended()} and {@link TimerManager#waitForSuspend(long)}
     * methods to determine the state of the suspend.
     * 
     * @throws IllegalStateException if this TimerManager has been stopped.
     * @see commonj.timers.TimerManager#suspend()
     */
    public void suspend() {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        
        synchronized (this) {
            suspending = true;
            notifyAll();
        }
    }

    /**
     * Blocks until all TimerListeners have completed execution after a stop
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeoutMs the maximum time to wait
     * @return <code>true</code> if this TimerManager stopped and <code>false</code> 
     *                 if the timeout elapsed before the stop completed
     * @throws InterruptedException if interrupted while waiting
     * @throws IllegalArgumentException thrown if the timeoutMs is negative.
     * @see commonj.timers.TimerManager#waitForStop(long)
     */
    public boolean waitForStop(long timeoutMs) throws InterruptedException,
            IllegalArgumentException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        if (timeoutMs < 0) {
            throw new IllegalArgumentException("timeout is negative.");
        }
        
        long endTime = System.currentTimeMillis() + timeoutMs;
        synchronized (this) {
            while (! stopped 
                    && timeoutMs != IMMEDIATE 
                    && System.currentTimeMillis() < endTime) {
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime > 0) {
                    wait(waitTime);
                }
            }
        }
        return stopped;
    }

    /**
     * Blocks until all TimerListeners have completed execution after a suspend
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeoutMs the maximum time to wait
     * @return <code>true</code> if this TimerManager suspended and <code>false</code>
     *                 if the timeout elapsed before the suspend completed
     * @throws InterruptedException if interrupted while waiting
     * @throws IllegalStateException if this TimerManager has been stopped.
     * @throws IllegalArgumentException thrown if the timeoutMs is negative.
     * @see commonj.timers.TimerManager#waitForSuspend(long)
     */
    public boolean waitForSuspend(long timeoutMs) throws InterruptedException,
            IllegalStateException, IllegalArgumentException {
        if (stopped) {
            throw new IllegalStateException("Already stopped.");
        }
        if (timeoutMs < 0) {
            throw new IllegalArgumentException("timeout is negative.");
        }
        
        long endTime = System.currentTimeMillis() + timeoutMs;
        synchronized (this) {
            while (! suspended 
                    && timeoutMs != IMMEDIATE 
                    && System.currentTimeMillis() < endTime) {
                long waitTime = endTime - System.currentTimeMillis();
                if (waitTime > 0) {
                    wait(waitTime);
                }
            }
        }
        return suspended;
    }
    
    /*
     * Schedule a one shot timer.
     */
    private FooTimer scheduleOneShotTimer(TimerListener listener, long time)
            throws IllegalStateException, IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }
        
        FooTimer timer = new OneShotTimer(time, listener);
        scheduleTimer(timer);
        return timer;
    }

    /*
     * Schedule a fixed-delay timer.
     */
    private FooTimer scheduleFixedDelayTimer(TimerListener listener, long time, 
            long period) throws IllegalStateException, IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }
        if (period < 1) {
            throw new IllegalArgumentException("period < 1.");
        }
        
        FooTimer timer = new FixedDelayTimer(time, period, listener);
        scheduleTimer(timer);
        return timer;
    }

    /*
     * Schedule a fixed-rate timer.
     */
    private FooTimer scheduleFixedRateTimer(TimerListener listener, long time, 
            long period) throws IllegalStateException, IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }
        if (period < 1) {
            throw new IllegalArgumentException("period < 1.");
        }
        
        FooTimer timer = new FixedRateTimer(time, period, listener);
        scheduleTimer(timer);
        return timer;
    }

    /*
     * Schedule a timer.
     */
    private synchronized void scheduleTimer(FooTimer timer) 
            throws IllegalStateException {
        if (stopped) {
            throw new IllegalStateException("TimerManager is stopped.");
        }
        timers.add(new TimerExecutor(timer, this));
        notifyAll();
    }

}
