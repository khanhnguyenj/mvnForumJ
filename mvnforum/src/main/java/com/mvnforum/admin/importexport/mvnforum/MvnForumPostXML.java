/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumPostXML.java,v 1.10 2009/01/06 18:31:31 minhnn Exp $
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

import com.mvnforum.admin.PostXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.10 $, $Date: 2009/01/06 18:31:31 $
 * <br/>
 * <code>MvnForumPostXML</code> class encapsulates processing of
 * posts' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>PostXML</code> and other neccessary classes in order to perform
 * the actual creation of a post.
 */
public class MvnForumPostXML {

    private PostXML postXML = null;
    private boolean postCreated = false;
    //only one of parentPost and parentThread will be defined (later), and the other will be null
    private MvnForumThreadXML parentThread = null;
    private MvnForumPostXML parentPost     = null;

    String postMemberName         = null;
    String postLastEditMemberName = null;
    String postTopic              = null;
    String postBody               = null;
    String postCreationDate       = null;
    String postLastEditDate       = null;
    String postCreationIP         = null;
    String postLastEditIP         = null;
    String postEditCount          = null;
    String postFormatOption       = null;
    String postOption             = null;
    String postStatus             = null;
    String postIcon               = null;
    String postAttachCount        = null;

    public MvnForumPostXML() {
        postXML = new PostXML();
        postCreated = false;
        parentThread = null;
        parentPost = null;
    }

    public int getPostID() {
        return postXML.getPostID();
    }

    public void setPostID(String id) {
        postXML.setPostID(id);
    }

    /**
     * This method simply calls <code>setPostID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setPostID()</code> as a setter
     * method for <code>postID</code> property).
     */
    public void setPostId(String id) {
        setPostID(id);
    }

    public int getParentCategoryID() {
        return postXML.getParentCategoryID();
    }

    public int getParentForumID() {
        return postXML.getParentForumID();
    }

    public int getParentThreadID() {
        return postXML.getParentThreadID();
    }

    public int getParentPostID() {
        return postXML.getParentPostID();
    }

    public void setParentThreadOrPost(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof MvnForumThreadXML) {
            parentThread=(MvnForumThreadXML)o;
            //warning: parent thread might be not added to database yet
            parentPost=null;
        } else if (o instanceof MvnForumPostXML) {
            parentPost=(MvnForumPostXML)o;
            //warning: parent post might be not added to database yet
            parentThread=null;
        } else {
            throw new ForeignKeyNotFoundException("Can't find neither parent thread nor post.");
        }
    }

    public void setPostMemberName(String value) {
        postMemberName=value;
    }

    public void setPostLastEditMemberName(String value) {
        postLastEditMemberName=value;
    }

    public void setPostTopic(String value) {
        postTopic=value;
    }

    public void setPostBody(String value) {
        postBody=value;
    }

    public void setPostCreationDate(String value) {
        postCreationDate=value;
    }

    public void setPostLastEditDate(String value) {
        postLastEditDate=value;
    }

    public void setPostCreationIP(String value) {
        postCreationIP=value;
    }

    public void setPostLastEditIP(String value) {
        postLastEditIP=value;
    }

    public void setPostEditCount(String value) {
        postEditCount=value;
    }

    public void setPostFormatOption(String value) {
        postFormatOption=value;
    }

    public void setPostOption(String value) {
        postOption=value;
    }

    public void setPostStatus(String value) {
        postStatus=value;
    }

    public void setPostIcon(String value) {
        postIcon=value;
    }

    public void setPostAttachCount(String value) {
        postAttachCount=value;
    }

    public void addPost() throws CreateException, DuplicateKeyException,
    ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException, BadInputException {
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this post has
         * subelements that need it already be defined, so they first call
         * this method to create post before creating data that refer him.
         */
        if (postCreated) {
            return;
        }
        /* Second, create parent thread and/or post if they are not yet created. */
        if (parentPost!=null) {
            parentPost.addPost();
            postXML.setParentPostID(parentPost.getPostID());
            postXML.setParentThreadID(parentPost.getParentThreadID());
            postXML.setParentForumID(parentPost.getParentForumID());
            postXML.setParentCategoryID(parentPost.getParentCategoryID());
        } else if (parentThread!=null) {
            parentThread.addThread();
            //postXML.setParentPostID(0) is not neccessary
            postXML.setParentThreadID(parentThread.getThreadID());
            postXML.setParentForumID(parentThread.getParentForumID());
            postXML.setParentCategoryID(parentThread.getParentCategoryID());
        }

        ImportMvnForum.addMessage("Adding post \""+postTopic+"\".");
        postXML.addPost(postMemberName, postLastEditMemberName,
                        postTopic, postBody,
                        postCreationDate, postLastEditDate,
                        postCreationIP, postLastEditIP,
                        postEditCount, postFormatOption,
                        postOption, postStatus,
                        postIcon, postAttachCount);
        postCreated=true;

        if (parentPost!=null) {
            parentPost.updateAddedReply(this, postMemberName, postCreationDate);
        } else if (parentThread!=null) {
            parentThread.updateAddedPost(this, postMemberName, postCreationDate);
        }
    }

    public void updateAddedReply(MvnForumPostXML subPost, String postUsername, String postCreationDate) {
        if ((!postCreated) || (postXML.getPostID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        //if (subPost.getParentPostID()!=0) {//reply to a post in thread, so we increase the ThreadReplyCount
        //    threadXML.increaseReplyCount();
        //}
        //threadXML.updateLastPostMemberName(postUsername);
        //threadXML.updateLastPostDate(postCreationDate);
        if (parentPost!=null) {
            parentPost.updateAddedReply(subPost, postUsername, postCreationDate);
        } else if (parentThread!=null) {
            parentThread.updateAddedPost(subPost, postUsername, postCreationDate);
        }
    }

    public void updateAddedAttachment(MvnForumAttachmentXML subAttachment) {
        if ((!postCreated) || (postXML.getPostID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        // //check what is needed to be updated
        if (parentPost != null) {
            parentPost.updateAddedAttachment(subAttachment);
        } else if (parentThread != null) {
            parentThread.updateAddedAttachment(subAttachment);
        }
    }


}
