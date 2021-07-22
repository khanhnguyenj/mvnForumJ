<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/misctasks.jsp,v 1.145 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.145 $
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

<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.search.post.RebuildPostIndexTask"%>
<%@ page import="com.mvnforum.search.member.RebuildMemberIndexTask"%>
<%@ page import="com.mvnforum.search.attachment.RebuildAttachmentIndexTask"%>
<%@ page import="com.mvnforum.search.albumitem.RebuildAlbumItemIndexTaskMapping"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">

<% 
boolean autoRefresh = ParamUtil.getParameterBoolean(request, "refresh");
boolean atLeastOneIndexRebuilding = RebuildPostIndexTask.isRebuilding() || RebuildMemberIndexTask.isRebuilding() || RebuildAttachmentIndexTask.isRebuilding() || RebuildAlbumItemIndexTaskMapping.isRebuilding();
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.misctasks.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>

<% if ( autoRefresh && atLeastOneIndexRebuilding ) {%>
<meta http-equiv="refresh" content="10; url=<%=urlResolver.encodeURL(request, response, "misctasks?refresh=true")%>" />
<% }%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.misctasks.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.misctasks.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <%if (permission.canAdminSystem()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "testsystem")%>" class="command"><fmt:message key="mvnforum.admin.testsystem.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "configindex")%>" class="command"><fmt:message key="mvnforum.admin.configindex.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "viewlogsystem")%>" class="command"><fmt:message key="mvnforum.admin.viewlogsystem.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "edittemplate")%>" class="command"><fmt:message key="mvnforum.admin.edittemplate.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "editcss")%>" class="command"><fmt:message key="mvnforum.admin.editcss.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "restorecss", URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.misctasks.restore_css"/></a><br/>
    <%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "listeventlogs")%>" class="command"><fmt:message key="mvnforum.admin.listeventlogsx.title"/></a><br/>
      <a href="<%=urlResolver.encodeURL(request, response, "listalbumitemtags")%>" class="command"><fmt:message key="mvnforum.admin.listalbumitemtagsx.title"/></a><br/>
      <a href="<%=urlResolver.encodeURL(request, response, "reportstatistics")%>" class="command"><fmt:message key="mvnforum.admin.reportstatisticsx.title"/></a><br />
      <a href="<%=urlResolver.encodeURL(request, response, "listbannedemails")%>" class="command"><fmt:message key="mvnforum.admin.listbannedemailsx.title"/></a><br/>
      <a href="<%=urlResolver.encodeURL(request, response, "listbannedips")%>" class="command"><fmt:message key="mvnforum.admin.listbannedipsx.title"/></a><br/>
      <a href="<%=urlResolver.encodeURL(request, response, "listbannedusernames")%>" class="command"><fmt:message key="mvnforum.admin.listbannedusernamesx.title"/></a><br/>
      <a href="<%=urlResolver.encodeURL(request, response, "listbannedwords")%>" class="command"><fmt:message key="mvnforum.admin.listbannedwordsx.title"/></a><br/>
    <%}%>
  <%}%>

<%--
Import/export is not working correctly on all DBMSes with current MemberID=0 for Guest.
Problem is with some DBMSes in the INSERT query in admin.MemberXML class (line 406).
It could be avoided, but then we need 3 queries (to insert a record,
then to get that id from DBMS, then to update it to 0), and even then
some other issues can arrise on some DBMSes (can't update to id=0).
There are two possible solutions:
1) make MemberID column not to be identity column - this is out of question, since
   we need it to be identity column
2) don't use MemberID=0, but some other (2,3, or whatever) - this is just like we have
   now in Groups table (GroupsID=0 is currently unused, and we don't add records with
   that ID). In previous revisions we have been (successfully) using MemberID=2 for Guest,
   without any negative implications (all JSPs were adjusted to differentiate virtual
   Guest user from "real" users).
<%if (permission.canAdminSystem()) { %
    <a href="<%=urlResolver.encodeURL(request, response, "importexport")%>" class="command">Import/Export Data</a><br/>
<%}%
--%>
</div>

<%if (permission.canAdminSystem()) {%>
<br/>
<a name="rebuildindex"></a>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>
    <fmt:message key="mvnforum.admin.misctasks.rebuild_search_info"/><br />
    <span class="warning"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_warning"/></span><br />
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_all_search_index"/></a><br/>
    
    <% if (atLeastOneIndexRebuilding) {%>
    <form action="<%=urlResolver.encodeURL(request, response, "misctasks")%>" name="RefreshForm">
      <%=urlResolver.generateFormAction(request, response, "misctasks")%>
      <%-- mvn:securitytoken no need check here --%>
      <span class="warning"><fmt:message key="mvnforum.admin.misctasks.status_rebuilding_index"/></span> <fmt:message key="mvnforum.admin.misctasks.check_to_refresh"/>
      <input class="noborder" type="checkbox" <%if (autoRefresh) {%> checked="checked" <%}%> name="refresh" onchange="RefreshForm.submit()"/>
    </form>
    <% } %>
    </td>
  </tr>
</table>
</mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="3"><fmt:message key="mvnforum.admin.misctasks.search_engine_info"/></td>
  </tr>
<mvn:cssrows>
<%
int postNumDocs = ((Integer)request.getAttribute("PostNumDocs")).intValue();
int memberNumDocs = ((Integer)request.getAttribute("MemberNumDocs")).intValue();
int attachmentNumDocs = ((Integer)request.getAttribute("AttachmentNumDocs")).intValue();

int postCount = ((Integer)request.getAttribute("PostCount")).intValue();
int memberCount = ((Integer)request.getAttribute("MemberCount")).intValue();
int attachmentCount = ((Integer)request.getAttribute("AttachmentCount")).intValue();

  if (postNumDocs >= 0) { %>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.misctasks.post_search_engine"/></td>
    <td>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_index"/>: <b><%=postNumDocs%></b> <br/>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_database"/>: <b><%=postCount%></b> <br/>
      <% if (postNumDocs != postCount && !RebuildPostIndexTask.isRebuilding()) {%>
        <span class="warning"><fmt:message key="mvnforum.admin.misctasks.index_and_database_count_warning"/></span>
      <% } else if (postNumDocs != postCount && RebuildPostIndexTask.isRebuilding()) {%>
        <fmt:message key="mvnforum.admin.misctasks.waiting_for_rebuild_this_index"/>
      <% }%>
    </td>
    <td align="center">
      <% if (!RebuildPostIndexTask.isRebuilding()) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=post&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a>
      <% } else {%>
        <fmt:message key="mvnforum.admin.misctasks.rebuilding_search_index"/>
      <% } %>
    </td>
  </tr>
  <%} %>
  <%if (memberDAO.isSupportGetMembers() && (memberNumDocs >= 0)) {%>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.misctasks.member_search_engine"/></td>
    <td>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_index"/>: <b><%=memberNumDocs%></b> <br/>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_database"/>: <b><%=memberCount%></b> <br/>
      <% if (memberNumDocs != memberCount && !RebuildMemberIndexTask.isRebuilding()) {%>
        <span class="warning"><fmt:message key="mvnforum.admin.misctasks.index_and_database_count_warning"/></span>
      <% } else if (memberNumDocs != memberCount && RebuildMemberIndexTask.isRebuilding()) {%>
        <fmt:message key="mvnforum.admin.misctasks.waiting_for_rebuild_this_index"/>
      <% }%>
    </td>
    <td align="center">
      <% if (!RebuildMemberIndexTask.isRebuilding()) {%>
         <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=member&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a>
      <% } else {%>
        <fmt:message key="mvnforum.admin.misctasks.rebuilding_search_index"/>
      <% } %>
    </td>
  </tr>
  <% } %>
  <%if (MVNForumConfig.getEnableAttachment() && (attachmentNumDocs >= 0)) {%>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.misctasks.attachment_search_engine"/></td>
    <td>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_index"/>: <b><%=attachmentNumDocs%></b> <br/>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_database"/>: <b><%=attachmentCount%></b> <br/>
      <% if (attachmentNumDocs != attachmentCount && !RebuildAttachmentIndexTask.isRebuilding()) {%>
        <span class="warning"><fmt:message key="mvnforum.admin.misctasks.index_and_database_count_warning"/></span>
      <% } else if (attachmentNumDocs != attachmentCount && RebuildAttachmentIndexTask.isRebuilding()) {%>
        <fmt:message key="mvnforum.admin.misctasks.waiting_for_rebuild_this_index"/>
      <% }%>
    </td>
    <td align="center">
      <% if (!RebuildAttachmentIndexTask.isRebuilding()) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=attachment&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a>
      <% } else {%>
        <fmt:message key="mvnforum.admin.misctasks.rebuilding_search_index"/>
      <% } %>
    </td>
  </tr>
  <%}%>
  <%if ((environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) && MVNForumConfig.getEnablePrivateAlbum()) {
      int albumItemNumDocs = ((Integer)request.getAttribute("AlbumItemNumDocs")).intValue();
      int albumItemCount = ((Integer)request.getAttribute("AlbumItemCount")).intValue();
      if (albumItemNumDocs >= 0) {
  %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.misctasks.albumitem_search_engine"/></td>
    <td>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_index"/>: <b><%=albumItemNumDocs%></b> <br/>
      <fmt:message key="mvnforum.admin.misctasks.number_of_objects_in_database"/>: <b><%=albumItemCount%></b> <br/>
      <% if (albumItemNumDocs != albumItemCount && !RebuildAlbumItemIndexTaskMapping.isRebuilding()) {%>
        <span class="warning"><fmt:message key="mvnforum.admin.misctasks.index_and_database_count_warning"/></span>
      <% } else if (albumItemNumDocs != albumItemCount && RebuildAlbumItemIndexTaskMapping.isRebuilding()) {%>
        <fmt:message key="mvnforum.admin.misctasks.waiting_for_rebuild_this_index"/>
      <% }%>
    </td>
    <td align="center">
      <% if (!RebuildAlbumItemIndexTaskMapping.isRebuilding()) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=albumitem&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a>
      <% } else {%>
        <fmt:message key="mvnforum.admin.misctasks.rebuilding_search_index"/>
      <% } %>
    </td>
  </tr>
  <%  }
  }%>
  <%
  int albumItemNumDocs = -1;
  if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
      albumItemNumDocs = ((Integer)request.getAttribute("AlbumItemNumDocs")).intValue();
  }
  %>
  <% if ( (postNumDocs < 0) || 
          (memberDAO.isSupportGetMembers() && (memberNumDocs < 0)) || 
          (MVNForumConfig.getEnableAttachment() && (attachmentNumDocs < 0)) || 
          (MVNForumConfig.getEnablePrivateAlbum() && (albumItemNumDocs < 0)) ) { %>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <span class="warning"><fmt:message key="mvnforum.admin.misctasks.cannot_view_search_index"/></span>
      <br/>
      <%if (postNumDocs < 0) {%>
        &raquo; <fmt:message key="mvnforum.admin.misctasks.post_search_engine"/> - <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=post&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a><br/>
      <%}%>
      <%if ( memberDAO.isSupportGetMembers() && (memberNumDocs < 0) ) {%>
        &raquo; <fmt:message key="mvnforum.admin.misctasks.member_search_engine"/> - <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=member&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a><br/>
      <%}%>
      <%if ( MVNForumConfig.getEnableAttachment() && (attachmentNumDocs < 0) ) {%>
        &raquo; <fmt:message key="mvnforum.admin.misctasks.attachment_search_engine"/> - <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=attachment&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a><br/>
      <%}%>
      <%if ( MVNForumConfig.getEnablePrivateAlbum() && (albumItemNumDocs < 0) ) { %>
        &raquo; <fmt:message key="mvnforum.admin.misctasks.albumitem_search_engine"/> - <a class="command" href="<%=urlResolver.encodeURL(request, response, "rebuildindex?target=albumitem&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.misctasks.rebuild_search_index"/></a><br/>
      <%} %>
    </td>
  </tr>
  <% }%>
</mvn:cssrows>
</table>
<%}// if can admin system for search index %>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
