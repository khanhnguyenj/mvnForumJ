/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinMessageFolderBean.java,v 1.2 2009/10/07 01:53:39 lexuanttkhtn Exp $
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

/**
 * The Class VBulletinMessageFolderBean.
 */
public class VBulletinMessageFolderBean {

    /** The user id. */
    private int userID;
    
    /** The user name. */
    private String userName;
    
    /** The p m folders. */
    private String pMFolders;

    /**
     * Gets the user id.
     * 
     * @return the user id
     */
    public int getUserID() {
        return userID;
    }
    
    /**
     * Sets the user id.
     * 
     * @param userID the new user id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Sets the user name.
     * 
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Gets the pM folders.
     * 
     * @return the pM folders
     */
    public String getPMFolders() {
        return pMFolders;
    }
    
    /**
     * Sets the pM folders.
     * 
     * @param pMFolders the new pM folders
     */
    public void setPMFolders(String pMFolders) {
        this.pMFolders = pMFolders;
    }
    
} //end of class VBulletinMessageFolderBean
