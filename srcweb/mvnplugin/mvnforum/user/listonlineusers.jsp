<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listonlineusers.jsp,v 1.70 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.70 $
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
<%@ page import="java.sql.Timestamp" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter"%>
<%@ page import="net.myvietnam.mvncore.web.impl.GenericRequestServletImpl" %>
<%@ page import="com.mvnforum.auth.OnlineUserAction" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listonlineusers.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.listonlineusers.title"/>
</div>
<br/>

<%
Collection onlineUserActions = (Collection) request.getAttribute("OnlineUserActions");
int guestCount = 0;
int memberCount = 0;
int invisibleMemberCount = 0;
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
} // end of for%> 

<table width="95%" align="center">
  <tr>
    <td class="highlight">
    <fmt:message key="mvnforum.common.there_are"/> <b><%=onlineUserActions.size()%></b> <fmt:message key="mvnforum.common.online_users"/> 
    (<b><%=guestCount%></b> <%if (guestCount>1) {%><fmt:message key="mvnforum.common.guests"/><%} else {%><fmt:message key="mvnforum.common.guest"/><%}%>, 
     <b><%=memberCount%></b> <%if (memberCount>1) {%><fmt:message key="mvnforum.common.members"/><%} else {%><fmt:message key="mvnforum.common.member"/><%}%>
     <%if (enableInvisible) {%>
         ,<%-- last comma if folling exists --%>
         <b><%=invisibleMemberCount%></b> <%if (invisibleMemberCount>1) {%><fmt:message key="mvnforum.common.member.online.invisible_members"/><%} else {%><fmt:message key="mvnforum.common.member.online.invisible_member"/><%}%>
     <%}%>
    )
    </td>
  </tr>
</table>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.user"/></td>
    <td><fmt:message key="mvnforum.user.listonlineusers.table.what_are_doing"/></td>
    <td align="center"><fmt:message key="mvnforum.user.listonlineusers.table.duration_since_last_activity"/></td>
    <td align="center"><fmt:message key="mvnforum.user.listonlineusers.table.online_time"/></td>
  </tr>
<%
Timestamp now = DateUtil.getCurrentGMTTimestamp();
GenericRequest genericRequest = new GenericRequestServletImpl(request);
//boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
boolean canAdmin = permission.canAdminSystem();
for (Iterator iterator = onlineUserActions.iterator(); iterator.hasNext(); ) {
    OnlineUserAction onlineUserAction = (OnlineUserAction)iterator.next();
    boolean userInvisible = onlineUserAction.isInvisibleMember();
%>
  <tr class="<mvn:cssrow/>">
    <td>
    <%
    int mID = onlineUserAction.getMemberID();
    String mName = onlineUserAction.getMemberName();
    if ( ( mID == 0) || (mID==MVNForumConstant.MEMBER_ID_OF_GUEST) ) {
      if ( (mName == null) || (mName.length()==0) ) {
        mName = MVNForumConfig.getDefaultGuestName();
      } %>
      <span class="memberName"><%=mName%></span>
 <% } else {
      if (enableInvisible && userInvisible && !canAdmin) { %>
        <span class="memberName"><fmt:message key="mvnforum.common.member.online.invisible_member"/></span>
    <%} else {%>
        <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(mName))%>" class="memberName"><%=mName%></a> 
        <%if (enableInvisible && userInvisible && canAdmin) { %>(<fmt:message key="mvnforum.common.member.online.invisible"/>)<%}%>
    <%}%>
    <%if (onlineUserAction.getSessionCount() > 1) { %>
        (<%=onlineUserAction.getSessionCount()%> <fmt:message key="mvnforum.common.member.online.sessions"/>)
    <%}
    } //else %>
    </td>
    <td>
    <%
    String desc = onlineUserAction.getDesc(genericRequest);
    String url = onlineUserAction.getUrl();
    if (desc == null) {
      desc = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.member.online.unknow_action");
    }
    if (url == null) {
      out.print(desc);
    } else {
    %>
    <a href="<%=urlResolver.encodeURL(request, response, url)%>"><%=desc%></a>
    <% } %>
<%if (permission.canAdminSystem()) {%>
    <br/>
    [<%=onlineUserAction.getRemoteAddr()%>] <%=DisableHtmlTagFilter.filter(onlineUserAction.getUserAgent())%>
<%}/*if can admin system*/%>
    </td>
    <td align="center"><%=onlineUserAction.getDurationSinceLastRequestDesc(now, onlineUser.getLocale())%></td>
    <td align="center"><%=onlineUserAction.getOnlineDurarionDesc(now, onlineUser.getLocale())%></td>
  </tr>
<% }//for %>
</table>
</mvn:cssrows>
<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
