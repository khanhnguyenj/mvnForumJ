/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbRanksDAOImpl.java,v 1.3 2007/01/15 10:27:11 dungbtm Exp $
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

import org.mvnforum.phpbb2mvnforum.db.PhpbbRanks;
import org.mvnforum.phpbb2mvnforum.db.PhpbbRanksDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbRanksDAOImpl implements PhpbbRanksDAO {

    public Collection getBeans() throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT rank_id, rank_title, rank_min, rank_special, rank_image");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbRanks bean = new PhpbbRanks();
                bean.setrank_id(resultSet.getInt("rank_id"));
                bean.setrank_title(resultSet.getString("rank_title"));
                bean.setrank_min(resultSet.getInt("rank_min"));
                bean.setrank_special(resultSet.getInt("rank_special"));
                bean.setrank_image(resultSet.getString("rank_image"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_ranksDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
