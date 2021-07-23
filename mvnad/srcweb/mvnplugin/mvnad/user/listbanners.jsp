<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/user/listbanners.jsp,v 1.11 2009/07/16 03:29:35 lexuanttkhtn Exp $
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

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.mvnsoft.mvnad.AdModuleUtils"%>
<%@ page import="com.mvnsoft.mvnad.db.BannerBean"%>
<%@ page import="net.myvietnam.mvncore.util.DateUtil"%>
<%@ page import="net.myvietnam.mvncore.util.StringUtil"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
<mvn:title><fmt:message key="mvnad.user.index.title"/> - <fmt:message key="mvnad.user.listbanners.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/overlib.js"></script>
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.user.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.user.listbanners.title"/>
</div>
<br />

<%
Collection bannerBeans = (Collection) request.getAttribute("BannerBeans"); 
%>
<table class="tborder" align="center" width="95%" cellpadding="3" cellspacing="0">
<mvn:cssrows>
  <tr class="portlet-section-header" align="center">
    <td><fmt:message key="mvnad.common.banner.id"/></td>
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
    <td><fmt:message key="mvnad.common.banner.start_date"/></td>
    <td><fmt:message key="mvnad.common.banner.end_date"/></td>
    <td><fmt:message key="mvnad.common.banner.status"/></td>
  </tr>
<%
for (Iterator iterator = bannerBeans.iterator(); iterator.hasNext(); ) {
    BannerBean bannerBean = (BannerBean) iterator.next();
%>
  <tr class="<mvn:cssrow/>" align="center">
    <td width="5%">
      <%=bannerBean.getBannerID()%>
    </td>
    
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
    <td><%=onlineUser.getGMTDateFormat(bannerBean.getBannerStartDate())%></td>
    <td><%=onlineUser.getGMTDateFormat(bannerBean.getBannerEndDate())%></td>
    <td>
  <%if (bannerBean.isValid(DateUtil.getCurrentGMTTimestamp())) {%>
      <fmt:message key="mvnad.common.status.running"/>
  <%} else {%>
      <fmt:message key="mvnad.common.status.expired"/>
  <%}%>
    </td>
  </tr>
<%}// for
  if (bannerBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>">
    <td colspan="16" align="center"><fmt:message key="mvnad.admin.listbanners.no_banner_here" /></td>
  </tr>
<%}%>
</mvn:cssrows>
</table>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>
