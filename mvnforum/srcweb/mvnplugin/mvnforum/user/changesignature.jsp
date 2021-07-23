<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/changesignature.jsp,v 1.80 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.80 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.changesignature.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false; document.submitform.previewbutton.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function PreviewForm() {
  document.submitform.preview.value='true';
  document.submitform.action=document.getElementById('previewUrl').value;
  <%=urlResolver.generateFormActionForJavascript(request, response, "changesignature", "submitform")%>
  <mvn:servlet>
  document.submitform.submitbutton.disabled=true;
  document.submitform.previewbutton.disabled=true;
  </mvn:servlet>
  document.submitform.submit();
}

function SubmitForm() {
  <mvn:servlet>
  document.submitform.submitbutton.disabled=true;
  document.submitform.previewbutton.disabled=true;
  </mvn:servlet>
  document.submitform.submit();
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.changesignature.title"/>
</div>
<br/>

<%
boolean isPreviewing = ParamUtil.getParameterBoolean(request, "preview");
String signature = ParamUtil.getAttribute(request, "MemberSignature");
if (isPreviewing) { %>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.action.preview"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><%=MyUtil.filter(signature, false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></td>
  </tr>
</mvn:cssrows>
</table>
<br/>
<% } %>

<input type="hidden" name="previewUrl" id="previewUrl" value="<%=urlResolver.encodeURL(request, response, "changesignature", URLResolverService.ACTION_URL) %>" />
<form action="<%=urlResolver.encodeURL(request, response, "changesignatureprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "changesignatureprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.changesignature.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><label for="MemberSignature"><fmt:message key="mvnforum.user.changesignature.signature"/></label><br/>
      <a href="javascript: __Open('<%=urlResolver.encodeURL(request, response, "bbcode")%>', 650, 250)" class="linkinfo"><fmt:message key="mvnforum.common.bbcode.support_bbcode"/></a>
    </td>
    <td><textarea cols="60" rows="15" id="MemberSignature" name="MemberSignature" onkeyup="initTyper(this);"><%=signature%></textarea></td>
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
  <tr>
    <td class="portlet-section-footer" colspan="2" align="center">
      <input type="hidden" name="preview" value=""/>
      <input type="button" name="previewbutton" value="<fmt:message key="mvnforum.common.action.preview"/>" onclick="javascript:PreviewForm();" class="liteoption"/>
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.changesignature.button.change_signature"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption"/>
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
