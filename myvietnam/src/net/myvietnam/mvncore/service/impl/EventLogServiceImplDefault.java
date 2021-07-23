/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/EventLogServiceImplDefault.java,v 1.19 2008/12/30 10:46:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.19 $
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
 * @author: Linh Dang
 */
package net.myvietnam.mvncore.service.impl;

import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogServiceImplDefault extends EventLogServiceAbstract implements EventLogService {
    
    private static final Logger log = LoggerFactory.getLogger(EventLogServiceImplDefault.class);
    
    private static int count;
    
    public EventLogServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   
    
    public void logEvent(String memberName, String eventLogIP, String eventLogModule, String eventLogSubModule,
                         String eventLogName, String eventLogDesc, int eventLogLevel) {
        
        String logMessage = "User '" + memberName + "' from IP " + eventLogIP + " in module '" + eventLogModule + "' and sub module '" + eventLogSubModule + "' do action '" + eventLogName + "'. Description: " + eventLogDesc;  

        switch (eventLogLevel) {
            case EventLogService.CRITICAL:
                log.error(logMessage);
                break;
            
            case EventLogService.HIGH:
                log.warn(logMessage);
                break;
            
            case EventLogService.MEDIUM:
                log.info(logMessage);
                break;
            
            case EventLogService.LOW:
                log.trace(logMessage);
                break;
            
            default:
                AssertionUtil.doAssert(false, "Cannot process Event Logger with level = " + eventLogLevel);
        }        
    }   

}
