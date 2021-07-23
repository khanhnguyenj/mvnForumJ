/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/ThreadDAOImplJDBC.java,v 1.82 2010/06/21 08:27:56 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.82 $
 * $Date: 2010/06/21 08:27:56 $
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

import com.mvnforum.db.*;

public class ThreadDAOImplJDBC implements ThreadDAO {

    private static final Logger log = LoggerFactory.getLogger(ThreadDAOImplJDBC.class);

    public ThreadDAOImplJDBC() {
    }

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
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + threadID + ") in table 'Thread'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     * Excluded columns: ThreadID
     */
    private void create(int forumID, String memberName, String lastPostMemberName,
                        String threadTopic, String threadBody, int threadVoteCount,
                        int threadVoteTotalStars, Timestamp threadCreationDate, Timestamp threadLastPostDate,
                        int threadType, int threadPriority,int threadOption, int threadStatus,
                        int threadHasPoll, int threadViewCount, int threadReplyCount,
                        String threadIcon, int threadDuration, int threadAttachCount)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        ThreadBean.validateThreadStatus(threadStatus);

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot create new Thread.");
        }

        /* Here we allow memberName to be empty or null, which means
           unknown user or guest created the thread. */
        if ((memberName!=null) && (memberName.length()>0)) {
            try {
                // @todo: modify the parameter list as needed
                // You may have to regenerate this method if the needed columns don't have attribute 'include'
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Post.");
            }
        } else {
            memberName=""; // This is needed, otherwise we will get 'null' in the sql query, instead of ''
        }

        //we also allow guests to send posts (if admin allows that)
        if ((lastPostMemberName!=null) && (lastPostMemberName.length()>0)) {
            try {
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(lastPostMemberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create table 'Thread'.");
            }
        } else {
            lastPostMemberName=""; //so we don't get 'null' in sql query
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, forumID);
            statement.setString(2, memberName);
            statement.setString(3, lastPostMemberName);
            statement.setString(4, threadTopic);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(5, new StringReader(threadBody), threadBody.length());
            } else {
                statement.setString(5, threadBody);
            }
            statement.setInt(6, threadVoteCount);
            statement.setInt(7, threadVoteTotalStars);
            statement.setTimestamp(8, threadCreationDate);
            statement.setTimestamp(9, threadLastPostDate);
            statement.setInt(10, threadType);
            statement.setInt(11, threadPriority);
            statement.setInt(12, threadOption);
            statement.setInt(13, threadStatus);
            statement.setInt(14, threadHasPoll);
            statement.setInt(15, threadViewCount);
            statement.setInt(16, threadReplyCount);
            statement.setString(17, threadIcon);
            statement.setInt(18, threadDuration);
            statement.setInt(19, threadAttachCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Thread'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int createThread(int forumID, String memberName, String lastPostMemberName,
                        String threadTopic, String threadBody, int threadVoteCount,
                        int threadVoteTotalStars, Timestamp threadCreationDate, Timestamp threadLastPostDate,
                        int threadType,int threadPriority, int threadOption, int threadStatus,
                        int threadHasPoll, int threadViewCount, int threadReplyCount,
                        String threadIcon, int threadDuration, int threadAttachCount)
                        throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {

        create(forumID, memberName, lastPostMemberName, threadTopic, threadBody, threadVoteCount, threadVoteTotalStars, threadCreationDate, threadLastPostDate, threadType, threadPriority,threadOption, threadStatus, threadHasPoll, threadViewCount, threadReplyCount, threadIcon, threadDuration, threadAttachCount);
        int threadID = 0;
        try {
            threadID = findThreadID(forumID, memberName, threadCreationDate);
        } catch (ObjectNotFoundException ex) {
            // Hack the Oracle 9i problem
            Timestamp roundTimestamp = new Timestamp((threadCreationDate.getTime()/1000)*1000);
            threadID = findThreadID(forumID, memberName, roundTimestamp);
        }
        return threadID;
    }

    public void delete(int threadID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /**
     * zt.add.080923
     */
    public Collection getAllThreads(String filterCriteria)
        throws IllegalArgumentException, DatabaseException {
        
        Collection categories = DAOFactory.getCategoryDAO().getCategories();
        while (categories.iterator().hasNext()) {
            CategoryBean categoryBean = (CategoryBean) categories.iterator().next();
            ForumBean forumBean = (ForumBean) DAOFactory.getForumDAO().getForums_inCategory(categoryBean.getCategoryID());
        }
        return null;
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
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.delete_inForum.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadTopic, ThreadBody, ThreadIcon
     * Excluded columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadVoteCount,
     *                   ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadOption,
     *                   ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadDuration
     */
    public void updateTopic_Body_Icon(int threadID, // primary key
                        String threadTopic, String threadBody, String threadIcon, int threadPriority)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadTopic = ?, ThreadBody = ?, ThreadIcon = ?, ThreadPriority = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, threadTopic);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(2, new StringReader(threadBody), threadBody.length());
            } else {
                statement.setString(2, threadBody);
            }
            statement.setString(3, threadIcon);

            statement.setInt(4, threadPriority);

            // primary key column(s)
            statement.setInt(5, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateTopic_Body_Icon.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateThreadAttachCount(int threadID,
                                        int attachCount)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadAttachCount = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, attachCount);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update ThreadAttachCount in table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateThreadAttachCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that threadID is in database
     */
    public void increaseReplyCount(int threadID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ThreadReplyCount = ThreadReplyCount + 1 WHERE ThreadID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, threadID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ThreadReplyCount in table Thread. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.increaseReplyCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: LastPostMemberName
     * Excluded columns: ThreadID, ForumID, MemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     */
    public void updateLastPostMemberName(int threadID, // primary key
                                         String lastPostMemberName)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        //we also allow guests to send posts (if admin allows that)
        if ((lastPostMemberName!=null) && (lastPostMemberName.length()>0)) {
            try {
                // @todo: modify the parameter list as needed
                // If this method does not change the foreign key columns, you can comment this block of code.
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(lastPostMemberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot update table 'Thread'.");
            }
        } else {
            lastPostMemberName=""; //so we don't get 'null' in sql query
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET LastPostMemberName = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, lastPostMemberName);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                // Some drivers don't update database if it detect old and new data are the same
                // @todo: should check driver, not check database
                // Currently there is only one driver: Caucho MySql driver
                if (DBUtils.getDatabaseType() != DBUtils.DATABASE_MYSQL) {
                    throw new ObjectNotFoundException("Cannot update table Thread where primary key = (" + threadID + ").");
                }
                log.warn("WARNING: By pass the check for Caucho MySql driver.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateLastPostMemberName.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadLastPostDate
     * Excluded columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic,
     *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     */
    public void updateLastPostDate(int threadID, // primary key
                        Timestamp threadLastPostDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadLastPostDate = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, threadLastPostDate);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateLastPostDate.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     * Excluded columns: ThreadID
     */
    public ThreadBean getThread(int threadID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Thread where primary key = (" + threadID + ").");
            }

            ThreadBean bean = new ThreadBean();
            // @todo: uncomment the following line(s) as needed
            bean.setThreadID(threadID);
            bean.setForumID(resultSet.getInt("ForumID"));
            bean.setMemberName(resultSet.getString("MemberName"));
            bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
            bean.setThreadTopic(resultSet.getString("ThreadTopic"));
            bean.setThreadBody(resultSet.getString("ThreadBody"));
            bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
            bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
            bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
            bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
            bean.setThreadType(resultSet.getInt("ThreadType"));
            bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
            bean.setThreadOption(resultSet.getInt("ThreadOption"));
            bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
            bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
            bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
            bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
            bean.setThreadIcon(resultSet.getString("ThreadIcon"));
            bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getThread(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * Note: this is a customized method
     */
    public int getNumberOfEnableThreads_inForum(int forumID)
        throws DatabaseException {

        return getNumberOfBeans_inForum(forumID, true);
    }

    public int getNumberOfDisableThreads_inForum(int forumID)
        throws DatabaseException {

        return getNumberOfBeans_inForum(forumID, false);
    }

    private int getNumberOfBeans_inForum(int forumID, boolean enable)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");
        if (enable) {
            sql.append(" AND ThreadStatus <> 1 ");
        } else {//disable
            sql.append(" AND ThreadStatus = 1 ");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ThreadDAOImplJDBC.getNumberOfBeans_inForum.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNumberOfBeans_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfNormalEnableThreads_inForum(int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? AND ThreadType = ").append(ThreadBean.THREAD_TYPE_DEFAULT);
        sql.append(" AND ThreadStatus <> 1 ");//mean enable thread
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ThreadDAOImplJDBC.getNumberOfNormalEnableThreads_inForum.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNumberOfNormalEnableThreads_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfEnableThreads(boolean onlyNoReply, int status, int category, int forum)
        throws DatabaseException {

        return getNumberOfBeans(true, onlyNoReply, true, status, category, forum);
    }

    public int getNumberOfDisableThreads()
        throws DatabaseException {

        return getNumberOfBeans(false, false, false, -1, -1, -1);
    }

    private int getNumberOfBeans(boolean enable, boolean onlyNoReply, boolean checkForumStatus, int status, int category, int forum)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME + " thread");
        if (checkForumStatus) {
            sql.append(" , " + ForumDAO.TABLE_NAME + " forum");
        }
        if (enable) {
            sql.append(" WHERE ThreadStatus <> 1 ");
        } else {//disable
            sql.append(" WHERE ThreadStatus = 1 ");
        }
        if (onlyNoReply) {
            sql.append(" AND ThreadReplyCount = 0 ");
        }
        if (checkForumStatus) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.ForumStatus <> 1)");
        }
        if (status > -1) {
            sql.append(" AND ThreadStatus = ?");
        } else {
            AssertionUtil.doAssert(status >= -1, "Assertion in ThreadDAOImplJDBC.getNumberOfBeans.");
        }
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in ThreadDAOImplJDBC.getNumberOfBeans.");
        } if (category > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND thread.ForumID = ?) ");
        }

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            if (status > -1) {
                statement.setInt(1, status);
                if (category > -1) {
                    statement.setInt(2, category);
                } else if (forum > -1) {
                    statement.setInt(2, forum);
                }
            } else {
                if (category > -1) {
                    statement.setInt(1, category);
                } else if (forum > -1) {
                    statement.setInt(1, forum);
                }
            }

            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ThreadDAOImplJDBC.getNumberOfBeans.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNumberOfBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }



    /************************************************
     * Customized methods come below
     ************************************************/


    /**
     * This is a customized method
     */
    private int findThreadID(int forumID, String memberName, Timestamp threadCreationDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? AND MemberName = ? AND ThreadCreationDate = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            statement.setString(2, memberName);
            statement.setTimestamp(3, threadCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the ThreadID in table Thread.");
            }

            return resultSet.getInt("ThreadID");
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.findThreadID.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    public Collection getThreads_inFavorite_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT thread.ThreadID, thread.ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME + " thread, " + FavoriteThreadDAO.TABLE_NAME + " favorite ");
        sql.append(" WHERE thread.ThreadID = favorite.ThreadID AND favorite.MemberID = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getThreads_inFavorite_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getEnableThreads_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, boolean onlyNoReply, int status, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order, true, onlyNoReply, true, status, category, forum);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_withSortSupport_limit_noscroll(offset, rowsToReturn, sort, order, true, onlyNoReply, true, status, category, forum);
        }
        return getBeans_withSortSupport_limit_general(offset, rowsToReturn, sort, order, true, onlyNoReply, true, status, category, forum);
    }

    public Collection getEnableThreads_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, Timestamp from, Timestamp to)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ThreadPriority")) &&
            (!sort.equals("ThreadTopic")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order, from, to);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_withSortSupport_limit_noscroll(offset, rowsToReturn, sort, order, from, to);
        }
        return getBeans_withSortSupport_limit_general(offset, rowsToReturn, sort, order, from, to);
    }

    public Collection getDisableBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order, false, false, false, -1, -1, -1);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_withSortSupport_limit_noscroll(offset, rowsToReturn, sort, order, false, false, false, -1, -1, -1);
        }
        return getBeans_withSortSupport_limit_general(offset, rowsToReturn, sort, order, false, false, false, -1, -1, -1);
    }

    /*
     * Included columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic,
     *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate,
     *                   ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount,
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    private Collection getBeans_withSortSupport_limit_mysql(int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNoReply, boolean checkForumStatus, int status, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ForumID")) &&
            (!sort.equals("ThreadPriority")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);

        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME + " thread");
        if (checkForumStatus) {
            sql.append(", " + ForumDAO.TABLE_NAME + " forum");
        }
        if (enable) {
            sql.append(" WHERE ThreadStatus <> 1 ");
        } else {// disable
            sql.append(" WHERE ThreadStatus = 1 ");
        }
        if (onlyNoReply) {
            sql.append(" AND ThreadReplyCount = 0 ");
        }
        if (checkForumStatus) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.ForumStatus <> " + ForumBean.FORUM_STATUS_DISABLED + ")");
        }
        if (status > -1) {
            sql.append(" AND ThreadStatus = ?");
        } else {
            AssertionUtil.doAssert(status >= -1, "Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
        }
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
        } else if (category > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND thread.ForumID = ?) ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            //ERROR HERE, CHECK AGAIN
            int i = 0;
            if (status > -1) {
                statement.setInt(++i, status);
                if (category > -1) {
                    statement.setInt(++i, category);
                } else if (forum > -1) {
                    statement.setInt(++i, forum);
                }
            } else {
                if (category > -1) {
                    statement.setInt(++i, category);
                } else if (forum > -1) {
                    statement.setInt(++i, forum);
                }
            }
            statement.setInt(++i, offset);
            statement.setInt(++i, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private Collection getBeans_withSortSupport_limit_mysql(int offset, int rowsToReturn, String sort, String order, Timestamp from, Timestamp to)
        throws IllegalArgumentException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);

        // minhnn on 21 June 2010: I think that we only need to select from 1 table mvnforumThread and no need GROUP BY here
        sql.append("SELECT t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadType, t.ThreadStatus, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");// postgreSQL need AS
        sql.append(" FROM ").append(TABLE_NAME).append(" t, ").append(PostDAO.TABLE_NAME).append(" p,").append(ForumDAO.TABLE_NAME).append(" f");
        sql.append(" WHERE (t.ThreadID = p.ThreadID");
        sql.append("   AND p.ForumID = f.ForumID");
        sql.append("   AND t.ThreadCreationDate >= ? AND t.ThreadCreationDate <= ?)");
        sql.append(" GROUP BY t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadStatus, t.ThreadType, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            statement.setInt(3, offset);
            statement.setInt(4, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    private Collection getBeans_withSortSupport_limit_noscroll(int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNoReply, boolean checkForumStatus, int status, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ForumID")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME + " thread");
        if (checkForumStatus) {
            sql.append(", " + ForumDAO.TABLE_NAME + " forum");
        }
        if (enable) {
            sql.append(" WHERE ThreadStatus <> 1 ");
        } else {// disable
            sql.append(" WHERE ThreadStatus = 1 ");
        }
        if (onlyNoReply) {
            sql.append(" AND ThreadReplyCount = 0 ");
        }
        if (checkForumStatus) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.ForumStatus <> " + ForumBean.FORUM_STATUS_DISABLED + ")");
        }
        if (status > -1) {
            sql.append(" AND ThreadStatus = ?" );
        } else {
            AssertionUtil.doAssert(status >= -1, "Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
        }
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
        } if (category > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND thread.ForumID = ?) ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            if (status > -1) {
                statement.setInt(1, status);
                if (category > -1) {
                    statement.setInt(2, category);
                } else if (forum > -1) {
                    statement.setInt(2, forum);
                }
            } else {
                if (category > -1) {
                    statement.setInt(1, category);
                } else if (forum > -1) {
                    statement.setInt(1, forum);
                }
            }

            statement.setMaxRows(offset + rowsToReturn);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private Collection getBeans_withSortSupport_limit_noscroll(int offset, int rowsToReturn, String sort, String order, Timestamp from, Timestamp to)
        throws IllegalArgumentException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        
        // minhnn on 21 June 2010: I think that we only need to select from 1 table mvnforumThread and no need GROUP BY here
        sql.append("SELECT t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadType, t.ThreadStatus, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");// postgreSQL need AS
        sql.append(" FROM ").append(TABLE_NAME).append(" t, ").append(PostDAO.TABLE_NAME).append(" p, ").append(ForumDAO.TABLE_NAME).append(" f");
        sql.append(" WHERE (t.ThreadID = p.ThreadID");
        sql.append(" AND p.ForumID = f.ForumID");
        sql.append(" AND t.ThreadCreationDate >= ? AND t.ThreadCreationDate <= ?)");
        sql.append(" GROUP BY t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadStatus, t.ThreadType, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            statement.setMaxRows(offset + rowsToReturn);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic,
     *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate,
     *                   ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount,
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    private Collection getBeans_withSortSupport_limit_general(int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNoReply, boolean checkForumStatus, int status, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ForumID")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME + " thread");
        if (checkForumStatus) {
            sql.append(", " + ForumDAO.TABLE_NAME + " forum");
        }
        if (enable) {
            sql.append(" WHERE ThreadStatus <> 1 ");
        } else {// disable
            sql.append(" WHERE ThreadStatus = 1 ");
        }
        if (onlyNoReply) {
            sql.append(" AND ThreadReplyCount = 0 ");
        }
        if (checkForumStatus) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.ForumStatus <> " + ForumBean.FORUM_STATUS_DISABLED + ")");
        }
        if (status > -1) {
            sql.append(" AND ThreadStatus = ?" );
        } else {
            AssertionUtil.doAssert(status >= -1, "Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_general.");
        }
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_general.");
        } if (category > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND (forum.ForumID = thread.ForumID AND thread.ForumID = ?) ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (status > -1) {
                statement.setInt(1, status);
                if (category > -1) {
                    statement.setInt(2, category);
                } else if (forum > -1) {
                    statement.setInt(2, forum);
                }
            } else {
                if (category > -1) {
                    statement.setInt(1, category);
                } else if (forum > -1) {
                    statement.setInt(1, forum);
                }
            }

            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private Collection getBeans_withSortSupport_limit_general(int offset, int rowsToReturn, String sort, String order, Timestamp from, Timestamp to)
        throws IllegalArgumentException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        
        // minhnn on 21 June 2010: I think that we only need to select from 1 table mvnforumThread and no need GROUP BY here
        sql.append("SELECT t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadType, t.ThreadStatus, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");// postgreSQL need AS
        sql.append(" FROM ").append(TABLE_NAME).append(" t, ").append(PostDAO.TABLE_NAME).append(" p, ").append(ForumDAO.TABLE_NAME).append(" f");
        sql.append(" WHERE (t.ThreadID = p.ThreadID");
        sql.append(" AND p.ForumID = f.ForumID");
        sql.append(" AND t.ThreadCreationDate >= ? AND t.ThreadCreationDate <= ?)");
        sql.append(" GROUP BY t.ThreadID, t.MemberName, t.LastPostMemberName, t.ThreadCreationDate, t.ThreadLastPostDate, t.ThreadViewCount, t.ThreadReplyCount, t.ThreadStatus, t.ThreadType, t.ThreadPriority, t.ThreadTopic, t.ThreadAttachCount");
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_withSortSupport_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getNormalEnableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inForum_withSortSupport_limit_mysql(forumID, offset, rowsToReturn, sort, order, true, true);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inForum_withSortSupport_limit_noscroll(forumID, offset, rowsToReturn, sort, order, true, true);
        }
        return getBeans_inForum_withSortSupport_limit_general(forumID, offset, rowsToReturn, sort, order, true, true);
    }

    public Collection getAllEnableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inForum_withSortSupport_limit_mysql(forumID, offset, rowsToReturn, sort, order, true, false);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inForum_withSortSupport_limit_noscroll(forumID, offset, rowsToReturn, sort, order, true, false);
        }
        return getBeans_inForum_withSortSupport_limit_general(forumID, offset, rowsToReturn, sort, order, true, false);
    }

    public Collection getDisableThreads_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inForum_withSortSupport_limit_mysql(forumID, offset, rowsToReturn, sort, order, false, false);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inForum_withSortSupport_limit_noscroll(forumID, offset, rowsToReturn, sort, order, false, false);
        }
        return getBeans_inForum_withSortSupport_limit_general(forumID, offset, rowsToReturn, sort, order, false, false);
    }

    /*
     * Included columns: ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     * Excluded columns: ForumID
     */
    /**
     * Note: This is a customized method
     */
    private Collection getBeans_inForum_withSortSupport_limit_mysql(int forumID, int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNormalThread)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ThreadPriority")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? ");
        if (onlyNormalThread) {
            sql.append(" AND ThreadType = ").append(ThreadBean.THREAD_TYPE_DEFAULT);
        }
        if (enable) {
            sql.append(" AND ThreadStatus <> 1 ");
        } else {//disable
            sql.append(" AND ThreadStatus = 1 ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            statement.setInt(2, offset);
            statement.setInt(3, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(forumID);
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_inForum_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     * Excluded columns: ForumID
     */
    /**
     * Note: This is a customized method
     */
    private Collection getBeans_inForum_withSortSupport_limit_noscroll(int forumID, int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNormalThread)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ThreadPriority")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? ");
        if (onlyNormalThread) {
            sql.append(" AND ThreadType = ").append(ThreadBean.THREAD_TYPE_DEFAULT);
        }
        if (enable) {
            sql.append(" AND ThreadStatus <> 1 ");
        } else {//disable
            sql.append(" AND ThreadStatus = 1 ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setMaxRows(offset + rowsToReturn);
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(forumID);
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_inForum_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody,
     *                   ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType,
     *                   ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,
     *                   ThreadIcon, ThreadDuration
     * Excluded columns: ForumID
     */
    /**
     * Note: This is a customized method
     */
    private Collection getBeans_inForum_withSortSupport_limit_general(int forumID, int offset, int rowsToReturn, String sort, String order, boolean enable, boolean onlyNormalThread)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ThreadPriority")) &&
            (!sort.equals("ThreadViewCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? ");
        if (onlyNormalThread) {
            sql.append(" AND ThreadType = ").append(ThreadBean.THREAD_TYPE_DEFAULT);
        }
        if (enable) {
            sql.append(" AND ThreadStatus <> 1 ");
        } else {//disable
            sql.append(" AND ThreadStatus = 1 ");
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, forumID);
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(forumID);
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getBeans_inForum_withSortSupport_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID
     */
    public void updateForumID(int threadID, // primary key
                               int forumID)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // If this method does not change the foreign key columns, you can comment this block of code.
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot update table 'Thread'.");
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

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateForumID.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadStatus
     */
    public void updateThreadStatus(int threadID, // primary key
                                   int threadStatus)
        throws ObjectNotFoundException, DatabaseException {

        ThreadBean.validateThreadStatus(threadStatus);

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadStatus = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, threadStatus);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread (ThreadStatus) where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateThreadStatus.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ThreadType
     */
    public void updateThreadType(int threadID, // primary key
                                 int threadType)
        throws ObjectNotFoundException, DatabaseException {

        ThreadBean.validateThreadType(threadType);

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadType = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, threadType);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread (ThreadType) where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateThreadType.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that threadID is in database
     */
    public void increaseViewCount(int threadID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ThreadViewCount = ThreadViewCount + 1 WHERE ThreadID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, threadID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ThreadViewCount in table Thread. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.increaseViewCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateReplyCount(int threadID, // primary key
                                 int threadReplyCount)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException {

        if (threadReplyCount < 0) {
            throw new IllegalArgumentException("Cannot update a negative reply count.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadReplyCount = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, threadReplyCount);

            // primary key column(s)
            statement.setInt(2, threadID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateReplyCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateThreadHasPoll(int threadID, int pollCount)
        throws ObjectNotFoundException, DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ThreadHasPoll = ?");
        sql.append(" WHERE ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, pollCount);
            statement.setInt(2, threadID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update ThreadHasPoll in table Thread where primary key = (" + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.updateThreadHasPoll.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getPreviousEnableThread(int forumID, int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT MAX(ThreadID) FROM " + TABLE_NAME + " WHERE ThreadID < ? AND ForumID = ? AND ThreadStatus <> 1 ";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, threadID);
            statement.setInt(2, forumID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Cannot get the previous thread of the thread you requested: ThreadID = " + threadID);
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getPreviousEnableThread.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNextEnableThread(int forumID, int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT MIN(ThreadID) FROM " + TABLE_NAME + " WHERE ThreadID > ? AND ForumID = ? AND ThreadStatus <> 1 ";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, threadID);
            statement.setInt(2, forumID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Cannot get the next thread of the thread you requested: ThreadID = " + threadID);
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNextEnableThread.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    public Collection getEnableThreads_inGlobal(Timestamp sinceDate)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration");
        sql.append(" FROM " + TABLE_NAME + " thread, " + ForumDAO.TABLE_NAME + " forum ");
        sql.append(" WHERE (thread.ThreadStatus <> 1) AND (thread.ForumID = forum.ForumID) AND (ThreadLastPostDate > ?) ");
        sql.append(" ORDER BY forum.CategoryID ASC, thread.ForumID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, sinceDate);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreads_inGlobal.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    public Collection getEnableThreads_inCategory(int categoryID, Timestamp sinceDate)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration");
        sql.append(" FROM " + TABLE_NAME + " thread, " + ForumDAO.TABLE_NAME + " forum ");
        sql.append(" WHERE (thread.ThreadStatus <> 1) AND (thread.ForumID = forum.ForumID) AND (forum.CategoryID = ?) AND (ThreadLastPostDate > ?) ");
        sql.append(" ORDER BY thread.ForumID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            statement.setTimestamp(2, sinceDate);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreads_inCategory.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    public Collection getEnableThreads_inForum(int forumID, Timestamp sinceDate)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE (ThreadStatus <> 1) AND (ForumID = ?) AND (ThreadLastPostDate > ?) ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            statement.setTimestamp(2, sinceDate);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreads_inForum.");
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
     *                   ThreadReplyCount, ThreadIcon, ThreadDuration
     * Excluded columns:
     */
    public Collection getEnableThreads_inThread(int threadID, Timestamp sinceDate)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE (ThreadStatus <> 1) AND (ThreadID = ?) AND (ThreadLastPostDate > ?) ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            statement.setTimestamp(2, sinceDate);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreads_inThread.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfEnableThreadsWithPendingPosts()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(DISTINCT thread.ThreadID)");
        sql.append(" FROM " + TABLE_NAME + " thread, " + PostDAO.TABLE_NAME + " post ");
        sql.append(" WHERE post.ThreadID = thread.ThreadID AND PostStatus = 1 AND ThreadStatus <> 1");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ThreadDAOImplJDBC.getNumberOfEnableThreadsWithPendingPosts.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNumberOfEnableThreadsWithPendingPosts.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfEnableThreadsWithPendingPosts_inForum(int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(DISTINCT thread.ThreadID)");
        sql.append(" FROM " + TABLE_NAME + " thread, " + PostDAO.TABLE_NAME + " post ");
        sql.append(" WHERE post.ThreadID = thread.ThreadID AND PostStatus = 1 AND ThreadStatus <> 1 AND thread.ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ThreadDAOImplJDBC.getNumberOfEnableThreadsWithPendingPosts_inForum.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNumberOfEnableThreadsWithPendingPosts_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

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
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException {

        return getEnableThreadsWithPendingPosts_withSortSupport_limit_noscroll(-1/*mean all forums*/, offset, rowsToReturn, sort, order);
    }

    /**
     * This method is used to get enable threads that having at least one pending post (for moderation)
     *
     * Note: current implementation use the NOSCROLL method which is quite slow
     * NOTE: This method is a hack for Oracle because of the error "Not a Group By expression"
     *       and when add to the group by clause, then it cannot group by a LONG column
     *
     * @param offset
     * @param rowsToReturn
     * @param sort
     * @param order
     * @return
     * @throws java.lang.IllegalArgumentException if the arguments are not valid
     * @throws DatabaseException
     */
    public Collection getEnableThreadsWithPendingPosts_inForum_withSortSupport_limit(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException {

        return getEnableThreadsWithPendingPosts_withSortSupport_limit_noscroll(forumID, offset, rowsToReturn, sort, order);
    }

    /**
     *
     * @param forumID the forumID to get threads, or -1 mean get threads in all forums
     */
    private Collection getEnableThreadsWithPendingPosts_withSortSupport_limit_noscroll(int forumID, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException, ObjectNotFoundException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ThreadLastPostDate")) &&
            (!sort.equals("ThreadCreationDate")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ThreadReplyCount")) &&
            (!sort.equals("ThreadViewCount")) &&
            (!sort.equals("ForumID")) &&
            (!sort.equals("ThreadPendingPostCount")) ) {//ThreadPendingPostCount get from GROUP BY clause
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT thread.ThreadID, thread.ForumID, thread.MemberName, ThreadCreationDate, ThreadLastPostDate, ThreadViewCount, ThreadReplyCount, COUNT(thread.ThreadID) AS ThreadPendingPostCount");
        sql.append(" FROM " + TABLE_NAME + " thread, " + PostDAO.TABLE_NAME + " post ");
        // PostStatus = 1 means pending(disabled) posts
        // ThreadStatus <> 1 means enable threads
        sql.append(" WHERE post.ThreadID = thread.ThreadID AND PostStatus = 1 AND ThreadStatus <> 1");
        if (forumID != -1) {
            sql.append(" AND thread.ForumID = ?");
        }
        sql.append(" GROUP BY thread.ThreadID, thread.ForumID, thread.MemberName, ThreadCreationDate, ThreadLastPostDate, ThreadViewCount, ThreadReplyCount");
        if (sort.equals("ThreadPendingPostCount")) {
            sql.append(" ORDER BY ThreadPendingPostCount " + order);// ColumnName, ASC|DESC
        } else {
            sql.append(" ORDER BY thread." + sort + " " + order);// ColumnName, ASC|DESC
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            if (forumID != -1) {
                statement.setInt(1, forumID);
            }
            statement.setMaxRows(offset + rowsToReturn);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadPendingPostCount(resultSet.getInt("ThreadPendingPostCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreadsHavingPendingPosts_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

        ArrayList newThreadBeans = new ArrayList();
        for (Iterator iter = retValue.iterator(); iter.hasNext(); ) {
            ThreadBean threadBean = (ThreadBean)iter.next();
            ThreadBean fullThreadBean = getThread(threadBean.getThreadID());
            fullThreadBean.setThreadPendingPostCount(threadBean.getThreadPendingPostCount());
            newThreadBeans.add(fullThreadBean);
        }
        return newThreadBeans;
    }

    private Collection getEnableThreads_inType_inForum(int forumID, int threadType, boolean checkForumStatus)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, thread.ForumID, MemberName, thread.LastPostMemberName, ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadPriority, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount, ThreadIcon, ThreadDuration, ThreadAttachCount");
        sql.append(" FROM " + TABLE_NAME + " thread, " + ForumDAO.TABLE_NAME + " forum ");
        sql.append(" WHERE thread.ForumID = forum.ForumID ");
        boolean isGlobalAnnoucement = ( (forumID == ALL_FORUMS) && (threadType == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT) );
        sql.append(" AND ThreadType = ? ");
        if (isGlobalAnnoucement == false) {
            sql.append(" AND thread.ForumID = ? ");
        }
        if (checkForumStatus) {
            sql.append(" AND forum.ForumStatus <> ").append(ForumBean.FORUM_STATUS_DISABLED);
        }
        sql.append(" AND ThreadStatus <> ").append(ThreadBean.THREAD_STATUS_DISABLED);
        sql.append(" ORDER BY ThreadLastPostDate DESC");
        // log.debug("SQL:: " + sql);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadType);
            if (isGlobalAnnoucement == false) {
                statement.setInt(2, forumID);
            }

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ThreadBean bean = new ThreadBean();
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setThreadTopic(resultSet.getString("ThreadTopic"));
                bean.setThreadBody(resultSet.getString("ThreadBody"));
                bean.setThreadVoteCount(resultSet.getInt("ThreadVoteCount"));
                bean.setThreadVoteTotalStars(resultSet.getInt("ThreadVoteTotalStars"));
                bean.setThreadCreationDate(resultSet.getTimestamp("ThreadCreationDate"));
                bean.setThreadLastPostDate(resultSet.getTimestamp("ThreadLastPostDate"));
                bean.setThreadType(resultSet.getInt("ThreadType"));
                bean.setThreadPriority(resultSet.getInt("ThreadPriority"));
                bean.setThreadOption(resultSet.getInt("ThreadOption"));
                bean.setThreadStatus(resultSet.getInt("ThreadStatus"));
                bean.setThreadHasPoll(resultSet.getInt("ThreadHasPoll"));
                bean.setThreadViewCount(resultSet.getInt("ThreadViewCount"));
                bean.setThreadReplyCount(resultSet.getInt("ThreadReplyCount"));
                bean.setThreadIcon(resultSet.getString("ThreadIcon"));
                bean.setThreadDuration(resultSet.getInt("ThreadDuration"));
                bean.setThreadAttachCount(resultSet.getInt("ThreadAttachCount"));
                retValue.add(bean);
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getEnableThreads_inType_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
        return retValue;
    }

    public Collection getEnableStickies_inForum(int forumID) throws DatabaseException {
        return this.getEnableThreads_inType_inForum(forumID, ThreadBean.THREAD_TYPE_STICKY, true);
    }

    public Collection getEnableForumAnnouncements_inForum(int forumID) throws DatabaseException {
        return this.getEnableThreads_inType_inForum(forumID, ThreadBean.THREAD_TYPE_FORUM_ANNOUNCEMENT, true);
    }

    public Collection getEnableGlobalAnnouncements() throws DatabaseException {
        return this.getEnableThreads_inType_inForum(ALL_FORUMS/* not belongs to any forum */, ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT, true);
    }

}// end of class ThreadDAOImplJDBC
