<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/mymessagefolder.jsp,v 1.45 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.45 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.common.messagefolder.title"/></mvn:title>
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
  <a href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.mymessage.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.messagefolder.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.user.mymessagefolder.guide"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <a class="command" href="<%=urlResolver.encodeURL(request, response, "addmessagefolder")%>"><fmt:message key="mvnforum.common.messagefolder.title.createnewfolder"/></a><br/>
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.messagefolder.title.foldername"/></td>
    <td align="center"><fmt:message key="mvnforum.common.order"/></td>
    <td align="center"><fmt:message key="mvnforum.common.messagefolder.title.total_messages"/></td>
    <td align="center"><fmt:message key="mvnforum.common.messagefolder.title.unread_messages"/></td>
    <td align="center"><fmt:message key="mvnforum.common.messagefolder.title.foldercreationdate"/></td>
    <td align="center"><fmt:message key="mvnforum.common.messagefolder.title.foldermodifieddate"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action"/></td>
  </tr>
<%
Collection messageFolderBeans = (Collection) request.getAttribute("FolderMessageBeans");
for (Iterator iterator = messageFolderBeans.iterator(); iterator.hasNext(); ) {
    MessageFolderBean messageFolderBean = (MessageFolderBean)iterator.next();
    String folderName = messageFolderBean.getFolderName();
    String folderImageSource = "";
    if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_INBOX)) {
      folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_inbox.gif";
    } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
      folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_draft.gif";
    } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_TRASH)) {
      folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_trash.gif";
    } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_SENT)) {
      folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_sent.gif";
    } else {
      folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_default.gif";
    }
%>
  <tr class="<mvn:cssrow/>">
    <td align="left">
      <img src="<%=folderImageSource%>" alt="<%=folderName%>" title="<%=folderName%>" style="vertical-align:middle;" border="0" height="19" width="19"/>
      <a href="<%=urlResolver.encodeURL(request, response, "mymessage?folder=" + folderName)%>"><%=MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), folderName)%></a>
    </td>
    <td align="center">
      <table width="100%" class="noborder">
        <tr class="<mvn:cssrow autoIncrease="false"/>">
          <td width="30%" align="center">
            <% if (messageFolderBean.getFolderOrder() > 0) { %>
            <a href="<%=urlResolver.encodeURL(request, response, "updatefolderorder?folder=" + folderName + "&amp;action=up&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_up"/>" title="<fmt:message key="mvnforum.common.order.move_up"/>"/></a>
            <% } %>
          </td>
          <td width="40%" align="center"><b><%=messageFolderBean.getFolderOrder()%></b></td>
          <td width="30%" align="center"><a href="<%=urlResolver.encodeURL(request, response, "updatefolderorder?folder=" + folderName + "&amp;action=down&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/down.gif" border="0" alt="<fmt:message key="mvnforum.common.order.move_down"/>" title="<fmt:message key="mvnforum.common.order.move_down"/>"/></a></td>
        </tr>
      </table>
    </td>
    <td align="center"><b><%=messageFolderBean.getMessageCount()%></b></td>
    <td align="center"><b><%=messageFolderBean.getUnreadMessageCount()%></b></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(messageFolderBean.getFolderCreationDate())%></td>
    <td align="center"><%=onlineUser.getGMTTimestampFormat(messageFolderBean.getFolderModifiedDate())%></td>
    <td align="center">
    <%
    if ((!folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_INBOX)) &&
        (!folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_SENT)) &&
        (!folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) &&
        (!folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_TRASH))) {
    %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletemessagefolder?folder=" + folderName)%>"><fmt:message key="mvnforum.common.action.delete"/></a>
    <% } else { %>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <% } %>
    </td>
  </tr>
<%
}//for 
%>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
