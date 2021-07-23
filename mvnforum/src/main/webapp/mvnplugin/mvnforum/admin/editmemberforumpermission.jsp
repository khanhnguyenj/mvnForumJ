<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/editmemberforumpermission.jsp,v 1.88 2009/08/13 09:11:57 thonh Exp $
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
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.auth.AbstractPermission" %>
<%@ page import="com.mvnforum.MyUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.editmemberforumpermission.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript">
//<![CDATA[
function doAdd() {
  if (validatePassword()) {
    document.submitform.btnAction.value = "Add";
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }
    document.submitform.submit();
  }
}
function doRemove() {
  if (validatePassword()) {
    document.submitform.btnAction.value = "Remove";
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }
    document.submitform.submit();
  }
}
function validatePassword() {
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
//]]>
</script>
</mvn:head>
<mvn:body>
<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
ForumBean forumBean = (ForumBean)request.getAttribute("ForumBean");
int[] currentPerms = (int[])request.getAttribute("CurrentPermissions");
%>

<% if (Boolean.TRUE.equals(request.getAttribute("UserZone"))) { %>
 
<%@ include file="../user/header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprivateforums")%>"><fmt:message key="mvnforum.user.myprivateforumsx.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.editmemberforumpermission.title"/>
</div>
<br/>
<% } else {%>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.editmemberforumpermission.title"/>
</div>

<% } %>
<br/>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.editmemberforumpermission.title"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.common.permission.current_member"/>:</td>
    <td><b><%=memberBean.getMemberName()%></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.common.permission.current_forum"/>:</td>
    <td><b><%=forumBean.getForumName()%></b></td>
  </tr>
</table>
</mvn:cssrows>
<br/>
<form action="<%=urlResolver.encodeURL(request, response, "updatememberforumpermission", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "updatememberforumpermission")%>
<mvn:securitytoken />
<input type="hidden" name="memberid" value="<%=memberBean.getMemberID()%>"/>
<input type="hidden" name="forum" value="<%=forumBean.getForumID()%>"/>
<input type="hidden" name="btnAction" value="" />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.permission"/></td>
    <td width="20%" align="center"><fmt:message key="mvnforum.common.permission.current_status"/></td>
    <td width="20%" align="center"><fmt:message key="mvnforum.common.action.add"/></td>
    <td width="20%" align="center"><fmt:message key="mvnforum.common.action.remove"/></td>
  </tr>
  <tr class="portlet-section-subheader">
    <td colspan="4" align="center"><b><fmt:message key="mvnforum.common.permission.combined_permission"/></b></td>
  </tr>
<%
int[] allCombinedPerms = AbstractPermission.forumCombinedPermissionArray;
%>
<mvn:cssrows>
<%
for (int i = 0; i < allCombinedPerms.length; i++) {
    int perm = allCombinedPerms[i];

    boolean havePerm = false;
    for (int j = 0; j < currentPerms.length; j++) {
        if (currentPerms[j] == perm) {
            havePerm = true;
            break;
        }
    }
%>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <%=AbstractPermission.getDescription(perm)%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<%=MyUtil.getIndividualPermission(perm, false) %>')" onmouseout="return nd();" alt=""/>
    </td>
    <% if (havePerm) {%>
    <td align="center"><font color="#008000"><fmt:message key="mvnforum.common.yes"/></font></td>
    <td align="center"></td>
    <td align="center"><input type="checkbox" name="remove" value="<%=perm%>" class="noborder"/></td>
    <% } else {%>
    <td align="center"><font color="#FF0080"><fmt:message key="mvnforum.common.no"/></font></td>
    <td align="center"><input type="checkbox" name="add" value="<%=perm%>" class="noborder"/></td>
    <td align="center"></td>
    <% }%>
  </tr>
<%
} //for
%>
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="4" align="center"><b><fmt:message key="mvnforum.common.permission.individual_permission"/></b></td>
  </tr>
<%
int[] allIndividualPerms = AbstractPermission.forumIndividualPermissionArray;
%>
<mvn:cssrows>
<%
for (int i = 0; i < allIndividualPerms.length; i++) {
    int perm = allIndividualPerms[i];

    boolean havePerm = false;
    for (int j = 0; j < currentPerms.length; j++) {
        if (currentPerms[j] == perm) {
            havePerm = true;
            break;
        }
    }
%>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><%=AbstractPermission.getDescription(perm)%></td>
    <% if (havePerm) {%>
    <td align="center"><font color="#008000"><fmt:message key="mvnforum.common.yes"/></font></td>
    <td align="center"></td>
    <td align="center"><input type="checkbox" name="remove" value="<%=perm%>" class="noborder"/></td>
    <% } else {%>
    <td align="center"><font color="#FF0080"><fmt:message key="mvnforum.common.no"/></font></td>
    <td align="center"><input type="checkbox" name="add" value="<%=perm%>" class="noborder"/></td>
    <td align="center"></td>
    <% }%>
  </tr>
<%
} //for
%>
</mvn:cssrows>
  <tr class="portlet-section-footer">
    <td colspan="2" align="right"><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label> <input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="20"/></td>
    <!--
    DO NOT use onclick="disabled=true; submit();" with 2 button below, because it will remove the 'name' attribute
    -->
    <td align="center"><input type="button" onclick="doAdd();" value="<fmt:message key="mvnforum.common.action.add"/>" class="portlet-form-button"/></td>
    <td align="center"><input type="button" onclick="doRemove();" value="<fmt:message key="mvnforum.common.action.remove"/>" class="portlet-form-button"/></td>
  </tr>
</table>
</form>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>