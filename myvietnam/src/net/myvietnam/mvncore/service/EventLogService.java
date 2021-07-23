/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/EventLogService.java,v 1.11 2009/10/12 04:04:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2009/10/12 04:04:31 $
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
 * @author: Linh Dang 
 */
package net.myvietnam.mvncore.service;

import java.util.Locale;

/**
 * A service to log and tracking event caused by users. For example, when a user delete a forum, 
 * we should log it to later reference.
 * 
 * The default implementation of Opensource Edition is to log to file to SLF4J. The default 
 * implementation of Enterprise Edition is to stored in a database.
 */
public interface EventLogService {
    
    /**
     * The severity of the action is low, such as add a forum
     */
    public static final int LOW         = 1;
    
    /**
     * The severity of the action is medium, such as edit a member
     */
    public static final int MEDIUM      = 2;
    
    /**
     * The severity of the action is high, such as delete a member
     */
    public static final int HIGH        = 3;
    
    /**
     * The severity of the action is critical, such as delete a forum
     */
    public static final int CRITICAL    = 4;
    
    public void logEvent(String memberName, String eventLogIP, String eventLogModule, String eventLogSubModule,
                         String eventLogName, String eventLogDesc, int eventLogLevel);    
    
    /**
     * Get the description of the event log level for a locale
     * 
     * @param eventLogLevel the level of the event log
     * @param locale the locale to get the description
     * @return the description of the event log level for a locale
     */
    public String getLogLevelDesc(int eventLogLevel, Locale locale);
}
