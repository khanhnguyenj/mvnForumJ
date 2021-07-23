<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/groupmanagement.jsp,v 1.86 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.86 $
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
<%@ page import="java.util.*" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.groupmanagement.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
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
  <fmt:message key="mvnforum.admin.groupmanagement.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.groupmanagement.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "addgroup")%>" class="command"><fmt:message key="mvnforum.admin.addgroup.title"/></a><br/>
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.group.group_name_desc"/></td>
    <%-- td align="center"><fmt:message key="mvnforum.common.group.group_owner_name"/></td --%>
    <td align="center"><fmt:message key="mvnforum.common.date.create_date"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission.assign_forum"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission.edit_global_permission"/></td>
    <td align="center"><fmt:message key="mvnforum.common.numberof.members"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<mvn:cssrows>
<%
Collection groupBeans = (Collection) request.getAttribute("GroupsBeans");
for (Iterator iterator = groupBeans.iterator(); iterator.hasNext(); ) {
    GroupsBean groupBean = (GroupsBean)iterator.next();
    int groupID = groupBean.getGroupID();
%>
  <tr class="<mvn:cssrow/>">
    <td>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewgroup?group=" + groupID)%>"><%=groupBean.getGroupName()%></a>
      <br/><%=groupBean.getGroupDesc()%>
    </td>
<%--     
    <td align="center">
        <%
        if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) { %>
          <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {//normal group
            if (groupBean.getGroupOwnerID() == 0) { %>
              <fmt:message key="mvnforum.common.group.no_owner"/>
          <%} else {
                out.print(groupBean.getGroupOwnerName());
            }
        }
        %>
    </td>
--%>    
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(groupBean.getGroupCreationDate())%>
    </td>
    <td align="center">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "assignforumtogroup?group=" + groupBean.getGroupID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt="Assign Forum's Permissions for group : <%=groupBean.getGroupName()%>" /></a>
    </td>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "editgrouppermission?group=" + groupBean.getGroupID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt="Assign Permissions for group : <%=groupBean.getGroupName()%>" /></a>
    </td>
    <td align="center" nowrap="nowrap">
      <% if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {/*"Registered Members" group*/ %>
        <% if (internalUserDatabase) { %>
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><%=groupBean.getGroupMemberCount()%></a>
        <% } else { %>  
          <%=groupBean.getGroupMemberCount()%>
        <% } %>
      <% } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {/*other reserved groups*/ %>
           <fmt:message key="mvnforum.common.not_applicable"/>
      <% } else {/*normal group*/ %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "listmembergroup?group=" + groupID)%>"><%=groupBean.getGroupMemberCount()%></a>
      <%}%>
    </td>
    <td align="center">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editgroupinfo?group=" + groupID)%>"><img src="<%=imagePath%>/button_edit.gif" border="0" alt="Edit Group: <%=groupBean.getGroupName()%>" /></a>
    </td>
    <td align="center">
      <% if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) { %>
        <fmt:message key="mvnforum.common.not_applicable"/>
      <% } else {/*regular non-reserved group*/ %>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletegroup?group=" + groupID)%>"><fmt:message key="mvnforum.common.action.delete"/></a>
      <% } %>
    </td>
  </tr>
<%
}//for
if (groupBeans.size() == 0) {
%>
  <tr class="<mvn:cssrow/>"><td colspan="4" align="center"><fmt:message key="mvnforum.admin.groupmanagement.no_group"/></td></tr>
<%
}
%>
</mvn:cssrows>
</table>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>