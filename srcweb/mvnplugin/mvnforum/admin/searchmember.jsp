<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/searchmember.jsp,v 1.59 2009/09/15 10:49:05 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.59 $
 - $Date: 2009/09/15 10:49:05 $
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
 - @author: Tran Van Giang   
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.*" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.searchmember.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<%
  String membername = ParamUtil.getParameterFilter(request, "membername");// always disable HTML
  String email = ParamUtil.getParameterFilter(request, "email");// always disable HTML
  String lastname = ParamUtil.getParameterFilter(request, "lastname");// always disable HTML
  String firstname = ParamUtil.getParameterFilter(request, "firstname");// always disable HTML
  String country = ParamUtil.getParameterFilter(request, "country");// always disable HTML
  String date = ParamUtil.getParameterFilter(request, "date");
  String beforeafter = ParamUtil.getParameterFilter(request, "beforeafter");
%>
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
  if ((document.submitform.membername.value == '') &&
      (document.submitform.lastname.value == '') &&
      (document.submitform.firstname.value == '') &&
      (document.submitform.email.value == '') &&
      (document.submitform.country.value == '') ) {
    alert("<fmt:message key="mvnforum.admin.searchmember.javascript_prompt"/>");
    return false;
  }
  return true;
}

function InitParam() {
  var date =        document.submitform.date;
  var beforeafter = document.submitform.beforeafter;
  <% if (date.length() > 0) {%> 
    var id = document.getElementById("date<%=date%>");
    if (id != null) id.selected = true;
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
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.searchmember.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.searchmember.info"/> <a href="http://lucene.apache.org/java/2_4_1/queryparsersyntax.html">http://lucene.apache.org/java/2_4_1/queryparsersyntax.html</a>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "searchmember", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "searchmember")%>
<%-- mvn:securitytoken no need check here --%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td nowrap="nowrap" colspan="2"><fmt:message key="mvnforum.admin.searchmember.search_member_by"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap" width="20%"><label for="membername"><fmt:message key="mvnforum.common.member.login_name"/></label></td>
    <td nowrap="nowrap" width="78%"><input type="text" id="membername" name="membername" value="<%=membername%>" size="30" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap" width="20%"><label for="firstname"><fmt:message key="mvnforum.common.member.first_name"/></label></td>
    <td nowrap="nowrap" width="78%"><input type="text" id="firstname" name="firstname" value="<%=firstname%>" size="30" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap" width="20%"><label for="lastname"><fmt:message key="mvnforum.common.member.last_name"/></label></td>
    <td nowrap="nowrap" width="78%"><input type="text" id="lastname" name="lastname" value="<%=lastname%>" size="30" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap" width="20%"><label for="email"><fmt:message key="mvnforum.common.member.email"/></label></td>
    <td nowrap="nowrap" width="78%"><input type="text" id="email" name="email" value="<%=email%>" size="30" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap" width="20%"><label for="country"><fmt:message key="mvnforum.common.member.country"/></label></td>
    <td nowrap="nowrap" width="78%"><input type="text" id="country" name="country" value="<%=country%>" size="30" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td>
      <select name="date">
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
  <tr class="portlet-section-footer" align="center">
    <td nowrap="nowrap" colspan="2">
    <input type="button" value="<fmt:message key="mvnforum.common.action.search"/>" name="submitbutton" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    <input type="reset" name="B2" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>

<%
Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
if (memberBeans != null) {
    String sort  = ParamUtil.getParameterFilter(request, "sort");
    String order = ParamUtil.getParameterFilter(request, "order");
    if (sort.length() == 0) sort = "MemberName";
    if (order.length() == 0) order = "ASC";
    int totalMembers = ((Integer)request.getAttribute("TotalMembers")).intValue();
    int memberPostsPerPage = onlineUser.getPostsPerPage();
%>
<pg:pager
  url="searchmember"
  items="<%= totalMembers %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.members"); %>
<pg:param name="sort"/>
<pg:param name="order"/>
<pg:param name="rows"/>
<pg:param name="membername"/>
<pg:param name="email"/>
<pg:param name="lastname"/>
<pg:param name="firstname"/>
<pg:param name="country"/>
<pg:param name="date"/>
<pg:param name="beforeafter"/>
<br/>
<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.id"/></td>
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.email"/></td>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.view_count"/></td>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.post_count"/></td>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.country"/></td>
  </tr>
<mvn:cssrows>
<%
for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
    <%if ( (memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST) ) {%>
      <%-- @todo: replace alt string in next <img> --%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="<fmt:message key="mvnforum.common.member.no_gender"/>" title="<fmt:message key="mvnforum.common.member.no_gender"/>">
      <%if (memberBean.getMemberStatus() == 1) {%>
      <%} else {%>
      <%}%>
      <span class="memberName"><%=memberBean.getMemberName()%></span>
    <%} else {%>
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>">
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>">
      <%}%>
      <%if (memberBean.getMemberStatus() == 1) {%>
      <a
      <%} else {%>
      <a class="memberName"
      <%}%>
      href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>
    <%}%>
    </td>
    <td nowrap="nowrap"><%=memberBean.getMemberFirstname()%> <%=memberBean.getMemberLastname()%></td>
    <td nowrap="nowrap">
    <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else if (onlineUser.isGuest()) {%>
      <fmt:message key="mvnforum.common.member.email.hidden_to_guest"/>
    <%} else if ( (memberBean.getMemberEmailVisible()==1) || (permission.canAdminSystem()) ) {%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
    <%} else { %>
      <fmt:message key="mvnforum.common.member.email.dont_want_to_show"/>
    <%}%>
    </td>
    <td nowrap="nowrap" align="center"><%=memberBean.getMemberViewCount()%></td>
    <td nowrap="nowrap" align="center">
      <%if (MVNForumConfig.getEnableShowPostCount()) { %>
        <%=memberBean.getMemberPostCount()%>
      <% } %>
    </td>
    <td nowrap="nowrap" align="center">
    <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%>
    <%}%>
    </td>
    <td nowrap="nowrap" align="center">
    <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {%>
      <%=memberBean.getMemberCountry()%>
    <%}%>
    </td>
  </tr>
</pg:item>
<%
}//for
if (memberBeans.size() == 0) {
%>
  <tr class="<mvn:cssrow/>"><td colspan="7" align="center"><fmt:message key="mvnforum.admin.searchmember.result_no_members"/></td></tr>
<% }//if %>
</table>
</mvn:cssrows>
<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>
<% }//if memberBeans != null %>

<br />

<%@ include file="footer.jsp"%>
<script type="text/javascript">
//<![CDATA[
    InitParam();
//]]>
</script>

</mvn:body>
</mvn:html>
</fmt:bundle>