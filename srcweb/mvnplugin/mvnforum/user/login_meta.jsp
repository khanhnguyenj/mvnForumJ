<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/login_meta.jsp,v 1.17 2009/04/22 08:30:39 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.17 $
 - $Date: 2009/04/22 08:30:39 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>

<script type="text/javascript">
//<![CDATA[
function checkEnterLogin(event) {
  var agt=navigator.userAgent.toLowerCase();
  
  // Maybe, Opera make an onClick event when user press enter key 
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitFormLogin();
}

function SubmitFormLogin() {
  if (ValidateFormLogin()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.sublogin.MemberMatkhau, document.sublogin.md5pw);
    }    
    <mvn:servlet>
    document.sublogin.submitbutton.disabled=true;
    </mvn:servlet>
    return document.sublogin.submit();
  }
  return false;
}

function ValidateFormLogin() {
  if (isBlank(document.sublogin.MemberName, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;
  if (isBlank(document.sublogin.MemberMatkhau, "<fmt:message key="mvnforum.common.member.password"/>")) return false;
  //Check Password's length
  if (document.sublogin.MemberMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.sublogin.MemberMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
  <table class="noborder">
  <tr>
    <%-- form tag must be between tr and td, otherwise it will break layout in IE --%>
    <form action="<%=urlResolver.encodeURL(request, response, "loginprocess", URLResolverService.ACTION_URL)%>" method="post" name="sublogin" onsubmit="return false;">
    <%=urlResolver.generateFormAction(request, response, "loginprocess")%>
    <mvn:securitytoken />
      <td>
        <input type="hidden" name="FromLoginPage" value="true" />
        <input type="hidden" name="md5pw" value="" />
        <input type="hidden" name="url" value="<%=DisableHtmlTagFilter.filter(ParamUtil.getParameter(request, "url"))%>" />
        <label for="MemberName_header"><fmt:message key="mvnforum.common.member.login_name"/></label>
        <input type="text" id="MemberName_header" name="MemberName" value="<%=ParamUtil.getAttribute(request, "MemberName")%>" size="10" />
        <label for="MemberMatkhau_header"><fmt:message key="mvnforum.common.member.password"/></label>
        <input type="password" id="MemberMatkhau_header" name="MemberMatkhau" onkeypress="checkEnterLogin(event);" size="10" />
        <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.login"/>" onclick="javascript:SubmitFormLogin();" class="portlet-form-button" />
      </td>
    </form>
  </tr>
</table>
</fmt:bundle>