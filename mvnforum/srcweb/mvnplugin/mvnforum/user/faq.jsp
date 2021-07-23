<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/faq.jsp,v 1.39 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.39 $
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

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
    <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - Frequently Ask Questions (FAQ)</mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="index"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
   Frequently Ask Questions (FAQ)
</div>

<div class="pagedesc">
  Frequently Asked Questions is a document compiled from common questions and quality answers provided on the
  <a href="http://www.mvnForum.com">mvnForum web site</a> on both technical and common user issues.
  If you cannot find an answer to your questions, consider reviewing the online documentation <a class="command" href="docs">here</a>
  or by posting a question to the <a href="http://www.mvnForum.com">mvnForum</a>.
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">General</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      Frequently asked questions about using the forums
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What is mvnForum?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       mvnForum is an open source, powerful, easy to use, easy to setup bulletin board (forum) developed following
       Java J2EE standards and technologies (Jsp/Servlet).
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What license is mvnForum released under?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       mvnForum is a free, open-source project released under the terms of the GNU General Public License.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Where can I see the update-to-date version of this FAQ?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The latest version of this FAQ are <a href="http://www.mvnforum.com/mvnforum/faq">here</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Where can I see a list of forum features?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The latest information about forum features are <a href="http://www.mvnforum.com/mvnforumweb/feature.jsp">here</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Who has developed mvnForum?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The core mvnForum framework is being developed by a group of dedicated Vietnamese developers at <a href="http://www.myvietnam.net">MyVietnam.net</a>
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">Installation</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      Installation FAQ
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Where do I get the latest binary installation of the mvnForum?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The latest mvnForum release can be found <a href="http://www.mvnforum.com/mvnforumweb/download.jsp">here<a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Where do I get the latest source code for development?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       See these notes for accessing the source via CVS: <a href="http://www.mvnforum.com/mvnforumweb/docs/CVS.txt">here<a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What databases are currently supported?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The following databases are supported and have been tested: MySQL 3.23.51, postgreSQL 7.3, Oracle 8i 8.1.7, Microsoft SQL Server 2000, and hsqldb 1.7.1
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What databases are planned to be supported?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The goal is to support ALL major database vendors that supply JDBC drivers that support JDBC 2.0 standards
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What Application Servers are supported?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       mvnForum can be installed on any Servlet Container which supports Jsp 1.2 and Servlet 2.3. mvnForum has been tested on the following Application Servers: Apache Tomcat  4.1.7, 4.1.10, 4.1.12, 4.1.18, and Resin 2.1.4
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">Administration</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      Administration FAQ
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       How do I add a new Forum to a Category?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       See the Online Documentation section here
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Should I associate the Admin section of the forum with an easy to identify URL?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       It is up to you, but by default, we suggest not doing this so as to minimize users attempts to access the administration site.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Can I restrict certain users to specific Forums?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       That feature is not currently supported.  See the <a href="http://www.mvnforum.com/mvnforumweb/docs/TODO.txt">TODO</a> list for the intended inclusion of this capability.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Can a forum be moderated?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       Currently, mvnForum does not support moderated forums. The goal is to include this in RC1.  See the <a href="http://www.mvnforum.com/mvnforumweb/docs/TODO.txt">TODO</a>.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What do I have to do to localize mvnForum?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       Support a new language in mvnForum is very easy, just translate the file mvnForum_i18n.properties, then change the file name
       to mvnForum_i18n_<yourlocale>.properties. Update the build.xml to support your new locale and/or change the file build.properties
       to make your new locale as the default displayed language.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       How do I change the default displayed language in mvnForum?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
      If you are building mvnForum, just change the file build.properties to the appropriate values.<p>
      If you want to change the default displayed language in the binary distribution package, just copy all the files from
      the subfolder of webapp\mvnplugin\mvnforum\user that contains your preferred language to the webapp\mvnplugin\mvnforum\user.
      For example, if you want to change the default language to Vietnamese, just copy all files from folder
      <b>webapp\mvnplugin\mvnforum\user\vi</b> to folder <b>webapp\mvnplugin\mvnforum\user</b>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Does mvnForum support Internationalization (i18n)?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       Yes. mvnForum uses unicode in the backend, so it supports (in theory) ALL languages
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">User</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      User FAQ
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What user authentication protocols are supported?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       mvnForum supports a realm authentication model, customized authentication model and the typical user name/password authentication.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Can I delete a post I made?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       You cannot delete your post unless you are a moderator. However, you can edit your post in a limited amount of time (1 week).
       If you do want to delete your post, please contact your forum moderators/administrators and tell them to delete it.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Can I follow a thread?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       At RC1, you will be able to follow a thread a get digest summaries of Categories, Forums, and Threads.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What sort of files can I attach to a forum post?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       At RC1, you will be able to attach the following files:
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       What HTML tags are supported?  How can I add new tags to the list of supported tags?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       See ...
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">Development</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      Development-related FAQ
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       How do I change the colors and fonts?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The majority of the styles in mvnForum are defined by a single point of entry for cascading style sheets.
       Copy the existing style.css file and make the changes you want to.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/q.gif" alt="" />
    </td>
    <td width="100%">
       Can I incorporate the mvnForum site in my site?
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/a.gif" alt="" />
    </td>
    <td width="100%">
       The easiest way to merge the mvnForum into your existing site is to modify the header.jsp to match your site template.
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2">Request a new F.A.Q.</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      If you have thought of a question that is not answered above, email <a href="mailto:cord_sw@lupinex.com">FAQ Keeper</a>
      or post a request at <a href="http://www.mvnForum.com/mvnforum/listthreads?forum=18">mvnForum Development</a>
    </td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="/mvnplugin/mvnforum/user/footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
