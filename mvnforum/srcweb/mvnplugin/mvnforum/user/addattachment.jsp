<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addattachment.jsp,v 1.94 2009/08/13 05:28:02 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.94 $
 - $Date: 2009/08/13 05:28:02 $
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
<%@ page import="net.myvietnam.mvncore.util.StringUtil" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.addattachment.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<%@ include file="header.jsp"%>

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
  if (isBlank(document.submitform.AttachFilename, "<fmt:message key="mvnforum.user.addattachment.file_to_attach"/>")) return false;
  return true;
}

var attNum = 0;
function callAdd(attNum) {
  document.getElementById("SpanAtt" + (attNum + 1)).innerHTML=''; 
  return false;
}

function addAtt() {
  st = " <span id=SpanAtt" + (attNum + 1) + "><input type='file' name='AttachFilename" + (attNum + 1) + "' id ='AttachFilename" + (attNum + 1) + "' size='50'>&nbsp;<a href='#' onclick='return callAdd(" + attNum + ");'><fmt:message key="mvnforum.common.action.remove"/></" + "a></" + "span><div id=aDiv" + (attNum + 1) + "></ " + "div>";
  document.getElementById('aDiv' + attNum).innerHTML = st;
  idoffile = "AttachFilename" + attNum;
  attNum = (attNum + 1);
  document.getElementById(idoffile).focus();
}
//]]>
</script>

<br/>
<%
PostBean postBean = (PostBean)request.getAttribute("PostBean");
ForumCache forumCache = ForumCache.getInstance();

int forumID = postBean.getForumID();
String forumName = forumCache.getBean(forumID).getForumName();
//String postID = String.valueOf(ParamUtil.getParameterInt(request, "post"));
%>

<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "addattachmentprocess", URLResolverService.UPLOAD_URL)%>" method="post" enctype="multipart/form-data" name="submitform">
<%=urlResolver.generateFormAction(request, response, "addattachmentprocess")%>
<mvn:securitytoken />
<input type="hidden" name="offset" value="<%=ParamUtil.getParameterFilter(request, "offset")%>" />
<input type="hidden" name="PostID" value="<%=postBean.getPostID()%>" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.addattachment.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.post.topic"/></td>
    <td><%=postBean.getPostTopic()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.post.body"/></td>
    <td><%=MyUtil.filter(StringUtil.getShorterString(postBean.getPostBody(), MVNForumConfig.getMaxCharsInShortSummary()), false/*html*/, true/*emotion*/, false/*mvnCode*/, true/*newLine*/, false/*URL*/)%></td>
  </tr>
  
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.user.addattachment.file_to_attach"/><span class="requiredfield"> *</span></td>
    <td>
      <div id="file-browsers">
      <input id="file" type="file" name="AttachFilename" size="50" /><br/>
      <%if (permission.canAdminSystem() == false) { %>
        <span class="warning"><b>(<fmt:message key="mvnforum.common.attachment.max_attachment_size"/> = <%= MVNForumConfig.getMaxAttachmentSize()%> bytes)</b></span>
      <%}%>
        <div id="aDiv0"></div>
        <a href="#" onclick="addAtt()"><fmt:message key="mvnforum.common.action.attach_more"/></a>
      </div>
    </td>
  </tr>
  
  <tr class="<mvn:cssrow/>">
    <td valign="top"><label for="AttachDesc"><fmt:message key="mvnforum.user.addattachment.attach_desc"/></label></td>
    <td><textarea cols="60" rows="10" id="AttachDesc" name="AttachDesc" onkeyup="initTyper(this);"></textarea></td>
  </tr>
<%--  
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.option"/></td>
    <td><input type="checkbox" name="AttachMore" value="yes" class="noborder" /> <fmt:message key="mvnforum.user.addattachment.attach_more"/></td>
  </tr>
--%>  
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.addattachment.button.attach_file"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
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
