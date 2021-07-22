/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/MemberPermissionDAOImplJDBC.java,v 1.4 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.4 $
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

import com.mvnforum.db.MemberPermissionDAO;

public class MemberPermissionDAOImplJDBC implements MemberPermissionDAO {

    public void findByPrimaryKey(int memberID, int permission)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND Permission = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, permission);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + memberID + ", " + permission + ") in table 'MemberPermission'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(int memberID, int permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        try {
            //Check if primary key already exists
            findByPrimaryKey(memberID, permission);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new MemberPermission with the same [MemberID, Permission] (" + memberID + ", " + permission + ").");
        } catch (ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        if (memberID != 0) {
            DAOFactory factory = new DAOFactory (); 
            try {
                factory.getMemberDAO().findByPrimaryKey(memberID);
            } catch (ObjectNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (DatabaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, Permission)");
        sql.append(" VALUES (?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setInt(2, permission);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'MemberPermission'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }


    }

    public void delete(int memberID, int permission)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub

    }

    public void delete_inMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public Collection getBeans_inMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

}
