<?xml version="1.0" encoding="utf-8" ?>
<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/rss2.jsp,v 1.19 2009/03/13 01:43:04 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.19 $
 - $Date: 2009/03/13 01:43:04 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2007 by MyVietnam.net
 -
 - All copyright notices regarding mvnForum MUST remain 
 - intact in the scripts and in the outputted HTML.
 - The "powered by" text/logo with a link back to
 - http://www.mvnForum.com and http://www.MyVietnam.net in 
 - the footer of the pages MUST remain visible when the pages
 - are viewed on the internet or intranet.
 -
 - This program is free software; you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation; either version 2 of the License, or
 - any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program; if not, write to the Free Software
 - Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -
 - Support can be obtained from support forums at:
 - http://www.mvnForum.com/mvnforum/index
 -
 - Correspondence and Marketing Questions can be sent to:
 - info at MyVietnam net
 -
 - @author: Minh Nguyen  
 - @author: Mai  Nguyen  
 --%>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="net.myvietnam.mvncore.util.*" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.user.UserModuleConfig" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<% request.setAttribute("contentType", "text/xml;charset=utf-8");%>
<%@ include file="inc_common.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<% response.setContentType("text/xml;charset=utf-8");%>
<%
Collection threadBeans = (Collection) request.getAttribute("ThreadBeans");
int forumID = ((Integer)request.getAttribute("ForumID")).intValue();
String prefix = ParamUtil.getServerPath() + contextPath + UserModuleConfig.getUrlPattern();
String logoUrl = ParamUtil.getServerPath() + contextPath + "/mvnplugin/mvnforum/images/logo.gif";

String channelLink = prefix + "/index";
String channelTitle = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.forum.title_name");
channelTitle = DisableHtmlTagFilter.filter(channelTitle);
String channelDesc;
Timestamp now = DateUtil.getCurrentGMTTimestamp();
String channelPubDate = DateUtil.getDateRFC822(now);
String channelLastBuildDate = channelPubDate;
String channelDocs = prefix + "/rsssummary";
String channelGenerator = "mvnForum Feed Generator " + mvnForumInfo.getProductVersion();
ForumCache forumCache = ForumCache.getInstance();
if (forumID > 0) {
    // Forum specific RSS
    String forumName = forumCache.getBean(forumID).getForumName();
    forumName = DisableHtmlTagFilter.filter(forumName);
    channelTitle = channelTitle + " (Forum: " + forumName + ")";
    channelDesc  = "RSS Feed of " + channelTitle;
} else {
    // global RSS
    channelTitle = channelTitle;
    channelDesc  = "RSS Feed of " + channelTitle + " (Global RSS)";
}
%>
<rss version="2.0">
<channel>
  <title><%=channelTitle%></title>
  <link><%=prefix%>/index</link>
  <description><%=channelDesc%></description>
  <language>en-us</language>
<%--  <pubDate><%=channelPubDate%></pubDate> --%>
  <lastBuildDate><%=channelLastBuildDate%></lastBuildDate>
  <docs><%=channelDocs%></docs>
  <generator><%=channelGenerator%></generator>
  <image>
    <title><fmt:message key="mvnforum.common.forum.title_name"/></title>
    <url><%=logoUrl%></url>
    <link><%=prefix%>/index</link>
    <width>141</width>
    <height>50</height>
    <description>mvnForum - free open source Jsp/Servlet forum</description>
  </image>

  <textInput>
    <title>Search</title>
    <description>Search all posts</description>
    <name>key</name>
    <link><%=prefix%>/search</link>
  </textInput>
<%
for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
  ThreadBean threadBean = (ThreadBean)iterator.next(); %>
  <item>
    <title><%=DisableHtmlTagFilter.filter(threadBean.getThreadTopic())%></title>
    <%
    String threadUrl = "/viewthread?thread=" + threadBean.getThreadID();
    String threadUrlWithLastPage = "/viewthread?thread=" + threadBean.getThreadID() + "&amp;lastpage=yes";
    if (MVNForumConfig.getEnableFriendlyURL()) {
      threadUrl = FriendlyURLParamUtil.createFriendlyURL(threadUrl);
      threadUrlWithLastPage = FriendlyURLParamUtil.createFriendlyURL(threadUrlWithLastPage);
    }
    %>
    <link><%=prefix%><%=threadUrl%></link>
    <description><%=DisableHtmlTagFilter.filter( StringUtil.getShorterString(threadBean.getThreadBody(), MVNForumConfig.getMaxCharsInRSS()) )%></description>
    <%-- author><%=threadBean.getMemberName()%></author --%>
    <pubDate><%=DateUtil.getDateRFC822(threadBean.getThreadCreationDate())%></pubDate>
    <category><%=DisableHtmlTagFilter.filter( forumCache.getBean(threadBean.getForumID()).getForumName() )%></category>
    
    <guid><%=prefix%><%=threadUrlWithLastPage%></guid>
  </item>
<% }//for%>
</channel>
</rss>
</fmt:bundle>

