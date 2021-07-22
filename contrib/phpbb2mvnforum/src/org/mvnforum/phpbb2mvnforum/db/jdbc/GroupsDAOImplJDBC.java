/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/GroupsDAOImplJDBC.java,v 1.7 2008/03/31 10:34:51 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.7 $
 * $Date: 2008/03/31 10:34:51 $
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

import org.mvnforum.util.DBUtils;

import com.mvnforum.db.GroupsBean;
import com.mvnforum.db.GroupsDAO;

public class GroupsDAOImplJDBC implements GroupsDAO {

    public void findByPrimaryKey(int groupID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupName");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + groupID + ") in table 'Groups'.");
            }
        } catch (SQLException sqle) {
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
        sql.append("SELECT GroupID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupName = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, groupName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [GroupName] (" + groupName
                        + ") in table 'Groups'.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.findByAlternateKey_GroupName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(String groupOwnerName, String groupName, String groupDesc, int groupOption,
            Timestamp groupCreationDate, Timestamp groupModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        int groupOwnerID = 0;
        
        
        // Kiem tra xem GroupName co bi trung ko?
        // Neu ko thi nhay den ObjectNotFoundException, OK!
        try {
            //Check if alternate key already exists
            findByAlternateKey_GroupName(groupName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Groups with the same [GroupName] (" + groupName + ").");
        } catch (ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            DAOFactory factory = new DAOFactory ();
            groupOwnerID = 0;
            if ((groupOwnerName != null) && (groupOwnerName.length() > 0)) {// have group owner
                try {
                    groupOwnerID = factory.getMemberDAO().getMemberFromMemberName(groupOwnerName).getMemberID();
                } catch (ObjectNotFoundException ex) {
                    // This exception should never be thrown
                    throw new ObjectNotFoundException("ASSERTION: This should never happen.");
                }
            }
        } catch (ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Groups.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME);
        sql.append(" (GroupOwnerID, GroupOwnerName, GroupName, GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, groupOwnerID);
            statement.setString(2, groupOwnerName);
            statement.setString(3, groupName);
            statement.setString(4, groupDesc);
            statement.setInt(5, groupOption);
            statement.setTimestamp(6, groupCreationDate);
            statement.setTimestamp(7, groupModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Groups'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in GroupsDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int groupID)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub

    }

    public void update(int groupID, String groupName, String groupDesc, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {
        // TODO Auto-generated method stub

    }

    public void updateOwner(int groupID, String groupOwnerName, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        // TODO Auto-generated method stub

    }

    public GroupsBean getGroup(int groupID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getMyGroups(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getGroups()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getNumberOfGroups()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getGroupIDFromGroupName(String groupName)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

}
