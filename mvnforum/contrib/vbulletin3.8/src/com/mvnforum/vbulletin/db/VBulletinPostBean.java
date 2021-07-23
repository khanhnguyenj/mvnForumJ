/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinPostBean.java,v 1.3 2009/10/07 04:23:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2009/10/07 04:23:22 $
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
 * The Class VBulletinPostBean.
 */
public class VBulletinPostBean {

    /** The post id. */
    private int postID;
    
    /** The thread id. */
    private int threadID;
    
    /** The parent id. */
    private int parentID;
    
    /** The username. */
    private String username;
    
    /** The user id. */
    private int userID;
    
    /** The title. */
    private String title;
    
    /** The date line. */
    private long dateLine;
    
    /** The page text. */
    private String pageText;
    
    /** The allow smilie. */
    private int allowSmilie;
    
    /** The show signature. */
    private int showSignature;
    
    /** The ip address. */
    private String ipAddress;
    
    /** The icon id. */
    private int iconID;
    
    /** The visible. */
    private int visible;
    
    /** The attach. */
    private int attach;
    
    /** The infraction. */
    private int infraction;
    
    /** The report thread id. */
    private int reportThreadID;

    /**
     * Gets the post id.
     * 
     * @return the post id
     */
    public int getPostID() {
        return postID;
    }
    
    /**
     * Sets the post id.
     * 
     * @param postID the new post id
     */
    public void setPostID(int postID) {
        this.postID = postID;
    }
    
    /**
     * Gets the thread id.
     * 
     * @return the thread id
     */
    public int getThreadID() {
        return threadID;
    }
    
    /**
     * Sets the thread id.
     * 
     * @param threadID the new thread id
     */
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }
    
    /**
     * Gets the parent id.
     * 
     * @return the parent id
     */
    public int getParentID() {
        return parentID;
    }
    
    /**
     * Sets the parent id.
     * 
     * @param parentID the new parent id
     */
    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
    
    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username.
     * 
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
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
     * Gets the date line.
     * 
     * @return the date line
     */
    public long getDateLine() {
        return dateLine;
    }
    
    /**
     * Sets the date line.
     * 
     * @param dateLine the new date line
     */
    public void setDateLine(long dateLine) {
        this.dateLine = dateLine;
    }
    
    /**
     * Gets the page text.
     * 
     * @return the page text
     */
    public String getPageText() {
        return pageText;
    }
    
    /**
     * Sets the page text.
     * 
     * @param pageText the new page text
     */
    public void setPageText(String pageText) {
        this.pageText = pageText;
    }
    
    /**
     * Gets the allow smilie.
     * 
     * @return the allow smilie
     */
    public int getAllowSmilie() {
        return allowSmilie;
    }
    
    /**
     * Sets the allow smilie.
     * 
     * @param allowSmilie the new allow smilie
     */
    public void setAllowSmilie(int allowSmilie) {
        this.allowSmilie = allowSmilie;
    }
    
    /**
     * Gets the show signature.
     * 
     * @return the show signature
     */
    public int getShowSignature() {
        return showSignature;
    }
    
    /**
     * Sets the show signature.
     * 
     * @param showSignature the new show signature
     */
    public void setShowSignature(int showSignature) {
        this.showSignature = showSignature;
    }
    
    /**
     * Gets the ip address.
     * 
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * Sets the ip address.
     * 
     * @param ipAddress the new ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    /**
     * Gets the icon id.
     * 
     * @return the icon id
     */
    public int getIconID() {
        return iconID;
    }
    
    /**
     * Sets the icon id.
     * 
     * @param iconID the new icon id
     */
    public void setIconID(int iconID) {
        this.iconID = iconID;
    }
    
    /**
     * Gets the visible.
     * 
     * @return the visible
     */
    public int getVisible() {
        return visible;
    }
    
    /**
     * Sets the visible.
     * 
     * @param visible the new visible
     */
    public void setVisible(int visible) {
        this.visible = visible;
    }
    
    /**
     * Gets the attach.
     * 
     * @return the attach
     */
    public int getAttach() {
        return attach;
    }
    
    /**
     * Sets the attach.
     * 
     * @param attach the new attach
     */
    public void setAttach(int attach) {
        this.attach = attach;
    }
    
    /**
     * Gets the infraction.
     * 
     * @return the infraction
     */
    public int getInfraction() {
        return infraction;
    }
    
    /**
     * Sets the infraction.
     * 
     * @param infraction the new infraction
     */
    public void setInfraction(int infraction) {
        this.infraction = infraction;
    }
    
    /**
     * Gets the report thread id.
     * 
     * @return the report thread id
     */
    public int getReportThreadID() {
        return reportThreadID;
    }
    
    /**
     * Sets the report thread id.
     * 
     * @param reportThreadID the new report thread id
     */
    public void setReportThreadID(int reportThreadID) {
        this.reportThreadID = reportThreadID;
    }
    
} //end of class PostBean
