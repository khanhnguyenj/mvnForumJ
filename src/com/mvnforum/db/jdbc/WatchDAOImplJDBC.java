/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/WatchDAOImplJDBC.java,v 1.25 2009/01/02 03:17:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.25 $
 * $Date: 2009/01/02 03:17:31 $
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

import com.mvnforum.MVNForumConstant;
import com.mvnforum.db.*;

public class WatchDAOImplJDBC implements WatchDAO {

    private static final Logger log = LoggerFactory.getLogger(WatchDAOImplJDBC.class);

    public WatchDAOImplJDBC() {
    }

    public void findByPrimaryKey(int watchID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE WatchID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, watchID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + watchID + ") in table 'Watch'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, CategoryID, ForumID, ThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND CategoryID = ? AND ForumID = ? AND ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, categoryID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [MemberID, CategoryID, ForumID, ThreadID] (" + memberID + ", " + categoryID + ", " + forumID + ", " + threadID + ") in table 'Watch'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, CategoryID, ForumID, ThreadID, WatchType,
     *                   WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate
     * Excluded columns: WatchID
     */
    public void create(int memberID, int categoryID, int forumID,
                       int threadID, int watchType, int watchOption,
                       int watchStatus, Timestamp watchCreationDate, Timestamp watchLastSentDate,
                       Timestamp watchEndDate)
        throws IllegalArgumentException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        if ((memberID == 0) || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST)) {
            throw new IllegalArgumentException("Cannot add a new watch for Guest (id = " + memberID + ")");
        }
        int notZeroCount = 0;
        if (categoryID != 0) {
            notZeroCount++;
        }
        if (forumID != 0) {
            notZeroCount++;
        }
        if (threadID != 0) {
            notZeroCount++;
        }
        if (notZeroCount > 1) {
            throw new IllegalArgumentException("Cannot add watch with more than 1 element.");
        }

        // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(memberID, categoryID, forumID, threadID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Watch with the same [MemberID, CategoryID, ForumID, ThreadID] (" + memberID + ", " + categoryID + ", " + forumID + ", " + threadID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (categoryID != 0) {
                DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Category' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (forumID != 0) {
                DAOFactory.getForumDAO().findByPrimaryKey(forumID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (threadID != 0) {
                DAOFactory.getThreadDAO().findByPrimaryKey(threadID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Thread' does not exist. Cannot create new Watch.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, CategoryID, ForumID, ThreadID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setInt(2, categoryID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            statement.setInt(5, watchType);
            statement.setInt(6, watchOption);
            statement.setInt(7, watchStatus);
            statement.setTimestamp(8, watchCreationDate);
            statement.setTimestamp(9, watchLastSentDate);
            statement.setTimestamp(10, watchEndDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Watch'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int watchID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE WatchID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, watchID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Watch where primary key = (" + watchID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.delete.");
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
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.delete_inMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inCategory(int categoryID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.delete_inCategory.");
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
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.delete_inForum.");
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
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.delete_inThread.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: WatchLastSentDate
     * Excluded columns: WatchID, MemberID, CategoryID, ForumID, ThreadID,
     *                   WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchEndDate
     */
    public void updateLastSentDate(int watchID, // primary key
                        Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET WatchLastSentDate = ?");
        sql.append(" WHERE WatchID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, watchLastSentDate);

            // primary key column(s)
            statement.setInt(2, watchID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Watch where primary key = (" + watchID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.updateLastSentDate.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, CategoryID, ForumID, ThreadID, WatchType,
     *                   WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate
     * Excluded columns: WatchID
     */
    public WatchBean getWatch(int watchID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, CategoryID, ForumID, ThreadID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE WatchID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, watchID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Watch where primary key = (" + watchID + ").");
            }

            WatchBean bean = new WatchBean();
            // @todo: uncomment the following line(s) as needed
            bean.setWatchID(watchID);
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setCategoryID(resultSet.getInt("CategoryID"));
            bean.setForumID(resultSet.getInt("ForumID"));
            bean.setThreadID(resultSet.getInt("ThreadID"));
            bean.setWatchType(resultSet.getInt("WatchType"));
            bean.setWatchOption(resultSet.getInt("WatchOption"));
            bean.setWatchStatus(resultSet.getInt("WatchStatus"));
            bean.setWatchCreationDate(resultSet.getTimestamp("WatchCreationDate"));
            bean.setWatchLastSentDate(resultSet.getTimestamp("WatchLastSentDate"));
            bean.setWatchEndDate(resultSet.getTimestamp("WatchEndDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getWatch(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: WatchID, WatchType, WatchOption, WatchStatus, WatchCreationDate,
     *                   WatchLastSentDate, WatchEndDate
     * Excluded columns: MemberID, CategoryID, ForumID, ThreadID
     */
    public WatchBean getWatch_byAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND CategoryID = ? AND ForumID = ? AND ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, categoryID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Watch where alternate key [MemberID, CategoryID, ForumID, ThreadID] = (" + memberID + ", " + categoryID + ", " + forumID + ", " + threadID + ").");
            }

            WatchBean bean = new WatchBean();
            // @todo: uncomment the following line(s) as needed
            bean.setMemberID(memberID);
            bean.setCategoryID(categoryID);
            bean.setForumID(forumID);
            bean.setThreadID(threadID);
            bean.setWatchID(resultSet.getInt("WatchID"));
            bean.setWatchType(resultSet.getInt("WatchType"));
            bean.setWatchOption(resultSet.getInt("WatchOption"));
            bean.setWatchStatus(resultSet.getInt("WatchStatus"));
            bean.setWatchCreationDate(resultSet.getTimestamp("WatchCreationDate"));
            bean.setWatchLastSentDate(resultSet.getTimestamp("WatchLastSentDate"));
            bean.setWatchEndDate(resultSet.getTimestamp("WatchEndDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getWatch_byAlternateKey_MemberID_CategoryID_ForumID_ThreadID(ak).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: WatchID, MemberID, CategoryID, ForumID, ThreadID,
     *                   WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate,
     *                   WatchEndDate
     * Excluded columns:
     */
    public Collection getWatches()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchID, MemberID, CategoryID, ForumID, ThreadID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WatchBean bean = new WatchBean();
                bean.setWatchID(resultSet.getInt("WatchID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setWatchType(resultSet.getInt("WatchType"));
                bean.setWatchOption(resultSet.getInt("WatchOption"));
                bean.setWatchStatus(resultSet.getInt("WatchStatus"));
                bean.setWatchCreationDate(resultSet.getTimestamp("WatchCreationDate"));
                bean.setWatchLastSentDate(resultSet.getTimestamp("WatchLastSentDate"));
                bean.setWatchEndDate(resultSet.getTimestamp("WatchEndDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getWatchs.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfWatches()
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
            AssertionUtil.doAssert(resultSet.next(), "Assertion in WatchDAOImplJDBC.getNumberOfWatches.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getNumberOfWatches.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfWatches_forMember(int memberID)
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
            AssertionUtil.doAssert(resultSet.next(), "Assertion in WatchDAOImplJDBC.getNumberOfWatches_forMember.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getNumberOfWatches_forMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

/***************************************************************************
 * Customized methods come below
 ***************************************************************************/

    /*
     * Included columns: MemberID, WatchLastSentDate
     * Excluded columns: WatchID, CategoryID, ForumID, ThreadID, WatchType,
     *                   WatchOption, WatchStatus, WatchCreationDate, WatchEndDate
     */
    public Collection getMemberBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT MemberID, MIN(WatchLastSentDate) AS lastsent");// postgreSQL need AS
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" GROUP BY MemberID ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WatchBean bean = new WatchBean();
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setWatchLastSentDate(resultSet.getTimestamp("lastsent"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getMemberBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: WatchID, MemberID, CategoryID, ForumID, ThreadID,
     *                   WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate,
     *                   WatchEndDate
     * Excluded columns:
     */
    public Collection getWatches_forMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchID, MemberID, CategoryID, ForumID, ThreadID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WatchBean bean = new WatchBean();
                bean.setWatchID(resultSet.getInt("WatchID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setWatchType(resultSet.getInt("WatchType"));
                bean.setWatchOption(resultSet.getInt("WatchOption"));
                bean.setWatchStatus(resultSet.getInt("WatchStatus"));
                bean.setWatchCreationDate(resultSet.getTimestamp("WatchCreationDate"));
                bean.setWatchLastSentDate(resultSet.getTimestamp("WatchLastSentDate"));
                bean.setWatchEndDate(resultSet.getTimestamp("WatchEndDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.getWatches_forMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: WatchLastSentDate
     * Excluded columns: WatchID, MemberID, CategoryID, ForumID, ThreadID,
     *                   WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchEndDate
     */
    public void updateLastSentDate_forMember(int memberID,
                        Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET WatchLastSentDate = ?");
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, watchLastSentDate);

            // primary key column(s)
            statement.setInt(2, memberID);

            if (statement.executeUpdate() < 1) {
                throw new ObjectNotFoundException("Cannot update table Watch where primary key = (" + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.updateLastSentDate_forMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateWatchType(int watchID, int watchType)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET WatchType = ?");
        sql.append(" WHERE WatchID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, watchType);
            statement.setInt(2, watchID);

            if (statement.executeUpdate() < 1) {
                throw new ObjectNotFoundException("Cannot update table Watch where primary key = (" + watchID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.updateWatchType.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class WatchDAOImplJDBC
