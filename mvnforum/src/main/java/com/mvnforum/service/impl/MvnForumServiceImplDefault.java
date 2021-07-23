/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/MvnForumServiceImplDefault.java,v 1.31 2009/06/16 03:15:17 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
 * $Date: 2009/06/16 03:15:17 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.service.impl;

import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.service.*;

public class MvnForumServiceImplDefault implements MvnForumService {
    
    private static final Logger log = LoggerFactory.getLogger(MvnForumServiceImplDefault.class);

    protected SearchService                    searchService = null;
    protected CategoryService                  categoryService;
    protected CategoryBuilderService           categoryBuilderService;
    protected ThreadBuilderService             threadBuilderService;
    protected WatchMailService                 watchMailService;
    protected MvnForumInfoService              mvnForumInfoService;
    protected MvnForumAdService                mvnForumAdService;
    protected MvnForumCMSService               mvnForumCMSService;
    protected ModuleProcessorService           moduleProcessorService;
    protected MvnForumLifeCycleService         mvnForumLifeCycleService;
    protected CentralAuthenticationService     centralAuthenticationService;
    protected MvnForumDatabaseService          mvnForumDatabaseService;
    protected VotePeriodBuilderService         votePeriodBuilderService;

    private static int count;

    public MvnForumServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public synchronized SearchService getSearchService() {
        if (searchService == null) {
            searchService = new SearchServiceImplDisk();
        }
        return searchService;
    }

    public synchronized CategoryService getCategoryService() {
        if (categoryService == null) {
            categoryService = new CategoryServiceImplDefault();
        }
        return categoryService;
    }

    public synchronized CategoryBuilderService getCategoryBuilderService() {
        if (categoryBuilderService == null) {
            categoryBuilderService = new CategoryBuilderServiceImplDefault();
        }
        return categoryBuilderService;
    }
    
    public ThreadBuilderService getThreadBuilderService() {
        if (threadBuilderService == null) {
            threadBuilderService = new ThreadBuilderServiceImplDefault();
            // TODO berni a ThreadBuilderServiceImplDefault-ot
        }
        return threadBuilderService;
    }

    public synchronized WatchMailService getWatchMailService() {
        if (watchMailService == null) {
            watchMailService = new WatchMailServiceImplDefault();
        }
        return watchMailService;
    }

    public synchronized MvnForumInfoService getMvnForumInfoService() {
        if (mvnForumInfoService == null) {
            mvnForumInfoService = new MvnForumInfoServiceImplDefault();
        }
        return mvnForumInfoService;
    }

    public synchronized MvnForumAdService getMvnForumAdService() {
        if (mvnForumAdService == null) {
            String className = "com.mvnsoft.mvnad.service.impl.MvnForumAdServiceImplDefault";
            try {
                mvnForumAdService = (MvnForumAdService) Class.forName(className).newInstance();
                mvnForumAdService.reload();
            } catch (ClassNotFoundException e) {
                mvnForumAdService = new MvnForumAdServiceImplStub();
                log.info("Could not find class " + className + ". mvnAd is disabled.");
            } catch (InstantiationException e) {
                mvnForumAdService = new MvnForumAdServiceImplStub();
                log.error("Could not initiate class " + className + ". mvnAd is disabled.", e);
            } catch (IllegalAccessException e) {
                mvnForumAdService = new MvnForumAdServiceImplStub();
                log.error("Could not access class " + className + ". mvnAd is disabled.", e);
            }
        }
        return mvnForumAdService;
    }
    
    public synchronized MvnForumCMSService getMvnForumCMSService() {
        if (mvnForumCMSService == null) {
            String className = "com.mvnsoft.mvncms.service.impl.MvnForumCMSServiceImplDefault";
            try {
                mvnForumCMSService = (MvnForumCMSService) Class.forName(className).newInstance();
            } catch (Exception e) {
                mvnForumCMSService = new MvnForumCMSServiceImplStub();
                log.info("Could not find class " + className + ". mvnCMS is disabled.");
            }
        }
        return mvnForumCMSService;
    }
    
    public synchronized ModuleProcessorService getModuleProcessorService() {
        if (moduleProcessorService == null) {
            moduleProcessorService = new ModuleProcessorServiceImplDefault();
        }
        return moduleProcessorService;
    }

    public synchronized MvnForumLifeCycleService getMvnForumLifeCycleService() {
        if (mvnForumLifeCycleService == null) {
            mvnForumLifeCycleService = new MvnForumLifeCycleServiceImplDefault();
        }
        return mvnForumLifeCycleService;
    }

    public synchronized CentralAuthenticationService getCentralAuthenticationService() {
        if (centralAuthenticationService == null) {
            centralAuthenticationService = new CentralAuthenticationServiceImplStub();
        }
        return centralAuthenticationService;
    }
    
    public synchronized MvnForumDatabaseService getMvnForumDatabaseService() {
        if (mvnForumDatabaseService == null) {
            mvnForumDatabaseService = new MvnForumDatabaseServiceImplDefault();
        }
        return mvnForumDatabaseService;
    }
    
    public synchronized VotePeriodBuilderService getVotePeriodBuilderService() {
        if (votePeriodBuilderService == null) {
            votePeriodBuilderService = new VotePeriodBuilderServiceImplDefault();
        }
        return votePeriodBuilderService;
    }

}
