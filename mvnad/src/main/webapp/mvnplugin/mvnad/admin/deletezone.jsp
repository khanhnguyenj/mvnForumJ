<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/deletezone.jsp,v 1.14 2009/08/13 10:53:58 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.14 $
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

<%@ page import="com.mvnsoft.mvnad.db.*"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<%ZoneBean zoneBean = (ZoneBean) request.getAttribute("ZoneBean");%>

<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/md5.js"></script>
<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {   
  var agt=navigator.userAgent.toLowerCase();
  
  // Maybe, Opera make an onClick event when user press enter key 
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitForm();
}

function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnad.common.ad.password"/>")) {
    return false;
  }
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnad.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.deletezone.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listzones")%>"><fmt:message key="mvnad.common.ad.zone_management"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.deletezone.title"/>
</div>
<br />

<div class="pagedesc warning">
  <fmt:message key="mvnad.admin.deletezone.info"/><br /><br />
  <fmt:message key="mvnad.admin.deletezone.warning"/>
</div>
<br />

<form action="<%=urlResolver.encodeURL(request, response, "deletezoneprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "deletezoneprocess")%>
<mvn:securitytoken />
<input type="hidden" name="ZoneID" value="<%=zoneBean.getZoneID()%>" />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
  <table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
    <tr class="portlet-section-header">
      <td colspan="2"><fmt:message key="mvnad.admin.deletezone.title"/></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.name"/></td>
      <td><%=zoneBean.getZoneName()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.description"/></td>
      <td><%=zoneBean.getZoneDesc()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.target_window"/></td>
      <td><%=zoneBean.getZoneTargetWindow()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.cell_width"/></td>
      <td><%=zoneBean.getZoneCellWidth()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.cell_height"/></td>
      <td><%=zoneBean.getZoneCellHeight()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.cell_horizontal_count"/></td>
      <td><%=zoneBean.getZoneCellHorizontalCount()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.cell_vertical_count"/></td>
      <td><%=zoneBean.getZoneCellVerticalCount()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.max_banners"/></td>
      <td><%=zoneBean.getZoneMaxBanners()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.direction"/></td>
      <td>
      <%if (zoneBean.getZoneDirection() == ZoneBean.ZONE_DIRECTION_HORIZONTAL) {%>
        <fmt:message key="mvnad.common.zone.direction.horizontal"/>      
      <%} else if (zoneBean.getZoneDirection() == ZoneBean.ZONE_DIRECTION_VERTICAL) {%>
        <fmt:message key="mvnad.common.zone.direction.vertical"/>
      <%}%>
      </td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td><fmt:message key="mvnad.common.zone.auto_reload_time"/></td>
      <td><%=zoneBean.getZoneAutoReloadTime()%></td>
    </tr>
    <tr class="<mvn:cssrow/>">
      <td width="30%"><label for="MemberCurrentMatkhau"><fmt:message key="mvnad.common.ad.your_pass"/><span class="requiredfield"> *</span></label></td>
      <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event)"/></td>
    </tr>
    <tr class="portlet-section-footer">
      <td colspan="2" align="center">
        <input type="button" name="submitbutton" value="<fmt:message key="mvnad.admin.deletezone.comfirm"/>" onclick="SubmitForm()" class="portlet-form-button" />
        <input type="button" value="<fmt:message key="mvnad.common.action.go_back"/>" onclick="history.back(1)" class="liteoption" />
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