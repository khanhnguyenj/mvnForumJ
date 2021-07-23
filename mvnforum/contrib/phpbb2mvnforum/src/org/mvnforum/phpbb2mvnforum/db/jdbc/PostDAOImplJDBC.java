/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PostDAOImplJDBC.java,v 1.8 2007/01/15 10:27:11 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.8 $
 * $Date: 2007/01/15 10:27:11 $
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
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.PostDAO;
import org.mvnforum.util.DBUtils;

public class PostDAOImplJDBC implements PostDAO {

    public void findByPrimaryKey(int postID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + postID + ") in table 'post'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in postDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }   
    
    /*
     * Included columns: PostID, ParentPostID, ForumID, ThreadID, MemberID, 
     *                   MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, 
     *                   PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, 
     *                   PostOption, PostStatus, PostIcon, PostAttachCount
     * Excluded columns: 
     */
    public void create(int postID, int parentPostID, int forumID, 
                        int threadID, int memberID, String memberName, 
                        String lastEditMemberName, String postTopic, String postBody, 
                        Timestamp postCreationDate, Timestamp postLastEditDate, String postCreationIP, 
                        String postLastEditIP, int postEditCount, int postFormatOption, 
                        int postOption, int postStatus, String postIcon, 
                        int postAttachCount)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(postID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new post with the same [PostID] (" + postID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, postID);
            statement.setInt(2, parentPostID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            statement.setInt(5, memberID);
            statement.setString(6, memberName);
            statement.setString(7, lastEditMemberName);
            statement.setString(8, postTopic);
            statement.setString(9, postBody);
            statement.setTimestamp(10, postCreationDate);
            statement.setTimestamp(11, postLastEditDate);
            statement.setString(12, postCreationIP);
            statement.setString(13, postLastEditIP);
            statement.setInt(14, postEditCount);
            statement.setInt(15, postFormatOption);
            statement.setInt(16, postOption);
            statement.setInt(17, postStatus);
            statement.setString(18, postIcon);
            statement.setInt(19, postAttachCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'post'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in postDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void createMultiple(Collection beans) {
        // TODO Auto-generated method stub
        
    }
}
