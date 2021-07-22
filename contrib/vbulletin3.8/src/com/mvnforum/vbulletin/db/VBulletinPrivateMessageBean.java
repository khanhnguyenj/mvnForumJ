/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinPrivateMessageBean.java,v 1.2 2009/10/07 01:53:39 lexuanttkhtn Exp $
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
package com.mvnforum.vbulletin.db;

import java.sql.Timestamp;

/**
 * The Class VBulletinPrivateMessageBean.
 */
public class VBulletinPrivateMessageBean {

    /** The title. */
    private String title;
    
    /** The message. */
    private String message;
    
    /** The from user name. */
    private String fromUserName;
    
    /** The to user name. */
    private String toUserName;
    
    /** The to user array. */
    private String toUserArray;
    
    /** The folder id. */
    private int folderID;
    
    /** The read count. */
    private int readCount;
    
    /** The date line. */
    private Timestamp dateLine;
    
    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title.
     * 
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Sets the message.
     * 
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Gets the from user name.
     * 
     * @return the from user name
     */
    public String getFromUserName() {
        return fromUserName;
    }
    
    /**
     * Sets the from user name.
     * 
     * @param fromUserName the new from user name
     */
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    
    /**
     * Gets the to user name.
     * 
     * @return the to user name
     */
    public String getToUserName() {
        return toUserName;
    }
    
    /**
     * Sets the to user name.
     * 
     * @param toUserName the new to user name
     */
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
    
    /**
     * Gets the to user array.
     * 
     * @return the to user array
     */
    public String getToUserArray() {
        return toUserArray;
    }
    
    /**
     * Sets the to user array.
     * 
     * @param toUserArray the new to user array
     */
    public void setToUserArray(String toUserArray) {
        this.toUserArray = toUserArray;
    }
    
    /**
     * Gets the folder id.
     * 
     * @return the folder id
     */
    public int getFolderID() {
        return folderID;
    }
    
    /**
     * Sets the folder id.
     * 
     * @param folderID the new folder id
     */
    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }
    
    /**
     * Gets the read count.
     * 
     * @return the read count
     */
    public int getReadCount() {
        return readCount;
    }
    
    /**
     * Sets the read count.
     * 
     * @param readCount the new read count
     */
    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
    
    /**
     * Gets the date line.
     * 
     * @return the date line
     */
    public Timestamp getDateLine() {
        return dateLine;
    }
    
    /**
     * Sets the date line.
     * 
     * @param dateLine the new date line
     */
    public void setDateLine(Timestamp dateLine) {
        this.dateLine = dateLine;
    }
    
} //end of class VBulletinPrivateMessageBean
