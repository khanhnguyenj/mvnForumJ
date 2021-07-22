<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/listpendingusers.jsp,v 1.38 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.38 $
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
 - @author: XuanTL  
 --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.listpendingusers.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
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
  <fmt:message key="mvnforum.admin.listpendingusers.title"/>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.listpendingusers.info"/>
</div>
<br />


<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
if (sort.length() == 0) sort = "MemberCreationDate";
if (order.length() == 0) order = "DESC";

int pendingMembers = ((Integer)request.getAttribute("PendingMembers")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();
%>

<pg:pager
  url="listpendingusers"
  items="<%= pendingMembers %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = "Pending Users"; %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>
<%if (memberDAO.isSupportGetMembers_withSortSupport_limit() && 
      (memberDAO.isSupportGetNumberOfMembers() || 
       memberDAO.isSupportGetNumberOfMembers_inActivationStatus() ||
       memberDAO.isSupportGetNumberOfMembers_inMemberStatus()) ) {%>

<table width="95%" align="center">
  <tr class="portlet-font">
    <td nowrap="nowrap">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "listpendingusers", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "listpendingusers")%>
      <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
      <select id="sort" name="sort">
      <option value="MemberID" <%if (sort.equals("MemberID")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.id"/></option>
      <option value="MemberName" <%if (sort.equals("MemberName")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.login_name"/></option>
      <option value="MemberFirstname" <%if (sort.equals("MemberFirstname")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.first_name"/></option>
      <option value="MemberLastname" <%if (sort.equals("MemberLastname")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.last_name"/></option>
      <option value="MemberGender" <%if (sort.equals("MemberGender")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.gender"/></option>
      <option value="MemberBirthday" <%if (sort.equals("MemberBirthday")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.birthday"/></option>
      <option value="MemberCreationDate" <%if (sort.equals("MemberCreationDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.join_date"/></option>
      <option value="MemberLastLogon" <%if (sort.equals("MemberLastLogon")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.last_login"/></option>
      <option value="MemberViewCount" <%if (sort.equals("MemberViewCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.view_count"/></option>
      <option value="MemberPostCount" <%if (sort.equals("MemberPostCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.post_count"/></option>
      <option value="MemberCountry" <%if (sort.equals("MemberCountry")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.country"/></option>
      <option value="MemberEmail" <%if (sort.equals("MemberEmail")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.email"/></option>
      <option value="MemberHomepage" <%if (sort.equals("MemberHomepage")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.homepage"/></option>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
      </select>
      <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="SubmitForm();" class="liteoption" />
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

<form action="<%=urlResolver.encodeURL(request, response, "listpendingusersprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "listpendingusersprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>   
    <td align="center"><fmt:message key="mvnforum.common.action"/></td>   
  </tr>

<mvn:cssrows>
<%
Collection memberBeans = (Collection) request.getAttribute("PendingMemberBeans");
for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="Unknown gender" />
      <%} else {%>
        <%if (memberBean.getMemberGender() == 1) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="Male" />
        <%} else {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="Female" />
        <%}%>
      <%}%>
      <a class="memberName" href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>
    </td>
    <td nowrap="nowrap"><%=memberBean.getMemberFirstname()%> <%=memberBean.getMemberLastname()%></td>
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
      <%}%>
    </td>
    <td align="center">
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%>
      <%}%>
    </td>  
    <td align="center">
      <input type="radio" name="listpendingaction_<%=memberBean.getMemberID()%>" value="enable" class="noborder" /><fmt:message key="mvnforum.common.action.approve"/> &nbsp;
      <input type="radio" name="listpendingaction_<%=memberBean.getMemberID()%>" value="delete" class="noborder" /><fmt:message key="mvnforum.common.action.delete"/> &nbsp;
      <input type="radio" name="listpendingaction_<%=memberBean.getMemberID()%>" value="ignore" checked="checked" class="noborder" /><fmt:message key="mvnforum.common.action.ignore"/> &nbsp;
    </td>
  </tr>
</pg:item>
<%}%> 
  <% if (memberBeans.size() == 0) { %>
    <tr class="<mvn:cssrow/>"><td colspan="5" align="center"><fmt:message key="mvnforum.admin.listpendingusers.table.no_member"/></td></tr>
  <% } else { %>
    <tr class="portlet-section-footer">
      <td align="center" colspan="5">
        <input name="submit" type="submit" class="portlet-form-button" value="<fmt:message key="mvnforum.common.action.do_action"/>">
        <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption">
      </td>
    </tr>
  <% } %>
</mvn:cssrows> 
</table>
</form>
<%}%>

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
