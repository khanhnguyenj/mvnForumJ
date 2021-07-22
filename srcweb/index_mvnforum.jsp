<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/index_mvnforum.jsp,v 1.21 2010/08/20 04:54:21 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.21 $
 - $Date: 2010/08/20 04:54:21 $
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
  <title>MVN Collaboration Suite Demo</title>
</head>

<body>

For the original Tomcat home, click here <a href="/index.jsp">Original Tomcat home</a>

<hr>
<a href="http://www.mvnforum.com"><img src="/mvnforum/mvnplugin/mvnforum/images/logo.gif" alt="" width="141" height="50" border="0"></a>
<% if (false /* mvnAd */) {%>
<a href="http://www.mvnforum.com"><img src="/mvnforum/mvnplugin/mvnad/images/logo.gif" alt="" width="140" height="50" border="0"></a>
<% } %>
<p>
For mvnForum Index page, click <a href="/mvnforum/">mvnForum Index page</a>
<p>
For mvnForum Admin page, click <a href="/mvnforum/mvnforumadmin">mvnForum Admin page</a>
<p>
<% if (false /* mvnAd */) {%>
For mvnAd Index page, click <a href="/mvnforum/ad">mvnAd Index page</a>
<p>
For mvnAd Admin page, click <a href="/mvnforum/adadmin">mvnAd Admin page</a>
<p>
<% } %>
Please use username <font color="#FF0000">admin</font> and password <font color="#FF0000">admin</font>

<% if (false /* mvnForum enterprise */) {%>
<hr>
<a href="http://www.mvnforum.com"><img src="/mvnforum_enterprise/mvnplugin/mvnforum/images/logo.gif" alt="" width="141" height="50" border="0"></a>
<% if (true /* mvnAd */) {%>
<a href="http://www.mvnforum.com"><img src="/mvnforum_enterprise/mvnplugin/mvnad/images/logo.gif" alt="" width="140" height="50" border="0"></a>
<% } %>
<p>
For mvnForum Enterprise Index page, click <a href="/mvnforum_enterprise/">mvnForum Index page</a>
<p>
For mvnForum Enterprise Admin page, click <a href="/mvnforum_enterprise/mvnforumadmin">mvnForum Admin page</a>
<p>
<% if (true /* mvnAd enterprise */) {%>
For mvnAd Enterprise Index page, click <a href="/mvnforum_enterprise/ad">mvnAd Index page</a>
<p>
For mvnAd Enterprise Admin page, click <a href="/mvnforum_enterprise/adadmin">mvnAd Admin page</a>
<p>
<% } %>
Please use username <font color="#FF0000">admin</font> and password <font color="#FF0000">admin</font>
<% } %>

<% if (false /* mvnForum portlet */) {%>
<hr>
<a href="http://www.mvnforum.com"><img src="/mvncms_portlet/mvnplugin/mvnforum/images/logo.gif" alt="" width="141" height="50" border="0"></a>
<a href="http://www.mvnforum.com"><img src="/mvncms_portlet/mvnplugin/mvnad/images/logo.gif" alt="" width="140" height="50" border="0"></a>
<a href="http://www.mvnforum.com"><img src="/mvncms_portlet/mvnplugin/mvncms/images/logo_portlet.gif" alt="" width="140" height="50" border="0"></a>
<p>
For Apache Pluto Portal, click <a href="/pluto/portal">Pluto Portal</a>
<p>
Please use username <font color="#FF0000">admin</font> and password <font color="#FF0000">admin</font>
<% } %>

<% if (false /* mvnPublish */) {%>
<hr>
<b>MVN Collaboration Suite</b> includes <b>mvnPublish</b>, <b>mvnForum Enterprise</b> and <b>mvnAd Enterprise</b>
<p>
<a href="http://www.mvnforum.com"><img src="/mvncms/mvnplugin/mvncms/images/logo.gif" alt="" width="140" height="50" border="0"></a>
<a href="http://www.mvnforum.com"><img src="/mvncms/mvnplugin/mvnforum/images/logo.gif" alt="" width="141" height="50" border="0"></a>
<a href="http://www.mvnforum.com"><img src="/mvncms/mvnplugin/mvnad/images/logo.gif" alt="" width="140" height="50" border="0"></a>
<p>
For mvnForum Enterprise Index page, click <a href="/mvncms/mvnforum">mvnForum Index page</a>
<p>
For mvnForum Enterprise Admin page, click <a href="/mvncms/mvnforumadmin">mvnForum Admin page</a>
<p>
For mvnAd Enterprise Index page, click <a href="/mvncms/ad">mvnAd Index page</a>
<p>
For mvnAd Enterprise Admin page, click <a href="/mvncms/adadmin">mvnAd Admin page</a>
<p>
<p>
For mvnPublish Index page, click <a href="/mvncms/template1">mvnPublish Index page</a>
<p>
For mvnPublish Config Layout page, click <a href="/mvncms/template1/confighome">mvnPublish Config Layout page</a>
<p>
For mvnPublish Admin page, click <a href="/mvncms/cmsadmin">mvnPublish Admin page</a>
<p>
Please use username <font color="#FF0000">admin</font> and password <font color="#FF0000">admin</font>
<% } %>

</body>
</html>
