<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/index.jsp,v 1.109 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.109 $
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

<%@ page import="net.myvietnam.mvncore.info.*" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.index.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath(request)%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <fmt:message key="mvnforum.admin.index.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.index.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <%if (permission.canAdminSystem()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "testsystem")%>" class="command"><fmt:message key="mvnforum.admin.testsystem.title"/></a><br/>
  <%}%>
</div>
<br/>

<%
LibInfo libInfo = new LibInfo();
if (libInfo.isSupportImageProcessing() == false) {%>
<div class="pagedesc center">
  <span class="warning"><fmt:message key="mvnforum.admin.testsystem.not_support_image_processing"/></span>
  (<fmt:message key="mvnforum.admin.testsystem.more_info"/> <a href="http://java.sun.com/products/java-media/2D/forDevelopers/java2dfaq.html#xvfb">Sun 2D FAQ</a>)
</div>
<br/>
<% } %>

<%
if (permission.canAdminSystem()) {
    String startTime = ParamUtil.getAttribute(request, "StartTime");
    String nowTime = ParamUtil.getAttribute(request, "NowTime");
    String upTime = ParamUtil.getAttribute(request, "UpTime");
%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td align="center" width="30%"><fmt:message key="mvnforum.admin.index.mvnforum_status"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.index.start_time"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.index.now"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.index.up_time"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="center" class="portlet-font">
    <%if (MVNForumConfig.getShouldShowUserArea()) {%>
      <span style="color:#008000"><fmt:message key="mvnforum.common.on"/></span> - <a href="<%=urlResolver.encodeURL(request, response, "changemode?mode=off&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.index.turn_off"/></a>
    <%} else {%>
      <span style="color:#FF0080"><fmt:message key="mvnforum.common.off"/></span> - <a href="<%=urlResolver.encodeURL(request, response, "changemode?mode=on&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.index.turn_on"/></a>
    <%}%>
    </td>
    <td align="center"><%=startTime%></td>
    <td align="center"><%=nowTime%></td>
    <td align="center"><%=upTime%></td>
  </tr>
</table>
</mvn:cssrows>
<%}%>
<br/>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.mvnforum_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.product_name"/></td>
    <td><%= mvnForumInfo.getProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_desc"/></td>
    <td><%=mvnForumInfo.getProductDesc()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_version"/></td>
    <td><%= mvnForumInfo.getProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_release_date"/></td>
    <td><%= mvnForumInfo.getProductReleaseDate()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_homepage"/></td>
    <td><a href="<%= mvnForumInfo.getProductHomepage()%>"><%=mvnForumInfo.getProductHomepage()%></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt="mvnForum Professional Services"/>
    <span style="color:#008000"><fmt:message key="mvnforum.admin.index.mvnforum_professional_services"/></span></td>
    <td><a href="http://www.myvietnam.net/myvietnam/myvietnam/mvnforumservices"><fmt:message key="mvnforum.admin.index.mvnforum_professional_services_information"/></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><input type="submit" class="portlet-form-button" value="<fmt:message key="mvnforum.admin.index.check_latest_version_now"/>" onclick="javascript: document.mvnforumimage.src='http://www.mvnforum.com/mvnforum/getmvnforumimage?version=<%= mvnForumInfo.getProductVersion()%>&amp;date=<%= mvnForumInfo.getProductReleaseDate()%>'; return false;"/></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/emptyspace.gif" alt="Latest mvnForum version" name="mvnforumimage" id="mvnforumimage" border="0"/></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.myvietnam_framework_information"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.product_name"/></td>
    <td><%=mvnCoreInfo.getProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_desc"/></td>
    <td><%=mvnCoreInfo.getProductDesc()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_version"/></td>
    <td><%=mvnCoreInfo.getProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_release_date"/></td>
    <td><%=mvnCoreInfo.getProductReleaseDate()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.product_homepage"/></td>
    <td><a href="<%=mvnCoreInfo.getProductHomepage()%>"><%=mvnCoreInfo.getProductHomepage()%></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><input type="submit" class="portlet-form-button" value="<fmt:message key="mvnforum.admin.index.check_latest_version_now"/>" onclick="javascript: document.mvncoreimage.src='http://www.mvnforum.com/mvnforum/getmvncoreimage?version=<%=mvnCoreInfo.getProductVersion()%>&amp;date=<%=mvnCoreInfo.getProductReleaseDate()%>'; return false;"/></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/emptyspace.gif" alt="Latest mvnCore version" name="mvncoreimage" id="mvncoreimage" border="0"/></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<%if (permission.canAdminSystem()) {%>
<% ServletInfo servletInfo = new ServletInfo(application); %>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.application_server_info"/>:</td>
  </tr>
  <%if (isPortlet) { %>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.portal_info"/></td>
    <td><%=request.getAttribute("PortalInfo")%></td>
  </tr>
  <%}%>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.application_server"/></td>
    <td><%=servletInfo.getServerInfo()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.servlet_version"/></td>
    <td><%=servletInfo.getServletVersion()%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<% DatabaseInfo databaseInfo = new DatabaseInfo();%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.database_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.database_name"/></td>
    <td><%=databaseInfo.getDatabaseProductName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.database_version"/></td>
    <td><%=databaseInfo.getDatabaseProductVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.database_url"/></td>
    <td><%=databaseInfo.getDatabaseUrl().replaceAll("&", "&amp;")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.database_username"/></td>
    <td><%=databaseInfo.getDatabaseUsername()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.jdbc_driver_name"/></td>
    <td><%=databaseInfo.getDriverName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.jdbc_driver_version"/></td>
    <td><%=databaseInfo.getDriverVersion()%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<% SystemInfo systemInfo = new SystemInfo(); %>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.system_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.os_name"/></td>
    <td><%=systemInfo.getOsName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.os_version"/></td>
    <td><%=systemInfo.getOsVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.cpu"/></td>
    <td><%=systemInfo.getCpu()%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.virtual_machine_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.virtual_machine_name"/></td>
    <td><%=systemInfo.getVmName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_vendor"/></td>
    <td><%=systemInfo.getVmVendor()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_version"/></td>
    <td><%=systemInfo.getVmVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_runtime_name"/></td>
    <td><%=systemInfo.getRuntimeName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_runtime_version"/></td>
    <td><%=systemInfo.getRuntimeVersion()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_total_memory"/></td>
    <td><%=systemInfo.getTotalMemoryKB()%> KB</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.virtual_machine_free_memory"/></td>
    <td><%=systemInfo.getFreeMemoryKB()%> KB</td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.index.cache_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.index.cache_report_member"/> - <a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=member&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_cache"/></a></td>
    <td><%=MemberCache.getInstance().getEfficiencyReport()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.cache_report_post"/> - <a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=post&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_cache"/></a></td>
    <td><%=PostCache.getInstance().getEfficiencyReport()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.cache_report_thread"/> - <a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=thread&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_cache"/></a></td>
    <td><%=ThreadCache.getInstance().getEfficiencyReport()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.cache_report_forum"/> - <a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=forum&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_cache"/></a></td>
    <td><%=ForumCache.getInstance().getEfficiencyReport()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.index.cache_report_category"/> - <a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=category&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_cache"/></a></td>
    <td><%=CategoryCache.getInstance().getEfficiencyReport()%></td>
  </tr>
  <%@ include file="index_cache.jsp" %>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center"><a href="<%=urlResolver.encodeURL(request, response, "clearcache?target=all&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.clear_all_cache"/></a></td>
  </tr>
</table>
</mvn:cssrows>
<%}/*if can admin system*/%>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
