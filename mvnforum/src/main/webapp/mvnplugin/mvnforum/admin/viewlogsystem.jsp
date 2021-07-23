<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/viewlogsystem.jsp,v 1.69 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.69 $
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.viewlogsystem.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>
<%
String logs[]      = {"ALL", "TRACE",      "DEBUG",      "INFO",      "WARN",      "ERROR",      "FATAL"};
String logValues[] = {"All", "Only Trace", "Only Debug", "Only Info", "Only Warn", "Only Error", "Only Fatal"};
int lineNums[]     = { 25, 50, 100, 200, 400, 800 };

int lineCount = ParamUtil.getParameterInt(request, "linecount", 25);
String log = ParamUtil.getParameterFilter(request, "log");
if (log.length() == 0) {
  log = "ALL";
}
String logDir = ParamUtil.getAttribute(request, "LogDir");
String logFileName = ParamUtil.getAttribute(request, "LogFileName");
String logFileSize = ParamUtil.getAttribute(request, "LogFileSize");
String logFileHumanSize = ParamUtil.getAttribute(request, "LogFileHumanSize");
String fileName = ParamUtil.getAttribute(request, "FileName");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>"><fmt:message key="mvnforum.admin.misctasks.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.viewlogsystem.title"/>
</div>

<br/>
<div class="pagedesc">
  <fmt:message key="mvnforum.admin.viewlogsystem.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "listlogfiles")%>" class="command"><fmt:message key="mvnforum.admin.listlogfiles.title"/></a> <fmt:message key="mvnforum.admin.viewlogsystem.info_of_listlogfiles"/><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "backupsystemlog?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.viewlogsystem.backup_system_log"/></a> <fmt:message key="mvnforum.admin.viewlogsystem.info_of_backupsystemlog"/><br/>
  <a href="<%=urlResolver.encodeURL(request, response, "backupsystemlog?empty=yes" + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.viewlogsystem.backup_system_log_and_empty"/></a> <fmt:message key="mvnforum.admin.viewlogsystem.info_of_backupsystemlog"/>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "viewlogsystem", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "viewlogsystem")%>
<input type="hidden" name="filename" value="<%=fileName%>" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.viewlogsystem.log_file_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.file.file_directory"/>:</td>
    <td class="portlet-font"><%=logDir%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.file.file_name"/>:</td>
    <td class="portlet-font"><%=logFileName%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.file.file_size"/>:</td>
    <td class="portlet-font"><%=logFileHumanSize%> (<%=logFileSize%> bytes)</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.viewlogsystem.how_many_lines_to_view"/></td>
    <td>
      <select name="linecount" onchange="document.submitform.submit();">
       <% for (int i = 0; i < lineNums.length; i ++) {%>
        <option value="<%=lineNums[i]%>" <%if (lineNums[i] == lineCount) {%> selected="selected" <%}%> ><%=lineNums[i]%></option>
       <%}%>
      </select> <fmt:message key="mvnforum.admin.viewlogsystem.before_filter"/>
      <select name="log" onchange="document.submitform.submit();">
      <% for (int i = 0; i < logs.length; i ++) {%>
        <option <%if (log.equals(logs[i])) {%> selected="selected" <%}%> value="<%=logs[i]%>"><%=logValues[i]%></option>
      <%}%>
      </select>
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>
<table width="95%" class="noborder" cellspacing="0" cellpadding="0" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.viewlogsystem.line"/></td>
    <td width="100%" align="center"><fmt:message key="mvnforum.admin.viewlogsystem.content"/></td>
  </tr>
  <tr>
    <td colspan="2">
<%if (isPortlet) {
    String[] lines = (String[])request.getAttribute("ContentLog");
%>
      <%=log%>
      <table width="100%" class="noborder" cellspacing="0" cellpadding="5" align="center">
<%
    int index = 0;
%>
<mvn:cssrows>
<%
    for (int i = 0; i < lines.length; i ++) {
    if (lines[i] == null ) break;// all last elements is null
    if ( !log.equals("ALL") && (lines[i].indexOf(log) < 0) ) continue; %>
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
     <%}//for
        %>
</mvn:cssrows>        
   </table>
<%} else {
    String logframe = "logframe?filename" + fileName + "&amp;linecount=" + lineCount + "&amp;log=" + log;%>
      <iframe src="<%=urlResolver.encodeURL(request, response, logframe)%>" align="middle"
        frameborder="0" height="400" width="100%" marginheight="0" marginwidth="0" scrolling="auto"></iframe>
<%}%>
    </td>
  </tr>
</table>
<br/>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>