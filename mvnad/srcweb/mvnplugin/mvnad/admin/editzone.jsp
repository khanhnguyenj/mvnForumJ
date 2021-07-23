<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/editzone.jsp,v 1.12 2009/08/13 10:53:58 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.12 $
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

<%@ page import="com.mvnsoft.mvnad.db.ZoneBean"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.editzone.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/md5.js"></script>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
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
function ValidateForm() {
  if (isBlank(document.submitform.ZoneName, "<fmt:message key="mvnad.common.zone.name"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ZoneCellWidth, "<fmt:message key="mvnad.common.zone.cell_width"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ZoneCellHeight, "<fmt:message key="mvnad.common.zone.cell_height"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ZoneCellHorizontalCount, "<fmt:message key="mvnad.common.zone.cell_horizontal_count"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ZoneCellVerticalCount, "<fmt:message key="mvnad.common.zone.cell_vertical_count"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ZoneMaxBanners, "<fmt:message key="mvnad.common.zone.max_banners"/>")) return false;
  if (document.submitform.ZoneMaxBanners.value < 1) {
    alert("<fmt:message key="mvnad.common.zone.max_banners"/> <fmt:message key="mvnad.common.js.prompt.mustbe_greater_or_equal_to"/> 1");
    return false;
  }
  if (!isUnsignedInteger(document.submitform.ZoneAutoReloadTime, "<fmt:message key="mvnad.common.zone.auto_reload_time"/>")) return false;
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnad.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}
function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }
    document.submitform.submitbutton.disabled=true;
    document.submitform.submit();
  }
}
//]]>
</script>
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br />

<%
ZoneBean adZoneBean = (ZoneBean)request.getAttribute("adZoneBean");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listzones")%>"><fmt:message key="mvnad.common.ad.zone_management"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.editzone.title"/>
</div>
<br />

<form name="submitform" action="<%=urlResolver.encodeURL(request, response, "editzoneprocess", URLResolverService.ACTION_URL)%>" method="post" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "editzoneprocess")%>
<mvn:securitytoken />
<input type="hidden" name="ZoneID" value="<%=adZoneBean.getZoneID()%>" />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.editzone.title"/><br /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneName"><fmt:message key="mvnad.common.zone.name"/><span class="requiredfield"> *</span></label><br /></td>
    <td><input type="text" id="ZoneName" name="ZoneName" size="70" value="<%=adZoneBean.getZoneName()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="sort"><label for="ZoneDesc"><fmt:message key="mvnad.common.zone.description"/></label><br /></td>
    <td><input type="text" id="ZoneDesc" name="ZoneDesc" size="70" value="<%=adZoneBean.getZoneDesc()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneTargetWindow"><fmt:message key="mvnad.common.zone.target_window"/><span class="requiredfield"> *</span></label><br /></td>
    <td>
      <select id="ZoneTargetWindow" name="ZoneTargetWindow">
        <option value="_blank"<% if ("_blank".equals(adZoneBean.getZoneTargetWindow())) {%> selected="selected"<% } %>><fmt:message key="mvnad.common.target_link._blank"/></option>
        <option value="_parent"<% if ("_parent".equals(adZoneBean.getZoneTargetWindow())) {%> selected="selected"<% } %>><fmt:message key="mvnad.common.target_link._parent"/></option>
        <option value="_self"<% if ("_self".equals(adZoneBean.getZoneTargetWindow())) {%> selected="selected"<% } %>><fmt:message key="mvnad.common.target_link._self"/></option>
        <option value="_top"<% if ("_top".equals(adZoneBean.getZoneTargetWindow())) {%> selected="selected"<% } %>><fmt:message key="mvnad.common.target_link._top"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneCellWidth"><fmt:message key="mvnad.common.zone.cell_width"/><span class="requiredfield"> *</span></label><br /></td>
    <td><input type="text" id="ZoneCellWidth" name="ZoneCellWidth" size="13" value="<%=adZoneBean.getZoneCellWidth()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneCellHeight"><fmt:message key="mvnad.common.zone.cell_height"/><span class="requiredfield"> *</span></label><br /></td>
    <td><input type="text" id="ZoneCellHeight" name="ZoneCellHeight" size="13" value="<%=adZoneBean.getZoneCellHeight()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneCellHorizontalCount"><fmt:message key="mvnad.common.zone.cell_horizontal_count"/><span class="requiredfield"> *</span></label><br /></td>
    <td><input type="text" id="ZoneCellHorizontalCount" name="ZoneCellHorizontalCount" size="13" value="<%=adZoneBean.getZoneCellHorizontalCount()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneCellVerticalCount"><fmt:message key="mvnad.common.zone.cell_vertical_count"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="ZoneCellVerticalCount" name="ZoneCellVerticalCount" size="13" value="<%=adZoneBean.getZoneCellVerticalCount()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneMaxBanners"><fmt:message key="mvnad.common.zone.max_banners"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="ZoneMaxBanners" name="ZoneMaxBanners" size="13" value="<%=adZoneBean.getZoneMaxBanners()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneDirection"><fmt:message key="mvnad.common.zone.direction"/><span class="requiredfield"> *</span></label><br /></td>
    <td>
      <select id="ZoneDirection" name="ZoneDirection">
        <option value="<%=ZoneBean.ZONE_DIRECTION_HORIZONTAL%>"<%if (adZoneBean.getZoneDirection() == ZoneBean.ZONE_DIRECTION_HORIZONTAL) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.direction.horizontal"/></option>
        <option value="<%=ZoneBean.ZONE_DIRECTION_VERTICAL%>"<%if (adZoneBean.getZoneDirection() == ZoneBean.ZONE_DIRECTION_VERTICAL) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.direction.vertical"/></option>          
      </select>
    </td>
  </tr>
    <tr class="<mvn:cssrow/>">
      <td nowrap="nowrap"><label for="ZoneType"><fmt:message key="mvnad.common.zone.type"/><span class="requiredfield"> *</span></label></td>
      <td>
        <select id="ZoneType" name="ZoneType">
          <option value="<%=ZoneBean.ZONE_TYPE_NORMAL%>"<%if (adZoneBean.getZoneType() == ZoneBean.ZONE_TYPE_NORMAL) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.type.normal"/></option>
          <option value="<%=ZoneBean.ZONE_TYPE_DIRECT_CODE%>"<%if (adZoneBean.getZoneType() == ZoneBean.ZONE_TYPE_DIRECT_CODE) {%> selected="selected"<%}%>><fmt:message key="mvnad.common.zone.type.direct_code"/></option>
        </select>
      </td>
    </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ZoneAutoReloadTime"><fmt:message key="mvnad.common.zone.auto_reload_time"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="ZoneAutoReloadTime" name="ZoneAutoReloadTime" size="13" value="<%=adZoneBean.getZoneAutoReloadTime()%>" />&nbsp;<fmt:message key="mvnad.common.zone.auto_reload_time_info"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><label for="MemberCurrentMatkhau"><fmt:message key="mvnad.common.ad.your_pass"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event)"/></td>
  </tr>
  <tr class="portlet-section-footer" align="center">
    <td colspan="2">
      <input type="button" value="<fmt:message key="mvnad.common.action.save"/>" class="portlet-form-button" onclick="SubmitForm()" name="submitbutton" />
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
