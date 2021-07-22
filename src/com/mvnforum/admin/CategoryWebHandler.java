/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/CategoryWebHandler.java,v 1.67 2009/05/04 08:44:11 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.67 $
 * $Date: 2009/05/04 08:44:11 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.admin;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.db.*;
import com.mvnforum.service.*;

public class CategoryWebHandler {

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();
    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();
    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    public CategoryWebHandler() {
    }

    public void prepareAdd(GenericRequest request, GenericResponse response)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddCategory();

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, CategoryTreeListener.SHOW_FULL_CATEGORY);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws BadInputException, CreateException, DatabaseException, DuplicateKeyException,
        ForeignKeyNotFoundException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddCategory();

        MyUtil.saveVNTyperMode(request, response);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        
        Locale locale = I18nUtil.getLocaleInRequest(request);

        int parentCategoryID = GenericParamUtil.getParameterUnsignedInt(request, "ParentCategoryID");

        if (parentCategoryID != 0) {
            try {
                DAOFactory.getCategoryDAO().findByPrimaryKey(parentCategoryID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(parentCategoryID)});
                throw new ObjectNotFoundException(localizedMessage);
            }
        }
        
        String categoryName             = GenericParamUtil.getParameterSafe(request, "CategoryName", true);
        categoryName = DisableHtmlTagFilter.filter(categoryName);
        String categoryDesc             = GenericParamUtil.getParameterSafe(request, "CategoryDesc", false);
        categoryDesc = DisableHtmlTagFilter.filter(categoryDesc);
        int categoryOption              = 0; // @todo review and support it later
        int categoryStatus              = 0; // @todo review and support it later

        try {
            DAOFactory.getCategoryDAO().findByAlternateKey_CategoryName(categoryName);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.categoryname_exists", new Object[] {categoryName});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ObjectNotFoundException ex) {
            
        }
        DAOFactory.getCategoryDAO().create(parentCategoryID, categoryName, categoryDesc,
                now/*categoryCreationDate*/, now/*categoryModifiedDate*/, 0/*categoryOrder*/,
                categoryOption, categoryStatus);
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.AddCategory", new Object[]{categoryName});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "add category", actionDesc, EventLogService.MEDIUM);

        // Now clear the cache
        CategoryCache.getInstance().clear();

        request.setAttribute("CategoryName", categoryName);
    }

    public void prepareDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanDeleteCategory();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int categoryID = GenericParamUtil.getParameterInt(request, "category");

        Collection forumsInCategory = DAOFactory.getForumDAO().getForums_inCategory(categoryID);
        if (forumsInCategory.isEmpty() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_not_empty_category");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete a not-empty category. Please delete all forums in this category first.");
        }

        CategoryBean categoryBean;
        try {
            categoryBean = DAOFactory.getCategoryDAO().getCategory(categoryID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        request.setAttribute("CategoryBean", categoryBean);
    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();

        permission.ensureCanDeleteCategory();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int categoryID = GenericParamUtil.getParameterInt(request, "category");

        Collection forumsInCategory = DAOFactory.getForumDAO().getForums_inCategory(categoryID);
        if (forumsInCategory.isEmpty() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_not_empty_category");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete a not-empty category. Please delete all forums in this category first.");
        }

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        /*
        try {
            // NOTE: implement it when we add table GroupCategory
            GroupCategoryWebHelper.deleteGroupCategory_inCategory(categoryID);
        } catch (ObjectNotFoundException ex) {}
        */
        DAOFactory.getWatchDAO().delete_inCategory(categoryID);

        try {
            DAOFactory.getCategoryDAO().delete(categoryID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.DeleteCategoryProcess", new Object[]{new Integer(categoryID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "delete category", actionDesc, EventLogService.HIGH);

        // Now clear the cache
        CategoryCache.getInstance().clear();
    }

    /*
     * @todo: check permission
     */
    public void prepareEdit(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditCategory();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        // primary key column(s)
        int categoryID  = GenericParamUtil.getParameterInt(request, "category");

        CategoryBean categoryBean;
        try {
            categoryBean = DAOFactory.getCategoryDAO().getCategory(categoryID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        request.setAttribute("CategoryBean", categoryBean);
    }

    /*
     * @todo: check permission
     */
    public void processUpdate(GenericRequest request, GenericResponse response)
        throws BadInputException, DatabaseException, DuplicateKeyException,
        AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditCategory();
        MyUtil.ensureCorrectCurrentPassword(request);
        
        MyUtil.saveVNTyperMode(request, response);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        // primary key column(s)
        int categoryID                  = GenericParamUtil.getParameterInt(request, "CategoryID");

        // column(s) to update
        String categoryName             = GenericParamUtil.getParameterSafe(request, "CategoryName", true);
        categoryName = DisableHtmlTagFilter.filter(categoryName);
        String categoryDesc             = GenericParamUtil.getParameterSafe(request, "CategoryDesc", false);
        categoryDesc = DisableHtmlTagFilter.filter(categoryDesc);
        int categoryOrder               = GenericParamUtil.getParameterUnsignedInt(request, "CategoryOrder");
        int categoryOption              = 0;// @todo review and support it later
        int categoryStatus              = 0;// @todo review and support it later

        try {
            DAOFactory.getCategoryDAO().update(categoryID, // primary key
                                     categoryName, categoryDesc, now/*categoryModifiedDate*/,
                                     categoryOrder, categoryOption, categoryStatus);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateCategory", new Object[]{new Integer(categoryID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update category", actionDesc, EventLogService.MEDIUM);

        // Now clear the cache
        CategoryCache.getInstance().clear();
    }

    /*
     * @todo: check permission
     */
    public void processUpdateCategoryOrder(GenericRequest request)
        throws BadInputException, DatabaseException,
        AuthenticationException, ObjectNotFoundException {
        
        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditCategory();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        // primary key column(s)
        int categoryID = GenericParamUtil.getParameterInt(request, "category");
        
        String action  = GenericParamUtil.getParameterSafe(request, "action", true);
        try {
            if (action.equals("up")) {
                DAOFactory.getCategoryDAO().decreaseCategoryOrder(categoryID, now);
            } else if (action.equals("down")) {
                DAOFactory.getCategoryDAO().increaseCategoryOrder(categoryID, now);
            } else {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_update_category.unknown_action", new Object[] {action});
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("Cannot update CategoryOrder: unknown action: " + action);
            }
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // Now clear the cache
        CategoryCache.getInstance().clear();
    }
}
