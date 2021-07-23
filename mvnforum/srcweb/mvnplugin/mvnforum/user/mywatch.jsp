<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/mywatch.jsp,v 1.76 2009/07/16 08:46:42 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.76 $
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
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.LocaleMessageUtil"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.mywatch.title"/></mvn:title>
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
  <fmt:message key="mvnforum.user.mywatch.title"/>
</div>
<br/>

<%
Collection globalWatchBeans = (Collection) request.getAttribute("GlobalWatchBeans");
Collection categoryWatchBeans = (Collection) request.getAttribute("CategoryWatchBeans");
Collection forumWatchBeans = (Collection) request.getAttribute("ForumWatchBeans");
Collection threadWatchBeans = (Collection) request.getAttribute("ThreadWatchBeans");

CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
%>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.mywatch.guide"/><br/>
  <%if (globalWatchBeans.size()==0) {%>
    <br/><fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "addwatch")%>"><fmt:message key="mvnforum.user.mywatch.add_watch"/></a><br/>
  <%}%>
</div>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.mywatch.my_global_watch"/>:
    <%if (globalWatchBeans.size()>=1) {%><fmt:message key="mvnforum.common.yes"/><%}else{%><fmt:message key="mvnforum.common.no"/><%}%>
    </td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.user.mywatch.my_global_watch"/></td>
    <td align="center"><fmt:message key="mvnforum.common.watch.type"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.start_on"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.last_sent"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%if (globalWatchBeans.size() == 1) {
    Iterator globalIterator = globalWatchBeans.iterator();
    WatchBean watchBean = (WatchBean) globalIterator.next();
    int watchType = watchBean.getWatchType();
%>
  <tr class="<mvn:cssrow/>">
    <td><b><fmt:message key="mvnforum.user.mywatch.my_global_watch"/></b></td>
    <td align="center"><%=LocaleMessageUtil.getWatchTypeDescFromInt(onlineUser.getLocale(), watchType)%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchCreationDate())%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchLastSentDate())%></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editwatch?watch=" + watchBean.getWatchID())%>" class="command"><fmt:message key="mvnforum.common.action.edit"/></a></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "deletewatchprocess?watch=" + watchBean.getWatchID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a></td>
  </tr>
<%} else {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.mywatch.no_global_watch"/></td></tr>
<%}%>
</mvn:cssrows>
</table>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.mywatch.my_category_watch"/> (<%=categoryWatchBeans.size()%>):</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.category"/></td>
    <td align="center"><fmt:message key="mvnforum.common.watch.type"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.start_on"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.last_sent"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
for (Iterator catIterator = categoryWatchBeans.iterator(); catIterator.hasNext(); ) {
    WatchBean watchBean = (WatchBean)catIterator.next();
    int watchType = watchBean.getWatchType();
    CategoryBean categoryBean = categoryCache.getBean(watchBean.getCategoryID());
%>
  <tr class="<mvn:cssrow/>">
    <td>
      <b><%=categoryBean.getCategoryName()%></b><br/>
      <%=MyUtil.filter(categoryBean.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
    <td align="center"><%=LocaleMessageUtil.getWatchTypeDescFromInt(onlineUser.getLocale(), watchType)%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchCreationDate())%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchLastSentDate())%></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editwatch?watch=" + watchBean.getWatchID())%>" class="command"><fmt:message key="mvnforum.common.action.edit"/></a></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "deletewatchprocess?watch=" + watchBean.getWatchID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a></td>
  </tr>
<%
}//for
if (categoryWatchBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.mywatch.no_category_watch"/></td></tr>
<%}%>
</mvn:cssrows>
</table>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.mywatch.my_forum_watch"/> (<%=forumWatchBeans.size()%>):</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.forum"/></td>
    <td align="center"><fmt:message key="mvnforum.common.watch.type"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.start_on"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.last_sent"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
for (Iterator forumIterator = forumWatchBeans.iterator(); forumIterator.hasNext(); ) {
    WatchBean watchBean = (WatchBean)forumIterator.next();
    int watchType = watchBean.getWatchType();
    ForumBean forumBean = forumCache.getBean(watchBean.getForumID());
%>
  <tr class="<mvn:cssrow/>">
    <td>
      <b><%=forumBean.getForumName()%></b><br/>
      <%=MyUtil.filter(forumBean.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
    <td align="center"><%=LocaleMessageUtil.getWatchTypeDescFromInt(onlineUser.getLocale(), watchType)%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchCreationDate())%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchLastSentDate())%></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editwatch?watch=" + watchBean.getWatchID())%>" class="command"><fmt:message key="mvnforum.common.action.edit"/></a></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "deletewatchprocess?watch=" + watchBean.getWatchID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a></td>
  </tr>
<%
}//for
if (forumWatchBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.mywatch.no_forum_watch"/></td></tr>
<%}%>
</mvn:cssrows>
</table>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.mywatch.my_thread_watch"/> (<%=threadWatchBeans.size()%>):</td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="3"><fmt:message key="mvnforum.common.thread"/></td>
    <td align="center"><fmt:message key="mvnforum.common.watch.type"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.start_on"/></td>
    <td align="center"><fmt:message key="mvnforum.user.mywatch.last_sent"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
for (Iterator threadIterator = threadWatchBeans.iterator(); threadIterator.hasNext(); ) {
    WatchBean watchBean = (WatchBean)threadIterator.next();
    int watchType = watchBean.getWatchType();
    ThreadBean threadBean = watchBean.getThreadBean();
%>
  <tr class="<mvn:cssrow/>">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=MyUtil.getThreadIconName(onlineUser.getLastLogonTimestamp().getTime(), threadBean.getThreadLastPostDate().getTime(), threadBean.getThreadReplyCount()+1, threadBean.getThreadStatus())%>" border="0" alt=""></td>
    <td width="16"><%=EnableEmotionFilter.filter(threadBean.getThreadIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%></td>
    <td>
      <a class="messageTopic" href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID())%>">
        <%=MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
      <br/>
      <%=MyUtil.filterWithoutBBCode(StringUtil.getShorterString(threadBean.getThreadBody(), MVNForumConfig.getMaxCharsInShortSummary()), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
    </td>
    <td align="center"><%=LocaleMessageUtil.getWatchTypeDescFromInt(onlineUser.getLocale(), watchType)%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchCreationDate())%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(watchBean.getWatchLastSentDate())%></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editwatch?watch=" + watchBean.getWatchID())%>" class="command"><fmt:message key="mvnforum.common.action.edit"/></a></td>
    <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "deletewatchprocess?watch=" + watchBean.getWatchID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a></td>
  </tr>
<%
}//for

if (threadWatchBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.mywatch.no_thread_watch"/></td></tr>
<%}%>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
