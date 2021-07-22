/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/MVNForumContextListener.java,v 1.23 2008/12/31 03:50:25 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.23 $
 * $Date: 2008/12/31 03:50:25 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum;

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

import com.mvnforum.service.MvnForumServiceFactory;

public class MVNForumContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(MVNForumContextListener.class);

    private static MVNForumContextListener instance;

    private Timestamp startTimestamp;

    public MVNForumContextListener() {
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
        MvnForumServiceFactory.getMvnForumService().getMvnForumLifeCycleService().contextInitialized(event);

        startTimestamp = DateUtil.getCurrentGMTTimestamp();
    }

    /**
     * Notification that the servlet context is about to be shut down.
     *
     * @param event ServletContextEvent
     */
    public void contextDestroyed(ServletContextEvent event) {

        MvnForumServiceFactory.getMvnForumService().getMvnForumLifeCycleService().contextDestroyed(event);
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

    public static MVNForumContextListener getInstance() {
        return instance;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

}
