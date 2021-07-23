/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/AttachmentBean.java,v 1.12 2010/06/15 11:56:59 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2010/06/15 11:56:59 $
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
package com.mvnforum.db;

import java.sql.Timestamp;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
 *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
 *                   AttachDownloadCount, AttachOption, AttachStatus
 * Excluded columns:
 */
public class AttachmentBean {
    
    private int attachID;
    private int postID;
    private int memberID;
    private String attachFilename;
    private int attachFileSize;
    private String attachMimeType;
    private String attachDesc;
    private String attachCreationIP;
    private Timestamp attachCreationDate;
    private Timestamp attachModifiedDate;
    private int attachDownloadCount;
    /**
     * This field attachOption is to store the id of binary storage of this attachment
     */
    private int attachOption;
    private int attachStatus;

    public int getAttachID() {
        return attachID;
    }
    public void setAttachID(int attachID) {
        this.attachID = attachID;
    }

    public int getPostID() {
        return postID;
    }
    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getAttachFilename() {
        return attachFilename;
    }
    public void setAttachFilename(String attachFilename) {
        this.attachFilename = StringUtil.getEmptyStringIfNull(attachFilename);
    }

    public int getAttachFileSize() {
        return attachFileSize;
    }
    public void setAttachFileSize(int attachFileSize) {
        this.attachFileSize = attachFileSize;
    }

    public String getAttachMimeType() {
        return attachMimeType;
    }
    public void setAttachMimeType(String attachMimeType) {
        this.attachMimeType = StringUtil.getEmptyStringIfNull(attachMimeType);
    }

    public String getAttachDesc() {
        return attachDesc;
    }
    public void setAttachDesc(String attachDesc) {
        this.attachDesc = StringUtil.getEmptyStringIfNull(attachDesc);
    }

    public String getAttachCreationIP() {
        return attachCreationIP;
    }
    public void setAttachCreationIP(String attachCreationIP) {
        this.attachCreationIP = StringUtil.getEmptyStringIfNull(attachCreationIP);
    }

    public Timestamp getAttachCreationDate() {
        return attachCreationDate;
    }
    public void setAttachCreationDate(Timestamp attachCreationDate) {
        this.attachCreationDate = attachCreationDate;
    }

    public Timestamp getAttachModifiedDate() {
        return attachModifiedDate;
    }
    public void setAttachModifiedDate(Timestamp attachModifiedDate) {
        this.attachModifiedDate = attachModifiedDate;
    }

    public int getAttachDownloadCount() {
        return attachDownloadCount;
    }
    public void setAttachDownloadCount(int attachDownloadCount) {
        this.attachDownloadCount = attachDownloadCount;
    }

    public int getAttachOption() {
        return attachOption;
    }
    public void setAttachOption(int attachOption) {
        this.attachOption = attachOption;
    }

    public int getAttachStatus() {
        return attachStatus;
    }
    public void setAttachStatus(int attachStatus) {
        this.attachStatus = attachStatus;
    }

    /////////////////////////////////////////////////////////////////
    // utility methods
    private int forumID;
    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

} //end of class AttachmentBean
