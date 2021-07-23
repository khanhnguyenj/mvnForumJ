<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/configstepthree.jsp,v 1.52 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.52 $
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
 - @author: ThanhNTK 
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.configstepthree.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
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
  <fmt:message key="mvnforum.admin.configstepthree.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "configstepthreeprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "configstepthreeprocess")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.config.config_step3"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_firstname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_firstname">require_register_firstname</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_firstname = mvnforumConfig.getString("mvnforumconfig.require_register_firstname", "");%>
    <input type="checkbox" id="require_register_firstname" name="require_register_firstname" value="true" class="noborder"
    <% if ("true".equals(require_register_firstname)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_lastname" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_lastname">require_register_lastname</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_lastname = mvnforumConfig.getString("mvnforumconfig.require_register_lastname", "");%>
    <input type="checkbox" id="require_register_lastname" name="require_register_lastname" value="true" class="noborder"
    <% if ("true".equals(require_register_lastname)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_gender" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_gender">require_register_gender</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_gender = mvnforumConfig.getString("mvnforumconfig.require_register_gender", "");%>
    <input type="checkbox" id="require_register_gender" name="require_register_gender" value="true" class="noborder"
    <% if ("true".equals(require_register_gender)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_birthday" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_birthday">require_register_birthday</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_birthday = mvnforumConfig.getString("mvnforumconfig.require_register_birthday", "");%>
    <input type="checkbox" id="require_register_birthday" name="require_register_birthday" value="true" class="noborder"
    <% if ("true".equals(require_register_birthday)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_address" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_address">require_register_address</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_address = mvnforumConfig.getString("mvnforumconfig.require_register_address", "");%>
    <input type="checkbox" id="require_register_address" name="require_register_address" value="true" class="noborder"
    <% if ("true".equals(require_register_address)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_city" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_city">require_register_city</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_city = mvnforumConfig.getString("mvnforumconfig.require_register_city", "");%>
    <input type="checkbox" id="require_register_city" name="require_register_city" value="true" class="noborder"
    <% if ("true".equals(require_register_city)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_state" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_state">require_register_state</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_state = mvnforumConfig.getString("mvnforumconfig.require_register_state", "");%>
    <input type="checkbox" id="require_register_state" name="require_register_state" value="true" class="noborder"
    <% if ("true".equals(require_register_state)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_country" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_country">require_register_country</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_country = mvnforumConfig.getString("mvnforumconfig.require_register_country", "");%>
    <input type="checkbox" id="require_register_country" name="require_register_country" value="true" class="noborder"
    <% if ("true".equals(require_register_country)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_phone" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_phone">require_register_phone</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_phone = mvnforumConfig.getString("mvnforumconfig.require_register_phone", "");%>
    <input type="checkbox" id="require_register_phone" name="require_register_phone" value="true" class="noborder"
    <% if ("true".equals(require_register_phone)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_mobile" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_mobile">require_register_mobile</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_mobile = mvnforumConfig.getString("mvnforumconfig.require_register_mobile", "");%>
    <input type="checkbox" id="require_register_mobile" name="require_register_mobile" value="true" class="noborder"
    <% if ("true".equals(require_register_mobile)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_fax" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_fax">require_register_fax</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_fax = mvnforumConfig.getString("mvnforumconfig.require_register_fax", "");%>
    <input type="checkbox" id="require_register_fax" name="require_register_fax" value="true" class="noborder"
    <% if ("true".equals(require_register_fax)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_career" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_career">require_register_career</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_career = mvnforumConfig.getString("mvnforumconfig.require_register_career", "");%>
    <input type="checkbox" id="require_register_career" name="require_register_career" value="true" class="noborder"
    <% if ("true".equals(require_register_career)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_homepage" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_homepage">require_register_homepage</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_homepage = mvnforumConfig.getString("mvnforumconfig.require_register_homepage", "");%>
    <input type="checkbox" id="require_register_homepage" name="require_register_homepage" value="true" class="noborder"
    <% if ("true".equals(require_register_homepage)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_yahoo" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_yahoo">require_register_yahoo</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_yahoo = mvnforumConfig.getString("mvnforumconfig.require_register_yahoo", "");%>
    <input type="checkbox" id="require_register_yahoo" name="require_register_yahoo" value="true" class="noborder"
    <% if ("true".equals(require_register_yahoo)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_aol" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_aol">require_register_aol</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_aol = mvnforumConfig.getString("mvnforumconfig.require_register_aol", "");%>
    <input type="checkbox" id="require_register_aol" name="require_register_aol" value="true" class="noborder"
    <% if ("true".equals(require_register_aol)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_icq" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_icq">require_register_icq</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_icq = mvnforumConfig.getString("mvnforumconfig.require_register_icq", "");%>
    <input type="checkbox" id="require_register_icq" name="require_register_icq" value="true" class="noborder"
    <% if ("true".equals(require_register_icq)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_msn" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_msn">require_register_msn</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_msn = mvnforumConfig.getString("mvnforumconfig.require_register_msn", "");%>
    <input type="checkbox" id="require_register_msn" name="require_register_msn" value="true" class="noborder"
    <% if ("true".equals(require_register_msn)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_link_1" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_link_1">require_register_link_1</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_link_1 = mvnforumConfig.getString("mvnforumconfig.require_register_link_1", "");%>
    <input type="checkbox" id="require_register_link_1" name="require_register_link_1" value="true" class="noborder"
    <% if ("true".equals(require_register_link_1)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" onmouseover="return overlib('<fmt:message key="mvnforum.help.require_register_link_2" bundle="${guide}" />')" onmouseout="return nd();" alt=""/><label for="require_register_link_2">require_register_link_2</label> <span class="requiredfield">*</span></td>
    <td>
    <% String require_register_link_2 = mvnforumConfig.getString("mvnforumconfig.require_register_link_2", "");%>
    <input type="checkbox" id="require_register_link_2" name="require_register_link_2" value="true" class="noborder"
    <% if ("true".equals(require_register_link_2)) { %>
        checked="checked"
    <% } %>
    />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/>:</label> <span class="requiredfield">*</span></td>
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
