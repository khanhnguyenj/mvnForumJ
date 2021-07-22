/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/ForumWebHandler.java,v 1.101 2009/07/02 10:44:59 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.101 $
 * $Date: 2009/07/02 10:44:59 $
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
package com.mvnforum.user;

import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.categorytree.impl.CategoryTreePath;
import com.mvnforum.common.OnlineUserUtil;
import com.mvnforum.db.*;
import com.mvnforum.service.*;

public class ForumWebHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ForumWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();
    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();
    
    public ForumWebHandler() {
    }

    public void prepareList(GenericRequest request, GenericResponse response, String requestURI)
        throws DatabaseException, AuthenticationException, MissingURLMapEntryException, BadInputException, ObjectNotFoundException {

        OnlineUserUtil.updateOnlineUserAction(request, requestURI);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);

        MVNForumPermission permission = onlineUser.getPermission();

        // Calculate to get the mosts
        long now = DateUtil.getCurrentGMTTimestamp().getTime();
        Timestamp since = new Timestamp(now - DateUtil.WEEK);

        if (MVNForumConfig.getEnableMostActiveMembers()) {
            Collection mostActiveMembers = PostCache.getInstance().getMostActiveMembers(since, MVNForumConfig.getMaxActiveMembers());

            request.setAttribute("MostActiveMembers", mostActiveMembers);
        }
        if (MVNForumConfig.getEnableMostActiveThreads()) {
            Collection mostActiveThreads = PostUtil.getMyMostActiveThreads(permission, since);

            request.setAttribute("MostActiveThreads", mostActiveThreads);
        }

        if (MVNForumConfig.getEnableSiteStatisticsOverview()) {
            if (MVNForumConfig.getEnableListNewMembersInRecentDays()) {
                int recentDays = MVNForumConfig.getDaysToShowRecentMembers();
                Timestamp fromDate = DateUtil.getCurrentGMTTimestampExpiredDay(-recentDays);
                Timestamp toDate = DateUtil.getCurrentGMTTimestamp();
                Collection membersInRecentDays = MyUtil.getNewMembersInRecentDays(fromDate, toDate, "MemberCreationDate", "DESC");
                request.setAttribute("MemberBeans", membersInRecentDays);
                
                Collection memberBeans = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(0, 1, "MemberID", "DESC", MemberDAO.ALL_MEMBER_STATUS);
                if (memberBeans.size() != 1) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.wrong_size", new Object[] {"MemberBeans", "==",new Integer(memberBeans.size())});
                    throw new AssertionError(localizedMessage);
                    //throw new AssertionError("Assertion: MemberBeans size == 1 (but the value = " + memberBeans.size() + ")");
                }
                
                MemberBean memberBean = (MemberBean) memberBeans.iterator().next();
                request.setAttribute("MemberBean", memberBean);
                
                int numberOfMembers = DAOFactory.getMemberDAO().getNumberOfMembers();
                request.setAttribute("NumberOfMembers", new Integer(numberOfMembers));
            }
        }

        // now set the attribute
        boolean duplicateUsers = MVNForumConfig.getEnableDuplicateOnlineUsers();
        request.setAttribute("OnlineUserActions", onlineUserManager.getOnlineUserActions(0 /*default*/, duplicateUsers)); // no permission

        int categoryId = GenericParamUtil.getParameterInt(request, "category", 0);
        boolean other_category = GenericParamUtil.getParameterBoolean(request, "other_category_invisible");
        boolean show_sub_category = GenericParamUtil.getParameterBoolean(request, "show_sub_category");
        
        if (categoryId != 0) {
            try {
                DAOFactory.getCategoryDAO().findByPrimaryKey(categoryId);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryId)});
                throw new ObjectNotFoundException(localizedMessage);
            }
        }

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, CategoryTreePath.FORUMID_IN_LIST_CATEGORY_CASE, categoryId, null);
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryBean category = null;
        if (categoryId != 0) {
            category = CategoryCache.getInstance().getBean(categoryId);
            tree.setRoot(category);
        }
        CategoryTreeListener listener = categoryService.getListForums(request, response, category, other_category, show_sub_category);
        tree.addCategeoryTreeListener(listener);

        request.setAttribute("Result", tree.build());

        if (tree.getID().equals("") == false) {
            request.setAttribute("id", tree.getID());
        }
    }

    public void prepareListIndex(GenericRequest request, GenericResponse response, String requestURI)
        throws DatabaseException, AuthenticationException, MissingURLMapEntryException, ObjectNotFoundException {

        OnlineUserUtil.updateOnlineUserAction(request, requestURI);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);

        MVNForumPermission permission = onlineUser.getPermission();

        ForumCache forumCache = ForumCache.getInstance();
        Collection forumBeans = forumCache.getBeans();
        Collection cTotal = new ArrayList();

        for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
            ForumBean forumBean = (ForumBean) forumIterator.next();

            Collection cThreadTemp = ThreadCache.getInstance().getNormalEnableThreads_inForum_withSortSupport_limit(forumBean.getForumID(), 0, 1, "ThreadLastPostDate", "DESC");
            if (cThreadTemp.size() == 0) {
                continue;
            } else if (cThreadTemp.size() > 1) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.wrong_size", new Object[] {"ThreadBeans", "<=",new Integer(cThreadTemp.size())});
                throw new AssertionError(localizedMessage);
                //throw new AssertionError("Assertion: ThreadBeans size <= 1 (but the value = " + cThreadTemp.size() + ")");
            }
            ThreadBean threadBean = (ThreadBean) (cThreadTemp.iterator().next());

            /*
            Collection cPostTemp = DAOFactory.getPostDAO().getLastEnablePosts_inThread_limit(threadBean.getThreadID(), 1);
            if (cPostTemp.size() != 1) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.wrong_size", new Object[] {"PostBeans", "==",new Integer(cPostTemp.size())});
                throw new AssertionError(localizedMessage);
                //throw new AssertionError("Assertion: PostBeans size == 1 (but the value = " + cPostTemp.size() + ")");
            }
            PostBean postBean = (PostBean) (cPostTemp.iterator().next());
            */
            PostBean postBean = PostCache.getInstance().getLastEnablePost_inThread(threadBean.getThreadID());

            Collection cGeneralInfo = new ArrayList(2);
            cGeneralInfo.add(postBean);
            cGeneralInfo.add(threadBean);
            cTotal.add(cGeneralInfo);
        }

        // Calculate to get the mosts
        long now = DateUtil.getCurrentGMTTimestamp().getTime();
        Timestamp since = new Timestamp(now - DateUtil.WEEK);

        if (MVNForumConfig.getEnableMostActiveMembers()) {
            Collection mostActiveMembers = PostCache.getInstance().getMostActiveMembers(since, MVNForumConfig.getMaxActiveMembers());

            request.setAttribute("MostActiveMembers", mostActiveMembers);
        }

        if (MVNForumConfig.getEnableMostActiveThreads()) {
            Collection mostActiveThreads = PostUtil.getMyMostActiveThreads(permission, since);

            request.setAttribute("MostActiveThreads", mostActiveThreads);
        }

        if (MVNForumConfig.getEnableSiteStatisticsOverview()) {
            if (MVNForumConfig.getEnableListNewMembersInRecentDays()) {
                int recentDays = MVNForumConfig.getDaysToShowRecentMembers();
                Timestamp fromDate = DateUtil.getCurrentGMTTimestampExpiredDay(-recentDays);
                Timestamp toDate = DateUtil.getCurrentGMTTimestamp();
                Collection membersInRecentDays = MyUtil.getNewMembersInRecentDays(fromDate, toDate, "MemberCreationDate", "DESC");
                request.setAttribute("MemberBeans", membersInRecentDays);
                
                Collection memberBeans = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(0, 1, "MemberID", "DESC", MemberDAO.ALL_MEMBER_STATUS);
                if (memberBeans.size() != 1) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.wrong_size", new Object[] {"MemberBeans", "==",new Integer(memberBeans.size())});
                    throw new AssertionError(localizedMessage);
                    //throw new AssertionError("Assertion: MemberBeans size == 1 (but the value = " + memberBeans.size() + ")");
                }
                
                MemberBean memberBean = (MemberBean) memberBeans.iterator().next();
                request.setAttribute("MemberBean", memberBean);
                
                int numberOfMembers = DAOFactory.getMemberDAO().getNumberOfMembers();
                request.setAttribute("NumberOfMembers", new Integer(numberOfMembers));
            }
        }

        // now set the attribute
        boolean duplicateUsers = MVNForumConfig.getEnableDuplicateOnlineUsers();
        request.setAttribute("OnlineUserActions", onlineUserManager.getOnlineUserActions(0 /*default*/, duplicateUsers)); // no permission
        request.setAttribute("LastPosts", cTotal);

        CategoryBuilder treebuilder =categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, CategoryTreePath.FORUMID_IN_LIST_CATEGORY_CASE, 0, null);
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementListAllForum(request, response);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

}
