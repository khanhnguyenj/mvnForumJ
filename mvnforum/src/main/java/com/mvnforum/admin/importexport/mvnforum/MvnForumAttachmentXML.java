/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumAttachmentXML.java,v 1.10 2009/01/06 18:31:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/01/06 18:31:31 $
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
 * @author: Igor Manic   
 */
package com.mvnforum.admin.importexport.mvnforum;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.admin.AttachmentXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.10 $, $Date: 2009/01/06 18:31:31 $
 * <br/>
 * <code>MvnForumAttachmentXML</code> class encapsulates processing of
 * attachments' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>AttachmentXML</code> and other neccessary classes in order to perform
 * the actual creation of an attachment.
 */
public class MvnForumAttachmentXML {

    private AttachmentXML attachmentXML=null;
    private boolean attachmentCreated=false;
    private MvnForumPostXML parentPost =null;

    String attachMemberName    = null;
    String attachFilename      = null;
    String attachFileSize      = null;
    String attachMimeType      = null;
    String attachDesc          = null;
    String attachCreationIP    = null;
    String attachCreationDate  = null;
    String attachModifiedDate  = null;
    String attachDownloadCount = null;
    String attachOption        = null;
    String attachStatus        = null;

    public MvnForumAttachmentXML() {
        attachmentXML = new AttachmentXML();
        attachmentCreated = false;
        parentPost = null;
    }

    public int getAttachmentID() {
        return attachmentXML.getAttachmentID();
    }

    public void setAttachmentID(String id) {
        if (id != null) {
            attachmentXML.setAttachmentID(id);
        }
    }

    /**
     * This method simply calls <code>setAttachmentID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setAttachmentID()</code> as a setter
     * method for <code>attachmentID</code> property).
     */
    public void setAttachmentId(String id) {
        setAttachmentID(id);
    }

    public int getParentCategoryID() {
        return attachmentXML.getParentCategoryID(); //==parentPost.getParentCategoryID();
    }

    public int getParentForumID() {
        return attachmentXML.getParentForumID(); //==parentPost.getParentForumID();
    }

    public int getParentThreadID() {
        return attachmentXML.getParentThreadID(); //==parentPost.getParentThreadID();
    }

    public int getParentPostID() {
        return attachmentXML.getParentPostID(); //==parentPost.getPostID();
    }

    public void setParentPost(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof MvnForumPostXML) {
            parentPost=(MvnForumPostXML)o;
            //warning: parent post might be not added to database yet
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent post.");
        }
    }

    public void setAttachMemberName(String value) {
        attachMemberName=value;
    }

    public void setAttachFilename(String value) {
        attachFilename=value;
    }

    public void setAttachFileSize(String value) {
        attachFileSize=value;
    }

    public void setAttachMimeType(String value) {
        attachMimeType=value;
    }

    public void setAttachDesc(String value) {
        attachDesc=value;
    }

    public void setAttachCreationIP(String value) {
        attachCreationIP=value;
    }

    public void setAttachCreationDate(String value) {
        attachCreationDate=value;
    }

    public void setAttachModifiedDate(String value) {
        attachModifiedDate=value;
    }

    public void setAttachDownloadCount(String value) {
        attachDownloadCount=value;
    }

    public void setAttachOption(String value) {
        attachOption=value;
    }

    public void setAttachStatus(String value) {
        attachStatus=value;
    }

    public void addAttachment() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, 
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this attachment has
         * subelements that need it already be defined, so they first call
         * this method to create attachment before creating data that refer him.
         */
        if (attachmentCreated) {
            return;
        }
        /* Second, create parent post if it's not yet created. */
        if (parentPost != null) {
            parentPost.addPost();
            attachmentXML.setParentPostID(parentPost.getPostID());
            attachmentXML.setParentThreadID(parentPost.getParentThreadID());
            attachmentXML.setParentForumID(parentPost.getParentForumID());
            attachmentXML.setParentCategoryID(parentPost.getParentCategoryID());
        }

        ImportMvnForum.addMessage("Adding attachment \"" + attachFilename + "\".");
        //AttachmentXML.addAttachment() will check itself if the ID is assigned, as it should be
        attachmentXML.addAttachment(attachMemberName, attachFilename,
                      attachFileSize, attachMimeType, attachDesc,
                      attachCreationIP, attachCreationDate, attachModifiedDate,
                      attachDownloadCount, attachOption, attachStatus);
        attachmentCreated=true;

        if (parentPost != null) {
            parentPost.updateAddedAttachment(this);
        }
    }


}
