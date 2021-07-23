<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/deletethread.jsp,v 1.87 2009/08/13 07:24:43 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.87 $
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
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.deletethread.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<%@ include file="header.jsp"%>
<br/>
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

<%
int numberOfPosts = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
int numberOfPendingPosts = ((Integer)request.getAttribute("NumberOfPendingPosts")).intValue();
ThreadBean threadBean = (ThreadBean)request.getAttribute("ThreadBean");

ForumCache forumCache = ForumCache.getInstance();
int forumID = threadBean.getForumID();
String forumName = forumCache.getBean(forumID).getForumName();
%>

<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>
<br/>

<div class="pagedesc warning">
  <fmt:message key="mvnforum.user.deletethread.guide"/>
</div>
<br/>

<!-- the onsubmit event prevent from being submitted by pressing enter  -->
<form action="<%=urlResolver.encodeURL(request, response, "deletethreadprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "deletethreadprocess")%>
<mvn:securitytoken />
<input type="hidden" name="thread" value="<%=threadBean.getThreadID()%>" />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.deletethread.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.common.thread.topic"/></td>
    <td><%=threadBean.getThreadTopic()%></td>
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
<%if (request.getAttribute("ThisThreadHasPoll") != null) {%>  
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.poll"/></td>
    <td><input type="checkbox" name="DeletePoll" value="yes" class="noborder" /> <fmt:message key="mvnforum.user.deletethread.delete_poll"/></td>
  </tr>
<%}%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
    <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.deletethread.button.delete_thread"/>" onclick="javascript:SubmitForm()" class="portlet-form-button" />
    <input type="reset" name="reset" value="<fmt:message key="mvnforum.common.action.go_back"/>" onclick="javascript:history.back(1)" class="liteoption" />
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
