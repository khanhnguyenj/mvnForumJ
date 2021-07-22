<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/editforum.jsp,v 1.107 2009/08/13 09:11:57 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.107 $
 - $Date: 2009/08/13 09:11:57 $
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
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.editforum.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
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

  if (isBlank(document.submitform.CategoryID, "<fmt:message key="mvnforum.common.category.category_name"/>")) return false;
  if (isBlank(document.submitform.ForumName, "<fmt:message key="mvnforum.common.forum.forum_name"/>")) return false;
  if (!isUnsignedInteger(document.submitform.ForumOrder, "<fmt:message key="mvnforum.common.forum.forum_order"/>")) return false;
  
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

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "forummanagement")%>"><fmt:message key="mvnforum.admin.forummanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.editforum.title"/>
</div>
<br/>

<%
ForumBean forumBean = (ForumBean)request.getAttribute("ForumBean");
Collection categoryBeans = CategoryCache.getInstance().getBeans();
%>

<form action="<%=urlResolver.encodeURL(request, response, "updateforum", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "updateforum")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<input type="hidden" name="ForumID" value="<%=forumBean.getForumID()%>" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.editforum.title"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="CategoryID"><fmt:message key="mvnforum.common.category.category_name"/><span class="requiredfield"> *</span></label></td>
    <td>
<%--
      <select name="CategoryID">
<% for (Iterator iterator = categoryBeans.iterator(); iterator.hasNext(); ) {
    CategoryBean row = (CategoryBean)iterator.next(); %>
        <option value="<%=row.getCategoryID()%>" <%if (row.getCategoryID() == forumBean.getCategoryID()) {%>selected="selected" <%}%>><%=row.getCategoryName()%></option>
<% } //for%>
      </select>
--%>
<%= request.getAttribute("Result") %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumName"><fmt:message key="mvnforum.common.forum.forum_name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="ForumName" name="ForumName" value="<%=forumBean.getForumName()%>" size="62" onkeyup="initTyper(this);" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><label for="ForumDesc"><fmt:message key="mvnforum.common.forum.forum_description"/>:</label><br/>
      <a href="javascript: __Open('<%=urlResolver.encodeURL(request, response, "bbcode")%>', 650, 250)" class="linkinfo"><fmt:message key="mvnforum.common.bbcode.support_bbcode"/></a>
    </td>
    <td><textarea cols="60" rows="6" id="ForumDesc" name="ForumDesc" onkeyup="initTyper(this);"><%=forumBean.getForumDesc()%></textarea></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumOwnerName"><fmt:message key="mvnforum.common.forum.forum_owner_name"/>:</label></td>
    <td><input type="text" id="ForumOwnerName" name="ForumOwnerName" value="<%=forumBean.getForumOwnerName()%>" size="30" /></td>
  </tr>  
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumOrder"><fmt:message key="mvnforum.common.forum.forum_order"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="ForumOrder" name="ForumOrder" value="<%=forumBean.getForumOrder()%>" size="30" maxlength="5" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumStatus"><fmt:message key="mvnforum.common.forum.status"/>:</label></td>
    <td>
      <select id="ForumStatus" name="ForumStatus" style="width: 220px;">
        <option value="0" <%if (forumBean.getForumStatus() == 0) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.status.active"/></option>
        <option value="1" <%if (forumBean.getForumStatus() == 1) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.status.disabled"/></option>
        <option value="2" <%if (forumBean.getForumStatus() == 2) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.status.locked"/></option>
        <option value="3" <%if (forumBean.getForumStatus() == 3) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.status.closed"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumType"><fmt:message key="mvnforum.common.forum.type"/>:</label></td>
    <td>
      <select id="ForumType" name="ForumType" style="width: 220px;">
        <option value="0" <%if (forumBean.getForumType() == 0) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.type.normal"/></option>
        <option value="1" <%if (forumBean.getForumType() == 1) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.type.private"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ForumModerationMode"><fmt:message key="mvnforum.common.forum.moderation_mode"/>:</label></td>
    <td>
      <select id="ForumModerationMode" name="ForumModerationMode" style="width: 220px;">
        <option value="0" <%if (forumBean.getForumModerationMode() == 0) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.moderation_mode.default"/></option>
        <option value="1" <%if (forumBean.getForumModerationMode() == 1) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.moderation_mode.no"/></option>
        <option value="2" <%if (forumBean.getForumModerationMode() == 2) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.moderation_mode.thread_post"/></option>
        <option value="3" <%if (forumBean.getForumModerationMode() == 3) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.moderation_mode.thread"/></option>
        <option value="4" <%if (forumBean.getForumModerationMode() == 4) {%> selected="selected" <%}%>><fmt:message key="mvnforum.common.forum.moderation_mode.post"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="30" onkeypress="checkEnter(event);" /></td>
  </tr>
  
<%if (currentLocale.equals("vi")) {/*vietnamese here*/%>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.vietnamese_type"/>:</td>
    <td>
      <input type="radio" name="vnselector" id="TELEX" value="TELEX" onclick="setTypingMode(1);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.telex"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VNI" value="VNI" onclick="setTypingMode(2);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.vni"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VIQR" value="VIQR" onclick="setTypingMode(3);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.VIQR"/><br/>
      <input type="radio" name="vnselector" id="NOVN" value="NOVN" onclick="setTypingMode(0);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.not_use"/>
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
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