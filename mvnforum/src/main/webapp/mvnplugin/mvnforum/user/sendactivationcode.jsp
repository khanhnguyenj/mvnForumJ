<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/sendactivationcode.jsp,v 1.68 2009/08/13 07:24:43 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.68 $
 - $Date: 2009/08/13 07:24:43 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.sendactivationcode.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
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
  if (isBlank(document.submitform.MemberEmail, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  if (!isEmail(document.submitform.MemberEmail, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  return true;
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.sendactivationcode.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "sendactivationcodeprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "sendactivationcodeprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.sendactivationcode.prompt"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40%"><label for="MemberName"><fmt:message key="mvnforum.common.member.login_name"/><span class="requiredfield"> *</span> </label></td>
    <td><input type="text" id="MemberName" name="MemberName" value="<%=ParamUtil.getParameterFilter(request, "member")%>" size="30"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberEmail"><fmt:message key="mvnforum.common.member.email"/><span class="requiredfield"> *</span> </label></td>
    <td><input type="text" id="MemberEmail" name="MemberEmail" value="<%=ParamUtil.getParameterFilter(request, "email")%>" size="30"/></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
    <input name="submitbutton" type="button" value="<fmt:message key="mvnforum.user.sendactivationcode.button.send_activation_code"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
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

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
