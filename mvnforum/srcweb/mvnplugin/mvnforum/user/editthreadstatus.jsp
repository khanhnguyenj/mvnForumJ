<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/editthreadstatus.jsp,v 1.59 2009/08/13 07:24:43 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.59 $
 - $Date: 2009/08/13 07:24:43 $
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

<%@ page import="com.mvnforum.LocaleMessageUtil" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.editthreadstatus.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<%@ include file="header.jsp"%>
<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {
  var agt=navigator.userAgent.toLowerCase();
  
  // Maybe, Opera make an onClick event when user press enter key 
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
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) return false;
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
<br/>

<%
int numberOfPosts = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
int numberOfPendingPosts = ((Integer)request.getAttribute("NumberOfPendingPosts")).intValue();
ThreadBean threadBean = (ThreadBean)request.getAttribute("ThreadBean");

ForumCache forumCache = ForumCache.getInstance();
int forumID = threadBean.getForumID();
String forumName = forumCache.getBean(forumID).getForumName();
%>

<div class="nav center">
  <%= request.getAttribute("tree")%>
</div>
<br/>

<div class="pagedesc info">
  <span class="warning">
  <fmt:message key="mvnforum.user.editthreadstatus.guide"/><br/><br/>
  </span>
  <fmt:message key="mvnforum.common.thread.status"/>:<br/>
  <fmt:message key="mvnforum.user.editthreadstatus.help_disabled"/><br/>
  <fmt:message key="mvnforum.user.editthreadstatus.help_active"/><br/>
  <fmt:message key="mvnforum.user.editthreadstatus.help_locked"/><br/>
  <fmt:message key="mvnforum.user.editthreadstatus.help_closed"/>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "editthreadstatusprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "editthreadstatusprocess")%>
<mvn:securitytoken />
<input type="hidden" name="thread" value="<%=threadBean.getThreadID()%>" />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.editthreadstatus.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.common.thread.topic"/></td>
    <td class="messageTextBold"><%=threadBean.getThreadTopic()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.thread.body"/></td>
    <td><%=MyUtil.filter(threadBean.getThreadBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.numberof.posts"/></td>
    <td class="messageTextBold"><%=numberOfPosts%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.numberof.pending_posts"/></td>
    <td class="messageTextBold"><%=numberOfPendingPosts%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.forum"/></td>
    <td class="messageTextBold"><%=forumName%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.user.editthreadstatus.current_thread_status"/></td>
    <td class="messageTextBold"><%=LocaleMessageUtil.getThreadStatusDescFromInt(onlineUser.getLocale(), threadBean.getThreadStatus())%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="ThreadStatus"><fmt:message key="mvnforum.common.thread.status"/></label></td>
    <td>
      <select id="ThreadStatus" name="ThreadStatus" size="1">
      <%for (int i = 0; i < 4; i++) {
          if (threadBean.getThreadStatus() != i) {%>
        <option value="<%=i%>"><%=LocaleMessageUtil.getThreadStatusDescFromInt(onlineUser.getLocale(), i)%></option>
        <%}
        }%>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
    <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.editthreadstatus.button.edit_thread_status"/>" onclick="javascript:SubmitForm()" class="portlet-form-button" />
    <input type="reset" value="<fmt:message key="mvnforum.common.action.go_back"/>" onclick="javascript:history.back(1)" class="liteoption" />
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
