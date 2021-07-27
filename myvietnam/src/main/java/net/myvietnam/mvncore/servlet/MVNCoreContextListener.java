/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/servlet/MVNCoreContextListener.java,v 1.1 2009/03/02 07:52:32 nguyendnc Exp $
 * $Author: nguyendnc $
 * $Revision: 1.1 $
 * $Date: 2009/03/02 07:52:32 $
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
 * @author: Minh Nguyen  
 */
package net.myvietnam.mvncore.servlet;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.*;

import net.myvietnam.mvncore.info.ServletInfo;
import net.myvietnam.mvncore.info.SystemInfo;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MVNCoreContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(MVNCoreContextListener.class);

    private static MVNCoreContextListener instance;

    private Timestamp startTimestamp;

    public MVNCoreContextListener() {
        instance = this;
    }

    /**
     * Notification that the web application is ready to process requests.
     *
     * @param event ServletContextEvent
     */
    public void contextInitialized(ServletContextEvent event) {

        log.debug("contextInitialized on " + new Date());
        
        SystemInfo systemInfo = new SystemInfo();
        log.info("Virtual Machine Name    : " + systemInfo.getVmName());
        log.info("Virtual Machine Vendor  : " + systemInfo.getVmVendor());
        log.info("Virtual Machine Version : " + systemInfo.getVmVersion());
        
        ServletContext context = event.getServletContext();
        
        ServletInfo servletInfo = new ServletInfo(context);
        log.info("Servlet Container Name    : " + servletInfo.getServerInfo());
        log.info("Servlet Container Version : " + servletInfo.getServletVersion());

        // We MUST initialize FileUtil's ServletClassesPath first before call any ServiceFactory
        // because ServiceFactory need the correct Servlet path first before it can be initialized
        String realPath = context.getRealPath("/WEB-INF/classes");// Add '/' before WEB-INF to fix the Oracle 10G bug
        FileUtil.setServletClassesPath(realPath);

        MvnCoreServiceFactory.getMvnCoreService().getMvnCoreLifeCycleService().contextInitialized(event);

        startTimestamp = DateUtil.getCurrentGMTTimestamp();
    }

    /**
     * Notification that the servlet context is about to be shut down.
     *
     * @param event ServletContextEvent
     */
    public void contextDestroyed(ServletContextEvent event) {

        MvnCoreServiceFactory.getMvnCoreService().getMvnCoreLifeCycleService().contextDestroyed(event);

        instance = null;

        try {
            //we will sleep 2 seconds, so the background thread of TimerUtil and WhirlyCache could be destroyed
            log.debug("About to sleep 2 seconds.");
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            //ignore
        }

        log.debug("contextDestroyed");
        log.debug("");// print an empty line
        
    }

    // below are add on method

    public static MVNCoreContextListener getInstance() {
        return instance;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

}