<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/meta.jsp,v 1.24 2010/06/22 02:32:45 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.24 $
 - $Date: 2010/06/22 02:32:45 $
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
<%if (isServlet) {%>
<meta name="copyright" content="This software is copyrighted by MyVietnam.net and released under the terms of the GNU General Public License GPL license. All rights reserved." />
<meta name="Keywords" content="mvnForum, server side java, online meetings, collaboration, bulletin board, board, forum, discussion board, jsp, servlet, java, j2se, j2ee, database, jdbc, connection pool, mysql, MyVietnam, mvnForum, framework, web app, web application, servlet container, tomcat, jboss, weblogic, websphere" />
<meta name="Description" content="mvnForum is a powerful Jsp/Servlet forum (discussion board) - based on Java technology." />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link href="<%=contextPath%>/mvnplugin/mvnforum/images/favicon.ico" rel="SHORTCUT ICON"/>
<%--
// I have to comment these below line because when back a page,
// the page is reload, so user have to type data again
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
--%>
<meta http-equiv="Expires" content="-1" />
<%}%>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prototype/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/jquery/jquery.js"></script>
