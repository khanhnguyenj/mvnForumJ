/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinAttachmentBean.java,v 1.3 2009/10/07 04:23:22 lexuanttkhtn Exp $
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
 * The Class VBulletinAttachmentBean.
 */
public class VBulletinAttachmentBean {

    /** The attachment id. */
    private int attachmentID;
    
    /** The user id. */
    private int userID;
    
    /** The date line. */
    private long dateLine;
    
    /** The thumbnail_ date line. */
    private int thumbnail_DateLine;
    
    /** The file name. */
    private String fileName;
    
    /** The file data. */
    private String fileData;
    
    /** The visible. */
    private int visible;
    
    /** The counter. */
    private int counter;
    
    /** The file size. */
    private int fileSize;
    
    /** The post id. */
    private int postID;
    
    /** The file hash. */
    private String fileHash;
    
    /** The post hash. */
    private String postHash;
    
    /** The thumbnail. */
    private String thumbnail;
    
    /** The thumbnail_ file size. */
    private int thumbnail_FileSize;
    
    /** The extension. */
    private String extension;

    /**
     * Gets the attachment id.
     * 
     * @return the attachment id
     */
    public int getAttachmentID() {
        return attachmentID;
    }
    
    /**
     * Sets the attachment id.
     * 
     * @param attachmentID the new attachment id
     */
    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
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
     * Gets the thumbnail_ date line.
     * 
     * @return the thumbnail_ date line
     */
    public int getThumbnail_DateLine() {
        return thumbnail_DateLine;
    }
    
    /**
     * Sets the thumbnail_ date line.
     * 
     * @param thumbnail_DateLine the new thumbnail_ date line
     */
    public void setThumbnail_DateLine(int thumbnail_DateLine) {
        this.thumbnail_DateLine = thumbnail_DateLine;
    }
    
    /**
     * Gets the file name.
     * 
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * Sets the file name.
     * 
     * @param fileName the new file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Gets the file data.
     * 
     * @return the file data
     */
    public String getFileData() {
        return fileData;
    }
    
    /**
     * Sets the file data.
     * 
     * @param fileData the new file data
     */
    public void setFileData(String fileData) {
        this.fileData = fileData;
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
     * Gets the counter.
     * 
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }
    
    /**
     * Sets the counter.
     * 
     * @param counter the new counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    /**
     * Gets the file size.
     * 
     * @return the file size
     */
    public int getFileSize() {
        return fileSize;
    }
    
    /**
     * Sets the file size.
     * 
     * @param fileSize the new file size
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    
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
     * Gets the file hash.
     * 
     * @return the file hash
     */
    public String getFileHash() {
        return fileHash;
    }
    
    /**
     * Sets the file hash.
     * 
     * @param fileHash the new file hash
     */
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
    
    /**
     * Gets the post hash.
     * 
     * @return the post hash
     */
    public String getPostHash() {
        return postHash;
    }
    
    /**
     * Sets the post hash.
     * 
     * @param postHash the new post hash
     */
    public void setPostHash(String postHash) {
        this.postHash = postHash;
    }
    
    /**
     * Gets the thumbnail.
     * 
     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }
    
    /**
     * Sets the thumbnail.
     * 
     * @param thumbnail the new thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    /**
     * Gets the thumbnail_ file size.
     * 
     * @return the thumbnail_ file size
     */
    public int getThumbnail_FileSize() {
        return thumbnail_FileSize;
    }
    
    /**
     * Sets the thumbnail_ file size.
     * 
     * @param thumbnail_FileSize the new thumbnail_ file size
     */
    public void setThumbnail_FileSize(int thumbnail_FileSize) {
        this.thumbnail_FileSize = thumbnail_FileSize;
    }
    
    /**
     * Gets the extension.
     * 
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }
    
    /**
     * Sets the extension.
     * 
     * @param extension the new extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
} //end of class VBulletinAttachmentBean
