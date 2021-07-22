/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinAttachmentTypeBean.java,v 1.2 2009/10/07 01:53:39 lexuanttkhtn Exp $
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
 * The Class VBulletinAttachmentTypeBean.
 */
public class VBulletinAttachmentTypeBean {

    /** The extension. */
    private String extension;
    
    /** The mimetype. */
    private String mimetype;
    
    /** The size. */
    private int size;
    
    /** The width. */
    private int width;
    
    /** The height. */
    private int height;
    
    /** The enabled. */
    private int enabled;
    
    /** The display. */
    private int display;
    
    /** The thumbnail. */
    private int thumbnail;
    
    /** The new window. */
    private int newWindow;

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
    
    /**
     * Gets the mimetype.
     * 
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }
    
    /**
     * Sets the mimetype.
     * 
     * @param mimetype the new mimetype
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
    
    /**
     * Gets the size.
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Sets the size.
     * 
     * @param size the new size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Gets the width.
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Sets the width.
     * 
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * Gets the height.
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Sets the height.
     * 
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * Gets the enabled.
     * 
     * @return the enabled
     */
    public int getEnabled() {
        return enabled;
    }
    
    /**
     * Sets the enabled.
     * 
     * @param enabled the new enabled
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Gets the display.
     * 
     * @return the display
     */
    public int getDisplay() {
        return display;
    }
    
    /**
     * Sets the display.
     * 
     * @param display the new display
     */
    public void setDisplay(int display) {
        this.display = display;
    }
    
    /**
     * Gets the thumbnail.
     * 
     * @return the thumbnail
     */
    public int getThumbnail() {
        return thumbnail;
    }
    
    /**
     * Sets the thumbnail.
     * 
     * @param thumbnail the new thumbnail
     */
    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    /**
     * Gets the new window.
     * 
     * @return the new window
     */
    public int getNewWindow() {
        return newWindow;
    }
    
    /**
     * Sets the new window.
     * 
     * @param newWindow the new new window
     */
    public void setNewWindow(int newWindow) {
        this.newWindow = newWindow;
    }
    
} //end of class VBulletinAttachmentTypeBean
