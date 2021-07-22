/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/CategoryServiceImplDefault.java,v 1.19 2009/03/20 02:41:14 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.19 $
 * $Date: 2009/03/20 02:41:14 $
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
 * @author: Phuong, Pham Dinh Duy
 */
package com.mvnforum.service.impl;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.categorytree.CategoryTreeListener;
import com.mvnforum.categorytree.impl.*;
import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.ForumBean;
import com.mvnforum.service.CategoryService;

public class CategoryServiceImplDefault implements CategoryService {

    private static int count;

    public CategoryServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public CategoryTreeListener getManagementCategoryTree(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException {

        return new ForumManagementListenerImplDefault(request, response);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, boolean viewFullCategory)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response);
    }

    public CategoryTreeListener getManagementListAllForum(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException {

        return new ListAllForumImpl(request, response);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, String page, boolean viewFullCategory)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response, page);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, String page, int forum, int category)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response, page, forum, category);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, String page,int forumID)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response, page, forumID);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, boolean addForum, String selectName, boolean viewFullCategory)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response, addForum, selectName);
    }

    public CategoryTreeListener getManagementCategorySelector(GenericRequest request, GenericResponse response, ForumBean forumBean, boolean viewFullCategory)
        throws AuthenticationException, DatabaseException {

        return new CategorySelector(request, response, forumBean);
    }

    public ViewMemberPermissionsImpl getManagementViewMemberPermissions(GenericRequest request) {

        return new ViewMemberPermissionsImpl(request);
    }

    public CategoryTreeListener getManagementRSS(GenericRequest request, GenericResponse response, String sort, String order)
        throws DatabaseException, AuthenticationException {

        return new RSSImpl(request, response, sort, order);
    }

    public CategoryTreeListener getManagementModcp(GenericRequest request, GenericResponse response)
        throws DatabaseException, AuthenticationException {

        return new ModcpImpl(request, response);
    }

    public CategoryTreeListener getListForums(GenericRequest request, GenericResponse response, CategoryBean rootCategory, boolean otherInvisible, boolean show_sub_category)
        throws DatabaseException, AuthenticationException {

        return new ListForums(request, response);
    }

    public CategoryTreeListener getCategoryTreePath(GenericRequest request, GenericResponse response, int forumID, int categoryID, String display) 
        throws DatabaseException, ObjectNotFoundException, AuthenticationException {
        
        return new CategoryTreePath(request, response, forumID, categoryID, display);
    }

    public CategoryTreeListener getCategoryTreePath(GenericRequest request, GenericResponse response, int forumID, String mode, String action, String display) 
        throws DatabaseException, ObjectNotFoundException, AuthenticationException {
        
        return new CategoryTreePath(request, response, forumID, mode, action, display);
    }

}
