/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbGroupDAOImpl.java,v 1.5 2007/12/18 04:58:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2007/12/18 04:58:44 $
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
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.PhpbbGroup;
import org.mvnforum.phpbb2mvnforum.db.PhpbbGroupDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbGroupDAOImpl implements PhpbbGroupDAO {

    public Collection getBeans()
        throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT group_id, group_type, group_name, group_description, group_moderator, group_single_user");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbGroup bean = new PhpbbGroup();
                bean.setgroup_id(resultSet.getInt("group_id"));
                bean.setgroup_type(resultSet.getInt("group_type"));
                bean.setgroup_name(resultSet.getString("group_name"));
                bean.setgroup_description(resultSet.getString("group_description"));
                bean.setgroup_moderator(resultSet.getInt("group_moderator"));
                bean.setgroup_single_user(resultSet.getInt("group_single_user"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_groupsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public PhpbbGroup getBean(int groupID) throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT group_id, group_type, group_name, group_description, group_moderator, group_single_user");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE group_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_groups where primary key = (" + groupID + ").");
            }

            PhpbbGroup bean = new PhpbbGroup();
            bean.setgroup_id(resultSet.getInt("group_id"));
            bean.setgroup_type(resultSet.getInt("group_type"));
            bean.setgroup_name(resultSet.getString("group_name"));
            bean.setgroup_description(resultSet.getString("group_description"));
            bean.setgroup_moderator(resultSet.getInt("group_moderator"));
            bean.setgroup_single_user(resultSet.getInt("group_single_user"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_groupsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public PhpbbGroup getTruthGroup(int groupID)
        throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT group_id, group_type, group_name, group_description, group_moderator, group_single_user");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE group_id=? and group_single_user=?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, 0);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                //throw new ObjectNotFoundException("Cannot find the row in table phpbb_groups where primary key = (" + groupID + ").");
                return null;
            }

            PhpbbGroup bean = new PhpbbGroup();
            bean.setgroup_id(resultSet.getInt("group_id"));
            bean.setgroup_type(resultSet.getInt("group_type"));
            bean.setgroup_name(resultSet.getString("group_name"));
            bean.setgroup_description(resultSet.getString("group_description"));
            bean.setgroup_moderator(resultSet.getInt("group_moderator"));
            bean.setgroup_single_user(resultSet.getInt("group_single_user"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_groupsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }
}
