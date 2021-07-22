/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/StatisticsUtil.java,v 1.16 2008/06/01 17:22:06 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.16 $
 * $Date: 2008/06/01 17:22:06 $
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
 */
package com.mvnforum.common;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.PostBean;

public final class StatisticsUtil {

    private StatisticsUtil() {
    }

    /**
     * This method is used to update the thread statistics
     * It updates these information: threadReplyCount, lastPostMemberName, threadLastPostDate
     *
     * @param threadID the thread to update its statistic
     * @throws ObjectNotFoundException
     * @throws java.lang.IllegalArgumentException
     * @throws DatabaseException
     */
    public static void updateThreadStatistics(int threadID)
        throws ObjectNotFoundException, IllegalArgumentException, DatabaseException {

        int threadReplyCount = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID) - 1;
        DAOFactory.getThreadDAO().updateReplyCount(threadID, threadReplyCount);

        int attachCountInThread = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inThread(threadID);
        DAOFactory.getThreadDAO().updateThreadAttachCount(threadID, attachCountInThread);

        Collection lastPostInThread = DAOFactory.getPostDAO().getLastEnablePosts_inThread_limit(threadID, 1);
        Iterator iteratorInThread = lastPostInThread.iterator();
        if (iteratorInThread.hasNext()) {
            PostBean lastPostBeanInThread = (PostBean)iteratorInThread.next();
            String lastPostMemberName = lastPostBeanInThread.getMemberName();
            Timestamp threadLastPostDate = lastPostBeanInThread.getPostCreationDate();
            try {
                DAOFactory.getThreadDAO().updateLastPostMemberName(threadID, lastPostMemberName);
            } catch (ForeignKeyNotFoundException ex) {
                AssertionUtil.doAssert(false, "Assertion: cannot update LastPostMemberName of Thread in StatisticsUtil.updateThreadStatistics");
            }
            DAOFactory.getThreadDAO().updateLastPostDate(threadID, threadLastPostDate);
        }
    }

    /**
     * This method is used to update the forum statistics
     * It updates these information: forumThreadCount, forumPostCount, lastPostMemberName, forumLastPostDate
     *
     * @param forumID the forum to update its statistics
     * @throws java.lang.IllegalArgumentException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     */
    public static void updateForumStatistics(int forumID)
        throws ObjectNotFoundException, DatabaseException {

        int forumThreadCount = DAOFactory.getThreadDAO().getNumberOfEnableThreads_inForum(forumID);
        int forumPostCount = DAOFactory.getPostDAO().getNumberOfEnablePosts_inForum(forumID);

        // because the disable thread has first post in enable, so the
        // correct number of posts are the enable posts - disable threads
        int forumDisableThreadCount = DAOFactory.getThreadDAO().getNumberOfDisableThreads_inForum(forumID);
        forumPostCount -= forumDisableThreadCount;

        DAOFactory.getForumDAO().updateStatistics(forumID, forumThreadCount, forumPostCount);

        Collection lastPostInForum = DAOFactory.getPostDAO().getLastEnablePosts_inForum_limit(forumID, 1);
        Iterator iteratorInForum = lastPostInForum.iterator();
        if (iteratorInForum.hasNext()) {
            PostBean lastPostBeanInForum = (PostBean)iteratorInForum.next();
            String lastPostMemberName = lastPostBeanInForum.getMemberName();
            Timestamp forumLastPostDate = lastPostBeanInForum.getPostCreationDate();
            try {
                DAOFactory.getForumDAO().updateLastPostMemberName(forumID, lastPostMemberName);
            } catch (ForeignKeyNotFoundException ex) {
                AssertionUtil.doAssert(false, "Assertion: cannot update LastPostMemberName of Forum in StatisticsUtil.updateForumStatistics");
            }
            DAOFactory.getForumDAO().updateLastPostDate(forumID, forumLastPostDate);
        }
    }

    public static void updateMemberStatistics(int memberID)
        throws DatabaseException, ObjectNotFoundException {

        int memberPostCount = DAOFactory.getPostDAO().getNumberOfPosts_inMember(memberID);
        if (DAOFactory.getMemberDAO().isSupportUpdatePostCount()) {
            DAOFactory.getMemberDAO().updatePostCount(memberID, memberPostCount);
        }
    }

}
