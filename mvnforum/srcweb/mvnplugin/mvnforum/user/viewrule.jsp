<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/viewrule.jsp,v 1.11 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.11 $
 - $Date: 2009/07/16 03:21:13 $
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
 - @author: Xuan, Tran Le
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.viewrule.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    document.submitform.submit();
  } else {
    alert("<fmt:message key="mvnforum.user.viewrule.prompt"/>");
  }
}

function ValidateForm() {
  return document.submitform.agree.checked;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.viewrule.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "registermember", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "registermember")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td>
      <fmt:message key="mvnforum.user.viewrule.prompt"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td style="width: 100%; height: 100%;" align="center">
    <%String ruleFile = (String) request.getAttribute("RuleFile");%>
    <iframe src="<%=ruleFile%>" frameborder="0" height="300" width="100%"> </iframe>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="center">
      <input type="checkbox" name="agree" value="yes" class="noborder"/> <fmt:message key="mvnforum.user.viewrule.agree"/>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td align="center">
      <input type="button" class="portlet-form-button" value="<fmt:message key="mvnforum.common.action.register"/>" onclick="javascript:SubmitForm();" />
      <input type="button" class="liteoption" value="<fmt:message key="mvnforum.common.action.cancel"/>" onclick="javascript:gotoPage('<%=urlResolver.encodeURL(request, response, "index")%>');" />
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
