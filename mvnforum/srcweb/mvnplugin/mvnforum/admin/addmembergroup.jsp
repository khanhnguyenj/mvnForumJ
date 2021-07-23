<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/addmembergroup.jsp,v 1.88 2009/08/13 09:11:57 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.88 $
 - $Date: 2009/08/13 09:11:57 $
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
<%@ page import="java.util.*" %>
<%@ page import="com.mvnforum.db.GroupsBean" %>
<%@ page import="com.mvnforum.db.MemberGroupBean" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.addmembergroup.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
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
    if (ValidateExpire()) {
      var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
      if (enableEncrypted) {
        pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
      }
      <mvn:servlet>
        document.submitform.submitbutton.disabled=true;
      </mvn:servlet>  
      return document.submitform.submit();
    }
  }
  return false;
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberNames, "<fmt:message key="mvnforum.common.members"/>")) return false;
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) return false;
  if (document.submitform.MemberCurrentMatkhau.value.length <3 ) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  
  return true;
}
var expiretype = 0;
function ValidateExpire() {
  if (expiretype == 1) {
    if (isBlank(document.submitform.day, "<fmt:message key="mvnforum.common.date.day"/>")) return false;
    if (isBlank(document.submitform.month, "<fmt:message key="mvnforum.common.date.month"/>")) return false;
    if (isBlank(document.submitform.year, "<fmt:message key="mvnforum.common.date.year"/>")) return false;
  }
  return true;
}
           
function chooseExpireType(type) {
  expiretype = type;
  if (type == 0) {
    document.getElementById("chooseexpiredate").style.display = 'none';
  } else if (type == 1) {
    document.getElementById("chooseexpiredate").style.display = '';
  }
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>
<%
GroupsBean groupBean = (GroupsBean)request.getAttribute("GroupsBean");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "groupmanagement")%>"><fmt:message key="mvnforum.admin.groupmanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.group"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=" + groupBean.getGroupID())%>"><%=groupBean.getGroupName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.addmembergroup.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.addmembergroup.info"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "addmembergroupprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "addmembergroupprocess")%>
<mvn:securitytoken />
<input type="hidden" name="group" value="<%=groupBean.getGroupID()%>" />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.addmembergroup.enter_information"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><label for="MemberNames"><fmt:message key="mvnforum.common.members"/><span class="requiredfield"> *</span></label></td>
    <td><textarea cols="60" rows="10" id="MemberNames" name="MemberNames"></textarea></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><label for="Privilege"><fmt:message key="mvnforum.common.group.privilege"/>:</label></td>
    <td>
      <select id="Privilege" name="Privilege" size="1">
        <option value="0" selected="selected"><fmt:message key="mvnforum.common.group.privilege.no_privilege"/></option>
        <%--
        <option value="1"><fmt:message key="mvnforum.common.group.privilege.moderator"/></option>
        <option value="2"><fmt:message key="mvnforum.common.group.privilege.admin"/></option>
        --%>
      </select>
      <fmt:message key="mvnforum.admin.addmembergroup.help.privilege_for_all"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.date.expire_date"/><span class="requiredfield"> *</span></td>
    <td>
      <table class="noborder" cellpadding="3" cellspacing="0" width="100%">
        <tr>
          <td>
            <input name="expiretype" value="0" class="noborder" type="radio" onclick="chooseExpireType(0);" checked="checked" /> <fmt:message key="mvnforum.common.status.never_expired"/>&nbsp;&nbsp;
            <input name="expiretype" value="1" class="noborder" type="radio" onclick="chooseExpireType(1);" /> <fmt:message key="mvnforum.common.date.expire_date"/> (dd/mm/yyyy)
          </td>            
        </tr>
        <tr>               
          <td style="display: none" id="chooseexpiredate">
            <%@ include file="inc_date_option.jsp"%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.add"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
