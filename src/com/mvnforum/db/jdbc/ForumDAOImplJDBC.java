/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/ForumDAOImplJDBC.java,v 1.49 2009/02/24 05:05:15 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.49 $
 * $Date: 2009/02/24 05:05:15 $
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
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class ForumDAOImplJDBC implements ForumDAO {

    private static final Logger log = LoggerFactory.getLogger(ForumDAOImplJDBC.class);

    public ForumDAOImplJDBC() {
    }

    public void findByPrimaryKey(int forumID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + forumID + ") in table 'Forum'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_ForumName_CategoryID(String forumName, int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumName, CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE lower(ForumName) = lower(?) AND CategoryID = ?");
        } else {
            sql.append(" WHERE ForumName = ? AND CategoryID = ?");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, forumName);
            statement.setInt(2, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [ForumName, CategoryID] (" + forumName + ", " + categoryID + ") in table 'Forum'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.findByAlternateKey_ForumName_CategoryID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate,
     *                   ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption,
     *                   ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount,
     *                   ForumPostCount
     * Excluded columns: ForumID
     */
    public void create(int categoryID, String forumOwnerName, String lastPostMemberName,
                        String forumName, String forumDesc, Timestamp forumCreationDate,
                        Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder,
                        int forumType, int forumFormatOption, int forumOption,
                        int forumStatus, int forumModerationMode, String forumPassword,
                        int forumThreadCount, int forumPostCount)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

         // check valid
         ForumBean.validateForumType(forumType);
         ForumBean.validateForumFormatOption(forumFormatOption);
         ForumBean.validateForumOption(forumOption);
         ForumBean.validateForumStatus(forumStatus);
         ForumBean.validateForumModerationMode(forumModerationMode);

        // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_ForumName_CategoryID(forumName, categoryID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Forum with the same [ForumName, CategoryID] (" + forumName + ", " + categoryID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Category' does not exist. Cannot create new Forum.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (forumOwnerName.length() > 0) {
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(forumOwnerName);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Forum.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, categoryID);
            statement.setString(2, forumOwnerName);
            statement.setString(3, lastPostMemberName);
            statement.setString(4, forumName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(5, new StringReader(forumDesc), forumDesc.length());
            } else {
                statement.setString(5, forumDesc);
            }
            statement.setTimestamp(6, forumCreationDate);
            statement.setTimestamp(7, forumModifiedDate);
            statement.setTimestamp(8, forumLastPostDate);
            statement.setInt(9, forumOrder);
            statement.setInt(10, forumType);
            statement.setInt(11, forumFormatOption);
            statement.setInt(12, forumOption);
            statement.setInt(13, forumStatus);
            statement.setInt(14, forumModerationMode);
            statement.setString(15, forumPassword);
            statement.setInt(16, forumThreadCount);
            statement.setInt(17, forumPostCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Forum'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryID, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate,
     *                   ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption,
     *                   ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount,
     *                   ForumPostCount
     * Excluded columns: ForumID
     */
    public int createForum(int categoryID, String forumOwnerName, String lastPostMemberName,
                        String forumName, String forumDesc, Timestamp forumCreationDate,
                        Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder,
                        int forumType, int forumFormatOption, int forumOption,
                        int forumStatus, int forumModerationMode, String forumPassword,
                        int forumThreadCount, int forumPostCount)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

         create(categoryID, forumOwnerName, lastPostMemberName, forumName, forumDesc, forumCreationDate, forumModifiedDate, forumLastPostDate,
                forumOrder, forumType, forumFormatOption, forumOption, forumStatus, forumModerationMode, forumPassword, forumThreadCount, forumPostCount);

         ForumBean forumBean = null;
         try {
             forumBean = getBean_byAlternateKey_ForumName_CategoryID(forumName, categoryID);
         } catch (ObjectNotFoundException ex) {
             log.error("Cannot find ForumID after creating forum.", ex);
         }
         return forumBean.getForumID();
    }

    public void delete(int forumID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Forum where primary key = (" + forumID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryID, ForumOwnerName, ForumName, ForumDesc, ForumModifiedDate, ForumOrder,
     *                   ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode
     * Excluded columns: ForumID, LastPostMemberName, ForumCreationDate, ForumLastPostDate, ForumPassword,
     *                   ForumThreadCount, ForumPostCount
     */
    public void update(int forumID, // primary key
                       int categoryID, String forumOwnerName, String forumName, String forumDesc,
                       Timestamp forumModifiedDate, int forumOrder, int forumType,
                       int forumFormatOption, int forumOption, int forumStatus,
                       int forumModerationMode)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, IllegalArgumentException {

        // check valid
        ForumBean.validateForumType(forumType);
        ForumBean.validateForumFormatOption(forumFormatOption);
        ForumBean.validateForumOption(forumOption);
        ForumBean.validateForumStatus(forumStatus);
        ForumBean.validateForumModerationMode(forumModerationMode);

        ForumBean bean = getForum(forumID);

        if ( (forumName.equalsIgnoreCase(bean.getForumName()) == false) ||
             (categoryID != bean.getCategoryID()) ) {
            // Forum tries to change its alternate key <ForumName, CategoryID>, so we must check if it already exist
            try {
                findByAlternateKey_ForumName_CategoryID(forumName, categoryID);
                throw new DuplicateKeyException("Alternate key [ForumName, CategoryID] (" + forumName + ", " + categoryID + ") already exists. Cannot update Forum.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
        }

        try {
            // @todo: modify the parameter list as needed
            // If this method does not change the foreign key columns, you can comment this block of code.
            DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Category' does not exist. Cannot update table 'Forum'.");
        }

        try {
            // @todo : modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (forumOwnerName.length() > 0) {
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(forumOwnerName);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot update Forum.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET CategoryID = ?, ForumOwnerName = ?, ForumName = ?, ForumDesc = ?, ForumModifiedDate = ?, ForumOrder = ?, ForumType = ?, ForumFormatOption = ?, ForumOption = ?, ForumStatus = ?, ForumModerationMode = ?");
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, categoryID);
            statement.setString(2, forumOwnerName);
            statement.setString(3, forumName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(4, new StringReader(forumDesc), forumDesc.length());
            } else {
                statement.setString(4, forumDesc);
            }
            statement.setTimestamp(5, forumModifiedDate);
            statement.setInt(6, forumOrder);
            statement.setInt(7, forumType);
            statement.setInt(8, forumFormatOption);
            statement.setInt(9, forumOption);
            statement.setInt(10, forumStatus);
            statement.setInt(11, forumModerationMode);

            // primary key column(s)
            statement.setInt(12, forumID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Forum where primary key = (" + forumID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: LastPostMemberName
     * Excluded columns: ForumID, CategoryID, ForumOwnerName, ForumName, ForumDesc, ForumCreationDate,
     *                   ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption,
     *                   ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount,
     *                   ForumPostCount
     */
    public void updateLastPostMemberName(int forumID, // primary key
                        String lastPostMemberName)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        //allow anonymous/guests to send posts (if admin allows that)
        if ( (lastPostMemberName!=null) && (lastPostMemberName.length()>0) ) {
            try {
                // @todo: modify the parameter list as needed
                // If this method does not change the foreign key columns, you can comment this block of code.
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(lastPostMemberName);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot update table 'Forum'.");
            }
        } else {
            lastPostMemberName = ""; //so we don't get 'null' in sql query
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET LastPostMemberName = ?");
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, lastPostMemberName);

            // primary key column(s)
            statement.setInt(2, forumID);

            if (statement.executeUpdate() != 1) {
                // Some drivers don't update database if it detect old and new data are the same
                // @todo: should check driver, not check database
                // Currently there is only one driver: Caucho MySql driver
                if (DBUtils.getDatabaseType() != DBUtils.DATABASE_MYSQL) {
                    throw new ObjectNotFoundException("Cannot update table Forum where primary key = (" + forumID + ").");
                } else {
                    log.warn("WARNING: By pass the check for Caucho MySql driver.");
                }
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.updateLastPostMemberName.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumLastPostDate
     * Excluded columns: ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc,
     *                   ForumCreationDate, ForumModifiedDate, ForumOrder, ForumType, ForumFormatOption,
     *                   ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount,
     *                   ForumPostCount
     */
    public void updateLastPostDate(int forumID, // primary key
                        Timestamp forumLastPostDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ForumLastPostDate = ?");
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, forumLastPostDate);

            // primary key column(s)
            statement.setInt(2, forumID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Forum where primary key = (" + forumID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.updateLastPostDate.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void resetForumOwnerNameWhenDeleteMember(String memberName)
        throws DatabaseException {

        if ( (memberName == null) || (memberName.length() == 0) ) {
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" SET ForumOwnerName = '' WHERE lower(ForumOwnerName) = lower(?)");
        } else {
            sql.append(" SET ForumOwnerName = '' WHERE ForumOwnerName = ?");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, memberName);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            log.error("SQL execution error!", sqlException);
            throw new DatabaseException("Error when executing SQL in ForumDAOImplJDBC.resetForumOwnerNameWhenDeleteMember");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumThreadCount, ForumPostCount
     * Excluded columns: ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc,
     *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType,
     *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword
     */
    public void updateStatistics(int forumID, // primary key
                        int forumThreadCount, int forumPostCount)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ForumThreadCount = ?, ForumPostCount = ?");
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, forumThreadCount);
            statement.setInt(2, forumPostCount);

            // primary key column(s)
            statement.setInt(3, forumID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Forum where primary key = (" + forumID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.updateStatistics.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that threadID is in database
     */
    public void increasePostCount(int forumID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ForumPostCount = ForumPostCount + 1 WHERE ForumID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ForumPostCount in table Forum. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.increasePostCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that threadID is in database
     */
    public void increaseThreadCount(int forumID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ForumThreadCount = ForumThreadCount + 1 WHERE ForumID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ForumThreadCount in table Forum. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.increaseThreadCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that threadID is in database
     */
    public void decreaseThreadCount(int forumID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ForumThreadCount = ForumThreadCount - 1 WHERE ForumID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ForumThreadCount in table Forum. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.decreaseThreadCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate,
     *                   ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption,
     *                   ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount,
     *                   ForumPostCount
     * Excluded columns: ForumID
     */
    public ForumBean getForum(int forumID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Forum where primary key = (" + forumID + ").");
            }

            ForumBean bean = new ForumBean();
            // @todo: uncomment the following line(s) as needed
            bean.setForumID(forumID);
            bean.setCategoryID(resultSet.getInt("CategoryID"));
            bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
            bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
            bean.setForumName(resultSet.getString("ForumName"));
            bean.setForumDesc(resultSet.getString("ForumDesc"));
            bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
            bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
            bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
            bean.setForumOrder(resultSet.getInt("ForumOrder"));
            bean.setForumType(resultSet.getInt("ForumType"));
            bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
            bean.setForumOption(resultSet.getInt("ForumOption"));
            bean.setForumStatus(resultSet.getInt("ForumStatus"));
            bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
            bean.setForumPassword(resultSet.getString("ForumPassword"));
            bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
            bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getForum(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc,
     *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType,
     *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword,
     *                   ForumThreadCount, ForumPostCount
     * Excluded columns:
     */
    public Collection getForums()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY CategoryID ASC, ForumOrder ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getForums.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc,
     *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType,
     *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword,
     *                   ForumThreadCount, ForumPostCount
     * Excluded columns:
     */
    public Collection getForums_inCategory(int categoryID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");
        sql.append(" ORDER BY CategoryID ASC, ForumOrder ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getForums_inCategory.");
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
     * This method should be call only when we can make sure that memberID is in database
     */
    public void decreaseForumOrder(int forumID, Timestamp forumModifiedDate)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ForumOrder = ForumOrder - 1, ForumModifiedDate = ? WHERE ForumID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, forumModifiedDate);
            statement.setInt(2, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ForumOrder in table Forum. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.decreaseForumOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that memberID is in database
     */
    public void increaseForumOrder(int forumID, Timestamp forumModifiedDate)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET ForumOrder = ForumOrder + 1, ForumModifiedDate = ? WHERE ForumID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, forumModifiedDate);
            statement.setInt(2, forumID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the ForumOrder in table Forum. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.increaseForumOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc,
     *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType,
     *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword,
     *                   ForumThreadCount, ForumPostCount
     * Excluded columns:
     */
    protected static ForumBean getBean_byAlternateKey_ForumName_CategoryID(String forumName, int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumName = ? AND CategoryID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, forumName);
            statement.setInt(2, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Forum where alternate key [ForumName, CategoryID] = (" + forumName + ", " + categoryID + ").");
            }

            ForumBean bean = new ForumBean();
            // @todo: uncomment the following line(s) as needed
            //bean.setForumName(forumName);
            //bean.setCategoryID(categoryID);
            bean.setForumID(resultSet.getInt("ForumID"));
            bean.setCategoryID(resultSet.getInt("CategoryID"));
            bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
            bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
            bean.setForumName(resultSet.getString("ForumName"));
            bean.setForumDesc(resultSet.getString("ForumDesc"));
            bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
            bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
            bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
            bean.setForumOrder(resultSet.getInt("ForumOrder"));
            bean.setForumType(resultSet.getInt("ForumType"));
            bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
            bean.setForumOption(resultSet.getInt("ForumOption"));
            bean.setForumStatus(resultSet.getInt("ForumStatus"));
            bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
            bean.setForumPassword(resultSet.getString("ForumPassword"));
            bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
            bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getBean_byAlternateKey_ForumName_CategoryID(ak).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     *
     */
    public Collection getBeans_withSortSupport_limit_general(int offset, int rowsToReturn, String sort, String order) throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ForumLastPostDate")) &&
            (!sort.equals("ForumCreationDate")) &&
            (!sort.equals("ForumName")) &&
            (!sort.equals("ForumPostCount")) &&
            (!sort.equals("ForumThreadCount")) ) {
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

        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getBeans_withSortSupport_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     *
     */
    public Collection getBeans_withSortSupport_limit_mysql(int offset, int rowsToReturn, String sort, String order) 
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ForumLastPostDate")) &&
            (!sort.equals("ForumCreationDate")) &&
            (!sort.equals("ForumName")) &&
            (!sort.equals("ForumPostCount")) &&
            (!sort.equals("ForumThreadCount")) ) {
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

        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setMaxRows(offset + rowsToReturn);
            statement.setInt(1, offset);
            statement.setInt(2, rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            //System.out.println("===sql mysql: " + sql);
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     *
     */
    public Collection getBeans_withSortSupport_limit_noscroll(int offset, int rowsToReturn, String sort, String order) 
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ForumLastPostDate")) &&
            (!sort.equals("ForumCreationDate")) &&
            (!sort.equals("ForumName")) &&
            (!sort.equals("ForumPostCount")) &&
            (!sort.equals("ForumThreadCount")) ) {
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

        sql.append("SELECT ForumID, CategoryID, ForumOwnerName, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            System.out.println("===sql noscroll: " + sql);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     *
     */
    public Collection getForums_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order) 
        throws IllegalArgumentException, DatabaseException {
        
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_withSortSupport_limit_noscroll(offset, rowsToReturn, sort, order);
        }
        return getBeans_withSortSupport_limit_general(offset, rowsToReturn, sort, order);
    }

    public Collection getForums_withSortSupport_limit_ViewCount(int offset, int rowsToReturn, String sort, String order) 
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("ForumLastPostDate")) &&
            (!sort.equals("ForumCreationDate")) &&
            (!sort.equals("ForumName")) &&
            (!sort.equals("ForumPostCount")) &&
            (!sort.equals("ViewCount")) &&
            (!sort.equals("ForumThreadCount")) ) {
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
        
        // Oracle and DB2 does not support GROUP BY in the LONGVACHAR column
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
            sql.append("SELECT forum1.ForumID, CategoryID, ForumOwnerName, forum1.LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount, ViewCount");
            sql.append(" FROM (");
            sql.append("       SELECT forum.ForumID AS id, SUM(thread.ThreadViewCount) AS ViewCount");
            sql.append("       FROM " + TABLE_NAME + " forum, " + ThreadDAO.TABLE_NAME + " thread");
            sql.append("       WHERE forum.ForumID = thread.ForumID (+)");
            sql.append("       GROUP BY forum.ForumID) temptable, mvnforumForum forum1");
            sql.append(" WHERE forum1.ForumID = temptable.id");
            sql.append(" ORDER BY " + sort + " " + order);
        } else {
            sql.append("SELECT forum1.ForumID, CategoryID, ForumOwnerName, forum1.LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount, ViewCount");
            sql.append(" FROM (");
            sql.append("       SELECT forum.ForumID AS id, SUM(thread.ThreadViewCount) AS ViewCount");
            sql.append("       FROM " + TABLE_NAME + " forum LEFT OUTER JOIN " + ThreadDAO.TABLE_NAME + " thread ON forum.ForumID = thread.ForumID");
            sql.append("       GROUP BY forum.ForumID) temptable, mvnforumForum forum1");
            sql.append(" WHERE forum1.ForumID = temptable.id");
            sql.append(" ORDER BY " + sort + " " + order);
            //System.out.println("ForumDAOImplJDBC.getForums_withSortSupport_limit_ViewCount() sql = " + sql);
            /*
            sql.append("SELECT DISTINCT forum.ForumID, CategoryID, ForumOwnerName, forum.LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount, SUM(thread.ThreadViewCount) AS ViewCount");
            sql.append(" FROM " + TABLE_NAME + " forum LEFT OUTER JOIN " + ThreadDAO.TABLE_NAME + " thread ON forum.ForumID = thread.ForumID");
            sql.append(" GROUP BY forum.ForumID, CategoryID, ForumOwnerName, forum.LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount");
            sql.append(" ORDER BY " + sort + " " + order);
            */
        }

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (rowsToReturn < 10000) {
                /* mysql have this error
                ava.sql.SQLException: setMaxRows() out of range. 2147483647 > 50000000.                                                                                     
                at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:910)                                                                                     
                at com.mysql.jdbc.Statement.setMaxRows(Statement.java:2224)
                */       
                statement.setMaxRows(offset + rowsToReturn);
            }
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ForumBean bean = new ForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setForumOwnerName(resultSet.getString("ForumOwnerName"));
                bean.setLastPostMemberName(resultSet.getString("LastPostMemberName"));
                bean.setForumName(resultSet.getString("ForumName"));
                bean.setForumDesc(resultSet.getString("ForumDesc"));
                bean.setForumCreationDate(resultSet.getTimestamp("ForumCreationDate"));
                bean.setForumModifiedDate(resultSet.getTimestamp("ForumModifiedDate"));
                bean.setForumLastPostDate(resultSet.getTimestamp("ForumLastPostDate"));
                bean.setForumOrder(resultSet.getInt("ForumOrder"));
                bean.setForumType(resultSet.getInt("ForumType"));
                bean.setForumFormatOption(resultSet.getInt("ForumFormatOption"));
                bean.setForumOption(resultSet.getInt("ForumOption"));
                bean.setForumStatus(resultSet.getInt("ForumStatus"));
                bean.setForumModerationMode(resultSet.getInt("ForumModerationMode"));
                bean.setForumPassword(resultSet.getString("ForumPassword"));
                bean.setForumThreadCount(resultSet.getInt("ForumThreadCount"));
                bean.setForumPostCount(resultSet.getInt("ForumPostCount"));
                bean.setViewCount(resultSet.getInt("ViewCount"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getForums_withSortSupport_limit_ViewCount.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
} // end of class ForumDAOImplJDBC
