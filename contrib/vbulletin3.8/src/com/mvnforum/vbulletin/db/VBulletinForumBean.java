/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinForumBean.java,v 1.3 2009/10/07 04:23:22 lexuanttkhtn Exp $
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
 * The Class VBulletinForumBean.
 */
public class VBulletinForumBean {

    /** The forum id. */
    private int forumID;
    
    /** The style id. */
    private int styleID;
    
    /** The title. */
    private String title;
    
    /** The title_ clean. */
    private String title_Clean;
    
    /** The description. */
    private String description;
    
    /** The description_ clean. */
    private String description_Clean;
    
    /** The options. */
    private int options;
    
    /** The show private. */
    private int showPrivate;
    
    /** The display order. */
    private int displayOrder;
    
    /** The reply count. */
    private int replyCount;
    
    /** The last post. */
    private long lastPost;
    
    /** The last poster. */
    private String lastPoster;
    
    /** The last post id. */
    private int lastPostID;
    
    /** The last thread. */
    private String lastThread;
    
    /** The last thread id. */
    private int lastThreadID;
    
    /** The last icon id. */
    private int lastIconID;
    
    /** The last prefix id. */
    private String lastPrefixID;
    
    /** The thread count. */
    private int threadCount;
    
    /** The day sprune. */
    private int daySprune;
    
    /** The new post email. */
    private String newPostEmail;
    
    /** The new thread email. */
    private String newThreadEmail;
    
    /** The parent id. */
    private int parentID;
    
    /** The parent list. */
    private String parentList;
    
    /** The password. */
    private String password;
    
    /** The link. */
    private String link;
    
    /** The child list. */
    private String childList;
    
    /** The default sort field. */
    private String defaultSortField;
    
    /** The default sort order. */
    private String defaultSortOrder;
    
    /** The image prefix. */
    private String imagePrefix;

    /**
     * Gets the forum id.
     * 
     * @return the forum id
     */
    public int getForumID() {
        return forumID;
    }
    
    /**
     * Sets the forum id.
     * 
     * @param forumID the new forum id
     */
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }
    
    /**
     * Gets the style id.
     * 
     * @return the style id
     */
    public int getStyleID() {
        return styleID;
    }
    
    /**
     * Sets the style id.
     * 
     * @param styleID the new style id
     */
    public void setStyleID(int styleID) {
        this.styleID = styleID;
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
     * Gets the title_ clean.
     * 
     * @return the title_ clean
     */
    public String getTitle_Clean() {
        return title_Clean;
    }
    
    /**
     * Sets the title_ clean.
     * 
     * @param title_Clean the new title_ clean
     */
    public void setTitle_Clean(String title_Clean) {
        this.title_Clean = title_Clean;
    }
    
    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description_ clean.
     * 
     * @return the description_ clean
     */
    public String getDescription_Clean() {
        return description_Clean;
    }
    
    /**
     * Sets the description_ clean.
     * 
     * @param description_Clean the new description_ clean
     */
    public void setDescription_Clean(String description_Clean) {
        this.description_Clean = description_Clean;
    }
    
    /**
     * Gets the options.
     * 
     * @return the options
     */
    public int getOptions() {
        return options;
    }
    
    /**
     * Sets the options.
     * 
     * @param options the new options
     */
    public void setOptions(int options) {
        this.options = options;
    }
    
    /**
     * Gets the show private.
     * 
     * @return the show private
     */
    public int getShowPrivate() {
        return showPrivate;
    }
    
    /**
     * Sets the show private.
     * 
     * @param showPrivate the new show private
     */
    public void setShowPrivate(int showPrivate) {
        this.showPrivate = showPrivate;
    }
    
    /**
     * Gets the display order.
     * 
     * @return the display order
     */
    public int getDisplayOrder() {
        return displayOrder;
    }
    
    /**
     * Sets the display order.
     * 
     * @param displayOrder the new display order
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    /**
     * Gets the reply count.
     * 
     * @return the reply count
     */
    public int getReplyCount() {
        return replyCount;
    }
    
    /**
     * Sets the reply count.
     * 
     * @param replyCount the new reply count
     */
    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
    
    /**
     * Gets the last post.
     * 
     * @return the last post
     */
    public long getLastPost() {
        return lastPost;
    }
    
    /**
     * Sets the last post.
     * 
     * @param lastPost the new last post
     */
    public void setLastPost(long lastPost) {
        this.lastPost = lastPost;
    }
    
    /**
     * Gets the last poster.
     * 
     * @return the last poster
     */
    public String getLastPoster() {
        return lastPoster;
    }
    
    /**
     * Sets the last poster.
     * 
     * @param lastPoster the new last poster
     */
    public void setLastPoster(String lastPoster) {
        this.lastPoster = lastPoster;
    }
    
    /**
     * Gets the last post id.
     * 
     * @return the last post id
     */
    public int getLastPostID() {
        return lastPostID;
    }
    
    /**
     * Sets the last post id.
     * 
     * @param lastPostID the new last post id
     */
    public void setLastPostID(int lastPostID) {
        this.lastPostID = lastPostID;
    }
    
    /**
     * Gets the last thread.
     * 
     * @return the last thread
     */
    public String getLastThread() {
        return lastThread;
    }
    
    /**
     * Sets the last thread.
     * 
     * @param lastThread the new last thread
     */
    public void setLastThread(String lastThread) {
        this.lastThread = lastThread;
    }
    
    /**
     * Gets the last thread id.
     * 
     * @return the last thread id
     */
    public int getLastThreadID() {
        return lastThreadID;
    }
    
    /**
     * Sets the last thread id.
     * 
     * @param lastThreadID the new last thread id
     */
    public void setLastThreadID(int lastThreadID) {
        this.lastThreadID = lastThreadID;
    }
    
    /**
     * Gets the last icon id.
     * 
     * @return the last icon id
     */
    public int getLastIconID() {
        return lastIconID;
    }
    
    /**
     * Sets the last icon id.
     * 
     * @param lastIconID the new last icon id
     */
    public void setLastIconID(int lastIconID) {
        this.lastIconID = lastIconID;
    }
    
    /**
     * Gets the last prefix id.
     * 
     * @return the last prefix id
     */
    public String getLastPrefixID() {
        return lastPrefixID;
    }
    
    /**
     * Sets the last prefix id.
     * 
     * @param lastPrefixID the new last prefix id
     */
    public void setLastPrefixID(String lastPrefixID) {
        this.lastPrefixID = lastPrefixID;
    }
    
    /**
     * Gets the thread count.
     * 
     * @return the thread count
     */
    public int getThreadCount() {
        return threadCount;
    }
    
    /**
     * Sets the thread count.
     * 
     * @param threadCount the new thread count
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    
    /**
     * Gets the day sprune.
     * 
     * @return the day sprune
     */
    public int getDaySprune() {
        return daySprune;
    }
    
    /**
     * Sets the day sprune.
     * 
     * @param daySprune the new day sprune
     */
    public void setDaySprune(int daySprune) {
        this.daySprune = daySprune;
    }
    
    /**
     * Gets the new post email.
     * 
     * @return the new post email
     */
    public String getNewPostEmail() {
        return newPostEmail;
    }
    
    /**
     * Sets the new post email.
     * 
     * @param newPostEmail the new new post email
     */
    public void setNewPostEmail(String newPostEmail) {
        this.newPostEmail = newPostEmail;
    }
    
    /**
     * Gets the new thread email.
     * 
     * @return the new thread email
     */
    public String getNewThreadEmail() {
        return newThreadEmail;
    }
    
    /**
     * Sets the new thread email.
     * 
     * @param newThreadEmail the new new thread email
     */
    public void setNewThreadEmail(String newThreadEmail) {
        this.newThreadEmail = newThreadEmail;
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
     * Gets the parent list.
     * 
     * @return the parent list
     */
    public String getParentList() {
        return parentList;
    }
    
    /**
     * Sets the parent list.
     * 
     * @param parentList the new parent list
     */
    public void setParentList(String parentList) {
        this.parentList = parentList;
    }
    
    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the link.
     * 
     * @return the link
     */
    public String getLink() {
        return link;
    }
    
    /**
     * Sets the link.
     * 
     * @param link the new link
     */
    public void setLink(String link) {
        this.link = link;
    }
    
    /**
     * Gets the child list.
     * 
     * @return the child list
     */
    public String getChildList() {
        return childList;
    }
    
    /**
     * Sets the child list.
     * 
     * @param childList the new child list
     */
    public void setChildList(String childList) {
        this.childList = childList;
    }
    
    /**
     * Gets the default sort field.
     * 
     * @return the default sort field
     */
    public String getDefaultSortField() {
        return defaultSortField;
    }
    
    /**
     * Sets the default sort field.
     * 
     * @param defaultSortField the new default sort field
     */
    public void setDefaultSortField(String defaultSortField) {
        this.defaultSortField = defaultSortField;
    }
    
    /**
     * Gets the default sort order.
     * 
     * @return the default sort order
     */
    public String getDefaultSortOrder() {
        return defaultSortOrder;
    }
    
    /**
     * Sets the default sort order.
     * 
     * @param defaultSortOrder the new default sort order
     */
    public void setDefaultSortOrder(String defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
    }
    
    /**
     * Gets the image prefix.
     * 
     * @return the image prefix
     */
    public String getImagePrefix() {
        return imagePrefix;
    }
    
    /**
     * Sets the image prefix.
     * 
     * @param imagePrefix the new image prefix
     */
    public void setImagePrefix(String imagePrefix) {
        this.imagePrefix = imagePrefix;
    }
    
} //end of class ForumBean
