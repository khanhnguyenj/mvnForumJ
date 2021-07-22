<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/addmember.jsp,v 1.82 2009/08/13 05:28:03 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.82 $
 - $Date: 2009/08/13 05:28:03 $
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

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.addmember.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberName, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;
  if (isBlank(document.submitform.MemberMatkhau, "<fmt:message key="mvnforum.common.member.password"/>")) return false;

  if (document.submitform.MemberMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberMatkhau.focus();
    return false;
  }

  if (isBlank(document.submitform.MemberMatkhauConfirm, "<fmt:message key="mvnforum.common.member.password"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;
  if (document.submitform.MemberMatkhau.value!=document.submitform.MemberMatkhauConfirm.value) {
    alert("<fmt:message key="mvnforum.common.member.password"/> <fmt:message key="mvnforum.common.js.prompt.notmatch"/>"); 
    return false;
  }

  if (isBlank(document.submitform.MemberEmail, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  if (isBlank(document.submitform.MemberEmailConfirm, "<fmt:message key="mvnforum.common.member.email"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;
  if (!isEmail(document.submitform.MemberEmail, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  if (!isEmail(document.submitform.MemberEmailConfirm, "<fmt:message key="mvnforum.common.member.email"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;
  if (document.submitform.MemberEmail.value != document.submitform.MemberEmailConfirm.value ) {
    alert("<fmt:message key="mvnforum.common.member.email"/> <fmt:message key="mvnforum.common.js.prompt.notmatch"/>"); 
    return false;
  }
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.addmember.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.addmember.info"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "addmemberprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "addmemberprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.addmember.enter_information"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberName"><fmt:message key="mvnforum.common.member.login_name"/></label><span class="requiredfield"> *</span></td>
    <td><input type="text" size="60" id="MemberName" name="MemberName" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMatkhau"><fmt:message key="mvnforum.common.member.password"/></label><span class="requiredfield"> *</span></td>
    <td><input type="password" size="60" id="MemberMatkhau" name="MemberMatkhau" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMatkhauConfirm"><fmt:message key="mvnforum.common.member.password"/> (<fmt:message key="mvnforum.common.retype"/>)</label><span class="requiredfield"> *</span></td>
    <td><input type="password" size="60" id="MemberMatkhauConfirm" name="MemberMatkhauConfirm" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberEmail"><fmt:message key="mvnforum.common.member.email"/></label><span class="requiredfield"> *</span></td>
    <td><input type="text" size="60" id="MemberEmail" name="MemberEmail" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberEmailConfirm"><fmt:message key="mvnforum.common.member.email"/> (<fmt:message key="mvnforum.common.retype"/>)</label><span class="requiredfield"> *</span></td>
    <td><input type="text" size="60" id="MemberEmailConfirm" name="MemberEmailConfirm" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.addmember.button.pre_register"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
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
