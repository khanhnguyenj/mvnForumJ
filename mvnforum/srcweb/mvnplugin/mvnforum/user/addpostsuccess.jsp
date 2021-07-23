<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addpostsuccess.jsp,v 1.74 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.74 $
 - $Date: 2009/07/16 03:21:13 $
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
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.db.PostBean" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
PostBean postBean = (PostBean)request.getAttribute("PostBean");
String forumID  = ParamUtil.getAttribute(request, "ForumID");
String threadID = ParamUtil.getAttribute(request, "ThreadID");
int postID = postBean.getPostID();
String offset   = "0";// we cannot know the offset, so set it to 0 (first page)
boolean attachMore = ((Boolean)request.getAttribute("AttachMore")).booleanValue();
boolean uploadApplet = false;
boolean addPoll = false;
if ((environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) && (request.getAttribute("UploadApplet") != null)) {
    uploadApplet = ((Boolean)request.getAttribute("UploadApplet")).booleanValue();
}
if ((environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) && (request.getAttribute("AddPoll") != null)) {
    addPoll = ((Boolean)request.getAttribute("AddPoll")).booleanValue();
}
boolean addFavoriteThread = ((Boolean)request.getAttribute("AddFavoriteParentThread")).booleanValue();
boolean addWatchThread = ((Boolean)request.getAttribute("AddWatchParentThread")).booleanValue();
boolean isPendingThread = ((Boolean)request.getAttribute("IsPendingThread")).booleanValue();

String viewthreadLink = "viewthread?thread=" + threadID + "&amp;lastpage=yes#" + postID;
String viewthreadDesc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_new_post");
String attachLink = "addattachment?post=" + postID + "&offset=" + offset;
String uploadAppletLink = "uploadimage?post=" + postID + "&offset=" + offset;
String addPollLink = "addthreadpoll?thread=" + threadID;
String defaultLink = viewthreadLink;
if (attachMore) {
    defaultLink = attachLink;
} else if (uploadApplet) {
    defaultLink = uploadAppletLink;
} else if (addPoll) {
    defaultLink = addPollLink;
}
boolean goAutomatically = ( (postBean.getPostStatus()!=PostBean.POST_STATUS_DISABLED) && (isPendingThread==false) );
if (goAutomatically == false) {
    viewthreadLink = "viewthread?thread=" + threadID;
    viewthreadDesc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_current_thread");
} 
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.addpostsuccess.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<% if (goAutomatically) { %>
<meta http-equiv="refresh" content="3; url=<%=urlResolver.encodeURL(request, response, defaultLink)%>"/>
<% } %>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>
<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>
<br/>

<% if (goAutomatically == false) { %>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.addpostsuccess.moderation_waiting"/>
</div>
<br/>
<% } %>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.success.prompt"/></td>
  </tr>
<% if (attachMore) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, attachLink)%>"><fmt:message key="mvnforum.user.success.go_attach_file"/></a> <% if (goAutomatically) { %>(<fmt:message key="mvnforum.common.success.automatic"/>)<% } %></td>
  </tr>
<% } else if (uploadApplet) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, uploadAppletLink)%>"><fmt:message key="mvnforum.user.success.go_uploadApplet_file"/></a> <% if (goAutomatically) { %>(<fmt:message key="mvnforum.common.success.automatic"/>)<% } %></td>
  </tr>
<% } else if (addPoll) { %> 
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, addPollLink)%>"><fmt:message key="mvnforum.user.success.go_addpoll"/></a> <% if (goAutomatically) { %>(<fmt:message key="mvnforum.common.success.automatic"/>)<% } %></td>
  </tr>
<% }%>
<% if (isPendingThread == false) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, viewthreadLink)%>"><%=viewthreadDesc%></a><% if ((attachMore == false) && (uploadApplet == false) && (addPoll == false)) { %> (<fmt:message key="mvnforum.common.success.automatic"/>)<%}%></td>
  </tr>
<% } %>
<% if (addFavoriteThread) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "myfavoritethread")%>"><fmt:message key="mvnforum.user.success.go_myfavoritethread"/></a></td>
  </tr>
<% } %>
<% if (addWatchThread) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "mywatch")%>"><fmt:message key="mvnforum.user.success.go_mywatch"/></a></td>
  </tr>
<% } %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + request.getAttribute("ForumID"))%>"><fmt:message key="mvnforum.user.success.go_current_forum"/></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.user.success.go_index"/></a></td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>