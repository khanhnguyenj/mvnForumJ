/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinThreadBean.java,v 1.3 2009/10/07 04:23:22 lexuanttkhtn Exp $
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
 * The Class VBulletinThreadBean.
 */
public class VBulletinThreadBean {

    /** The thread id. */
    private int threadID;
    
    /** The title. */
    private String title;
    
    /** The prefix id. */
    private String prefixID;
    
    /** The first post id. */
    private int firstPostID;
    
    /** The last post id. */
    private int lastPostID;
    
    /** The last post. */
    private long lastPost;
    
    /** The forum id. */
    private int forumID;
    
    /** The poll id. */
    private int pollID;
    
    /** The open. */
    private int open;
    
    /** The reply count. */
    private int replyCount;
    
    /** The hidden count. */
    private int hiddenCount;
    
    /** The deleted count. */
    private int deletedCount;
    
    /** The post username. */
    private String postUsername;
    
    /** The post userid. */
    private int postUserid;
    
    /** The last poster. */
    private String lastPoster;
    
    /** The date line. */
    private long dateLine;
    
    /** The views. */
    private int views;
    
    /** The icon id. */
    private int iconID;
    
    /** The notes. */
    private String notes;
    
    /** The visible. */
    private int visible;
    
    /** The sticky. */
    private int sticky;
    
    /** The vote num. */
    private int voteNum;
    
    /** The vote total. */
    private int voteTotal;
    
    /** The attach. */
    private int attach;
    
    /** The similar. */
    private String similar;
    
    /** The taglist. */
    private String taglist;

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
     * Gets the prefix id.
     * 
     * @return the prefix id
     */
    public String getPrefixID() {
        return prefixID;
    }
    
    /**
     * Sets the prefix id.
     * 
     * @param prefixID the new prefix id
     */
    public void setPrefixID(String prefixID) {
        this.prefixID = prefixID;
    }
    
    /**
     * Gets the first post id.
     * 
     * @return the first post id
     */
    public int getFirstPostID() {
        return firstPostID;
    }
    
    /**
     * Sets the first post id.
     * 
     * @param firstPostID the new first post id
     */
    public void setFirstPostID(int firstPostID) {
        this.firstPostID = firstPostID;
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
     * Gets the poll id.
     * 
     * @return the poll id
     */
    public int getPollID() {
        return pollID;
    }
    
    /**
     * Sets the poll id.
     * 
     * @param pollID the new poll id
     */
    public void setPollID(int pollID) {
        this.pollID = pollID;
    }
    
    /**
     * Gets the open.
     * 
     * @return the open
     */
    public int getOpen() {
        return open;
    }
    
    /**
     * Sets the open.
     * 
     * @param open the new open
     */
    public void setOpen(int open) {
        this.open = open;
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
     * Gets the hidden count.
     * 
     * @return the hidden count
     */
    public int getHiddenCount() {
        return hiddenCount;
    }
    
    /**
     * Sets the hidden count.
     * 
     * @param hiddenCount the new hidden count
     */
    public void setHiddenCount(int hiddenCount) {
        this.hiddenCount = hiddenCount;
    }
    
    /**
     * Gets the deleted count.
     * 
     * @return the deleted count
     */
    public int getDeletedCount() {
        return deletedCount;
    }
    
    /**
     * Sets the deleted count.
     * 
     * @param deletedCount the new deleted count
     */
    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
    
    /**
     * Gets the post username.
     * 
     * @return the post username
     */
    public String getPostUsername() {
        return postUsername;
    }
    
    /**
     * Sets the post username.
     * 
     * @param postUsername the new post username
     */
    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }
    
    /**
     * Gets the post userid.
     * 
     * @return the post userid
     */
    public int getPostUserid() {
        return postUserid;
    }
    
    /**
     * Sets the post userid.
     * 
     * @param postUserid the new post userid
     */
    public void setPostUserid(int postUserid) {
        this.postUserid = postUserid;
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
     * Gets the views.
     * 
     * @return the views
     */
    public int getViews() {
        return views;
    }
    
    /**
     * Sets the views.
     * 
     * @param views the new views
     */
    public void setViews(int views) {
        this.views = views;
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
     * Gets the notes.
     * 
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }
    
    /**
     * Sets the notes.
     * 
     * @param notes the new notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
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
     * Gets the sticky.
     * 
     * @return the sticky
     */
    public int getSticky() {
        return sticky;
    }
    
    /**
     * Sets the sticky.
     * 
     * @param sticky the new sticky
     */
    public void setSticky(int sticky) {
        this.sticky = sticky;
    }
    
    /**
     * Gets the vote num.
     * 
     * @return the vote num
     */
    public int getVoteNum() {
        return voteNum;
    }
    
    /**
     * Sets the vote num.
     * 
     * @param voteNum the new vote num
     */
    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
    
    /**
     * Gets the vote total.
     * 
     * @return the vote total
     */
    public int getVoteTotal() {
        return voteTotal;
    }
    
    /**
     * Sets the vote total.
     * 
     * @param voteTotal the new vote total
     */
    public void setVoteTotal(int voteTotal) {
        this.voteTotal = voteTotal;
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
     * Gets the similar.
     * 
     * @return the similar
     */
    public String getSimilar() {
        return similar;
    }
    
    /**
     * Sets the similar.
     * 
     * @param similar the new similar
     */
    public void setSimilar(String similar) {
        this.similar = similar;
    }
    
    /**
     * Gets the tag list.
     * 
     * @return the tag list
     */
    public String getTaglist() {
        return taglist;
    }
    
    /**
     * Sets the tag list.
     * 
     * @param taglist the new tag list
     */
    public void setTaglist(String taglist) {
        this.taglist = taglist;
    }
    
} //end of class VBulletinThreadBean
