/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/PostUtil.java,v 1.31 2009/04/27 08:34:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
 * $Date: 2009/04/27 08:34:34 $
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import javax.mail.MessagingException;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.common.ActiveThread;
import com.mvnforum.common.StatisticsUtil;
import com.mvnforum.db.*;
import com.mvnforum.search.post.PostIndexer;

import freemarker.template.TemplateException;

public final class PostUtil {

    private static final Logger log = LoggerFactory.getLogger(PostUtil.class);

    public static final int NUMBER_OF_MORE_THREADS_TO_GET = 5;
    
    private PostUtil() {
    }

    public static void addPost(int parentPostID, int forumID, int memberID, String currentIP, String memberName,
                               String postTopic, String postBody, String postIcon)
        throws DatabaseException, ObjectNotFoundException, CreateException, ForeignKeyNotFoundException {

        //TODO will be set later
        Locale locale = Locale.getDefault();
        int threadID = 0;
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        boolean isForumModerator = false;

        if (parentPostID == 0) {// new thread
            ForumBean forumBean = null;
            try {
                forumBean = ForumCache.getInstance().getBean(forumID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] { new Integer(forumID) });
                throw new ObjectNotFoundException(localizedMessage);
            }
            forumBean.ensureNotDisabledForum(locale);
            forumBean.ensureNotClosedForum(locale);
            forumBean.ensureNotLockedForum(locale);
        } else {// reply to a post
            // this is a parent post
            PostBean postBean = null;
            try {
                postBean = DAOFactory.getPostDAO().getPost(parentPostID);// can throw DatabaseException
            } catch (ObjectNotFoundException ex) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] { new Integer(parentPostID) });
                throw new ObjectNotFoundException(localizedMessage);
            }

            ForumBean forumBean = null;
            try {
                forumBean = ForumCache.getInstance().getBean(forumID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] { new Integer(forumID) });
                throw new ObjectNotFoundException(localizedMessage);
            }
            forumBean.ensureNotDisabledForum(locale);
            forumBean.ensureNotClosedForum(locale);
            forumBean.ensureNotLockedForum(locale);

            // now we prepare to list latest post in the thread
            threadID = postBean.getThreadID();

            // now check if thread is closed or locked, if it is, then cannot reply to a post
            ThreadBean threadBean = null;
            try {
                threadBean = DAOFactory.getThreadDAO().getThread(threadID);
            } catch (ObjectNotFoundException ex) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] { new Integer(threadID) } );
                throw new ObjectNotFoundException(localizedMessage);
            }

            threadBean.ensureStatusCanReply();
        }

        // Timestamp postLastEditDate = now;
        String postCreationIP = ( (currentIP == null) || (currentIP.equals("")) ) ? "127.0.0.1" : currentIP;
        String postLastEditIP = "";// should we init it to postCreationIP ???
        int postFormatOption = 0;
        int postOption = 0;
        int postStatus = PostBean.POST_STATUS_DEFAULT;

        try {
            // Ensure that moderator don't have to moderate the thread to enable it
            if (ForumCache.getInstance().getBean(forumID).shouldModeratePost() && !isForumModerator) {
                // we will not disable post that is a thread (parentPostID == 0)
                if (parentPostID != 0) {// replied post
                    postStatus = PostBean.POST_STATUS_DISABLED;
                }
            }
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] { new Integer(forumID) });
            throw new ObjectNotFoundException(localizedMessage);
        }

        int postAttachCount = 0;

        int postID = DAOFactory.getPostDAO().createPost(parentPostID, forumID, threadID, memberID, memberName,
                ""/*lastEditMemberName*/, postTopic, postBody, now/*postCreationDate*/, now/*postLastEditDate*/,
                postCreationIP, postLastEditIP, 0/*postEditCount*/, postFormatOption, postOption, postStatus,
                postIcon, postAttachCount);

        StatisticsUtil.updateMemberStatistics(memberID);
        StatisticsUtil.updateForumStatistics(forumID);
        StatisticsUtil.updateThreadStatistics(threadID);

        /** @todo Update PostEditLog table here */

        // Now clear the cache
        PostCache.getInstance().clear();
        ThreadCache.getInstance().clear();

        // now, update the Search Index
        //@todo check the performance here
        PostBean justAddedPostBean = null;
        try {
            justAddedPostBean = DAOFactory.getPostDAO().getPost(postID);
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] { new Integer(postID) });
            throw new ObjectNotFoundException(localizedMessage);
        }

        PostIndexer.scheduleAddPostTask(justAddedPostBean);
    }

    public static void sendEmailToAdminBecauseCensoredPost(GenericRequest request, int postID, int forumID, int threadID, int memberID)
        throws IOException, TemplateException, MessagingException, BadInputException {

        HashMap subjectInfo = new HashMap();
        subjectInfo.put("postID", new Integer(postID));

        HashMap messageInfo = new HashMap();
        messageInfo.put("forumID", new Integer(forumID));
        messageInfo.put("threadID", new Integer(threadID));
        messageInfo.put("postID", new Integer(postID));
        messageInfo.put("memberID", new Integer(memberID));

        StringBuffer postURL = new StringBuffer(512);
        postURL.append(ParamUtil.getServerPath());
        postURL.append(request.getContextPath());
        postURL.append(UserModuleConfig.getUrlPattern());
        postURL.append("/viewthread?thread=");
        postURL.append(threadID);
        postURL.append("#");
        postURL.append(postID);
        messageInfo.put("postURL", postURL.toString());

        String emailAddr = MVNForumConfig.getWebMasterEmail();

        MailMessageStruct mailMessageStruct = new MailMessageStruct();
        mailMessageStruct.setFrom(emailAddr);
        mailMessageStruct.setTo(emailAddr);
        mailMessageStruct.setSubject(MyUtil.getStringFromFreeMarkerTemplate(subjectInfo, MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_SUBJECT));
        mailMessageStruct.setMessage(MyUtil.getStringFromFreeMarkerTemplate(messageInfo, MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_BODY));

        try {
            MailUtil.sendMail(mailMessageStruct);
        } catch (UnsupportedEncodingException e) {
            log.error("Cannot support encoding", e);
        }

    }

    public static Collection getMostActiveThreadsAfterCheckThreadType(Timestamp since)
        throws DatabaseException {

        // get more than NUMBER_OF_MORE_THREADS_TO_GET threads in case user does not have permission to view previous threads
        Collection mostActiveThreads = PostCache.getInstance().getMostActiveThreads(since, MVNForumConfig.getMaxActiveThreads() + NUMBER_OF_MORE_THREADS_TO_GET);
        for (Iterator iter = mostActiveThreads.iterator(); iter.hasNext(); ) {
            ActiveThread activeThread = (ActiveThread) iter.next();

            if (MVNForumConfig.getOnlyNormalThreadTypeInActiveThreads()) {
                if (activeThread.getThreadType() == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT) {
                    iter.remove();
                } else if (activeThread.getThreadType() == ThreadBean.THREAD_TYPE_FORUM_ANNOUNCEMENT) {
                    iter.remove();
                } else if (activeThread.getThreadType() == ThreadBean.THREAD_TYPE_STICKY) {
                    iter.remove();
                }
            }
        }
        return mostActiveThreads;
    }

    public static Collection getMyMostActiveThreads(MVNForumPermission permission, Timestamp since)
        throws DatabaseException, ObjectNotFoundException {

        // get more than 2 threads in case user does not have permission to view previous threads
        Collection mostActiveThreads = getMostActiveThreadsAfterCheckThreadType(since);
        
        // first loop, we will remove post that user should not be shown
        for (Iterator iter = mostActiveThreads.iterator(); iter.hasNext(); ) {
            ActiveThread activeThread = (ActiveThread) iter.next();

            ForumBean forumBean = null;
            try {
                forumBean = ForumCache.getInstance().getBean(activeThread.getForumID());
            } catch (ObjectNotFoundException e) {
                Locale locale = Locale.getDefault();
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] { new Integer(activeThread.getForumID()) });
                throw new ObjectNotFoundException(localizedMessage);
            }

            if (permission.canReadPost(activeThread.getForumID()) == false) {
                iter.remove();
            } else if (forumBean.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                iter.remove();
            }
        }

        // the second loop, we only keep posts that limit by MVNForumConfig.getMaxActiveThreads()
        if (mostActiveThreads.size() > MVNForumConfig.getMaxActiveThreads()) {
            int count = 0;
            for (Iterator iter = mostActiveThreads.iterator(); iter.hasNext(); ) {
                ActiveThread activeThread = (ActiveThread) iter.next();
                count++;
                if (count > MVNForumConfig.getMaxActiveThreads()) {
                    iter.remove();
                }
            }
        }
        return mostActiveThreads;
    }

    public static Collection getChildrenPosts(int postID) throws DatabaseException, ObjectNotFoundException {

        PostBean postBean = DAOFactory.getPostDAO().getPost(postID);

        Collection posts = new ArrayList();

        int threadID = postBean.getThreadID();
        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);
        Collection postBeans = PostCache.getInstance().getEnablePosts_inThread_limit(threadID, 0, numberOfPosts);
        for (Iterator iterator = postBeans.iterator(); iterator.hasNext();) {
            PostBean post = (PostBean) iterator.next();
            if (post.getParentPostID() == postID) {
                posts.add(post);
            }
        }

        return posts;
    }
}
