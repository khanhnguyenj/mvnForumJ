<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/listzones.jsp,v 1.13 2009/07/16 03:29:35 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.13 $
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

<%@ page import="java.util.*"%>
<%@ page import="com.mvnsoft.mvnad.AdModuleUtils"%>
<%@ page import="com.mvnsoft.mvnad.MVNAdResourceBundle"%>
<%@ page import="com.mvnsoft.mvnad.db.*"%>
<%@ page import="net.myvietnam.mvncore.util.*"%>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
if (sort.length() == 0) sort = "ZoneID";
if (order.length() == 0) order = "DESC";

int totalZones = ((Integer) request.getAttribute("TotalZones")).intValue();
int rowsPerPage = onlineUser.getPostsPerPage();
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.listzones.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//<![CDATA[
function handleGo() {
<mvn:servlet>
  document.form.go.disabled=true;
</mvn:servlet>
  document.form.submit();
}
//]]>  
</script>
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.common.ad.zone_management"/>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnad.admin.listzones.info"/>
  <br />
  <a href="<%=urlResolver.encodeURL(request, response, "addzone")%>" class="command"><fmt:message key="mvnad.admin.listzones.add_new_zone"/></a>
</div>
<br />

<pg:pager
  url="listzones"
  items="<%= totalZones %>"
  maxPageItems="<%= rowsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNAdResourceBundle.getString(onlineUser.getLocale(), "mvnad.common.numberof.zones"); %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>

<table width="95%" align="center">
  <tr class="portlet-font">
    <td nowrap="nowrap">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "listzones", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "listzones")%>
       <label for="sort"><fmt:message key="mvnad.common.sort_by"/></label>
      <select id="sort" name="sort">
        <option value="ZoneID"<%if (sort.equals("ZoneID")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.id"/></option>
        <option value="MemberName"<%if (sort.equals("MemberName")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.member_name"/></option>
        <option value="ZoneName"<%if (sort.equals("ZoneName")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.name"/></option>
        <option value="ZoneAutoReloadTime"<%if (sort.equals("ZoneAutoReloadTime")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.auto_reload_time"/></option>
      </select>
       <label for="order"><fmt:message key="mvnad.common.order"/></label>
      <select id="order" name="order">
        <option value="ASC"<%if (order.equals("ASC")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.ascending"/></option>
        <option value="DESC"<%if (order.equals("DESC")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.descending"/></option>
      </select>

      <input type="button" name="go" value="<fmt:message key="mvnad.common.action.go"/>" onclick="handleGo();" class="liteoption"/>
    </form>
    </td>
  </tr>
</table>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>

<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" width="5%"><fmt:message key="mvnad.common.zone.id"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.member_name"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.name"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.description"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.target_window"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.cell_width"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.cell_height"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.cell_horizontal_count"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.cell_vertical_count"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.max_banners"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.direction"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.type"/></td>
    <td align="center"><fmt:message key="mvnad.common.zone.auto_reload_time"/></td>
    <td align="center"><fmt:message key="mvnad.common.ad.actions"/></td>
  </tr>
<%
  Collection zoneBeans = (Collection) request.getAttribute("ZoneBeans");
  for (Iterator iterator = zoneBeans.iterator(); iterator.hasNext(); ) {
    ZoneBean zoneBean = (ZoneBean) iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td align="center"><a title="<fmt:message key="mvnad.admin.listzones.edit_zoneid"/>=<%=zoneBean.getZoneID()%>" href="<%=urlResolver.encodeURL(request, response, "editzone?zoneID=" + zoneBean.getZoneID())%>" class="command"><%=zoneBean.getZoneID()%></a></td>
    <td align="center"><%=zoneBean.getMemberName()%></td>
    <td align="center"><%=zoneBean.getZoneName()%></td>
    <td><%=zoneBean.getZoneDesc()%></td>
    <td align="center"><%=zoneBean.getZoneTargetWindow()%></td>
    <td align="center"><%=zoneBean.getZoneCellWidth()%></td>
    <td align="center"><%=zoneBean.getZoneCellHeight()%></td>
    <td align="center"><%=zoneBean.getZoneCellHorizontalCount()%></td>
    <td align="center"><%=zoneBean.getZoneCellVerticalCount()%></td>
    <td align="center"><%=zoneBean.getZoneMaxBanners()%></td>
    <td align="center"><%=AdModuleUtils.getZoneDirectionName(zoneBean.getZoneDirection(), onlineUser.getLocale())%></td>
    <td align="center"><%=AdModuleUtils.getZoneTypeName(zoneBean.getZoneType(), onlineUser.getLocale())%></td>
    <td align="center"><%=zoneBean.getZoneAutoReloadTime()%></td>
    <td align="center" width="170">
      <a href="<%=urlResolver.encodeURL(request, response, "deletezone?zoneID=" + zoneBean.getZoneID())%>" class="command">
        <fmt:message key="mvnad.admin.listzones.deltele_zone"/></a><br />
      <a title="<fmt:message key="mvnad.admin.listzones.list_all_banner_of_zoneid"/>=<%=zoneBean.getZoneID()%>" href="<%=urlResolver.encodeURL(request, response, "listbanners?ZoneID=" + zoneBean.getZoneID())%>" class="command">
        <fmt:message key="mvnad.admin.listzones.list_banner"/></a>
        (<span class="requiredfield"><b><%=zoneBean.getBannerCount()%></span></b>)<br />
      <a title="<fmt:message key="mvnad.admin.listzones.link_this_zone_with_banner"/>" href="<%=urlResolver.encodeURL(request, response, "addbannerofzone?ZoneID=" + zoneBean.getZoneID())%>" class="command">
        <fmt:message key="mvnad.admin.listzones.link_with_banner"/></a><br />
      <a title="<fmt:message key="mvnad.admin.listzones.get_html_code_of_this_zone"/>" href="<%=urlResolver.encodeURL(request, response, "getcode?ZoneID=" + zoneBean.getZoneID())%>" class="command">
        <fmt:message key="mvnad.admin.listzones.get_html_code_of_this_zone"/></a>
    </td>
  </tr>
</pg:item>  
<%}
  if (zoneBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>">
    <td colspan="14" align="center"><fmt:message key="mvnad.admin.listzones.no_zone_here"/></td>
  </tr>
<%}%>
</mvn:cssrows>
</table>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>