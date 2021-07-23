/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageFolderBean.java,v 1.14 2009/03/26 06:52:01 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.14 $
 * $Date: 2009/03/26 06:52:01 $
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

/*
 * Included columns: FolderName, MemberID, FolderOrder, FolderStatus, FolderOption,
 *                   FolderType, FolderCreationDate, FolderModifiedDate
 * Excluded columns:
 */
public class MessageFolderBean {
    
    private String folderName;
    private int memberID;
    private int folderOrder;
    private int folderStatus;
    private int folderOption;
    private int folderType;
    private Timestamp folderCreationDate;
    private Timestamp folderModifiedDate;

    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getFolderOrder() {
        return folderOrder;
    }
    public void setFolderOrder(int folderOrder) {
        this.folderOrder = folderOrder;
    }

    public int getFolderStatus() {
        return folderStatus;
    }
    public void setFolderStatus(int folderStatus) {
        this.folderStatus = folderStatus;
    }

    public int getFolderOption() {
        return folderOption;
    }
    public void setFolderOption(int folderOption) {
        this.folderOption = folderOption;
    }

    public int getFolderType() {
        return folderType;
    }
    public void setFolderType(int folderType) {
        this.folderType = folderType;
    }

    public Timestamp getFolderCreationDate() {
        return folderCreationDate;
    }
    public void setFolderCreationDate(Timestamp folderCreationDate) {
        this.folderCreationDate = folderCreationDate;
    }

    public Timestamp getFolderModifiedDate() {
        return folderModifiedDate;
    }
    public void setFolderModifiedDate(Timestamp folderModifiedDate) {
        this.folderModifiedDate = folderModifiedDate;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private int messageCount;
    private int unreadMessageCount;

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getUnreadMessageCount() {
        return this.unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

} //end of class MessageFolderBean
