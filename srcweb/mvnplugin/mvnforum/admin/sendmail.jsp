<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/sendmail.jsp,v 1.104 2009/08/13 09:11:56 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.104 $
 - $Date: 2009/08/13 09:11:56 $
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
<%@ page import="java.util.*" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.sendmail.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;document.submitform.previewbutton.disabled=false;">
<% 
String from = "";
String to = "";
String cc = "";
String bcc = "";
int mailToSelector = -1;
String group = "";
String subject = "";
String message = "";
boolean preview = ParamUtil.getParameterBoolean(request, "preview");
if (preview) {
    from = ParamUtil.getParameterEmail(request, "From");
    to = ParamUtil.getParameterFilter(request, "To");
    cc = ParamUtil.getParameterFilter(request, "Cc");
    bcc = ParamUtil.getParameterFilter(request, "Bcc");
    mailToSelector = ParamUtil.getParameterInt(request, "MailToSelector");

    group = ParamUtil.getParameterFilter(request, "group");
    subject = ParamUtil.getParameterFilter(request, "Subject");
    message = ParamUtil.getParameterFilter(request, "Message");
}
%>
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.submitform.previewbutton.disabled=true;
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
    document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}
function ValidateForm() {
  if (isBlank(document.submitform.From, "<fmt:message key="mvnforum.common.email.from"/>")) return false;
  if (!isEmail(document.submitform.From, "<fmt:message key="mvnforum.common.email.from"/>")) return false;
  if (document.getElementById("MailToSelector@0").checked == true) {
    if (isBlank(document.submitform.To, "<fmt:message key="mvnforum.common.email.to"/>")) return false;
    // Note that we don't check email for To because To can be a string of many emails
  }
  
  if (isBlank(document.submitform.Subject, "<fmt:message key="mvnforum.common.email.subject"/>")) return false;
  if (isBlank(document.submitform.Message, "<fmt:message key="mvnforum.common.email.message"/>")) return false;
  return true;
}
function InsertDefaultEmail() {
  document.submitform.From.value='<%=MVNForumConfig.getWebMasterEmail()%>';
}
function InitParam() {
<% if (preview) {%>
  <% if (mailToSelector >= 0) {%> 
      var id = document.getElementById("MailToSelector@<%=mailToSelector%>");
      if (id != null) id.checked = true;
  <% } %>
  <% if (group.length() > 0) {%> 
      var id = document.getElementById("group@<%=group%>");
      if (id != null) id.selected = true;
 <%  }
   } %>
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.sendmail.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.sendmail.info"/> <br />
  <fmt:message key="mvnforum.admin.sendmail.see_help_at"/> <a href="http://www.freemarker.org">FreeMarker</a>
</div>
<br/>

<% 
if (preview) { 
    String previewTo = ParamUtil.getAttribute(request, "PreviewTo");
    String previewCc = ParamUtil.getAttribute(request, "PreviewCc");
    String previewBcc = ParamUtil.getAttribute(request, "PreviewBcc");

    String previewSubject = ParamUtil.getAttribute(request, "PreviewSubject");
    String previewMessage = ParamUtil.getAttribute(request, "PreviewMessage");
    String previewEmail = ParamUtil.getAttribute(request, "PreviewEmail");
    String warningMessage = ParamUtil.getAttribute(request, "WarningMessage");
%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <%if (previewEmail.indexOf("@") > -1 ) {%>
      <td colspan="2"><fmt:message key="mvnforum.admin.sendmail.preview_to_email"/> <%=previewEmail%></td>
    <%} else {%>
      <td colspan="2"><fmt:message key="mvnforum.admin.sendmail.preview_without_template"/></td>
    <%}%>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.from"/><span class="requiredfield"> *</span></td>
    <td class="portlet-font"><%=from%></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="To">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.to"/><span class="requiredfield"> *</span></td>
    <td class="portlet-font">
    <%=previewTo%>
    <%if (mailToSelector > 0) { %>
      <span class="warning">( <%=warningMessage%> )</span>
    <% } %>  
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right"><fmt:message key="mvnforum.common.email.cc"/>:</td>
    <td class="portlet-font"><%=previewCc%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right"><fmt:message key="mvnforum.common.email.bcc"/>:</td>
    <td class="portlet-font"><%=previewBcc%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><fmt:message key="mvnforum.common.email.subject"/><span class="requiredfield"> *</span></td>
    <td class="portlet-font"><%=previewSubject%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.email.message"/><span class="requiredfield"> *</span></td>
    <td class="portlet-font"><%=previewMessage%></td>
  </tr>
</table>
</mvn:cssrows>
<br/>     
<%}//if preview%>  

<input type="hidden" name="previewUrl" id="previewUrl" value="<%=urlResolver.encodeURL(request, response, "sendmail", URLResolverService.ACTION_URL)%>" />
<form action="<%=urlResolver.encodeURL(request, response, "sendmailprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "sendmailprocess")%>
<mvn:securitytoken />
<input type="hidden" name="preview" value="" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.sendmail.title"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><label for="From"><fmt:message key="mvnforum.common.email.from"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" id="From" name="From" value="<%=from%>" size="60" /> 
      <input type="button" value="<fmt:message key="mvnforum.common.action.insert"/> <%=MVNForumConfig.getWebMasterEmail()%>" class="liteoption" onclick="javascript:InsertDefaultEmail();" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="To">
    <td align="right" valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.email.to"/><span class="requiredfield"> *</span></td>
    
    <td>
    <%if (memberDAO.isSupportGetEnableMembers_inActivationStatus ()) { %>
      <input type="radio" name="MailToSelector" value="1" id="MailToSelector@1" class="noborder" /> <fmt:message key="mvnforum.admin.sendmail.all_members_in_group"/>
      <select name="group">
      <%
        Collection groupBeans = (Collection)request.getAttribute("GroupBeans");
        for (Iterator groupIter = groupBeans.iterator(); groupIter.hasNext(); ) {
          GroupsBean groupBean = (GroupsBean)groupIter.next();
          int groupID = groupBean.getGroupID();
          String groupName = groupBean.getGroupName();
      %>
          <option id="group@<%=groupID%>" value="<%=groupID%>"><%=groupName%></option>
      <%}%> 
      </select>
      <br/>
      <input type="radio" name="MailToSelector" value="2" id="MailToSelector@2" class="noborder" /> <fmt:message key="mvnforum.admin.sendmail.all_activates_members"/> <br/>
      <input type="radio" name="MailToSelector" value="3" id="MailToSelector@3" class="noborder" /> <fmt:message key="mvnforum.admin.sendmail.all_non_activates_members"/> <br/>
    <%} %>
      <input type="radio" name="MailToSelector" value="0" id="MailToSelector@0" checked="checked" class="noborder" /> <fmt:message key="mvnforum.admin.sendmail.specific_email_addresses"/> <br/>
      <input type="text" name="To" value="<%=to%>" size="60" />
    </td>

  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><label for="Cc"><fmt:message key="mvnforum.common.email.cc"/>:</label></td>
    <td><input type="text" id="Cc" name="Cc" value="<%=cc%>" size="60" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><label for="Bcc"><fmt:message key="mvnforum.common.email.bcc"/>:</label></td>
    <td><input type="text" id="Bcc" name="Bcc" size="60" value="<%=bcc%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" nowrap="nowrap"><label for="Subject"><fmt:message key="mvnforum.common.email.subject"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="Subject" name="Subject" value="<%=subject%>" size="60" onkeyup="initTyper(this);"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top" nowrap="nowrap"><label for="Message"><fmt:message key="mvnforum.common.email.message"/><span class="requiredfield"> *</span></label></td>
    <td><textarea cols="60" rows="15" id="Message" name="Message" onkeyup="initTyper(this);"><%=message%></textarea></td>
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
<script type="text/javascript">
//<![CDATA[
    InitParam();
//]]>
</script>
</mvn:body>
</mvn:html>
</fmt:bundle>