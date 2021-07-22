/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PostBean.java,v 1.13 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/01/02 18:18:50 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: PostID, ParentPostID, ForumID, ThreadID, MemberID,
 *                   MemberName, LastEditMemberName, PostTopic, PostBody, PostCreationDate,
 *                   PostLastEditDate, PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption,
 *                   PostOption, PostStatus, PostIcon, PostAttachCount
 * Excluded columns:
 */
public class PostBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
    /**
     * The default value mean post is enable and normal
     */
    public static final int POST_STATUS_DEFAULT = 0;

    /**
     * The disable post is invisible for normal user and visible to moderator
     */
    public static final int POST_STATUS_DISABLED = 1;

    private int postID;
    private int parentPostID;
    private int forumID;
    private int threadID;
    private int memberID;
    private String memberName;
    private String lastEditMemberName;
    private String postTopic;
    private String postBody;
    private Timestamp postCreationDate;
    private Timestamp postLastEditDate;
    private String postCreationIP;
    private String postLastEditIP;
    private int postEditCount;
    private int postFormatOption;
    private int postOption;
    private int postStatus;
    private String postIcon;
    private int postAttachCount;
    
    public int getPostID() {
        return postID;
    }
    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getParentPostID() {
        return parentPostID;
    }
    public void setParentPostID(int parentPostID) {
        this.parentPostID = parentPostID;
    }

    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public int getThreadID() {
        return threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLastEditMemberName() {
        return lastEditMemberName;
    }
    public void setLastEditMemberName(String lastEditMemberName) {
        this.lastEditMemberName = StringUtil.getEmptyStringIfNull(lastEditMemberName);
    }

    public String getPostTopic() {
        return postTopic;
    }
    public void setPostTopic(String postTopic) {
        this.postTopic = postTopic;
    }

    public String getPostBody() {
        return postBody;
    }
    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public Timestamp getPostCreationDate() {
        return postCreationDate;
    }
    public void setPostCreationDate(Timestamp postCreationDate) {
        this.postCreationDate = postCreationDate;
    }

    public Timestamp getPostLastEditDate() {
        return postLastEditDate;
    }
    public void setPostLastEditDate(Timestamp postLastEditDate) {
        this.postLastEditDate = postLastEditDate;
    }

    public String getPostCreationIP() {
        return postCreationIP;
    }
    public void setPostCreationIP(String postCreationIP) {
        this.postCreationIP = postCreationIP;
    }

    public String getPostLastEditIP() {
        return postLastEditIP;
    }
    public void setPostLastEditIP(String postLastEditIP) {
        this.postLastEditIP = StringUtil.getEmptyStringIfNull(postLastEditIP);
    }

    public int getPostEditCount() {
        return postEditCount;
    }
    public void setPostEditCount(int postEditCount) {
        this.postEditCount = postEditCount;
    }

    public int getPostFormatOption() {
        return postFormatOption;
    }
    public void setPostFormatOption(int postFormatOption) {
        this.postFormatOption = postFormatOption;
    }

    public int getPostOption() {
        return postOption;
    }
    public void setPostOption(int postOption) {
        this.postOption = postOption;
    }

    public int getPostStatus() {
        return postStatus;
    }
    public void setPostStatus(int postStatus) {
        this.postStatus = postStatus;
    }

    public String getPostIcon() {
        return postIcon;
    }
    public void setPostIcon(String postIcon) {
        this.postIcon = StringUtil.getEmptyStringIfNull(postIcon);
    }

    public int getPostAttachCount() {
        return postAttachCount;
    }
    public void setPostAttachCount(int postAttachCount) {
        this.postAttachCount = postAttachCount;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private MemberBean memberBean = null;
    private Collection attachmentBeans = null;
    private int postLevel;

    public MemberBean getMemberBean() {
        return memberBean;
    }
    public void setMemberBean(MemberBean memberBean) {
        this.memberBean = memberBean;
    }

    public Collection getAttachmentBeans() {
        return attachmentBeans;
    }
    public void setAttachmentBeans(Collection attachmentBeans) {
        this.attachmentBeans = attachmentBeans;
    }
    
    public int getPostLevel() {
        return postLevel;
    }
    public void setPostLevel(int postLevel) {
        this.postLevel = postLevel;
    }

} //end of class PostBean
