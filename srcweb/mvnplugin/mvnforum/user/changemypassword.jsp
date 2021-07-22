<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/changemypassword.jsp,v 1.24 2009/08/13 05:28:02 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.24 $
 - $Date: 2009/08/13 05:28:02 $
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.changemypassword.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
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
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
      pw2md5(document.submitform.MemberMatkhau, document.submitform.md5pwnew);
      pw2md5(document.submitform.MemberMatkhauConfirm, document.submitform.md5pwconfirm);
    }
    <mvn:servlet>
    document.submitform.submitbutton.disabled=true;
    </mvn:servlet>
    document.submitform.submit();
  }
}

function ValidateForm() {
  var field,temp;

  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) return false;
  if (document.submitform.MemberCurrentMatkhau.value.length <3) {
    alert(" <fmt:message key="mvnforum.common.prompt.current_password"/>:  <fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }

  field=document.submitform.MemberMatkhau;
  temp=field.value
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.new_password"/>")) return false;

  //Check Password's length
  if (temp.length < 3) {
    alert("<fmt:message key="mvnforum.common.member.new_password"/>: <fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    field.focus();
    return false;
  }

  field=document.submitform.MemberMatkhauConfirm;
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.new_password"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;

  if (temp!=field.value) {
    alert("<fmt:message key="mvnforum.common.member.new_password"/> (<fmt:message key="mvnforum.common.retype"/>) <fmt:message key="mvnforum.common.js.prompt.notmatch"/>");
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
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.changemypassword.title"/>
</div>
<br/>

<%if (onlineUser.isPasswordExpired()) {%>
<div class="pagedesc center warning">
  <fmt:message key="mvnforum.user.changemypassword.warning">
    <fmt:param><%=MVNForumConfig.getMaxPasswordDays()%></fmt:param>
  </fmt:message>
</div>
<br/>
<%}%>

<form action="<%=urlResolver.encodeURL(request, response, "changemypasswordprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "changemypasswordprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value=""/>
<input type="hidden" name="md5pwnew" value=""/>
<input type="hidden" name="md5pwconfirm" value=""/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.changemypassword.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40%"><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMatkhau"><fmt:message key="mvnforum.common.member.new_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberMatkhau" name="MemberMatkhau"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMatkhauConfirm"><fmt:message key="mvnforum.common.member.new_password"/> (<fmt:message key="mvnforum.common.retype"/>)<span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberMatkhauConfirm" name="MemberMatkhauConfirm" onkeypress="checkEnter(event);"/></td>
  </tr>
  <tr>
    <td class="portlet-section-footer" colspan="2" align="center">
      <input name="submitbutton" type="button" value="<fmt:message key="mvnforum.user.changemypassword.button.change_password"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
