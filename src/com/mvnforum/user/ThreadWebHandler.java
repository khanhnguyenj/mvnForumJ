/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/ThreadWebHandler.java,v 1.164 2010/08/10 11:56:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.164 $
 * $Date: 2010/08/10 11:56:31 $
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
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.common.*;
import com.mvnforum.db.*;
import com.mvnforum.search.post.DeletePostIndexTask;
import com.mvnforum.search.post.PostIndexer;
import com.mvnforum.service.CategoryBuilderService;
import com.mvnforum.service.CategoryService;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.ThreadBuilderService;
import com.mvnforum.threadtree.ThreadTree;
import com.mvnforum.threadtree.ThreadTreeBuilder;

public class ThreadWebHandler {

    private static final Logger log = LoggerFactory.getLogger(ThreadWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    private static CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();

    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();
    
    private static ThreadBuilderService threadBuilderService     = MvnForumServiceFactory.getMvnForumService().getThreadBuilderService();

    public ThreadWebHandler() {
    }

    // NOTE that as presently, if user can delete post, then he could split the thread
    public void prepareSplit(GenericRequest request, GenericResponse response)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can split
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // int threadID = GenericParamUtil.getParameterInt(request, "thread");
        int postID = GenericParamUtil.getParameterInt(request, "post");
        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);
        } catch(ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        if (postBean.getParentPostID() == 0) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.first_post_cannot_be_split");
            throw new BadInputException(localizedMessage);
        }

        int forumID = postBean.getForumID();

        // permission.ensureCanSplitPost(forumID);
        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        permission.ensureCanDeletePost(forumID);

        int threadID = postBean.getThreadID();
        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        threadBean.ensureStatusCanEdit();

        // Collection postBeans = PostUtil.getSubPosts(threadID, postID);
        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);
        if (numberOfPosts == 1) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_be_split");
            throw new BadInputException(localizedMessage);
        }

        request.setAttribute("PostBean", postBean);

        ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);

        String display = MVNForumResourceBundle.getString(locale, "mvnforum.user.splitthread.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, display);
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "assignforumtogroup_movethread", 0);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    // NOTE that as presently, if user can delete post, then he could split the thread
    public void processSplit(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException,
        ForeignKeyNotFoundException, AuthenticationException, CreateException, InterceptorException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can split
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int postID = GenericParamUtil.getParameterInt(request, "post");
        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);
        } catch(ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        if (postBean.getParentPostID() == 0) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.first_post_cannot_be_split");
            throw new BadInputException(localizedMessage);
        }

        int threadID = postBean.getThreadID();
        int forumID = postBean.getForumID();

        permission.ensureCanDeletePost(forumID);

        ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);
/*
        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
*/
        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        int destForumID = GenericParamUtil.getParameterInt(request, "destforum");

        // now make sure that the dest forum is existed
        ForumBean destForumBean = ForumCache.getInstance().getBean(destForumID);
        destForumBean.ensureNotDisabledForum(locale);
        destForumBean.ensureNotClosedForum(locale);
        destForumBean.ensureNotLockedForum(locale);

        String threadTopic = GenericParamUtil.getParameter(request, "PostTopic");
        threadTopic = DisableHtmlTagFilter.filter(threadTopic);// always disable HTML
        threadTopic = InterceptorService.getInstance().validateContent(threadTopic);

        //create a new Thread
        int newThreadID = DAOFactory.getThreadDAO().createThread(destForumID, postBean.getMemberName(), postBean.getMemberName(), threadTopic, postBean.getPostBody(), 0, 0, postBean.getPostCreationDate(), postBean.getPostCreationDate(), 0, 0, 0, 0, 0, 0, 0, postBean.getPostIcon(), 0, 0);

        Stack stack = new Stack();
        Collection postFirstChildrenBeans = PostUtil.getChildrenPosts(postID);
        for (Iterator iterator = postFirstChildrenBeans.iterator(); iterator.hasNext();) {
            PostBean post = (PostBean) iterator.next();
            stack.add(post);
        }

        int postBeanParentID = postBean.getParentPostID();

        // update split post
        postBean.setParentPostID(0);
        postBean.setThreadID(newThreadID);
        postBean.setForumID(destForumID);
        DAOFactory.getPostDAO().update(postBean.getPostID(), postBean.getParentPostID(), postBean.getForumID(), postBean.getThreadID());

        String option = request.getParameter("option");
        if ("afterthis".equals(option)) {
            while (!stack.isEmpty()) {
                PostBean currentPost = (PostBean) stack.pop();
                int currentParentPostID = currentPost.getPostID();
                Collection postBeans = PostUtil.getChildrenPosts(currentParentPostID);
                for (Iterator iterator = postBeans.iterator(); iterator.hasNext();) {
                    PostBean post = (PostBean) iterator.next();
                    stack.add(post);
                }
                currentPost.setThreadID(newThreadID);
                currentPost.setForumID(destForumID);
                DAOFactory.getPostDAO().update(currentPost.getPostID(), currentPost.getParentPostID(), currentPost.getForumID(), currentPost.getThreadID());
            }
        } else if ("onlythis".equals(option)) {
            while (!stack.isEmpty()) {
                PostBean currentPost = (PostBean) stack.pop();
                DAOFactory.getPostDAO().update(currentPost.getPostID(), postBeanParentID, currentPost.getForumID(), currentPost.getThreadID());
            }
        }

        // now, update the statistics in the source thread and new thread
        // It updates these information: threadReplyCount, lastPostMemberName, threadLastPostDate
        StatisticsUtil.updateThreadStatistics(threadID);
        StatisticsUtil.updateThreadStatistics(newThreadID);

        // now, update the statistics in the source forum and dest forum
        StatisticsUtil.updateForumStatistics(forumID);
        StatisticsUtil.updateForumStatistics(destForumID);

        // now update the search index
        PostIndexer.scheduleUpdateThreadTask(threadID);
        PostIndexer.scheduleUpdateThreadTask(newThreadID);

        // Clear the cache
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.MoveThreadProcess", new Object[]{new Integer(threadID), new Integer(forumID), new Integer(destForumID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER, "split thread", actionDesc, EventLogService.MEDIUM);

        request.setAttribute("NewThreadID", String.valueOf(newThreadID));
    }

    public void prepareDelete(GenericRequest request, GenericResponse response)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        Locale locale = I18nUtil.getLocaleInRequest(request);

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);

        // check constraint
        int forumID = threadBean.getForumID();
        try {
            ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // check to make sure we have this member in database 
        String authorName = threadBean.getMemberName();
        try {
            MemberCache.getInstance().getMemberIDFromMemberName(authorName);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {authorName});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        // Note that this method will check the permission and 
        // check if this thread is too old, has replies, or is disabled
        ThreadChecker.checkDeleteThread(onlineUser, threadBean);

        int numberOfPendingPosts = DAOFactory.getPostDAO().getNumberOfDisablePosts_inThread(threadID);

        request.setAttribute("ThreadBean", threadBean);
        request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
        request.setAttribute("NumberOfPendingPosts", new Integer(numberOfPendingPosts));

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.deletethread.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/));

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processDelete(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();
        
        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        // primary key column(s)
        Locale locale = I18nUtil.getLocaleInRequest(request);

        int threadID = GenericParamUtil.getParameterInt(request, "thread");
        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        try {
            ForumCache.getInstance().getBean(threadBean.getForumID()).ensureNotDisabledForum(locale);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(threadBean.getForumID())});
            throw new ObjectNotFoundException(localizedMessage);
        }

        deleteThread(request, threadBean);

        //now, update the statistics in the forum and member
        int forumID = threadBean.getForumID();
        StatisticsUtil.updateForumStatistics(forumID);

        // Clear the cache
        ForumCache.getInstance().clear();
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.DeleteThreadProcess", new Object[] {new Integer(threadID),new Integer(forumID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER, "delete thread", actionDesc, EventLogService.HIGH);

        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    // Note that this method does not update the forum statistics
    // The caller must call method to update the forum statistics
    public void deleteThread(GenericRequest request, ThreadBean threadBean)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();
        
        // Note that this method will check the permission and 
        // check if this post is too old, has replies, or is disabled
        ThreadChecker.checkDeleteThread(onlineUser, threadBean);
        
        // primary key column(s)
        int threadID = threadBean.getThreadID();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // now, check the permission
        String authorName   = threadBean.getMemberName();
        int authorID        = 0;
        try {
            authorID = MemberCache.getInstance().getMemberIDFromMemberName(authorName);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {authorName});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        // Delete all attachments in this thread,
        // we must call this before any attempt to delete the thread
        // That is, the order when delete is VERY IMPORTANT
        AttachmentWebHandler.deleteAttachments_inThread(threadID);

        DAOFactory.getFavoriteThreadDAO().delete_inThread(threadID);

        DAOFactory.getWatchDAO().delete_inThread(threadID);

        DAOFactory.getPostDAO().delete_inThread(threadID);

        // now delete the thread, note that we delete it after delete all child objects
        DAOFactory.getThreadDAO().delete(threadID);

        // now update the search index
        // @todo : what if the above methods throw exception, then no thread is deleted from index ???
        PostIndexer.scheduleDeletePostTask(threadID, DeletePostIndexTask.OBJECT_TYPE_THREAD);

        //now, update the statistics in the member
        StatisticsUtil.updateMemberStatistics(authorID);
    }

    public void deleteSuccessForRender(GenericRequest request, GenericResponse response) 
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {

        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        int forumID = (Integer.valueOf((String)request.getAttribute("ForumID"))).intValue();

        String deleteSuccessLabel = MVNForumResourceBundle.getString(locale, "mvnforum.user.deletethreadsuccess.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, deleteSuccessLabel);
        categorytree.addCategeoryTreeListener(treelistener);

        request.setAttribute("tree", categorytree.build());
    }

    /**
     * Need some help here
     * zt.add.090817
     * @param request
     * @param response
     */
    public void prepareMergeThread(GenericRequest request, GenericResponse response)  throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {
        
        log.debug("prepareMergeThread starts");
        OnlineUser             onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission     permission = onlineUser.getPermission();
        Locale                 locale         = I18nUtil.getLocaleInRequest(request);
    
        int                 forumID     = -1;
        // primary key column(s)
        int                 threadID     = GenericParamUtil.getParameterInt(request, "thread");
        String                filter        = GenericParamUtil.getParameter(request, "filterCriteria");
    
        ThreadBean             threadBean     = null;
        try {
            threadBean                     = DAOFactory.getThreadDAO().getThread(threadID);
            forumID                     = threadBean.getForumID();
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
    
        ForumBean forumBean             = ForumCache.getInstance().getBean(threadBean.getForumID());
        forumBean.ensureNotDisabledForum(locale);
    
        // now, check the permission
        // @todo: is it the correct permission ???
        permission.ensureCanDeletePost(threadBean.getForumID());
    
        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);
    
        log.debug("numberOfPosts:: " + numberOfPosts);
        request.setAttribute("ThreadBean", threadBean);
        request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
    
        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.mergethread.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/));
    
        // we should put a comment here
        CategoryBuilder         treebuilder     = categoryBuilderService.getCategoryTreeBuilder();
        log.debug("CategoryTree");
        CategoryTree             categorytree     = new CategoryTree(treebuilder);
        log.debug("CategoryTreeListener");
        CategoryTreeListener     treelistener     = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());
    
        // we should put a comment here
        ThreadTreeBuilder        builder            = threadBuilderService.getThreadTreeBuilder(forumID, filter);
        log.debug("ThreadTree");
        ThreadTree                 tree             = new ThreadTree(builder, threadID);
        log.debug("ThreadTree ok");
        request.setAttribute("Result", tree.build(locale));
        log.debug("prepareMergeThread ends");
    }


    /**
     * zt.add.080918
     * @param request
     * @throws BadInputException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     * @throws AuthenticationException
     */
    public void processMergeThread(GenericRequest request) throws BadInputException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException, AuthenticationException {
        SecurityUtil.checkHttpPostMethod(request);
    
        OnlineUser             onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission     permission = onlineUser.getPermission();
    
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();
        Locale                 locale         = I18nUtil.getLocaleInRequest(request);
        // primary key column(s)
        int                 threadID     = GenericParamUtil.getParameterInt(request, "thread");
    
        // now, check the permission
        ThreadBean             threadBean     = null;
        try {
            threadBean                             = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String             localizedMessage     = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int                 forumID     = threadBean.getForumID();
        permission.ensureCanDeletePost(forumID);
        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    
        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);
        int                 destThreadID = GenericParamUtil.getParameterInt(request, "destthread");
        
    
        // make sure that we don't move to the same forum (meaningless)
        if (destThreadID == threadID) {
            String             localizedMessage    = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_move_thread_to_the_same_forum");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot move thread to the same forum.");
        }

        ThreadBean destThreadBean = null;
        try {
            destThreadBean = DAOFactory.getThreadDAO().getThread(destThreadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int    destForumID    = destThreadBean.getForumID();
    
//        // now make sure that the dest forum is existed
        ForumCache.getInstance().getBean(forumID);

//        DAOFactory.getThreadDAO().updateForumID(threadID, forumID);
//        DAOFactory.getPostDAO().update_ForumID_inThread(threadID, forumID);
//        DAOFactory.getFavoriteThreadDAO().update_ForumID_inThread(threadID, forumID);
        
        // tadadammmmm
        DAOFactory.getPostDAO().mergeThreads(threadID, destThreadID);
        DAOFactory.getThreadDAO().delete(threadID);
        
        DAOFactory.getFavoriteThreadDAO().delete_inThread(threadID);
        DAOFactory.getWatchDAO().delete_inThread(threadID);
        DAOFactory.getPostDAO().delete_inThread(threadID);

        DAOFactory.getThreadDAO().updateForumID(destThreadID, destForumID);
//        DAOFactory.getPostDAO().update_ForumID_inThread(threadID, destForumID);
        DAOFactory.getFavoriteThreadDAO().update_ForumID_inThread(destThreadID, destForumID);

        //now, update the statistics
        StatisticsUtil.updateForumStatistics(forumID);
        StatisticsUtil.updateForumStatistics(destForumID);

        // now update the search index
        // @todo : what if the above methods throw exception, then no thread is deleted from index ???
        PostIndexer.scheduleDeletePostTask(threadID, DeletePostIndexTask.OBJECT_TYPE_THREAD);

        // Clear the cache
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.MergeThreadProcess", new Object[]{new Integer(threadID),new Integer(destThreadID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER, "move thread", actionDesc, EventLogService.MEDIUM);
    
        request.setAttribute("ThreadID", String.valueOf(destThreadID));
        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    public void prepareMoveThread(GenericRequest request, GenericResponse response)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        int forumID = -1;
        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
            forumID = threadBean.getForumID();
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        ForumBean forumBean = ForumCache.getInstance().getBean(threadBean.getForumID());
        forumBean.ensureNotDisabledForum(locale);

        // now, check the permission
        // @todo: is it the correct permission ???
        permission.ensureCanDeletePost(threadBean.getForumID());

        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);

        request.setAttribute("ThreadBean", threadBean);
        request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.movethread.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/));

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());


        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "assignforumtogroup_movethread", forumID);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void processMoveThread(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException,
        ForeignKeyNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        // now, check the permission
        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int forumID = threadBean.getForumID();
        permission.ensureCanDeletePost(forumID);

        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        int destForumID = GenericParamUtil.getParameterInt(request, "destforum");

        // make sure that we don't move to the same forum (meaningless)
        if (destForumID == forumID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_move_thread_to_the_same_forum");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot move thread to the same forum.");
        }

        // now make sure that the dest forum is existed
        ForumCache.getInstance().getBean(destForumID);

        DAOFactory.getThreadDAO().updateForumID(threadID, destForumID);
        DAOFactory.getPostDAO().update_ForumID_inThread(threadID, destForumID);
        DAOFactory.getFavoriteThreadDAO().update_ForumID_inThread(threadID, destForumID);

        //now, update the statistics in the source forum and dest forum
        StatisticsUtil.updateForumStatistics(forumID);
        StatisticsUtil.updateForumStatistics(destForumID);

        // now update the search index
        // @todo : what if the above methods throw exception, then no thread is deleted from index ???
        PostIndexer.scheduleUpdateThreadTask(threadID);

        // Clear the cache
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post
        ForumCache.getInstance().clear();// affect the thread count in a forum in the index page

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.MoveThreadProcess", new Object[]{new Integer(threadID),new Integer(forumID),new Integer(destForumID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER, "move thread", actionDesc, EventLogService.MEDIUM);

        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    public void prepareEditThreadStatus(GenericRequest request, GenericResponse response)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        ForumBean forumBean = ForumCache.getInstance().getBean(threadBean.getForumID());
        forumBean.ensureNotDisabledForum(locale);

        // now, check the permission
        permission.ensureCanModerateThread(threadBean.getForumID());

        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);

        int numberOfPendingPosts = DAOFactory.getPostDAO().getNumberOfDisablePosts_inThread(threadID);

        request.setAttribute("ThreadBean", threadBean);
        request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
        request.setAttribute("NumberOfPendingPosts", new Integer(numberOfPendingPosts));

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.editthreadstatus.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/));

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processEditThreadStatus(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can edit thread status
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        // now, check the permission
        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int forumID = threadBean.getForumID();

        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);

        permission.ensureCanModerateThread(forumID);

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        int threadStatus = GenericParamUtil.getParameterInt(request, "ThreadStatus");

        ThreadBean.validateThreadStatus(threadStatus);

        // now if change from Disable to Enable, update the lastpostdate so
        // that the Watch will see this thread as a new thread
        if ((threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED) &&
            (threadStatus != ThreadBean.THREAD_STATUS_DISABLED)) {
            Timestamp now = DateUtil.getCurrentGMTTimestamp();
            DAOFactory.getThreadDAO().updateLastPostDate(threadID, now);
        }

        DAOFactory.getThreadDAO().updateThreadStatus(threadID, threadStatus);
        StatisticsUtil.updateForumStatistics(forumID);
        //@todo: should update other info ???

        // Clear the cache
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post

        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    public void prepareEditThreadType(GenericRequest request, GenericResponse response)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        ForumBean forumBean = ForumCache.getInstance().getBean(threadBean.getForumID());
        forumBean.ensureNotDisabledForum(locale);

        // now, check the permission
        permission.ensureCanModerateThread(threadBean.getForumID());

        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);

        int numberOfPendingPosts = DAOFactory.getPostDAO().getNumberOfDisablePosts_inThread(threadID);

        request.setAttribute("ThreadBean", threadBean);
        request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
        request.setAttribute("NumberOfPendingPosts", new Integer(numberOfPendingPosts));

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.editthreadtype.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/));

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processEditThreadType(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can edit thread status
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int threadID = GenericParamUtil.getParameterInt(request, "thread");
        int threadType = GenericParamUtil.getParameterUnsignedInt(request, "ThreadType", ThreadBean.THREAD_TYPE_DEFAULT);

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int forumID = threadBean.getForumID();

        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);

        if (threadType > ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT /* 3 */) {
            throw new BadInputException("Not support this thread type");
        }

        if ((threadType == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT) ||
            (threadBean.getThreadType() == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT)) {
            permission.ensureCanAdminSystem();
        } else {
            permission.ensureCanModerateThread(forumID);
        }

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        DAOFactory.getThreadDAO().updateThreadType(threadID, threadType);
        //@todo: should update other info ???

        // Clear the cache
        ThreadCache.getInstance().clear();
        PostCache.getInstance().clear();// affect mostActiveThreads in Post

        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    public void prepareList_limit(GenericRequest request, GenericResponse response, String requestURI)
        throws AuthenticationException, DatabaseException, BadInputException, MissingURLMapEntryException, ObjectNotFoundException {

        OnlineUserUtil.updateOnlineUserAction(request, requestURI);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        int forumID = GenericParamUtil.getParameterInt(request, "forum");

        // make sure there is the forum
        // will throw an BadInputException if there is not this forum
        ForumBean forumBean = null;
        try {
            forumBean = ForumCache.getInstance().getBean(forumID);
        } catch (ObjectNotFoundException e1) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, 0, null);
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

        forumBean.ensureNotDisabledForum(locale);

        // make sure user can read post in this forum
        permission.ensureCanReadPost(forumID);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = ThreadCache.getInstance().getNumberOfEnableThreads_inForum(forumID);
        int totalNormalThreads = ThreadCache.getInstance().getNumberOfNormalEnableThreads_inForum(forumID);
        if (offset > totalNormalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = ThreadCache.getInstance().getNormalEnableThreads_inForum_withSortSupport_limit(forumID, offset, postsPerPage, sort, order);

        // the correct value is the enable posts - disable threads, so we could get directly from the ForumBean
        //int totalPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inForum(forumID);
        int totalPosts = forumBean.getForumPostCount();

        int pendingThreadCount = 0;
        int threadsWithPendingPostsCount = 0;
        if (permission.canModerateThread(forumID)) {
            pendingThreadCount = DAOFactory.getThreadDAO().getNumberOfDisableThreads_inForum(forumID);
            threadsWithPendingPostsCount = DAOFactory.getThreadDAO().getNumberOfEnableThreadsWithPendingPosts_inForum(forumID);
        }

        Collection allThreadBeans = new ArrayList();

        Collection globalAnnounces = ThreadCache.getInstance().getEnableGlobalAnnouncements();
        allThreadBeans.addAll(globalAnnounces);
        Collection announcements = ThreadCache.getInstance().getEnableForumAnnouncements_inForum(forumID);
        allThreadBeans.addAll(announcements);
        if (offset == 0) {
            Collection stickies = ThreadCache.getInstance().getEnableStickies_inForum(forumID);
            allThreadBeans.addAll(stickies);
        }
        allThreadBeans.addAll(threadBeans);

        request.setAttribute("ThreadBeans", allThreadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));
        request.setAttribute("TotalNormalThreads", new Integer(totalNormalThreads));
        request.setAttribute("TotalPosts", new Integer(totalPosts));
        request.setAttribute("PendingThreadCount", new Integer(pendingThreadCount));
        request.setAttribute("ThreadsWithPendingPostsCount", new Integer(threadsWithPendingPostsCount));

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "viewthread", forumID);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());

        if (MVNForumConfig.getEnableOnlineUsers()) {
            OnlineUserUtil.setRequestAttributeOfOnlineActions(request, Action.PAGE_ID_LISTTHREADS, new Integer(forumID));
        }

        if (MVNForumConfig.getEnableEasyWatching()) {
            // check whether this forum is being watched
            Boolean isWatched = Boolean.FALSE;
            if (onlineUser.isMember()) {
                isWatched = Boolean.valueOf(WatchUtil.isForumWatched(onlineUser.getMemberID(), forumBean));
            }
            request.setAttribute("isWatched", isWatched);
        }
    }

    public void prepareListRecentThreads_limit(GenericRequest request,GenericResponse response)
        throws DatabaseException, BadInputException,
        AuthenticationException, ObjectNotFoundException, DatabaseException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = DAOFactory.getThreadDAO().getNumberOfEnableThreads(false, -1, -1, -1);
        if (offset > totalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = DAOFactory.getThreadDAO().getEnableThreads_withSortSupport_limit(offset, postsPerPage, sort, order, false, -1, -1, -1);

        // now remove thread that current user does not have permission
        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            int currentForumID = threadBean.getForumID();
            if (permission.canReadPost(currentForumID) == false) {
                iterator.remove();
            } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                iterator.remove();
            }
        }

        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "listrecentthreads", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void prepareListRecentDisabledThreads_limit(GenericRequest request)
        throws DatabaseException, BadInputException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can view pending/disabled threads
        permission.ensureIsAuthenticated();
        permission.ensureCanModerateThreadInAtLeastOneForum();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = DAOFactory.getThreadDAO().getNumberOfDisableThreads();
        if (offset > totalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = DAOFactory.getThreadDAO().getDisableBeans_withSortSupport_limit(offset, postsPerPage, sort, order);

        // now remove thread that current user does not have permission
        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            int currentForumID = threadBean.getForumID();
            if (permission.canModerateThread(currentForumID) == false) {
                iterator.remove();
            } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                iterator.remove();
            }
        }

        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));
    }

    public void prepareListDisabledThreads_limit_xml(GenericRequest request)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can view pending/disabled threads
        permission.ensureIsAuthenticated();
        permission.ensureCanModerateThreadInAtLeastOneForum();

        Collection pendingThreadBeans = DAOFactory.getThreadDAO().getDisableBeans_withSortSupport_limit(0, 10000, "ThreadLastPostDate", "DESC");
        // now remove thread that current user does not have permission
        for (Iterator iterator = pendingThreadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            if (permission.canModerateThread(threadBean.getForumID()) == false) {
                iterator.remove();
            }
        }

        Collection threadWithPendingPostsBeans = DAOFactory.getThreadDAO().getEnableThreadsWithPendingPosts_withSortSupport_limit(0, 10000, "ForumID", "DESC");
        for (Iterator iterator = threadWithPendingPostsBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            if (permission.canModerateThread(threadBean.getForumID()) == false) {
                iterator.remove();
            } else {
                Collection pendingPosts = DAOFactory.getPostDAO().getDisablePosts_inThread_limit(threadBean.getThreadID(), 0, 10000);
                threadBean.setPendingPosts(pendingPosts);
            }
        }

        request.setAttribute("PendingThreadBeans", pendingThreadBeans);
        request.setAttribute("ThreadWithPendingPostsBeans", threadWithPendingPostsBeans);
    }

    public void prepareListRecentEnableThreadsWithPendingPosts_limit(GenericRequest request)
        throws DatabaseException, BadInputException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can view enable threads with pending posts
        permission.ensureIsAuthenticated();
        permission.ensureCanModerateThreadInAtLeastOneForum();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = DAOFactory.getThreadDAO().getNumberOfEnableThreadsWithPendingPosts();
        if (offset > totalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = DAOFactory.getThreadDAO().getEnableThreadsWithPendingPosts_withSortSupport_limit(offset, postsPerPage, sort, order);

        // now remove thread that current user does not have permission
        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            int currentForumID = threadBean.getForumID();
            if (permission.canModerateThread(currentForumID) == false) {
                iterator.remove();
            } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                iterator.remove();
            }
        }

        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));
    }

    public void prepareListEnableThreadsWithPendingPosts_inForum_limit(GenericRequest request)
        throws DatabaseException, BadInputException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        // user must have been authenticated before he can view enable threads with pending posts
        permission.ensureIsAuthenticated();

        int forumID = GenericParamUtil.getParameterInt(request, "forum");
        permission.ensureCanModerateThread(forumID);

        try {
            ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        } catch (ObjectNotFoundException e1) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = DAOFactory.getThreadDAO().getNumberOfEnableThreadsWithPendingPosts_inForum(forumID);
        if (offset > totalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = DAOFactory.getThreadDAO().getEnableThreadsWithPendingPosts_inForum_withSortSupport_limit(forumID, offset, postsPerPage, sort, order);

        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));
    }

    public void prepareModerationControlPanel(GenericRequest request, GenericResponse response)
        throws DatabaseException, DatabaseException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can view enable threads with pending posts
        permission.ensureIsAuthenticated();
        permission.ensureCanModerateThreadInAtLeastOneForum();

        Collection forumBeans = ForumCache.getInstance().getBeans();
        for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
            ForumBean forumBean = (ForumBean)iter.next();
            int forumID = forumBean.getForumID();

            int pendingThreadCount = 0;
            int threadsWithPendingPostsCount = 0;
            int pendingPostCount = 0;

            if (permission.canModerateThread(forumID) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
                pendingThreadCount = DAOFactory.getThreadDAO().getNumberOfDisableThreads_inForum(forumID);
                threadsWithPendingPostsCount = DAOFactory.getThreadDAO().getNumberOfEnableThreadsWithPendingPosts_inForum(forumID);
                pendingPostCount = DAOFactory.getPostDAO().getNumberOfDisablePosts_inForum(forumID);
            }

            // note that if user does not have permission on this forum, then the value is 0
            forumBean.setPendingThreadCount(pendingThreadCount);
            forumBean.setThreadsWithPendingPostsCount(threadsWithPendingPostsCount);
            forumBean.setPendingPostCount(pendingPostCount);
        }

        int pendingThreadCount = DAOFactory.getThreadDAO().getNumberOfDisableThreads();
        int threadsWithPendingPostsCount = DAOFactory.getThreadDAO().getNumberOfEnableThreadsWithPendingPosts();

        // Note that because this forumBeans is a new instance
        // we have to put it in session instead of get it again from the ForumCache
        request.setAttribute("ForumBeans", forumBeans);
        request.setAttribute("PendingThreadCount", new Integer(pendingThreadCount));
        request.setAttribute("ThreadsWithPendingPostsCount", new Integer(threadsWithPendingPostsCount));

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementModcp(request, response);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void prepareModeratePendingThreads_inForum_limit(GenericRequest request)
        throws DatabaseException, AuthenticationException, BadInputException,
        DatabaseException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        // user must have been authenticated before he can view pending/disabled threads
        permission.ensureIsAuthenticated();

        int forumID = GenericParamUtil.getParameterInt(request, "forum");

        // make sure there is the forum
        // will throw an BadInputException if there is not this forum
        try {
            ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        } catch (ObjectNotFoundException e1) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        permission.ensureCanModerateThread(forumID);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalThreads = DAOFactory.getThreadDAO().getNumberOfDisableThreads_inForum(forumID);
        if (offset > totalThreads) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection threadBeans = DAOFactory.getThreadDAO().getDisableThreads_inForum_withSortSupport_limit(forumID, offset, postsPerPage, sort, order);
        Collection firstPostBeans = new ArrayList();

        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            PostBean postBean = DAOFactory.getPostDAO().getFirstPost_inThread(threadBean.getThreadID());
            firstPostBeans.add(postBean);

            MemberBean memberBean = null;
            if (postBean.getMemberID() != 0 && postBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_GUEST) {
                // Use cache for maximum performance
                memberBean = MemberCache.getInstance().getMember(postBean.getMemberID());
            }
            postBean.setMemberBean(memberBean);

            int postAttachCount = postBean.getPostAttachCount();
            if ((postAttachCount > 0) && MVNForumConfig.getEnableAttachment()) {
                int postID = postBean.getPostID();
                Collection attachBeans = DAOFactory.getAttachmentDAO().getAttachments_inPost(postID);
                int actualAttachCount = attachBeans.size();

                // now check if the attachCount in table Post equals to the actual attachCount in table Attachment
                if (postAttachCount != actualAttachCount) {
                    if (actualAttachCount != DAOFactory.getAttachmentDAO().getNumberOfAttachments_inPost(postID)) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.serious_error.cannot_process_attachment_count");
                        throw new AssertionError(localizedMessage);
                        //throw new AssertionError("Assertion: Serious error: cannot process Attachment Count in table Attachment");
                    }
                    log.warn("The attachment count in table Post and Attachment are not synchronized. In table Post = " +
                            postAttachCount + " and in table Attachment = " + actualAttachCount + ". Synchronize to " + actualAttachCount);
                    DAOFactory.getPostDAO().updateAttachCount(postID, actualAttachCount);
                }
                if (actualAttachCount > 0) {
                    postBean.setAttachmentBeans(attachBeans);
                }
            }
        }

        request.setAttribute("FirstPostBeans", firstPostBeans);
        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalThreads));
    }

    public void processModeratePendingThreads(GenericRequest request)
        throws DatabaseException, AuthenticationException, BadInputException, DatabaseException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        // user must have been authenticated before he can moderate pending/disabled threads
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // check normal permission, note that we don't check
        // permission on a forumID because we allow moderate posts
        // in multiple forums even if the web interface does not support it
        int forumID = -1;
        try {
            forumID = GenericParamUtil.getParameterInt(request, "forum");
            ForumCache.getInstance().getBean(forumID);// check valid forumID
            permission.ensureCanModerateThread(forumID);

            ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        } catch (BadInputException ex) {
            // just ignore, in case of use customized client
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        permission.ensureCanModerateThreadInAtLeastOneForum();

        try {
            String prefix = "modthreadaction_";
            for (Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements(); ) {
                String param = (String) enumeration.nextElement();
                if (param.startsWith(prefix)) {
                    String modValue = GenericParamUtil.getParameter(request, param, true);
                    String strThreadID = param.substring(prefix.length());
                    int threadID = Integer.parseInt(strThreadID);
                    if (modValue.equals("approve")) {
                        ThreadBean threadBean = null;
                        try {
                            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
                        } catch (ObjectNotFoundException e) {
                            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
                            throw new ObjectNotFoundException(localizedMessage);
                        }
                        int currentForumID = threadBean.getForumID();
                        permission.ensureCanModerateThread(currentForumID);
                        DAOFactory.getThreadDAO().updateThreadStatus(threadID, ThreadBean.THREAD_STATUS_DEFAULT);

                        // now if change from Disable to Enable, update the lastpostdate so
                        // that the Watch will see this thread as a new thread
                        if (threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED ) {
                            Timestamp now = DateUtil.getCurrentGMTTimestamp();
                            DAOFactory.getThreadDAO().updateLastPostDate(threadID, now);
                        }
                    } else if (modValue.equals("delete")) {
                        ThreadBean threadBean = null;
                        try {
                            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
                        } catch (ObjectNotFoundException e) {
                            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
                            throw new ObjectNotFoundException(localizedMessage);
                        }
                        deleteThread(request, threadBean);
                    } else {
                        // it means ignore, do nothing
                    }
                }
            }
        } finally {
            // now update the forum statistics
            if (forumID != -1) {
                StatisticsUtil.updateForumStatistics(forumID);
            }
        }

        // Now clear the cache
        PostCache.getInstance().clear();
        ThreadCache.getInstance().clear();

        request.setAttribute("ForumID", String.valueOf(forumID));
    }

    public void prepareList_inFavorite(GenericRequest request)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int memberID = onlineUser.getMemberID();

        Collection threadBeans = DAOFactory.getThreadDAO().getThreads_inFavorite_inMember(memberID);

        //remove threads that current user don't have permission
        for (Iterator iter = threadBeans.iterator(); iter.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iter.next();
            int currentForumID = threadBean.getForumID();
            if (permission.canReadPost(currentForumID) == false) {
                iter.remove();
            } else if (threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED) {
                if (permission.canModerateThread(currentForumID) == false) {
                    iter.remove();
                }
            } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                iter.remove();
            }
        }
        int max = MVNForumConfig.getMaxFavoriteThreads();
        int favoriteThreadCount = threadBeans.size();
        double ratio = 0;
        if (max == 0) {
            ratio = 1.0;
        } else {
            ratio = (double)favoriteThreadCount / max;
        }
        request.setAttribute("QuotaRatio", new Double(ratio * 100));
        request.setAttribute("ThreadBeans", threadBeans);
    }

    public void prepareRSSSummary(GenericRequest request, GenericResponse response)
        throws DatabaseException, AuthenticationException {

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        // call this to check the parameter sort and order
        DAOFactory.getThreadDAO().getEnableThreads_withSortSupport_limit(0/*offset*/, 1/*rows*/, sort, order, false, -1, -1, -1);

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementRSS(request, response, sort, order);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void prepareListRSS(GenericRequest request)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadLastPostDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int offset = 0;// offset MUST always equals 0, please don't change !!!
        int rows = MVNForumConfig.getRowsPerRSS();
        try {
            rows = GenericParamUtil.getParameterInt(request, "rows");
            if (rows <= 0) {
                rows = MVNForumConfig.getRowsPerRSS();
            }
        } catch (Exception ex) {
            //just ignore
        }

        // now find that user want global/category/forum RSS
        int forumID = -1;
        int categoryID = -1;

        try {
            forumID = GenericParamUtil.getParameterInt(request, "forum");

            // check if forum id existed or not
            try {
                ForumCache.getInstance().getBean(forumID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
                throw new ObjectNotFoundException(localizedMessage);
            }
            
        } catch (BadInputException ex) {
            try {
                categoryID = GenericParamUtil.getParameterInt(request, "category");

                // check if category id existed or not
                try {
                    CategoryCache.getInstance().getBean(categoryID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
            } catch (BadInputException ex1) {
                // ignore
            }
        }

        Collection threadBeans = null;
        if (forumID > 0) {
            if (permission.canReadPost(forumID)) {
                threadBeans = DAOFactory.getThreadDAO().getAllEnableThreads_inForum_withSortSupport_limit(forumID, offset, rows, sort, order);
            } else {
                // don't have permission on this forum, just create empty Collection
                threadBeans = new ArrayList();
            }
        } else if (categoryID > 0) {
            //@todo implement later
        } else {// global RSS
            threadBeans = DAOFactory.getThreadDAO().getEnableThreads_withSortSupport_limit(offset, rows, sort, order, false, -1,-1, -1);

            //remove threads that current user don't have permission
            for (Iterator iter = threadBeans.iterator(); iter.hasNext(); ) {
                ThreadBean threadBean = (ThreadBean)iter.next();
                int currentForumID = threadBean.getForumID();
                if (permission.canReadPost(currentForumID) == false) {
                    iter.remove();
                } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                    iter.remove();
                }
            }
        }

        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("ForumID", new Integer(forumID));
        request.setAttribute("CategoryID", new Integer(categoryID));
    }

    public void prepareListUnansweredThreads(GenericRequest request, GenericResponse response)
        throws BadInputException,DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "ThreadCreationDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int category = -1;
        int forum = -1;

        String forum_category_option = "";
        try {
            forum_category_option = GenericParamUtil.getParameter(request, "forum_category_option");
            if (forum_category_option.startsWith("c")) {
                category = Integer.parseInt(forum_category_option.substring(1));
            } else if (forum_category_option.startsWith("f")) {
                forum = Integer.parseInt(forum_category_option.substring(1));
            } else {
                throw new BadInputException("Parameter forum_category_option is not valid.");
            }
        } catch (BadInputException e) {
            //throw new BadInputException("Parameter forum_category_option is required.");
        }
        
        int threadStatus = -1;
        String status = GenericParamUtil.getParameter(request, "status");
        if (status.length() == 0) {
            status = "all";
        }

        if (status.equalsIgnoreCase("active")) {
            threadStatus = ThreadBean.THREAD_STATUS_DEFAULT;
        } else if (status.equalsIgnoreCase("closed")) {
            threadStatus = ThreadBean.THREAD_STATUS_CLOSED;
        } else if (status.equalsIgnoreCase("locked")) {
            threadStatus = ThreadBean.THREAD_STATUS_LOCKED;
        } else if (status.equalsIgnoreCase("all")) {
            threadStatus = -1;
        } else {
            throw new BadInputException("The value of parameter 'status' is invalid");
        }
        // get all threads from database
        int totalOfUnanswerThread = DAOFactory.getThreadDAO().getNumberOfEnableThreads(true, threadStatus, category, forum);

        if (offset > totalOfUnanswerThread) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
        }
        // only base on unanswered threads
        Collection threadBeans = DAOFactory.getThreadDAO().getEnableThreads_withSortSupport_limit(offset, postsPerPage, sort, order, true, threadStatus, category, forum);
        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iterator.next();
            int currentForumID = threadBean.getForumID();
            // check again
            if (permission.canReadPost(currentForumID) == false) {
                iterator.remove();
            } else {
                ForumBean forumBean = null;
                try {
                    forumBean = ForumCache.getInstance().getBean(currentForumID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(currentForumID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
                if (forumBean.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                    iterator.remove();
                }
            }
        }
        request.setAttribute("ThreadBeans", threadBeans);
        request.setAttribute("TotalThreads", new Integer(totalOfUnanswerThread));
        request.setAttribute("forum", new Integer(forum));

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "listunansweredthreads", forum, category);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

}
