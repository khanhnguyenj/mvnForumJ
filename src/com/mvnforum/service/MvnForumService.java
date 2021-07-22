/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/MvnForumService.java,v 1.28 2009/04/16 11:09:55 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.28 $
 * $Date: 2009/04/16 11:09:55 $
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
package com.mvnforum.service;

public interface MvnForumService {

    public SearchService getSearchService();

    public CategoryService getCategoryService();

    public CategoryBuilderService getCategoryBuilderService();

    public WatchMailService getWatchMailService();

    public ThreadBuilderService getThreadBuilderService();

    public MvnForumInfoService getMvnForumInfoService();
    
    public MvnForumAdService getMvnForumAdService();

    public ModuleProcessorService getModuleProcessorService();

    public MvnForumLifeCycleService getMvnForumLifeCycleService();

    public MvnForumCMSService getMvnForumCMSService();
    
    public CentralAuthenticationService getCentralAuthenticationService();
    
    public MvnForumDatabaseService getMvnForumDatabaseService();
    
    public VotePeriodBuilderService getVotePeriodBuilderService();

}
