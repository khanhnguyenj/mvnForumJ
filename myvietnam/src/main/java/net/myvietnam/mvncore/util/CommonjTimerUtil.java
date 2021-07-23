/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/CommonjTimerUtil.java,v 1.11 2008/12/30 10:46:28 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.11 $
 * $Date: 2008/12/30 10:46:28 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Trung Tang  
 */
package net.myvietnam.mvncore.util;

import javax.naming.Context;
import javax.naming.InitialContext;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.thirdparty.commonj.timers.FooTimerManager;
import net.myvietnam.mvncore.thirdparty.commonj.util.ThreadPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commonj.timers.TimerManager;

public final class CommonjTimerUtil {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(CommonjTimerUtil.class);

    // static variable
    private static CommonjTimerUtil instance = null;

    // instance variables
    private TimerManager timerManager = null;

    // private constructor will prevent any instantiation
    private CommonjTimerUtil() {
        log.debug("CommonjTimerUtil is instantiated.");
        init();
    }
    
    public void start() {
        // do nothing
    }
    
    /**
     * This static method is used to get the Singleton instance of CommonjTimerUtil
     * @return the singleton instance of CommonjTimerUtil
     */
    public static synchronized CommonjTimerUtil getInstance() {
        if (instance == null) {
            instance = new CommonjTimerUtil();
        }
        return instance;
    }
    
    private void init() {
        String timerManagerDataSourceName = MVNCoreConfig.getTimerManagerDataSourceName();
        if ( (timerManagerDataSourceName != null) && (timerManagerDataSourceName.length() > 0) ) {
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                timerManager = (TimerManager) envCtx.lookup(timerManagerDataSourceName);
                log.info("TimerManager obtained successfully from JNDI resource.");
            } catch (Throwable e) {
                log.error("Cannot obtain TimerManager from JNDI resource.", e);
            }
        }
        if (timerManager == null) {
            // try to create TimerManager
            ThreadPool threadPool = new ThreadPool(ThreadPool.DEFAULT_MIN_THREADS, ThreadPool.DEFAULT_MAX_THREADS, ThreadPool.DEFAULT_QUEUE_LENGTH); 
            timerManager = new FooTimerManager(threadPool);
            log.info("Built-in TimerManager created because could not get from JNDI resource.");
        }
    }
    
    public TimerManager getTimerManager() {
        return timerManager;
    }

    public void stop() {
        if ( (timerManager != null) && (timerManager.isStopped() == false) ) {
            try {
                timerManager.stop();
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    public boolean isStopped() {
        return ( (timerManager == null) || timerManager.isStopped() );
    }

    public void waitForStop(long timeout_ms) {
        
        if (timeout_ms <= 0) {
            return;
        }
        
        if (timerManager != null) {
            long expireTime = System.currentTimeMillis() + timeout_ms;
            while ( (timerManager.isStopped() == false) && (System.currentTimeMillis() < expireTime) ) {
                try {
                    // each loop we wait for 100 ms
                    timerManager.waitForStop(100);
                } catch (IllegalArgumentException e) {
                    // ignore
                } catch (InterruptedException e) {
                    // ignore
                } catch (Throwable e) {
                    // ignore all runtime error of the TimerManager implementation 
                }
            }//while
        }//if
    }
}
