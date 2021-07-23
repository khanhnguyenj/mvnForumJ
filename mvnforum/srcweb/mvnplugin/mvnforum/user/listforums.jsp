<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listforums.jsp,v 1.177 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.177 $
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
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.common.*" %>
<%@ page import="com.mvnforum.auth.OnlineUserAction" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listforums.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath(request)%>" rel="stylesheet" type="text/css" />
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/listforums.js" type="text/javascript"></script>
<script type="text/javascript">
//<![CDATA[
function start() {
  <% if (request.getAttribute("id") != null ) { %>
    execute1("<%=request.getAttribute("id")%>");
  <% } else { %>
    <%if (MVNForumConfig.getEnableExpanseCategoryTree() == false) { %>
      init();
    <%}%>  
  <%}%>  
}
//]]>
</script>
</mvn:head>
<mvn:body onload="start();">
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>
<br/>
<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
ThreadIconLegend threadIconLegend = new ThreadIconLegend();

if (MVNForumConfig.getEnableMostActiveThreads()) {
  boolean hasAnAttachment = false;
  boolean hasPoll = false;

%>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.common.most_active_threads_since_last_week"/></td>
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

<%
  int index = 0;
  Collection mostActiveThreads = (Collection)request.getAttribute("MostActiveThreads");
  int memberPostsPerPage = onlineUser.getPostsPerPage();
%>
<mvn:cssrows>
<%
  for (Iterator iter = mostActiveThreads.iterator(); iter.hasNext(); ) {
     ActiveThread thread = (ActiveThread)iter.next();
     index++;
     String threadIcon = MyUtil.getThreadIconName(onlineUser.getLastLogonTimestamp().getTime(), thread.getLastDate().getTime(), thread.getReplyCount()+1, thread.getThreadStatus());
     threadIconLegend.updateIconLegend(threadIcon);
     
     int lastPostCount = thread.getLastPostCount();
     int postCount = thread.getReplyCount() + 1;
     int totalPages = (postCount / memberPostsPerPage) + ( (postCount % memberPostsPerPage) == 0 ? 0 : 1);
%>
  <tr class="<mvn:cssrow/>">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadIcon%>" border="0" alt=""/></td>
    <td width="16"><%=EnableEmotionFilter.filter(thread.getIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%></td>
    <td>
     <% 
       String typeImage = MyUtil.getThreadTypeIcon(thread.getThreadType());
       threadIconLegend.updateIconLegend(typeImage);
       String threadPriorityIcon = MyUtil.getThreadPriorityIcon(thread.getThreadPriority());
       threadIconLegend.updateIconLegend(threadPriorityIcon);
     %>
      <% if (thread.getThreadType() != ThreadBean.THREAD_TYPE_DEFAULT) { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=typeImage%>" alt="" />
      <% } %>
      <% if ( ( (thread.getThreadPriority() == ThreadBean.THREAD_PRIORITY_LOW) && MVNForumConfig.getEnableLowPriorityIcon() ) || ( (thread.getThreadPriority() == ThreadBean.THREAD_PRIORITY_NORMAL) && MVNForumConfig.getEnableNormalPriorityIcon()) || ( (thread.getThreadPriority() == ThreadBean.THREAD_PRIORITY_HIGH) && MVNForumConfig.getEnableHighPriorityIcon() ) ) { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadPriorityIcon%>" border="0" alt="" />
      <% } %>
      <% if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE && MVNForumConfig.getEnablePoll()) {
           if (thread.getPollCount() > 0) {
              hasPoll = true; %>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/>
      <%   }
         }
      %>
      <% if (thread.getAttachCount() > 0) {
           hasAnAttachment = true; %>
         <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" title="<%=thread.getAttachCount()%> <% if (thread.getAttachCount() == 1) {%><fmt:message key="mvnforum.common.attachment"/><% } else {%><fmt:message key="mvnforum.common.attachments"/><%}%>" alt=""/>
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + thread.getThreadID())%>" class="messageTopic"><%=MyUtil.filter(thread.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a><br/>
      (<b><%=lastPostCount%></b> <% if (lastPostCount == 1) {%><fmt:message key="mvnforum.common.new_post"/><% } else { %><fmt:message key="mvnforum.common.new_posts"/><% } %>)
      <%int threadID = thread.getThreadID();%>
      <%@ include file="inc_pages.jsp"%>
      <%if ( (index == 1) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ACTIVE_THREAD) > 0) ) {%>
        <br/>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ACTIVE_THREAD))%>
      <%}%>
    </td>
    <td>
      <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + thread.getForumID())%>" class="messageTopic"><%=forumCache.getBean(thread.getForumID()).getForumName()%></a>
    </td>
    <td align="center">
      <% if ((thread.getAuthor()!=null) && (thread.getAuthor().length()>0)) { %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(thread.getAuthor()))%>" class="memberName"><%=thread.getAuthor()%></a>
      <% } %>
    </td>
    <td align="center"><b><%=thread.getReplyCount()%></b></td>
    <td align="center"><b><%=thread.getViewCount()%></b></td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(thread.getLastDate())%><br/>
      <% if ((thread.getLastMember()!=null) && (thread.getLastMember().length()>0)) { %>
      <fmt:message key="mvnforum.common.by"/> <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(thread.getLastMember()))%>" class="memberName"><%=thread.getLastMember()%></a>&nbsp;
      <% } %>
      <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + thread.getThreadID() + "&amp;lastpage=yes#lastpost")%>" class="messageTopic"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/right.gif" border="0" alt="<fmt:message key="mvnforum.common.last"/>" title="<fmt:message key="mvnforum.common.last"/>"/></a>
    </td>
  </tr>
<% } // end for each most active thread%>
<% if (mostActiveThreads.size() == 0) { %>
  <tr class="<mvn:cssrow/>">
    <td colspan="8" align="center"><fmt:message key="mvnforum.common.no_active_threads"/></td>
  </tr>
<% }// if no active threads %>
</mvn:cssrows>
</table>
<br/>
<% if (threadIconLegend.isHasThreadIconLegend() || hasAnAttachment || hasPoll) { %>
  <table width="95%" class="noborder" align="center">
<% } %>
  <% if (hasPoll) {%>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/poll.gif" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.thread.has_poll"/></td>
  </tr>
  <%}%>
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
<%} // end of enable most active threads %>


<%
int numberOfThreads = MyUtil.getViewableThreads(forumBeans, permission);
int numberOfPosts = MyUtil.getViewablePosts(forumBeans, permission);

//Copy code from listonlineusers.jsp
Collection onlineUserActions = (Collection)request.getAttribute("OnlineUserActions");
int guestCount = 0;
int memberCount = 0;
int invisibleMemberCount = 0;
int visibleMemberCount = 0;
boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
for (Iterator countIterator = onlineUserActions.iterator(); countIterator.hasNext(); ) {
    OnlineUserAction onlineUserAction = (OnlineUserAction)countIterator.next();
    boolean invisible = onlineUserAction.isInvisibleMember();
    int mID = onlineUserAction.getMemberID();
    if ( (mID==0) || (mID==MVNForumConstant.MEMBER_ID_OF_GUEST) ) {
        guestCount++;
    } else if (enableInvisible) {
        if (invisible) {
           invisibleMemberCount++;
        } else {
           memberCount++;
        }
    } else { // disable invisible feature
        memberCount++;
    }
}
visibleMemberCount = memberCount - invisibleMemberCount;
%>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.listforums.table.desc"/> (<%=numberOfPosts%> <fmt:message key="mvnforum.common.posts"/> <fmt:message key="mvnforum.common.in"/> <%=numberOfThreads%> <fmt:message key="mvnforum.common.threads"/>)</td>
  </tr>
</table>
<%
boolean hasANoChangeForum = false;
boolean hasAChangedForum = false;
%>
<%-- 
<table class="tborder" width="95%" cellspacing="1" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.listforums.table.forum_name_desc"/></td>
    <td align="center"><fmt:message key="mvnforum.common.thread_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.post_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.last_post"/></td>
  </tr>


<%
boolean hasANoChangeForum = false;
boolean hasAChangedForum = false;
for (Iterator catIterator = categoryBeans.iterator(); catIterator.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIterator.next();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission) == false) continue;
%>
 <tr class="portlet-section-subheader">
    <td colspan="5">
      <a onclick="showhide('category_<%=categoryBean.getCategoryID()%>');return false" href="javascript:void(0)">
      <img align="middle" border="0" height="13" width="14" src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/arrow-subnav-down.gif" alt=""/></a>
      <b><%=categoryBean.getCategoryName()%></b><br/>
      <span class="portlet-font"><%=MyUtil.filter(categoryBean.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></span>
    </td>
  </tr>
  <tbody id="category_<%=categoryBean.getCategoryID()%>">
<%
    int forumCountInCurrentCategory = 0;
    for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIterator.next();
        if ((forumBean.getCategoryID() == categoryBean.getCategoryID()) && permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
            //forumCountInCurrentCategory++;
            String forumIcon = MyUtil.getForumIconName(onlineUser.getLastLogonTimestamp().getTime(), forumBean.getForumLastPostDate().getTime());
            if (forumIcon.equals("f_norm_no.gif")) {
                hasANoChangeForum = true;
            } else if (forumIcon.equals("f_norm_new.gif")) {
                hasAChangedForum = true;
            }
            String color = (forumCountInCurrentCategory++%2 == 0 ? "portlet-section-body" : "portlet-section-alternate");
%>
  <tr class="<%=color%>">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=forumIcon%>" border="0" alt=""/></td>
    <td>
      <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumBean.getForumID())%>" class="messageTopic"><%=forumBean.getForumName()%></a><br/>
      <%=MyUtil.filter(forumBean.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
    <td align="center"><b><%=forumBean.getForumThreadCount()%></b></td>
    <td align="center"><b><%=forumBean.getForumPostCount()%></b></td>
    <td align="center" nowrap="nowrap">
<%if ( (forumBean.getLastPostMemberName().length() == 0) || (forumBean.getForumThreadCount() == 0) ) {%>
    <fmt:message key="mvnforum.user.listforums.table.no_post"/>
<%} else {%>
    <%=onlineUser.getGMTTimestampFormat(forumBean.getForumLastPostDate())%><br/>
    <fmt:message key="mvnforum.common.by"/>
    <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(forumBean.getLastPostMemberName()))%>" class="memberName"><%=forumBean.getLastPostMemberName()%></a>
<%} // else %>
    </td>
  </tr>
<%
        }//if the forum is in the current category
    } //for forumIndex

    if (forumCountInCurrentCategory == 0) {
%>
  <tr class="portlet-section-body">
    <td colspan="5" align="center"><fmt:message key="mvnforum.user.listforums.table.no_forum"/></td>
  </tr>
<%
    }// if no forum in this category%>
  </tbody>
<%} //for catIndex
%>
<% if (categoryBeans.size() == 0) { %>
  <tr class="portlet-section-body">
    <td colspan="5" align="center"><fmt:message key="mvnforum.user.listforums.table.no_category"/></td>
  </tr>
<% }// if no category %>


</table>
<br/>
<table width="95%" class="noborder" align="center">
  <% if (hasANoChangeForum) {%>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/f_norm_no.gif" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.forum.read_active"/></td>
  </tr>
  <%}%>
  <% if (hasAChangedForum) {%>
  <tr class="portlet-font">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/f_norm_new.gif" border="0" alt=""/></td>
    <td><fmt:message key="mvnforum.common.legend.forum.unread_active"/></td>
  </tr>
  <%}%>
</table>
--%>
<%= request.getAttribute("Result") %>
<br/>
<%
if (MVNForumConfig.getEnableSiteStatisticsOverview() || MVNForumConfig.getEnableMostActiveMembers() || MVNForumConfig.getEnableOnlineUsers()) {
%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.general_information"/></td>
  </tr>
<% if (MVNForumConfig.getEnableSiteStatisticsOverview()) {%>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/stats.gif" alt=""/></td>
    <td width="100%">
      <fmt:message key="mvnforum.common.totalcategories"/>: <b><%=MyUtil.getViewableCategories(categoryBeans, permission)%></b><br/>
      <fmt:message key="mvnforum.common.totalforum"/>: <b><%=MyUtil.getViewableForums(forumBeans, permission)%></b><br/>
      <fmt:message key="mvnforum.common.totaltopic"/>: <b><%=numberOfThreads%></b><br/>
      <fmt:message key="mvnforum.common.totalpost"/>: <b><%=numberOfPosts%></b><br/>
      
   <% if (MVNForumConfig.getEnableListNewMembersInRecentDays()) {
        long numberOfMembers = ((Integer)request.getAttribute("NumberOfMembers")).intValue();
        Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
        MemberBean lastMember = (MemberBean) request.getAttribute("MemberBean");
        %>
        <fmt:message key="mvnforum.common.totalmember"/>: <b><%=numberOfMembers%></b><br/>
        <fmt:message key="mvnforum.common.latestmember"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(lastMember.getMemberName()))%>" class="memberName"><%=lastMember.getMemberName()%></a><br/>
        <% 
         if (memberBeans.size() == 0) { %>
           <fmt:message key="mvnforum.common.norecentdaysmembers">
             <fmt:param><%=MVNForumConfig.getDaysToShowRecentMembers()%></fmt:param>
           </fmt:message>
       <%} else { %>
           <fmt:message key="mvnforum.common.recentdaysmembers">
             <fmt:param><%=MVNForumConfig.getDaysToShowRecentMembers()%></fmt:param>
           </fmt:message>
        <% for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
             MemberBean memberBean = (MemberBean) iterator.next();
             String mName = memberBean.getMemberName();
             if (mName.equals(MVNForumConfig.getDefaultGuestName())) { %>
               <%=mName%>
           <%} else { %>
                <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(mName))%>" class="memberName"><%=mName%></a>
          <% }
             if (iterator.hasNext()) { %>
               ,
           <%} //if
           } // for
         } // else
      } // list members in recent days %>
    </td>
  </tr>
 <%}%>
<%if (MVNForumConfig.getEnableMostActiveMembers() || MVNForumConfig.getEnableOnlineUsers()) {%>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user_stat.gif" alt=""/></td>
    <td width="100%">
 <% if (MVNForumConfig.getEnableMostActiveMembers()) {%>
   <% Collection mostActiveMembers = (Collection)request.getAttribute("MostActiveMembers"); %>
   <% if (mostActiveMembers.size() == 0) { %>
      <fmt:message key="mvnforum.common.no_active_members"/>
   <% } else { %>
      <fmt:message key="mvnforum.common.most_active_members_since_last_week"/>:
     <% for (Iterator iter = mostActiveMembers.iterator(); iter.hasNext(); ) {
          ActiveMember member = (ActiveMember)iter.next();
          int postCount = member.getLastPostCount(); %>
         <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(member.getMemberName()))%>" class="memberName"><%=member.getMemberName()%></a>
        (<%=postCount%>
        <% if (postCount == 1) {%><fmt:message key="mvnforum.common.new_post"/><% } else { %><fmt:message key="mvnforum.common.new_posts"/><% } %>)
       <% if (iter.hasNext()) {%>
            , <%-- add comma at here if the element is not the last element--%>
       <% } // end if
        } // end for%>
   <% }// if no active members %>
   <br/>
<% } // end of enable active members %>
<%if (MVNForumConfig.getEnableOnlineUsers()) {%>
      <fmt:message key="mvnforum.common.there_are"/> <b><%=onlineUserActions.size()%></b> <fmt:message key="mvnforum.common.online_users"/>
      (<b><%=guestCount%></b> <%if (guestCount>1) {%><fmt:message key="mvnforum.common.guests"/><%}else{%><fmt:message key="mvnforum.common.guest"/><%}%>,
      <b><%=memberCount%></b> <%if (memberCount>1) {%><fmt:message key="mvnforum.common.members"/><%}else{%><fmt:message key="mvnforum.common.member"/><%}%>
      <%if (enableInvisible) {%>
         ,<%-- last comma if folling exists --%>
         <b><%=invisibleMemberCount%></b> <%if (invisibleMemberCount>1) {%><fmt:message key="mvnforum.common.member.online.invisible_members"/><%} else {%><fmt:message key="mvnforum.common.member.online.invisible_member"/><%}%>
      <%}%>)
      <br/>
      <%if (visibleMemberCount > 0) {%>
      <fmt:message key="mvnforum.common.online_users"/>:
      <%}%>
        <%boolean canAdmin = permission.canAdminSystem();
          for (Iterator iterator = onlineUserActions.iterator(); iterator.hasNext(); ) {
            OnlineUserAction onlineUserAction = (OnlineUserAction)iterator.next();
            boolean userInvisible = onlineUserAction.isInvisibleMember();
            int mID = onlineUserAction.getMemberID();
            String mName = onlineUserAction.getMemberName();
            if ( (mID!=0) && (mID!=MVNForumConstant.MEMBER_ID_OF_GUEST) ) { %>
             <%if (enableInvisible && userInvisible && !canAdmin) { %>
                <%--<span class="memberName"><fmt:message key="mvnforum.common.member.online.invisible_member"/></span>--%>
             <%} else {%>
                <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(mName))%>" class="memberName"><%=mName%></a>
                <%if (enableInvisible && userInvisible && canAdmin) { %>(<fmt:message key="mvnforum.common.member.online.invisible"/>)<%}%>
             <%}%>
             <% if (onlineUserAction.getSessionCount() > 1) { %>
                (<%=onlineUserAction.getSessionCount()%> <fmt:message key="mvnforum.common.member.online.sessions"/>)
             <% } %>
          <%} /*if (!guest)*/%>
        <%} /*for*/%>
<% } // if enable online users%>
    </td>
  </tr>
<% } // end of enable any of 2 options %>
</mvn:cssrows>
</table>
<% } // end of enable any of 3 options %>
<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
