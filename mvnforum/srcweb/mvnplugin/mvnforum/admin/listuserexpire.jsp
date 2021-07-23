<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/listuserexpire.jsp,v 1.68 2009/09/15 02:47:08 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.68 $
 - $Date: 2009/09/15 02:47:08 $
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
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="net.myvietnam.mvncore.util.*" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.listuserexpire.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;document.form.go.disabled=false;">

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
  if (document.submitform.member.value == '' && document.submitform.memberid.value == '' && document.submitform.memberemail.value == '') {
    alert("Please enter MemberID or MemberName or MemberEmail to find member.");
    return false;
  }
  if (!isEmail(document.submitform.memberemail, "E-mail")) return false;
  return true;
}
function SubmitFormGo() {
  <mvn:servlet>
    document.form.go.disabled=true;
  </mvn:servlet>  
  document.form.submit();
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
  <fmt:message key="mvnforum.admin.listuserexpire.title"/>
</div>
<br/>

<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");

if (sort.length() == 0) sort = "MemberExpireDate";
if (order.length() == 0) order = "ASC";

int totalMembers = ((Integer)request.getAttribute("TotalMembers")).intValue();
Collection memberBeans = (Collection) request.getAttribute("ExpireMemberBeans");
int memberPostsPerPage = onlineUser.getPostsPerPage();
%>

<pg:pager
  url="listuserexpire"
  items="<%= totalMembers %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = "Members"; %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "viewmember", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "viewmember")%>
<%-- mvn:securitytoken no need check here --%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2">Find Member</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>
    <label for="memberid">Find Member by MemberID:</label> <input type="text" id="memberid" name="memberid" size="5" maxlength="5" />
    &nbsp;&nbsp;&nbsp;<label for="member">or by MemberName:</label> <input type="text" id="member" name="member" />
    &nbsp;&nbsp;&nbsp;<label for="memberemail">or by E-Mail:</label> <input type="text" id="memberemail" name="memberemail" size="30" />
    <input type="button" name="submitbutton" value="Find Member" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>

<table width="95%" align="center">
  <tr class="portlet-font">
    <td nowrap="nowrap">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "listuserexpire")%>">
      <%=urlResolver.generateFormAction(request, response, "listuserexpire")%>
      <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
      <select id="sort" name="sort">
      <option value="MemberID" <%if (sort.equals("MemberID")) {%>selected="selected"<%}%>>Member ID</option>
      <option value="MemberName" <%if (sort.equals("MemberName")) {%>selected="selected"<%}%>>Login Name</option>
      <option value="MemberFirstname" <%if (sort.equals("MemberFirstname")) {%>selected="selected"<%}%>>First Name</option>
      <option value="MemberLastname" <%if (sort.equals("MemberLastname")) {%>selected="selected"<%}%>>Last Name</option>
      <option value="MemberGender" <%if (sort.equals("MemberGender")) {%>selected="selected"<%}%>>Gender</option>
      <option value="MemberBirthday" <%if (sort.equals("MemberBirthday")) {%>selected="selected"<%}%>>Birthday</option>
      <option value="MemberCreationDate" <%if (sort.equals("MemberCreationDate")) {%>selected="selected"<%}%>>Join Date</option>
      <option value="MemberExpireDate" <%if (sort.equals("MemberExpireDate")) {%>selected="selected"<%}%>>Expire Date</option>
      <option value="MemberLastLogon" <%if (sort.equals("MemberLastLogon")) {%>selected="selected"<%}%>>Last Login</option>
      <option value="MemberViewCount" <%if (sort.equals("MemberViewCount")) {%>selected="selected"<%}%>>Views</option>
      <option value="MemberPostCount" <%if (sort.equals("MemberPostCount")) {%>selected="selected"<%}%>>Posts</option>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>>Ascending</option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>>Descending</option>
      </select>
      <input type="button" name="go" value="Go" onclick="SubmitFormGo();" class="liteoption" />
    </form>
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
<br/>
<form action="<%=urlResolver.encodeURL(request, response, "updatememberexpireprocess", URLResolverService.ACTION_URL)%>" method="post">
<%=urlResolver.generateFormAction(request, response, "updatememberexpireprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td align="center" width="15%">Expire Warning</td>
    <td>Name</td>
    <td align="center">Join Date</td>
    <td align="center">Expire Date</td>
    <td align="center"></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td align="center">
 <% //Compare Date
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    Timestamp expiresoonDate = DateUtil.getCurrentGMTTimestampExpiredDay(7 /*MVNForumConfig.getExpireSoonDate()*/ );
    Timestamp memberExpireDate = memberBean.getMemberExpireDate();
    if (memberExpireDate == null) memberExpireDate = now;
    if (now.after(memberExpireDate)) { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/warning_expired.gif" alt="" />    
 <% } else if (expiresoonDate.after(memberExpireDate)) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/warning_expiresoon.gif" alt="" />    
 <% } %>
    </td>
    <td nowrap="nowrap">
      <%if (memberBean.getMemberStatus() != 0) {%>
        <a class="disabledItem"
      <%} else {%>
        <a class="memberName"
      <%}%>
        href="viewmember?memberid=<%=memberBean.getMemberID()%>"><%=memberBean.getMemberName()%></a>
    </td>
    <td align="center" nowrap="nowrap"><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%></td>
    <td align="center" nowrap="nowrap"><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberExpireDate())%></td>    
    <td align="center" width="16">
      <input type="checkbox" name="selectedmember" value="<%=memberBean.getMemberID()%>" class="noborder" />
    </td>
  </tr>
</pg:item>
<%
}//for
%>
<%
if (memberBeans.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="6" align="center">There are no soon-to-expire members.</td></tr>
<% } else { //if %>
  <tr class="portlet-section-footer">
    <td colspan="8" align="right">
      <label for="addtime">Number of dates to renew:</label> <input type="text" id="addtime" name="addtime" value="90" size="5" maxlength="5" />
      <input type="submit" name="renew" value="Renew" class="portlet-form-button" /> or 
      <input type="submit" name="disable" value="Disable" class="portlet-form-button" /> or
      <input type="submit" name="enable" value="Enable" class="portlet-form-button" />
    </td>
  </tr>
<% } %>
</mvn:cssrows>
</table>
</form>

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