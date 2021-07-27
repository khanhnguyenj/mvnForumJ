<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/editbannerofzone.jsp,v 1.10 2009/08/13 10:53:58 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.10 $
 - $Date: 2009/08/13 10:53:58 $
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

<%@ page import="java.util.*" %>
<%@ page import="com.mvnsoft.mvnad.db.*"%>
<%@ page import="net.myvietnam.mvncore.util.DateUtil"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
ZoneBannerBean zoneBannerBean = (ZoneBannerBean) request.getAttribute("ZoneBannerBean");
BannerBean bannerBean = (BannerBean) request.getAttribute("BannerBean");
ZoneBean zoneBean = (ZoneBean) request.getAttribute("ZoneBean");
%>

<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.editbannerofzone.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//<![CDATA[
function changeImage(s) {
  document.submitForm.imgChange.src = s;
}
//]]>
</script>
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listbanners")%>"><fmt:message key="mvnad.admin.listbanners.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.editbannerofzone.title"/>
</div>
<br />

<form action="<%=urlResolver.encodeURL(request, response, "editbannerofzoneprocess", URLResolverService.ACTION_URL)%>" name="submitForm" method="post">
<%=urlResolver.generateFormAction(request, response, "editbannerofzoneprocess")%>
<mvn:securitytoken />
<input type="hidden" name="ZoneID" value="<%=zoneBean.getZoneID()%>" />
<input type="hidden" name="BannerID" value="<%=bannerBean.getBannerID()%>" />
<mvn:cssrows>
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2">
      <fmt:message key="mvnad.admin.editbannerofzone.title"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="15%"><fmt:message key="mvnad.common.zone.name"/></td>
    <td><%=zoneBean.getZoneName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.name"/></td>
    <td><%=bannerBean.getBannerName()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><fmt:message key="mvnad.common.bannerofzone.zone_publish_start_date"/><span class="requiredfield"> *</span></td>
    <td>
      <%prefixName = "RelationPublishStartDate";
        float startYear = 0;
        Timestamp currentTimestamp = zoneBannerBean.getRelationPublishStartDate();
      %>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><fmt:message key="mvnad.common.bannerofzone.zone_publish_end_date"/><span class="requiredfield"> *</span></td>
    <td>
      <%prefixName = "RelationPublishEndDate";
        currentTimestamp = zoneBannerBean.getRelationPublishEndDate();
      %>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="portlet-section-footer" align="center">
    <td colspan="2">
      <input type="submit" value="<fmt:message key="mvnad.common.action.save"/>" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnad.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>