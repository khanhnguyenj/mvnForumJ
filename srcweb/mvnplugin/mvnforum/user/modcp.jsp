<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/modcp.jsp,v 1.36 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.36 $
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
 - @author: Igor Manic   
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.modcp.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.modcp.title"/>
</div>
<br/>

<%
CategoryCache categoryCache = CategoryCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = (Collection)request.getAttribute("ForumBeans");
int pendingThreadCount = ((Integer)request.getAttribute("PendingThreadCount")).intValue();
int threadsWithPendingPostsCount = ((Integer)request.getAttribute("ThreadsWithPendingPostsCount")).intValue();
%>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.modcp.guide"/>
  <br/>
  <a class="command" href="<%=urlResolver.encodeURL(request, response, "listrecentpendingthreads")%>"><fmt:message key="mvnforum.user.modcp.pending_threads"/></a>
  <%if (pendingThreadCount > 0) {%><span class="pendingyes">(<%=pendingThreadCount%>)</span><%}%>
  <br/>
  <a class="command" href="<%=urlResolver.encodeURL(request, response, "listrecentthreadswithpendingposts")%>"><fmt:message key="mvnforum.user.modcp.threads_has_pending_posts"/></a>
  <%if (threadsWithPendingPostsCount > 0) {%><span class="pendingyes">(<%=threadsWithPendingPostsCount%>)</span><%}%>
  <br/>
</div>
<br/>
<%--
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.user.listforums.table.forum_name_desc"/></td>
    <td align="center"><fmt:message key="mvnforum.common.numberof.pending_threads"/></td>
    <td align="center"><fmt:message key="mvnforum.common.numberof.threads_with_pending_posts"/></td>
    <td align="center"><fmt:message key="mvnforum.common.numberof.pending_posts"/></td>
  </tr>--%>
<%--
<%
for (Iterator catIterator = categoryBeans.iterator(); catIterator.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIterator.next();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission) == false) continue;
%>
  <tr class="portlet-section-subheader">
    <td colspan="4">
      <b><%=categoryBean.getCategoryName()%></b><br/>
      <%=MyUtil.filter(categoryBean.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
  </tr>
<%
    int forumCountInCurrentCategory = 0;
    for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIterator.next();
        int forumID = forumBean.getForumID();
        if ((forumBean.getCategoryID() == categoryBean.getCategoryID()) && permission.canModerateThread(forumID) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
            forumCountInCurrentCategory++;
%>
  <tr class="<%=MyUtil.getRowCSS(forumCountInCurrentCategory+1)%>">
    <td>
      <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID)%>" class="messageTopic"><%=forumBean.getForumName()%></a><br/>
      <%=MyUtil.filter(forumBean.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
    <td align="center" class="<%=forumBean.getPendingThreadCount()>0? "pendingyes" : "pendingno"%>">
      <%=forumBean.getPendingThreadCount()%>
      <%if (forumBean.getPendingThreadCount()>0) {%>
        <br/><a class="command" href="<%=urlResolver.encodeURL(request, response, "moderatependingthreads?forum=" + forumID)%>"><fmt:message key="mvnforum.user.modcp.pending_threads"/></a>
      <%}%>
    </td>
    <td align="center" class="<%=forumBean.getThreadsWithPendingPostsCount()>0? "pendingyes" : "pendingno"%>">
      <%=forumBean.getThreadsWithPendingPostsCount()%>
      <%if (forumBean.getThreadsWithPendingPostsCount()>0) {%>
        <br/><a class="command" href="<%=urlResolver.encodeURL(request, response, "listthreadswithpendingposts?forum=" + forumID)%>"><fmt:message key="mvnforum.user.modcp.threads_has_pending_posts"/></a>
      <%}%>
    </td>
    <td align="center" class="<%=forumBean.getPendingPostCount()>0? "pendingyes" : "pendingno"%>">
      <%=forumBean.getPendingPostCount()%>
    </td>
  </tr>
<%
        }//if the forum is in the current category
    } //for forumIndex
    if (forumCountInCurrentCategory == 0) {
%>
  <tr class="portlet-section-body">
    <td colspan="4" align="center"><fmt:message key="mvnforum.user.listforums.table.no_forum"/></td>
  </tr>
<%
    }// if no forum in this category
} //for catIndex
%>
<%
if (categoryBeans.size() == 0) {
%>
  <tr class="portlet-section-body">
    <td colspan="4" align="center"><fmt:message key="mvnforum.user.listforums.table.no_category"/></td>
  </tr>
<%
}// if no category
%>
--%>
<%= request.getAttribute("Result") %>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
