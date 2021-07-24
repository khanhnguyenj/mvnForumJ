<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/index.jsp,v 1.8 2009/07/16 03:29:35 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.8 $
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
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.info.ServletInfo"%>
<%@ page import="net.myvietnam.mvncore.info.DatabaseInfo"%>
<%@ page import="net.myvietnam.mvncore.info.SystemInfo"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.index.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="index"><fmt:message key="mvnad.admin.index.title"/></a>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnad.admin.index.thank_info"/>
</div>
<br />

<%
if (onlineUser.getPermission().canAdminSystem()) {
    String startTime = ParamUtil.getAttribute(request, "StartTime");
    String nowTime = ParamUtil.getAttribute(request, "NowTime");
    String upTime = ParamUtil.getAttribute(request, "UpTime");
%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="30%"><fmt:message key="mvnad.admin.index.mvnad_status"/></td>
    <td align="center"><fmt:message key="mvnad.admin.index.start_time"/></td>
    <td align="center"><fmt:message key="mvnad.admin.index.now"/></td>
    <td align="center"><fmt:message key="mvnad.admin.index.up_time"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.mvnad_status"/></td>
    <td align="center"><%=startTime%></td>
    <td align="center"><%=nowTime%></td>
    <td align="center"><%=upTime%></td>
  </tr>
</table>
</mvn:cssrows>
<%}%>
<br />

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.mvnad_information"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.product_name"/></td>
    <td><%= mvnAdInfo.getProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_desc"/></td>
    <td><%=mvnAdInfo.getProductDesc()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_version"/></td>
    <td><%= mvnAdInfo.getProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_release_date"/></td>
    <td><%= mvnAdInfo.getProductReleaseDate()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_homepage"/></td>
    <td><a href="<%= mvnAdInfo.getProductHomepage()%>"><%=mvnAdInfo.getProductHomepage()%></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnad/images/group.gif" border="0" alt="mvnAd Professional Services" />
    <span style="color:#008000"><fmt:message key="mvnad.admin.index.mvnad_professional_services"/></span></td>
    <td><a href="http://www.myvietnam.net/myvietnam/myvietnam/mvnforumservices"><fmt:message key="mvnad.admin.index.mvnad_professional_services_info"/></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><input type="submit" class="portlet-form-button" value="<fmt:message key="mvnad.admin.index.check_latest_version"/>" onclick="javascript:document.mvnforumimage.src='http://www.mvnforum.com/delivery/getmvnadimage?version=<%= mvnAdInfo.getProductVersion()%>&amp;date=<%= mvnAdInfo.getProductReleaseDate()%>'; return false;" /></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnad/images/emptyspace.gif" alt="Latest mvnAd version" name="mvnforumimage" id="mvnforumimage" border="0"/></td>
  </tr>
</table>
</mvn:cssrows>
<br />

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.myvietnam_framework_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.product_name"/></td>
    <td><%=mvnCoreInfo.getProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_desc"/></td>
    <td><%=mvnCoreInfo.getProductDesc()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_version"/></td>
    <td><%=mvnCoreInfo.getProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_release_date"/></td>
    <td><%=mvnCoreInfo.getProductReleaseDate()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.product_homepage"/></td>
    <td><a href="<%=mvnCoreInfo.getProductHomepage()%>"><%=mvnCoreInfo.getProductHomepage()%></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><input type="submit" class="portlet-form-button" value="<fmt:message key="mvnad.admin.index.check_latest_version"/>" onclick="javascript: document.mvncoreimage.src='http://www.mvnforum.com/mvnforum/getmvncoreimage?version=<%=mvnCoreInfo.getProductVersion()%>&amp;date=<%=mvnCoreInfo.getProductReleaseDate()%>'; return false;" /></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnad/images/emptyspace.gif" alt="Latest mvnCore version" name="mvncoreimage" id="mvncoreimage" border="0" /></td>
  </tr>
</table>
</mvn:cssrows>
<br />
<%if (permission.canAdminSystem()) {%>
<% ServletInfo servletInfo = new ServletInfo(application); %>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.application_server_info"/>:</td>
  </tr>
  <%if (isPortlet) { %>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.portal_info"/></td>
    <td><%=request.getAttribute("PortalInfo")%></td>
  </tr>
  <%}%>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.application_server"/></td>
    <td><%=servletInfo.getServerInfo()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.servlet_version"/></td>
    <td><%=servletInfo.getServletVersion()%></td>
  </tr>
</table>
</mvn:cssrows>
<br />

<% DatabaseInfo databaseInfo = new DatabaseInfo();%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.database_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.database_name"/></td>
    <td><%=databaseInfo.getDatabaseProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.database_version"/></td>
    <td><%=databaseInfo.getDatabaseProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.database_url"/></td>
    <td><%=databaseInfo.getDatabaseUrl().replaceAll("&", "&amp;")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.database_username"/></td>
    <td><%=databaseInfo.getDatabaseUsername()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.jdbc_driver_name"/></td>
    <td><%=databaseInfo.getDriverName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.jdbc_driver_version"/></td>
    <td><%=databaseInfo.getDriverVersion()%></td>
  </tr>
</table>
</mvn:cssrows>
<br />

<% SystemInfo systemInfo = new SystemInfo(); %>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.system_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.os_name"/></td>
    <td><%=systemInfo.getOsName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.os_version"/></td>
    <td><%=systemInfo.getOsVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.cpu"/></td>
    <td><%=systemInfo.getCpu()%></td>
  </tr>
</table>
</mvn:cssrows>
<br />

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.index.virtual_machine_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnad.admin.index.virtual_machine_name"/></td>
    <td><%=systemInfo.getVmName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_vendor"/></td>
    <td><%=systemInfo.getVmVendor()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_version"/></td>
    <td><%=systemInfo.getVmVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_runtime_name"/></td>
    <td><%=systemInfo.getRuntimeName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_runtime_version"/></td>
    <td><%=systemInfo.getRuntimeVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_total_memory"/></td>
    <td><%=systemInfo.getTotalMemoryKB()%> KB</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.admin.index.virtual_machine_free_memory"/></td>
    <td><%=systemInfo.getFreeMemoryKB()%> KB</td>
  </tr>
</table>
</mvn:cssrows>
<%}/*if can admin system*/%>
<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>