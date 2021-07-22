<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/listmembers.jsp,v 1.110 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.110 $
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
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.listmembers.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function handleGo() {
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
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.listmembers.title"/>
</div>
<br/>

<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
if (sort.length() == 0) sort = "MemberCreationDate";
if (order.length() == 0) order = "DESC";

Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
int totalMembers = ((Integer)request.getAttribute("TotalMembers")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();
%>
<pg:pager
  url="listmembers"
  items="<%= totalMembers %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.members"); %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>

<table width="95%" align="center">
  <tr class="portlet-font">
    <td nowrap="nowrap">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "listmembers", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "listmembers")%>
      <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
      <select id="sort" name="sort">
      <option value="MemberID" <%if (sort.equals("MemberID")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.id"/></option>
      <option value="MemberName" <%if (sort.equals("MemberName")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.login_name"/></option>
      <%if (MVNForumConfig.getEnableShowFirstName()) {%>
      <option value="MemberFirstname" <%if (sort.equals("MemberFirstname")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.first_name"/></option>
      <%}%> 
      <%if (MVNForumConfig.getEnableShowLastName()) {%>
      <option value="MemberLastname" <%if (sort.equals("MemberLastname")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.last_name"/></option>
      <%}%> 
      <%if (MVNForumConfig.getEnableShowGender()) {%>  
      <option value="MemberGender" <%if (sort.equals("MemberGender")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.gender"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowBirthday()) {%>
      <option value="MemberBirthday" <%if (sort.equals("MemberBirthday")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.birthday"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowJoinDate()) {%>
      <option value="MemberCreationDate" <%if (sort.equals("MemberCreationDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.join_date"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowLastLogin()) {%>
      <option value="MemberLastLogon" <%if (sort.equals("MemberLastLogon")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.last_login"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowViewCount()) {%>
      <option value="MemberViewCount" <%if (sort.equals("MemberViewCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.view_count"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowPostCount()) {%>
      <option value="MemberPostCount" <%if (sort.equals("MemberPostCount")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.post_count"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowCountry()) { %>
      <option value="MemberCountry" <%if (sort.equals("MemberCountry")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.country"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowEmail()) { %>
      <option value="MemberEmail" <%if (sort.equals("MemberEmail")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.email"/></option>
      <%}%>
      <%if (MVNForumConfig.getEnableShowHomepage()) { %>       
      <option value="MemberHomepage" <%if (sort.equals("MemberHomepage")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.homepage"/></option>
      <%}%>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
      </select>

      <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="javascript:handleGo();" class="liteoption"/>
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

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.id"/></td>
    <%if (MVNForumConfig.getEnableShowLastName()||MVNForumConfig.getEnableShowFirstName()) {%>
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.full_name"/></td>    
    <%}%>  
    <%if (MVNForumConfig.getEnableShowEmail()) { %>
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.member.email"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowCountry()) { %>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.country"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowHomepage()) { %>       
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.homepage"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowViewCount()) {%>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.view_count"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowPostCount()) {%>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.post_count"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowJoinDate()) {%>
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowLastLogin()) {%>      
    <td nowrap="nowrap" align="center"><fmt:message key="mvnforum.common.member.last_login"/></td>
    <%}%>
  </tr>

<%
for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
    <%if ( (memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST) ) {%>
      <%-- @todo: replace alt string in next <img> --%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>"/>
      <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_DISABLE) {%>
      <%} else {%>
      <%}%>
      <span class="memberName"><%=memberBean.getMemberName()%></span>
    <%} else {%>
      <%if (MVNForumConfig.getEnableShowGender()) {%>  
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>"/>
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>"/>
      <%}
      }%>
      <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_DISABLE) {%>
      <a
      <%} else {%>
      <a class="memberName"
      <%}%>
      href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><%=memberBean.getMemberName()%></a>
    <%}%>
    </td>
    <%if (MVNForumConfig.getEnableShowFirstName() || MVNForumConfig.getEnableShowLastName()) {%>  
    <td nowrap="nowrap"><%if (MVNForumConfig.getEnableShowFirstName()){%><%=memberBean.getMemberFirstname()%><%}%><%if (MVNForumConfig.getEnableShowLastName()){%><%}%> <%=memberBean.getMemberLastname()%></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowEmail()) { %>
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
    <%}%>
    <%if (MVNForumConfig.getEnableShowCountry()) { %>
    <td nowrap="nowrap" align="center"><%=memberBean.getMemberCountry()%></td>
    <%}%> 
  <% if (MVNForumConfig.getEnableShowHomepage()) { %>       
      <td nowrap="nowrap" align="center">
    <%
    String homePage = memberBean.getMemberHomepage_http();
    if (homePage.equals("http://") == false) { %>
        <a href="<%=homePage%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt=""/></a></td>
    <%}%>
      </td>
  <%}%>
    <%if (MVNForumConfig.getEnableShowViewCount()) {%>
    <td nowrap="nowrap" align="center"><%=memberBean.getMemberViewCount()%></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowPostCount()) {%>
    <td nowrap="nowrap" align="center"><%=memberBean.getMemberPostCount()%></td>
    <%}%>
    <%if (MVNForumConfig.getEnableShowJoinDate()) {%>
    <td nowrap="nowrap" align="center">
    <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%>
    <%}%>
    </td>
    <%}%>
   <%if (MVNForumConfig.getEnableShowLastLogin()) {%>
    <td nowrap="nowrap" align="center">
    <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
    <%} else {%>
      <%=onlineUser.getGMTTimestampFormat(memberBean.getMemberLastLogon())%>
    <%}%>    
    </td>
    <%}%>
  </tr>
</pg:item>
<%
}//for
if (memberBeans.size() == 0) {
%>
  <tr class="<mvn:cssrow/>"><td colspan="9" align="center"><fmt:message key="mvnforum.user.listmembers.table.no_member"/></td></tr>
<% }//if %>
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

<%-- I don't think it is neccessary to show the legend --%>
<%-- table width="95%" border="0" align="center">
  <tr class="messageTextBold">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>"/></td>
    <td>Male</td>
  </tr>
  <tr class="messageTextBold">
    <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>"/></td>
    <td>Female</td>
  </tr>
</table --%>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
