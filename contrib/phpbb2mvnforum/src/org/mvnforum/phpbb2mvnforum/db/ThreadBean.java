/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/ThreadBean.java,v 1.6 2009/03/20 03:16:15 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/03/20 03:16:15 $
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
package org.mvnforum.phpbb2mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic,
 *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate,
 *                   ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount,
 *                   ThreadReplyCount, ThreadIcon, ThreadDuration
 * Excluded columns:
 */
public class ThreadBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
    /**
     * The default value mean thread is enable and normal
     */
    public static final int THREAD_STATUS_DEFAULT  = 0;

    /**
     * The disable thread is invisible for normal user and visible to moderator
     */
    public static final int THREAD_STATUS_DISABLED = 1;

    /**
     * No changes (edit, attach, reply) could be maded, moderator
     * have to change this status before making any changes is possible
     */
    public static final int THREAD_STATUS_LOCKED   = 2;

    /**
     * Noone can reply, but moderator can change it.
     */
    public static final int THREAD_STATUS_CLOSED   = 3;

    /**
     * Thread type is traditional
     */
    public static final int THREAD_TYPE_DEFAULT             = 0;

    public static final int THREAD_TYPE_STICKY              = 1;

    public static final int THREAD_TYPE_FORUM_ANNOUNCEMENT  = 2;

    public static final int THREAD_TYPE_GLOBAL_ANNOUNCEMENT = 3;

    private int threadID;
    private int forumID;
    private String memberName;
    private String lastPostMemberName;
    private String threadTopic;
    private String threadBody;
    private int threadVoteCount;
    private int threadVoteTotalStars;
    private Timestamp threadCreationDate;
    private Timestamp threadLastPostDate;
    private int threadType;
    private int threadOption;
    private int threadStatus;
    private int threadHasPoll;
    private int threadViewCount;
    private int threadReplyCount;
    private String threadIcon;
    private int threadDuration;
    private int threadAttachCount;

    public int getThreadID() {
        return threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLastPostMemberName() {
        return lastPostMemberName;
    }
    public void setLastPostMemberName(String lastPostMemberName) {
        this.lastPostMemberName = lastPostMemberName;
    }

    public String getThreadTopic() {
        return threadTopic;
    }
    public void setThreadTopic(String threadTopic) {
        this.threadTopic = threadTopic;
    }

    public String getThreadBody() {
        return threadBody;
    }
    public void setThreadBody(String threadBody) {
        this.threadBody = threadBody;
    }

    public int getThreadVoteCount() {
        return threadVoteCount;
    }
    public void setThreadVoteCount(int threadVoteCount) {
        this.threadVoteCount = threadVoteCount;
    }

    public int getThreadVoteTotalStars() {
        return threadVoteTotalStars;
    }
    public void setThreadVoteTotalStars(int threadVoteTotalStars) {
        this.threadVoteTotalStars = threadVoteTotalStars;
    }

    public Timestamp getThreadCreationDate() {
        return threadCreationDate;
    }
    public void setThreadCreationDate(Timestamp threadCreationDate) {
        this.threadCreationDate = threadCreationDate;
    }

    public Timestamp getThreadLastPostDate() {
        return threadLastPostDate;
    }
    public void setThreadLastPostDate(Timestamp threadLastPostDate) {
        this.threadLastPostDate = threadLastPostDate;
    }

    public int getThreadType() {
        return threadType;
    }
    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }

    public int getThreadOption() {
        return threadOption;
    }
    public void setThreadOption(int threadOption) {
        this.threadOption = threadOption;
    }

    public int getThreadStatus() {
        return threadStatus;
    }
    public void setThreadStatus(int threadStatus) {
        this.threadStatus = threadStatus;
    }

    public int getThreadHasPoll() {
        return threadHasPoll;
    }
    public void setThreadHasPoll(int threadHasPoll) {
        this.threadHasPoll = threadHasPoll;
    }

    public int getThreadViewCount() {
        return threadViewCount;
    }
    public void setThreadViewCount(int threadViewCount) {
        this.threadViewCount = threadViewCount;
    }

    public int getThreadReplyCount() {
        return threadReplyCount;
    }
    public void setThreadReplyCount(int threadReplyCount) {
        this.threadReplyCount = threadReplyCount;
    }

    public String getThreadIcon() {
        return threadIcon;
    }
    public void setThreadIcon(String threadIcon) {
        this.threadIcon = StringUtil.getEmptyStringIfNull(threadIcon);
    }

    public int getThreadDuration() {
        return threadDuration;
    }
    public void setThreadDuration(int threadDuration) {
        this.threadDuration = threadDuration;
    }

    public int getThreadAttachCount() {
        return this.threadAttachCount;
    }
    public void setThreadAttachCount(int attachCount) {
        this.threadAttachCount = attachCount;
    }

    public String getXMLTag() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<Thread");
        xml.append(" threadID=\"").append(String.valueOf(threadID)).append("\"");
        xml.append(" forumID=\"").append(String.valueOf(forumID)).append("\"");
        xml.append(" memberName=\"").append(String.valueOf(memberName)).append("\"");
        xml.append(" lastPostMemberName=\"").append(String.valueOf(lastPostMemberName)).append("\"");
        xml.append(" threadTopic=\"").append(String.valueOf(threadTopic)).append("\"");
        xml.append(" threadBody=\"").append(String.valueOf(threadBody)).append("\"");
        xml.append(" threadVoteCount=\"").append(String.valueOf(threadVoteCount)).append("\"");
        xml.append(" threadVoteTotalStars=\"").append(String.valueOf(threadVoteTotalStars)).append("\"");
        xml.append(" threadCreationDate=\"").append(String.valueOf(threadCreationDate)).append("\"");
        xml.append(" threadLastPostDate=\"").append(String.valueOf(threadLastPostDate)).append("\"");
        xml.append(" threadType=\"").append(String.valueOf(threadType)).append("\"");
        xml.append(" threadOption=\"").append(String.valueOf(threadOption)).append("\"");
        xml.append(" threadStatus=\"").append(String.valueOf(threadStatus)).append("\"");
        xml.append(" threadHasPoll=\"").append(String.valueOf(threadHasPoll)).append("\"");
        xml.append(" threadViewCount=\"").append(String.valueOf(threadViewCount)).append("\"");
        xml.append(" threadReplyCount=\"").append(String.valueOf(threadReplyCount)).append("\"");
        xml.append(" threadIcon=\"").append(String.valueOf(threadIcon)).append("\"");
        xml.append(" threadDuration=\"").append(String.valueOf(threadDuration)).append("\"");
        xml.append(" threadAttachCount=\"").append(String.valueOf(threadAttachCount)).append("\"");
        xml.append(">");
        return xml.toString();
    }
    
    public void getBeanDocument(Document doc, Element element) {
        Element category = doc.createElement("Thread");
        element.appendChild(category);
        
        category.appendChild(getNode(doc, "ThreadID", String.valueOf(threadID)));
        category.appendChild(getNode(doc, "ForumID", String.valueOf(forumID)));
        category.appendChild(getNode(doc, "MemberName", String.valueOf(memberName)));
        category.appendChild(getNode(doc, "LastPostMemberName", String.valueOf(lastPostMemberName)));
        category.appendChild(getNode(doc, "ThreadTopic", String.valueOf(threadTopic)));
        category.appendChild(getNode(doc, "ThreadBody", String.valueOf(threadBody)));
        category.appendChild(getNode(doc, "ThreadVoteCount", String.valueOf(threadVoteCount)));
        category.appendChild(getNode(doc, "ThreadVoteTotalStars", String.valueOf(threadVoteTotalStars)));
        category.appendChild(getNode(doc, "ThreadCreationDate", String.valueOf(threadCreationDate)));
        category.appendChild(getNode(doc, "ThreadLastPostDate", String.valueOf(threadLastPostDate)));
        category.appendChild(getNode(doc, "ThreadType", String.valueOf(threadType)));
        category.appendChild(getNode(doc, "ThreadOption", String.valueOf(threadOption)));
        category.appendChild(getNode(doc, "ThreadStatus", String.valueOf(threadStatus)));
        category.appendChild(getNode(doc, "ThreadHasPoll", String.valueOf(threadHasPoll)));
        category.appendChild(getNode(doc, "ThreadViewCount", String.valueOf(threadViewCount)));
        category.appendChild(getNode(doc, "ThreadReplyCount", String.valueOf(threadReplyCount)));
        category.appendChild(getNode(doc, "ThreadIcon", String.valueOf(threadIcon)));
        category.appendChild(getNode(doc, "ThreadDuration", String.valueOf(threadDuration)));
        category.appendChild(getNode(doc, "ThreadAttachCount", String.valueOf(threadAttachCount)));
    }
    
    public static Node getNode (Document doc, String childName, String childValue ) {
        Element child = doc.createElement(childName);
        child.appendChild(doc.createTextNode(childValue));
        return child;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<ThreadSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ForumID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forumID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>MemberName</Name>\n");
        xml.append("        <Value>").append(String.valueOf(memberName)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>LastPostMemberName</Name>\n");
        xml.append("        <Value>").append(String.valueOf(lastPostMemberName)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadTopic</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadTopic)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadBody</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadBody)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadVoteCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadVoteCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadVoteTotalStars</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadVoteTotalStars)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadCreationDate</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadCreationDate)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadLastPostDate</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadLastPostDate)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadType</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadType)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadOption</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadOption)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadStatus</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadStatus)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadHasPoll</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadHasPoll)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadViewCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadViewCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadReplyCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadReplyCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadIcon</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadIcon)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadDuration</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadDuration)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadAttachCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadAttachCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</ThreadSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objThreadBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objThreadBeans.iterator();
        xml.append("<ThreadSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            ThreadBean objThreadBean = (ThreadBean)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ForumID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.forumID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>MemberName</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.memberName)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>LastPostMemberName</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.lastPostMemberName)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadTopic</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadTopic)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadBody</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadBody)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadVoteCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadVoteCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadVoteTotalStars</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadVoteTotalStars)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadCreationDate</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadCreationDate)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadLastPostDate</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadLastPostDate)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadType</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadType)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadOption</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadOption)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadStatus</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadStatus)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadHasPoll</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadHasPoll)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadViewCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadViewCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadReplyCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadReplyCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadIcon</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadIcon)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadDuration</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadDuration)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadAttachCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objThreadBean.threadAttachCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</ThreadSection>\n");
        return xml.toString();
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private int threadPendingPostCount;

    public int getThreadPendingPostCount() {
        return threadPendingPostCount;
    }
    public void setThreadPendingPostCount(int threadPendingPostCount) {
        this.threadPendingPostCount = threadPendingPostCount;
    }

    private Collection pendingPosts;

    public Collection getPendingPosts() {
        return pendingPosts;
    }
    public void setPendingPosts(Collection pendingPosts) {
        this.pendingPosts = pendingPosts;
    }

    static public void validateThreadStatus(int status) throws IllegalArgumentException {
        if ((status < 0) || (status > THREAD_STATUS_CLOSED)) {
            throw new IllegalArgumentException("Invalid ThreadStatus = " + status);
        }
    }

    static public void validateThreadType(int type) throws IllegalArgumentException {
        if ((type < 0) || (type > THREAD_TYPE_GLOBAL_ANNOUNCEMENT)) {
            throw new IllegalArgumentException("Invalid ThreadType = " + type);
        }
    }

    public void ensureStatusCanReply() throws IllegalArgumentException {
        if ( (getThreadStatus() == ThreadBean.THREAD_STATUS_LOCKED) ||
            (getThreadStatus() == ThreadBean.THREAD_STATUS_CLOSED)) {
            // or we can throw AssertionError. indicate that the gui MUST hide the option
            //@todo : localize me
            throw new IllegalStateException("Cannot reply when the thread is closed or locked.");
        }
    }

    public void ensureStatusCanEdit() throws IllegalArgumentException {
        if (getThreadStatus() == ThreadBean.THREAD_STATUS_LOCKED) {
            // or we can throw AssertionError. indicate that the gui MUST hide the option
            //@todo : localize me
            throw new IllegalStateException("Cannot edit post when the thread is locked.");
        }
    }

} //end of class ThreadBean
