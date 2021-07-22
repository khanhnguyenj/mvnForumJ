/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/PmAttachmentDAOImplJDBC.java,v 1.25 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.25 $
 * $Date: 2009/01/02 15:12:55 $
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

import com.mvnforum.db.*;

public class PmAttachmentDAOImplJDBC implements PmAttachmentDAO {

    private static final Logger log = LoggerFactory.getLogger(PmAttachmentDAOImplJDBC.class);

    public PmAttachmentDAOImplJDBC() {
    }

    /**
     * This is a customized method
     */
    protected static int findPmAttachID(int memberID, Timestamp pmAttachCreationDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PmAttachID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND PmAttachCreationDate = ? ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setTimestamp(2, pmAttachCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the PmAttachID in table PmAttachment.");
            }

            return resultSet.getInt("PmAttachID");
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.findPmAttachID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByPrimaryKey(int pmAttachID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PmAttachID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PmAttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, pmAttachID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + pmAttachID + ") in table 'PmAttachment'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc,
     *                   PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption,
     *                   PmAttachStatus
     * Excluded columns: PmAttachID
     */
    public int create(int memberID, String pmAttachFilename, int pmAttachFileSize,
                        String pmAttachMimeType, String pmAttachDesc, String pmAttachCreationIP,
                        Timestamp pmAttachCreationDate, Timestamp pmAttachModifiedDate, int pmAttachDownloadCount,
                        int pmAttachOption, int pmAttachStatus)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException, ObjectNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new PmAttachment.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption, PmAttachStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setString(2, pmAttachFilename);
            statement.setInt(3, pmAttachFileSize);
            statement.setString(4, pmAttachMimeType);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(5, new StringReader(pmAttachDesc), pmAttachDesc.length());
            } else {
                statement.setString(5, pmAttachDesc);
            }
            statement.setString(6, pmAttachCreationIP);
            statement.setTimestamp(7, pmAttachCreationDate);
            statement.setTimestamp(8, pmAttachModifiedDate);
            statement.setInt(9, pmAttachDownloadCount);
            statement.setInt(10, pmAttachOption);
            statement.setInt(11, pmAttachStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'PmAttachment'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

        int pmAttachID = 0;
        try {
            pmAttachID = findPmAttachID(memberID, pmAttachCreationDate);
        } catch (ObjectNotFoundException ex) {
            // Hack the Oracle 9i problem
            Timestamp roundTimestamp = new Timestamp((pmAttachCreationDate.getTime()/1000)*1000);
            pmAttachID = findPmAttachID(memberID, roundTimestamp);
        }
        return pmAttachID;
    }

    public void delete(int pmAttachID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE PmAttachID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, pmAttachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table PmAttachment where primary key = (" + pmAttachID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType,
     *                   PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount,
     *                   PmAttachOption, PmAttachStatus
     * Excluded columns:
     */
    public PmAttachmentBean getPmAttachment(int pmAttachID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption, PmAttachStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PmAttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, pmAttachID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table PmAttachment where primary key = (" + pmAttachID + ").");
            }

            PmAttachmentBean bean = new PmAttachmentBean();
            // @todo: uncomment the following line(s) as needed
            //bean.setPmAttachID(pmAttachID);
            bean.setPmAttachID(resultSet.getInt("PmAttachID"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setPmAttachFilename(resultSet.getString("PmAttachFilename"));
            bean.setPmAttachFileSize(resultSet.getInt("PmAttachFileSize"));
            bean.setPmAttachMimeType(resultSet.getString("PmAttachMimeType"));
            bean.setPmAttachDesc(resultSet.getString("PmAttachDesc"));
            bean.setPmAttachCreationIP(resultSet.getString("PmAttachCreationIP"));
            bean.setPmAttachCreationDate(resultSet.getTimestamp("PmAttachCreationDate"));
            bean.setPmAttachModifiedDate(resultSet.getTimestamp("PmAttachModifiedDate"));
            bean.setPmAttachDownloadCount(resultSet.getInt("PmAttachDownloadCount"));
            bean.setPmAttachOption(resultSet.getInt("PmAttachOption"));
            bean.setPmAttachStatus(resultSet.getInt("PmAttachStatus"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.getPmAttachment(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that postID is in database
     */
    public void increaseDownloadCount(int pmAttachID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET PmAttachDownloadCount = PmAttachDownloadCount + 1 WHERE PmAttachID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, pmAttachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the PmAttachDownloadCount in table PmAttachment. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.increaseDownloadCount(pk).");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType,
     *                   PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount,
     *                   PmAttachOption, PmAttachStatus
     * Excluded columns:
     */
    public Collection getPmAttachments_inMessage(int messageID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT pmattachment.PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption, PmAttachStatus");
        sql.append(" FROM " + TABLE_NAME + " pmattachment, " + PmAttachMessageDAO.TABLE_NAME + " pmattachmessage");
        sql.append(" WHERE pmattachment.PmAttachID = pmattachmessage.PmAttachID AND pmattachmessage.MessageID = ?");
        sql.append(" ORDER BY pmattachment.PmAttachID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PmAttachmentBean bean = new PmAttachmentBean();
                bean.setPmAttachID(resultSet.getInt("PmAttachID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setPmAttachFilename(resultSet.getString("PmAttachFilename"));
                bean.setPmAttachFileSize(resultSet.getInt("PmAttachFileSize"));
                bean.setPmAttachMimeType(resultSet.getString("PmAttachMimeType"));
                bean.setPmAttachDesc(resultSet.getString("PmAttachDesc"));
                bean.setPmAttachCreationIP(resultSet.getString("PmAttachCreationIP"));
                bean.setPmAttachCreationDate(resultSet.getTimestamp("PmAttachCreationDate"));
                bean.setPmAttachModifiedDate(resultSet.getTimestamp("PmAttachModifiedDate"));
                bean.setPmAttachDownloadCount(resultSet.getInt("PmAttachDownloadCount"));
                bean.setPmAttachOption(resultSet.getInt("PmAttachOption"));
                bean.setPmAttachStatus(resultSet.getInt("PmAttachStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.getPmAttachments_inMessage.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType,
     *                   PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount,
     *                   PmAttachOption, PmAttachStatus
     * Excluded columns:
     */
    // Note: this method using LEFT JOIN
    public Collection getOrphanPmAttachments()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
            // Oracle query
            sql.append("SELECT pmattachment.PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption, PmAttachStatus");
            sql.append(" FROM " + TABLE_NAME + " pmattachment , " + PmAttachMessageDAO.TABLE_NAME + " pmattachmessage");
            sql.append(" WHERE pmattachment.PmAttachID = pmattachmessage.PmAttachID (+) ");
            sql.append(" AND pmattachmessage.PmAttachID IS NULL ");
        } else {
            // standard query
            sql.append("SELECT pmattachment.PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize, PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate, PmAttachDownloadCount, PmAttachOption, PmAttachStatus");
            sql.append(" FROM " + TABLE_NAME + " pmattachment LEFT JOIN " + PmAttachMessageDAO.TABLE_NAME + " pmattachmessage");
            sql.append(" ON pmattachment.PmAttachID = pmattachmessage.PmAttachID ");
            sql.append(" WHERE pmattachmessage.PmAttachID IS NULL ");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PmAttachmentBean bean = new PmAttachmentBean();
                bean.setPmAttachID(resultSet.getInt("PmAttachID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setPmAttachFilename(resultSet.getString("PmAttachFilename"));
                bean.setPmAttachFileSize(resultSet.getInt("PmAttachFileSize"));
                bean.setPmAttachMimeType(resultSet.getString("PmAttachMimeType"));
                bean.setPmAttachDesc(resultSet.getString("PmAttachDesc"));
                bean.setPmAttachCreationIP(resultSet.getString("PmAttachCreationIP"));
                bean.setPmAttachCreationDate(resultSet.getTimestamp("PmAttachCreationDate"));
                bean.setPmAttachModifiedDate(resultSet.getTimestamp("PmAttachModifiedDate"));
                bean.setPmAttachDownloadCount(resultSet.getInt("PmAttachDownloadCount"));
                bean.setPmAttachOption(resultSet.getInt("PmAttachOption"));
                bean.setPmAttachStatus(resultSet.getInt("PmAttachStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.getOrphanPmAttachments.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updatePmAttachOption(int pmAttachID, int pmAttachOption)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET PmAttachOption = ? WHERE PmAttachID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, pmAttachOption);
            statement.setInt(2, pmAttachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the Option in table PmAttachment. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachmentDAOImplJDBC.updatePmAttachOption.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class PmAttachmentDAOImplJDBC
