<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addmessagesuccess.jsp,v 1.39 2009/10/27 10:51:01 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.39 $
 - $Date: 2009/10/27 10:51:01 $
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

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
boolean attachMore = ((Boolean)request.getAttribute("AttachMore")).booleanValue();
boolean addToSentFolder = ((Boolean)request.getAttribute("AddToSentFolder")).booleanValue();
String overQuotaReceivers = ParamUtil.getAttribute(request, "OverQuotaReceivers");
if (overQuotaReceivers == null) overQuotaReceivers = "";
Boolean addSentFolderOverQuota = (Boolean)request.getAttribute("AddSentFolderOverQuota");

boolean goAutomatically = (overQuotaReceivers.length() == 0) && (addSentFolderOverQuota == null);

String defaultLink = "mymessage"; // Return to mymessage
String attachLink = "addmessageattachment";
if (attachMore) {
    int messageID = ((Integer)request.getAttribute("MessageID")).intValue();
    attachLink = attachLink + "?message=" + messageID;
    if (addToSentFolder) {
        attachLink = attachLink + "&AddToSentFolder=true";
    }
    defaultLink = attachLink;
}
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.addmessagesuccess.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<% if (goAutomatically) { %>
<meta http-equiv="refresh" content="3; url=<%=urlResolver.encodeURL(request, response, defaultLink)%>"/>
<% } %>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.addmessagesuccess.title"/>
</div>
<br/>

<% if ((overQuotaReceivers.length() > 0) || (addSentFolderOverQuota != null)) { %>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.addmessagesuccess.success_message_with_warning"/><br/>
  <% if (overQuotaReceivers.length() > 0) { %>
    <b>&raquo;&nbsp;<i><span class="warning"><fmt:message key="mvnforum.user.addmessagesuccess.over_quota_list"/> <%=overQuotaReceivers%></span></i></b><br/>
  <% } %>
  <% if (addSentFolderOverQuota != null) { %>
    <b>&raquo;&nbsp;<i><span class="warning"><fmt:message key="mvnforum.user.addmessagesuccess.over_quota_cannot_add_to_sent"/></span></i></b><br/>
  <% } %>
</div>
<br/>
<% } %>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.success.prompt"/></td>
  </tr>
<% if (attachMore) { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, attachLink)%>"><fmt:message key="mvnforum.user.success.go_attach_file"/></a>
  <% if (goAutomatically) { %>
      (<fmt:message key="mvnforum.common.success.automatic"/>)
  <% } %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.success.go_mymessage"/></a></td>
  </tr>
<% } else { %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.success.go_mymessage"/></a>
  <% if (goAutomatically) { %>
      (<fmt:message key="mvnforum.common.success.automatic"/>)
  <% } %>
    </td>
  </tr>
<% } %>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.success.go_myprofile"/></a></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><b>&raquo;&nbsp;</b><a class="command" href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.user.success.go_index"/></a></td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
