<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/header.jsp,v 1.10 2009/05/07 09:33:52 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.10 $
 - $Date: 2009/05/07 09:33:52 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.MVNForumConfig" %>

<%@ include file="inc_js_checkvalid_myvietnamlib.jsp"%>
<table width="95%" class="noborder" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td width="252" rowspan="2">
      <a href="<%=MVNForumConfig.getLogoUrl()%>"><img src="<%=contextPath%>/mvnplugin/mvnad/images/logo.gif" border="0" alt="" /></a>
    </td>
    <td width="100%" height="25" align="right">
    <%if ( onlineUser.isMember() ) {%>
      <span class="welcomeHeader"><fmt:message key="mvnad.common.ad.welcome"/></span>&nbsp;<font color="#FF0000"><%=memberName%></font>
      <%if (MVNForumConfig.getEnableLogin()) {%>
        &nbsp;&nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "logout")%>" class="welcomeHeader"><fmt:message key="mvnad.common.action.logout"/></a>
      <%}%>
      <br/>
      <% if (MVNForumConfig.getEnableShowLastLoginOfCurrentMember()) {%>
        <fmt:message key="mvnad.common.member.last_login"/>:&nbsp;<%=onlineUser.getGMTTimestampFormat(onlineUser.getLastLogonTimestamp())%>
        <fmt:message key="mvnad.common.from_ip"/>&nbsp;<%=onlineUser.getLastLogonIP()%>
      <%}%>
    <%} else if ((memberName!=null) && (memberName.length()>0)) {%>
      <span class="welcomeHeader"><fmt:message key="mvnad.common.ad.welcome"/></span>&nbsp;<%=memberName%>
      <%if (MVNForumConfig.getEnableLogin()) {%>
        &nbsp;&nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "login")%>" class="welcomeHeader"><fmt:message key="mvnad.common.action.login"/></a>
      <%}%>
    <%} else {%>
      <span class="welcomeHeader"><fmt:message key="mvnad.common.ad.welcome"/></span>&nbsp;<%=MVNForumConfig.getDefaultGuestName()%>
      <%if (MVNForumConfig.getEnableLogin()) {%>
        &nbsp;&nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "login")%>" class="welcomeHeader"><fmt:message key="mvnad.common.action.login"/></a>
      <%}%>
    <%}%>
    </td>
  </tr>
  <tr>
    <td height="40" colspan="3" align="right" valign="bottom" nowrap="nowrap" class="zoneTitle"><fmt:message key="mvnad.common.ad.admin_control_panel"/></td>
  </tr>
</table>

<div class="topmenu">
  &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "index")%>" class="topmenu"><fmt:message key="mvnad.common.ad.index"/></a>
  <%if (permission.canViewZone()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "listzones")%>" class="topmenu"><fmt:message key="mvnad.common.ad.zone_management"/></a>
  <%}%>
  <%if (permission.canViewBanner()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "listbanners")%>" class="topmenu"><fmt:message key="mvnad.admin.listbanners.title"/></a>
  <%}%>
  <%if (permission.canUploadMedia()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "managemedia")%>" class="topmenu"><fmt:message key="mvnad.admin.managemedia.title"/></a>
  <%}%>
  <%if (permission.canManageForumAdvertisement()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "configforum")%>" class="topmenu"><fmt:message key="mvnad.common.ad.config_forum_advertisement"/></a>
  <%}%>
  <%if (permission.canViewChartAds() && (environmentService.getAdRunMode() == EnvironmentService.PRODUCT_ENTERPRISE || environmentService.isPortlet())) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewchart")%>" class="topmenu"><fmt:message key="mvnad.admin.viewchart.title"/></a>
  <%}%>
</div>
