<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/forummanagement.jsp,v 1.100 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.100 $
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

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.forummanagement.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/category.js" type="text/javascript"></script>
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
  <fmt:message key="mvnforum.admin.forummanagement.title"/>
</div>
<br/>

<%
Collection categoryBeans = CategoryCache.getInstance().getBeans();
%>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.forummanagement.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <%if (permission.canAddCategory()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "addcategory")%>" class="command"><fmt:message key="mvnforum.admin.addcategory.title"/></a><br/>
  <%}%>
  <%if ( permission.canAddForum() && (categoryBeans.size() > 0) ) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "addforum")%>" class="command"><fmt:message key="mvnforum.admin.addforum.title"/></a><br/>
  <%}%>
</div>

<br/>
<%--
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2">Name/Description</td>
    <td align="center"><fmt:message key="mvnforum.common.order"/></td>
    <td align="center">Creation Date</td>
    <td align="center">Type</td>
    <td align="center">Mode Moderation</td>
    <td align="center">Add Forum</td>
    <td align="center">Group Permission</td>
    <td align="center">Member Permission</td>
    <td align="center">Edit</td>
    <td align="center">Delete</td>
  </tr>
<%
for (Iterator catIterator = categoryBeans.iterator(); catIterator.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIterator.next();
%>
  <tr class="portlet-section-subheader">
    <td colspan="2">
      <b><%=categoryBean.getCategoryName()%></b><br/>
      <span class="portlet-font"><%=MyUtil.filter(categoryBean.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></span>
    </td>
    <td>
      <table width="100%">
        <tr class="portlet-font">
          <td width="40%" align="center">
            <% if (categoryBean.getCategoryOrder() > 0) { %>
            <a href="<%=urlResolver.encodeURL(request, response, "updatecategoryorder?category=" + categoryBean.getCategoryID() + "&amp;action=up", URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_up"/>" title="<fmt:message key="mvnforum.common.order.move_up"/>" /></a>
            <% } %>
          </td>
          <td width="40%" align="center"><b><%=categoryBean.getCategoryOrder()%></b></td>
          <td width="30%" align="center"><a href="<%=urlResolver.encodeURL(request, response, "updatecategoryorder?category=" + categoryBean.getCategoryID() + "&amp;action=down", URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/down.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_down"/>" title="<fmt:message key="mvnforum.common.order.move_down"/>" /></a></td>
        </tr>
      </table>
    </td>
    <td align="center" class="portlet-font"><%=onlineUser.getGMTTimestampFormat(categoryBean.getCategoryCreationDate())%></td>
    <td align="center"></td>
    <td align="center"></td>
    <td align="center">
<%if (permission.canAddForum()) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "addforum?category=" + categoryBean.getCategoryID())%>" class="command">Add Forum</a>
<%}%>
    </td>
    <td align="center"></td>
    <td align="center"></td>
    <td align="center">
<%if (permission.canEditCategory()) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "editcategory?category=" + categoryBean.getCategoryID())%>"><img src="<%=imagePath%>/button_edit.gif" border="0" alt="Edit category: <%=categoryBean.getCategoryName()%>" /></a>
<%}%>
    </td>
    <td align="center">
    <%if ( (forumCache.getNumberOfBeans(categoryBean.getCategoryID()) == 0) && (permission.canDeleteCategory()) ) {%>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletecategory?category=" + categoryBean.getCategoryID())%>">Delete</a>
    <%}%>
    </td>
  </tr>
<%
    int forumCountInCurrentCategory = 0;
    for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIterator.next();
        if (forumBean.getCategoryID() == categoryBean.getCategoryID()) {
            forumCountInCurrentCategory++;
            //String color = (forumCountInCurrentCategory%2 == 1) ? "portlet-section-body" : "portlet-section-alternate";
%>
  <tr class="<mvn:cssrow/>">
    <td width="20" align="center" nowrap="nowrap"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/forumicon.gif" border="0" alt="" /></td>
    <td width="30%">
      <b> <%if (forumBean.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED){%>class="disabledItem"<%}%>><%=forumBean.getForumName()%></b><br/>
      <%=MyUtil.filter(forumBean.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
    <td>
      <table width="100%">
        <tr class="<%=color%>">
          <td width="30%" align="center">
            <% if (forumBean.getForumOrder() > 0) { %>
            <a href="<%=urlResolver.encodeURL(request, response, "updateforumorder?forum=" + forumBean.getForumID() + "&action=up", URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_up"/>" title="<fmt:message key="mvnforum.common.order.move_up"/>" /></a>
            <% } %>
          </td>
          <td width="40%" align="center"><b><%=forumBean.getForumOrder()%></b></td>
          <td width="30%" align="center"><a href="<%=urlResolver.encodeURL(request, response, "updateforumorder?forum=" + forumBean.getForumID() + "&action=down", URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/down.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_down"/>" title="<fmt:message key="mvnforum.common.order.move_down"/>" /></a></td>
        </tr>
      </table>
    </td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(forumBean.getForumCreationDate())%></td>
    <td align="center"><%=forumBean.getForumTypeName()%></td>
    <td align="center"><%=forumBean.getForumModeName()%></td>
    <td align="center"></td>
    <td align="center">
  <%if ( permission.canAssignToForum(forumBean.getForumID()) ) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "assigngrouptoforum?forum=" + forumBean.getForumID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt="Edit Group Permissions for forum : <%=forumBean.getForumName()%>" /></a>
  <%}%>
    </td>
    <td align="center">
  <%if ( permission.canAssignToForum(forumBean.getForumID()) ) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "assignmembertoforum?forum=" + forumBean.getForumID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Edit Member Permissions for forum : <%=forumBean.getForumName()%>" /></a>
  <%}%>
    </td>
    <td align="center">
  <%if ( permission.canEditForum(forumBean.getForumID()) ) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "editforum?forum=" + forumBean.getForumID())%>"><img src="<%=imagePath%>/button_edit.gif" border="0" alt="Edit forum: <%=forumBean.getForumName()%>" /></a>
  <%}%>
    </td>
    <td align="center">
  <%if ( permission.canDeleteForum(forumBean.getForumID()) ) {%>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deleteforum?forum=" + forumBean.getForumID())%>">Delete</a>
  <%}%>
    </td>
  </tr>
<%
        }//if
    } //for forumIndex
    if (forumCountInCurrentCategory == 0) {
%>
  <tr class="<mvn:cssrow/>">
    <td colspan="11" align="center">There are no forums in this category. Click AddForum to add one.</td>
  </tr>
<%
    }
} //for catIndex
if (categoryBeans.size() == 0) {
%>
  <tr class="portlet-section-alternate">
    <td colspan="11" align="center">There are no categories.</td>
  </tr>
<%
}// if no category
%>
</table>
<%
    out.println(request.getAttribute("Result"));
    if (categoryBeans.size() == 0) {
%>
  <tr class="portlet-section-alternate">
    <td colspan="12" align="center">There are no categories.</td>
  </tr>
  
<%
}
%>
</table>
--%>
<%= request.getAttribute("Result") %>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>