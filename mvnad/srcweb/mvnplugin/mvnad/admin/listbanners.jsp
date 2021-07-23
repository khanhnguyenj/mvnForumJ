<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/listbanners.jsp,v 1.18 2009/09/10 03:14:39 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.18 $
 - $Date: 2009/09/10 03:14:39 $
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

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.mvnforum.db.MemberBean"%>
<%@ page import="com.mvnsoft.mvnad.AdModuleUtils"%>
<%@ page import="com.mvnsoft.mvnad.db.BannerBean"%>
<%@ page import="com.mvnsoft.mvnad.db.ZoneBean"%>
<%@ page import="net.myvietnam.mvncore.util.DateUtil"%>
<%@ page import="net.myvietnam.mvncore.util.StringUtil"%>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
String userName = (String) request.getAttribute("MemberName");
int zoneID = ((Integer) request.getAttribute("ZoneID")).intValue();
Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
Collection zoneBeans = (Collection) request.getAttribute("ZoneBeans");
Collection bannerBeans = (Collection) request.getAttribute("BannerBeans");

String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
if (sort.length() == 0) sort = "BannerID";
if (order.length() == 0) order = "DESC";

/*
int totalBanners = ((Integer) request.getAttribute("TotalBanners")).intValue();
int rowsPerPage = onlineUser.getPostsPerPage();
*/
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.listbanners.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//<![CDATA[
function _gotoPage(memberName, zoneID) {
  location.href = 'listbanners?MemberName=' + memberName + '&ZoneID=' + zoneID;
}
function checkUnlink(link) {
  if (confirm("<fmt:message key="mvnad.admin.listbanners.unlink_banner_with_zone"/>") == true) {
    location.href = link;
  }
}
function handleGo() {
<mvn:servlet>
  document.form.go.disabled=true;
</mvn:servlet>
  document.form.submit();
}
//]]>  
</script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/overlib.js"></script>
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.listbanners.title"/>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnad.admin.listbanners.info"/><br /><br />
  <%if (permission.canAddBanner()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "addbanner")%>" class="command"><fmt:message key="mvnad.admin.addbanner.title"/></a><br />
  <%} 
    if (permission.canUploadMedia()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "managemedia")%>" class="command"><fmt:message key="mvnad.admin.managemedia.title"/></a>
  <%}%>
</div>
<br />

<div class="pagedesc">
  <form name="form" action="<%=urlResolver.encodeURL(request, response, "listbanners", URLResolverService.ACTION_URL)%>" <mvn:method/>>
    <%=urlResolver.generateFormAction(request, response, "listbanners")%>
    <fmt:message key="mvnad.common.ad.filter_by"/>
    <br />
    <label for="MemberName"><fmt:message key="mvnad.common.ad.user"/></label>
    <select id="MemberName" name="MemberName"<%-- onchange="javascript:_gotoPage(this.options[this.selectedIndex].value, <%=zoneID%>)"--%>>
      <option value=""><fmt:message key="mvnad.common.ad.all"/></option>
  <%for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
      MemberBean memberBean = (MemberBean) iterator.next(); %>
      <option value="<%=memberBean.getMemberName()%>"<%if (userName.equals(memberBean.getMemberName())) {%> selected="selected"<%}%>><%=memberBean.getMemberName()%></option>
  <%}%>
    </select>
    <br />
    <label for="ZoneID"><fmt:message key="mvnad.common.zone"/></label>
    <select id="ZoneID" name="ZoneID"<%--  onchange="javascript:_gotoPage('<%=userName%>', this.options[this.selectedIndex].value)"--%>>
      <option value="0"><fmt:message key="mvnad.common.ad.all"/></option>
  <%for (Iterator iterator = zoneBeans.iterator(); iterator.hasNext(); ) {
      ZoneBean zoneBean = (ZoneBean) iterator.next(); %>
      <option value="<%=zoneBean.getZoneID()%>"<%if (zoneBean.getZoneID() == zoneID) {%> selected="selected" <%} %>><%=zoneBean.getZoneName()%></option>
  <%}%>
    </select>
    <br />
    <label for="sort"><fmt:message key="mvnad.common.sort_by"/></label>
    <select id="sort" name="sort">
      <option value="BannerID"<%if (sort.equals("BannerID")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.id"/></option>
    <%if (userName.length() == 0) {%>
      <option value="MemberName"<%if (sort.equals("MemberName")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.member_name"/></option>
    <%}%>
      <option value="BannerName"<%if (sort.equals("BannerName")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.name"/></option>
      <option value="BannerReceivedImpression"<%if (sort.equals("BannerReceivedImpression")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.received_impression"/></option>
      <option value="BannerReceivedClick"<%if (sort.equals("BannerReceivedClick")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.received_click"/></option>
      <option value="BannerStartDate"<%if (sort.equals("BannerStartDate")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.start_date"/></option>
      <option value="BannerEndDate"<%if (sort.equals("BannerEndDate")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.banner.end_date"/></option>
    </select>
    <label for="order"><fmt:message key="mvnad.common.order"/></label>
    <select id="order" name="order">
      <option value="ASC"<%if (order.equals("ASC")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.ascending"/></option>
      <option value="DESC"<%if (order.equals("DESC")) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.descending"/></option>
    </select>

    <input type="button" name="go" value="<fmt:message key="mvnad.common.action.go"/>" onclick="javascript:handleGo();" class="liteoption"/>
  </form>
  <%if (zoneID != 0) {%>
    <br />
    <a href="<%=urlResolver.encodeURL(request, response, "addbannerofzone?ZoneID=" + zoneID)%>" class="command">
      <fmt:message key="mvnad.admin.listbanners.link_more_banner_with_zone"/>
    </a>
  <%}%>
</div>
<br />

<%--
<pg:pager
  url="listbanners"
  items="<%= totalBanners %>"
  maxPageItems="<%= rowsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNAdResourceBundle.getString(onlineUser.getLocale(), "mvnad.common.numberof.banners"); %>
<pg:param name="sort"/>
<pg:param name="order"/>
<pg:param name="MemberName" value="<%=userName%>"/>
<pg:param name="ZoneID" value="<%=String.valueOf(zoneID)%>"/>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
--%>
<table class="tborder" align="center" width="95%" cellpadding="3" cellspacing="0">
<mvn:cssrows>
  <tr class="portlet-section-header" align="center">
    <td><fmt:message key="mvnad.common.banner.id"/></td>
    <td><fmt:message key="mvnad.common.banner.member_name"/></td>
    <td><fmt:message key="mvnad.common.banner.preview_image_url"/></td>
    <td><fmt:message key="mvnad.common.banner.target_url"/></td>
    <td><fmt:message key="mvnad.common.banner.name"/></td>
    <td><fmt:message key="mvnad.common.banner.description"/></td>
    <td><fmt:message key="mvnad.common.banner.type"/></td>
    <td><fmt:message key="mvnad.common.banner.width"/></td>
    <td><fmt:message key="mvnad.common.banner.height"/></td>
    <td><fmt:message key="mvnad.common.banner.weight"/></td>
    <td><fmt:message key="mvnad.common.banner.received_impression"/></td>
    <td><fmt:message key="mvnad.common.banner.received_click"/></td>
    <td><fmt:message key="mvnad.common.banner.zone_position_x"/></td>
    <td><fmt:message key="mvnad.common.banner.zone_position_y"/></td>
    <td><fmt:message key="mvnad.common.banner.number_linked_zones"/></td>
    <td><fmt:message key="mvnad.common.banner.start_date"/></td>
    <td><fmt:message key="mvnad.common.banner.end_date"/></td>
    <td><fmt:message key="mvnad.common.banner.status"/></td>
  <%if (zoneID != 0) {%>
    <td align="center"><fmt:message key="mvnad.common.ad.manage_link"/></td>
  <%} else {%>
    <td align="center"><fmt:message key="mvnad.common.action.delete"/></td>
  <%}%>
  </tr>
<%
for (Iterator iterator = bannerBeans.iterator(); iterator.hasNext();) {
    BannerBean bannerBean = (BannerBean) iterator.next();
%>
  <tr class="<mvn:cssrow/>" align="center">
    <td width="5%">
    <%if (permission.canEditBanner()) {%>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editbanner?id=" + bannerBean.getBannerID())%>">
      <%=bannerBean.getBannerID()%>
      </a>
    <%} else {%>
      <%=bannerBean.getBannerID()%>
    <%} %>
    </td>
    <td><%=bannerBean.getMemberName()%></td>
    
    <td>
      <div align="center">
        <%if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%>
          <%=bannerBean.getBannerHtmlCode()%>
        <%} else {%>
          <a href="<%=AdModuleUtils.getBannerWebPath(bannerBean)%>" target="_blank">
            <%=StringUtil.displayMediaContent(AdModuleUtils.getBannerWebPath(bannerBean), 0, 0)%>
          </a>
        <%}%>
      </div>
    </td>

    <td>
      <div align="center">
      <%if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%>
        <fmt:message key="mvnad.common.not_applicable"/>
      <%} else {%>
        <a href="<%=bannerBean.getBannerTargetURL()%>" target="_blank">
          <img src="<%=contextPath%>/mvnplugin/mvnad/images/link.gif" onmouseout="return nd();" onmouseover="return overlib('<%=StringUtil.escapeQuote(bannerBean.getBannerTargetURL())%>')" border="0" alt="" />
        </a>
      <%}%>
      </div>
    </td>

    <td><%=bannerBean.getBannerName()%></td>
    <td><%=bannerBean.getBannerDesc()%></td>
    <td>
    <%if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_IMAGE) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnad/images/icon_image.gif" border="0" alt="" /><br />
      <fmt:message key="mvnad.common.banner.type.image"/>
    <%} else if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_FLASH) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnad/images/icon_flash.gif" border="0" alt="" /><br />
      <fmt:message key="mvnad.common.banner.type.flash"/>
    <%} else if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_MOVIE) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnad/images/icon_movie.gif" border="0" alt="" /><br />
      <fmt:message key="mvnad.common.banner.type.movie"/>
    <%} else if (bannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnad/images/icon_html.gif" border="0" alt="" /><br />
      <fmt:message key="mvnad.common.banner.type.html"/>
    <%}%>
    </td>
    <td><%=bannerBean.getBannerWidth()%></td>
    <td><%=bannerBean.getBannerHeight()%></td>
    <td><%=bannerBean.getBannerWeight()%></td>
    <td><%=bannerBean.getBannerReceivedImpression()%></td>
    <td><%=bannerBean.getBannerReceivedClick()%></td>
    <td><%=bannerBean.getBannerZonePositionX()%></td>
    <td><%=bannerBean.getBannerZonePositionY()%></td>
    <td align="center" width="170">
      <a title="<fmt:message key="mvnad.admin.listbanners.list_all_zone_of_bannerid"/>=<%=bannerBean.getBannerID()%>" href="<%=urlResolver.encodeURL(request, response, "listzones?BannerID=" + bannerBean.getBannerID())%>" class="command">
        <fmt:message key="mvnad.admin.listzones.title"/></a>
      (<span class="requiredfield"><b><%=bannerBean.getZoneCount()%></b></span>)<br />
    </td>
    <td><%=onlineUser.getGMTDateFormat(bannerBean.getBannerStartDate())%></td>
    <td><%=onlineUser.getGMTDateFormat(bannerBean.getBannerEndDate())%></td>
    <td>
  <%if (bannerBean.isValid(DateUtil.getCurrentGMTTimestamp())) {%>
      <fmt:message key="mvnad.common.status.running"/>
  <%} else {%>
      <fmt:message key="mvnad.common.status.expired"/>
  <%}%>
    </td>
    
  <%if (zoneID != 0) {%>
    <td align="center">
      <a href="#" onclick="checkUnlink('<%=urlResolver.encodeURL(request, response, "deletebannerofzone?zoneID=" + zoneID + "&bannerID=" + bannerBean.getBannerID(), URLResolverService.ACTION_URL)%>');return false;" class="command">
        <fmt:message key="mvnad.admin.listbanners.delete_link"/>
      </a>
      <br />
      <a title="<fmt:message key="mvnad.admin.listbanners.edit_link_between_banner_zone"/>" href="<%=urlResolver.encodeURL(request, response, "editbannerofzone?ZoneID=" + zoneID + "&BannerID=" + bannerBean.getBannerID())%>" class="command">
        <fmt:message key="mvnad.admin.listbanners.edit_link"/>
      </a>
    </td>
  <%} else {%>
    <td align="center">
    <%if (permission.canDeleteBanner()) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "deletebanner?id=" + bannerBean.getBannerID())%>" class="command">
        <fmt:message key="mvnad.common.action.delete"/>
      </a>
    <%}%>
    </td>
  <%}%>
    
  </tr>
<%}
  if (bannerBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>">
    <td colspan="19" align="center"><fmt:message key="mvnad.admin.listbanners.no_banner_here" /></td>
  </tr>
<%}%>
</mvn:cssrows>
</table>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>