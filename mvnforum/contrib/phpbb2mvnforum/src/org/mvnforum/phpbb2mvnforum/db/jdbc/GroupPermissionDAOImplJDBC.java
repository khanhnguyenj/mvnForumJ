/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/GroupPermissionDAOImplJDBC.java,v 1.3 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:12 $
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
import java.util.Collection;

import org.mvnforum.util.DBUtils;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.db.GroupPermissionDAO;

public class GroupPermissionDAOImplJDBC implements GroupPermissionDAO {

    public void findByPrimaryKey(int groupID, int permission)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND Permission = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, permission);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + groupID + ", " + permission
                        + ") in table 'GroupPermission'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in GroupPermissionDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(int groupID, int permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        try {
            //Check if primary key already exists
            findByPrimaryKey(groupID, permission);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException(
                    "Primary key already exists. Cannot create new GroupPermission with the same [GroupID, Permission] ("
                            + groupID + ", " + permission + ").");
        } catch (ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (GroupID, Permission)");
        sql.append(" VALUES (?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, groupID);
            statement.setInt(2, permission);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'GroupPermission'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in GroupPermissionDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int groupID, int permission)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub

    }

    public void delete_inGroup(int groupID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public Collection getBeans_inGroup(int groupID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getDistinctGroups()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

}
