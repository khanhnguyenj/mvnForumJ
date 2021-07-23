/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/MvnCoreLifeCycleServiceImplDefault.java,v 1.22 2008/12/30 10:46:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.22 $
 * $Date: 2008/12/30 10:46:29 $
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
 * @author: Dung Bui
 */
package net.myvietnam.mvncore.service.impl;

import javax.servlet.ServletContextEvent;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.service.MvnCoreLifeCycleService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whirlycott.cache.CacheManager;

public class MvnCoreLifeCycleServiceImplDefault implements MvnCoreLifeCycleService {
    
    private static final Logger log = LoggerFactory.getLogger(MvnCoreLifeCycleServiceImplDefault.class);

    private static boolean called;
    
    private static int count;
    
    public MvnCoreLifeCycleServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }
    
    public static boolean isCalled() {
        return called;
    }
    
    public void contextInitialized(ServletContextEvent event) {
        
        called = true;

        // NOTE: the below code is commented out, but the idea is to automatically detect the context path
//        ServletContext context = event.getServletContext();
//        String contextPath = null;
//        String realpath = context.getRealPath("/WEB-INF");
//        ParamUtil.setContextPath(contextPath);
//        log.info("ParamUtil.setContextPath called with contextPath = " + contextPath);
        
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();
        CommonjTimerUtil.getInstance().start();
        
    }

    public void contextDestroyed(ServletContextEvent event) {

        // cancel the TimerUtil
        log.debug("About to cancel the TimerUtil");
        TimerUtil.getInstance().cancel();
        
        log.debug("About to stop the CommonjTimerUtil");
        CommonjTimerUtil.getInstance().stop();
        
        try {
            log.debug("About to shutdown the Whirly CacheManager");
            CacheManager.getInstance().shutdown();
        } catch (Throwable e) {
            log.error("Error", e);
        }

        // This code will release all connections currently pooled.
        // The next call to #getConnection will recreate the pool.
        try {
            DBUtils.closeAllConnections();
        } catch (Throwable e) {
            log.error("Error", e);
        }
        
        // we wait for maximum 1 minutes for all the task to finish
        CommonjTimerUtil.getInstance().waitForStop(DateUtil.MINUTE);

        // Note that we could consider to sleep 5 seconds to make sure all the background tasks is finished

    }
}
