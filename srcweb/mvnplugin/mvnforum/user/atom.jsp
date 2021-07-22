<?xml version="1.0" encoding="utf-8" ?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
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
Timestamp now = DateUtil.getCurrentGMTTimestamp();
String modified = DateUtil.getDateISO8601(now);
String link = prefix + "/index";
String title = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.forum.title_name");
title = DisableHtmlTagFilter.filter(title);
String tagLine;
String copyRight = "Copyright by this site owner";
String authorName = title + " Editor";
String authorEmail = ""; //please consider spam when putting email here
//String id = "";
String generator = "mvnForum Feed Generator " + mvnForumInfo.getProductVersion();
if (forumID > 0) {
    // Forum specific ATOM
    ForumCache forumCache = ForumCache.getInstance();
    String forumName = forumCache.getBean(forumID).getForumName();
    forumName = DisableHtmlTagFilter.filter(forumName);
    title = title + " (Forum: " + forumName + ")";
    tagLine = "ATOM Feed of " + title;
} else {
    // global ATOM
    title = title + " (Global ATOM)";
    tagLine = "ATOM Feed of " + title;
}
%>
<feed version="0.3" xmlns="http://purl.org/atom/ns#" xmlns:dc="http://purl.org/dc/elements/1.1/" xml:lang="en">
  <title><%=title%></title>
  <link rel="alternate" type="text/html" href="<%=link%>"/>
  <modified><%=modified%></modified>
  <tagline> <%-- Description --%><%=tagLine%></tagline>
  <copyright><%=copyRight%></copyright>
  <author>
    <name><%=authorName%></name>
    <email></email>
  </author>
  <id><%=prefix%>/rsssummary</id>
  <generator><%=generator%></generator>
<%for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
    ThreadBean threadBean = (ThreadBean)iterator.next(); %>
  <entry>
    <title><%=DisableHtmlTagFilter.filter(threadBean.getThreadTopic())%></title>
    <%
    String threadUrl = "/viewthread?thread=" + threadBean.getThreadID();
    if (MVNForumConfig.getEnableFriendlyURL()) {
      threadUrl = FriendlyURLParamUtil.createFriendlyURL(threadUrl);
    }
    %>
    <link rel="alternate" type="text/html" href="<%=prefix%><%=threadUrl%>"/>
    <id><%=prefix%><%=threadUrl%></id>
    <issued><%=DateUtil.getDateISO8601(threadBean.getThreadCreationDate())%></issued>
    <modified><%=DateUtil.getDateISO8601(threadBean.getThreadLastPostDate())%></modified>
    <content><%=DisableHtmlTagFilter.filter( StringUtil.getShorterString(threadBean.getThreadBody(), MVNForumConfig.getMaxCharsInRSS()) )%></content>
    <author><name><%=DisableHtmlTagFilter.filter(threadBean.getMemberName())%></name></author>
  </entry>
<%}//for%>
</feed>
</fmt:bundle>
