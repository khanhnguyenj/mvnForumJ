<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/help.jsp,v 1.52 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.52 $
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.help.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.help.title"/>
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2"><fmt:message key="mvnforum.user.action.desc.Help"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      <fmt:message key="mvnforum.user.action.desc.ViewHelp"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://www.mvnforum.com/mvnforumwiki/Wiki.jsp?page=MvnForumDocumentation"><fmt:message key="mvnforum.user.action.desc.HelpIntro"/></a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://mvnforum.cvs.sourceforge.net/*checkout*/mvnforum/mvnforum/docs/INSTALL.txt"><fmt:message key="mvnforum.user.action.desc.HelpInstall"/></a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://www.mvnforum.com/mvnforumwiki/Wiki.jsp?page=Mvnforum_user_guide"><fmt:message key="mvnforum.user.action.desc.HelpUser"/></a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://www.mvnforum.com/mvnforumwiki/Wiki.jsp?page=Mvnforum_admin_guide"><fmt:message key="mvnforum.user.action.desc.HelpAdmin"/></a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://www.mvnforum.com/mvnforumwiki/Wiki.jsp?page=Mvnforum_developer_guide"><fmt:message key="mvnforum.user.action.desc.HelpDeveloper"/></a> 
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center" colspan="2"><fmt:message key="mvnforum.user.action.desc.FAQ"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      <fmt:message key="mvnforum.user.action.desc.ViewDocument"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="40px">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt=""/>
    </td>
    <td width="100%">
      <a class="nav" href="http://www.mvnforum.com/mvnforumwiki/Wiki.jsp?page=Mvnforum_knowledgebase"><fmt:message key="mvnforum.user.action.desc.HelpKnowledge"/></a>
    </td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
