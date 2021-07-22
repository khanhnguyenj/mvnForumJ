<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/rsssummary.jsp,v 1.50 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.50 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="java.util.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.rss.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/listforums.js" type="text/javascript"></script>
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.rss.title"/>
</div>
<br/>
<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");

if (sort.length() == 0) sort = "ThreadLastPostDate";
if (order.length() == 0) order = "DESC";

CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
%>
<table width="95%" align="center">
  <tr class="portlet-font"><td>
  <form action="<%=urlResolver.encodeURL(request, response, "rsssummary", URLResolverService.ACTION_URL)%>" name="Refresh" <mvn:method/>>
    <%=urlResolver.generateFormAction(request, response, "rsssummary")%>
    <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
    <select id="sort" name="sort" onchange="document.Refresh.submit();">
      <option value="ThreadLastPostDate" <%if (sort.equals("ThreadLastPostDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.last_post_date"/></option>
      <option value="ThreadCreationDate" <%if (sort.equals("ThreadCreationDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.first_post_date"/></option>
      <option value="MemberName" <%if (sort.equals("MemberName")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.author"/></option>
      <option value="ThreadReplyCount" <%if (sort.equals("ThreadReplyCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.reply_count"/></option>
      <option value="ThreadViewCount" <%if (sort.equals("ThreadViewCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.view_count"/></option>
    </select>
    <label for="order"><fmt:message key="mvnforum.common.order"/></label>
    <select id="order" name="order" onchange="document.Refresh.submit();">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
    </select>
  </form>
  </td></tr>
</table>
<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.global_feed"/></td>
    <td align="center" style="width:80px;">RSS 0.91</td>
    <td align="center" style="width:80px;">RSS 2.0</td>
    <td align="center" style="width:80px;">ATOM</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.global_feed"/></td>
    <td align="center"> <%-- RSS 0.91 --%>
      <a href="<%=urlResolver.encodeURL(request, response, "rss?sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/xml.gif" alt="RSS 0.91 <fmt:message key="mvnforum.common.global_feed"/>" title="RSS 0.91 <fmt:message key="mvnforum.common.global_feed"/>" border="0" /></a>
    </td>
    <td align="center"> <%-- RSS 2.0 --%>
      <a href="<%=urlResolver.encodeURL(request, response, "rss2?sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/rss.gif" alt="RSS 2.0 <fmt:message key="mvnforum.common.global_feed"/>" title="RSS 2.0 <fmt:message key="mvnforum.common.global_feed"/>" border="0" /></a>
    </td>
    <td align="center"> <%-- ATOM --%>
      <a href="<%=urlResolver.encodeURL(request, response, "atom?sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/atom.gif" alt="ATOM <fmt:message key="mvnforum.common.global_feed"/>" title="ATOM <fmt:message key="mvnforum.common.global_feed"/>" border="0" /></a>
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>
<%-- <table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="70%">Forum Name</td>
    <td align="center">RSS 0.91</td>
    <td align="center">RSS 2.0</td>
    <td align="center">ATOM</td>
  </tr>--%>
<%--
<%
int categoryCount = 0;
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryID, permission) == false) continue; %>
      <tr class="portlet-section-subheader"><td colspan="4" class="messageTextBold"><%=categoryBean.getCategoryName()%></td></tr>
    <%
    categoryCount ++;
    int rowIndex = 0;
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        String forumName = forumBean.getForumName();
        int forumID = forumBean.getForumID();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumID) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {     
          String color = ((rowIndex++)%2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
%>
        <tr class="<%=color%>">
          <td><%=forumName%></td>
          <td align="center"> 
            <a href="<%=urlResolver.encodeURL(request, response, "rss?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/xml.gif" alt="RSS 0.91 <fmt:message key="mvnforum.common.forum_specific_feed"/>" title="RSS 0.91 <fmt:message key="mvnforum.common.forum_specific_feed"/>" border="0" /></a>
          </td>
          <td align="center"> 
            <a href="<%=urlResolver.encodeURL(request, response, "rss2?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/rss.gif" alt="RSS 2.0 <fmt:message key="mvnforum.common.forum_specific_feed"/>" title="RSS 2.0 <fmt:message key="mvnforum.common.forum_specific_feed"/>" border="0" /></a>
          </td>
          <td align="center"> 
            <a href="<%=urlResolver.encodeURL(request, response, "atom?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/atom.gif" alt="ATOM <fmt:message key="mvnforum.common.forum_specific_feed"/>" title="ATOM <fmt:message key="mvnforum.common.forum_specific_feed"/>" border="0" /></a>
          </td>
        </tr>
<%      } //if
    } // for forum
}// for category %>


<% if (categoryCount == 0) {%>
  <tr>
    <td colspan="4" align="center" class="portlet-section-body"><fmt:message key="mvnforum.user.listforums.table.no_category"/></td>
  </tr>
<% } %>
--%>
<%= request.getAttribute("Result") %>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
