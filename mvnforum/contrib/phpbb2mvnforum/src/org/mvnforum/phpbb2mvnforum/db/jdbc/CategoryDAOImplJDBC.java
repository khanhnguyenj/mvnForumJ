/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/CategoryDAOImplJDBC.java,v 1.6 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.6 $
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
import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.CategoryDAO;
import org.mvnforum.util.DBUtils;

public class CategoryDAOImplJDBC implements CategoryDAO {

    public void findByPrimaryKey(int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + categoryID
                        + ") in table 'category'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in categoryDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_CategoryName(String categoryName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryName");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryName = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, categoryName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [CategoryName] (" + categoryName
                        + ") in table 'category'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in categoryDAOImplJDBC.findByAlternateKey_CategoryName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryID, ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, 
     *                   CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus
     * Excluded columns: 
     */
    public void create(int categoryID, int parentCategoryID, String categoryName, 
                        String categoryDesc, Timestamp categoryCreationDate, Timestamp categoryModifiedDate, 
                        int categoryOrder, int categoryOption, int categoryStatus)
        throws CreateException, DatabaseException/*, DuplicateKeyException*/, DuplicateKeyException {

        // @todo: comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(categoryID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new category with the same [CategoryID] (" + categoryID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_CategoryName(categoryName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new category with the same [CategoryName] (" + categoryName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (CategoryID, ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, categoryID);
            statement.setInt(2, parentCategoryID);
            statement.setString(3, categoryName);
            statement.setString(4, categoryDesc);
            statement.setTimestamp(5, categoryCreationDate);
            statement.setTimestamp(6, categoryModifiedDate);
            statement.setInt(7, categoryOrder);
            statement.setInt(8, categoryOption);
            statement.setInt(9, categoryStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'category'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in categoryDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void createMultiple(Collection beans) {
        // TODO Auto-generated method stub
        
    }
}
