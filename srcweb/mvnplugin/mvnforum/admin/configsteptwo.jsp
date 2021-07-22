<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/configsteptwo.jsp,v 1.63 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.63 $
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
 - @author: Igor Manic   
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="net.myvietnam.mvncore.configuration.DOM4JConfiguration" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:setBundle basename="i18n/mvnforum/guide_mvnForum_i18n" scope="request" var="guide" />
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.configsteptwo.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
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
  if (!isUnsignedInteger(document.submitform.max_private_message, "max_private_message")) return false;
  if (isBlank(document.submitform.max_message_attachment_size, "max_message_attachment_size")) return false;
  if (isBlank(document.submitform.max_attachment_size, "max_attachment_size")) return false;
  if (!isUnsignedInteger(document.submitform.max_favorite_thread, "max_favorite_thread")) return false;
  if (!isUnsignedInteger(document.submitform.max_edit_days, "max_edit_days")) return false;
  if (!isUnsignedInteger(document.submitform.max_attach_days, "max_attach_days")) return false;
  if (!isUnsignedInteger(document.submitform.max_delete_days, "max_delete_days")) return false;
  if (!isUnsignedInteger(document.submitform.rows_per_page, "rows_per_page")) return false;
  if (!isUnsignedInteger(document.submitform.rows_per_rss, "rows_per_rss")) return false;
  if (!isUnsignedInteger(document.submitform.hot_topic_threshold, "hot_topic_threshold")) return false;
  if (!isUnsignedInteger(document.submitform.max_http_requests_per_hour_per_ip, "max_http_requests_per_hour_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_http_requests_per_minute_per_ip, "max_http_requests_per_minute_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_posts_per_hour_per_ip, "max_posts_per_hour_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_members_per_hour_per_ip, "max_members_per_hour_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_logins_per_hour_per_ip, "max_logins_per_hour_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_messages_per_hour_per_ip, "max_messages_per_hour_per_ip")) return false;
  if (!isUnsignedInteger(document.submitform.max_password_days, "max_password_days")) return false;
  if (!isUnsignedInteger(document.submitform.max_posts_per_hour_per_member, "max_posts_per_hour_per_member")) return false;
  if (!isUnsignedInteger(document.submitform.max_chars_in_short_summary, "max_chars_in_short_summary")) return false;
  if (!isUnsignedInteger(document.submitform.max_chars_in_long_summary, "max_chars_in_long_summary")) return false;
  if (!isUnsignedInteger(document.submitform.max_chars_in_rss, "max_chars_in_rss")) return false;
  if (!isUnsignedInteger(document.submitform.max_import_size, "max_import_size")) return false;
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
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>
<%
DOM4JConfiguration mvnforumConfig = (DOM4JConfiguration)request.getAttribute("mvnforumConfig");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "configindex")%>"><fmt:message key="mvnforum.admin.configindex.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.configsteptwo.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "configsteptwoprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "configsteptwoprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step2"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_private_message" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_private_message">max_private_message</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_private_message" name="max_private_message" value="<%=mvnforumConfig.getString("mvnforumconfig.max_private_message", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_message_attachment_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_message_attachment_size">max_message_attachment_size</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_message_attachment_size" name="max_message_attachment_size" value="<%=mvnforumConfig.getString("mvnforumconfig.max_message_attachment_size", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_attachment_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_attachment_size">max_attachment_size</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="60" id="max_attachment_size" name="max_attachment_size" value="<%=mvnforumConfig.getString("mvnforumconfig.max_attachment_size", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_favorite_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_favorite_thread">max_favorite_thread</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_favorite_thread" name="max_favorite_thread" value="<%=mvnforumConfig.getString("mvnforumconfig.max_favorite_thread", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_edit_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_edit_days">max_edit_days</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_edit_days" name="max_edit_days" value="<%=mvnforumConfig.getString("mvnforumconfig.max_edit_days", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_attach_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_attach_days">max_attach_days</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_attach_days" name="max_attach_days" value="<%=mvnforumConfig.getString("mvnforumconfig.max_attach_days", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_delete_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_delete_days">max_delete_days</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_delete_days" name="max_delete_days" value="<%=mvnforumConfig.getString("mvnforumconfig.max_delete_days", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.rows_per_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="rows_per_page">rows_per_page</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="rows_per_page" name="rows_per_page" value="<%=mvnforumConfig.getString("mvnforumconfig.rows_per_page", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.rows_per_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="rows_per_rss">rows_per_rss</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="rows_per_rss" name="rows_per_rss" value="<%=mvnforumConfig.getString("mvnforumconfig.rows_per_rss", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.hot_topic_threshold" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="hot_topic_threshold">hot_topic_threshold</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="hot_topic_threshold" name="hot_topic_threshold" value="<%=mvnforumConfig.getString("mvnforumconfig.hot_topic_threshold", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_posts_per_hour_per_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_posts_per_hour_per_member">max_posts_per_hour_per_member</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_posts_per_hour_per_member" name="max_posts_per_hour_per_member" value="<%=mvnforumConfig.getString("mvnforumconfig.max_posts_per_hour_per_member", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_posts_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_posts_per_hour_per_ip">max_posts_per_hour_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_posts_per_hour_per_ip" name="max_posts_per_hour_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_posts_per_hour_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_http_requests_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_http_requests_per_hour_per_ip">max_http_requests_per_hour_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_http_requests_per_hour_per_ip" name="max_http_requests_per_hour_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_http_requests_per_hour_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_http_requests_per_minute_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_http_requests_per_minute_per_ip">max_http_requests_per_minute_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_http_requests_per_minute_per_ip" name="max_http_requests_per_minute_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_http_requests_per_minute_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_members_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_members_per_hour_per_ip">max_members_per_hour_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_members_per_hour_per_ip" name="max_members_per_hour_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_members_per_hour_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_logins_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_logins_per_hour_per_ip">max_logins_per_hour_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_logins_per_hour_per_ip" name="max_logins_per_hour_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_logins_per_hour_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_messages_per_hour_per_ip" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_messages_per_hour_per_ip">max_messages_per_hour_per_ip</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_messages_per_hour_per_ip" name="max_messages_per_hour_per_ip" value="<%=mvnforumConfig.getString("mvnforumconfig.max_messages_per_hour_per_ip", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_password_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_password_days">max_password_days</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_password_days" name="max_password_days" value="<%=mvnforumConfig.getString("mvnforumconfig.max_password_days", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_short_summary" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_chars_in_short_summary">max_chars_in_short_summary</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_chars_in_short_summary" name="max_chars_in_short_summary" value="<%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_short_summary", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_long_summary" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_chars_in_long_summary">max_chars_in_long_summary</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_chars_in_long_summary" name="max_chars_in_long_summary" value="<%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_long_summary", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_chars_in_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_chars_in_rss">max_chars_in_rss</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_chars_in_rss" name="max_chars_in_rss" value="<%=mvnforumConfig.getString("mvnforumconfig.max_chars_in_rss", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_backup_on_server" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_backup_on_server">enable_backup_on_server</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_backup_on_server = mvnforumConfig.getString("mvnforumconfig.enable_backup_on_server", "");%>
    <input type="checkbox" id="enable_backup_on_server" name="enable_backup_on_server" value="true" class="noborder"
    <% if ("true".equals(enable_backup_on_server)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_import_size" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_import_size">max_import_size</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="60" id="max_import_size" name="max_import_size" value="<%=mvnforumConfig.getString("mvnforumconfig.max_import_size", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/>:</label> <span class="requiredfield">*</span></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="30" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save_and_reload"/>" class="portlet-form-button" onclick="javascript:SubmitForm();" />
      <input type="button" value="<fmt:message key="mvnforum.common.action.cancel"/>" class="liteoption" onclick="javascript:gotoPage('<%=urlResolver.encodeURL(request, response, "configindex")%>');" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
