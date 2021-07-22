/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/PostDAOImplJDBC.java,v 1.59 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.59 $
 * $Date: 2009/01/02 18:31:46 $
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
package com.mvnforum.db.jdbc;

import java.io.StringReader;
import java.sql.*;
import java.util.*;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.common.ActiveMember;
import com.mvnforum.common.ActiveThread;
import com.mvnforum.db.*;

public class PostDAOImplJDBC implements PostDAO {

    private static final Logger log = LoggerFactory.getLogger(PostDAOImplJDBC.class);

    public PostDAOImplJDBC() {
    }

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
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + postID + ") in table 'Post'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ParentPostID, ForumID, ThreadID, MemberID, MemberName,
     *                   LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate,
     *                   PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption,
     *                   PostStatus, PostIcon, PostAttachCount
     * Excluded columns: PostID
     */
    public void create(int parentPostID, int forumID, int threadID,
                        int memberID, String memberName, String lastEditMemberName,
                        String postTopic, String postBody, Timestamp postCreationDate,
                        Timestamp postLastEditDate, String postCreationIP, String postLastEditIP,
                        int postEditCount, int postFormatOption, int postOption,
                        int postStatus, String postIcon, int postAttachCount)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot create new Post.");
        }

        //allow anonymous/guests to post
        if (memberID != 0) {
            try {
                // @todo: modify the parameter list as needed
                // You may have to regenerate this method if the needed columns don't have attribute 'include'
                DAOFactory.getMemberDAO().findByPrimaryKey2(memberID, memberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Post.");
            }
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getThreadDAO().findByPrimaryKey(threadID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Thread' does not exist. Cannot create new Post.");
        }

        //We allow anonymous/guests to send posts too (if admin allows them to).
        if ((memberName!=null) && (memberName.length()>0)) {
            try {
                // @todo: modify the parameter list as needed
                // You may have to regenerate this method if the needed columns don't have attribute 'include'
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Post.");
            }
        } else {
            // This is needed, otherwise we will get 'null' in the sql query, instead of ''
            memberName = "";
        }

        if ((lastEditMemberName!=null) && (lastEditMemberName.length()>0)) {
            try {
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(lastEditMemberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create table 'Post'.");
            }
        } else {
            lastEditMemberName=""; //so we don't get 'null' in sql query
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (parentPostID != 0) {
                findByPrimaryKey(parentPostID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Post' does not exist. Cannot create new Post.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, parentPostID);
            statement.setInt(2, forumID);
            statement.setInt(3, threadID);
            statement.setInt(4, memberID);
            statement.setString(5, memberName);
            statement.setString(6, lastEditMemberName);
            statement.setString(7, postTopic);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(8, new StringReader(postBody), postBody.length());
            } else {
                statement.setString(8, postBody);
            }
            statement.setTimestamp(9, postCreationDate);
            statement.setTimestamp(10, postLastEditDate);
            statement.setString(11, postCreationIP);
            statement.setString(12, postLastEditIP);
            statement.setInt(13, postEditCount);
            statement.setInt(14, postFormatOption);
            statement.setInt(15, postOption);
            statement.setInt(16, postStatus);
            statement.setString(17, postIcon);
            statement.setInt(18, postAttachCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Post'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * zt.add.080919
     */
    public void mergeThreads(int threadID, int destThreadID) 
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        try {
            Connection connection = DBUtils.getConnection();
            Statement statement = connection.createStatement();
            
            // linking source thread root as a child of target root
            // step 1: finding target root element
            String queryFindRoot = "SELECT PostID " +
                                                    "FROM " + TABLE_NAME + " " +
                                                    "WHERE     ParentPostID=0 " +
                                                    "AND     ThreadID=" + destThreadID;
            ResultSet rsFindRoot = statement.executeQuery(queryFindRoot);
            if (rsFindRoot!=null && rsFindRoot.next()) {
                // TODO savepoint?
                int rootID = rsFindRoot.getInt(1);
                // step 2: linking source thread root as a child of target root
                String queryLinkToRoot = "UPDATE " + TABLE_NAME + " " +
                                                    "SET     ParentPostID=" + rootID + " " +
                                                    "WHERE    ParentPostID=0 " +
                                                    "AND     ThreadID=" + threadID;
                statement.executeUpdate(queryLinkToRoot);
                // step 3: rewriting thread id's in posts to target thread id
                String queryMove = "UPDATE " + TABLE_NAME + " " +
                                                    "SET     ThreadID = " + destThreadID + " " +
                                                    "WHERE     ThreadID = " + threadID;
                statement.executeUpdate(queryMove);
            }
            DBUtils.closeResultSet(rsFindRoot);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int createPost(int parentPostID, int forumID, int threadID,
                       int memberID, String memberName, String lastEditMemberName,
                       String postTopic, String postBody, Timestamp postCreationDate,
                       Timestamp postLastEditDate, String postCreationIP, String postLastEditIP,
                       int postEditCount, int postFormatOption, int postOption,
                       int postStatus, String postIcon, int postAttachCount)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        create(
            parentPostID, forumID, threadID, memberID, memberName, lastEditMemberName,
            postTopic, postBody, postCreationDate, postLastEditDate,
            postCreationIP, postLastEditIP, postEditCount, postFormatOption,
            postOption, postStatus, postIcon, postAttachCount);

        int postID = 0;
        try {
            postID = findPostID(forumID, memberName, postCreationDate);
        } catch (ObjectNotFoundException ex) {
            // Hack the Oracle 9i problem
            Timestamp roundTimestamp = new Timestamp((postCreationDate.getTime()/1000)*1000);
            try {
                postID = findPostID(forumID, memberName, roundTimestamp);
            } catch (ObjectNotFoundException e) {
                throw new CreateException("Cannot find the PostID in table Post.");
            }
        }
        return postID;
    }

    public void delete(int postID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Post where PostID = (" + postID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inThread(int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.delete_inThread.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inForum(int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.delete_inForum.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: LastEditMemberName, PostTopic, PostBody, PostLastEditDate, PostLastEditIP,
     *                   PostFormatOption, PostOption, PostStatus, PostIcon
     * Excluded columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
     *                   MemberName, PostCreationDate, PostCreationIP, PostEditCount, PostAttachCount
     */
    public void update(int postID, // primary key
                        String lastEditMemberName, String postTopic, String postBody,
                        Timestamp postLastEditDate, String postLastEditIP, int postFormatOption,
                        int postOption, int postStatus, String postIcon)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        //if admin allows guests to edit posts
        if ((lastEditMemberName!=null) && (lastEditMemberName.length()>0)) {
            try {
                // @todo: modify the parameter list as needed
                // If this method does not change the foreign key columns, you can comment this block of code.
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(lastEditMemberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot update table 'Post'.");
            }
        } else {
            lastEditMemberName=""; //so we don't get 'null' in sql query
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET LastEditMemberName = ?, PostTopic = ?, PostBody = ?, PostLastEditDate = ?, PostLastEditIP = ?, PostFormatOption = ?, PostOption = ?, PostStatus = ?, PostIcon = ?");
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, lastEditMemberName);
            statement.setString(2, postTopic);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(3, new StringReader(postBody), postBody.length());
            } else {
                statement.setString(3, postBody);
            }
            statement.setTimestamp(4, postLastEditDate);
            statement.setString(5, postLastEditIP);
            statement.setInt(6, postFormatOption);
            statement.setInt(7, postOption);
            statement.setInt(8, postStatus);
            statement.setString(9, postIcon);

            // primary key column(s)
            statement.setInt(10, postID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Post where primary key = (" + postID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ParentPostID, ForumID, ThreadID
     * Excluded columns: PostID, MemberID, LastEditMemberName, PostTopic, PostBody, PostLastEditDate
     *                   PostFormatOption, PostOption, PostStatus, PostIcon, PostLastEditIP,
     *                   MemberName, PostCreationDate, PostCreationIP, PostEditCount, PostAttachCount
     */
    public void update(int postID, // primary key
                       int parentPostID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ParentPostID = ?, ForumID = ?, ThreadID = ?");
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // column(s) to be updated
            statement.setInt(1, parentPostID);
            statement.setInt(2, forumID);
            statement.setInt(3, threadID);

            // primary key column(s)
            statement.setInt(4, postID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Post where primary key = (" + postID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PostAttachCount
     * Excluded columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
     *                   MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate,
     *                   PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption,
     *                   PostOption, PostStatus, PostIcon
     */
    public void updateAttachCount(int postID, // primary key
                                  int postAttachCount)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET PostAttachCount = ?");
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, postAttachCount);

            // primary key column(s)
            statement.setInt(2, postID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update AttachCount in table Post where primary key = (" + postID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.updateAttachCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PostStatus
     * Excluded columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
     *                   MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate,
     *                   PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption,
     *                   PostOption, PostIcon, PostAttachCount
     */
    public void updateStatus(int postID, // primary key
                             int postStatus)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET PostStatus = ?");
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, postStatus);

            // primary key column(s)
            statement.setInt(2, postID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update PostStatus in table Post where primary key = (" + postID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.updateStatus.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID
     */
    public void update_ForumID_inThread(int threadID, int forumID)
        throws DatabaseException, ForeignKeyNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // If this method does not change the foreign key columns, you can comment this block of code.
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot update table 'Post'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ForumID = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, forumID);

            // primary key column(s)
            statement.setInt(2, threadID);

            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.update_ForumID_inThread.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private int findPostID(int forumID, String memberName, Timestamp postCreationDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? AND MemberName = ? AND PostCreationDate = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            statement.setString(2, memberName);
            statement.setTimestamp(3, postCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the PostID in table Post.");
            }

            return resultSet.getInt("PostID");
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.findPostID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ParentPostID, ForumID, ThreadID, MemberID, MemberName,
     *                   LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate,
     *                   PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption,
     *                   PostStatus, PostIcon, PostAttachCount
     * Excluded columns: PostID
     */
    public PostBean getPost(int postID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Post where primary key = (" + postID + ").");
            }

            PostBean bean = new PostBean();
            // @todo: uncomment the following line(s) as needed
            bean.setPostID(postID);
            bean.setParentPostID(resultSet.getInt("ParentPostID"));
            bean.setForumID(resultSet.getInt("ForumID"));
            bean.setThreadID(resultSet.getInt("ThreadID"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setMemberName(resultSet.getString("MemberName"));
            bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
            bean.setPostTopic(resultSet.getString("PostTopic"));
            bean.setPostBody(resultSet.getString("PostBody"));
            bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
            bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
            bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
            bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
            bean.setPostEditCount(resultSet.getInt("PostEditCount"));
            bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
            bean.setPostOption(resultSet.getInt("PostOption"));
            bean.setPostStatus(resultSet.getInt("PostStatus"));
            bean.setPostIcon(resultSet.getString("PostIcon"));
            bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getPost(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public PostBean getFirstPost_inThread(int threadID)
        throws ObjectNotFoundException, DatabaseException {

        // Note that because the status of the first post are always Enable
        // so that we can safely use the below method
        Collection enablePostBeans = getEnablePosts_inThread_limit(threadID, 0, 1);
        Iterator iter = enablePostBeans.iterator();
        if (iter.hasNext()) {
            PostBean postBean = (PostBean)iter.next();
            return postBean;
        }
        throw new ObjectNotFoundException("Cannot find the first post in thread = " + threadID);
    }

    public Collection getEnablePosts_inThread_limit(int threadID, int offset, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException {
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inThread_limit_mysql(threadID, offset, rowsToReturn, true);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inThread_limit_noscroll(threadID, offset, rowsToReturn, true);
        }
        return getBeans_inThread_limit_general(threadID, offset, rowsToReturn, true);
    }

    public Collection getDisablePosts_inThread_limit(int threadID, int offset, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException {
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inThread_limit_mysql(threadID, offset, rowsToReturn, false);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inThread_limit_noscroll(threadID, offset, rowsToReturn, false);
        }
        return getBeans_inThread_limit_general(threadID, offset, rowsToReturn, false);
    }

    /*
     * Included columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
     *                   MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate,
     *                   PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption,
     *                   PostOption, PostStatus, PostIcon, PostAttachCount
     * Excluded columns:
     */
    private Collection getBeans_inThread_limit_mysql(int threadID, int offset, int rowsToReturn, boolean enable)
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        if (enable) {
            sql.append(" AND PostStatus <> 1 ");
        } else {//disable
            sql.append(" AND PostStatus = 1 ");
        }
        sql.append(" ORDER BY PostID ASC ");
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            statement.setInt(2, offset);
            statement.setInt(3, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getBeans_inThread_limit_mysql.");
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
    private Collection getBeans_inThread_limit_noscroll(int threadID, int offset, int rowsToReturn, boolean enable)
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        if (enable) {
            sql.append(" AND PostStatus <> 1 ");
        } else {//disable
            sql.append(" AND PostStatus = 1 ");
        }
        sql.append(" ORDER BY PostID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setMaxRows(offset + rowsToReturn);
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getBeans_inThread_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
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
    private Collection getBeans_inThread_limit_general(int threadID, int offset, int rowsToReturn, boolean enable)
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        if (enable) {
            sql.append(" AND PostStatus <> 1 ");
        } else {//disable
            sql.append(" AND PostStatus = 1 ");
        }
        sql.append(" ORDER BY PostID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }

            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getBeans_inThread_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfPosts_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);

            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PostDAOImplJDBC.getNumberOfPosts_inMember.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getNumberOfPosts_inMember(memberID).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfEnablePosts_inForum(int forumID)
        throws DatabaseException {

        return getNumberOfPosts_inForum(forumID, true);
    }

    public int getNumberOfDisablePosts_inForum(int forumID)
        throws DatabaseException {

        return getNumberOfPosts_inForum(forumID, false);
    }

    public int getNumberOfPosts_inForum(int forumID, boolean enable)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? ");
        if (enable) {
            sql.append(" AND PostStatus <> 1 ");
        } else {//disable
            sql.append(" AND PostStatus = 1 ");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, forumID);

            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PostDAOImplJDBC.getNumberOfEnablePosts_inForum.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getNumberOfPosts_inForum(forumID).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfEnablePosts_inThread(int threadID)
        throws DatabaseException {

        return getNumberOfPosts_inThread(threadID, true);
    }

    public int getNumberOfDisablePosts_inThread(int threadID)
        throws DatabaseException {

        return getNumberOfPosts_inThread(threadID, false);
    }

    public int getNumberOfPosts_inThread(int threadID, boolean enable)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        if (enable) {
            sql.append(" AND PostStatus <> 1 ");
        } else {//disable
            sql.append(" AND PostStatus = 1 ");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, threadID);

            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PostDAOImplJDBC.getNumberOfPosts_inThread.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getNumberOfPosts_inThread(threadID).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateParentPostID(int oldParentPostID, int newParentPostID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ParentPostID = ?");
        sql.append(" WHERE ParentPostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // column(s) to update
            statement.setInt(1, newParentPostID);

            // condition column
            statement.setInt(2, oldParentPostID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("No row is updated in table Post where ParentPostID = (" + oldParentPostID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.updateParentPostID.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that postID is in database
     */
    public void increaseEditCount(int postID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET PostEditCount = PostEditCount + 1 WHERE PostID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, postID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the PostEditCount in table Post. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.increaseEditCount.");
        } finally {
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
    public Collection getLastEnablePosts_inThread_limit(int threadID, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException {
        
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ? AND PostStatus = 0");
        sql.append(" ORDER BY PostCreationDate DESC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setMaxRows(rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }

            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getLastEnablePosts_inThread_limit.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
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
    public Collection getLastEnablePosts_inForum_limit(int forumID, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException {
        
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? AND PostStatus = 0");// only get enable posts
        sql.append(" ORDER BY PostCreationDate DESC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setMaxRows(rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }

            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getLastEnablePosts_inForum_limit.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfPosts()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PostDAOImplJDBC.getNumberOfPosts.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getNumberOfPosts.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
     *                   PostTopic, PostBody, PostCreationDate, PostLastEditDate
     * Excluded columns: MemberName, LastEditMemberName, PostCreationIP, PostLastEditIP, PostEditCount,
     *                   PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount
     */
    public Collection getPosts()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getPosts.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getMaxPostID()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MAX(PostID)");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PostDAOImplJDBC.getMaxPostID.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getMaxPostID.");
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
    public Collection getPosts_fromIDRange(int fromID, int toID)
        throws IllegalArgumentException, DatabaseException {

        if (fromID < 0) {
            throw new IllegalArgumentException("The fromID < 0 is not allowed.");
        }
        if (toID < fromID) {
            throw new IllegalArgumentException("toID < fromID is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ParentPostID, ForumID, ThreadID, MemberID, MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate, PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption, PostOption, PostStatus, PostIcon, PostAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE (PostID >= ?) AND (PostID <= ?)");
        sql.append(" ORDER BY PostID ASC ");
        try {
            connection = DBUtils.getConnection();
            //fix problem with oracle database when calling rebuild index [Vinaphone project]
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, fromID);
            statement.setInt(2, toID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostBean bean = new PostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setParentPostID(resultSet.getInt("ParentPostID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastEditMemberName(resultSet.getString("LastEditMemberName"));
                bean.setPostTopic(resultSet.getString("PostTopic"));
                bean.setPostBody(resultSet.getString("PostBody"));
                bean.setPostCreationDate(resultSet.getTimestamp("PostCreationDate"));
                bean.setPostLastEditDate(resultSet.getTimestamp("PostLastEditDate"));
                bean.setPostCreationIP(resultSet.getString("PostCreationIP"));
                bean.setPostLastEditIP(resultSet.getString("PostLastEditIP"));
                bean.setPostEditCount(resultSet.getInt("PostEditCount"));
                bean.setPostFormatOption(resultSet.getInt("PostFormatOption"));
                bean.setPostOption(resultSet.getInt("PostOption"));
                bean.setPostStatus(resultSet.getInt("PostStatus"));
                bean.setPostIcon(resultSet.getString("PostIcon"));
                bean.setPostAttachCount(resultSet.getInt("PostAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getPosts_fromIDRange.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This is a special method. This method return a collection of ActiveMember
     * instead of a collection of PostBean
     */
    public Collection getMostActiveMembers(Timestamp since, int rowsToReturn)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, MemberName, COUNT(PostID) AS PostCount");// postgreSQL need AS
        sql.append(" FROM ").append(TABLE_NAME);
        sql.append(" WHERE PostCreationDate > ?  AND PostStatus <> ").append(PostBean.POST_STATUS_DISABLED);
        sql.append(" GROUP BY MemberID, MemberName");
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_FIREBIRD) {// should also interbase ???
            //sql.append(" ORDER BY 3 DESC");
            sql.append(" ORDER BY COUNT(PostID) DESC");
        } else {
            sql.append(" ORDER BY PostCount DESC");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, since);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ActiveMember member = new ActiveMember();
                member.setMemberID(resultSet.getInt("MemberID"));
                member.setMemberName(resultSet.getString("MemberName"));
                member.setLastPostCount(resultSet.getInt("PostCount"));
                retValue.add(member);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getMostActiveMembers.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getMostActiveThreads(Timestamp since, int rowsToReturn)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT t.ThreadID, f.ForumID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, ThreadLastPostDate, ThreadViewCount, ThreadReplyCount, ThreadType, ThreadPriority, ThreadTopic, ThreadAttachCount, ThreadHasPoll, ThreadIcon, COUNT(PostID) AS PostCount");// postgreSQL need AS
        sql.append(" FROM ").append(TABLE_NAME).append(" p , ").append(ThreadDAO.TABLE_NAME).append(" t,").append(ForumDAO.TABLE_NAME).append(" f");
        sql.append(" WHERE (t.ThreadID = p.ThreadID AND t.ThreadStatus <> ").append(ThreadBean.THREAD_STATUS_DISABLED);
        sql.append(" AND p.ForumID = f.ForumID AND f.ForumStatus <> ").append(ForumBean.FORUM_STATUS_DISABLED);
        sql.append(" AND PostCreationDate > ?)");
        sql.append(" GROUP BY t.ThreadID, f.ForumID, t.MemberName, t.LastPostMemberName, ThreadCreationDate, ThreadLastPostDate, ThreadViewCount, ThreadReplyCount, ThreadType, ThreadPriority, ThreadTopic, ThreadAttachCount, ThreadHasPoll, ThreadIcon");
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_FIREBIRD) {// should also interbase ???
            // 12 is the position of 'PostCount' in Select Query
            //sql.append(" ORDER BY 12 DESC, t.ThreadLastPostDate DESC");
            sql.append(" ORDER BY COUNT(PostID) DESC, t.ThreadLastPostDate DESC");
        } else {
            sql.append(" ORDER BY PostCount DESC, t.ThreadLastPostDate DESC");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, since);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ActiveThread thread = new ActiveThread();
                thread.setThreadID(resultSet.getInt("ThreadID"));
                thread.setThreadTopic(resultSet.getString("ThreadTopic"));
                thread.setForumID(resultSet.getInt("ForumID"));
                thread.setLastPostCount(resultSet.getInt("PostCount"));
                thread.setLastDate(resultSet.getTimestamp("ThreadLastPostDate"));
                thread.setAuthor(resultSet.getString("MemberName"));
                thread.setLastMember(resultSet.getString("LastPostMemberName"));
                thread.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                thread.setThreadType(resultSet.getInt("ThreadType"));
                thread.setThreadPriority(resultSet.getInt("ThreadPriority"));
                thread.setViewCount(resultSet.getInt("ThreadViewCount"));
                thread.setPollCount(resultSet.getInt("ThreadHasPoll"));
                thread.setReplyCount(resultSet.getInt("ThreadReplyCount"));
                thread.setAttachCount(resultSet.getInt("ThreadAttachCount"));
                thread.setIcon(resultSet.getString("ThreadIcon"));
                retValue.add(thread);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getMostActiveThreads.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

} // end of class PostDAOImplJDBC

