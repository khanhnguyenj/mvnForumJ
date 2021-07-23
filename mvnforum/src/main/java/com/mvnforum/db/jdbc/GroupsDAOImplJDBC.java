/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/GroupsDAOImplJDBC.java,v 1.31 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
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

import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class GroupsDAOImplJDBC implements GroupsDAO {

    private static final Logger log = LoggerFactory.getLogger(GroupsDAOImplJDBC.class);

    public GroupsDAOImplJDBC() {
    }

    public void findByPrimaryKey(int groupID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + groupID + ") in table 'Groups'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_GroupName(String groupName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupName");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE lower(GroupName) = lower(?)");
        } else {
            sql.append(" WHERE GroupName = ?");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, groupName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [GroupName] (" + groupName + ") in table 'Groups'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.findByAlternateKey_GroupName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption,
     *                   GroupCreationDate, GroupModifiedDate
     * Excluded columns: GroupID
     */
    /**
     * NOTE: This is a customized method, it get groupOwnerID from groupOwnerName
     *      and I remove the groupOwnerID from the parameter list
     */
    public void create(String groupOwnerName, String groupName,
                       String groupDesc, int groupOption, Timestamp groupCreationDate,
                       Timestamp groupModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        int groupOwnerID = 0;// MUST init to 0, or this method will be wrong

        // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_GroupName(groupName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Groups with the same [GroupName] (" + groupName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            groupOwnerID = 0;
            if ((groupOwnerName!=null) && (groupOwnerName.length()>0)) {// have group owner
                DAOFactory.getMemberDAO().findByAlternateKey_MemberName(groupOwnerName);
                try {
                    groupOwnerID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(groupOwnerName);
                } catch (ObjectNotFoundException ex) {
                    // This exception should never be thrown
                    throw new ObjectNotFoundException("ASSERTION: This should never happen.");
                }
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Groups.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, groupOwnerID);
            statement.setString(2, groupOwnerName);
            statement.setString(3, groupName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(4, new StringReader(groupDesc), groupDesc.length());
            } else {
                statement.setString(4, groupDesc);
            }
            statement.setInt(5, groupOption);
            statement.setTimestamp(6, groupCreationDate);
            statement.setTimestamp(7, groupModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Groups'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupName, GroupDesc, GroupModifiedDate
     * Excluded columns: GroupID, GroupOwnerID, GroupOption, GroupCreationDate
     */
    public void update(int groupID, // primary key
                       String groupName, String groupDesc, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {

        GroupsBean bean = getGroup(groupID); // @todo: comment or delete this line if no alternate key are included

        if (groupName.equalsIgnoreCase(bean.getGroupName()) == false) {
            // Groups tries to change its alternate key <GroupName>, so we must check if it already exist
            try {
                findByAlternateKey_GroupName(groupName);
                throw new DuplicateKeyException("Alternate key [GroupName] (" + groupName + ") already exists. Cannot update Groups.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET GroupName = ?, GroupDesc = ?, GroupModifiedDate = ?");
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, groupName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(2, new StringReader(groupDesc), groupDesc.length());
            } else {
                statement.setString(2, groupDesc);
            }
            statement.setTimestamp(3, groupModifiedDate);

            // primary key column(s)
            statement.setInt(4, groupID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Groups where primary key = (" + groupID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupOwnerID, GroupOwnerName, GroupModifiedDate
     * Excluded columns: GroupID, GroupName, GroupDesc, GroupOption, GroupCreationDate
     */
    public void updateOwner(int groupID, // primary key
                            String groupOwnerName, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        int groupOwnerID = 0;//MUST init to 0

        //GroupsBean bean = getGroup(groupID); // @todo: comment or delete this line if no alternate key are included

        try {
            // @todo: modify the parameter list as needed
            // If this method does not change the foreign key columns, you can comment this block of code.
            groupOwnerID = 0;
            if ((groupOwnerName!=null) && (groupOwnerName.length()>0)) {// have group owner
                groupOwnerName = DAOFactory.getMemberDAO().findByAlternateKey_MemberName(groupOwnerName);
                try {
                    groupOwnerID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(groupOwnerName);
                } catch (ObjectNotFoundException ex) {
                    // This exception should never be thrown
                    throw new ObjectNotFoundException("ASSERTION: This should never happen.");
                }
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot update table 'Groups'.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET GroupOwnerID = ?, GroupOwnerName = ?, GroupModifiedDate = ?");
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, groupOwnerID);
            statement.setString(2, groupOwnerName);
            statement.setTimestamp(3, groupModifiedDate);

            // primary key column(s)
            statement.setInt(4, groupID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Groups where primary key = (" + groupID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.updateOwner.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int groupID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Groups where primary key = (" + groupID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption,
     *                   GroupCreationDate, GroupModifiedDate
     * Excluded columns: GroupID
     */
    public GroupsBean getGroup(int groupID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Groups where primary key = (" + groupID + ").");
            }

            GroupsBean bean = new GroupsBean();
            bean.setGroupID(groupID);
            bean.setGroupOwnerID(resultSet.getInt("GroupOwnerID"));
            bean.setGroupOwnerName(resultSet.getString("GroupOwnerName"));
            bean.setGroupName(resultSet.getString("GroupName"));
            bean.setGroupDesc(resultSet.getString("GroupDesc"));
            bean.setGroupOption(resultSet.getInt("GroupOption"));
            bean.setGroupCreationDate(resultSet.getTimestamp("GroupCreationDate"));
            bean.setGroupModifiedDate(resultSet.getTimestamp("GroupModifiedDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.getGroup(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getMyGroups(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        ArrayList retValue = new ArrayList();
        sql.append("SELECT g.GroupID, g.GroupName, g.GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate");
        sql.append(" FROM " + TABLE_NAME).append(" g, ").append(MemberGroupDAO.TABLE_NAME).append(" mg");
        sql.append(" WHERE (g.GroupID = mg.GroupID) AND (mg.MemberID = ?)");
        sql.append(" ORDER BY g.GroupID ASC");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                GroupsBean bean = new GroupsBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                bean.setGroupName(resultSet.getString("GroupName"));
                bean.setGroupDesc(resultSet.getString("GroupDesc"));
                bean.setGroupOption(resultSet.getInt("GroupOption"));
                bean.setGroupCreationDate(resultSet.getTimestamp("GroupCreationDate"));
                bean.setGroupModifiedDate(resultSet.getTimestamp("GroupModifiedDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.getMyGroups(memberID).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,
     *                   GroupOption, GroupCreationDate, GroupModifiedDate
     * Excluded columns:
     */
    public Collection getGroups()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        //minhnn: @todo: it should be GroupID, not GroupName
        sql.append(" ORDER BY GroupID ASC");// must sort be id to show number of members in group
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                GroupsBean bean = new GroupsBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                bean.setGroupOwnerID(resultSet.getInt("GroupOwnerID"));
                bean.setGroupOwnerName(resultSet.getString("GroupOwnerName"));
                bean.setGroupName(resultSet.getString("GroupName"));
                bean.setGroupDesc(resultSet.getString("GroupDesc"));
                bean.setGroupOption(resultSet.getInt("GroupOption"));
                bean.setGroupCreationDate(resultSet.getTimestamp("GroupCreationDate"));
                bean.setGroupModifiedDate(resultSet.getTimestamp("GroupModifiedDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.getGroups.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfGroups()
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
            AssertionUtil.doAssert(resultSet.next(), "Assertion in GroupsDAOImplJDBC.getNumberOfGroups.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.getNumberOfGroups.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

/************************************************
 * Customized methods come below
 ************************************************/

    /* @todo check if this method work with other DBMS other than MySql (check case-sensitive) */
    public int getGroupIDFromGroupName(String groupName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT GroupID FROM " + TABLE_NAME + " WHERE GroupName = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, groupName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Group where GroupName = " + groupName);
            }
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.getGroupIDFromGroupName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class GroupsDAOImplJDBC
