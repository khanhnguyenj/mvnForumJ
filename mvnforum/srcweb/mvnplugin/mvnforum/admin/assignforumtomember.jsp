<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/assignforumtomember.jsp,v 1.93 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.93 $
 - $Date: 2009/07/16 03:21:12 $
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
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.auth.AbstractPermission" %>
<%@ page import="com.mvnforum.MyUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.assignforumtomember.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
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
  if (document.submitform.forum.value == '') {
    alert("Please choose a forum to continue.");
    return false;
  }
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<%
ForumCache forumCache = ForumCache.getInstance();

MemberBean memberBean = (MemberBean) request.getAttribute("MemberBean");
Collection memberForumBeans = (Collection)request.getAttribute("MemberForumBeans");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.assignforumtomember.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "editmemberforumpermission")%>" name="submitform">
<%=urlResolver.generateFormAction(request, response, "editmemberforumpermission")%>
<input type="hidden" name="memberid" value="<%=memberBean.getMemberID()%>" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td width="50%" align="center"><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td width="50%" align="center"><fmt:message key="mvnforum.common.forum"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="center"><b><%=memberBean.getMemberName()%></b></td>
    <td align="center">
<%--
      <select name="forum" class="liteoption">
        <option value="">Choose Forum</option>
<%
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    int forumsInCurrentCat = forumCache.getNumberOfBeans(categoryID);
    if (forumsInCurrentCat == 0) continue; %>
        <option value=""></option>
        <option value=""><%=categoryBean.getCategoryName()%></option>
        <option value="">---------------------------------</option>
    <%
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumBean.getForumID())) { %>
        <option value="<%=forumBean.getForumID()%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=forumBean.getForumName()%></option>
<%      } //if
    } // for forum
}// for category %>
      </select>
--%>
<%= request.getAttribute("Result") %>
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.assignforumtomember.title"/>" onclick="JavaScript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</table>
</form>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.assignforumtomember.current_permission_for_member"/> "<%=memberBean.getMemberName()%>":</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td width="60%"><fmt:message key="mvnforum.common.forum.forum_name"/></td>
    <td width="40%" align="center"><fmt:message key="mvnforum.common.permission"/></td>
  </tr>
  <tr class="portlet-section-subheader">
    <td colspan="2" align="center"><b><fmt:message key="mvnforum.common.permission.combined_permission"/></b></td>
  </tr>
<%
  boolean hasCombinedPerms = false;
  int[] allCombinedPerms = AbstractPermission.forumCombinedPermissionArray;

  for (int i = 0; i < allCombinedPerms.length; i++) {
    int perm = allCombinedPerms[i];
    for (Iterator iterator = memberForumBeans.iterator(); iterator.hasNext(); ) {
      MemberForumBean memberForumBean = (MemberForumBean)iterator.next();
      if (memberForumBean.getPermission() == perm) {
        hasCombinedPerms = true;
%>
  <tr class="<mvn:cssrow/>">
    <td><%=forumCache.getBean(memberForumBean.getForumID()).getForumName()%></td>
    <td align="center">
      <%=AbstractPermission.getDescription(perm)%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<%=MyUtil.getIndividualPermission(perm, false) %>')" onmouseout="return nd();" />
    </td>
  </tr>
<%
      }//if
    }//for
  }
  if (!hasCombinedPerms) {
%>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center"><fmt:message key="mvnforum.common.permission.no_combined_permission"/></td>
  </tr>

<% } %>
  <tr class="portlet-section-subheader">
    <td colspan="2" align="center"><b><fmt:message key="mvnforum.common.permission.individual_permission"/></b></td>
  </tr>
<%
  boolean hasIndividualPerms = false;
  int[] allIndividualPerms = AbstractPermission.forumIndividualPermissionArray;
  for (int i = 0; i < allIndividualPerms.length; i++) {
    int perm = allIndividualPerms[i];
    for (Iterator iterator = memberForumBeans.iterator(); iterator.hasNext(); ) {
      MemberForumBean memberForumBean = (MemberForumBean)iterator.next();
      if (memberForumBean.getPermission() == perm) {
        hasIndividualPerms = true;
%>
  <tr class="<mvn:cssrow/>">
    <td><%=forumCache.getBean(memberForumBean.getForumID()).getForumName()%></td>
    <td align="center"><%=AbstractPermission.getDescription(perm)%></td>
  </tr>
<%
      }
    }
  }
%>
<%
  if (hasIndividualPerms == false) {
%>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center"><fmt:message key="mvnforum.common.permission.no_invidual_permission"/></td>
  </tr>
<% } %>
</mvn:cssrows>
</table>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>