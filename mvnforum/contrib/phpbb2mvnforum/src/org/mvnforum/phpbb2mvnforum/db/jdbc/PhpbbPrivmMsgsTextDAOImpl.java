/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbPrivmMsgsTextDAOImpl.java,v 1.4 2007/12/18 04:58:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
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

import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsText;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsTextDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbPrivmMsgsTextDAOImpl implements PhpbbPrivmMsgsTextDAO{
    
    /*
     * Included columns: privmsgs_text_id, privmsgs_bbcode_uid, privmsgs_text
     * Excluded columns: 
     */
    public PhpbbPrivmMsgsText getBean(int privmsgs_text_id) throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT privmsgs_text_id, privmsgs_bbcode_uid, privmsgs_text");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE privmsgs_text_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, privmsgs_text_id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_privmsgs_text where primary key = (" + privmsgs_text_id + ").");
            }

            PhpbbPrivmMsgsText bean = new PhpbbPrivmMsgsText();
            // @todo: uncomment the following line(s) as needed
            //bean.setprivmsgs_text_id(privmsgs_text_id);
            bean.setprivmsgs_text_id(resultSet.getInt("privmsgs_text_id"));
            bean.setprivmsgs_bbcode_uid(resultSet.getString("privmsgs_bbcode_uid"));
            bean.setprivmsgs_text(resultSet.getString("privmsgs_text"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgs_textDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
