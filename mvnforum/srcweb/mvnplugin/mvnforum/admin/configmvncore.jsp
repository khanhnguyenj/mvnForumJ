<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/configmvncore.jsp,v 1.79 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.79 $
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
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="net.myvietnam.mvncore.configuration.DOM4JConfiguration" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:setBundle basename="i18n/mvnforum/guide_mvnForum_i18n" scope="request" var="guide" />
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.configmvncore.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {
  var agt = navigator.userAgent.toLowerCase();

  // Maybe, Opera make an onclick event when user press enter key
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitForm();
}
function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
       pw2md5(document.submitform.MemberCurrentMatkhau, document.submitform.md5pw);
    }    
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (document.submitform.use_datasource.checked == false) {
    if (isBlank(document.submitform.database_type, "database_type")) return false;
    if (isBlank(document.submitform.driver_class_name, "driver_class_name")) return false;
    if (isBlank(document.submitform.database_url, "database_url")) return false;
    if (isBlank(document.submitform.database_user, "database_user")) return false;
    if (!isUnsignedInteger(document.submitform.max_connection, "max_connection")) return false;
    if (!isUnsignedInteger(document.submitform.max_time_to_wait, "max_time_to_wait")) return false;
    if (!isUnsignedInteger(document.submitform.minutes_between_refresh, "minutes_between_refresh")) return false;
  } else {
    if (isBlank(document.submitform.datasource_name, "datasource_name")) return false;
  }
  
  if (isBlank(document.submitform.default_mail_from, "default_mail_from")) return false;
  if (!isEmail(document.submitform.default_mail_from, "default_mail_from")) return false;
  
  if (document.submitform.receive_mail_enable_mail_source.checked == false) {
    if (isBlank(document.submitform.receive_mail_server, "recieve mail_server")) return false;
    if (!isUnsignedInteger(document.submitform.send_mail_port, "receive mail port")) return false;
  } else {
    if (isBlank(document.submitform.receive_mail_source_name, "receive mail source name")) return false;
  }
  
  if (document.submitform.send_mail_use_embeded_smtp_mail_server.checked == false) {
    if (document.submitform.send_mail_enable_mail_source.checked == false) {
      if (isBlank(document.submitform.send_mail_server, "send mail_server")) return false;
      if (!isUnsignedInteger(document.submitform.receive_mail_port, "send mail port")) return false;
    } else {
      if (isBlank(document.submitform.send_mail_source_name, "send mail source name")) return false;
    }
  }
  
  if (isBlank(document.submitform.server_path, "server_path")) return false;
  if (!isValidServerHourOffset(document.submitform.server_hour_offset, "server_hour_offset")) return false;
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) {
      return false;
  }
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}
function OnChangeUseDatasource() {
  if (document.submitform.use_datasource.checked == false) {
    document.getElementById("id_driver_class_name").style.display = '';
    document.getElementById("id_database_url").style.display = '';
    document.getElementById("id_database_user").style.display = '';
    document.getElementById("id_database_password").style.display = '';
    document.getElementById("id_max_connection").style.display = '';
    document.getElementById("id_max_time_to_wait").style.display = '';
    document.getElementById("id_minutes_between_refresh").style.display = '';
    document.getElementById("id_datasource_name").style.display = 'none';
  } else {
    document.getElementById("id_driver_class_name").style.display = 'none';
    document.getElementById("id_database_url").style.display = 'none';
    document.getElementById("id_database_user").style.display = 'none';
    document.getElementById("id_database_password").style.display = 'none';
    document.getElementById("id_max_connection").style.display = 'none';
    document.getElementById("id_max_time_to_wait").style.display = 'none';
    document.getElementById("id_minutes_between_refresh").style.display = 'none';
    document.getElementById("id_datasource_name").style.display = '';
  }
}
function OnChangeUseMailsource() {
  if (document.submitform.receive_mail_enable_mail_source.checked == false) {
    document.getElementById("id_receive_mail_mail_server").style.display = '';
    document.getElementById("id_recevie_mail_username").style.display = '';
    document.getElementById("id_receive_mail_password").style.display = '';
    document.getElementById("id_receive_mail_port").style.display = '';
    document.getElementById("id_receive_mail_source_name").style.display = 'none';
  } else {
    document.getElementById("id_receive_mail_mail_server").style.display = 'none';
    document.getElementById("id_recevie_mail_username").style.display = 'none';
    document.getElementById("id_receive_mail_password").style.display = 'none';
    document.getElementById("id_receive_mail_port").style.display = 'none';
    document.getElementById("id_receive_mail_source_name").style.display = '';
  }
  
  if (document.submitform.send_mail_use_embeded_smtp_mail_server.checked == false) {
    document.getElementById("id_send_mail_enable_mail_source").style.display = '';
    if (document.submitform.send_mail_enable_mail_source.checked == false) {
      document.getElementById("id_send_mail_use_secure_connection").style.display = '';
      document.getElementById("id_send_mail_mail_server").style.display = '';
      document.getElementById("id_send_mail_username").style.display = '';
      document.getElementById("id_send_mail_password").style.display = '';
      document.getElementById("id_send_mail_port").style.display = '';
      document.getElementById("id_send_mail_source_name").style.display = 'none';
    } else {
      document.getElementById("id_send_mail_use_secure_connection").style.display = 'none';
      document.getElementById("id_send_mail_mail_server").style.display = 'none';
      document.getElementById("id_send_mail_username").style.display = 'none';
      document.getElementById("id_send_mail_password").style.display = 'none';
      document.getElementById("id_send_mail_port").style.display = 'none';
      document.getElementById("id_send_mail_source_name").style.display = '';
    }
  } else {
    document.getElementById("id_send_mail_enable_mail_source").style.display = 'none';
    document.getElementById("id_send_mail_use_secure_connection").style.display = 'none';
    document.getElementById("id_send_mail_mail_server").style.display = 'none';
    document.getElementById("id_send_mail_username").style.display = 'none';
    document.getElementById("id_send_mail_password").style.display = 'none';
    document.getElementById("id_send_mail_port").style.display = 'none';
    document.getElementById("id_send_mail_source_name").style.display = 'none';
  }  
}
function isValidServerHourOffset(obj, message) {
    if (isBlank(obj, message)) {
      return false;
    }
    var  val = obj.value;
    if (isNaN(val) || val < -13 || val > 13) {
        alert(message + " <fmt:message key="mvnforum.common.js.prompt.server_hour_offset_mustbe_valid_number"/>");
        obj.focus();
        return false;
    }
    for (var i=0;i<val.length;i++) {
        if (val.charAt(i) == '.') {
            alert(message + " <fmt:message key="mvnforum.common.js.prompt.mustbe_unsigned_number"/>");
            obj.focus();
            return false;
        }
    }
    obj.value = parseInt(val,10);
    return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br />
<%
DOM4JConfiguration mvncoreConfig = (DOM4JConfiguration)request.getAttribute("mvncoreConfig");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "configindex")%>"><fmt:message key="mvnforum.admin.configindex.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.configmvncore.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "configmvncoreprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "configmvncoreprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_database"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_database_type">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="database_type">database_type</label> <span class="requiredfield">*</span></td>
    <td>
    <select id="database_type" name="database_type">
      <% String database_type = mvncoreConfig.getString("dboptions.database_type", "");%>
      <option value="0" <%= (database_type.equals("0")) ? "selected=\"selected\"" : "" %>><fmt:message key="mvnforum.admin.config.default"/> (<fmt:message key="mvnforum.admin.config.auto_detect"/>)</option>
      <option value="2" <%= (database_type.equals("2")) ? "selected=\"selected\"" : "" %>><fmt:message key="mvnforum.admin.config.not_support_scrollable_resultset"/></option>
    </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_datasource" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="use_datasource">use_datasource</label> <span class="requiredfield">*</span></td>
    <td>
    <% String use_datasource = mvncoreConfig.getString("dboptions.use_datasource", "");%>
    <input type="checkbox" id="use_datasource" name="use_datasource" value="true" class="noborder" onclick="javascript:OnChangeUseDatasource();"
    <% if ("true".equals(use_datasource)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_datasource_name">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.datasource_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="datasource_name">datasource_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="datasource_name" name="datasource_name" value="<%=mvncoreConfig.getString("dboptions.datasource_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow autoIncrease="false"/>" id="id_driver_class_name">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.driver_class_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="driver_class_name">driver_class_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="driver_class_name" name="driver_class_name" value="<%=mvncoreConfig.getString("dboptions.driver_class_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_database_url">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="database_url">database_url</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="database_url" name="database_url" value="<%=mvncoreConfig.getString("dboptions.database_url", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_database_user">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_user" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="database_user">database_user</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="database_user" name="database_user" value="<%=mvncoreConfig.getString("dboptions.database_user", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_database_password">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.database_password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/></label><label for="database_password">database_password</td>
    <td><input type="password" size="70" id="database_password" name="database_password" value="<%=mvncoreConfig.getString("dboptions.database_password", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_max_connection">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.max_connection" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_connection">max_connection</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="max_connection" name="max_connection" value="<%=mvncoreConfig.getString("dboptions.max_connection", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_max_time_to_wait">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.max_time_to_wait" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_time_to_wait">max_time_to_wait</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="max_time_to_wait" name="max_time_to_wait" value="<%=mvncoreConfig.getString("dboptions.max_time_to_wait", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_minutes_between_refresh">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.minutes_between_refresh" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="minutes_between_refresh">minutes_between_refresh</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="minutes_between_refresh" name="minutes_between_refresh" value="<%=mvncoreConfig.getString("dboptions.minutes_between_refresh", "")%>" /></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.default_mail_from" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_mail_from">default_mail_from</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="default_mail_from" name="default_mail_from" value="<%=mvncoreConfig.getString("mailoptions.default_mail_from", "")%>" /></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail.recieve_mail_option"/></td>
  </tr>  
<mvn:cssrows>
  <tr class="<mvn:cssrow/>" id="id_receive_mail_enable_mail_source">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_mail_source" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_enable_mail_source">enable_mail_source</label> <span class="requiredfield">*</span></td>
    <td> 
    <% String receive_enable_mail_source = mvncoreConfig.getString("mailoptions.receive_mail.enable_mail_source", "");%>
    <input type="checkbox" id="receive_mail_enable_mail_source" name="receive_mail_enable_mail_source" value="true" class="noborder" onclick="javascript:OnChangeUseMailsource();"
    <% if ("true".equals(receive_enable_mail_source)) { %>
        checked="checked"
    <% } %>
    />
    </td>    
  </tr>
  <tr class="<mvn:cssrow/>" id="id_receive_mail_source_name">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_source_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_source_name">mail_source_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="receive_mail_source_name" name="receive_mail_source_name" value="<%=mvncoreConfig.getString("mailoptions.receive_mail.mail_source_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow autoIncrease="false"/>" id="id_receive_mail_mail_server">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_server">mail_server</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="receive_mail_server" name="receive_mail_server" value="<%=mvncoreConfig.getString("mailoptions.receive_mail.mail_server", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_recevie_mail_username">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.username" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_username">username</label></td>
    <td><input type="text" size="70" id="receive_mail_username" name="receive_mail_username" value="<%=mvncoreConfig.getString("mailoptions.receive_mail.username", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_receive_mail_password">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_password">password</label></td>
    <td><input type="password" size="70" id="receive_mail_password" name="receive_mail_password" value="<%=mvncoreConfig.getString("mailoptions.receive_mail.password", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_receive_mail_port">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.port" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="receive_mail_port">port</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="receive_mail_port" name="receive_mail_port" value="<%=mvncoreConfig.getString("mailoptions.receive_mail.port", "25")%>" /></td>
  </tr> 
</mvn:cssrows>
  <tr class="portlet-section-subheader">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_mail.send_mail_option"/></td>
  </tr> 
<mvn:cssrows>
  <tr class="<mvn:cssrow/>" id="id_send_mail_use_embeded_smtp_mail_server">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_embeded_smtp_mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_use_embeded_smtp_mail_server">use_embeded_smtp_mail_server</label> <span class="requiredfield">*</span></td>
    <td> 
    <% String sendmail_use_embeded_smtp_mail_server = mvncoreConfig.getString("mailoptions.send_mail.use_embeded_smtp_mail_server", "");%>
    <input type="checkbox" id="send_mail_use_embeded_smtp_mail_server" name="send_mail_use_embeded_smtp_mail_server" value="true" class="noborder" onclick="javascript:OnChangeUseMailsource();"
    <% if ("true".equals(sendmail_use_embeded_smtp_mail_server)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr> 
  <tr class="<mvn:cssrow/>" id="id_send_mail_enable_mail_source">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_mail_source" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_enable_mail_source">enable_mail_source</label> <span class="requiredfield">*</span></td>
    <td> 
    <% String sendmail_enable_mail_source = mvncoreConfig.getString("mailoptions.send_mail.enable_mail_source", "");%>
    <input type="checkbox" id="send_mail_enable_mail_source" name="send_mail_enable_mail_source" value="true" class="noborder" onclick="javascript:OnChangeUseMailsource();"
    <% if ("true".equals(sendmail_enable_mail_source)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_send_mail_source_name">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_source_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_source_name">mail_source_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="send_mail_source_name" name="send_mail_source_name" value="<%=mvncoreConfig.getString("mailoptions.send_mail.mail_source_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_send_mail_use_secure_connection">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.use_secure_connection" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_use_secure_connection">use_secure_connection</label> <span class="requiredfield">*</span></td>
    <td> 
    <% String sendmail_use_secure_connection = mvncoreConfig.getString("mailoptions.send_mail.use_secure_connection", "");%>
    <input type="checkbox" id="send_mail_use_secure_connection" name="send_mail_use_secure_connection" value="true" class="noborder"
    <% if ("true".equals(sendmail_use_secure_connection)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr> 
  <tr class="<mvn:cssrow autoIncrease="false"/>" id="id_send_mail_mail_server">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mail_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_server">mail_server</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="send_mail_server" name="send_mail_server" value="<%=mvncoreConfig.getString("mailoptions.send_mail.mail_server", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_send_mail_username">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.username" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_username">username</label></td>
    <td><input type="text" size="70" id="send_mail_username" name="send_mail_username" value="<%=mvncoreConfig.getString("mailoptions.send_mail.username", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_send_mail_password">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_password">password</label></td>
    <td><input type="password" size="70" id="send_mail_password" name="send_mail_password" value="<%=mvncoreConfig.getString("mailoptions.send_mail.password", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_send_mail_port">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.port" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_mail_port">port</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="send_mail_port" name="send_mail_port" value="<%=mvncoreConfig.getString("mailoptions.send_mail.port", "25")%>" /></td>
  </tr>   
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_param"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.context_path" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="context_path">context_path</label></td>
    <td>
    <input type="text" size="70" id="context_path" name="context_path" value="<%=mvncoreConfig.getString("paramoptions.context_path", "")%>" /><br/>
    <fmt:message key="mvnforum.admin.config.auto_detect"/>: <b><%= request.getContextPath() %></b>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.server_path" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="server_path">server_path</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="70" id="server_path" name="server_path" value="<%=mvncoreConfig.getString("paramoptions.server_path", "")%>" /><br/>
    <fmt:message key="mvnforum.admin.config.auto_detect"/> (<fmt:message key="mvnforum.admin.config.method"/> 1): <b><%= ParamUtil.getServer(request) %></b><br/>
    <fmt:message key="mvnforum.admin.config.auto_detect"/> (<fmt:message key="mvnforum.admin.config.method"/> 2): <b><%= ParamUtil.getServer2(request) %></b>
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_date"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.server_hour_offset" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="server_hour_offset">server_hour_offset</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="70" id="server_hour_offset" name="server_hour_offset" value="<%=mvncoreConfig.getString("dateoptions.server_hour_offset", "0")%>" />
    <br/><fmt:message key="mvnforum.admin.config.auto_detect"/>: <b><%= java.util.TimeZone.getDefault().getRawOffset() / DateUtil.HOUR %></b>
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_user_agent"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.blocked_user_agent" bundle="${guide}" />')" onmouseout="return nd();" alt=""/> <label for="blocked_user_agent">blocked_user_agent</label></td>
    <td>
    <input type="text" size="70" id="blocked_user_agent" name="blocked_user_agent" value="<%=mvncoreConfig.getString("useragentoptions.blocked_user_agent", "")%>" />
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_core"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.timermanager_datasource" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="timermanager_datasource">timermanager_datasource</label></td>
    <td>
    <input type="text" size="70" id="timermanager_datasource" name="timermanager_datasource" value="<%=mvncoreConfig.getString("mvncoreconfig.timermanager_datasource", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_link_nofollow" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_link_nofollow">enable_link_nofollow</label></td>
    <td>
    <% String enable_link_nofollow = mvncoreConfig.getString("mvncoreconfig.enable_link_nofollow", "");%>
    <input type="checkbox" id="enable_link_nofollow" name="enable_link_nofollow" value="true" class="noborder"
    <% if ("true".equals(enable_link_nofollow)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.enable_encode_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_encode_url">enable_encode_url</label></td>
    <td>
    <% String enable_encode_url = mvncoreConfig.getString("mvncoreconfig.enable_encode_url", "");%>
    <input type="checkbox" id="enable_encode_url" name="enable_encode_url" value="true" class="noborder"
    <% if ("true".equals(enable_encode_url)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.portal_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="portal_type">portal_type</label></td>
    <td>
    <select id="portal_type" name="portal_type">
 <% String portal_type = mvncoreConfig.getString("mvncoreconfig.portal_type", "");
    String[] arr = {"non-portal", "uportal", "jetspeed2", "liferay", "jboss", "weblogic", "exoplatform"};
    for (int i = 0; i < arr.length; i++) {
      if (arr[i].equals(portal_type)) { %>
      <option value="<%=arr[i]%>" selected="selected"><%=arr[i]%></option>
    <%} else { %>
      <option value="<%=arr[i]%>"><%=arr[i]%></option>
    <%}
    } %>
    </select>
    </td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.allow_http_referer_prefix_list" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="allow_http_referer_prefix_list">allow_http_referer_prefix_list</label></td>
    <td>
    <input type="text" size="70" id="allow_http_referer_prefix_list" name="allow_http_referer_prefix_list" value="<%=mvncoreConfig.getString("mvncoreconfig.allow_http_referer_prefix_list", "")%>" />
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_core"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mvncoreservice_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mvncoreservice_implementation">mvncoreservice_implementation</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="70" id="mvncoreservice_implementation" name="mvncoreservice_implementation" value="<%=mvncoreConfig.getString("mvncoreconfig.mvncoreservice_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.timertaskext_implementation_list" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="timertaskext_implementation_list">timertaskext_implementation_list</label></td>
    <td>
    <input type="text" size="70" id="timertaskext_implementation_list" name="timertaskext_implementation_list" value="<%=mvncoreConfig.getString("mvncoreconfig.timertaskext_implementation_list", "")%>" />
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_mvncore_interceptor"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.mailinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mailinterceptor_implementation">mailinterceptor_implementation</label></td>
    <td>
    <input type="text" size="70" id="mailinterceptor_implementation" name="mailinterceptor_implementation" value="<%=mvncoreConfig.getString("interceptor.mailinterceptor_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.contentinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="contentinterceptor_implementation">contentinterceptor_implementation</label></td>
    <td>
    <input type="text" size="70" id="contentinterceptor_implementation" name="contentinterceptor_implementation" value="<%=mvncoreConfig.getString("interceptor.contentinterceptor_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.loginidinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="loginidinterceptor_implementation">loginidinterceptor_implementation</label></td>
    <td>
    <input type="text" size="70" id="loginidinterceptor_implementation" name="loginidinterceptor_implementation" value="<%=mvncoreConfig.getString("interceptor.loginidinterceptor_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvncore.help.passwordinterceptor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="passwordinterceptor_implementation">passwordinterceptor_implementation</label></td>
    <td>
    <input type="text" size="70" id="passwordinterceptor_implementation" name="passwordinterceptor_implementation" value="<%=mvncoreConfig.getString("interceptor.passwordinterceptor_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/>:</label> <span class="requiredfield">*</span></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="30" onkeypress="checkEnter(event);" /></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save_and_reload"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
      <input type="button" value="<fmt:message key="mvnforum.common.action.cancel"/>" class="liteoption" onclick="javascript:gotoPage('<%=urlResolver.encodeURL(request, response, "configindex")%>');" />
    </td>
  </tr>
</table>
</form>
<br/>
<script type="text/javascript">
//<![CDATA[
    OnChangeUseDatasource();
    OnChangeUseMailsource();
//]]>
</script>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
