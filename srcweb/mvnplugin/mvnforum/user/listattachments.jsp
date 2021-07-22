<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listattachments.jsp,v 1.44 2009/08/31 10:29:35 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.44 $
 - $Date: 2009/08/31 10:29:35 $
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
 - @author: Dung Bui
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
int totalAttachments = ((Integer)request.getAttribute("TotalAttachments")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();
Collection attachBeans = (Collection)request.getAttribute("AttachmentBeans");
%>

<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listattachments.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
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
  if (isBlank(document.submitform.key, "<fmt:message key="mvnforum.user.searchattachments.by_description"/>")) return false;
  return true;
}

//]]>
</script>

<%@ include file="header.jsp"%>

<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.listattachments.title"/>
</div>

<br/>

<form action="<%=urlResolver.encodeURL(request, response, "searchattachments", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "searchattachments")%>
<input type="hidden" name="offset" value="0"/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.searchattachments.title"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="key"><fmt:message key="mvnforum.user.searchattachments.by_description"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" size="60" id="key" name="key" onkeyup="initTyper(this);" /> - 
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "searchattachments")%>"><fmt:message key="mvnforum.common.action.advanced_search"/></a>
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

<pg:pager
  url="listattachments"
  items="<%=totalAttachments%>"
  maxPageItems="<%=memberPostsPerPage%>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.attachments"); %>
<%-- keep track of preference --%>
<pg:param name="forum"/>
<pg:param name="category"/>
<pg:param name="sort"/>
<pg:param name="order"/>
<%  try {
    //offset = ((Integer)request.getAttribute("offset")).intValue();
    offset = new Integer((String)request.getAttribute("offset"));
} catch (Exception e) {
    // do nothing
}
%>
<form name="form" action="<%=urlResolver.encodeURL(request, response, "listattachments", URLResolverService.ACTION_URL)%>" <mvn:method/> >
<%=urlResolver.generateFormAction(request, response, "listattachments")%>

<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");

if (sort.length() == 0) sort = "AttachFilename";
if (order.length() == 0) order = "DESC";
%>
<table width="95%" align="center">
  <tr class="portlet-font">
    <td>
      <label for="forum_category_option"><fmt:message key="mvnforum.common.filter_by.category_or_forum"/> &raquo;</label>
      <%= request.getAttribute("Result") %>
  </td>
  </tr>
  <tr>
    <td nowrap="nowrap" class="portlet-font">
      <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
      <select id="sort" name="sort">
      <option value="AttachFilename" <%if (sort.equals("AttachFilename")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.attachment"/></option>
      <option value="AttachFileSize" <%if (sort.equals("AttachFileSize")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.file.file_size"/></option>
      <option value="AttachDownloadCount" <%if (sort.equals("AttachDownloadCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.attachment.download_count"/></option>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
      </select>

      <input type="submit" name="go" value="<fmt:message key="mvnforum.common.go"/>" class="liteoption"/>
    </td>
  </tr>
</table>
<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</form>

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
  int index = 0;
  for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
    AttachmentBean attachBean = (AttachmentBean)attachIter.next();
    index++;
%>
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
    <%if ( (index == 1) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ATTACHEMNT) > 0) ) {%>
      <br/>
      <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_ATTACHEMNT))%>
    <%}%>
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
  <tr class="<mvn:cssrow/>"><td colspan="8" align="center"><fmt:message key="mvnforum.user.listattachments.table.no_attachment"/></td></tr>
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

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>