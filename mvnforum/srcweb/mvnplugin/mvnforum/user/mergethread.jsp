<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/mergethread.jsp,v 1.12 2009/11/18 08:03:40 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.12 $
 - $Date: 2009/11/18 08:03:40 $
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
<%@page import="com.mvnforum.threadtree.ThreadTreeBuilder"%>
<%@page import="com.mvnforum.service.ThreadBuilderService"%>
<%@page import="com.mvnforum.threadtree.ThreadTree"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.mergethread.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<%@ include file="header.jsp"%>
<br/>
<%
    CategoryCache     categoryCache     = CategoryCache.getInstance();
    ForumCache         forumCache         = ForumCache.getInstance();
    Collection         categoryBeans     = categoryCache.getBeans();
    Collection         forumBeans         = forumCache.getBeans();
    
    ThreadBean         threadBean         = (ThreadBean)request.getAttribute("ThreadBean");
    int             numberOfPosts     = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
    
    int             forumID         = threadBean.getForumID();
    String             forumName         = forumCache.getBean(forumID).getForumName();
    
    String            filterCriteria    = (String)request.getAttribute("filterCriteria");
    if (filterCriteria==null || filterCriteria.equalsIgnoreCase("null")) filterCriteria = "";
%>
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
  if (isBlank(document.submitform.destthread, "<fmt:message key="mvnforum.user.mergethread.destinationforum"/>")) return false;
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) return false;
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}

//helper function to create the form
function getNewSubmitForm(){
    var     filterForm     = document.createElement("FORM");
     document.body.appendChild(filterForm);
    filterForm.method     = "POST";
    return filterForm;
}

//helper function to add elements to the form
function createNewFormElementOld(inputForm, elementName, elementValue){
    var newElement         = document.createElement("<input name=\""+elementName+"\" type=\"hidden\">");
    newElement.setValue(elementValue);
    inputForm.appendChild(newElement);
    return newElement;
}

function createNewFormElement(inputForm, elementName, elementValue){
    var newElement         = document.createElement("INPUT");
    newElement.setAttribute("type", "hidden");
    newElement.setAttribute("name", "filterCriteria");
    newElement.setAttribute("value", elementValue);
    inputForm.appendChild(newElement);
    return newElement;
}

//function that creates the form, adds some elements
//and then submits it
function createFormAndSubmit(){
    var filterForm = getNewSubmitForm();
    createNewFormElement(filterForm, "filterCriteria", document.submitform.filterTextValue.value);
    filterForm.action= window.location;
    filterForm.submit();
}

//]]>
</script>

<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>

<div class="pagedesc warning">
  <fmt:message key="mvnforum.user.mergethread.guide"/>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "mergethreadprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "mergethreadprocess")%>
<mvn:securitytoken />
<input type="hidden" name="thread" value="<%=threadBean.getThreadID()%>" />
<input type="hidden" name="md5pw" value="" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.mergethread.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="35%"><fmt:message key="mvnforum.common.thread.topic"/></td>
    <td><%=threadBean.getThreadTopic()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.thread.body"/></td>
    <td><%=MyUtil.filter(threadBean.getThreadBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.post_count"/></td>
    <td class="messageTextBoldBlue"><%=numberOfPosts%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="filterTextValue"><fmt:message key="mvnforum.user.mergethread.filter"/></label></td>
    <td class="messageTextBoldBlue">
      <input id="filterTextValue" name="filterTextValue" type="text" value="<%=filterCriteria%>" />
      &nbsp;
      <a href="#" onclick="createFormAndSubmit();return false;"><fmt:message key="mvnforum.user.mergethread.filter_href"/></a> </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="destthread"><fmt:message key="mvnforum.user.mergethread.destinationforum"/></label></td>
    <td>
    <%= request.getAttribute("Result") %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span> </label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
    <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.mergethread.button.merge_thread"/>" onclick="javascript:SubmitForm()" class="portlet-form-button" />
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
