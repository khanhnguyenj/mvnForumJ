<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/deletenonactivatedmembers.jsp,v 1.66 2009/08/13 09:11:57 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.66 $
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
 - @author: Phong, Ta Quoc  
 - @author: Minh Nguyen     
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="net.myvietnam.mvncore.util.*" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.deletenonactivatedmembers.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<%@ include file="header.jsp"%>
<br/>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) {
    return false;
  }

  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}

function checkEnter(event) {
  var agt=navigator.userAgent.toLowerCase();

  // Maybe, Opera make an onClick event when user press enter key
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitForm();
}
//]]>
</script>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.deletenonactivatedmembers.title"/>
</div>

<br/>
<%
Collection memberBeans = (Collection) request.getAttribute("DeleteMembers");
%>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.deletenonactivatedmembers.members_to_be_deleted"/> (<%=memberBeans.size()%>):</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.member.id"/></td>
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next(); %>
  <tr class="<mvn:cssrow/>">
    <td><b><%=memberBean.getMemberID()%></b></td>
    <td nowrap="nowrap">
      <%if (memberBean.getMemberStatus() != 0) {%>
        <a class="disabledItem"
      <%} else {%>
        <a class="memberName"
      <%}%>
        href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>
    </td>
    <td nowrap="nowrap"><%=memberBean.getMemberLastname()%>&nbsp;<%=memberBean.getMemberFirstname()%></td>
    <td nowrap="nowrap"><%=memberBean.getMemberEmail()%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%></td>
  </tr>
<%
}//for
if (memberBeans.size() == 0) { %>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" align="center">
      <fmt:message key="mvnforum.admin.deletenonactivatedmembers.table.no_member"/>.&nbsp;&nbsp;
      <input type="reset" value="<fmt:message key="mvnforum.common.action.go_back"/>" onclick="javascript:history.back(1)" class="liteoption" />
    </td>
  </tr>
<% } %>
</mvn:cssrows>
</table>
<br/>
<% if (memberBeans.size() != 0) { %>
<% int days = ParamUtil.getParameterUnsignedInt(request, "days");%>
<form action="<%=urlResolver.encodeURL(request, response, "deletenonactivatedmembersprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "deletenonactivatedmembersprocess")%>
<mvn:securitytoken />
<input type="hidden" name="days" value="<%=days%>" />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.deletenonactivatedmembers.confirmation"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      <fmt:message key="mvnforum.admin.deletenonactivatedmembers.info1">
        <fmt:param><%=days%></fmt:param>
      </fmt:message>
      <br />
      <fmt:message key="mvnforum.admin.deletenonactivatedmembers.info2">
        <fmt:param><%=memberBeans.size()%></fmt:param>
      </fmt:message>
      <br />
      <fmt:message key="mvnforum.admin.deletenonactivatedmembers.warning"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="deletesentmessages"><fmt:message key="mvnforum.admin.deletemember.delete_sent_message"/>:</label></td>
    <td><input type="checkbox" id="deletesentmessages" name="deletesentmessages" class="noborder" value="yes" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td align="center" colspan="2">
    <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.deletenonactivatedmembers.button.confirm_delete"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    <input type="reset" value="<fmt:message key="mvnforum.common.action.go_back"/>" onclick="javascript:gotoPage('<%=urlResolver.encodeURL(request, response, "usermanagement")%>');" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<% } %>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>