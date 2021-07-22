<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listrecentthreads.jsp,v 1.152 2009/07/16 08:16:11 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.152 $
 - $Date: 2009/07/16 08:16:11 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.util.StringUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.common.ThreadIconLegend" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listrecentthreads.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<link rel="alternate" type="application/rss+xml" href="<%=urlResolver.encodeURL(request, response, "rss2")%>" title="<fmt:message key="mvnforum.user.listrecentthreads.title"/> - RSS"/>
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function handleGo() {
  <mvn:servlet>
  document.form.go.disabled=true;
  </mvn:servlet>
  document.form.submit();
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
<% if (MVNForumConfig.getEnablePortalLikeIndexPage()) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "listforums")%>"><fmt:message key="mvnforum.user.listforums.title"/></a>&nbsp;&raquo;&nbsp;
<% } %>
  <fmt:message key="mvnforum.user.listrecentthreads.title"/>
</div>
<br/>

<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
ThreadIconLegend threadIconLegend = new ThreadIconLegend();
%>
<table width="95%" align="center">
  <tr class="portlet-font"><td>
    <form action="">
<label for="FastGoForum"><fmt:message key="mvnforum.common.quick_go"/></label> &raquo;
<%--
 <select name="FastGoForum" onChange="gotoPage(this.options[this.selectedIndex].value)">
        <option value=""><fmt:message key="mvnforum.common.prompt.choose_forum"/></option>
<%
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryID, permission) == false) continue; %>
        <option value=""></option>
        <option value="<%=urlResolver.encodeURL(request, response, "listforums")%>"><%=categoryBean.getCategoryName()%></option>
        <option value="">---------------------------------</option>
    <%
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) { %>
        <option value="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumBean.getForumID())%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=forumBean.getForumName()%></option>
<%      } //if
    } // for forum
}// for category %>
  </select>
--%>
    <%= request.getAttribute("Result") %>
    </form>
  </td></tr>
</table>

<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");

if (sort.length() == 0) sort = "ThreadLastPostDate";
if (order.length() == 0) order = "DESC";
%>
<table width="95%" align="center">
  <tr>
    <td nowrap="nowrap" class="portlet-font">
    <form  name="form" action="<%=urlResolver.encodeURL(request, response, "listrecentthreads", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "listrecentthreads")%>
      <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
      <select id="sort" name="sort">
      <option value="ThreadLastPostDate" <%if (sort.equals("ThreadLastPostDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.last_post_date"/></option>
      <option value="ThreadCreationDate" <%if (sort.equals("ThreadCreationDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.first_post_date"/></option>
      <option value="MemberName" <%if (sort.equals("MemberName")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.post.author"/></option>
      <option value="ThreadReplyCount" <%if (sort.equals("ThreadReplyCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.reply_count"/></option>
      <option value="ThreadViewCount" <%if (sort.equals("ThreadViewCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.view_count"/></option>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
      </select>

      <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="javascript:handleGo();" class="liteoption"/>
    </form>
    </td>
  </tr>
</table>
<%
boolean hasAnAttachment = false;
boolean hasPoll = false;

Collection threadBeans = (Collection) request.getAttribute("ThreadBeans");
int totalThreads = ((Integer)request.getAttribute("TotalThreads")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();
%>

<pg:pager
  url="listrecentthreads"
  items="<%= totalThreads %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.threads"); %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="3"><fmt:message key="mvnforum.common.thread"/></td>
    <td><fmt:message key="mvnforum.common.forum"/></td>
    <td align="center"><fmt:message key="mvnforum.common.post.author"/></td>
    <td align="center"><fmt:message key="mvnforum.common.reply_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.view_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.last_post"/></td>
  </tr>
<mvn:cssrows>
<%
int index = 0;
for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
    ThreadBean threadBean = (ThreadBean)iterator.next();
    index++;
    String threadIcon = MyUtil.getThreadIconName(onlineUser.getLastLogonTimestamp().getTime(), threadBean.getThreadLastPostDate().getTime(), threadBean.getThreadReplyCount()+1, threadBean.getThreadStatus());
    threadIconLegend.updateIconLegend(threadIcon);
    int postCount = threadBean.getThreadReplyCount() + 1;
    int totalPages = (postCount / memberPostsPerPage) + ( (postCount % memberPostsPerPage) == 0 ? 0 : 1);
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadIcon%>" border="0" alt=""/></td>
    <td width="16"><%=EnableEmotionFilter.filter(threadBean.getThreadIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%></td>
    <td>
     <% 
       String typeImage = MyUtil.getThreadTypeIcon(threadBean.getThreadType());
       threadIconLegend.updateIconLegend(typeImage);
       String threadPriorityIcon = MyUtil.getThreadPriorityIcon(threadBean.getThreadPriority());
       threadIconLegend.updateIconLegend(threadPriorityIcon);
      %>
      <% if (threadBean.getThreadType() != ThreadBean.THREAD_TYPE_DEFAULT) { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=typeImage%>" alt=""/>
      <% } %>
      <% if ( ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_LOW) && MVNForumConfig.getEnableLowPriorityIcon() ) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_NORMAL) && MVNForumConfig.getEnableNormalPriorityIcon()) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_HIGH) && MVNForumConfig.getEnableHighPriorityIcon() ) ) { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadPriorityIcon%>" border="0" alt=""/>
      <% } %>
      <% if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE && MVNForumConfig.getEnablePoll()) {
          if (threadBean.getThreadHasPoll() > 0) {
              hasPoll = true; %>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/>
      <%}
      }%>
      <% if (threadBean.getThreadAttachCount() > 0) {
           hasAnAttachment = true; %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" alt=""/>
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID())%>" class="messageTopic">
        <%=MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
      <br/>
      <%=MyUtil.filterWithoutBBCode(StringUtil.getShorterString(threadBean.getThreadBody(), MVNForumConfig.getMaxCharsInShortSummary()), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
      <%int threadID = threadBean.getThreadID();%>
      <%@ include file="inc_pages.jsp"%>
    <%if ( (index == 1) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_RECEND_THREAD) > 0) ) {%>
      <br/>
      <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_RECEND_THREAD))%>
    <%}%>
    </td>
    <td>
      <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + threadBean.getForumID())%>" class="messageTopic"><%=forumCache.getBean(threadBean.getForumID()).getForumName()%></a>
    </td>
    <td align="center">
      <% if ((threadBean.getMemberName()!=null) && (threadBean.getMemberName().length()>0)) { %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getMemberName()))%>" class="memberName"><%=threadBean.getMemberName()%></a>
      <% } %>
    </td>
    <td align="center"><b><%=threadBean.getThreadReplyCount()%></b></td>
    <td align="center"><b><%=threadBean.getThreadViewCount()%></b></td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(threadBean.getThreadLastPostDate())%><br/>
      <% if ((threadBean.getLastPostMemberName()!=null) && (threadBean.getLastPostMemberName().length()>0)) { %>
      <fmt:message key="mvnforum.common.by"/> <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getLastPostMemberName()))%>" class="memberName"><%=threadBean.getLastPostMemberName()%></a>&nbsp;
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID() + "&amp;lastpage=yes#lastpost")%>" class="messageTopic"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/right.gif" border="0" alt="<fmt:message key="mvnforum.common.last"/>" title="<fmt:message key="mvnforum.common.last"/>"/></a>
    </td>
  </tr>
</pg:item>
<%
}//for
if (threadBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.listrecentthreads.table.no_threads"/></td></tr>
<%}%>
</mvn:cssrows>
</table>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>
<br/>
<% if (threadIconLegend.isHasThreadIconLegend() || hasAnAttachment || hasPoll) { %>
  <table width="95%" class="noborder" align="center">
<% } %>
  <% if (threadIconLegend.isHasGlobalAnnouncement()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_TYPE_GLOBAL_ANNOUNCEMENT %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.global_announcement"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasAnnouncement()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_TYPE_FORUM_ANNOUNCEMENT %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.announcement"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasSticky()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_TYPE_STICKY %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.sticky"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasPriorityHigh()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_HIGH %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_high"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasPriorityNormal()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_NORMAL %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_normal"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasPriorityLow()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_LOW %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_low"/></td>
  </tr>
  <%}%>
   <% if (hasPoll) {%>
   <tr class="portlet-font">
     <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/></td>
     <td><fmt:message key="mvnforum.common.legend.thread.has_poll"/></td>
   </tr>
   <%}%>
  <% if (hasAnAttachment) { %>
  <tr class="portlet-font">
    <td width="16" align="center"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.has_attachment"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_ACTIVE %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_ACTIVE %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_CLOSED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_CLOSED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_LOCKED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_LOCKED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_ACTIVE %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_ACTIVE %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_CLOSED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_CLOSED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_LOCKED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_LOCKED %>" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_locked"/></td>
  </tr>
  <%}%>
<% if (threadIconLegend.isHasThreadIconLegend() || hasAnAttachment || hasPoll) { %>
  </table>
  <br/>
<% } %>  
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
