<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/configindex.jsp,v 1.140 2010/06/04 03:40:19 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.140 $
 - $Date: 2010/06/04 03:40:19 $
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
 - @author: Igor Manic   
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.db.DBUtils" %>
<%@ page import="net.myvietnam.mvncore.util.StringUtil" %>
<%@ page import="net.myvietnam.mvncore.configuration.DOM4JConfiguration" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.MemberBean" %>
<%@ page import="com.mvnforum.db.WatchBean" %>
<%@ page import="com.mvnforum.db.ForumBean" %>
<%@ page import="com.mvnforum.search.post.PostSearchQuery" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:setBundle basename="i18n/mvnforum/guide_mvnForum_i18n" scope="request" var="guide" />
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.configindex.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
</mvn:head>
<mvn:body>

<%@ include file="header.jsp"%>
<br/>
<%
DOM4JConfiguration mvnforumConfig = (DOM4JConfiguration)request.getAttribute("mvnforumConfig");
DOM4JConfiguration mvncoreConfig  = (DOM4JConfiguration)request.getAttribute("mvncoreConfig");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>"><fmt:message key="mvnforum.admin.misctasks.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.configindex.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.configindex.info"/><br/>
  <span class="warning"><fmt:message key="mvnforum.admin.configindex.warning"/></span><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>

  <%if (permission.canAdminSystem()) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "configbackupprocess?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.configindex.command.backup_config_files"/></a><br/>
  <%}%>

  <%if (permission.canAdminSystem()) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "commitconfigs?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>" class="command"><fmt:message key="mvnforum.admin.configindex.command.reload_all_changes"/></a><br/>
  <%}%>

  <%if (permission.canAdminSystem() && (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE)) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "configenterprise", URLResolverService.RENDER_URL)%>" class="command"><fmt:message key="mvnforum.admin.configenterprisex.title"/></a><br/>
  <%}%>
</div>
<br/>

<%-- Below is mvncore.xml --%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_database"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>database_type <span class="requiredfield">*</span></td>
    <% 
    String str_database_type = mvncoreConfig.getString("dboptions.database_type", ""); 
    String databaseName = "Invalid value, please correct it.";
    try {
        int database_type = Integer.parseInt(str_database_type);
        databaseName = DBUtils.getDatabaseTypeName(database_type);
    } catch (Exception e) {}
    %>
    <td><%=mvncoreConfig.getString("dboptions.database_type", "")%> (<%=databaseName%>)</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_datasource" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>use_datasource <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.use_datasource", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.driver_class_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>driver_class_name <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.driver_class_name", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>database_url <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.database_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_user" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>database_user <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.database_user", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>database_password</td>
    <td><%=StringUtil.getHiddenPassword(mvncoreConfig.getString("dboptions.database_password", ""))%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.max_connection" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_connection <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.max_connection", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.max_time_to_wait" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_time_to_wait <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.max_time_to_wait", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.minutes_between_refresh" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>minutes_between_refresh <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.minutes_between_refresh", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.datasource_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>datasource_name <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dboptions.datasource_name", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.default_mail_from" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_mail_from <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.default_mail_from", "")%></td>
  </tr>  
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail.recieve_mail_option"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_mail_source" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_mail_source <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.enable_mail_source", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_source_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mail_source_name</td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.mail_source_name", "")%></td>
  </tr>    
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_secure_connection" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>use_secure_connection <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.use_secure_connection", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mail_server <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.mail_server", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.username" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>username</td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.username", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>password</td>
    <td><%=StringUtil.getHiddenPassword(mvncoreConfig.getString("mailoptions.receive_mail.password", ""))%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.port" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>port <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.receive_mail.port", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail.send_mail_option"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_embeded_smtp_mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>use_embeded_smtp_mail_server <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.use_embeded_smtp_mail_server", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_mail_source" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_mail_source <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.enable_mail_source", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_source_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mail_source_name</td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.mail_source_name", "")%></td>
  </tr>    
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_secure_connection" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>use_secure_connection <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.use_secure_connection", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mail_server <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.mail_server", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.username" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>username</td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.username", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>password</td>
    <td><%=StringUtil.getHiddenPassword(mvncoreConfig.getString("mailoptions.send_mail.password", ""))%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.port" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>port <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mailoptions.send_mail.port", "")%></td>
  </tr>    
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_param"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.context_path" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>context_path</td>
    <td><%=mvncoreConfig.getString("paramoptions.context_path", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.server_path" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>server_path <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("paramoptions.server_path", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_date"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.server_hour_offset" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>server_hour_offset <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("dateoptions.server_hour_offset", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_user_agent"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.blocked_user_agent" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>blocked_user_agent</td>
    <td><%=mvncoreConfig.getString("useragentoptions.blocked_user_agent", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_core"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.timermanager_datasource" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>timermanager_datasource</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.timermanager_datasource", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_link_nofollow" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_link_nofollow</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.enable_link_nofollow", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_encode_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_encode_url</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.enable_encode_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.portal_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>portal_type</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.portal_type", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.allow_http_referer_prefix_list" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>allow_http_referer_prefix_list</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.allow_http_referer_prefix_list", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mvncoreservice_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mvncoreservice_implementation <span class="requiredfield">*</span></td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.mvncoreservice_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.timertaskext_implementation_list" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>timertaskext_implementation_list</td>
    <td><%=mvncoreConfig.getString("mvncoreconfig.timertaskext_implementation_list", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_interceptor"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mailinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mailinterceptor_implementation</td>
    <td><%=mvncoreConfig.getString("interceptor.mailinterceptor_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.contentinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>contentinterceptor_implementation</td>
    <td><%=mvncoreConfig.getString("interceptor.contentinterceptor_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.loginidinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>loginidinterceptor_implementation</td>
    <td><%=mvncoreConfig.getString("interceptor.loginidinterceptor_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.passwordinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>passwordinterceptor_implementation</td>
    <td><%=mvncoreConfig.getString("interceptor.passwordinterceptor_implementation", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "configmvncore")%>" class="command"><fmt:message key="mvnforum.admin.configmvncore.title"/></a>
    </td>
  </tr>
</table>
<br/>

<%-- Below is mvnforum.xml --%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_url_pattern"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.user_url_pattern" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>
      <fmt:message key="mvnforum.admin.config.config_url_pattern.user_module"/> <span class="requiredfield">*</span>
    </td>
    <td><%=mvnforumConfig.getString("usermoduleconfig.url_pattern", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.admin_url_pattern" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>
      <fmt:message key="mvnforum.admin.config.config_url_pattern.admin_module"/> <span class="requiredfield">*</span>
    </td>
    <td><%=mvnforumConfig.getString("adminmoduleconfig.url_pattern", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "configurlpattern")%>" class="command"><fmt:message key="mvnforum.admin.configurlpattern.title"/></a>
    </td>
  </tr>
</table>
<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_core"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="32%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_home" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mvnforum_home <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.mvnforum_home", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="32%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_log" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mvnforum_log <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.mvnforum_log", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.webmaster_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>webmaster_email <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.webmaster_email", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.watch_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>watch_email <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.watch_email", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.logo_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>logo_url <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.logo_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.redirect_login_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>redirect_login_url <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.redirect_login_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.redirect_logout_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>redirect_logout_url <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.redirect_logout_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.supported_locales" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>supported_locales</td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.supported_locales", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_locale_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_locale_name <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.default_locale_name", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.locale_parameter_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>locale_parameter_name <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.locale_parameter_name", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_guest_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_guest_name <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.default_guest_name", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_guest_timezone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_guest_timezone <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.default_guest_timezone", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_cache_member <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_cache_member", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_cache_post <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_cache_post", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_cache_thread <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_cache_thread", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_forum" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_cache_forum <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_cache_forum", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_category" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_cache_category <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_cache_category", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_passwordless_auth" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_passwordless_auth <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_passwordless_auth", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_cookie" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_login_info_in_cookie <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_cookie", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_session" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_login_info_in_session <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_session", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_realm" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_login_info_in_realm <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_realm", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_customization" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_login_info_in_customization <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_customization", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_friendly_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_friendly_url <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_friendly_url", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_check_invalid_session" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_check_invalid_session <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_check_invalid_session", "")%></td>
  </tr> 
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_activation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_activation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_activation", "")%></td>
  </tr>   
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_login <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_login", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_new_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_new_member <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_new_member", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_new_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_new_post <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_new_post", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_rss <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_rss", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_watch" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_watch <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_watch", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_attachment <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_attachment", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_avatar" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_avatar <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_avatar", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_emoticon" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_emoticon <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_emoticon", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_captcha" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_captcha <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_captcha", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_portal_like_index_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_portal_like_index_page <span class="requiredfield">*</span></td>
   <td><%=mvnforumConfig.getString("mvnforumconfig.enable_portal_like_index_page", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_message_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_message_attachment <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_message_attachment", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_search" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_search <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_search", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_search_order_by" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_search_order_by <span class="requiredfield">*</span></td>
    <td>
    <%=mvnforumConfig.getString("mvnforumconfig.default_search_order_by", "")%>
    <%
    int default_search_order_by = mvnforumConfig.getInt("mvnforumconfig.default_search_order_by", PostSearchQuery.SEARCH_SORT_DEFAULT);
    String str_default_search_order_by = "Relevant";
    if (default_search_order_by == PostSearchQuery.SEARCH_SORT_TIME_DESC) {
        str_default_search_order_by = "Time Descending";
    } else if (default_search_order_by == PostSearchQuery.SEARCH_SORT_TIME_ASC) {
        str_default_search_order_by = "Time Ascending";
    }
    %>
    : <%=str_default_search_order_by%>
    </td>

    
  </tr>  
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_online_users" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_online_users <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_online_users", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_duplicate_onlineusers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_duplicate_onlineusers <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_duplicate_onlineusers", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_invisible_users" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_invisible_users <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_invisible_users", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_listmembers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_listmembers <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_listmembers", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_last_login_of_current_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_last_login_of_current_member <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_last_login_of_current_member", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_last_login" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_last_login <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_last_login", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_auto_watching" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_auto_watching <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_auto_watching", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_birthday" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_birthday <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_birthday", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_gender" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_gender <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_gender", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_address" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_address <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_address", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_city" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_city <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_city", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_state" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_state <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_state", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_homepage" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_homepage <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_homepage", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_country" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_country <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_country", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_phone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_phone <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_phone", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_mobile" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_mobile <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_mobile", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_fax" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_fax <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_fax", "")%></td>
  </tr> 
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_career" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_career <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_career", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_cool_link_1" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_cool_link_1 <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_cool_link_1", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_cool_link_2" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_cool_link_2 <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_cool_link_2", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_yahoo" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_yahoo <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_yahoo", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_aol" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_aol <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_aol", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_career" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_career <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_career", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_icq" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_icq <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_icq", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_msn" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_msn <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_msn", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_join_date" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_join_date <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_join_date", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_post_count" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_post_count <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_post_count", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_view_count" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_view_count <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_view_count", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_email <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_email", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_email_visible" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_email_visible <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_email_visible", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_name_visible" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_name_visible <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_name_visible", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_posts_per_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_posts_per_page <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_posts_per_page", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_modified_date" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_modified_date <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_modified_date", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_signature" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_signature <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_signature", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_online_status" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_online_status <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_online_status", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_firstname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_firstname <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_firstname", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_lastname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_show_lastname <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_show_lastname", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_use_popup_menu_in_viewthread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_use_popup_menu_in_viewthread <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_use_popup_menu_in_viewthread", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_rich_text_editor" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_rich_text_editor <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_rich_text_editor", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_split_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_split_thread <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_split_thread", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_listunansweredthreads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_listunansweredthreads <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_listunansweredthreads", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_users_browsing_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_list_users_browsing_thread <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_list_users_browsing_thread", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_email_to_admin_content_with_censored_words" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_email_to_admin_content_with_censored_words <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_email_to_admin_content_with_censored_words", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_guest_view_listusers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_guest_view_listusers <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_guest_view_listusers", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.only_normal_thread_type_in_active_threads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>only_normal_thread_type_in_active_threads <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.only_normal_thread_type_in_active_threads", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.send_watchmail_as_html" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>send_watchmail_as_html <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.send_watchmail_as_html", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_easy_watching" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_easy_watching <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_easy_watching", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_send_watch_mail_of_my_own_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_send_watch_mail_of_my_own_post <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_send_watch_mail_of_my_own_post", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_last_post_body_in_watch" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_last_post_body_in_watch <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_last_post_body_in_watch", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_message_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_message_attachment <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_message_attachment", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_most_active_threads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_most_active_threads <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_most_active_threads", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_most_active_members" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_most_active_members <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_most_active_members", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_site_statistics_overview" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_site_statistics_overview <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_site_statistics_overview", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_admin_can_change_password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_admin_can_change_password <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_admin_can_change_password", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_guest_view_image_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_guest_view_image_attachment <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_guest_view_image_attachment", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_expanse_category_tree_by_default" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_expanse_category_tree_by_default <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_expanse_category_tree_by_default", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_watch_option" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_watch_option <span class="requiredfield">*</span></td>
    <td>
    <%=mvnforumConfig.getString("mvnforumconfig.default_watch_option", "")%>
    <%
    int default_watch_option = mvnforumConfig.getInt("mvnforumconfig.default_watch_option", WatchBean.WATCH_OPTION_DEFAULT);
    String str_default_watch_option = "Default (daily watch)";
    if (default_watch_option == WatchBean.WATCH_OPTION_LIVE) {
        str_default_watch_option = "Live Watch";
    } else if (default_watch_option == WatchBean.WATCH_OPTION_HOURLY) {
        str_default_watch_option = "Hourly Watch";
    } else if (default_watch_option == WatchBean.WATCH_OPTION_DAILY) {
        str_default_watch_option = "Daily Watch";
    } else if (default_watch_option == WatchBean.WATCH_OPTION_WEEKLY) {
        str_default_watch_option = "Weekly Watch";
    }
    %>
    : <%=str_default_watch_option%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_moderation_option" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_moderation_option <span class="requiredfield">*</span></td>
    <td>
    <%=mvnforumConfig.getString("mvnforumconfig.default_moderation_option", "")%>
    <%
    int default_moderation_option = mvnforumConfig.getInt("mvnforumconfig.default_moderation_option", ForumBean.FORUM_MODERATION_MODE_NO_MODERATION);
    String str_default_moderation_option = "Default (No Moderation)";
    if (default_moderation_option == ForumBean.FORUM_MODERATION_MODE_THREAD_AND_POST) {
        str_default_moderation_option = "Thread and Post Moderation";
    } else if (default_moderation_option == ForumBean.FORUM_MODERATION_MODE_THREAD_ONLY) {
        str_default_moderation_option = "Thread only Moderation";
    } else if (default_moderation_option == ForumBean.FORUM_MODERATION_MODE_POST_ONLY) {
        str_default_moderation_option = "Post only Moderation";
    }
    %>
    : <%=str_default_moderation_option%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_encrypt_password_on_browser" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_encrypt_password_on_browser <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_encrypt_password_on_browser", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_external_user_database" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_external_user_database <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_external_user_database", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_new_members_in_recent_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_list_new_members_in_recent_days <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_list_new_members_in_recent_days", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.days_to_show_recent_members" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>days_to_show_recent_members <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.days_to_show_recent_members", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_users_browsing_forum" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_list_users_browsing_forum <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_list_users_browsing_forum", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_email_threatening_content" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_email_threatening_content <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_email_threatening_content", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.have_internet" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>have_internet <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.have_internet", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.event_log_locale" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>event_log_locale <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.event_log_locale", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_category_id" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_category_id <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.default_category_id", "")%></td>
  </tr>
   <tr class="<mvn:cssrow/>">
     <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_watch_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_watch_type <span class="requiredfield">*</span></td>
    <td>
      <%=mvnforumConfig.getString("mvnforumconfig.default_watch_type", "")%>
      <%
      int default_watch_type = mvnforumConfig.getInt("mvnforumconfig.default_watch_type", WatchBean.WATCH_TYPE_DIGEST);
      String str_default_watch_type = "Default (Digest)";
      if (default_watch_type == WatchBean.WATCH_TYPE_NONDIGEST) {
          str_default_watch_type = "Non Digest";
      }
      %>
      : <%=str_default_watch_type%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_status_of_registered_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>default_status_of_registered_member <span class="requiredfield">*</span></td>
    <td>
      <%=mvnforumConfig.getString("mvnforumconfig.default_status_of_registered_member", "")%>
      <%
      int default_status_of_registered_member = mvnforumConfig.getInt("mvnforumconfig.default_status_of_registered_member", MemberBean.MEMBER_STATUS_ENABLE);
      String str_default_status_of_registered_member = "Default (Enable Status)";
      if (default_status_of_registered_member == MemberBean.MEMBER_STATUS_PENDING) {
          str_default_status_of_registered_member = "Pending Status";
      }
      %>
      : <%=str_default_status_of_registered_member%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_register_rule" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_register_rule <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_register_rule", "")%></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_image_thumbnail"/></td>
  </tr>
<mvn:cssrows>    
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_image_thumbnail" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_image_thumbnail <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.image_thumbnail.enable", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.image_thumbnail_width" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>image_thumbnail_width <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.image_thumbnail.width", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.image_thumbnail_height" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>image_thumbnail_height <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.image_thumbnail.height", "")%></td>
  </tr>
</mvn:cssrows> 
<mvn:cssrows>    
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_factory"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.member_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>member_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.member_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.onlineuser_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>onlineuser_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.onlineuser_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.authenticator_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>authenticator_implementation</td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.authenticator_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.requestprocessor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>requestprocessor_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.requestprocessor_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.lucene_analyzer_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>lucene_analyzer_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.lucene_analyzer_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvn_auth_service_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mvn_auth_service_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.mvn_auth_service_implementation", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_service_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>mvnforum_service_implementation <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumfactoryconfig.mvnforum_service_implementation", "")%></td>
  </tr>
</mvn:cssrows>  
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "configstepone")%>" class="command"><fmt:message key="mvnforum.admin.configstepone.title"/></a>
    </td>
  </tr>
</table>
<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step2"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_private_message" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_private_message <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_private_message", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_message_attachment_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_message_attachment_size <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_message_attachment_size", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_attachment_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_attachment_size <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_attachment_size", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_favorite_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_favorite_thread <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_favorite_thread", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_edit_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_edit_days <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_edit_days", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_attach_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_attach_days <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_attach_days", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_delete_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_delete_days <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_delete_days", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.rows_per_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>rows_per_page <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.rows_per_page", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.rows_per_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>rows_per_rss <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.rows_per_rss", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.hot_topic_threshold" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>hot_topic_threshold <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.hot_topic_threshold", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_posts_per_hour_per_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_posts_per_hour_per_member <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_posts_per_hour_per_member", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_posts_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_posts_per_hour_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_posts_per_hour_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_http_requests_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_http_requests_per_hour_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_http_requests_per_hour_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_http_requests_per_minute_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_http_requests_per_minute_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_http_requests_per_minute_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_members_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_members_per_hour_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_members_per_hour_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_logins_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_logins_per_hour_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_logins_per_hour_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_messages_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_messages_per_hour_per_ip <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_messages_per_hour_per_ip", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_password_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_password_days <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_password_days", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_short_summary" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_chars_in_short_summary <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_short_summary", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_long_summary" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_chars_in_long_summary <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_long_summary", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_chars_in_rss <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_rss", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_backup_on_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>enable_backup_on_server <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.enable_backup_on_server", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_import_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>max_import_size <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.max_import_size", "")%></td>
  </tr>
</mvn:cssrows>  
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "configsteptwo")%>" class="command"><fmt:message key="mvnforum.admin.configsteptwo.title"/></a>
    </td>
  </tr>
</table>
<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step3"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_firstname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_firstname <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_firstname", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_lastname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_lastname <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_lastname", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_gender" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_gender <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_gender", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_birthday" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_birthday <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_birthday", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_address" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_address <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_address", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_city" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_city <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_city", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_state" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_state <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_state", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_country" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_country <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_country", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_phone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_phone <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_phone", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_mobile" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_mobile <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_mobile", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_fax" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_fax <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_fax", "")%></td>
  </tr>  
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_career" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_career <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_career", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_homepage" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_homepage <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_homepage", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_yahoo" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_yahoo <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_yahoo", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_aol" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_aol <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_aol", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_icq" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_icq <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_icq", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_msn" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_msn <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_msn", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_link_1" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_link_1 <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_link_1", "")%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_link_2" bundle="${guide}" />')" onmouseout="return nd();" alt=""/>require_register_link_2 <span class="requiredfield">*</span></td>
    <td><%=mvnforumConfig.getString("mvnforumconfig.require_register_link_2", "")%></td>
  </tr>
</mvn:cssrows>  
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "configstepthree")%>" class="command"><fmt:message key="mvnforum.admin.configstepthree.title"/></a>
    </td>
  </tr>
</table>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
