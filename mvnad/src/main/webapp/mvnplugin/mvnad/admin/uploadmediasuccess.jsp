<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/uploadmediasuccess.jsp,v 1.6 2009/07/16 03:29:35 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.6 $
 - $Date: 2009/07/16 03:29:35 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2008 by MyVietnam.net
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
 - @author: MyVietnam.net developers
 -
 --%>
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
String fromPage = (String) request.getAttribute("frompage");
if ("addbanner".equals(fromPage) == false) {
  fromPage = "managemedia";
}
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.uploadmediasuccess.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<meta http-equiv="refresh" content="3; url=<%=urlResolver.encodeURL(request, response, fromPage)%>"/>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <%if ("addbanner".equals(fromPage)) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "listbanners")%>"><fmt:message key="mvnad.admin.listbanners.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "addbanner")%>"><fmt:message key="mvnad.admin.addbanner.title"/></a>&nbsp;&raquo;&nbsp;
  <%} else {%>
  <a href="<%=urlResolver.encodeURL(request, response, "managemedia")%>"><fmt:message key="mvnad.admin.managemedia.title"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <fmt:message key="mvnad.admin.uploadmediasuccess.info"/>
</div>
<br />

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnad.common.success.prompt"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;</b>&nbsp;
    <%if ("addbanner".equals(fromPage)) {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "addbanner")%>"><fmt:message key="mvnad.admin.success.return_to_add_banner"/></a>
    <%} else {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "managemedia")%>"><fmt:message key="mvnad.admin.success.return_to_media_management"/></a>
    <%}%>
    &nbsp;(<fmt:message key="mvnad.common.success.automatic"/>)</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;</b>&nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.success.return_to_admin_control_panel"/></a></td>
  </tr>
</table>
</mvn:cssrows>
<br />

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>