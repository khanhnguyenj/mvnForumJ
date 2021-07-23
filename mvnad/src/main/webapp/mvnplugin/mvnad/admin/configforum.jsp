<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/configforum.jsp,v 1.11 2009/07/16 03:29:35 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.11 $
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
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mvnsoft.mvnad.db.ZoneBean"%>
<%@ page import="com.mvnforum.service.MvnForumAdService"%>
<%@ page import="com.mvnforum.service.MvnForumServiceFactory"%>
<%@ page import="net.myvietnam.mvncore.util.StringUtil"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
List zoneBeans = (List) request.getAttribute("ZoneBeans");
int[] zoneIDs = new int[zoneBeans.size()+1];
String[] zoneNames = new String[zoneBeans.size()+1];
zoneIDs[0] = 0;
zoneNames[0] = "";
for (int i = 0; i<zoneBeans.size(); i++) {
  ZoneBean zoneBean = (ZoneBean) zoneBeans.get(i);
  zoneNames[i+1] = zoneBean.getZoneName();
  zoneIDs[i+1] = zoneBean.getZoneID();
}
MvnForumAdService mvnForumAdService = MvnForumServiceFactory.getMvnForumService().getMvnForumAdService();
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.configforum.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/mvnplugin/mvnad/css/style.css" />
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.configforum.title"/>
</div>
<br />
  
<form action="<%=urlResolver.encodeURL(request, response, "configforumprocess", URLResolverService.ACTION_URL)%>" name="editLayoutForm" method="post">
<%=urlResolver.generateFormAction(request, response, "configforumprocess")%>
<mvn:securitytoken />
<table cellpadding="2" cellspacing="1" border="0" width="95%" align="center" class="tborder">
  <tr>
    <td class="topmenu" colspan="2"><fmt:message key="mvnad.admin.configforum.banner_heart"/></td>
    <td class="portlet-section-body" colspan="5"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adHeart", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_AD_POSTITION_HEART))%></td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.header_1"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum11", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_1))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum12", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_2))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum13", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_3))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum14", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_4))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum15", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_5))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum16", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_1_POSITION_6))%></td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.header_2"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum21", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_1))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum22", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_2))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum23", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_3))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum24", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_4))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum25", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_5))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum26", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_2_POSITION_6))%></td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.header_3"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum31", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_3_POSITION_1))%></td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum32", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_3_POSITION_2))%></td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adForum33", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_HEADER_3_POSITION_3))%></td>
    <td class="portlet-section-body">&nbsp;</td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.footer_1"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter11", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_1))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter12", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_2))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter13", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_3))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter14", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_4))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter15", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_5))%></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFooter16", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FOOTER_1_POSITION_6))%></td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_post"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstPost", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_post_page_2"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstPost2", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST_PAGE_2))%></td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body">&nbsp;</td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.last_odd_post"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adLastOddPost", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_ODD_POST))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.last_even_post"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adLastEvenPost", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_EVEN_POST))%></td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body">&nbsp;</td>
    <td class="portlet-section-body">&nbsp;</td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.view_message"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adViewMessage", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_VIEW_MESSAGE))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_attachment"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstAttachment", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ATTACHEMNT))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_recent_thread"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstRecentThread", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_RECEND_THREAD))%></td>
    <td class="portlet-section-body">&nbsp;</td>
  </tr>
  <tr>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_normal_thread"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstNormalThread", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_NORMAL_THREAD))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_active_thread"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstActiveThread", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ACTIVE_THREAD))%></td>
    <td class="topmenu"><fmt:message key="mvnad.admin.configforum.first_unanswered_thread"/></td>
    <td class="portlet-section-body"><%=StringUtil.getSelectionModel(zoneIDs, zoneNames, "adFirstUnansweredThread", mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_UNANSWERED_THREAD))%></td>
    <td class="portlet-section-body">&nbsp;</td>
  </tr>
  <tr class="portlet-section-footer" align="center">
    <td colspan="7">
      <input type="submit" value="<fmt:message key="mvnad.admin.configforum.save_button"/>" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnad.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</form>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>