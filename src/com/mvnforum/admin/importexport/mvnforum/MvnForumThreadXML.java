/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumThreadXML.java,v 1.12 2009/01/06 18:31:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
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

import com.mvnforum.admin.ThreadXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.12 $, $Date: 2009/01/06 18:31:31 $
 * <br/>
 * <code>MvnForumThreadXML</code> class encapsulates processing of
 * threads' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>ThreadXML</code> and other neccessary classes in order to perform
 * the actual creation of a thread, as well as other related items (like
 * favorite threads and thread watches).
 */
public class MvnForumThreadXML {

    private ThreadXML threadXML = null;
    private boolean threadCreated = false;
    private MvnForumForumXML parentForum = null;

    String threadMemberName          = null;
    String threadLastPostMemberName  = null;
    String threadTopic               = null;
    String threadBody                = null;
    String threadVoteCount           = null;
    String threadVoteTotalStars      = null;
    String threadCreationDate        = null;
    String threadLastPostDate        = null;
    String threadType                = null;
    String threadPriority            = null;
    String threadOption              = null;
    String threadStatus              = null;
    String threadHasPoll             = null;
    String threadViewCount           = null;
    String threadReplyCount          = null;
    String threadIcon                = null;
    String threadDuration            = null;
    String threadAttachCount         = null;

    public MvnForumThreadXML() {
        threadXML = new ThreadXML();
        threadCreated = false;
        parentForum = null;
    }

    public int getThreadID() {
        return threadXML.getThreadID();
    }

    public void setThreadID(String id) {
        threadXML.setThreadID(id);
    }

    /**
     * This method simply calls <code>setThreadID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setThreadID()</code> as a setter
     * method for <code>threadID</code> property).
     */
    public void setThreadId(String id) {
        setThreadID(id);
    }

    public int getParentCategoryID() {
        return threadXML.getParentCategoryID(); //==parentForum.getParentCategoryID();
    }

    public int getParentForumID() {
        return threadXML.getParentForumID(); //==parentForum.getForumID();
    }

    public void setParentForum(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof MvnForumForumXML) {
            parentForum=(MvnForumForumXML)o;
            //warning: parent forum might be not added to database yet
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent forum.");
        }
    }

    public void setThreadMemberName(String value) {
        threadMemberName=value;
    }

    public void setThreadLastPostMemberName(String value) {
        threadLastPostMemberName=value;
    }

    public void setThreadTopic(String value) {
        threadTopic=value;
    }

    public void setThreadBody(String value) {
        threadBody=value;
    }

    public void setThreadVoteCount(String value) {
        threadVoteCount=value;
    }

    public void setThreadVoteTotalStars(String value) {
        threadVoteTotalStars=value;
    }

    public void setThreadCreationDate(String value) {
        threadCreationDate=value;
    }

    public void setThreadLastPostDate(String value) {
        threadLastPostDate=value;
    }

    public void setThreadType(String value) {
        threadType=value;
    }

    public void setThreadOption(String value) {
        threadOption=value;
    }

    public void setThreadStatus(String value) {
        threadStatus=value;
    }

    public void setThreadHasPoll(String value) {
        threadHasPoll=value;
    }

    public void setThreadViewCount(String value) {
        threadViewCount=value;
    }

    public void setThreadReplyCount(String value) {
        threadReplyCount=value;
    }

    public void setThreadIcon(String value) {
        threadIcon=value;
    }

    public void setThreadDuration(String value) {
        threadDuration=value;
    }

    public void setThreadAttachCount(String value) {
        threadAttachCount=value;
    }

    public String getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(String threadPriority) {
        this.threadPriority = threadPriority;
    }

    public void addThread() throws CreateException, DuplicateKeyException,
    ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException, BadInputException {
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this thread has
         * subelements that need it already be defined, so they first call
         * this method to create thread before creating data that refer him.
         */
        if (threadCreated) {
            return;
        }
        /* Second, create parent forum if it's not yet created. */
        if (parentForum != null) {
            parentForum.addForum();
            threadXML.setParentForumID(parentForum.getForumID());
            threadXML.setParentCategoryID(parentForum.getParentCategoryID());
        }

        ImportMvnForum.addMessage("Adding thread \""+threadTopic+"\".");
        threadXML.addThread(threadMemberName, threadLastPostMemberName,
                            threadTopic, threadBody,
                            threadVoteCount, threadVoteTotalStars,
                            threadCreationDate, threadLastPostDate,
                            threadType, threadPriority, threadOption,
                            threadStatus, threadHasPoll,
                            threadViewCount, threadReplyCount,
                            threadIcon, threadDuration, threadAttachCount);
        threadCreated = true;

        if (parentForum != null) {
            parentForum.updateAddedThread(this);
        }
    }

    public void addThreadWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!threadCreated) || (threadXML.getThreadID()<0) ) {
            addThread();
        }
        ImportMvnForum.addMessage("Adding thread watch for member \"" + memberName + "\".");
        threadXML.addThreadWatch(memberName,
                  watchType, watchOption, watchStatus,
                  watchCreationDate, watchLastSentDate, watchEndDate);
    }

    public void addFavoriteThread(String memberName,
                String favoriteCreationDate, String favoriteType,
                String favoriteOption, String favoriteStatus)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!threadCreated) || (threadXML.getThreadID()<0) ) {
            addThread();
        }
        ImportMvnForum.addMessage("Adding favorite-thread for member \"" + memberName + "\".");
        threadXML.addFavoriteThread(memberName,
                  favoriteCreationDate, favoriteType,
                  favoriteOption, favoriteStatus);
    }

    public void updateAddedPost(MvnForumPostXML subPost, String postUsername, String postCreationDate) {
        if ((!threadCreated) || (threadXML.getThreadID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        //if (subPost.getParentPostID()!=0) {//reply to a post in thread, so we increase the ThreadReplyCount
        //    threadXML.increaseReplyCount();
        //}
        //threadXML.updateLastPostMemberName(postUsername);
        //threadXML.updateLastPostDate(postCreationDate);
        if (parentForum != null) {
            parentForum.updateAddedPost(subPost, postUsername, postCreationDate);
        }
    }

    public void updateAddedAttachment(MvnForumAttachmentXML subAttachment) {
        if ((!threadCreated) || (threadXML.getThreadID()<0)) {
            return; //todo Igor: process this error
        }
        //do nothing; MVN Forum XML already has correct final values for these:
        // //check what is needed to be updated
        if (parentForum != null) {
            parentForum.updateAddedAttachment(subAttachment);
        }
    }
    
}
