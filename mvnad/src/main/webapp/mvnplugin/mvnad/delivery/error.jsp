<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/delivery/error.jsp,v 1.5 2009/05/05 10:35:21 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.5 $
 - $Date: 2009/05/05 10:35:21 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2008 by MyVietnam.net
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
 - @author: MyVietnam.net developers
 -
 --%>
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.util.MailUtil" %>
<%@ page import="com.mvnforum.MVNForumConfig" %>

<%@ taglib uri="mvntaglib" prefix="mvn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.error.title"/></mvn:title>
<link href="<%=request.getContextPath()%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/mvnplugin/mvnad/js/myvietnam.js"></script>
</mvn:head>
<mvn:body>

<div class="pagedesc">
  <fmt:message key="mvnad.admin.error.error_message"/><br />
  <b><i><span class="warning"><%=session.getAttribute("ErrorMessage")%></span></i></b>
  <br />
  <fmt:message key="mvnad.admin.error.you_may"/> <a class="command" href="javascript:history.back(1)"><fmt:message key="mvnad.admin.error.go_back"/></a> <fmt:message key="mvnad.admin.error.now_and_try_again"/><br />
  <fmt:message key="mvnad.common.ad.or"/>
  <script type="text/javascript">
    var emailtitle = "<%=MailUtil.getEmailUsername(MVNForumConfig.getWebMasterEmail())%>";
    var emaildomain= "<%=MailUtil.getEmailDomain(MVNForumConfig.getWebMasterEmail())%>";
    writeEmail(emailtitle, emaildomain, "<fmt:message key="mvnad.admin.error.report_bug"/>", "", "<fmt:message key="mvnad.admin.error.report_to_admin"/>", "command");
  </script>
</div>

<br />
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>