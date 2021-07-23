<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/listlogfiles.jsp,v 1.45 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.45 $
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.io.File" %>
<%@ page import="net.myvietnam.mvncore.util.FileUtil" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.listlogfiles.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "viewlogsystem")%>"><fmt:message key="mvnforum.admin.viewlogsystem.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.listlogfiles.title"/>
</div>

<br/>
<%
File[] logFiles = (File[])request.getAttribute("LogFiles");
File currentLogFile = (File)request.getAttribute("CurrentLogFile");
%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.listlogfiles.archived_log_files"/>:</td>
    <td><fmt:message key="mvnforum.common.file.file_size"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.view"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.download"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<mvn:cssrows>
<% 
   for (int i = 0; i < logFiles.length; i++) {
       boolean isCurrent = false;
       if (currentLogFile.equals(logFiles[i])) isCurrent = true;
%>
  <tr class="<mvn:cssrow/>">
    <td>
    <%if (isCurrent) {%>
      <b><%=logFiles[i].getName()%> (Current File)</b>
    <%} else {%>
      <%=logFiles[i].getName()%>
    <%}%>
    </td>
    <td><%=FileUtil.getHumanSize(logFiles[i].length())%></td>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "viewlogsystem?filename=" + logFiles[i].getName())%>" class="command"><fmt:message key="mvnforum.common.action.view"/></a>
    </td>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "downloadlogfile?filename=" + logFiles[i].getName() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.download"/></a>
    </td>
    <td align="center">
    <%if (isCurrent) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {%>
      <a href="<%=urlResolver.encodeURL(request, response, "deletelogfile?filename=" + logFiles[i].getName() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a>
    <%}%>
    </td>
  </tr>
<% } //for%>
<% if (logFiles.length == 0) { %>
  <tr class="<mvn:cssrow/>"><td><fmt:message key="mvnforum.admin.listlogfiles.table.no_archived_log_files"/></td></tr>
<% } %>
</mvn:cssrows>
</table>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>