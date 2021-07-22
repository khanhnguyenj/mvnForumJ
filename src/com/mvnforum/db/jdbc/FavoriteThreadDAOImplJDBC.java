/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/FavoriteThreadDAOImplJDBC.java,v 1.18 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.18 $
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
 * @author: Mai  Nguyen
 */
package com.mvnforum.db.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class FavoriteThreadDAOImplJDBC implements FavoriteThreadDAO {

    private static final Logger log = LoggerFactory.getLogger(FavoriteThreadDAOImplJDBC.class);

    public FavoriteThreadDAOImplJDBC() {
    }

    public void findByPrimaryKey(int memberID, int threadID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, ThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + memberID + ", " + threadID + ") in table 'FavoriteThread'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, ThreadID, ForumID, FavoriteCreationDate, FavoriteType,
     *                   FavoriteOption, FavoriteStatus
     * Excluded columns:
     */
    public void create(int memberID, int threadID, int forumID,
                        Timestamp favoriteCreationDate, int favoriteType, int favoriteOption,
                        int favoriteStatus)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(memberID, threadID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new FavoriteThread with the same [MemberID, ThreadID] (" + memberID + ", " + threadID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnforumMember' does not exist. Cannot create new FavoriteThread.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getThreadDAO().findByPrimaryKey(threadID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnforumThread' does not exist. Cannot create new FavoriteThread.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnforumForum' does not exist. Cannot create new FavoriteThread.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, ThreadID, ForumID, FavoriteCreationDate, FavoriteType, FavoriteOption, FavoriteStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setInt(2, threadID);
            statement.setInt(3, forumID);
            statement.setTimestamp(4, favoriteCreationDate);
            statement.setInt(5, favoriteType);
            statement.setInt(6, favoriteOption);
            statement.setInt(7, favoriteStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'FavoriteThread'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.create.");
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
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot update table 'FavoriteThread'.");
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
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.update_ForumID_inThread.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int memberID, int threadID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND ThreadID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, threadID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table FavoriteThread where primary key = (" + memberID + ", " + threadID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.delete.");
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
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.delete_inThread.");
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
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.delete_inForum.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.delete_inMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }


    /*
     * Included columns: MemberID, ThreadID, FavoriteCreationDate, FavoriteType, FavoriteOption,
     *                   FavoriteStatus
     * Excluded columns:
     */
    public Collection getFavoriteThreads_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, ThreadID, FavoriteCreationDate, FavoriteType, FavoriteOption, FavoriteStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        sql.append(" ORDER BY ThreadID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FavoriteThreadBean bean = new FavoriteThreadBean();
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setFavoriteCreationDate(resultSet.getTimestamp("FavoriteCreationDate"));
                bean.setFavoriteType(resultSet.getInt("FavoriteType"));
                bean.setFavoriteOption(resultSet.getInt("FavoriteOption"));
                bean.setFavoriteStatus(resultSet.getInt("FavoriteStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.getFavoriteThreads_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfFavoriteThreads_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in FavoriteThreadDAOImplJDBC.getNumberOfFavoriteThreads_inMember.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in FavoriteThreadDAOImplJDBC.getNumberOfFavoriteThreads_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class FavoriteThreadDAOImplJDBC
