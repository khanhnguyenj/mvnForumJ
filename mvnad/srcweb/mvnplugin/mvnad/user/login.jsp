<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/user/login.jsp,v 1.17 2009/08/13 10:53:59 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.17 $
 - $Date: 2009/08/13 10:53:59 $
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

<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.login.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/md5.js"></script>
<%@ include file="header.jsp"%>

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
      pw2md5(document.submitform.MemberMatkhau, document.submitform.md5pw);
    }
  <mvn:servlet>
    document.submitform.submitbutton.disabled=true;
  </mvn:servlet>
    return document.submitform.submit();
  }
  return false;
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberName, "<fmt:message key="mvnad.common.member.login_name"/>")) return false;
  if (isBlank(document.submitform.MemberMatkhau, "<fmt:message key="mvnad.common.member.password"/>")) return false;
  //Check Password's length
  if (document.submitform.MemberMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnad.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.user.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.login.title"/>
</div>
<br />

<%
String errorMessage = ParamUtil.getAttribute(request, "Reason");
%>
<% if (errorMessage.length() > 0) { %>
<div class="pagedesc">
  <fmt:message key="mvnad.common.ad.message"/> <span class="warning"><%=errorMessage%></span>
  <%if (isPortlet && MVNForumConfig.getRedirectLoginURL().equals(MVNForumConfig.DEFAULT) == false) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "redirectlogin", URLResolverService.ACTION_URL)%>" class="command">Login</a>
  <%}%>
  <br/>
</div>
<br />
<% } %>
<% if (internalUserDatabase) {%>
<div class="pagedesc">
  <fmt:message key="mvnad.admin.login.info"/><br/>
</div>
<%}%>

<%if (MVNForumConfig.getEnableLogin()) {%>
<br/>
<form action="<%=urlResolver.encodeURL(request, response, "loginprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "loginprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.user.login.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40%"><label for="MemberName"><fmt:message key="mvnad.common.member.login_name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="MemberName" name="MemberName" value="<%=ParamUtil.getAttribute(request, "MemberName")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMatkhau"><fmt:message key="mvnad.common.member.password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberMatkhau" name="MemberMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnad.common.action.login"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>

<script type="text/javascript">
//<![CDATA[
    document.submitform.MemberName.focus();
//]]>
</script>
<%}%>
<br />

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>