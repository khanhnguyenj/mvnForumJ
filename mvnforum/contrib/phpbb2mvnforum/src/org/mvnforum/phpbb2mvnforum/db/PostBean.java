/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PostBean.java,v 1.4 2009/03/20 03:16:15 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
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

    public String getXMLTag() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<Post");
        xml.append(" postID=\"").append(String.valueOf(postID)).append("\"");
        xml.append(" parentPostID=\"").append(String.valueOf(parentPostID)).append("\"");
        xml.append(" forumID=\"").append(String.valueOf(forumID)).append("\"");
        xml.append(" threadID=\"").append(String.valueOf(threadID)).append("\"");
        xml.append(" memberID=\"").append(String.valueOf(memberID)).append("\"");
        xml.append(" memberName=\"").append(String.valueOf(memberName)).append("\"");
        xml.append(" lastEditMemberName=\"").append(String.valueOf(lastEditMemberName)).append("\"");
        xml.append(" postTopic=\"").append(String.valueOf(postTopic)).append("\"");
        xml.append(" postBody=\"").append(String.valueOf(postBody)).append("\"");
        xml.append(" postCreationDate=\"").append(String.valueOf(postCreationDate)).append("\"");
        xml.append(" postLastEditDate=\"").append(String.valueOf(postLastEditDate)).append("\"");
        xml.append(" postCreationIP=\"").append(String.valueOf(postCreationIP)).append("\"");
        xml.append(" postLastEditIP=\"").append(String.valueOf(postLastEditIP)).append("\"");
        xml.append(" postEditCount=\"").append(String.valueOf(postEditCount)).append("\"");
        xml.append(" postFormatOption=\"").append(String.valueOf(postFormatOption)).append("\"");
        xml.append(" postOption=\"").append(String.valueOf(postOption)).append("\"");
        xml.append(" postStatus=\"").append(String.valueOf(postStatus)).append("\"");
        xml.append(" postIcon=\"").append(String.valueOf(postIcon)).append("\"");
        xml.append(" postAttachCount=\"").append(String.valueOf(postAttachCount)).append("\"");
        xml.append(">");
        return xml.toString();
    }
    
    public void getBeanDocument(Document doc, Element element) {
        Element category = doc.createElement("Category");
        element.appendChild(category);
        
        category.appendChild(getNode(doc, "PostID", String.valueOf(postID)));
        category.appendChild(getNode(doc, "ParentPostID", String.valueOf(parentPostID)));
        category.appendChild(getNode(doc, "ForumID", String.valueOf(forumID)));
        category.appendChild(getNode(doc, "ThreadID", String.valueOf(threadID)));
        category.appendChild(getNode(doc, "MemberID", String.valueOf(memberID)));
        category.appendChild(getNode(doc, "MemberName", String.valueOf(memberName)));
        category.appendChild(getNode(doc, "LastEditMemberName", String.valueOf(lastEditMemberName)));
        category.appendChild(getNode(doc, "PostTopic", String.valueOf(postTopic)));
        category.appendChild(getNode(doc, "PostBody", String.valueOf(postBody)));
        category.appendChild(getNode(doc, "PostCreationDate", String.valueOf(postCreationDate)));
        category.appendChild(getNode(doc, "PostLastEditDate", String.valueOf(postLastEditDate)));
        category.appendChild(getNode(doc, "PostCreationIP", String.valueOf(postCreationIP)));
        category.appendChild(getNode(doc, "PostLastEditIP", String.valueOf(postLastEditIP)));
        category.appendChild(getNode(doc, "PostEditCount", String.valueOf(postEditCount)));
        category.appendChild(getNode(doc, "PostFormatOption", String.valueOf(postFormatOption)));
        category.appendChild(getNode(doc, "PostOption", String.valueOf(postOption)));
        category.appendChild(getNode(doc, "PostStatus", String.valueOf(postStatus)));
        category.appendChild(getNode(doc, "PostIcon", String.valueOf(postIcon)));
        category.appendChild(getNode(doc, "PostAttachCount", String.valueOf(postAttachCount)));
    }
    
    public static Node getNode (Document doc, String childName, String childValue ) {
        Element child = doc.createElement(childName);
        child.appendChild(doc.createTextNode(childValue));
        return child;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<PostSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ParentPostID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(parentPostID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ForumID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forumID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>ThreadID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(threadID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>MemberID</Name>\n");
        xml.append("        <Value>").append(String.valueOf(memberID)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>MemberName</Name>\n");
        xml.append("        <Value>").append(String.valueOf(memberName)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>LastEditMemberName</Name>\n");
        xml.append("        <Value>").append(String.valueOf(lastEditMemberName)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostTopic</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postTopic)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostBody</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postBody)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostCreationDate</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postCreationDate)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostLastEditDate</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postLastEditDate)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostCreationIP</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postCreationIP)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostLastEditIP</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postLastEditIP)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostEditCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postEditCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostFormatOption</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postFormatOption)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostOption</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postOption)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostStatus</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postStatus)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostIcon</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postIcon)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>PostAttachCount</Name>\n");
        xml.append("        <Value>").append(String.valueOf(postAttachCount)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</PostSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objPostBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objPostBeans.iterator();
        xml.append("<PostSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PostBean objPostBean = (PostBean)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ParentPostID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.parentPostID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ForumID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.forumID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>ThreadID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.threadID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>MemberID</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.memberID)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>MemberName</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.memberName)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>LastEditMemberName</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.lastEditMemberName)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostTopic</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postTopic)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostBody</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postBody)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostCreationDate</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postCreationDate)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostLastEditDate</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postLastEditDate)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostCreationIP</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postCreationIP)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostLastEditIP</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postLastEditIP)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostEditCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postEditCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostFormatOption</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postFormatOption)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostOption</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postOption)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostStatus</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postStatus)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostIcon</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postIcon)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>PostAttachCount</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objPostBean.postAttachCount)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</PostSection>\n");
        return xml.toString();
    }
    /************************************************
     * Customized methods come below
     ************************************************/
    private MemberBean memberBean = null;
    private Collection attachmentBeans = null;

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

} //end of class PostBean
