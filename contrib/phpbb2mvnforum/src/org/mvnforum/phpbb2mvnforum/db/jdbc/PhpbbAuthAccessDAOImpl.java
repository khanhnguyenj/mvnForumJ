/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbAuthAccessDAOImpl.java,v 1.3 2007/01/15 10:27:12 dungbtm Exp $
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
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;

import org.mvnforum.phpbb2mvnforum.db.PhpbbAuthAccess;
import org.mvnforum.phpbb2mvnforum.db.PhpbbAuthAccessDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbAuthAccessDAOImpl implements PhpbbAuthAccessDAO {

    public Collection getBeans()
        throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT group_id, forum_id, auth_view, auth_read, auth_post, auth_reply, auth_edit, auth_delete, auth_sticky, auth_announce, auth_vote, auth_pollcreate, auth_attachments, auth_mod");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbAuthAccess bean = new PhpbbAuthAccess();
                bean.setgroup_id(resultSet.getInt("group_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.setauth_view(resultSet.getInt("auth_view"));
                bean.setauth_read(resultSet.getInt("auth_read"));
                bean.setauth_post(resultSet.getInt("auth_post"));
                bean.setauth_reply(resultSet.getInt("auth_reply"));
                bean.setauth_edit(resultSet.getInt("auth_edit"));
                bean.setauth_delete(resultSet.getInt("auth_delete"));
                bean.setauth_sticky(resultSet.getInt("auth_sticky"));
                bean.setauth_announce(resultSet.getInt("auth_announce"));
                bean.setauth_vote(resultSet.getInt("auth_vote"));
                bean.setauth_pollcreate(resultSet.getInt("auth_pollcreate"));
                bean.setauth_attachments(resultSet.getInt("auth_attachments"));
                bean.setauth_mod(resultSet.getInt("auth_mod"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_auth_accessDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansByForumID(int forumId) throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT group_id, forum_id, auth_view, auth_read, auth_post, auth_reply, auth_edit, auth_delete, auth_sticky, auth_announce, auth_vote, auth_pollcreate, auth_attachments, auth_mod");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE forum_id=?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbAuthAccess bean = new PhpbbAuthAccess();
                bean.setgroup_id(resultSet.getInt("group_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.setauth_view(resultSet.getInt("auth_view"));
                bean.setauth_read(resultSet.getInt("auth_read"));
                bean.setauth_post(resultSet.getInt("auth_post"));
                bean.setauth_reply(resultSet.getInt("auth_reply"));
                bean.setauth_edit(resultSet.getInt("auth_edit"));
                bean.setauth_delete(resultSet.getInt("auth_delete"));
                bean.setauth_sticky(resultSet.getInt("auth_sticky"));
                bean.setauth_announce(resultSet.getInt("auth_announce"));
                bean.setauth_vote(resultSet.getInt("auth_vote"));
                bean.setauth_pollcreate(resultSet.getInt("auth_pollcreate"));
                bean.setauth_attachments(resultSet.getInt("auth_attachments"));
                bean.setauth_mod(resultSet.getInt("auth_mod"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_auth_accessDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
