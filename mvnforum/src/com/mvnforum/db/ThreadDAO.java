/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ThreadDAO.java,v 1.39 2008/11/24 10:51:06 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.39 $
 * $Date: 2008/11/24 10:51:06 $
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
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.*;

public interface ThreadDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Thread";

    public static final int ALL_FORUMS = -1;

    public static final int MAX_ROWS_TO_RETURN = Integer.MAX_VALUE;

    public static final int DEFAULT_OFFSET = 0;

    public void findByPrimaryKey(int threadID)
        throws ObjectNotFoundException, DatabaseException;

    public int createThread(int forumID, String memberName, String lastPostMemberName,
                        String threadTopic, String threadBody, int threadVoteCount,
                        int threadVoteTotalStars, Timestamp threadCreationDate, Timestamp threadLastPostDate,
                        int threadType, int threadPriority, int threadOption,
                        int threadStatus, int threadHasPoll, int threadViewCount,
                        int threadReplyCount, String threadIcon, int threadDuration, int threadAttachCount /* to backup */)
        throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException;

    public void delete(int threadID)
        throws DatabaseException, ObjectNotFoundException;

    public void delete_inForum(int forumID)
        throws DatabaseException;

    public void updateForumID(int threadID, // primary key
                               int forumID)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public void updateThreadStatus(int threadID, // primary key
                                   int threadStatus)
        throws ObjectNotFoundException, DatabaseException;

    public void updateThreadType(int threadID, // primary key
                                 int threadType)
        throws ObjectNotFoundException, DatabaseException;

    public void updateTopic_Body_Icon(int threadID, // primary key
                                      String threadTopic, String threadBody, String threadIcon, int threadPriority)
        throws ObjectNotFoundException, DatabaseException;

    public void increaseReplyCount(int threadID)
        throws DatabaseException, ObjectNotFoundException;

    public void updateLastPostMemberName(int threadID, // primary key
                                         String lastPostMemberName)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public void updateLastPostDate(int threadID, // primary key
                                   Timestamp threadLastPostDate)
        throws ObjectNotFoundException, DatabaseException;

    public void updateThreadAttachCount(int threadID,
                                        int count)
        throws ObjectNotFoundException, DatabaseException;

    public ThreadBean getThread(int threadID)
        throws ObjectNotFoundException, DatabaseException;

    public int getNumberOfEnableThreads_inForum(int forumID)
        throws DatabaseException;

    public int getNumberOfDisableThreads_inForum(int forumID)
        throws DatabaseException;

    public int getNumberOfNormalEnableThreads_inForum(int forumID)
        throws DatabaseException;

    public int getNumberOfEnableThreads(boolean onlyNoReply, int status, int category, int forum)
        throws DatabaseException;

    public Collection getAllThreads(String filterCriteria)
        throws IllegalArgumentException, DatabaseException;
    
    public Collection getEnableThreads_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, boolean onlyNoReply, int status, int category, int forum)
        throws IllegalArgumentException, DatabaseException;

    public Collection getEnableThreads_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, Timestamp from, Timestamp to)
        throws IllegalArgumentException, DatabaseException;

    public int getNumberOfDisableThreads()
        throws DatabaseException;

    public Collection getDisableBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public Collection getNormalEnableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public Collection getAllEnableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public Collection getDisableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    // Should Enable only ???
    public Collection getThreads_inFavorite_inMember(int memberID)
        throws DatabaseException;

    public Collection getEnableStickies_inForum(int forumID)
        throws DatabaseException;

    public Collection getEnableForumAnnouncements_inForum(int forumID)
        throws DatabaseException;

    public Collection getEnableGlobalAnnouncements()
        throws DatabaseException;

    public void increaseViewCount(int threadID)
        throws DatabaseException, ObjectNotFoundException;

    public void updateReplyCount(int threadID, // primary key
                                 int threadReplyCount)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException;

    public void updateThreadHasPoll(int threadID, int pollCount)
        throws ObjectNotFoundException, DatabaseException;

    public int getPreviousEnableThread(int forumID, int threadID)
        throws DatabaseException;

    public int getNextEnableThread(int forumID, int threadID)
        throws DatabaseException;

    public Collection getEnableThreads_inGlobal(Timestamp sinceDate)
        throws DatabaseException;

    public Collection getEnableThreads_inCategory(int categoryID, Timestamp sinceDate)
        throws DatabaseException;

    public Collection getEnableThreads_inForum(int forumID, Timestamp sinceDate)
        throws DatabaseException;

    public Collection getEnableThreads_inThread(int threadID, Timestamp sinceDate)
        throws DatabaseException;

    public int getNumberOfEnableThreadsWithPendingPosts()
        throws DatabaseException;

    public int getNumberOfEnableThreadsWithPendingPosts_inForum(int forumID)
        throws DatabaseException;

    /**
     * This method is used to get enable threads that having at least one pending post (for moderation)
     *
     * Note: current implementation use the NOSCROLL method which is quite slow
     *
     * @param offset
     * @param rowsToReturn
     * @param sort
     * @param order
     * @return
     * @throws java.lang.IllegalArgumentException if the arguments are not valid
     * @throws DatabaseException
     */
    public Collection getEnableThreadsWithPendingPosts_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException;

    public Collection getEnableThreadsWithPendingPosts_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException;

}
