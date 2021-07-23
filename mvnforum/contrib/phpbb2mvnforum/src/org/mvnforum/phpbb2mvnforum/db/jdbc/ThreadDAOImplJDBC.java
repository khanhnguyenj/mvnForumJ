/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/ThreadDAOImplJDBC.java,v 1.5 2007/01/15 10:27:10 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:27:10 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.ThreadDAO;
import org.mvnforum.util.DBUtils;

public class ThreadDAOImplJDBC implements ThreadDAO {

    public void findByPrimaryKey(int threadID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + threadID + ") in table 'thread'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in threadDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic, 
     *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, 
     *                   ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, 
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount
     * Excluded columns: 
     */
    public int createThread(int threadID, int forumID, String memberName, String lastPostMemberName,
            String threadTopic, String threadBody, int threadVoteCount, int threadVoteTotalStars,
            Timestamp threadCreationDate, Timestamp threadLastPostDate, int threadType, int threadOption,
            int threadStatus, int threadHasPoll, int threadViewCount, int threadReplyCount, String threadIcon,
            int threadDuration, int threadAttachCount)
        throws CreateException, DatabaseException, DuplicateKeyException {

        // @todo: comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(threadID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException(
                    "Primary key already exists. Cannot create new thread with the same [ThreadID] (" + threadID + ").");
        } catch (ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("INSERT INTO "
                        + TABLE_NAME
                        + " (ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, threadID);
            statement.setInt(2, forumID);
            statement.setString(3, memberName);
            statement.setString(4, lastPostMemberName);
            statement.setString(5, threadTopic);
            statement.setString(6, threadBody);
            statement.setInt(7, threadVoteCount);
            statement.setInt(8, threadVoteTotalStars);
            statement.setTimestamp(9, threadCreationDate);
            statement.setTimestamp(10, threadLastPostDate);
            statement.setInt(11, threadType);
            statement.setInt(12, threadOption);
            statement.setInt(13, threadStatus);
            statement.setInt(14, threadHasPoll);
            statement.setInt(15, threadViewCount);
            statement.setInt(16, threadReplyCount);
            statement.setString(17, threadIcon);
            statement.setInt(18, threadDuration);
            statement.setInt(19, threadAttachCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'thread'.");
            }
            return 0;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in threadDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void createMultiple(Collection beans) {
        // TODO Auto-generated method stub
        
    }

}
