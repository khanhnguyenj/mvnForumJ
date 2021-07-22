/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PostDAO.java,v 1.29 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.29 $
 * $Date: 2009/01/02 15:12:55 $
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

public interface PostDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Post";

    public void findByPrimaryKey(int postID)
        throws ObjectNotFoundException, DatabaseException;

    public int createPost(int parentPostID, int forumID, int threadID,
                          int memberID, String memberName, String lastEditMemberName,
                          String postTopic, String postBody, Timestamp postCreationDate,
                          Timestamp postLastEditDate, String postCreationIP, String postLastEditIP,
                          int postEditCount, int postFormatOption, int postOption,
                          int postStatus, String postIcon, int postAttachCount)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException;

    public void delete(int postID)
        throws DatabaseException, ObjectNotFoundException;

    public void delete_inThread(int threadID)
        throws DatabaseException;

    public void delete_inForum(int forumID)
        throws DatabaseException;

    public void update(int postID, // primary key
                       String lastEditMemberName, String postTopic, String postBody,
                       Timestamp postLastEditDate, String postLastEditIP, int postFormatOption,
                       int postOption, int postStatus, String postIcon)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public void update(int postID, // primary key
                       int parentPostID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public void updateAttachCount(int postID, // primary key
                                  int postAttachCount)
        throws ObjectNotFoundException, DatabaseException;

    public void updateStatus(int postID, // primary key
                           int postStatus)
        throws ObjectNotFoundException, DatabaseException;

    public void update_ForumID_inThread(int threadID, int forumID)
        throws DatabaseException, ForeignKeyNotFoundException;

    public void updateParentPostID(int oldParentPostID, int newParentPostID)
        throws ObjectNotFoundException, DatabaseException;

    public void increaseEditCount(int postID)
        throws DatabaseException, ObjectNotFoundException;

    public PostBean getPost(int postID)
        throws ObjectNotFoundException, DatabaseException;

    /**
     * zt.add.080919
     * Merge threads
     * @param threadID
     * @param destThreadID
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void mergeThreads(int threadID, int destThreadID) 
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;
    
    /**
      * This method is used the get the first post of thread for moderation
      *
      * @param threadID
      * @return
      * @throws ObjectNotFoundException
      * @throws DatabaseException
      */
    public PostBean getFirstPost_inThread(int threadID)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getEnablePosts_inThread_limit(int threadID, int offset, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException;

    public Collection getDisablePosts_inThread_limit(int threadID, int offset, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException;

    public int getNumberOfEnablePosts_inThread(int threadID)
        throws DatabaseException;

    public int getNumberOfDisablePosts_inThread(int threadID)
        throws DatabaseException;

    public int getNumberOfPosts_inMember(int memberID)
        throws DatabaseException;

    /**
     * This method could be used to update the statistics of the forum
     *
     * @param forumID int
     * @throws DatabaseException
     * @return int
     */
    public int getNumberOfEnablePosts_inForum(int forumID)
        throws DatabaseException;

    public int getNumberOfDisablePosts_inForum(int forumID)
        throws DatabaseException;

    /**
     * This method could be used to check the total posts when compare with number of docs in lucene
     *
     * @throws DatabaseException
     * @return int
     */
    public int getNumberOfPosts() throws DatabaseException;

    /**
     * This method could be used to get all post when rebuild lucene search index
     *
     * @throws DatabaseException
     * @return Collection
     */
    public Collection getPosts() throws DatabaseException;

    public int getMaxPostID()
        throws DatabaseException;

    /**
     * This method could be used to get all post when rebuild lucene search index
     *
     * @throws DatabaseException
     * @return Collection
     */
    public Collection getPosts_fromIDRange(int fromID, int toID)
        throws IllegalArgumentException, DatabaseException;

    /**
     * This method could be used to get latest post in a thread to update
     * thread statistics. It also could be used to get the latest posts
     * when replying to a post
     *
     * @param threadID int
     * @param rowsToReturn int
     * @throws IllegalArgumentException
     * @throws DatabaseException
     * @return Collection
     */
    public Collection getLastEnablePosts_inThread_limit(int threadID, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException;

    /**
     * This method could be used to update forum statistics by getting one
     * last post in a forum
     *
     * @param forumID int
     * @param rowsToReturn int
     * @throws IllegalArgumentException
     * @throws DatabaseException
     * @return Collection
     */
    public Collection getLastEnablePosts_inForum_limit(int forumID, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException;

    public Collection getMostActiveMembers(Timestamp since, int rowsToReturn)
        throws DatabaseException;

    public Collection getMostActiveThreads(Timestamp since, int rowsToReturn)
        throws DatabaseException;

}
