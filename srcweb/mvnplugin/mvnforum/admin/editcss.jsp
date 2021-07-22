<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/editcss.jsp,v 1.30 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.30 $
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
 - @author: ThanhNTK 
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.editcss.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false; document.submitform.previewbutton.disabled=false;">

<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
    <mvn:servlet>
      document.submitform.previewbutton.disabled=true;
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
}

function PreviewForm() {
    document.submitform.preview.value = 'true';
    <mvn:servlet>
      document.submitform.previewbutton.disabled=true;
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
}
//]]>
</script>

<%@ include file="header.jsp"%>
<%
  String filecss = ParamUtil.getAttribute(request, "CSSBody");
  boolean success = ParamUtil.getAttribute(request, "Success").equals("yes");
  String updatepreview = ParamUtil.getAttribute(request, "csspreview");
  String url = urlResolver.encodeURL(request, response, "index?csspreview=" + updatepreview, URLResolverService.RENDER_URL, "view"); 
%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>"><fmt:message key="mvnforum.admin.misctasks.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.editcss.title"/>
</div>

<br/>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.action.preview"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>
      <iframe id="css_preview_box" name="css_preview_box" src="<%=url%>" style="border: 1px solid black; margin-bottom: 2ex;" frameborder="0" height="300" width="100%">
      </iframe>
    </td>
  </tr>
</table>
</mvn:cssrows>
<br />
<mvn:cssrows>
<form  method="post" name="submitform" action="<%=urlResolver.encodeURL(request, response, "updatecss", URLResolverService.ACTION_URL)%>">
<%=urlResolver.generateFormAction(request, response, "updatecss")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.editcss.css_content"/>:</td>
  </tr>
  
  <% if (success) {%>
  <tr class="portlet-section-subheader">
    <td align="center" class="success"><fmt:message key="mvnforum.admin.editcss.save_css_success"/></td>
  </tr>
  <% } %>
  <tr class="<mvn:cssrow/>">
    <td><textarea style="width:100%" rows="18" cols="10" name="body"><%=filecss%></textarea></td>
  </tr>
  <tr class="portlet-section-footer">
    <td align="center">
      <input type="hidden" name="preview" value="" />
      <input type="button" name="previewbutton" value="<fmt:message key="mvnforum.common.action.preview"/>" onclick="PreviewForm();" class="portlet-form-button" /> 
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save"/>" onclick="SubmitForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption"/>
    </td>
  </tr>
</table>
</form>
</mvn:cssrows>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
