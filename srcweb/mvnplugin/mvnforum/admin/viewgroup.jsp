<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/viewgroup.jsp,v 1.75 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.75 $
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
<%@ page import="com.mvnforum.db.*"%>
<%@ page import="com.mvnforum.MVNForumConstant"%>
<%@ page import="com.mvnforum.MVNForumGlobal"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.viewgroup.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<%
GroupsBean objGroupsBean = (GroupsBean)request.getAttribute("GroupsBean");
int groupID = objGroupsBean.getGroupID();
String groupName = objGroupsBean.getGroupName();
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "groupmanagement")%>"><fmt:message key="mvnforum.admin.groupmanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.viewgroup.title"/>: <%=groupName%>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.viewgroup.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "editgroupinfo?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.editgroupinfo.title"/></a><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "editgrouppermission?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.editgrouppermission.title"/></a><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "assignforumtogroup?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.assignforumtogroup.title"/></a><br/>
  <%--
  <a href="javascript:alert('Not implemented yet.');" class="command">Change Group Option</a><br/>
  --%>
  <%if (groupID > MVNForumConstant.LAST_RESERVED_GROUP_ID) {%>
    <%-- a href="<%=urlResolver.encodeURL(request, response, "editgroupowner?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.editgroupowner.title"/></a><br/ --%>
    <a href="<%=urlResolver.encodeURL(request, response, "addmembergroup?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.addmembergroup.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "listmembergroup?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.listmembergroup.title"/></a><br/>
    <a href="<%=urlResolver.encodeURL(request, response, "deletegroup?group=" + groupID)%>" class="command"><fmt:message key="mvnforum.admin.viewgroup.command.delete_group"/></a> <span class="warning"><fmt:message key="mvnforum.common.warning"/></span><br/>
  <%}%>
</div>

<br/>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.group.header.group_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top" width="200"><fmt:message key="mvnforum.common.group.group_name"/>:</td>
    <td><b><%=groupName%></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.group.group_description"/>:</td>
    <td><%=objGroupsBean.getGroupDesc()%></td>
  </tr>
<%--   
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.group.group_owner_name"/>:</td>
    <td>
      <%if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) { %>
          <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {//normal group
            if (objGroupsBean.getGroupOwnerID() == 0) { %>
              <fmt:message key="mvnforum.common.group.no_owner"/>
          <%} else {
                out.print(objGroupsBean.getGroupOwnerName());
            }
        }%>
    </td>
  </tr>
--%>  
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.date.create_date"/>:</td>
    <td><%=onlineUser.getGMTTimestampFormat(objGroupsBean.getGroupCreationDate())%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.date.modified_date"/>:</td>
    <td><%=onlineUser.getGMTTimestampFormat(objGroupsBean.getGroupModifiedDate())%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
