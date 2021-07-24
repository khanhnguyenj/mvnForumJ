/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumForumXML.java,v 1.10 2009/01/06 18:31:31 minhnn Exp $
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

import com.mvnforum.admin.ForumXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.10 $, $Date: 2009/01/06 18:31:31 $
 * <br/>
 * <code>MvnForumForumXML</code> class encapsulates processing of
 * forums' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>ForumXML</code> and other neccessary classes in order to perform
 * the actual creation of a forum, as well as other related items (like
 * forum watches).
 */
public class MvnForumForumXML {

    private ForumXML forumXML = null;
    private boolean forumCreated = false;
    private MvnForumCategoryXML parentCategory = null;

    String lastPostMemberName  = null;
    String forumName           = null;
    String forumDesc           = null;
    String forumCreationDate   = null;
    String forumModifiedDate   = null;
    String forumLastPostDate   = null;
    String forumOrder          = null;
    String forumType           = null;
    String forumFormatOption   = null;
    String forumOption         = null;
    String forumStatus         = null;
    String forumModerationMode = null;
    String forumPassword       = null;
    String forumThreadCount    = null;
    String forumPostCount      = null;

    public MvnForumForumXML() {
        forumXML = new ForumXML();
        forumCreated = false;
        parentCategory = null;
    }

    public int getForumID() {
        return forumXML.getForumID();
    }

    public void setForumID(String id) {
        forumXML.setForumID(id);
    }

    /**
     * This method simply calls <code>setForumID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setForumID()</code> as a setter
     * method for <code>forumID</code> property).
     */
    public void setForumId(String id) {
        setForumID(id);
    }

    public int getParentCategoryID() {
        return forumXML.getParentCategoryID(); //==parentCategory.getCategoryID();
    }

    public void setParentCategory(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof MvnForumCategoryXML) {
            parentCategory=(MvnForumCategoryXML)o;
            /* warning: parent category might not be added to database yet, so
             * we don't have parentCategoryID now, and can't do this here:
             * forumXML.setParentCategoryID(parentCategory.getCategoryID());
             */
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent category.");
        }
    }

    public void setForumLastPostMemberName(String value) {
        lastPostMemberName=value;
    }

    public void setForumName(String value) {
        forumName=value;
    }

    public void setForumDesc(String value) {
        forumDesc=value;
    }

    public void setForumCreationDate(String value) {
        forumCreationDate=value;
    }

    public void setForumModifiedDate(String value) {
        forumModifiedDate=value;
    }

    public void setForumLastPostDate(String value) {
        forumLastPostDate=value;
    }

    public void setForumOrder(String value) {
        forumOrder=value;
    }

    public void setForumType(String value) {
        forumType=value;
    }

    public void setForumFormatOption(String value) {
        forumFormatOption=value;
    }

    public void setForumOption(String value) {
        forumOption=value;
    }

    public void setForumStatus(String value) {
        forumStatus=value;
    }

    public void setForumModerationMode(String value) {
        forumModerationMode=value;
    }

    public void setForumPassword(String value) {
        forumPassword=value;
    }

    public void setForumThreadCount(String value) {
        forumThreadCount=value;
    }

    public void setForumPostCount(String value) {
        forumPostCount=value;
    }


    public void addForum() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, 
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this forum has
         * subelements that need it already be defined, so they first call
         * this method to create forum before creating data that refer him.
         */
        if (forumCreated) {
            return;
        }
        /* Second, create parent category if it's not yet created. */
        if (parentCategory != null) {
            parentCategory.addCategory();
            forumXML.setParentCategoryID(parentCategory.getCategoryID());
        }

        ImportMvnForum.addMessage("Adding forum \"" + forumName + "\".");
        forumXML.addForum(lastPostMemberName, forumName, forumDesc,
                          forumCreationDate, forumModifiedDate, forumLastPostDate,
                          forumOrder, forumType, forumFormatOption,
                          forumOption, forumStatus, forumModerationMode,
                          forumPassword, forumThreadCount, forumPostCount);
        forumCreated = true;

        if (parentCategory != null) {
            parentCategory.updateAddedForum(this);
        }
    }

    public void addMemberForumPermission(String memberName, String forumPermission)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!forumCreated) || (forumXML.getForumID()<0) ) {
            addForum();
        }
        ImportMvnForum.addMessage("Adding forum-specific permission for member \""+memberName+"\".");
        forumXML.addMemberForumPermission(memberName, forumPermission);
    }

    public void addGroupForumPermission(String groupName, String forumPermission)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!forumCreated) || (forumXML.getForumID()<0) ) {
            addForum();
        }
        ImportMvnForum.addMessage("Adding forum-specific permission for group \""+groupName+"\".");
        forumXML.addGroupForumPermission(groupName, forumPermission);
    }

    public void addForumWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!forumCreated) || (forumXML.getForumID()<0) ) {
            addForum();
        }
        ImportMvnForum.addMessage("Adding forum watch for member \"" + memberName + "\".");
        forumXML.addForumWatch(memberName,
                    watchType, watchOption, watchStatus,
                    watchCreationDate, watchLastSentDate, watchEndDate);
    }

    public void updateAddedThread(MvnForumThreadXML subThread) {
        if ((!forumCreated) || (forumXML.getForumID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        //forumXML.increaseThreadCount();
        if (parentCategory!=null) {
            parentCategory.updateAddedThread(subThread);
        }
    }

    public void updateAddedPost(MvnForumPostXML subPost, String postUsername, String postCreationDate) {
        if ((!forumCreated) || (forumXML.getForumID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        //forumXML.increasePostCount();
        //forumXML.updateLastPostMemberName(postUsername);
        //forumXML.updateLastPostDate(postCreationDate);
        if (parentCategory!=null) {
            parentCategory.updateAddedPost(subPost);
        }
    }

    public void updateAddedAttachment(MvnForumAttachmentXML subAttachment) {
        if ((!forumCreated) || (forumXML.getForumID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        // //check what is needed to be updated
        if (parentCategory!=null) {
            parentCategory.updateAddedAttachment(subAttachment);
        }
    }

}
