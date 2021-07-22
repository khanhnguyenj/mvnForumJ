/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PmAttachmentBean.java,v 1.12 2010/06/15 11:56:59 minhnn Exp $
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
 * Included columns: PmAttachID, MemberID, PmAttachFilename, PmAttachFileSize,
 *                   PmAttachMimeType, PmAttachDesc, PmAttachCreationIP, PmAttachCreationDate, PmAttachModifiedDate,
 *                   PmAttachDownloadCount, PmAttachOption, PmAttachStatus
 * Excluded columns:
 */
public class PmAttachmentBean {
    
    private int pmAttachID;
    private int memberID;
    private String pmAttachFilename;
    private int pmAttachFileSize;
    private String pmAttachMimeType;
    private String pmAttachDesc;
    private String pmAttachCreationIP;
    private Timestamp pmAttachCreationDate;
    private Timestamp pmAttachModifiedDate;
    private int pmAttachDownloadCount;
    /**
     * This field pmAttachOption is to store the id of binary storage of this attachment
     */
    private int pmAttachOption;
    private int pmAttachStatus;

    public int getPmAttachID() {
        return pmAttachID;
    }
    public void setPmAttachID(int pmAttachID) {
        this.pmAttachID = pmAttachID;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getPmAttachFilename() {
        return pmAttachFilename;
    }
    public void setPmAttachFilename(String pmAttachFilename) {
        this.pmAttachFilename = StringUtil.getEmptyStringIfNull(pmAttachFilename);
    }

    public int getPmAttachFileSize() {
        return pmAttachFileSize;
    }
    public void setPmAttachFileSize(int pmAttachFileSize) {
        this.pmAttachFileSize = pmAttachFileSize;
    }

    public String getPmAttachMimeType() {
        return pmAttachMimeType;
    }
    public void setPmAttachMimeType(String pmAttachMimeType) {
        this.pmAttachMimeType = StringUtil.getEmptyStringIfNull(pmAttachMimeType);
    }

    public String getPmAttachDesc() {
        return pmAttachDesc;
    }
    public void setPmAttachDesc(String pmAttachDesc) {
        this.pmAttachDesc = StringUtil.getEmptyStringIfNull(pmAttachDesc);
    }

    public String getPmAttachCreationIP() {
        return pmAttachCreationIP;
    }
    public void setPmAttachCreationIP(String pmAttachCreationIP) {
        this.pmAttachCreationIP = StringUtil.getEmptyStringIfNull(pmAttachCreationIP);
    }

    public Timestamp getPmAttachCreationDate() {
        return pmAttachCreationDate;
    }
    public void setPmAttachCreationDate(Timestamp pmAttachCreationDate) {
        this.pmAttachCreationDate = pmAttachCreationDate;
    }

    public Timestamp getPmAttachModifiedDate() {
        return pmAttachModifiedDate;
    }
    public void setPmAttachModifiedDate(Timestamp pmAttachModifiedDate) {
        this.pmAttachModifiedDate = pmAttachModifiedDate;
    }

    public int getPmAttachDownloadCount() {
        return pmAttachDownloadCount;
    }
    public void setPmAttachDownloadCount(int pmAttachDownloadCount) {
        this.pmAttachDownloadCount = pmAttachDownloadCount;
    }

    public int getPmAttachOption() {
        return pmAttachOption;
    }
    public void setPmAttachOption(int pmAttachOption) {
        this.pmAttachOption = pmAttachOption;
    }

    public int getPmAttachStatus() {
        return pmAttachStatus;
    }
    public void setPmAttachStatus(int pmAttachStatus) {
        this.pmAttachStatus = pmAttachStatus;
    }

} //end of class PmAttachmentBean
