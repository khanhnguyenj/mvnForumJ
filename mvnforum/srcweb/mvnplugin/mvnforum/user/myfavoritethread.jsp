<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/myfavoritethread.jsp,v 1.90 2009/07/16 08:46:42 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.90 $
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
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.common.ThreadIconLegend" %>
<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.myfavoritethread.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.myfavoritethread.title"/>
</div>
<br/>

<%
boolean hasAnAttachment = false;
boolean hasPoll = false;
Collection threadBeans = (Collection) request.getAttribute("ThreadBeans");
ForumCache forumCache = ForumCache.getInstance();
double leftWidth = ((Double)request.getAttribute("QuotaRatio")).doubleValue();
ThreadIconLegend threadIconLegend = new ThreadIconLegend();
final double BAR_WIDTH = 300;
final double BAR_BORDER = 5;
String percentImage = "";
if (leftWidth <= 30) {
  percentImage = "percent_low.gif";
} else if (leftWidth <= 70) {
  percentImage = "percent_med.gif";
} else {
  percentImage = "percent_high.gif";
}
%>
<table width="95%" align="center" class="portlet-font">
  <tr>
    <td>&nbsp;<%-- this is need by IE --%></td>
    <td width="<%=BAR_WIDTH + BAR_BORDER%>" align="right">
      <table width="<%=BAR_WIDTH + BAR_BORDER%>" class="noborder" cellspacing="<%=BAR_BORDER%>" cellpadding="1" bgcolor="#AABBCC">
        <tr bgcolor="#FFFFFF">
          <td align="left" height="17" nowrap="nowrap" class="portlet-font">
            <img align="left" width="<%=Math.floor(leftWidth*(BAR_WIDTH/100))%>" height="15" src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=percentImage%>" vspace="0" hspace="0" alt=""/>
          </td>
        </tr>
        <tr><td height="12" align="center" class="portlet-msg-status"><%=Math.round(leftWidth)%>% <fmt:message key="mvnforum.common.of"/> <%=MVNForumConfig.getMaxFavoriteThreads()%> <fmt:message key="mvnforum.common.threads"/></td></tr>
      </table>
    </td>
  </tr>
</table>

<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="3"><fmt:message key="mvnforum.common.thread"/></td>
    <td><fmt:message key="mvnforum.common.forum"/></td>
    <td align="center"><fmt:message key="mvnforum.common.post.author"/></td>
    <td align="center"><fmt:message key="mvnforum.common.reply_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.view_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.last_post"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
    ThreadBean threadBean = (ThreadBean)iterator.next();
    String threadIcon = MyUtil.getThreadIconName(onlineUser.getLastLogonTimestamp().getTime(), threadBean.getThreadLastPostDate().getTime(), threadBean.getThreadReplyCount()+1, threadBean.getThreadStatus());
    threadIconLegend.updateIconLegend(threadIcon);
%>
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
    </td>
    <td><a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + threadBean.getForumID())%>" class="messageTopic"><%=forumCache.getBean(threadBean.getForumID()).getForumName()%></a></td>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getMemberName()))%>" class="memberName"><%=threadBean.getMemberName()%></a>
    </td>
    <td align="center"><%=threadBean.getThreadReplyCount()%></td>
    <td align="center"><%=threadBean.getThreadViewCount()%></td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(threadBean.getThreadLastPostDate())%><br/><fmt:message key="mvnforum.common.by"/>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(threadBean.getLastPostMemberName()))%>" class="memberName"><%=threadBean.getLastPostMemberName()%></a>&nbsp;
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID() + "&amp;lastpage=yes#lastpost")%>" class="messageTopic"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/right.gif" border="0" alt="<fmt:message key="mvnforum.common.last"/>" title="<fmt:message key="mvnforum.common.last"/>"/></a>
    </td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "deletefavoritethreadprocess?thread=" + threadBean.getThreadID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a></td>
  </tr>
<%
}//for
if (threadBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="9" align="center"><fmt:message key="mvnforum.user.myfavoritethread.table.no_favorite_threads"/></td></tr>
<%}%>
</mvn:cssrows>
</table>
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
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=ThreadIconLegend.THREAD_ICON_TYPE_FORUM_ANNOUNCEMENT %>" border="0"alt=""/></td>
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
  <%} %>
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
