<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/configstepone.jsp,v 1.103 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.103 $
 - $Date: 2009/07/16 03:21:12 $
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.configstepone.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

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
  if (isBlank(document.submitform.mvnforum_home, "mvnforum_home")) return false;
  if (isBlank(document.submitform.mvnforum_log, "mvnforum_log")) return false;
  if (isBlank(document.submitform.webmaster_email, "webmaster_email")) return false;
  if (!isEmail(document.submitform.webmaster_email, "websmater_email")) return false;
  if (isBlank(document.submitform.watch_email, "watch_email")) return false;
  if (!isEmail(document.submitform.watch_email, "watch_email")) return false; 
  if (isBlank(document.submitform.logo_url, "logo_url")) return false;
  if (isBlank(document.submitform.redirect_login_url, "redirect_login_url")) return false;
  //if (isBlank(document.submitform.supported_locales, "mvnforum.admin.config.supported_locales")) return false;
  if (isBlank(document.submitform.default_locale_name, "default_locale_name")) return false;
  if (isBlank(document.submitform.locale_parameter_name, "locale_parameter_name")) return false;
  if (isBlank(document.submitform.default_guest_name, "default_guest_name")) return false;
  if (!isUnsignedInteger(document.submitform.default_guest_timezone, "default_guest_timezone")) return false;
  if (!isUnsignedInteger(document.submitform.max_last_post_body_in_watch, "max_last_post_body_in_watch")) return false;
  if (!isUnsignedInteger(document.submitform.days_to_show_recent_members, "days_to_show_recent_members")) return false;
  
  if (isBlank(document.submitform.member_implementation, "member_implementation")) return false;
  if (isBlank(document.submitform.onlineuser_implementation, "onlineuser_implementation")) return false;
  if (isBlank(document.submitform.requestprocessor_implementation, "requestprocessor_implementation")) return false;
  if (isBlank(document.submitform.lucene_analyzer_implementation, "lucene_analyzer_implementation")) return false;
  if (isBlank(document.submitform.mvnforum_service_implementation, "mvnforum_service_implementation")) return false;
  if (isBlank(document.submitform.mvn_auth_service_implementation, "mvn_auth_service_implementation")) return false;
  
  if (document.submitform.enable_image_thumbnail.checked) {
    if (!isUnsignedInteger(document.submitform.image_thumbnail_width, "image_thumbnail_width")) return false;
    if (!isUnsignedInteger(document.submitform.image_thumbnail_height, "image_thumbnail_height")) return false;
  }
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
function OnChangeImageThumbnailStatus() {
  if (document.submitform.enable_image_thumbnail.checked == false) {
    document.getElementById("id_image_thumbnail_width").style.display = 'none';
    document.getElementById("id_image_thumbnail_height").style.display = 'none';
  } else {
    document.getElementById("id_image_thumbnail_width").style.display = '';
    document.getElementById("id_image_thumbnail_height").style.display = '';
  }
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
  <fmt:message key="mvnforum.admin.configstepone.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "configsteponeprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "configsteponeprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_core"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="32%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_home" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mvnforum_home">mvnforum_home</label> <span class="requiredfield">*</span></td>
    <td>
     <% String  mvnforum_home_current = mvnforumConfig.getString("mvnforumconfig.mvnforum_home", "");
        String  mvnforum_home_suggestion = application.getRealPath("/WEB-INF/mvnForumHome");
      %>
      <input type="text" size="70" id="mvnforum_home" name="mvnforum_home" value="<%=mvnforum_home_current%>" />
     <% if (mvnforum_home_current.equals(mvnforum_home_suggestion) == false) { %> 
     <br/>Suggestion: <b><%=mvnforum_home_suggestion%></b> 
     <%}%>
    </td> 
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_log" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mvnforum_log">mvnforum_log</label> <span class="requiredfield">*</span></td>
    <td>
     <% String  mvnforum_log_current = mvnforumConfig.getString("mvnforumconfig.mvnforum_log", "");
        String  mvnforum_log_suggestion = application.getRealPath("/WEB-INF/mvnForumHome/log/mvnforum.log");
      %>
     <input type="text" size="70" id="mvnforum_log" name="mvnforum_log" value="<%=mvnforum_log_current%>" />
     
     <% if (mvnforum_log_current.equals(mvnforum_log_suggestion) == false) { %> 
     <br/>Suggestion: <b><%=mvnforum_log_suggestion%></b>
     <%}%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.webmaster_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="webmaster_email">webmaster_email</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="webmaster_email" name="webmaster_email" value="<%=mvnforumConfig.getString("mvnforumconfig.webmaster_email", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.watch_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="watch_email">watch_email</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="watch_email" name="watch_email" value="<%=mvnforumConfig.getString("mvnforumconfig.watch_email", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.logo_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="logo_url">logo_url</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="logo_url" name="logo_url" value="<%=mvnforumConfig.getString("mvnforumconfig.logo_url", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.redirect_login_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="redirect_login_url">redirect_login_url</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="redirect_login_url" name="redirect_login_url" value="<%=mvnforumConfig.getString("mvnforumconfig.redirect_login_url", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.redirect_logout_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="redirect_logout_url">redirect_logout_url</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="redirect_logout_url" name="redirect_logout_url" value="<%=mvnforumConfig.getString("mvnforumconfig.redirect_logout_url", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.supported_locales" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="supported_locales">supported_locales</label></td>
    <td><input type="text" size="70" id="supported_locales" name="supported_locales" value="<%=mvnforumConfig.getString("mvnforumconfig.supported_locales", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_locale_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_locale_name">default_locale_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="default_locale_name" name="default_locale_name" value="<%=mvnforumConfig.getString("mvnforumconfig.default_locale_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.locale_parameter_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="locale_parameter_name">locale_parameter_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="locale_parameter_name" name="locale_parameter_name" value="<%=mvnforumConfig.getString("mvnforumconfig.locale_parameter_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_guest_name" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_guest_name">default_guest_name</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="default_guest_name" name="default_guest_name" value="<%=mvnforumConfig.getString("mvnforumconfig.default_guest_name", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_guest_timezone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_guest_timezone">default_guest_timezone</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="default_guest_timezone" name="default_guest_timezone" value="<%=mvnforumConfig.getString("mvnforumconfig.default_guest_timezone", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_cache_member">enable_cache_member</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_cache_member = mvnforumConfig.getString("mvnforumconfig.enable_cache_member", "");%>
    <input type="checkbox" id="enable_cache_member" name="enable_cache_member" value="true" class="noborder"
    <% if ("true".equals(enable_cache_member)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_cache_post">enable_cache_post</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_cache_post = mvnforumConfig.getString("mvnforumconfig.enable_cache_post", "");%>
    <input type="checkbox" id="enable_cache_post" name="enable_cache_post" value="true" class="noborder"
    <% if ("true".equals(enable_cache_post)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_cache_thread">enable_cache_thread</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_cache_thread = mvnforumConfig.getString("mvnforumconfig.enable_cache_thread", "");%>
    <input type="checkbox" id="enable_cache_thread" name="enable_cache_thread" value="true" class="noborder"
    <% if ("true".equals(enable_cache_thread)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_forum" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_cache_forum">enable_cache_forum</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_cache_forum = mvnforumConfig.getString("mvnforumconfig.enable_cache_forum", "");%>    
    <input type="checkbox" id="enable_cache_forum" name="enable_cache_forum" value="true" class="noborder"
    <% if ("true".equals(enable_cache_forum)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_cache_category" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_cache_category">enable_cache_category</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_cache_category = mvnforumConfig.getString("mvnforumconfig.enable_cache_category", "");%>
    <input type="checkbox" id="enable_cache_category" name="enable_cache_category" value="true" class="noborder"
    <% if ("true".equals(enable_cache_category)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_passwordless_auth" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_passwordless_auth">enable_passwordless_auth</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_passwordless_auth = mvnforumConfig.getString("mvnforumconfig.enable_passwordless_auth", "");%>    
    <input type="checkbox" id="enable_passwordless_auth" name="enable_passwordless_auth" value="true" class="noborder"
    <% if ("true".equals(enable_passwordless_auth)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_cookie" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_login_info_in_cookie">enable_login_info_in_cookie</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_login_info_in_cookie = mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_cookie", "");%>
    <input type="checkbox" id="enable_login_info_in_cookie" name="enable_login_info_in_cookie" value="true" class="noborder"
    <% if ("true".equals(enable_login_info_in_cookie)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_session" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_login_info_in_session">enable_login_info_in_session</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_login_info_in_session = mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_session", "");%>    
    <input type="checkbox" id="enable_login_info_in_session" name="enable_login_info_in_session" value="true" class="noborder"
    <% if ("true".equals(enable_login_info_in_session)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_realm" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_login_info_in_realm">enable_login_info_in_realm</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_login_info_in_realm = mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_realm", "");%>
    <input type="checkbox" id="enable_login_info_in_realm" name="enable_login_info_in_realm" value="true" class="noborder"
    <% if ("true".equals(enable_login_info_in_realm)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login_info_in_customization" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_login_info_in_customization">enable_login_info_in_customization</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_login_info_in_customization = mvnforumConfig.getString("mvnforumconfig.enable_login_info_in_customization", "");%>
    <input type="checkbox" id="enable_login_info_in_customization" name="enable_login_info_in_customization" value="true" class="noborder"
    <% if ("true".equals(enable_login_info_in_customization)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_friendly_url" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_friendly_url">enable_friendly_url</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_friendly_url = mvnforumConfig.getString("mvnforumconfig.enable_friendly_url", "");%>
    <input type="checkbox" id="enable_friendly_url" name="enable_friendly_url" value="true" class="noborder"
    <% if ("true".equals(enable_friendly_url)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_check_invalid_session" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_check_invalid_session">enable_check_invalid_session</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_check_invalid_session = mvnforumConfig.getString("mvnforumconfig.enable_check_invalid_session", "");%>
    <input type="checkbox" id="enable_check_invalid_session" name="enable_check_invalid_session" value="true" class="noborder"
    <% if ("true".equals(enable_check_invalid_session)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_activation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_activation">require_activation</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_activation = mvnforumConfig.getString("mvnforumconfig.require_activation", "");%>
    <input type="checkbox" id="require_activation" name="require_activation" value="true" class="noborder"
    <% if ("true".equals(require_activation)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_login" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_login">enable_login</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_login = mvnforumConfig.getString("mvnforumconfig.enable_login", "");%>
    <input type="checkbox" id="enable_login" name="enable_login" value="true" class="noborder"
    <% if ("true".equals(enable_login)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_new_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_new_member">enable_new_member</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_new_member = mvnforumConfig.getString("mvnforumconfig.enable_new_member", "");%>
    <input type="checkbox" id="enable_new_member" name="enable_new_member" value="true" class="noborder"
    <% if ("true".equals(enable_new_member)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_new_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_new_post">enable_new_post</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_new_post = mvnforumConfig.getString("mvnforumconfig.enable_new_post", "");%>
    <input type="checkbox" id="enable_new_post" name="enable_new_post" value="true" class="noborder"
    <% if ("true".equals(enable_new_post)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_rss" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_rss">enable_rss</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_rss = mvnforumConfig.getString("mvnforumconfig.enable_rss", "");%>
    <input type="checkbox" id="enable_rss" name="enable_rss" value="true" class="noborder"
    <% if ("true".equals(enable_rss)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_watch" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_watch">enable_watch</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_watch = mvnforumConfig.getString("mvnforumconfig.enable_watch", "");%>
    <input type="checkbox" id="enable_watch" name="enable_watch" value="true" class="noborder"
    <% if ("true".equals(enable_watch)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_attachment">enable_attachment</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_attachment = mvnforumConfig.getString("mvnforumconfig.enable_attachment", "");%>
    <input type="checkbox" id="enable_attachment" name="enable_attachment" value="true" class="noborder"
    <% if ("true".equals(enable_attachment)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_avatar" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_avatar">enable_avatar</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_avatar = mvnforumConfig.getString("mvnforumconfig.enable_avatar", "");%>
    <input type="checkbox" id="enable_avatar" name="enable_avatar" value="true" class="noborder"
    <% if ("true".equals(enable_avatar)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_emoticon" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_emoticon">enable_emoticon</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_emoticon = mvnforumConfig.getString("mvnforumconfig.enable_emoticon", "");%>
    <input type="checkbox" id="enable_emoticon" name="enable_emoticon" value="true" class="noborder"
    <% if ("true".equals(enable_emoticon)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_captcha" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_captcha">enable_captcha</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_captcha = mvnforumConfig.getString("mvnforumconfig.enable_captcha", "");%>
    <input type="checkbox" id="enable_captcha" name="enable_captcha" value="true" class="noborder"
    <% if ("true".equals(enable_captcha)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_portal_like_index_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_portal_like_index_page">enable_portal_like_index_page</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_portal_like_index_page = mvnforumConfig.getString("mvnforumconfig.enable_portal_like_index_page", "");%>
    <input type="checkbox" id="enable_portal_like_index_page" name="enable_portal_like_index_page" value="true" class="noborder"
    <% if ("true".equals(enable_portal_like_index_page)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_search" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_search">enable_search</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_search = mvnforumConfig.getString("mvnforumconfig.enable_search", "");%>
    <input type="checkbox" id="enable_search" name="enable_search" value="true" class="noborder"
    <% if ("true".equals(enable_search)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_search_order_by" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_search_order_by">default_search_order_by</label> <span class="requiredfield">*</span></td>
    <td>
    <select id="default_search_order_by" name="default_search_order_by">
    <% String default_search_order_by = mvnforumConfig.getString("mvnforumconfig.default_search_order_by", "");%>
      <option value="<%=PostSearchQuery.SEARCH_SORT_DEFAULT%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_DEFAULT).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>>Relevant</option>
      <option value="<%=PostSearchQuery.SEARCH_SORT_TIME_DESC%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_TIME_DESC).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>>Time Descending</option>
      <option value="<%=PostSearchQuery.SEARCH_SORT_TIME_ASC%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_TIME_ASC).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>>Time Ascending</option>
    </select>
    </td>
  </tr>  
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_online_users" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_online_users">enable_online_users</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_online_users = mvnforumConfig.getString("mvnforumconfig.enable_online_users", "");%>
    <input type="checkbox" id="enable_online_users" name="enable_online_users" value="true" class="noborder"
    <% if ("true".equals(enable_online_users)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_duplicate_onlineusers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_duplicate_onlineusers">enable_duplicate_onlineusers</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_duplicate_onlineusers = mvnforumConfig.getString("mvnforumconfig.enable_duplicate_onlineusers", "");%>
    <input type="checkbox" id="enable_duplicate_onlineusers" name="enable_duplicate_onlineusers" value="true" class="noborder"
    <% if ("true".equals(enable_duplicate_onlineusers)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_invisible_users" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_invisible_users">enable_invisible_users</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_invisible_users = mvnforumConfig.getString("mvnforumconfig.enable_invisible_users", "");%>
    <input type="checkbox" id="enable_invisible_users" name="enable_invisible_users" value="true" class="noborder"
    <% if ("true".equals(enable_invisible_users)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_listmembers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_listmembers">enable_listmembers</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_listmembers = mvnforumConfig.getString("mvnforumconfig.enable_listmembers", "");%>
    <input type="checkbox" id="enable_listmembers" name="enable_listmembers" value="true" class="noborder"
    <% if ("true".equals(enable_listmembers)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_last_login_of_current_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_last_login_of_current_member">enable_show_last_login_of_current_member</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_last_login_of_current_member = mvnforumConfig.getString("mvnforumconfig.enable_show_last_login_of_current_member", "");%>
    <input type="checkbox" id="enable_show_last_login_of_current_member" name="enable_show_last_login_of_current_member" value="true" class="noborder"
    <% if ("true".equals(enable_show_last_login_of_current_member)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_last_login" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_last_login">enable_show_last_login</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_last_login = mvnforumConfig.getString("mvnforumconfig.enable_show_last_login", "");%>
    <input type="checkbox" id="enable_show_last_login" name="enable_show_last_login" value="true" class="noborder"
    <% if ("true".equals(enable_show_last_login)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_auto_watching" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_auto_watching">enable_auto_watching</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_auto_watching = mvnforumConfig.getString("mvnforumconfig.enable_auto_watching", "");%>
    <input type="checkbox" id="enable_auto_watching" name="enable_auto_watching" value="true" class="noborder"
    <% if ("true".equals(enable_auto_watching)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_birthday" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_birthday">enable_show_birthday</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_birthday = mvnforumConfig.getString("mvnforumconfig.enable_show_birthday", "");%>
    <input type="checkbox" id="enable_show_birthday" name="enable_show_birthday" value="true" class="noborder"
    <% if ("true".equals(enable_show_birthday)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_gender" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_gender">enable_show_gender</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_gender = mvnforumConfig.getString("mvnforumconfig.enable_show_gender", "");%>
    <input type="checkbox" id="enable_show_gender" name="enable_show_gender" value="true" class="noborder"
    <% if ("true".equals(enable_show_gender)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_address" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_address">enable_show_address</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_address = mvnforumConfig.getString("mvnforumconfig.enable_show_address", "");%>
    <input type="checkbox" id="enable_show_address" name="enable_show_address" value="true" class="noborder"
    <% if ("true".equals(enable_show_address)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_city" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_city">enable_show_city</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_city = mvnforumConfig.getString("mvnforumconfig.enable_show_city", "");%>
    <input type="checkbox" id="enable_show_city" name="enable_show_city" value="true" class="noborder"
    <% if ("true".equals(enable_show_city)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_state" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_state">enable_show_state</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_state = mvnforumConfig.getString("mvnforumconfig.enable_show_state", "");%>
    <input type="checkbox" id="enable_show_state" name="enable_show_state" value="true" class="noborder"
    <% if ("true".equals(enable_show_state)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_homepage" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_homepage">enable_show_homepage</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_homepage = mvnforumConfig.getString("mvnforumconfig.enable_show_homepage", "");%>
    <input type="checkbox" id="enable_show_homepage" name="enable_show_homepage" value="true" class="noborder"
    <% if ("true".equals(enable_show_homepage)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_country" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_country">enable_show_country</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_country = mvnforumConfig.getString("mvnforumconfig.enable_show_country", "");%>
    <input type="checkbox" id="enable_show_country" name="enable_show_country" value="true" class="noborder"
    <% if ("true".equals(enable_show_country)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_phone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_phone">enable_show_phone</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_phone = mvnforumConfig.getString("mvnforumconfig.enable_show_phone", "");%>
    <input type="checkbox" id="enable_show_phone" name="enable_show_phone" value="true" class="noborder"
    <% if ("true".equals(enable_show_phone)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_mobile" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_mobile">enable_show_mobile</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_mobile = mvnforumConfig.getString("mvnforumconfig.enable_show_mobile", "");%>
    <input type="checkbox" id="enable_show_mobile" name="enable_show_mobile" value="true" class="noborder"
    <% if ("true".equals(enable_show_mobile)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_fax" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_fax">enable_show_fax</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_fax = mvnforumConfig.getString("mvnforumconfig.enable_show_fax", "");%>
    <input type="checkbox" id="enable_show_fax" name="enable_show_fax" value="true" class="noborder"
    <% if ("true".equals(enable_show_fax)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_career" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_career">enable_show_career</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_career = mvnforumConfig.getString("mvnforumconfig.enable_show_career", "");%>
    <input type="checkbox" id="enable_show_career" name="enable_show_career" value="true" class="noborder"
    <% if ("true".equals(enable_show_career)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_cool_link_1" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_cool_link_1">enable_show_cool_link_1</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_cool_link_1 = mvnforumConfig.getString("mvnforumconfig.enable_show_cool_link_1", "");%>
    <input type="checkbox" id="enable_show_cool_link_1" name="enable_show_cool_link_1" value="true" class="noborder"
    <% if ("true".equals(enable_show_cool_link_1)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_cool_link_2" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_cool_link_2">enable_show_cool_link_2</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_cool_link_2 = mvnforumConfig.getString("mvnforumconfig.enable_show_cool_link_2", "");%>
    <input type="checkbox" id="enable_show_cool_link_2" name="enable_show_cool_link_2" value="true" class="noborder"
    <% if ("true".equals(enable_show_cool_link_2)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_yahoo" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_yahoo">enable_show_yahoo</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_yahoo = mvnforumConfig.getString("mvnforumconfig.enable_show_yahoo", "");%>
    <input type="checkbox" id="enable_show_yahoo" name="enable_show_yahoo" value="true" class="noborder"
    <% if ("true".equals(enable_show_yahoo)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_aol" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_aol">enable_show_aol</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_aol = mvnforumConfig.getString("mvnforumconfig.enable_show_aol", "");%>
    <input type="checkbox" id="enable_show_aol" name="enable_show_aol" value="true" class="noborder"
    <% if ("true".equals(enable_show_aol)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_icq" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_icq">enable_show_icq</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_icq = mvnforumConfig.getString("mvnforumconfig.enable_show_icq", "");%>
    <input type="checkbox" id="enable_show_icq" name="enable_show_icq" value="true" class="noborder"
    <% if ("true".equals(enable_show_icq)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_msn" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_msn">enable_show_msn</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_msn = mvnforumConfig.getString("mvnforumconfig.enable_show_msn", "");%>
    <input type="checkbox" id="enable_show_msn" name="enable_show_msn" value="true" class="noborder"
    <% if ("true".equals(enable_show_msn)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_join_date" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_join_date">enable_show_join_date</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_join_date = mvnforumConfig.getString("mvnforumconfig.enable_show_join_date", "");%>
    <input type="checkbox" id="enable_show_join_date" name="enable_show_join_date" value="true" class="noborder"
    <% if ("true".equals(enable_show_join_date)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_post_count" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_post_count">enable_show_post_count</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_post_count = mvnforumConfig.getString("mvnforumconfig.enable_show_post_count", "");%>
    <input type="checkbox" id="enable_show_post_count" name="enable_show_post_count" value="true" class="noborder"
    <% if ("true".equals(enable_show_post_count)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_view_count" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_view_count">enable_show_view_count</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_view_count = mvnforumConfig.getString("mvnforumconfig.enable_show_view_count", "");%>
    <input type="checkbox" id="enable_show_view_count" name="enable_show_view_count" value="true" class="noborder"
    <% if ("true".equals(enable_show_view_count)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_email" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_email">enable_show_email</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_email = mvnforumConfig.getString("mvnforumconfig.enable_show_email", "");%>
    <input type="checkbox" id="enable_show_email" name="enable_show_email" value="true" class="noborder"
    <% if ("true".equals(enable_show_email)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_email_visible" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_email_visible">enable_show_email_visible</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_email_visible = mvnforumConfig.getString("mvnforumconfig.enable_show_email_visible", "");%>
    <input type="checkbox" id="enable_show_email_visible" name="enable_show_email_visible" value="true" class="noborder"
    <% if ("true".equals(enable_show_email_visible)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_name_visible" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_name_visible">enable_show_name_visible</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_name_visible = mvnforumConfig.getString("mvnforumconfig.enable_show_name_visible", "");%>
    <input type="checkbox" id="enable_show_name_visible" name="enable_show_name_visible" value="true" class="noborder"
    <% if ("true".equals(enable_show_name_visible)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_posts_per_page" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_posts_per_page">enable_show_posts_per_page</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_posts_per_page = mvnforumConfig.getString("mvnforumconfig.enable_show_posts_per_page", "");%>
    <input type="checkbox" id="enable_show_posts_per_page" name="enable_show_posts_per_page" value="true" class="noborder"
    <% if ("true".equals(enable_show_posts_per_page)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="enable_show_modified_date" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_modified_date">enable_show_modified_date</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_modified_date = mvnforumConfig.getString("mvnforumconfig.enable_show_modified_date", "");%>
    <input type="checkbox" id="enable_show_modified_date" name="enable_show_modified_date" value="true" class="noborder"
    <% if ("true".equals(enable_show_modified_date)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_signature" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_signature">enable_show_signature</label> <span class="requiredfield">*</span></td>
    <td>    
    <% String enable_show_signature = mvnforumConfig.getString("mvnforumconfig.enable_show_signature", "");%>
    <input type="checkbox" id="enable_show_signature" name="enable_show_signature" value="true" class="noborder"
    <% if ("true".equals(enable_show_signature)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_online_status" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_online_status">enable_show_online_status</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_online_status = mvnforumConfig.getString("mvnforumconfig.enable_show_online_status", "");%>
    <input type="checkbox" id="enable_show_online_status" name="enable_show_online_status" value="true" class="noborder"
    <% if ("true".equals(enable_show_online_status)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_firstname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_firstname">enable_show_firstname</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_firstname = mvnforumConfig.getString("mvnforumconfig.enable_show_firstname", "");%>
    <input type="checkbox" id="enable_show_firstname" name="enable_show_firstname" value="true" class="noborder"
    <% if ("true".equals(enable_show_firstname)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_show_lastname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_show_lastname">enable_show_lastname</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_show_lastname = mvnforumConfig.getString("mvnforumconfig.enable_show_lastname", "");%>
    <input type="checkbox" id="enable_show_lastname" name="enable_show_lastname" value="true" class="noborder"
    <% if ("true".equals(enable_show_lastname)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_use_popup_menu_in_viewthread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_use_popup_menu_in_viewthread">enable_use_popup_menu_in_viewthread</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_use_popup_menu_in_viewthread = mvnforumConfig.getString("mvnforumconfig.enable_use_popup_menu_in_viewthread", "");%>
    <input type="checkbox" id="enable_use_popup_menu_in_viewthread" name="enable_use_popup_menu_in_viewthread" value="true" class="noborder"
    <% if ("true".equals(enable_use_popup_menu_in_viewthread)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_rich_text_editor" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_rich_text_editor">enable_rich_text_editor</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_rich_text_editor = mvnforumConfig.getString("mvnforumconfig.enable_rich_text_editor", "");%>
    <input type="checkbox" id="enable_use_popup_menu_in_viewthread" name="enable_rich_text_editor" value="true" class="noborder"
    <% if ("true".equals(enable_rich_text_editor)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_split_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_split_thread">enable_split_thread</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_split_thread = mvnforumConfig.getString("mvnforumconfig.enable_split_thread", "");%>
    <input type="checkbox" id="enable_split_thread" name="enable_split_thread" value="true" class="noborder"
    <% if ("true".equals(enable_split_thread)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>  
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_listunansweredthreads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_listunansweredthreads">enable_listunansweredthreads</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_listunansweredthreads = mvnforumConfig.getString("mvnforumconfig.enable_listunansweredthreads", "");%>
    <input type="checkbox" id="enable_listunansweredthreads" name="enable_listunansweredthreads" value="true" class="noborder"
    <% if ("true".equals(enable_listunansweredthreads)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_users_browsing_thread" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_list_users_browsing_thread">enable_list_users_browsing_thread</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_list_users_browsing_thread = mvnforumConfig.getString("mvnforumconfig.enable_list_users_browsing_thread", "");%>
    <input type="checkbox" id="enable_list_users_browsing_thread" name="enable_list_users_browsing_thread" value="true" class="noborder"
    <% if ("true".equals(enable_list_users_browsing_thread)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_email_to_admin_content_with_censored_words" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_email_to_admin_content_with_censored_words">enable_email_to_admin_content_with_censored_words</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_email_to_admin_content_with_censored_words = mvnforumConfig.getString("mvnforumconfig.enable_email_to_admin_content_with_censored_words", "");%>
    <input type="checkbox" id="enable_email_to_admin_content_with_censored_words" name="enable_email_to_admin_content_with_censored_words" value="true" class="noborder"
    <% if ("true".equals(enable_email_to_admin_content_with_censored_words)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_guest_view_listusers" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_guest_view_listusers">enable_guest_view_listusers</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_guest_view_listusers = mvnforumConfig.getString("mvnforumconfig.enable_guest_view_listusers", "");%>
    <input type="checkbox" id="enable_guest_view_listusers" name="enable_guest_view_listusers" value="true" class="noborder"
    <% if ("true".equals(enable_guest_view_listusers)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.only_normal_thread_type_in_active_threads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="only_normal_thread_type_in_active_threads">only_normal_thread_type_in_active_threads</label> <span class="requiredfield">*</span></td>
    <td>
    <% String only_normal_thread_type_in_active_threads = mvnforumConfig.getString("mvnforumconfig.only_normal_thread_type_in_active_threads", "");%>
    <input type="checkbox" id="only_normal_thread_type_in_active_threads" name="only_normal_thread_type_in_active_threads" value="true" class="noborder"
    <% if ("true".equals(only_normal_thread_type_in_active_threads)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.send_watchmail_as_html" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="send_watchmail_as_html">send_watchmail_as_html</label> <span class="requiredfield">*</span></td>
    <td>
    <% String send_watchmail_as_html = mvnforumConfig.getString("mvnforumconfig.send_watchmail_as_html", "");%>
    <input type="checkbox" id="send_watchmail_as_html" name="send_watchmail_as_html" value="true" class="noborder"
    <% if ("true".equals(send_watchmail_as_html)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_easy_watching" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_easy_watching">enable_easy_watching</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_easy_watching = mvnforumConfig.getString("mvnforumconfig.enable_easy_watching", "");%>
    <input type="checkbox" id="enable_easy_watching" name="enable_easy_watching" value="true" class="noborder"
    <% if ("true".equals(enable_easy_watching)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_send_watch_mail_of_my_own_post" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_send_watch_mail_of_my_own_post">enable_send_watch_mail_of_my_own_post</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_send_watch_mail_of_my_own_post = mvnforumConfig.getString("mvnforumconfig.enable_send_watch_mail_of_my_own_post", "");%>
    <input type="checkbox" id="enable_send_watch_mail_of_my_own_post" name="enable_send_watch_mail_of_my_own_post" value="true" class="noborder"
    <% if ("true".equals(enable_send_watch_mail_of_my_own_post)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.max_last_post_body_in_watch" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="max_last_post_body_in_watch">max_last_post_body_in_watch</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="max_last_post_body_in_watch" name="max_last_post_body_in_watch" value="<%=mvnforumConfig.getString("mvnforumconfig.max_last_post_body_in_watch", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_private_message" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_private_message">enable_private_message</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_private_message = mvnforumConfig.getString("mvnforumconfig.enable_private_message", "");%>
    <input type="checkbox" id="enable_private_message" name="enable_private_message" value="true" class="noborder"
    <% if ("true".equals(enable_private_message)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_public_message" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_public_message">enable_public_message</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_public_message = mvnforumConfig.getString("mvnforumconfig.enable_public_message", "");%>
    <input type="checkbox" id="enable_public_message" name="enable_public_message" value="true" class="noborder"
    <% if ("true".equals(enable_public_message)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_message_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_message_attachment">enable_message_attachment</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_message_attachment = mvnforumConfig.getString("mvnforumconfig.enable_message_attachment", "");%>
    <input type="checkbox" id="enable_message_attachment" name="enable_message_attachment" value="true" class="noborder"
    <% if ("true".equals(enable_message_attachment)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_most_active_threads" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_most_active_threads">enable_most_active_threads</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_most_active_threads = mvnforumConfig.getString("mvnforumconfig.enable_most_active_threads", "");%>
    <input type="checkbox" id="enable_most_active_threads" name="enable_most_active_threads" value="true" class="noborder"
    <% if ("true".equals(enable_most_active_threads)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_most_active_members" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_most_active_members">enable_most_active_members</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_most_active_members = mvnforumConfig.getString("mvnforumconfig.enable_most_active_members", "");%>
    <input type="checkbox" id="enable_most_active_members" name="enable_most_active_members" value="true" class="noborder"
    <% if ("true".equals(enable_most_active_members)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_site_statistics_overview" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_site_statistics_overview">enable_site_statistics_overview</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_site_statistics_overview = mvnforumConfig.getString("mvnforumconfig.enable_site_statistics_overview", "");%>
    <input type="checkbox" id="enable_site_statistics_overview" name="enable_site_statistics_overview" value="true" class="noborder"
    <% if ("true".equals(enable_site_statistics_overview)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_admin_can_change_password" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_admin_can_change_password">enable_admin_can_change_password</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_admin_can_change_password = mvnforumConfig.getString("mvnforumconfig.enable_admin_can_change_password", "");%>
    <input type="checkbox" id="enable_admin_can_change_password" name="enable_admin_can_change_password" value="true" class="noborder"
    <% if ("true".equals(enable_admin_can_change_password)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_guest_view_image_attachment" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_guest_view_image_attachment">enable_guest_view_image_attachment</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_guest_view_image_attachment = mvnforumConfig.getString("mvnforumconfig.enable_guest_view_image_attachment", "");%>
    <input type="checkbox" id="enable_guest_view_image_attachment" name="enable_guest_view_image_attachment" value="true" class="noborder"
    <% if ("true".equals(enable_guest_view_image_attachment)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_expanse_category_tree_by_default" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_expanse_category_tree_by_default">enable_expanse_category_tree_by_default</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_expanse_category_tree_by_default = mvnforumConfig.getString("mvnforumconfig.enable_expanse_category_tree_by_default", "");%>
    <input type="checkbox" id="enable_expanse_category_tree_by_default" name="enable_expanse_category_tree_by_default" value="true" class="noborder"
    <% if ("true".equals(enable_expanse_category_tree_by_default)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_watch_option" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_watch_option">default_watch_option</label> <span class="requiredfield">*</span></td>
    <td>
    <select id="default_watch_option" name="default_watch_option">
    <% String default_watch_option = mvnforumConfig.getString("mvnforumconfig.default_watch_option", "");%>
      <option value="<%=WatchBean.WATCH_OPTION_DEFAULT%>" <%= (String.valueOf(WatchBean.WATCH_OPTION_DEFAULT).equals(default_watch_option)) ? "selected=\"selected\"" : "" %>>Default (daily watch)</option>
      <option value="<%=WatchBean.WATCH_OPTION_LIVE%>" <%= (String.valueOf(WatchBean.WATCH_OPTION_LIVE).equals(default_watch_option)) ? "selected=\"selected\"" : "" %>>Live Watch</option>
      <option value="<%=WatchBean.WATCH_OPTION_HOURLY%>" <%= (String.valueOf(WatchBean.WATCH_OPTION_HOURLY).equals(default_watch_option)) ? "selected=\"selected\"" : "" %>>Hourly Watch</option>
      <option value="<%=WatchBean.WATCH_OPTION_DAILY%>" <%= (String.valueOf(WatchBean.WATCH_OPTION_DAILY).equals(default_watch_option)) ? "selected=\"selected\"" : "" %>>Daily Watch</option>
      <option value="<%=WatchBean.WATCH_OPTION_WEEKLY%>" <%= (String.valueOf(WatchBean.WATCH_OPTION_WEEKLY).equals(default_watch_option)) ? "selected=\"selected\"" : "" %>>Weekly Watch</option>
    </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_moderation_option" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_moderation_option">default_moderation_option</label> <span class="requiredfield">*</span></td>
    <td>
    <select id="default_moderation_option" name="default_moderation_option">
    <% String default_moderation_option = mvnforumConfig.getString("mvnforumconfig.default_moderation_option", "");%>
      <option value="<%=ForumBean.FORUM_MODERATION_MODE_NO_MODERATION%>" <%= (String.valueOf(ForumBean.FORUM_MODERATION_MODE_NO_MODERATION).equals(default_moderation_option)) ? "selected=\"selected\"" : "" %>>Default (No Moderation)</option>
      <option value="<%=ForumBean.FORUM_MODERATION_MODE_THREAD_AND_POST%>" <%= (String.valueOf(ForumBean.FORUM_MODERATION_MODE_THREAD_AND_POST).equals(default_moderation_option)) ? "selected=\"selected\"" : "" %>>Thread and Post Moderation</option>
      <option value="<%=ForumBean.FORUM_MODERATION_MODE_THREAD_ONLY%>" <%= (String.valueOf(ForumBean.FORUM_MODERATION_MODE_THREAD_ONLY).equals(default_moderation_option)) ? "selected=\"selected\"" : "" %>>Thread only Moderation</option>
      <option value="<%=ForumBean.FORUM_MODERATION_MODE_POST_ONLY%>" <%= (String.valueOf(ForumBean.FORUM_MODERATION_MODE_POST_ONLY).equals(default_moderation_option)) ? "selected=\"selected\"" : "" %>>Post only Moderation</option>
    </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_encrypt_password_on_browser" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_encrypt_password_on_browser">enable_encrypt_password_on_browser</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_encrypt_password_on_browser = mvnforumConfig.getString("mvnforumconfig.enable_encrypt_password_on_browser", "");%>
    <input type="checkbox" id="enable_encrypt_password_on_browser" name="enable_encrypt_password_on_browser" value="true" class="noborder"
    <% if ("true".equals(enable_encrypt_password_on_browser)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_external_user_database" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_external_user_database">enable_external_user_database</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_external_user_database = mvnforumConfig.getString("mvnforumconfig.enable_external_user_database", "");%>
    <input type="checkbox" id="enable_external_user_database" name="enable_external_user_database" value="true" class="noborder"
    <% if ("true".equals(enable_external_user_database)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_new_members_in_recent_days" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_list_new_members_in_recent_days">enable_list_new_members_in_recent_days</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_list_new_members_in_recent_days = mvnforumConfig.getString("mvnforumconfig.enable_list_new_members_in_recent_days", "");%>
    <input type="checkbox" id="enable_list_new_members_in_recent_days" name="enable_list_new_members_in_recent_days" value="true" class="noborder"
    <% if ("true".equals(enable_list_new_members_in_recent_days)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.days_to_show_recent_members" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="days_to_show_recent_members">days_to_show_recent_members</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="days_to_show_recent_members" name="days_to_show_recent_members" value="<%=mvnforumConfig.getString("mvnforumconfig.days_to_show_recent_members", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_list_users_browsing_forum" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_list_users_browsing_forum">enable_list_users_browsing_forum</label><span class="requiredfield">*</span></td>
    <td>
    <% String enable_list_users_browsing_forum = mvnforumConfig.getString("mvnforumconfig.enable_list_users_browsing_forum", "");%>
    <input type="checkbox" id="enable_list_users_browsing_forum" name="enable_list_users_browsing_forum" value="true" class="noborder"
    <% if ("true".equals(enable_list_users_browsing_forum)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_email_threatening_content" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_email_threatening_content">enable_email_threatening_content</label> <span class="requiredfield">*</span></td>
    <td>
     <% String enable_email_threatening_content = mvnforumConfig.getString("mvnforumconfig.enable_email_threatening_content", "");%>
    <input type="checkbox" id="enable_email_threatening_content" name="enable_email_threatening_content" value="true" class="noborder"
    <% if ("true".equals(enable_email_threatening_content)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.have_internet" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="have_internet">have_internet</label> <span class="requiredfield">*</span></td>
    <td>
     <% String have_internet = mvnforumConfig.getString("mvnforumconfig.have_internet", "");%>
    <input type="checkbox" id="have_internet" name="have_internet" value="true" class="noborder"
    <% if ("true".equals(have_internet)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.event_log_locale" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="event_log_locale">event_log_locale</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="event_log_locale" name="event_log_locale" value="<%=mvnforumConfig.getString("mvnforumconfig.event_log_locale", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_category_id" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_category_id">default_category_id</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="default_category_id" name="default_category_id" value="<%=mvnforumConfig.getString("mvnforumconfig.default_category_id", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_watch_type" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_watch_type">default_watch_type</label> <span class="requiredfield">*</span></td>
    <td>
      <select id="default_watch_type" name="default_watch_type">
        <% String default_watch_type = mvnforumConfig.getString("mvnforumconfig.default_watch_type", "");%>
        <option value="<%=WatchBean.WATCH_TYPE_DIGEST%>" <%= (String.valueOf(WatchBean.WATCH_TYPE_DIGEST).equals(default_watch_type)) ? "selected=\"selected\"" : "" %>>Default (Digest)</option>
        <option value="<%=WatchBean.WATCH_TYPE_NONDIGEST%>" <%= (String.valueOf(WatchBean.WATCH_TYPE_NONDIGEST).equals(default_watch_type)) ? "selected=\"selected\"" : "" %>>Non Digest</option></select>  
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.default_status_of_registered_member" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="default_status_of_registered_member">default_status_of_registered_member</label> <span class="requiredfield">*</span></td>
    <td>
      <select id="default_status_of_registered_member" name="default_status_of_registered_member">
        <% String default_status_of_registered_member = mvnforumConfig.getString("mvnforumconfig.default_status_of_registered_member", "");%>
        <option value="<%=MemberBean.MEMBER_STATUS_ENABLE%>" <%= (String.valueOf(MemberBean.MEMBER_STATUS_ENABLE).equals(default_status_of_registered_member)) ? "selected=\"selected\"" : "" %>>Default (Enable Status)</option>
        <option value="<%=MemberBean.MEMBER_STATUS_PENDING%>" <%= (String.valueOf(MemberBean.MEMBER_STATUS_PENDING).equals(default_status_of_registered_member)) ? "selected=\"selected\"" : "" %>>Pending Status</option></select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_register_rule" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_register_rule">enable_register_rule</label> <span class="requiredfield">*</span></td>
    <td>
     <% String enable_register_rule = mvnforumConfig.getString("mvnforumconfig.enable_register_rule", "");%>
    <input type="checkbox" id="enable_register_rule" name="enable_register_rule" value="true" class="noborder"
    <% if ("true".equals(enable_register_rule)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_image_thumbnail"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>" id="id_image_thumbnail">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.enable_image_thumbnail" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="enable_image_thumbnail">enable_image_thumbnail</label> <span class="requiredfield">*</span></td>
    <td>
    <% String enable_image_thumbnail = mvnforumConfig.getString("mvnforumconfig.image_thumbnail.enable", "");
       boolean enableImageThumbnail = "true".equals(enable_image_thumbnail);
    %>
    <input type="checkbox" id="enable_image_thumbnail" name="enable_image_thumbnail" value="true" class="noborder" onchange="javascript:OnChangeImageThumbnailStatus();"
    <% if (enableImageThumbnail) { %> checked="checked"<%}%>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_image_thumbnail_width" <% if (enableImageThumbnail == false) { %> style="display: none;"<%}%>>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.image_thumbnail_width" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="image_thumbnail_width">image_thumbnail_width</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="image_thumbnail_width" name="image_thumbnail_width" value="<%=mvnforumConfig.getString("mvnforumconfig.image_thumbnail.width", "")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="id_image_thumbnail_height" <% if (enableImageThumbnail == false) { %> style="display: none;"<%}%>>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.image_thumbnail_height" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="image_thumbnail_height">image_thumbnail_height</label> <span class="requiredfield">*</span></td>
    <td><input type="text" size="70" id="image_thumbnail_height" name="image_thumbnail_height" value="<%=mvnforumConfig.getString("mvnforumconfig.image_thumbnail.height", "")%>" /></td>
  </tr>
</mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step1_factory"/></td>
  </tr>
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.member_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="member_implementation">member_implementation</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="70" id="member_implementation" name="member_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.member_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.onlineuser_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="onlineuser_implementation">onlineuser_implementation</label> <span class="requiredfield">*</span></td>
    <td>
    <input type="text" size="70" id="onlineuser_implementation" name="onlineuser_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.onlineuser_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.authenticator_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="authenticator_implementation">authenticator_implementation</label></td>
    <td>
    <input type="text" size="70" id="authenticator_implementation" name="authenticator_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.authenticator_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.requestprocessor_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="requestprocessor_implementation">requestprocessor_implementation</label> <span class="requiredfield">*</span></td>
    <td>
      <input type="text" size="70" id="requestprocessor_implementation" name="requestprocessor_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.requestprocessor_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.lucene_analyzer_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="lucene_analyzer_implementation">lucene_analyzer_implementation</label> <span class="requiredfield">*</span></td>
    <td>
      <input type="text" size="70" id="lucene_analyzer_implementation" name="lucene_analyzer_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.lucene_analyzer_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvn_auth_service_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mvn_auth_service_implementation">mvn_auth_service_implementation</label> <span class="requiredfield">*</span></td>
    <td>
      <input type="text" size="70" id="mvn_auth_service_implementation" name="mvn_auth_service_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.mvn_auth_service_implementation", "")%>" />
    </td>    
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.mvnforum_service_implementation" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="mvnforum_service_implementation">mvnforum_service_implementation</label> <span class="requiredfield">*</span></td>
    <td>
      <input type="text" size="70" id="mvnforum_service_implementation" name="mvnforum_service_implementation" value="<%=mvnforumConfig.getString("mvnforumfactoryconfig.mvnforum_service_implementation", "")%>" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td> <label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/>:</label><span class="requiredfield">*</span></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="30" onkeypress="checkEnter(event);" /></td>
  </tr>
</mvn:cssrows>  
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save_and_reload"/>" class="portlet-form-button" onclick="javascript:SubmitForm();" />
      <input type="button" value="<fmt:message key="mvnforum.common.action.cancel"/>" class="liteoption" onclick="javascript:gotoPage('<%=urlResolver.encodeURL(request, response, "configindex")%>');" />
    </td>
  </tr>
</table>
</form>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
