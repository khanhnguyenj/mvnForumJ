/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/AttachmentXML.java,v 1.15 2009/01/06 18:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.15 $
 * $Date: 2009/01/06 18:31:30 $
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
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.db.AttachmentDAO;
import com.mvnforum.db.DAOFactory;

/**
 * @author Igor Manic
 * @version $Revision: 1.15 $, $Date: 2009/01/06 18:31:30 $
 * <br/>
 * <code>AttachmentXML</code> todo Igor: enter description
 *
 */
public class AttachmentXML {

    private int attachmentID;
    /** Returns <code>AttachmentID</code> of this attachment or
      * <code>-1</code> if attachment is not created yet. */
    public int getAttachmentID() { return attachmentID; }

    private int parentPostID;
    /** Returns <code>PostID</code> of this attachment's parent post or
      * <code>-1</code> if this attachment is not created yet. */
    public int getParentPostID() { return parentPostID; }

    private int parentThreadID;
    /** Returns <code>ThreadID</code> of this attachment's parent thread or
      * <code>-1</code> if this attachment is not created yet. */
    public int getParentThreadID() { return parentThreadID; }

    private int parentForumID;
    /** Returns <code>ForumID</code> of this attachment's parent forum or
      * <code>-1</code> if this attachment is not created yet. */
    public int getParentForumID() { return parentForumID; }

    private int parentCategoryID;
    /** Returns <code>CategoryID</code> of this attachment's parent category or
      * <code>-1</code> if this attachment is not created yet. */
    public int getParentCategoryID() { return parentCategoryID; }

    public AttachmentXML() {
        attachmentID = -1;
        parentPostID = -1;
        parentThreadID = -1;
        parentForumID = -1;
        parentCategoryID = -1;
    }

    public void setAttachmentID(String id) {
        attachmentID=XMLUtil.stringToIntDef(id, -1);
    }

    public void setParentPost(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof PostXML) {
            parentPostID=((PostXML)o).getPostID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent post.");
        }
    }

    public void setParentPostID(int value) {
        if (value<0) {
            parentPostID = -1;
        } else {
            parentPostID = value;
        }
    }

    public void setParentThread(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof PostXML) {
            parentThreadID=((PostXML)o).getParentThreadID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent thread.");
        }
    }

    public void setParentThreadID(int value) {
        if (value<0) {
            parentThreadID = -1;
        } else {
            parentThreadID = value;
        }
    }

    public void setParentForum(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof PostXML) {
            parentForumID=((PostXML)o).getParentForumID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent forum.");
        }
    }

    public void setParentForumID(int value) {
        if (value<0) {
            parentForumID = -1;
        } else {
            parentForumID = value;
        }
    }

    public void setParentCategory(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof PostXML) {
            parentCategoryID=((PostXML)o).getParentCategoryID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent category.");
        }
    }

    public void setParentCategoryID(int value) {
        if (value < 0) {
            parentCategoryID = -1;
        } else {
            parentCategoryID = value;
        }
    }

    /**
     * Creates an attachment. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.<br/>
     * This method must be called after you've assigned the positive ID to this
     * attachment (using <code>setAttachmentID(java.lang.String)</code> method).
     *
     * @param memberName Can be null.
     * @param attachFilename Name of attachment file to be displayed on forum pages.
     * @param attachFileSize Size of attachment file.
     * @param attachMimeType MIME type of attachment file.
     * @param attachDesc Can be null.
     * @param attachCreationIP Can be null.
     * @param attachCreationDate Can be null.
     * @param attachModifiedDate Can be null.
     * @param attachDownloadCount Can be null.
     * @param attachOption Can be null.
     * @param attachStatus Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addAttachment(
                       String memberName, String attachFilename,
                       String attachFileSize, String attachMimeType,
                       String attachDesc, String attachCreationIP,
                       String attachCreationDate, String attachModifiedDate,
                       String attachDownloadCount, String attachOption,
                       String attachStatus)
        throws CreateException, ObjectNotFoundException, DatabaseException {

        if (attachmentID > 0) {
            addAttachment(Integer.toString(attachmentID),
                          memberName, attachFilename, attachFileSize, attachMimeType,
                          attachDesc, attachCreationIP, attachCreationDate, attachModifiedDate,
                          attachDownloadCount, attachOption, attachStatus);
        } else {
            throw new CreateException("Can't create an attachment, because it has no ID assigned yet.");
        }
    }

    /**
     * Creates an attachment. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param strAttachmentID Must be non-null valid integer number, because it
     *                        is the only way to know which file on server corresponds
     *                        to this attachment.
     * @param memberName Can be null.
     * @param attachFilename Name of attachment file to be displayed on forum pages.
     * @param attachFileSize Size of attachment file.
     * @param attachMimeType MIME type of attachment file.
     * @param attachDesc Can be null.
     * @param attachCreationIP Can be null.
     * @param attachCreationDate Can be null.
     * @param attachModifiedDate Can be null.
     * @param attachDownloadCount Can be null.
     * @param attachOption Can be null.
     * @param attachStatus Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addAttachment(String strAttachmentID,
                       String memberName, String attachFilename,
                       String attachFileSize, String attachMimeType,
                       String attachDesc, String attachCreationIP,
                       String attachCreationDate, String attachModifiedDate,
                       String attachDownloadCount, String attachOption,
                       String attachStatus)
        throws CreateException, ObjectNotFoundException, DatabaseException {

        if (parentPostID<0) {
            throw new CreateException("Can't create an attachment, because no parent post assigned yet.");
        }
        boolean idOk = (attachFilename!=null) &&
                       (attachFileSize!=null) &&
                       (attachMimeType!=null) &&
                       (strAttachmentID!=null) &&
                       (!strAttachmentID.equals(""));
        //now ensure that strAttachmentID is valid number, and >=0
        if (idOk) { 
            try {
                attachmentID = Integer.parseInt(strAttachmentID);
                idOk = (attachmentID>=0);
            } catch (NumberFormatException e) {
                idOk = false;
            }
        }

        if (!idOk) {
            attachmentID = -1;
            throw new CreateException("Not enough data to create an attachment, or the ID is invalid.");
        } else {
            //these values are not neccessary here, but I wanted to use them to validate
            //the strings - e.g, does attachOption string really contain a number ?
            int attachFileSize1;
            java.sql.Timestamp attachCreationDate1;
            java.sql.Timestamp attachModifiedDate1;
            int attachDownloadCount1;
            int attachOption1;
            int attachStatus1;

            try {
                if (memberName == null) {
                    memberName = "";
                }
                attachFileSize1= XMLUtil.stringToIntDef(attachFileSize, 0);
                if (attachDesc == null) {
                    attachDesc = "";
                }
                if (attachCreationIP == null) {
                    attachCreationIP = "0.0.0.0";
                }
                attachCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(attachCreationDate);
                attachModifiedDate1 = XMLUtil.stringToSqlTimestampDefNull(attachModifiedDate);
                attachDownloadCount1 = XMLUtil.stringToIntDef(attachDownloadCount, 0);
                attachOption1 = XMLUtil.stringToIntDef(attachOption, 0);
                attachStatus1 = XMLUtil.stringToIntDef(attachStatus, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for an attachment. Expected a number.");
            }

            //allow memberName to be empty, meaning unknown user (don't use MEMBER_ID_OF_GUEST)
            int memberID = 0;
            if (memberName.length() > 0) {
                memberID=DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
            }
            //I must change all possible nulls into "", so I don't get "'null'" in SQL query
            String attachModifiedDate2= XMLUtil.sqlTimestampToStringDefEmpty(attachModifiedDate1);

            attachFilename=EnableHtmlTagFilter.filter(attachFilename);
            attachMimeType=EnableHtmlTagFilter.filter(attachMimeType);
            attachDesc=EnableHtmlTagFilter.filter(attachDesc);
            if (ImportWebHelper.execUpdateQuery(
               "INSERT INTO "+ AttachmentDAO.TABLE_NAME +
               " (AttachID, PostID, MemberID," +
               " AttachFilename, AttachFileSize, AttachMimeType," +
               " AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate," +
               " AttachDownloadCount, AttachOption, AttachStatus)" +
               " VALUES (" +strAttachmentID+ ", "+parentPostID+", " +memberID+
               ", '" +attachFilename+ "', " +attachFileSize1+
               ", '" +attachMimeType+ "', '" +attachDesc+
               "', '" +attachCreationIP+ "', '" +attachCreationDate1+
               "', '" +attachModifiedDate2+ "', " +attachDownloadCount1+
               ", " +attachOption1+ ", " +attachStatus1+ ")"
            ) != 1) {
                throw new CreateException("Error adding attachment \""+attachFilename+"\" into table '"+
                    AttachmentDAO.TABLE_NAME +"'.");
            }

            //attachmentID was already set up
        }
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================
    public static void exportAttachmentList(XMLWriter xmlWriter, int parentPostID)
    throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        Collection attachments=ExportWebHelper.execSqlQuery(
                   "SELECT AttachID, MemberID,"+
                   " AttachFilename, AttachFileSize, AttachMimeType, AttachDesc,"+
                   " AttachCreationIP, AttachCreationDate, AttachModifiedDate,"+
                   " AttachDownloadCount, AttachOption, AttachStatus"+
                   " FROM "+AttachmentDAO.TABLE_NAME+
                   " WHERE PostID="+Integer.toString(parentPostID));
        Iterator iter=attachments.iterator();
        String[] attachment=null;
        //try {
            xmlWriter.startElement("AttachmentList");
            try {
                while ( (attachment=(String[])iter.next()) !=null) {
                    if (attachment.length!=12) {
                        throw new ExportException("Error while retrieving list of attachments for postID="+parentPostID+".");
                    }
                    xmlWriter.startElement("Attachment", new String[]{"id", attachment[0]});
                    String memberName=DAOFactory.getMemberDAO().getMember(Integer.parseInt(attachment[1])).getMemberName();
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("AttachFilename");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(attachment[2]));
                    xmlWriter.endElement("AttachFilename");
                    xmlWriter.startElement("AttachFileSize");
                    xmlWriter.writeData(attachment[3]);
                    xmlWriter.endElement("AttachFileSize");
                    xmlWriter.startElement("AttachMimeType");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(attachment[4]));
                    xmlWriter.endElement("AttachMimeType");
                    xmlWriter.startElement("AttachDesc");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(attachment[5]));
                    xmlWriter.endElement("AttachDesc");
                    xmlWriter.startElement("AttachCreationIP");
                    xmlWriter.writeData(attachment[6]);
                    xmlWriter.endElement("AttachCreationIP");
                    xmlWriter.startElement("AttachCreationDate");
                    xmlWriter.writeData(attachment[7]);
                    xmlWriter.endElement("AttachCreationDate");
                    xmlWriter.startElement("AttachModifiedDate");
                    xmlWriter.writeData(attachment[8]);
                    xmlWriter.endElement("AttachModifiedDate");
                    xmlWriter.startElement("AttachDownloadCount");
                    xmlWriter.writeData(attachment[9]);
                    xmlWriter.endElement("AttachDownloadCount");
                    xmlWriter.startElement("AttachOption");
                    xmlWriter.writeData(attachment[10]);
                    xmlWriter.endElement("AttachOption");
                    xmlWriter.startElement("AttachStatus");
                    xmlWriter.writeData(attachment[11]);
                    xmlWriter.endElement("AttachStatus");
                    xmlWriter.endElement("Attachment");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("AttachmentList");
         //} catch throw exportexception
    }
}
