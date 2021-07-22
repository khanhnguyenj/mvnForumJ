<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/sendmail.jsp,v 1.59 2009/10/27 10:51:01 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.59 $
 - $Date: 2009/10/27 10:51:01 $
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

<%@ page import="net.myvietnam.mvncore.util.ParamUtil"%>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.sendmail.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false; document.submitform.previewbutton.disabled=false;">

<%
boolean preview = ParamUtil.getParameterBoolean(request, "preview");
String from = ParamUtil.getAttribute(request, "FromEmail");
String toAdmin = ParamUtil.getAttribute(request, "ToAdmin");
String toMember = ParamUtil.getAttribute(request, "ToMember");
String subject = ParamUtil.getAttribute(request, "Subject");//already filtered in Java code
String message = ParamUtil.getAttribute(request, "Message");//already filtered in Java code
%>

<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
  <mvn:servlet>
    document.submitform.submitbutton.disabled=true;
  </mvn:servlet>
    document.submitform.submit();
  }
}

function SubmitPreviewForm() {
  if (ValidateForm()) {
    document.submitform.preview.value='true';
    document.submitform.action=document.getElementById('previewUrl').value;
    <%=urlResolver.generateFormActionForJavascript(request, response, "sendmail", "submitform")%>
    <mvn:servlet> 
    document.submitform.previewbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (isBlank(document.submitform.Subject, "<fmt:message key="mvnforum.common.email.subject"/>")) {
    return false;
  }
  if (isBlank(document.submitform.Message, "<fmt:message key="mvnforum.common.email.message"/>")) {
    return false;
  }
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index" /></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.sendmail.title"/>
</div>
<br/>

<% if (preview) { %>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.email.preview_send"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap" width="30%"><fmt:message key="mvnforum.common.email.from"/>:</td>
    <td><%=from%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.to"/>:</td>
    <td>
    <% if (toAdmin.length() > 0) { %>
      <fmt:message key="mvnforum.user.sendmail.to_admin"/>
    <% } else { %>
      <%=toMember%>
    <% } %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.subject"/>:</td>
    <td><%=DisableHtmlTagFilter.filter(subject)%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.email.message"/>:</td>
    <td><%=DisableHtmlTagFilter.filter(message)%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>
<% } // if preview %>

<input type="hidden" name="previewUrl" id="previewUrl" value="<%=urlResolver.encodeURL(request, response, "sendmail", URLResolverService.ACTION_URL)%>" />
<form action="<%=urlResolver.encodeURL(request, response, "sendmailprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "sendmailprocess")%> 
<mvn:securitytoken />
<input type="hidden" name="preview" value="" />
<input type="hidden" name="ToAdmin" value="<%=toAdmin%>" />
<input type="hidden" name="ToMember" value="<%=toMember%>" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.sendmail.title"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap" width="30%"><fmt:message key="mvnforum.common.email.from"/>:</td>
    <td><%=from%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.to"/>:</td>
    <td>
    <% if (toAdmin.length() > 0) { %>
      <fmt:message key="mvnforum.user.sendmail.to_admin"/>
    <% } else { %>
      <%=toMember%>
    <% } %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><label for="Subject"><fmt:message key="mvnforum.common.email.subject"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="Subject" name="Subject" value="<%=DisableHtmlTagFilter.filter(subject)%>" size="62" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top" nowrap="nowrap"><label for="Message"><fmt:message key="mvnforum.common.email.message"/><span class="requiredfield"> *</span></label></td>
    <td><textarea cols="60" rows="15" id="Message" name="Message"><%=DisableHtmlTagFilter.filter(message)%></textarea></td>
  </tr>
  <%if (currentLocale.equals("vi")) {/*vietnamese here*/%>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.vietnamese_type"/>:</td>
    <td>
      <input type="radio" name="vnselector" id="TELEX" value="TELEX" onclick="setTypingMode(1);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.telex"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VNI" value="VNI" onclick="setTypingMode(2);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.vni"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VIQR" value="VIQR" onclick="setTypingMode(3);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.VIQR"/><br/>
      <input type="radio" name="vnselector" id="NOVN" value="NOVN" onclick="setTypingMode(0);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.not_use"/>
      <script type="text/javascript">
      //<![CDATA[
      initVNTyperMode();
      //]]>
      </script>
    </td>
  </tr>
  <%}// end if vietnamese%>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="previewbutton" value="<fmt:message key="mvnforum.common.action.preview"/>" onclick="javascript:SubmitPreviewForm();" class="liteoption" /> 
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.send_mail"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" /> 
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
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
