<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/edittemplate.jsp,v 1.77 2009/08/13 09:11:57 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.77 $
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
 - @author: Phong, Ta Quoc  
 --%>
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="com.mvnforum.MVNForumGlobal" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.edittemplate.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

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

function ValidateForm() {
  if (isBlank(document.submitform.subject, "<fmt:message key="mvnforum.admin.edittemplate.subject"/>")) return false;
  if (isBlank(document.submitform.body, "<fmt:message key="mvnforum.admin.edittemplate.body"/>")) return false;
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<%
  String subject = ParamUtil.getAttribute(request, "TemplateSubject");
  String body = ParamUtil.getAttribute(request, "TemplateBody");
  String template = ParamUtil.getParameterFilter(request, "template");
  boolean success = (! ParamUtil.getAttribute(request, "Successful").equals(""));
  request.removeAttribute("Successful");
  if (template.equals("")) {
    template = MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX;
  }
  // note that we already check the template input in the Java Handler
%>
<br/>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>"><fmt:message key="mvnforum.admin.misctasks.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.edittemplate.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.edittemplate.info"/>
</div>
<br/>
<mvn:cssrows>  
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.edittemplate.variables"/></td>
    <td><fmt:message key="mvnforum.admin.edittemplate.desc_and_usage"/></td>
  </tr>
<% if (template.equals(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{memberName}</td>
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{serverName}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.server_name"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{MVNForumInfo}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_information"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{activationUrl}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.activation_url"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{activateCode}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.activation_code"/></td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{memberName}</td>
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{serverName}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.server_name"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{MVNForumInfo}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_information"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{passwordResetUrl}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.reset_url"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{currentTempPassword}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.reset_code"/></td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{postID}</td>
    <td>ID of censored post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{forumID}</td>
    <td>ID of forum that contains censored post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadID}</td>
    <td>ID of thread that contains censored post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{memberID}</td>
    <td>ID of author of censored post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{postURL}</td>
    <td>URL of censored post</td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{lastSent}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.last_watch_mail"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{now}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.now_present"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{forumBase}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_link"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.categoryName}</td>
    <td>Category that contains thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.forumName}</td>
    <td>Forum that contains thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadTopic}</td>
    <td>Topic of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadBody}</td>
    <td>Body of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadUrl}</td>
    <td>URL to this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostTopic}</td>
    <td>Topic of last post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostBody}</td>
    <td>Body of last post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostUrl}</td>
    <td>URL to last post of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostMemberName}</td>
    <td>The last member that post in this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadLastPostDate}</td>
    <td>Date of last post in this thread</td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{lastSent}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.last_watch_mail"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{now}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.now_present"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" valign="top">
    <td>&#36;{forumBase}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_link"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" valign="top">
    <td>&#36;{threadWatchList}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.thread_watch_list"/>
<pre>
&lt;#list threadWatchList as mailbeans >
&lt;#if mailbeans.leader >
[Category: &#36;{mailbeans.categoryName}]
  [Forum: &#36;{mailbeans.forumName}]
&lt;/#if>
    Thread [&#36;{mailbeans.threadTopic}] given to us by &#36;{mailbeans.memberName}
      last posted by &#36;{mailbeans.lastPostMemberName} on &#36;{mailbeans.threadLastPostDate}
      &#36;{mailbeans.threadUrl}

&lt;/#list>
</pre>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" valign="top">
    <td>&#36;{mailbeans.forumName}</td>
    <td>Forum Name</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadTopic}</td>
    <td>Thread Topic</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadBody}</td>
    <td>Thread Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.memberName}</td>
    <td>Thread Owner</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadUrl}</td>
    <td>Thread URL</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostTopic}</td>
    <td>Last Post Topic</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostBody}</td>
    <td>Last Post Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostUrl}</td>
    <td>Last Post URL</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostMemberName}</td>
    <td>The last post member in the thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadLastPostDate}</td>
    <td>The last post date in the thread</td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{lastSent}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.last_watch_mail"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{now}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.now_present"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{forumBase}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_link"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.forumName}</td>
    <td>Forum that contains this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.categoryName}</td>
    <td>Category that contains this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.memberReceiveMail}</td>
    <td>Member who will receive this email</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.confirmedCode}</td>
    <td>This code need to be confirmed when user reply to this email as a post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadID}</td>
    <td>ID of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.memberName}</td>
    <td>Author of this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadTopic}</td>
    <td>Topic of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadBody}</td>
    <td>Body of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadBodyLong}</td>
    <td>Content of post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadBodyShort}</td>
    <td>Summarized content of the post</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadUrl}</td>
    <td>URL of this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostTopic}</td>
    <td>Last Post Topic</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostBody}</td>
    <td>Last Post Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostUrl}</td>
    <td>Last Post URL</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.lastPostMemberName}</td>
    <td>Last member that post in this thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{threadWatch.threadLastPostDate}</td>
    <td>Date of the last post</td>
  </tr>
<%} else if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX)) {%>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{lastSent}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.last_watch_mail"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{now}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.now_present"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{forumBase}</td>
    <td><fmt:message key="mvnforum.admin.edittemplate.forum_link"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" valign="top">
    <td>threadWatchList</td>
    <td>The thread watch list. Iterator over a list of thread watch list.
      <br/>Usage: <#list threadWatchList as mailbeans > ...&lt;/#list>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" valign="top">
    <td>forumName</td>
    <td>Above is the properties of the thread watch list :
      <br/>Usage:
<pre>
&lt;#list threadWatchList as mailbeans >
&lt;#if mailbeans.leader >
[Category: &#36;{mailbeans.categoryName}]
  [Forum: &#36;{mailbeans.forumName}]
&lt;/#if>
    Thread [&#36;{mailbeans.threadTopic}] given to us by &#36;{mailbeans.memberName} <br/>
    ThreadBody &#36;{mailbeans.threadBodyLong}
      last posted by &#36;{mailbeans.lastPostMemberName} on &#36;{mailbeans.threadLastPostDate}
      &#36;{mailbeans.threadUrl}
      Please <a href="mailto:&#36;{mailbeans.mailFrom}?subject=Reply:[mvnForum] - Reply for thread &#36;{mailbeans.threadTopic}<#if mailbeans.haveConfirmedCode>&body=Please input your reply between the ThreadBody tag.%0A[THREADBODY]%0A%0A[/THREADBODY]%0ANOTE: Below is information we need to confirm your reply email. Please DO NOT remove or change it on your reply%0A[THREADID = &#36;{mailbeans.threadID}]%0A[MEMBERNAME = &#36;{mailbeans.memberReceiveMail}]%0A[CONFIRMEDCODE = &#36;{mailbeans.confirmedCode}]<#else>&body=Please input your reply between the ThreadBody tag.%0A[THREADBODY]%0A%0A[/THREADBODY]%0A>[THREADID = &#36;{mailbeans.threadID}]%0A>[MEMBERNAME = &#36;{mailbeans.memberReceiveMail}]</#if>">click here</a> to reply this post

&lt;/#list>
</pre>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadTopic}</td>
    <td>Thread Topic</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadBody}</td>
    <td>Thread Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadBodyLong}</td>
    <td>Content of thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.memberName}</td>
    <td>Thread Owner</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadUrl}</td>
    <td>Thread URL</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostTopic}</td>
    <td>Last Post Topic</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostBody}</td>
    <td>Last Post Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostUrl}</td>
    <td>Last Post URL</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.lastPostMemberName}</td>
    <td>The last post member in the thread</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>&#36;{mailbeans.threadLastPostDate}</td>
    <td>The last post date in the thread</td>
  </tr>
<%}%>
  <tr class="portlet-section-footer">
    <td colspan="2">
      <fmt:message key="mvnforum.admin.edittemplate.see_template_help"/> <a href="http://www.freemarker.org">FreeMarker</a>
    </td>
  </tr>
</table>
</mvn:cssrows>  
<br/>

<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.edittemplate.title"/>:</td>
  </tr>
  
  <% if (success) {%>
  <tr class="portlet-section-subheader">
    <td colspan="2" align="center" class="success"><fmt:message key="mvnforum.admin.edittemplate.success_info"/></td>
  </tr>
  <% } %>
  <tr class="<mvn:cssrow/>">
    <td><label for="template"><fmt:message key="mvnforum.admin.edittemplate.template_for"/><span class="requiredfield"> *</span></label></td>
    <td>
      <form name="changetemplate" action="<%=urlResolver.encodeURL(request, response, "edittemplate", URLResolverService.ACTION_URL)%>" <mvn:method/>>
        <%=urlResolver.generateFormAction(request, response, "edittemplate")%>
        <%-- mvn:securitytoken no need check here --%>
        <select id="template" name="template" onchange="document.changetemplate.submit();">
          <option value="<%=MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.active_member"/></option> 
          <option value="<%=MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.recover_pass"/></option>
          <option value="<%=MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX)) {%> selected <%}%>><fmt:message key="mvnforum.admin.edittemplate.inform_to_admin"/></option>
          <option value="<%=MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.single"/></option>
          <option value="<%=MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.digest"/></option>
       <% if ((environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) && MVNForumConfig.getEnableWatchGateway()) { %>
          <option value="<%=MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.gateway_single"/></option>
          <option value="<%=MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX%>" <%if (template.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX)) {%> selected="selected" <%}%>><fmt:message key="mvnforum.admin.edittemplate.gateway_digest"/></option>          
       <% } %>
        </select>
      </form>
    </td>
  </tr>
<form action="<%=urlResolver.encodeURL(request, response, "updatetemplate", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "updatetemplate")%>
<mvn:securitytoken />
  <tr class="<mvn:cssrow/>">
    <td><label for="subject"><fmt:message key="mvnforum.admin.edittemplate.subject"/><span class="requiredfield"> *</span></label></td>
    <td nowrap="nowrap"><input type="text" id="subject" name="subject" value="<%=subject%>" size="80"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="body"><fmt:message key="mvnforum.admin.edittemplate.body"/><span class="requiredfield"> *</span></label></td>
    <td nowrap="nowrap"><textarea cols="80" rows="18" id="body" name="body" tabindex="2"><%=body%></textarea></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input name="template" type="hidden" value="<%=template%>"/>
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save"/>" onclick="SubmitForm();" class="portlet-form-button"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</form>
</table>
</mvn:cssrows>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
