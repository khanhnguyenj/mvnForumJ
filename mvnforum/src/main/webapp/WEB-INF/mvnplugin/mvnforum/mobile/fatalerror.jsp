<?xml version="1.0" encoding="UTF-8"?>
<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/WEB-INF/mvnplugin/mvnforum/mobile/fatalerror.jsp,v 1.9 2009/05/18 10:46:34 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.9 $
 - $Date: 2009/05/18 10:46:34 $
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
 - @author: Nhan Luu Duy  
 --%>
 <!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
  "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page session="false" isErrorPage="true"%>

<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%
log.error("Assertion in user.fatalerror.jsp", exception); 
%>

<%@ include file="inc_common.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.fatalerror.title"/></mvn:title>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br/>

<div class="padding1px">
    <span><fmt:message key="mvnforum.user.fatalerror.error_info"/></span>
</div>
<br/>

<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>
<%!
    private static final Logger log = LoggerFactory.getLogger("com.mvnforum.mobile.fatalerror.jsp");
%>
