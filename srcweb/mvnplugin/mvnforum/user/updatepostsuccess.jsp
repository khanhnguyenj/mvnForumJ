<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/updatepostsuccess.jsp,v 1.65 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.65 $
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
<%@ page import="com.mvnforum.db.PostBean" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
PostBean postBean = (PostBean)request.getAttribute("PostBean");
String threadID = ParamUtil.getAttribute(request, "ThreadID");
int offset = ParamUtil.getParameterInt(request, "offset", 0);
boolean attachMore = ((Boolean)request.getAttribute("AttachMore")).booleanValue();
boolean isPendingThread = ((Boolean)request.getAttribute("IsPendingThread")).booleanValue();
boolean goAutomatically = ( (postBean.getPostStatus()!=PostBean.POST_STATUS_DISABLED) && (isPendingThread==false) );

boolean uploadApplet = false;
if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE && request.getAttribute("UploadApplet") != null) {
    uploadApplet = ((Boolean)request.getAttribute("UploadApplet")).booleanValue();
}
String url;
String desc;
if (postBean.getPostStatus() == PostBean.POST_STATUS_DEFAULT) {
  if (attachMore) {
    url = "addattachment?post=" + postBean.getPostID() + "&amp;offset=" + offset;
    desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_attach_file");
  } else if (uploadApplet) {
    url = "uploadimage?post=" + postBean.getPostID() + "&amp;offset=" + offset;
    desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_uploadApplet_file");
  } else {
    url = "viewthread?thread=" + threadID + "&amp;offset=" + offset + "#" + postBean.getPostID();
    desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_current_post");
  }
} else if ((postBean.getPostStatus() != PostBean.POST_STATUS_DEFAULT) && (permission.canModerateThread(postBean.getForumID()))) {
  url = "moderatependingposts?thread=" + threadID + "&amp;offset=" + offset + "#" + postBean.getPostID();
  desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_current_posts_moderation");
} else {
  url = "viewthread?thread=" + threadID;
  desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.success.go_current_thread");
}
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.updatepostsuccess.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<% if (isPendingThread == false) { %>
<meta http-equiv="refresh" content="3; url=<%=urlResolver.encodeURL(request, response, url)%>"/>
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
<% if (goAutomatically == false && permission.canModerateThread(postBean.getForumID()) == false) { %>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.updatepostsuccess.moderation_waiting"/>
</div>
<br/>
<% } %>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.success.prompt"/></td>
  </tr>
  <%if (isPendingThread == false) {%>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, url)%>"><%= desc %></a> (<fmt:message key="mvnforum.common.success.automatic"/>)</td>
  </tr>
  <%}%>
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
