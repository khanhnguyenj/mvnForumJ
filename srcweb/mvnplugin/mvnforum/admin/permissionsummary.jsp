<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/permissionsummary.jsp,v 1.47 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.47 $
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
 - @author: Minh Nguyen  
 - @author: Mai  Nguyen  
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.permissionsummary.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<%
Collection authorizedMembers = (Collection)request.getAttribute("AuthorizedMembers");
Collection forumsAuthorizedMembers = (Collection)request.getAttribute("ForumsAuthorizedMembers");
Collection authorizedGroups = (Collection)request.getAttribute("AuthorizedGroups");
Collection forumAuthorizedGroups = (Collection)request.getAttribute("ForumAuthorizedGroups");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.permissionsummary.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.permissionsummary.info"/><br/>
</div>

<br/>
<%if (memberDAO.isSupportGetAuthorizedMembers()) { %>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.permissionsummary.member_having_global_permission"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.change_permission"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.view_permission"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = authorizedMembers.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
  <tr class="<mvn:cssrow/>">
    <td><a class="memberName" href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a></td>
    <td><%=memberBean.getMemberFirstname()%>&nbsp;<%=memberBean.getMemberLastname()%></td>
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
      <%}%>
    </td>
    <td align="center">
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%>
      <%}%>
    </td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editmemberpermission?memberid=" + memberBean.getMemberID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Permissions for member : <%=memberBean.getMemberName()%> " /></a></td>
    <td align="center"><a class="command" href="<%=urlResolver.encodeURL(request, response, "viewmemberpermissions?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.permissionsummary.view_permission"/></a></td>
  </tr>
<%
}//for
if (authorizedMembers.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="6" align="center"><fmt:message key="mvnforum.admin.permissionsummary.no_member_having_global_permission"/>.</td></tr>
<% } //if%>
</table>
</mvn:cssrows>
<br/>
<%} %>
<%if (memberDAO.isSupportGetForumsAuthorizedMembers()) { %>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.permissionsummary.member_having_forum_specific_permission"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.change_permission"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.view_permission"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = forumsAuthorizedMembers.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
  <tr class="<mvn:cssrow/>">
    <td><a class="memberName" href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a></td>
    <td><%=memberBean.getMemberFirstname()%>&nbsp;<%=memberBean.getMemberLastname()%></td>
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
      <%}%>
    </td>
    <td align="center">
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%>
      <%}%>
    </td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "assignforumtomember?memberid=" + memberBean.getMemberID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Permissions for member : <%=memberBean.getMemberName()%> " /></a></td>
    <td align="center"><a class="command" href="<%=urlResolver.encodeURL(request, response, "viewmemberpermissions?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.permissionsummary.view_permission"/></a></td>
  </tr>
<%
}//for
if (forumsAuthorizedMembers.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="6" align="center"><fmt:message key="mvnforum.admin.permissionsummary.no_member_having_forum_specific_permission"/>.</td></tr>
<% } //if%>
</table>
</mvn:cssrows>
<br/>
<%} %>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.permissionsummary.group_having_global_permission"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.group.group_name_desc"/></td>
    <%-- td align="center"><fmt:message key="mvnforum.common.group.group_owner_name"/></td --%>
    <td align="center"><fmt:message key="mvnforum.common.date.create_date"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.change_permission"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = authorizedGroups.iterator(); iterator.hasNext(); ) {
    GroupsBean groupBean = (GroupsBean)iterator.next();
    int groupID = groupBean.getGroupID();
%>
  <tr class="<mvn:cssrow/>">
    <td>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=" + groupID)%>"><%=groupBean.getGroupName()%></a><br/>
      <%=groupBean.getGroupDesc()%>
    </td>
<%--     
    <td align="center">
      <%
      if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) { %>
          <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {//normal group
          if (groupBean.getGroupOwnerID() == 0) { %>
              <fmt:message key="mvnforum.common.group.no_owner"/>
      <%  } else {
              out.print(groupBean.getGroupOwnerName());
          }
      }
      %>
    </td>
--%>    
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(groupBean.getGroupCreationDate())%>
    </td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editgrouppermission?group=" + groupID)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Permissions for group : <%=groupBean.getGroupName()%> " /></a></td>
  </tr>
<%
}//for
if (authorizedGroups.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="3" align="center"><fmt:message key="mvnforum.admin.permissionsummary.no_group_having_global_permission"/>.</td></tr>
<% } //if%>
</table>
</mvn:cssrows>
<br/>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.admin.permissionsummary.group_having_forum_specific_permission"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.group.group_name_desc"/></td>
    <td align="center"><fmt:message key="mvnforum.common.group.group_owner_name"/></td>
    <td align="center"><fmt:message key="mvnforum.common.date.create_date"/></td>
    <td align="center"><fmt:message key="mvnforum.admin.permissionsummary.change_permission"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = forumAuthorizedGroups.iterator(); iterator.hasNext(); ) {
    GroupsBean groupBean = (GroupsBean)iterator.next();
    int groupID = groupBean.getGroupID();
%>
  <tr class="<mvn:cssrow/>">
    <td>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=" + groupID)%>"><%=groupBean.getGroupName()%></a><br/>
      <%=groupBean.getGroupDesc()%>
    </td>
    <td align="center">
      <%
      if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) { %>
          <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {//normal group
          if (groupBean.getGroupOwnerID() == 0) { %>
              <fmt:message key="mvnforum.common.group.no_owner"/>
      <%  } else {
              out.print(groupBean.getGroupOwnerName());
          }
      }
      %>
    </td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(groupBean.getGroupCreationDate())%>
    </td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "assignforumtogroup?group=" + groupID)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Permissions for group : <%=groupBean.getGroupName()%> " /></a></td>
  </tr>
<%
}//for
if (forumAuthorizedGroups.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="4" align="center"><fmt:message key="mvnforum.admin.permissionsummary.no_group_having_forum_specific_permission"/>.</td></tr>
<% } //if%>
</table>
</mvn:cssrows>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>