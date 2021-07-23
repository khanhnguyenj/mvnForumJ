<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/searchattachments.jsp,v 1.41 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.41 $
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

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<%
String key = ParamUtil.getParameterFilter(request, "key");
String attachmentName = ParamUtil.getParameterFilter(request, "attachmentname");
// below parameters are checked in the Handler
String forum = ParamUtil.getParameterFilter(request, "forum");
String date = ParamUtil.getParameterFilter(request, "date");
String beforeafter = ParamUtil.getParameterFilter(request, "beforeafter");
String rows = ParamUtil.getParameterFilter(request, "rows");
%>
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.searchattachments.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;" >

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
  if (document.submitform.key.value == '' && document.submitform.attachmentname.value == '') {
    alert("<fmt:message key="mvnforum.user.search.js.prompt.at_least_one_item"/> (<fmt:message key="mvnforum.user.searchattachments.by_description"/>, <fmt:message key="mvnforum.user.searchattachments.by_name"/>)");
    return false;
  }
  return true;
}

function InitParam() {
  var date = document.submitform.date;
  var beforeafter = document.submitform.beforeafter;
  
  <% if (date.length() > 0) {%> 
    var id = document.getElementById("date<%=date%>");
    if (id != null) id.selected = true;
  <% }%>
  <% if (forum.length() > 0) {%>
       document.submitform.forum.value = <%=forum%>;
  <% }%>
  <% if (rows.length() > 0) {%>
       document.submitform.rows.value = <%=rows%>;
  <% }%>
  
  <% if (beforeafter.length() > 0) {%> 
    var id = document.getElementById("beforeafter<%=beforeafter%>");
    if (id != null) id.selected = true;
  <% }%>  
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listattachments")%>"><fmt:message key="mvnforum.user.listattachments.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.searchattachments.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "searchattachments", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "searchattachments")%>
<input type="hidden" name="offset" value="0"/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.search.prompt"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="key"><fmt:message key="mvnforum.user.searchattachments.by_description"/></label></td>
    <td><input type="text" size="60" id="key" name="key" value="<%=key%>" onkeyup="initTyper(this);" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="attachmentname"><fmt:message key="mvnforum.user.searchattachments.by_name"/></label></td>
    <td><input type="text" size="60" id="attachmentname" name="attachmentname" value="<%=attachmentName%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="forum"><fmt:message key="mvnforum.user.search.in_category_forum"/></label></td>
    <td>
<%--
      <select name="forum">
        <option value="0" selected="selected"><fmt:message key="mvnforum.user.search.all_forums"/></option>
<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryID, permission) == false) continue; %>
        <option value=""></option>
        <option value="-<%=categoryID%>"><%=categoryBean.getCategoryName()%></option>
        <option value="">---------------------------------</option>
    <%
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) { %>
        <option value="<%=forumBean.getForumID()%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=forumBean.getForumName()%></option>
<%      } //if
    } // for forum
}// for category %>
      </select>
--%>
<%=request.getAttribute("Result")%>      
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="date"><fmt:message key="mvnforum.user.search.by_date"/></label></td>
    <td>
      <select id="date" name="date">
        <option id="date0" value="0"><fmt:message key="mvnforum.common.search.date.any_date"/></option>
        <option id="date1" value="1"><fmt:message key="mvnforum.common.search.date.yesterday"/></option>
        <option id="date7" value="7"><fmt:message key="mvnforum.common.search.date.a_week"/></option>
        <option id="date14" value="14"><fmt:message key="mvnforum.common.search.date.2_weeks"/></option>
        <option id="date30" value="30"><fmt:message key="mvnforum.common.search.date.a_month"/></option>
        <option id="date90" value="90"><fmt:message key="mvnforum.common.search.date.3_months"/></option>
        <option id="date180" value="180"><fmt:message key="mvnforum.common.search.date.6_months"/></option>
        <option id="date365" value="365"><fmt:message key="mvnforum.common.search.date.a_year"/></option>
      </select>
      <select name="beforeafter">
        <option id="beforeafter1" value="1"><fmt:message key="mvnforum.common.search.date.newer"/></option>
        <option id="beforeafter2" value="2"><fmt:message key="mvnforum.common.search.date.older"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="rows"><fmt:message key="mvnforum.user.search.result_per_page"/></label></td>
    <td>
      <select id="rows" name="rows" size="1">
        <option value="10" >10</option>
        <option value="20" >20</option>
        <option value="30" >30</option>
        <option value="50" >50</option>
        <option value="100" >100</option>
      </select>
    </td>
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.search"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption"/>
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>
<br/>

<%
Collection attachBeans = (Collection) request.getAttribute("AttachBeans");
if (attachBeans != null) {
    int resultPerPage = ((Integer)request.getAttribute("rows")).intValue();
    int totalAttachs = ((Integer)request.getAttribute("TotalAttachs")).intValue();
%>
<pg:pager
  url="searchattachments"
  items="<%= totalAttachs %>"
  maxPageItems="<%= resultPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.attachments"); %>
<%-- keep track of preference --%>
<pg:param name="key"/>
<pg:param name="attachmentname"/>
<pg:param name="forum"/>
<pg:param name="date"/>
<pg:param name="beforeafter"/>
<pg:param name="rows"/>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>

<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td align="center"><fmt:message key="mvnforum.common.attachment.image_preview"/></td>
    <td align="center"><fmt:message key="mvnforum.common.attachment"/></td>
    <td align="center"><fmt:message key="mvnforum.common.post"/></td>
    <td align="center"><fmt:message key="mvnforum.common.file.file_size"/></td>
    <td align="center"><fmt:message key="mvnforum.common.attachment.download_count"/></td>
    <td align="center"><fmt:message key="mvnforum.common.attachment.desc"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
String enablethumbnail = "";
if (MVNForumConfig.getEnableThumbnail()) {
  enablethumbnail = "thumbnail=yes&amp;";
}
%>
<%for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
    AttachmentBean attachBean = (AttachmentBean)attachIter.next(); %>
  <pg:item>
  <tr class="<mvn:cssrow/>" valign="middle">
    <td align="center">
      <%if (attachBean.getAttachMimeType().startsWith("image/") && (permission.canGetAttachment(attachBean.getForumID()) || MVNForumConfig.getEnableGuestViewImageAttachment()) ) {%>
        <img height="50" src="<%=urlResolver.encodeURL(request, response, "getattachment?" + enablethumbnail + "attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>" alt="<%=attachBean.getAttachFilename()%>" title="<%=attachBean.getAttachFilename()%>" border="0" />
      <%}%>
    </td>
    <td>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>" />
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>"><%=attachBean.getAttachFilename()%></a>
    </td>
    <td align="center">
      <%--
      <a class="messageTopic" href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + postBean.getThreadID() + "#" + postBean.getPostID())%>">
      <%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
      --%>
    </td>
    <td align="center"><%=attachBean.getAttachFileSize()%> bytes</td>
    <td align="center"><%=attachBean.getAttachDownloadCount()%></td>
    <td align="center">
      <%if (attachBean.getAttachDesc().length() > 0) {%>
        <%=MyUtil.filter(attachBean.getAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>
      <%}%>
    </td>
    <td align="center">
      <%if (permission.canEditPost(attachBean.getForumID()) ) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "editattachment?attach=" + attachBean.getAttachID() + "&amp;offset=" + offset)%>"><fmt:message key="mvnforum.common.action.edit"/></a>&nbsp;
      <%}%>
    </td>
    <td align="center">
      <%if (permission.canDeletePost(attachBean.getForumID()) ) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "deleteattachment?attach=" + attachBean.getAttachID() + "&amp;offset=" + offset)%>"><fmt:message key="mvnforum.common.action.delete"/></a>&nbsp;
      <%}%>
    </td>
  </tr>
  </pg:item> 
<%}//end for
  if (attachBeans.size() == 0) {%>
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.searchattachments.no_attachment"/></td></tr>
<%}%>
</mvn:cssrows>
</table>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>
<%} //if attachBeans != null %>
  
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
