<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listthreadswithpendingposts.jsp,v 1.71 2009/07/16 08:46:42 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.71 $
 - $Date: 2009/07/16 08:46:42 $
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listthreadswithpendingposts.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
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

<%
int forumID = ParamUtil.getParameterInt(request, "forum");
ForumCache forumCache = ForumCache.getInstance();
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
ThreadIconLegend threadIconLegend = new ThreadIconLegend();

if (sort.length() == 0) sort = "ThreadLastPostDate";
if (order.length() == 0) order = "DESC";
%>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "modcp")%>"><fmt:message key="mvnforum.user.modcp.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.forum"/>: <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID)%>"><%=forumCache.getBean(forumID).getForumName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.listthreadswithpendingposts.title"/>
</div>
<br/>
<table width="95%" align="center">
  <tr>
    <td nowrap="nowrap" class="portlet-font">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "listthreadswithpendingposts", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "listthreadswithpendingposts")%>
      <input type="hidden" name="forum" value="<%=forumID%>" />
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

      <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="javascript:handleGo();" class="liteoption" />
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
  url="listthreadswithpendingposts"
  items="<%= totalThreads %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.threads"); %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>
<pg:param name="forum"/>

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
    <td align="center"><fmt:message key="mvnforum.common.post.author"/></td>
    <td align="center"><fmt:message key="mvnforum.common.reply_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.numberof.pending_posts"/></td>
    <td align="center"><fmt:message key="mvnforum.common.last_post"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
    ThreadBean threadBean = (ThreadBean)iterator.next();
    String threadIcon = MyUtil.getThreadIconName(onlineUser.getLastLogonTimestamp().getTime(), threadBean.getThreadLastPostDate().getTime(), threadBean.getThreadReplyCount()+1, threadBean.getThreadStatus());
    threadIconLegend.updateIconLegend(threadIcon);
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadIcon%>" border="0" alt="" /></td>
    <td width="16"><%=EnableEmotionFilter.filter(threadBean.getThreadIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%></td>
    <% 
       String threadPriorityIcon = MyUtil.getThreadPriorityIcon(threadBean.getThreadPriority());
       threadIconLegend.updateIconLegend(threadPriorityIcon);
    %>
    <td>
      <% if ( ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_LOW) && MVNForumConfig.getEnableLowPriorityIcon() ) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_NORMAL) && MVNForumConfig.getEnableNormalPriorityIcon()) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_HIGH) && MVNForumConfig.getEnableHighPriorityIcon() ) ) { %>
         <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadPriorityIcon%>" border="0" alt="" />
      <% } %>
      <% if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE && MVNForumConfig.getEnablePoll()) {
           if (threadBean.getThreadHasPoll() > 0) {
             hasPoll = true; %>
             <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/>
      <%   }
         }
      %>
      <% if (threadBean.getThreadAttachCount() > 0) { 
           hasAnAttachment = true; %> 
         <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" title="<%=threadBean.getThreadAttachCount()%> <% if (threadBean.getThreadAttachCount() == 1) {%><fmt:message key="mvnforum.common.attachment"/><% } else {%><fmt:message key="mvnforum.common.attachments"/><%}%>" alt="" />
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "moderatependingposts?thread=" + threadBean.getThreadID())%>" class="messageTopic">
        <%=MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
      <br/>
      <%=MyUtil.filterWithoutBBCode(StringUtil.getShorterString(threadBean.getThreadBody(), MVNForumConfig.getMaxCharsInShortSummary()), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
    </td>
    <td align="center">
      <% if ((threadBean.getMemberName()!=null) && (threadBean.getMemberName().length()>0)) { %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getMemberName()))%>" class="memberName"><%=threadBean.getMemberName()%></a>
      <% } %>
    </td>
    <td align="center"><b><%=threadBean.getThreadReplyCount()%></b></td>
    <td align="center" class="<%=threadBean.getThreadPendingPostCount()>0? "pendingyes" : "pendingno"%>"><%=threadBean.getThreadPendingPostCount()%></td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(threadBean.getThreadLastPostDate())%><br/>
      <% if ((threadBean.getLastPostMemberName()!=null) && (threadBean.getLastPostMemberName().length()>0)) { %>
      <fmt:message key="mvnforum.common.by"/> <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getLastPostMemberName()))%>" class="memberName"><%=threadBean.getLastPostMemberName()%></a>&nbsp;
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID() + "&amp;lastpage=yes#lastpost")%>" class="messageTopic"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/right.gif" border="0" alt="<fmt:message key="mvnforum.common.last"/>" title="<fmt:message key="mvnforum.common.last"/>" /></a>
    </td>
  </tr>
</pg:item>
<%
}//for
if (threadBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="7" align="center"><fmt:message key="mvnforum.user.listthreadswithpendingposts.table.no_threads"/></td></tr>
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
  <% if (threadIconLegend.isHasPriorityHigh()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_HIGH %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_high"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasPriorityNormal()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_NORMAL %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_normal"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasPriorityLow()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_PRIORITY_LOW %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.priority_low"/></td>
  </tr>
  <%}%>
  <% if (hasPoll) {%>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.has_poll"/></td>
  </tr>
  <%} %>
  <% if (hasAnAttachment) { %>
  <tr class="portlet-font">
    <td width="16" align="center"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.has_attachment"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_ACTIVE %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_ACTIVE %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_CLOSED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_CLOSED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotUnreadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_UNREAD_LOCKED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_unread_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasHotReadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_HOT_READ_LOCKED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.hot_read_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_ACTIVE %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadActive()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_ACTIVE %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_active"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_CLOSED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadClosed()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_CLOSED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_closed"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdUnreadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_UNREAD_LOCKED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_unread_locked"/></td>
  </tr>
  <%}%>
  <% if (threadIconLegend.isHasColdReadLocked()) { %>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_COLD_READ_LOCKED %>" border="0" alt="" /></td>
    <td><fmt:message key="mvnforum.common.legend.thread.cold_read_locked"/></td>
  </tr>
  <%}%>
<% if (threadIconLegend.isHasThreadIconLegend() || hasAnAttachment || hasPoll) { %>
  <tr><td></td></tr>
</table>
<br/>
<% } %>  
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
