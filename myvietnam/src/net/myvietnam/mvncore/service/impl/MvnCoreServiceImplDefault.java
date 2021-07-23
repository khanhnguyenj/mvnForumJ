/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/MvnCoreServiceImplDefault.java,v 1.25 2009/05/20 07:19:19 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.25 $
 * $Date: 2009/05/20 07:19:19 $
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
 * @author: Trong Vo
 */
package net.myvietnam.mvncore.service.impl;

import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.AssertionUtil;

public class MvnCoreServiceImplDefault implements MvnCoreService {
    
    protected EncoderService          encoderService;
    protected URLResolverService      urlResolverService;
    protected EnvironmentService      environmentService;
    protected MvnCoreInfoService      mvnCoreInfoService;
    protected BinaryStorageService    binaryStorageService;
    protected MvnCoreLifeCycleService mvnCoreLifeCycleService;
    protected FileUploadParserService fileUploadParserService;
    protected EventLogService         eventLogService;
    protected IPFilterService         ipFilterService;
    protected SpellCheckerService     spellCheckerService;
    
    private static int count;
    
    public MvnCoreServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   
    
    public synchronized EncoderService getEncoderService() {
        if (encoderService == null) {
            encoderService = new EncoderServiceImplDefault() ;
        }
        return encoderService;
    }

    public synchronized URLResolverService getURLResolverService() {
        if (urlResolverService == null) {
            urlResolverService = new URLResolverServiceImplServletDefault();
        }
        return urlResolverService;
    }

    public synchronized EnvironmentService getEnvironmentService() {
        if (environmentService == null) {
            environmentService = new EnvironmentServiceImplDefault(EnvironmentService.PRODUCT_OPENSOURCE /*forum*/, EnvironmentService.PRODUCT_DISABLED /*cms*/, EnvironmentService.PRODUCT_DISABLED /*ad*/, false /*isPortlet*/, "default" /*customizeFor*/);
        }
        return environmentService;
    }

    public synchronized MvnCoreInfoService getMvnCoreInfoService() {
        if (mvnCoreInfoService == null) {
            mvnCoreInfoService = new MvnCoreInfoServiceImplDefault();
        }
        return mvnCoreInfoService;
    }

    public synchronized BinaryStorageService getBinaryStorageService() {
        if (binaryStorageService == null) {
            binaryStorageService = new BinaryStorageServiceImplEmpty(); 
        }
        return binaryStorageService;
    }

    public synchronized MvnCoreLifeCycleService getMvnCoreLifeCycleService() {
        if (mvnCoreLifeCycleService == null) {
            mvnCoreLifeCycleService = new MvnCoreLifeCycleServiceImplDefault();
        }
        return mvnCoreLifeCycleService;
    }

    public synchronized FileUploadParserService getFileUploadParserService() {
        if (fileUploadParserService == null) {
            fileUploadParserService =  new FileUploadParserServiceImplServlet();
        }
        return fileUploadParserService;
    }

    public synchronized EventLogService getEventLogService() {
        if (eventLogService == null) {
            eventLogService = new EventLogServiceImplDefault();
        }
        return  eventLogService;
    }

    public synchronized IPFilterService getIPFilterService() {
        if (ipFilterService == null) {
            ipFilterService = new IPFilterServiceImplDefault();
        }
        return ipFilterService;
    }

    public SpellCheckerService getSpellCheckerService() {
        return null;
    }

}
