/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinMessageFolderDAOImplJDBC.java,v 1.2 2009/10/07 01:53:39 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.2 $
 * $Date: 2009/10/07 01:53:39 $
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

import com.mvnforum.vbulletin.db.*;

/**
 * The Class VBulletinMessageFolderDAOImplJDBC.
 */
public class VBulletinMessageFolderDAOImplJDBC implements VBulletinMessageFolderDAO {

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinMessageFolderDAO#getBeans()
     */
    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT folder.UserID, user.UserName, PMFolders");
        sql.append(" FROM " + TABLE_NAME + " folder, " + VBulletinUserDAO.TABLE_NAME + " user");
        sql.append(" WHERE PMFolders != '' AND folder.UserID = user.UserID");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                VBulletinMessageFolderBean bean = new VBulletinMessageFolderBean();
                bean.setUserID(resultSet.getInt("UserID"));
                bean.setUserName(resultSet.getString("UserName"));
                bean.setPMFolders(resultSet.getString("PMFolders"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in UsertextfieldDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
