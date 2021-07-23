<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/viewmemberpermissions.jsp,v 1.54 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.54 $
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
<%@ page import="com.mvnforum.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.viewmemberpermissions.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
Collection groups = (Collection)request.getAttribute("MemberGroups");
MVNForumPermission perm = (MVNForumPermission)request.getAttribute("Permissions");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.viewmemberpermissions.title"/>
</div>

<br/>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.member.header.member_info"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.common.member.id"/></td>
    <td><%=memberBean.getMemberID()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><b><%=memberBean.getMemberName()%></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><%=memberBean.getMemberFirstname()%>&nbsp;<%=memberBean.getMemberLastname()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td><%=memberBean.getMemberEmail()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.admin.viewmemberpermissions.groups_member_belong_to"/>: <b><%=groups.size() + 1%></b></td>
    <td><b>Member</b> (<a class="command" href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=2")%>"><fmt:message key="mvnforum.admin.viewgroup.title"/></a>)<br/>
    <%
      for (Iterator groupIter = groups.iterator(); groupIter.hasNext(); ) { 
        GroupsBean group = (GroupsBean)groupIter.next(); %>
        <b><%=group.getGroupName()%></b>
        (<a class="command" href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=" + group.getGroupID())%>"><fmt:message key="mvnforum.admin.viewgroup.title"/></a> - <a class="command" href="<%=urlResolver.encodeURL(request, response, "listmembergroup?group=" + group.getGroupID())%>"><fmt:message key="mvnforum.admin.listmembergroup.title"/></a>)
        <br/>
    <%}%>
    </td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight" colspan="19"><fmt:message key="mvnforum.common.permission.global_permissions"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="80%"><fmt:message key="mvnforum.common.permission"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission.current_status"/></td>
  </tr>
<mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.common.permission.special_permissions"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Activated</td>
    <td align="center"><%if (perm.isActivated()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
</mvn:cssrows>
  <%--tr class="<mvn:cssrow/>">
    <td>Authenticated</td>
    <td align="center"><%if (perm.isAuthenticated()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr--%>
<mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.common.permission.forum_category_permissions"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Add Category</td>
    <td align="center"><%if (perm.canAddCategory()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Edit Category</td>
    <td align="center"><%if (perm.canEditCategory()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Delete Category</td>
    <td align="center"><%if (perm.canDeleteCategory()) {%> <font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Add Forum</td>
    <td align="center"><%if (perm.canAddForum()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.common.permission.other_permissions"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td>Admin System</td>
    <td align="center"><%if (perm.canAdminSystem()) {%> <font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <%--<tr class="<mvn:cssrow/>">
    <td>Login</td>
    <td align="center"><%if (perm.canLogin()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>--%>
  <tr class="<mvn:cssrow/>">
    <td>Send Mail</td>
    <td align="center"><%if (perm.canSendMail()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Use Avatar</td>
    <td align="center"><%if (perm.canUseAvatar()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Use Private Message</td>
    <td align="center"><%if (perm.canUseMessage()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Add Attachment in Private Message</td>
    <td align="center"><%if (perm.canAddMessageAttachment()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
</mvn:cssrows>
</table>

<%if (environmentService.getCmsRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>
<br/>
<table width="95%" align="center">
  <tr>
    <td class="highlight" colspan="19"><fmt:message key="mvnforum.common.permission.cms_permissions"/>:</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="80%"><fmt:message key="mvnforum.common.permission"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission.current_status"/></td>
  </tr>
<mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.common.permission.worklow_permissions"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>Publish Contents</td>
    <td align="center"><%if (perm.canPublishContentInAnyChannel()) {%><font color="#008000"><fmt:message key="mvnforum.common.yes"/><%} else {%><font color="#FF0080"><fmt:message key="mvnforum.common.no"/><%}%></font></td>
  </tr>
</mvn:cssrows>
</table>
<%}%>

<br/>
<table width="95%" align="center">
  <tr>
    <td class="highlight" colspan="19"><fmt:message key="mvnforum.common.permission.forum_specific_permissions"/>:</td>
  </tr>
</table>
<%--
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header" align="center">
    <td>Forum</td>
    <td>Edit</td>
    <td>Delete</td>
    <td>Assign</td>
    
    <td>Thread</td>
    <td>Add</td>
    <td>Moderate</td>
    
    <td>Post</td>
    <td>Read</td>
    <td>Add</td>
    <td>Delete</td>
    <td>Edit Own</td>
    <td>Edit Any</td>
    
    <td>Attachment</td>
    <td>Add</td>
    <td>Get</td>
    
    <td>Poll</td>
    <td>Add</td>
    <td>Edit</td>
    <td>Delete</td>
  </tr>
<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
%>
<%
for (Iterator catIterator = categoryBeans.iterator(); catIterator.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIterator.next();
%>
   <tr class="portlet-section-subheader"><td colspan="19"><b><%=categoryBean.getCategoryName()%></b></td></tr>
  <%int forumCountInCurrentCategory = 0;
    for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIterator.next();
        
        if (forumBean.getCategoryID() == categoryBean.getCategoryID()) {
            forumCountInCurrentCategory++;
            String color = (forumCountInCurrentCategory%2 == 1) ? "portlet-section-body" : "portlet-section-alternate";
            int forumID = forumBean.getForumID();
            boolean editForum = perm.canEditForum(forumID);
            boolean deleteForum = perm.canDeleteForum(forumID);
            boolean assignForum = perm.canAssignToForum(forumID);
            boolean moderateThread = perm.canModerateThread(forumID);
            boolean addThread = perm.canAddThread(forumID);
            boolean readPost = perm.canReadPost(forumID);
            boolean addPost = perm.canAddPost(forumID);
            boolean deletePost = perm.canDeletePost(forumID);
            boolean editPost = perm.canEditPost(forumID);
            boolean addAttach = perm.canAddAttachment(forumID);
            boolean getAttach = perm.canGetAttachment(forumID);
            boolean addPoll = perm.canAddPoll(forumID);
            boolean editPoll = perm.canEditPoll(forumID);
            boolean deletePoll = perm.canDeletePoll(forumID);
  %>
  <tr class="<%=color%>" align="center">
    <td align="left"> <%=forumBean.getForumName()%></td>
    <td><%if (editForum) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (deleteForum) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (assignForum) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td class="messageTextBold">Thread &raquo;</td>
    <td><%if (addThread) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (moderateThread) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td class="messageTextBold">Post &raquo;</td>
    <td><%if (readPost) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (addPost) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (deletePost) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (editPost) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td class="messageTextBold">Attachment &raquo;</td>
    <td><%if (addAttach) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (getAttach) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td class="messageTextBold">Poll &raquo;</td>
    <td><%if (addPoll) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (editPoll) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
    <td><%if (deletePoll) {%><font color="#008000">Yes<%} else {%><font color="#FF0080">No<%}%></font></td>
  </tr>
<%      }//if
    } //for forumIndex%>
 <% if (forumCountInCurrentCategory == 0) { %>
    <tr class="portlet-section-body"><td colspan="19" align="center"><fmt:message key="mvnforum.user.listforums.table.no_forum"/></td></tr>
 <% } 
} // for category%>

<%=request.getAttribute("CategoryTree")%>
<% if (categoryBeans.size() == 0) { %>
    <tr class="portlet-section-body"><td colspan="19" align="center"><fmt:message key="mvnforum.user.listforums.table.no_category"/></td></tr>
<% } %>
</table>
--%>
<%=request.getAttribute("Result")%>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>