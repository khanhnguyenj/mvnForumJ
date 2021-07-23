/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/EnvironmentServiceImplDefault.java,v 1.14 2009/08/31 03:48:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/08/31 03:48:16 $
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

import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.util.DateUtil;

public class EnvironmentServiceImplDefault implements EnvironmentService {
    
    public static final String DEFAULT_REASON = "Normal System"; 
    public static final long   DEFAULT_RETRY_PERIOD = DateUtil.MINUTE * 5; 
    
    private boolean shouldRun = true;
    private String reason = DEFAULT_REASON;
    private long lastStopTime = 0;
    private int maxRetryCount = 0;
    private int currentRetryCount = 0;
    
    private int mvnForumOption;
    private int mvnCMSOption;
    private int mvnAdOption;
    private boolean isPortlet;
    private String customizeFor;
    
    public EnvironmentServiceImplDefault(int mvnForumOption, int mvnCMSOption, int mvnAdOption, 
                                         boolean isPortlet, String customizeFor) {
        if ( (mvnForumOption != PRODUCT_DISABLED) &&
             (mvnForumOption != PRODUCT_OPENSOURCE) &&
             (mvnForumOption != PRODUCT_ENTERPRISE) ) {
            throw new AssertionError("Assertion in EnvironmentServiceImplDefault constructor. Cannot accept mvnForumOption = " + mvnForumOption);
        }
        if ( (mvnCMSOption != PRODUCT_DISABLED) &&
             (mvnCMSOption != PRODUCT_ENTERPRISE) ) {
            throw new AssertionError("Assertion in EnvironmentServiceImplDefault constructor. Cannot accept mvnCMSOption = " + mvnCMSOption);
        }
        if ( (mvnAdOption != PRODUCT_DISABLED) &&
             (mvnAdOption != PRODUCT_OPENSOURCE) &&
             (mvnAdOption != PRODUCT_ENTERPRISE) ) {
            throw new AssertionError("Assertion in EnvironmentServiceImplDefault constructor. Cannot accept mvnAdOption = " + mvnAdOption);
        }
        
        this.mvnForumOption = mvnForumOption;
        this.mvnCMSOption = mvnCMSOption;
        this.mvnAdOption = mvnAdOption;
        this.isPortlet = isPortlet;
        this.customizeFor = customizeFor;
    }

    public int getForumRunMode() {
        return mvnForumOption;
    }
    
    public int getCmsRunMode() {
        return mvnCMSOption;
    }
    
    public int getAdRunMode() {
        return mvnAdOption;
    }
    
    public boolean isPortlet() {
        return isPortlet;
    }    

    public String customizeFor() {
        return customizeFor;
    }

    public void setShouldRun() {
        this.shouldRun = true;
        this.reason = DEFAULT_REASON;
    }

    public void setShouldStop(String reason) {
        setShouldStop(reason, 0);
    }

    public void setShouldStop(String reason, int retryCount) {
        this.shouldRun = false;
        this.reason = reason;
        this.lastStopTime = System.currentTimeMillis();
        if (maxRetryCount == 0) {
            this.maxRetryCount = retryCount;
        }
    }

    public synchronized boolean isShouldRun() {
        if (shouldRun == true) {
            return true;
        }
        
        // now, should run is false, we check if we can still retry
        if (currentRetryCount >= maxRetryCount) {
            return false;
        }
        
        // now, should run is false, and we can retry, we check if we pass the period yet?
        if ((System.currentTimeMillis() - lastStopTime) < DEFAULT_RETRY_PERIOD) {
            return false;
        }
        
        // now, should run is false, and we can retry, and it is the time to retry
        currentRetryCount++;
        shouldRun = true;
        
        return shouldRun;
    }    

    public String getReason() {
        return reason;
    }

    public void overloadEnvironment() {
    }

}
