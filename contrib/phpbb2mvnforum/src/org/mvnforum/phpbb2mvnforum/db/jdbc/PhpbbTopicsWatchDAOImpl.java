/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbTopicsWatchDAOImpl.java,v 1.3 2007/01/15 10:27:11 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:11 $
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

import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsWatch;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsWatchDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbTopicsWatchDAOImpl implements PhpbbTopicsWatchDAO {

    public Collection getBeansByUserID(int userID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, user_id, notify_status");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE user_id=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, userID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbTopicsWatch bean = new PhpbbTopicsWatch();
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setuser_id(resultSet.getInt("user_id"));
                bean.setnotify_status(resultSet.getInt("notify_status"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_topics_watchDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeans(int userID, int topicID)
        throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, user_id, notify_status");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE user_id=? and topic_id=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, userID);
            statement.setInt(2, topicID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbTopicsWatch bean = new PhpbbTopicsWatch();
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setuser_id(resultSet.getInt("user_id"));
                bean.setnotify_status(resultSet.getInt("notify_status"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in phpbb_topics_watchDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }

    public Collection getBeansByTopicID(int topicID) throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, user_id, notify_status");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE topic_id=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, topicID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbTopicsWatch bean = new PhpbbTopicsWatch();
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setuser_id(resultSet.getInt("user_id"));
                bean.setnotify_status(resultSet.getInt("notify_status"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_topics_watchDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
