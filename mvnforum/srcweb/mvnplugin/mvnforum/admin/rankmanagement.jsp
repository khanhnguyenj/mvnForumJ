<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/rankmanagement.jsp,v 1.87 2009/08/13 09:11:56 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.87 $
 - $Date: 2009/08/13 09:11:56 $
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

<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.rankmanagement.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
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
  if (isBlank(document.submitform.RankMinPosts, "<fmt:message key="mvnforum.admin.rankmanagement.minimum_posts"/>")) return false;
  if (isBlank(document.submitform.RankTitle, "<fmt:message key="mvnforum.admin.rankmanagement.rank_title"/>")) return false;
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
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.rankmanagement.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.rankmanagement.info"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "addrankprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "addrankprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.rankmanagement.add_new_rank"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="RankMinPosts"><fmt:message key="mvnforum.admin.rankmanagement.minimum_posts"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="RankMinPosts" name="RankMinPosts" size="10" maxlength="6" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="RankTitle"><fmt:message key="mvnforum.admin.rankmanagement.rank_title"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="RankTitle" name="RankTitle" onkeyup="initTyper(this);" size="50" /></td>
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.add"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption"/>
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="20%" align="center"><fmt:message key="mvnforum.admin.rankmanagement.minimum_posts"/></td>
    <td><fmt:message key="mvnforum.admin.rankmanagement.rank_title"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.edit"/></td>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  </tr>
<%
java.util.ArrayList rankBeans = RankCache.getInstance().getBeans();
%>
<mvn:cssrows>
<%
for (int i = 0; i < rankBeans.size(); i++) {
    RankBean rankBean = (RankBean)rankBeans.get(i);
%>
  <tr class="<mvn:cssrow/>">
    <td align="center"><%=rankBean.getRankMinPosts()%></td>
    <td><%=EnableMVNCodeFilter.filter(rankBean.getRankTitle())%></td>
    <td align="center">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editrank?rank=" + rankBean.getRankID())%>"><fmt:message key="mvnforum.common.action.edit"/></a>
    </td>
    <td align="center">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deleterankprocess?rank=" + rankBean.getRankID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.common.action.delete"/></a>
    </td>
  </tr>
<%
}//for
%>
<%
if (rankBeans.size() == 0) {
%>
  <tr class="<mvn:cssrow/>"><td colspan="6" align="center">There are no ranks.</td></tr>
<% } %>
</mvn:cssrows>
</table>
<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
