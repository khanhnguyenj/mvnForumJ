<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/logframe.jsp,v 1.28 2009/03/13 01:43:01 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.28 $
 - $Date: 2009/03/13 01:43:01 $
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
<mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.logframe.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%
String log = ParamUtil.getParameterFilter(request, "log");
String[] lines = (String[])request.getAttribute("ContentLog");
%>
<%=log%>
<table width="100%" class="noborder" cellspacing="0" cellpadding="5" align="center">
<%
int index = 0;
%>
<mvn:cssrows>
<%
for (int i = 0; i < lines.length; i++) {
  if (lines[i] == null) break;// all last elements is null
  if ( (log.equals("ALL") == false) && (lines[i].indexOf(log) < 0) ) continue; %>
  <tr class="<mvn:cssrow/>">
    <td class="portlet-section-subheader"><%=index++ +1%></td>
    <td nowrap="nowrap">
      <% if ( (lines[i].indexOf("WARN") >= 0) || (lines[i].indexOf("ERROR") >= 0) || (lines[i].indexOf("FATAL") >= 0) ) {%>
        <span class="warning"><pre><%=lines[i]%></pre></span>
      <%} else {%>
        <pre><%=lines[i]%></pre>
      <%}%>
    </td>
  </tr>
<%}//for%>
</mvn:cssrows>
</table>
</mvn:body>
</mvn:html>
</fmt:bundle>