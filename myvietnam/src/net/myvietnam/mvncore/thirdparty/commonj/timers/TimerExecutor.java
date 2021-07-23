/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/thirdparty/commonj/timers/TimerExecutor.java,v 1.6 2008/05/21 02:52:44 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.6 $
 * $Date: 2008/05/21 02:52:44 $
 */
package net.myvietnam.mvncore.thirdparty.commonj.timers;

/**
 * Timer executor.
 *
 * @author  Kelly
 * @version 1.0
 */
public final class TimerExecutor implements Runnable {

    private boolean running = false;
    private FooTimer timer;
    private FooTimerManager timerManager;

    /**
     * Creates a new instance of TimerExecutor.
     */
    public TimerExecutor(FooTimer timer, FooTimerManager timerManager) {
        this.timer = timer;
        this.timerManager = timerManager;
    }

    /**
     * run method to execute a timer
     */
    public void run() {
        
        running = true;
        try {
            // minhnn: I added the try/cache here because we want computeNextExecutionTime() always be called
            try {
                // execute the timer
                timer.execute();
            } catch (Throwable e) {
                // ignore
            }
            
            // compute next execution time
            timer.computeNextExecutionTime();
        }
        catch (Exception e) { 
            // ignore
        }
        finally {
            running = false;
            // minhnn: because this thread does not own the monitor of object timerManager
            // so we cannot call notifyAll()
            //timerManager.notifyAll();
        }
    }

    /**
     * Getter for timer
     * @return Returns the timer.
     */
    public FooTimer getTimer() {
        return timer;
    }

    /**
     * Setter for timer
     * @param timer The timer to set.
     */
    public void setTimer(FooTimer timer) {
        this.timer = timer;
    }

    /**
     * Getter for timerManager
     * @return Returns the timerManager.
     */
    public FooTimerManager getTimerManager() {
        return timerManager;
    }

    /**
     * Setter for timerManager
     * @param timerManager The timerManager to set.
     */
    public void setTimerManager(FooTimerManager timerManager) {
        this.timerManager = timerManager;
    }

    /**
     * Is timer running
     * @return <code>true</code> if timer is running
     */
    public boolean isRunning() {
        return running;
    }
    
    public void scheduleToRun() {
        running = true;
    }

}
