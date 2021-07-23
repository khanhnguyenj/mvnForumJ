/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbPostTextDAOImpl.java,v 1.5 2007/12/18 04:58:44 minhnn Exp $
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

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.PhpbbPostText;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostTextDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbPostTextDAOImpl implements PhpbbPostTextDAO {
    
    /*
     * Included columns: post_id, bbcode_uid, post_subject, post_text
     * Excluded columns: 
     */
    public PhpbbPostText getBean(int post_id) throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT post_id, bbcode_uid, post_subject, post_text");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE post_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, post_id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_posts_text where primary key = (" + post_id + ").");
            }

            PhpbbPostText bean = new PhpbbPostText(); 
            bean.setpost_id(resultSet.getInt("post_id"));
            bean.setbbcode_uid(resultSet.getString("bbcode_uid"));
            bean.setpost_subject(resultSet.getString("post_subject"));
            bean.setpost_text(resultSet.getString("post_text"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_posts_textDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: post_id, bbcode_uid, post_subject, post_text
     * Excluded columns: 
     */
    public String getPostTextFromPostID(int post_id) throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT post_text");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE post_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, post_id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_posts_text where primary key = (" + post_id + ").");
            }
            return resultSet.getString("post_text");
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_posts_textDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
