/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/MvnCoreLifeCycleService.java,v 1.3 2008/01/18 09:34:56 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/01/18 09:34:56 $
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
package net.myvietnam.mvncore.service;

import javax.servlet.ServletContextEvent;

public interface MvnCoreLifeCycleService {

    /**
     * Notification that the web application initialization process is starting. All
     * ServletContextListeners are notified of context initialization before any filter
     * or servlet in the web application is initialized.
     * 
     * @param event
     */
    public void contextInitialized(ServletContextEvent event);
    
    /**
     * Notification that the servlet context is about to be shut down. All servlets and
     * filters have been destroy()ed before any ServletContextListeners are notified
     * of context destruction.
     * 
     * @param event
     */
    public void contextDestroyed(ServletContextEvent event);
}
