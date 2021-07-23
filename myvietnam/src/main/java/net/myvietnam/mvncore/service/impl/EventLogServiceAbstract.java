/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/EventLogServiceAbstract.java,v 1.4 2007/10/15 04:58:23 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.4 $
 * $Date: 2007/10/15 04:58:23 $
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
 * @author: Linh, Dang Hong
 */
package net.myvietnam.mvncore.service.impl;

import java.util.Locale;

import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.service.EventLogService;

public abstract class EventLogServiceAbstract implements EventLogService {
    
    public String getLogLevelDesc(int eventLogLevel, Locale locale) {
        
        String desc;
        switch (eventLogLevel) {
            case CRITICAL:
                desc = MVNCoreResourceBundle.getString(locale, "mvncore.eventlog.level.critical");
                break;
            
            case HIGH :
                desc = MVNCoreResourceBundle.getString(locale, "mvncore.eventlog.level.high");
                break;
            
            case MEDIUM:
                desc = MVNCoreResourceBundle.getString(locale, "mvncore.eventlog.level.medium");
                break;
            
            case LOW:
                desc = MVNCoreResourceBundle.getString(locale, "mvncore.eventlog.level.low");
                break;
                
            default:
                // should have a warning here
                desc = MVNCoreResourceBundle.getString(locale, "mvncore.eventlog.level.undefined");
        }
        return desc; 
        
    }
    
}
