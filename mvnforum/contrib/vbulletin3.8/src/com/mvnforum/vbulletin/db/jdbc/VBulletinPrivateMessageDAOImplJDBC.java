/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinPrivateMessageDAOImplJDBC.java,v 1.3 2009/10/07 04:23:23 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2009/10/07 04:23:23 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.vbulletin.db.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.db.DBUtils2;
import net.myvietnam.mvncore.exception.DatabaseException;

import com.mvnforum.vbulletin.db.VBulletinPrivateMessageBean;
import com.mvnforum.vbulletin.db.VBulletinPrivateMessageDAO;

/**
 * The Class VBulletinPrivateMessageDAOImplJDBC.
 */
public class VBulletinPrivateMessageDAOImplJDBC implements VBulletinPrivateMessageDAO {
    
    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinPrivateMessageDAO#getBeans()
     */
    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Title, Message, FromUserName, user.UserName ToUserName, ToUserArray, FolderID, MessageRead, DateLine");
        sql.append(" FROM pm, pmtext, user");
        sql.append(" WHERE pm.PMTextID = pmtext.PMTextID AND user.UserID = pm.UserID");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                VBulletinPrivateMessageBean bean = new VBulletinPrivateMessageBean();
                bean.setTitle(resultSet.getString("Title"));
                bean.setMessage(resultSet.getString("Message"));
                bean.setFromUserName(resultSet.getString("FromUserName"));
                bean.setToUserName(resultSet.getString("ToUserName"));
                bean.setToUserArray(resultSet.getString("ToUserArray"));
                bean.setFolderID(resultSet.getInt("FolderID"));
                bean.setReadCount(resultSet.getInt("MessageRead"));
                bean.setDateLine(new Timestamp(resultSet.getLong("DateLine") * 1000));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in VBulletinPrivateMessageDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
