/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/GroupForumDAOImplJDBC.java,v 1.15 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.15 $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class GroupForumDAOImplJDBC implements GroupForumDAO {

    private static final Logger log = LoggerFactory.getLogger(GroupForumDAOImplJDBC.class);

    public GroupForumDAOImplJDBC() {
    }

    public void findByPrimaryKey(int groupID, int forumID, int permission)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, ForumID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND ForumID = ? AND Permission = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, forumID);
            statement.setInt(3, permission);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + groupID + ", " + forumID + ", " + permission + ") in table 'GroupForum'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, ForumID, Permission
     * Excluded columns:
     */
    public void create(int groupID, int forumID, int permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(groupID, forumID, permission);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new GroupForum with the same [GroupID, ForumID, Permission] (" + groupID + ", " + forumID + ", " + permission + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot create new GroupForum.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getGroupsDAO().findByPrimaryKey(groupID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Groups' does not exist. Cannot create new GroupForum.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (GroupID, ForumID, Permission)");
        sql.append(" VALUES (?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, groupID);
            statement.setInt(2, forumID);
            statement.setInt(3, permission);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'GroupForum'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int groupID, int forumID, int permission)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND ForumID = ? AND Permission = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, forumID);
            statement.setInt(3, permission);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table GroupForum where primary key = (" + groupID + ", " + forumID + ", " + permission + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inGroup(int groupID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? ");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);

            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.delete_inGroup.");
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
        sql.append(" WHERE ForumID = ? ");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);

            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.delete_inForum.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }


/************************************************
 * Customized methods come below
 ************************************************/

    /*
     * Included columns: Permission
     * Excluded columns: GroupID, ForumID
     */
    public Collection getBeans_inGroupForum(int groupID, int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND ForumID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, forumID);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GroupForumBean bean = new GroupForumBean();
                bean.setGroupID(groupID);
                bean.setForumID(forumID);
                bean.setPermission(resultSet.getInt("Permission"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.getBeans_inGroupForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, ForumID, Permission
     * Excluded columns:
     */
    public Collection getBeans_inForum(int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, ForumID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ? ");
        sql.append(" ORDER BY GroupID ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GroupForumBean bean = new GroupForumBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setPermission(resultSet.getInt("Permission"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.getBeans_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, ForumID, Permission
     * Excluded columns:
     */
    public Collection getBeans_inGroup(int groupID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, ForumID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? ");
        sql.append(" ORDER BY ForumID ");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GroupForumBean bean = new GroupForumBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setPermission(resultSet.getInt("Permission"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.getBeans_inGroup.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID
     * Excluded columns: Permission
     */
    public Collection getDistinctGroups()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT GroupID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY GroupID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GroupForumBean bean = new GroupForumBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupForumDAOImplJDBC.getDistinctGroups.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}// end of class GroupForumDAOImplJDBC
