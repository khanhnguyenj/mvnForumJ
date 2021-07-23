/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/MvnAdContextListener.java,v 1.5 2008/12/31 04:13:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.5 $
 * $Date: 2008/12/31 04:13:29 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
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
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumContextListener;
import com.mvnsoft.mvnad.service.MvnAdServiceFactory;

public class MvnAdContextListener extends MVNForumContextListener {

    private static final Logger log = LoggerFactory.getLogger(MvnAdContextListener.class);

    private static MvnAdContextListener instance;

    public MvnAdContextListener() {
        instance = this;
    }

    /**
     * Notification that the web application is ready to process requests.
     *
     * @param event ServletContextEvent
     */
    public void contextInitialized(ServletContextEvent event) {

        log.debug("contextInitialized");
        
        super.contextInitialized(event);

        MvnAdServiceFactory.getMvnAdService().getMvnAdLifeCycleService().contextInitialized(event);

    }

    /**
     * Notification that the Servlet context is about to be shut down.
     *
     * @param event ServletContextEvent
     */
    public void contextDestroyed(ServletContextEvent event) {
        
        MvnAdServiceFactory.getMvnAdService().getMvnAdLifeCycleService().contextDestroyed(event);
        
        super.contextDestroyed(event);

        instance = null;

        log.debug("contextDestroyed");
    }

    // below are add on method
    
    public static MVNForumContextListener getInstance() {
        return instance;
    }

}
