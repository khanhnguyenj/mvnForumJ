/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/CategoryDAOImplJDBC.java,v 1.22 2009/09/30 07:00:57 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.22 $
 * $Date: 2009/09/30 07:00:57 $
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

import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.CategoryDAO;

public class CategoryDAOImplJDBC implements CategoryDAO {

    private static final Logger log = LoggerFactory.getLogger(CategoryDAOImplJDBC.class);

    public CategoryDAOImplJDBC() {
    }

    public int findByPrimaryKey(int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + categoryID + ") in table '" + TABLE_NAME + "'.");
            }
            
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int findByAlternateKey_CategoryName(String categoryName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE lower(CategoryName) = lower(?)");
        } else {
            sql.append(" WHERE CategoryName = ?");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, categoryName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [CategoryName] (" + categoryName + ") in table '" + TABLE_NAME + "'.");
            }
            
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.findByAlternateKey_CategoryName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private int findCategoryID(int parentCategoryID, String categoryName, Timestamp categoryCreationDate)
        throws ObjectNotFoundException, DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE (ParentCategoryID = ?) AND (CategoryName = ?) AND (CategoryCreationDate = ?) ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, parentCategoryID);
            statement.setString(2, categoryName);
            statement.setTimestamp(3, categoryCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the CategoryID in table Category.");
            }
    
            return resultSet.getInt("CategoryID");
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.findCategoryID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate,
     *                   CategoryOrder, CategoryOption, CategoryStatus
     * Excluded columns: CategoryID
     */
    public void create(int parentCategoryID, String categoryName, String categoryDesc,
                       Timestamp categoryCreationDate, Timestamp categoryModifiedDate, int categoryOrder,
                       int categoryOption, int categoryStatus)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @TODO: Comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_CategoryName(categoryName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Category with the same [CategoryName] (" + categoryName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @TODO: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            if (parentCategoryID != 0) {
                findByPrimaryKey(parentCategoryID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table '" + TABLE_NAME + "' does not exist. Cannot create new Category.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, parentCategoryID);
            statement.setString(2, categoryName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(3, new StringReader(categoryDesc), categoryDesc.length());
            } else {
                statement.setString(3, categoryDesc);
            }
            statement.setTimestamp(4, categoryCreationDate);
            statement.setTimestamp(5, categoryModifiedDate);
            statement.setInt(6, categoryOrder);
            statement.setInt(7, categoryOption);
            statement.setInt(8, categoryStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table '" + TABLE_NAME + "'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int createCategory(int parentCategoryID, String categoryName, String categoryDesc,
                              Timestamp categoryCreationDate, Timestamp categoryModifiedDate, int categoryOrder,
                              int categoryOption, int categoryStatus)
            throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException, DuplicateKeyException {

        create(parentCategoryID, categoryName, categoryDesc, categoryCreationDate, categoryModifiedDate, categoryOrder, categoryOption, categoryStatus);
        int categoryID = 0;
        try {
            categoryID = findCategoryID(parentCategoryID, categoryName, categoryCreationDate);
        } catch (ObjectNotFoundException ex) {
            // Hack the Oracle 9i problem
            Timestamp roundTimestamp = new Timestamp((categoryCreationDate.getTime()/1000)*1000);
            categoryID = findCategoryID(parentCategoryID, categoryName, roundTimestamp);
        }
        return categoryID;
    }

    public void delete(int categoryID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table '" + TABLE_NAME + "' where primary key = (" + categoryID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: CategoryName, CategoryDesc, CategoryModifiedDate, CategoryOrder, CategoryOption,
     *                   CategoryStatus
     * Excluded columns: CategoryID, ParentCategoryID, CategoryCreationDate
     */
    public void update(int categoryID, // primary key
                       String categoryName, String categoryDesc, Timestamp categoryModifiedDate,
                       int categoryOrder, int categoryOption, int categoryStatus)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {

        CategoryBean bean = getCategory(categoryID); // @TODO: comment or delete this line if no alternate key are included

        if (categoryName.equalsIgnoreCase(bean.getCategoryName()) == false) {
            // Category tries to change its alternate key <CategoryName>, so we must check if it already exist
            try {
                findByAlternateKey_CategoryName(categoryName);
                throw new DuplicateKeyException("Alternate key [CategoryName] (" + categoryName + ") already exists. Cannot update Category.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET CategoryName = ?, CategoryDesc = ?, CategoryModifiedDate = ?, CategoryOrder = ?, CategoryOption = ?, CategoryStatus = ?");
        sql.append(" WHERE CategoryID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, categoryName);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(2, new StringReader(categoryDesc), categoryDesc.length());
            } else {
                statement.setString(2, categoryDesc);
            }
            statement.setTimestamp(3, categoryModifiedDate);
            statement.setInt(4, categoryOrder);
            statement.setInt(5, categoryOption);
            statement.setInt(6, categoryStatus);

            // primary key column(s)
            statement.setInt(7, categoryID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table '" + TABLE_NAME + "' where primary key = (" + categoryID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate,
     *                   CategoryOrder, CategoryOption, CategoryStatus
     * Excluded columns: CategoryID
     */
    public CategoryBean getCategory(int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE CategoryID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table '" + TABLE_NAME + "' where primary key = (" + categoryID + ").");
            }

            CategoryBean bean = new CategoryBean();
            bean.setCategoryID(categoryID);
            bean.setParentCategoryID(resultSet.getInt("ParentCategoryID"));
            bean.setCategoryName(resultSet.getString("CategoryName"));
            bean.setCategoryDesc(resultSet.getString("CategoryDesc"));
            bean.setCategoryCreationDate(resultSet.getTimestamp("CategoryCreationDate"));
            bean.setCategoryModifiedDate(resultSet.getTimestamp("CategoryModifiedDate"));
            bean.setCategoryOrder(resultSet.getInt("CategoryOrder"));
            bean.setCategoryOption(resultSet.getInt("CategoryOption"));
            bean.setCategoryStatus(resultSet.getInt("CategoryStatus"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.getCategory(pk).");
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
    public Collection getCategories()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT CategoryID, ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate, CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @TODO: uncomment as needed
        sql.append(" ORDER BY CategoryOrder ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                CategoryBean bean = new CategoryBean();
                bean.setCategoryID(resultSet.getInt("CategoryID"));
                bean.setParentCategoryID(resultSet.getInt("ParentCategoryID"));
                bean.setCategoryName(resultSet.getString("CategoryName"));
                bean.setCategoryDesc(resultSet.getString("CategoryDesc"));
                bean.setCategoryCreationDate(resultSet.getTimestamp("CategoryCreationDate"));
                bean.setCategoryModifiedDate(resultSet.getTimestamp("CategoryModifiedDate"));
                bean.setCategoryOrder(resultSet.getInt("CategoryOrder"));
                bean.setCategoryOption(resultSet.getInt("CategoryOption"));
                bean.setCategoryStatus(resultSet.getInt("CategoryStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.getCategories.");
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
     * This method should be call only when we can make sure that CategoryID is in database
     */
    public void decreaseCategoryOrder(int categoryID, Timestamp categoryModifiedDate)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET CategoryOrder = CategoryOrder - 1, CategoryModifiedDate = ? WHERE CategoryID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, categoryModifiedDate);
            statement.setInt(2, categoryID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the CategoryOrder in table '" + TABLE_NAME + "'. Please contact Web site Administrator.");
            }
            //@TODO: review this
            // ATTENTION !!!
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.decreaseCategoryOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that CategoryID is in database
     */
    public void increaseCategoryOrder(int categoryID, Timestamp categoryModifiedDate)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET CategoryOrder = CategoryOrder + 1, CategoryModifiedDate = ? WHERE CategoryID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, categoryModifiedDate);
            statement.setInt(2, categoryID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the CategoryOrder in table '" + TABLE_NAME + "'. Please contact Web site Administrator.");
            }
            //@TODO: review this
            // ATTENTION !!!
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in CategoryDAOImplJDBC.increaseCategoryOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class CategoryDAOImplJDBC
