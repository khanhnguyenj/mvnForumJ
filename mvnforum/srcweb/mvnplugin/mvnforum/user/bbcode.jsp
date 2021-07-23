<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/bbcode.jsp,v 1.7 2009/03/13 01:43:04 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.7 $
 - $Date: 2009/03/13 01:43:04 $
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
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.common.bbcode.title"/></mvn:title>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>

<table width="350" align="center" border="1">
  <tr>
    <td align="center" width="50%"><b>BBCode</b></td>
    <td align="center" width="50%"><b><fmt:message key="mvnforum.common.bbcode.effect"/></b></td>
  </tr>
  <tr>
    <td align="center">[b]<fmt:message key="mvnforum.common.bbcode.bold_text"/>[/b]</td>
    <td align="center"><b><fmt:message key="mvnforum.common.bbcode.bold_text"/></b></td>
  </tr>
  <tr>
    <td align="center">[i]<fmt:message key="mvnforum.common.bbcode.italicized_text"/>[/i]</td>
    <td align="center"><i><fmt:message key="mvnforum.common.bbcode.italicized_text"/></i></td>
  </tr>
  <tr>
    <td align="center">[u]<fmt:message key="mvnforum.common.bbcode.underlined_text"/>[/u]</td>
    <td align="center"><u><fmt:message key="mvnforum.common.bbcode.underlined_text"/></u></td>
  </tr>
  <tr>
    <td align="center">[url=http://www.mvnForum.com]mvnForum[/url]</td>
    <td align="center"><a href="http://www.mvnForum.com" title="http://www.mvnForum.com" rel="nofollow">mvnForum</a></td>
  </tr>
  <tr>
    <td align="center">[img]http://www.mvnforum.com/mvnplugin/mvnforum/images/logo.gif[/img]</td>
    <td align="center"><img src="http://www.mvnforum.com/mvnplugin/mvnforum/images/logo.gif" alt="" /></td>
  </tr>
  <tr>
    <td align="center">[quote]<fmt:message key="mvnforum.common.bbcode.quoted_text"/>[/quote]</td>
    <td class="quote" align="center"><fmt:message key="mvnforum.common.bbcode.quoted_text"/></td>
  </tr>
  <tr>
    <td align="center">[size=3]<fmt:message key="mvnforum.common.bbcode.large_text"/>[/size]</td>
    <td align="center"><font size="3"><fmt:message key="mvnforum.common.bbcode.large_text"/></font></td>
  </tr>
  <tr>
    <td align="center">[color=red]<fmt:message key="mvnforum.common.bbcode.red_text"/>[/color]</td>
    <td align="center"><span style="color: red"><fmt:message key="mvnforum.common.bbcode.red_text"/></span></td>
  </tr>
  <tr>
    <td align="center">[:)]</td>
    <td align="center"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" title="smile" border="0" /></td>
  </tr>
</table>
  
</mvn:body>
</mvn:html>
</fmt:bundle>
