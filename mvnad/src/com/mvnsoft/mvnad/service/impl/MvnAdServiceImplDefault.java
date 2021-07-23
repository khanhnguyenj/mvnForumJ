/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/service/impl/MvnAdServiceImplDefault.java,v 1.3 2008/07/26 01:29:47 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/07/26 01:29:47 $
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
package com.mvnsoft.mvnad.service.impl;

import net.myvietnam.mvncore.util.AssertionUtil;

import com.mvnsoft.mvnad.service.*;

public class MvnAdServiceImplDefault implements MvnAdService {
    
    protected MvnAdInfoService mvnAdInfoService                         = null;
    protected ExternalBannerService externalBannerService               = null;
    protected MvnAdModuleProcessorService mvnAdModuleProcessorService   = null;
    protected MvnAdLifeCycleService mvnAdLifeCycleService               = null;
    protected MvnAdTrackerService mvnAdTrackerService                   = null;

    private static int count;
    
    public MvnAdServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: MvnAdServiceImplDefault must have only one instance.");
    }
    
    public synchronized ExternalBannerService getExternalBannerService() {
        
        if (externalBannerService == null) {
            externalBannerService = new ExternalBannerServiceImplDefault();
        }
        
        return externalBannerService;
    }

    public synchronized MvnAdInfoService getMvnAdInfoService() {
        
        if (mvnAdInfoService == null) {
            mvnAdInfoService = new MvnAdInfoServiceImplDefault();
        }
        return mvnAdInfoService;
    }

    public synchronized MvnAdModuleProcessorService getMvnAdModuleProcessorService() {
        
        if (mvnAdModuleProcessorService == null) {
            mvnAdModuleProcessorService = new MvnAdModuleProcessorServiceImplDefault();
        }
        return mvnAdModuleProcessorService;
    }

    public synchronized MvnAdLifeCycleService getMvnAdLifeCycleService() {
        
        if (mvnAdLifeCycleService == null) {
            mvnAdLifeCycleService = new MvnAdLifeCycleServiceImplDefault();
        }
        return mvnAdLifeCycleService;
    }
    
    public synchronized MvnAdTrackerService getMvnAdTrackerService() {
        
        if (mvnAdTrackerService == null) {
            mvnAdTrackerService = new MvnAdTrackerServiceImplDefault();
        }
        return mvnAdTrackerService;
    }
}
