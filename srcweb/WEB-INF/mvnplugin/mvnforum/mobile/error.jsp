<?xml version="1.0" encoding="UTF-8"?>
<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/WEB-INF/mvnplugin/mvnforum/mobile/error.jsp,v 1.10 2009/04/29 08:12:45 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.10 $
 - $Date: 2009/04/29 08:12:45 $
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
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>

<%@ include file="inc_common.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.error.title"/></mvn:title>
  <link href="<%=contextPath%>/mvnplugin/mvnforum/css/styleMobile.css" rel="stylesheet" type="text/css"/>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>

<div class="padding1px">
    <a class="padding1px" href="index"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.user.error.title"/>
</div>

<div class="padding1px">
    <fmt:message key="mvnforum.user.error.prompt"/>:<br/>
    <b><i><span class="padding1px"><%=DisableHtmlTagFilter.filter((String)session.getAttribute("ErrorMessage"))%></span></i></b>
</div>

<%session.setAttribute("ErrorMessage", null); %>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>