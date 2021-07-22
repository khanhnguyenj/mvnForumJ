/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/ActiveThread.java,v 1.15 2007/10/26 10:25:32 phuongpdd Exp $
 * $Author: phuongpdd $
 * $Revision: 1.15 $
 * $Date: 2007/10/26 10:25:32 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.common;

import java.sql.Timestamp;

import net.myvietnam.mvncore.util.StringUtil;

public class ActiveThread {

    private int threadID;

    private String threadTopic;

    private int forumID;

    private int viewCount;

    private int replyCount;

    private String author;

    private String lastMember;

    private int threadType;

    private int threadStatus;

    private String icon;

    private Timestamp lastDate;

    private int lastPostCount;

    private int attachCount;

    private int pollCount;
    
    private int threadPriority;

    private Timestamp threadCreationDate;

    public ActiveThread() {
        lastPostCount = 0;
    }

    public int getThreadID() {
        return this.threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public String getThreadTopic() {
        return this.threadTopic;
    }
    public void setThreadTopic(String threadTopic) {
        this.threadTopic = threadTopic;
    }

    public int getLastPostCount() {
        return this.lastPostCount;
    }
    public void setLastPostCount(int postCount) {
        this.lastPostCount = postCount;
    }

    public int getForumID() {
        return this.forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getLastDate() {
        return this.lastDate;
    }
    public void setLastDate(Timestamp lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastMember() {
        return this.lastMember;
    }
    public void setLastMember(String lastMember) {
        this.lastMember = lastMember;
    }

    public int getThreadType() {
        return threadType;
    }
    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }

    public int getThreadStatus() {
        return threadStatus;
    }
    public void setThreadStatus(int threadStatus) {
        this.threadStatus = threadStatus;
    }

    public int getReplyCount() {
        return this.replyCount;
    }
    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getViewCount() {
        return this.viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = StringUtil.getEmptyStringIfNull(icon);
    }

    public int getAttachCount() {
        return this.attachCount;
    }
    public void setAttachCount(int attachCount) {
        this.attachCount = attachCount;
    }

    public int getPollCount() {
        return this.pollCount;
    }
    public void setPollCount(int pollCount) {
        this.pollCount = pollCount;
    }

    public int getThreadPriority() {
        return threadPriority;
    }
    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public Timestamp getThreadCreationDate() {
        return threadCreationDate;
    }
    public void setThreadCreationDate(Timestamp threadCreationDate) {
        this.threadCreationDate = threadCreationDate;
    }
}
