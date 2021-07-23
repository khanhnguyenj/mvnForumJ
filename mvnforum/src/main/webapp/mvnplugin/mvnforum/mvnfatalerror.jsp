<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/mvnfatalerror.jsp,v 1.12 2009/02/20 07:52:38 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.12 $
 - $Date: 2009/02/20 07:52:38 $
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
 --%>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="net.myvietnam.mvncore.util.MailUtil" %>
<%@ page import="com.mvnforum.MVNForumConfig" %>

<%@ taglib uri="mvntaglib" prefix="mvn" %>

<mvn:html locale="en">
<mvn:head>
  <mvn:title>mvnForum Fatal Error !!!</mvn:title>
<script type="text/javascript" src="<%=request.getContextPath()%>/mvnplugin/mvnforum/js/myvietnam.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/mvnplugin/mvnforum/css/style.css" />
</mvn:head>
<mvn:body>

<h2 align="center" style="color: blue">mvnForum Fatal Error Message : <%=request.getAttribute("fatal_error_message")%></h2>
<h3 align="center" style="color: black">If you believe this is a bug, click to:
    <script type="text/javascript">
    //<![CDATA[
      var emailtitle = "<%=MailUtil.getEmailUsername(MVNForumConfig.getWebMasterEmail())%>";
      var emaildomain= "<%=MailUtil.getEmailDomain(MVNForumConfig.getWebMasterEmail())%>";
      writeEmail(emailtitle, emaildomain, "Report fatal error when runing mvnForum", "", "report this error to Website administrator", "command");
    //]]>
    </script>
 </h3>

</mvn:body>
</mvn:html>
