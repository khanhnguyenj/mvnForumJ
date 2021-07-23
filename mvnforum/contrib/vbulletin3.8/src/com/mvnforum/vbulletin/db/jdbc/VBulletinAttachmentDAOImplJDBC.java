/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinAttachmentDAOImplJDBC.java,v 1.3 2009/10/07 04:23:23 lexuanttkhtn Exp $
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

import com.mvnforum.vbulletin.db.VBulletinAttachmentBean;
import com.mvnforum.vbulletin.db.VBulletinAttachmentDAO;

/**
 * The Class VBulletinAttachmentDAOImplJDBC.
 */
public class VBulletinAttachmentDAOImplJDBC implements VBulletinAttachmentDAO {

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinAttachmentDAO#getBeans(int)
     */
    public Collection getBeans(int postID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachmentID, UserID, DateLine, Thumbnail_DateLine, FileName, FileData, Visible, Counter, FileSize, PostID, FileHash, PostHash, Thumbnail, Thumbnail_FileSize, Extension");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?"); // TODO: uncomment as needed
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                VBulletinAttachmentBean bean = new VBulletinAttachmentBean();
                bean.setAttachmentID(resultSet.getInt("AttachmentID"));
                bean.setUserID(resultSet.getInt("UserID"));
                bean.setDateLine(resultSet.getLong("DateLine"));
                bean.setThumbnail_DateLine(resultSet.getInt("Thumbnail_DateLine"));
                bean.setFileName(resultSet.getString("FileName"));
                bean.setFileData(resultSet.getString("FileData"));
                bean.setVisible(resultSet.getInt("Visible"));
                bean.setCounter(resultSet.getInt("Counter"));
                bean.setFileSize(resultSet.getInt("FileSize"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setFileHash(resultSet.getString("FileHash"));
                bean.setPostHash(resultSet.getString("PostHash"));
                bean.setThumbnail(resultSet.getString("Thumbnail"));
                bean.setThumbnail_FileSize(resultSet.getInt("Thumbnail_FileSize"));
                bean.setExtension(resultSet.getString("Extension"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
