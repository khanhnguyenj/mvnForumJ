<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/usermanagement.jsp,v 1.198 2009/11/25 10:33:54 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.198 $
 - $Date: 2009/11/25 10:33:54 $
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.usermanagement.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="refreshOnUnload()">

<script type="text/javascript">
//<![CDATA[
function refreshOnUnload() {
  document.submitform.submitbutton.disabled=false;
<%if (memberDAO.isSupportGetMembers_withSortSupport_limit()) {%>  
  document.form.go.disabled=false;
<%}%>
}

function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

<% if (internalUserDatabase) { %>
function ValidateForm() {
  if (document.submitform.member.value == '' && document.submitform.memberid.value == '' && document.submitform.memberemail.value == '') {
    alert("<fmt:message key="mvnforum.admin.usermanagement.find_member_javascript_prompt"/>");
    return false;
  }
  if (!isEmail(document.submitform.memberemail, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  return true;
}
<% } else { %>
function ValidateForm() {
  if (document.submitform.member.value == '' && document.submitform.memberid.value == '') {
    alert("<fmt:message key="mvnforum.admin.usermanagement.find_member_javascript_prompt"/>");
    return false;
  }
  return true;
}
<% } %>

<%if (memberDAO.isSupportGetMembers_withSortSupport_limit()) {%>
function SubmitFormGo() {
  <mvn:servlet>
    document.form.go.disabled=true;
  </mvn:servlet>  
  document.form.submit();
}
<%}%>
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
  <fmt:message key="mvnforum.admin.usermanagement.title"/>
</div>
<br/>

<%if (permission.canAdminSystem() && internalUserDatabase) { %>    
<form action="<%=urlResolver.encodeURL(request, response, "deletenonactivatedmembers", URLResolverService.RENDER_URL)%>" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "deletenonactivatedmembers")%>
<%-- mvn:securitytoken no need check here --%>
<%}%>
<div class="pagedesc">
  <fmt:message key="mvnforum.admin.usermanagement.info"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <% if (internalUserDatabase && permission.canAdminSystem()) { %>
    <a href="<%=urlResolver.encodeURL(request, response, "addmember")%>" class="command"><fmt:message key="mvnforum.admin.addmember.title"/></a><br/>
  <% } %>
  <% if (memberDAO.isSupportGetMembersFromIDRange()) { %>
    <a href="<%=urlResolver.encodeURL(request, response, "searchmember")%>" class="command"><fmt:message key="mvnforum.admin.searchmember.title"/></a><br/>
  <% } %>
  <% if (isServlet) { %>
    <a href="<%=urlResolver.encodeURL(request, response, "listpendingusers")%>" class="command"><fmt:message key="mvnforum.admin.listpendingusers.title"/></a><br/>
  <%} %>
  <%-- 
    <a href="<%=urlResolver.encodeURL(request, response, "listuserexpire")%>" class="command"><fmt:message key="mvnforum.admin.listuserexpire.title"/></a><br/>
  --%>
  <% if (permission.canAdminSystem()) { %>
    <a href="<%=urlResolver.encodeURL(request, response, "permissionsummary")%>" class="command"><fmt:message key="mvnforum.admin.permissionsummary.title"/></a><br/>
  <% } %>
  <% if (permission.canAdminSystem()) { %>
    <a href="<%=urlResolver.encodeURL(request, response, "rankmanagement")%>" class="command"><fmt:message key="mvnforum.admin.rankmanagement.title"/></a><br/>
  <% } %>
  <%if ( permission.canAdminSystem() && internalUserDatabase && (MVNForumConfig.getAlwaysActivation() == false) ) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "sendactivatemailtoallprocess?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request))%>" class="command"><fmt:message key="mvnforum.admin.usermanagement.send_activation_mail"/></a><br/>
  <%} %>
  <% if (permission.canAdminSystem() && internalUserDatabase) { %>    
    <label for="days"><fmt:message key="mvnforum.admin.usermanagement.delete_non_actived_member"/></label>
    <input type="text" id="days" name="days" value="7" size="4" maxlength="4" /> <fmt:message key="mvnforum.admin.usermanagement.day_ago"/>
    <input type="submit" value="<fmt:message key="mvnforum.admin.usermanagement.start_step_1"/>" class="portlet-form-button"/>
  <% } %>
</div>

<%if (permission.canAdminSystem() && internalUserDatabase) { %>    
</form>
<%}%>  
<br/>
<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
if (sort.length() == 0) sort = "MemberCreationDate";
if (order.length() == 0) order = "DESC";

int totalMembers = ((Integer)request.getAttribute("TotalMembers")).intValue();
int enabledMembers = ((Integer)request.getAttribute("EnabledMembers")).intValue();
int disabledMembers = ((Integer)request.getAttribute("DisabledMembers")).intValue();
int pendingMembers = ((Integer)request.getAttribute("PendingMembers")).intValue();
int activatedMembers = ((Integer)request.getAttribute("ActivatedMembers")).intValue();
int nonactivatedMembers = ((Integer)request.getAttribute("NonActivatedMembers")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();
%>

<%if (memberDAO.isSupportGetMembers_withSortSupport_limit() && 
      (memberDAO.isSupportGetNumberOfMembers() || 
       memberDAO.isSupportGetNumberOfMembers_inActivationStatus() ||
       memberDAO.isSupportGetNumberOfMembers_inMemberStatus()) ) {%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.usermanagement.member_statistics"/></td>
  </tr>
  <%if (memberDAO.isSupportGetNumberOfMembers()) { %>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><fmt:message key="mvnforum.admin.usermanagement.total_members"/></td>
    <td><b><%= totalMembers %></b></td>
  </tr>
  <%} %>
  <%if (memberDAO.isSupportGetNumberOfMembers_inMemberStatus()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.usermanagement.enabled_members"/></td>
    <td><b><%= enabledMembers %></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.usermanagement.disabled_members"/></td>
    <td><b><%= disabledMembers %></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.usermanagement.pending_members"/></td>
    <td>
      <b><%= pendingMembers %></b>
      <% if ( memberDAO.isSupportGetMembers_withSortSupport_limit() && (pendingMembers > 0) ) { %>
         - <a href="<%=urlResolver.encodeURL(request, response, "listpendingusers")%>" class="command"><fmt:message key="mvnforum.admin.listpendingusers.title"/></a>
      <% } %>
    </td>
  </tr>
  <%} %>
  <%if (memberDAO.isSupportGetNumberOfMembers_inActivationStatus()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.usermanagement.activated_members"/></td>
    <td><b><%= activatedMembers %></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.admin.usermanagement.non_activated_members"/></td>
    <td><b><%= nonactivatedMembers %></b></td>
  </tr>
  <%} %>
</table>
</mvn:cssrows>
<%} %>
<br/>
<pg:pager
  url="usermanagement"
  items="<%= totalMembers %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = "Members"; %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>
<form action="<%=urlResolver.encodeURL(request, response, "viewmember", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "viewmember")%>
<%-- mvn:securitytoken no need check here --%>
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.usermanagement.find_member"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>
    <label for="memberid"><fmt:message key="mvnforum.admin.usermanagement.find_member_by_memberid"/>:</label> <input type="text" id="memberid" name="memberid" size="5" maxlength="5" />
    &nbsp;&nbsp;&nbsp;<label for="member"><fmt:message key="mvnforum.admin.usermanagement.or_by_membername"/>:</label> <input type="text" id="member" name="member" />
  <% if (internalUserDatabase) { %>
  <%-- in case of LDAP, we could not search by email --%>
    &nbsp;&nbsp;&nbsp;<label for="memberemail"><fmt:message key="mvnforum.admin.usermanagement.or_by_email"/>:</label> <input type="text" id="memberemail" name="memberemail" size="30" />
  <% } %>
    <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.usermanagement.find_member"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>
<%if (memberDAO.isSupportGetMembers_withSortSupport_limit()) { %>
<table width="95%" align="center">
  <tr class="portlet-font">
    <td nowrap="nowrap">
    <form name="form" action="<%=urlResolver.encodeURL(request, response, "usermanagement", URLResolverService.ACTION_URL)%>" <mvn:method/>>
      <%=urlResolver.generateFormAction(request, response, "usermanagement")%>
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
      <option value="MemberStatus" <%if (sort.equals("MemberStatus")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.status"/></option>
      </select>
      <label for="order"><fmt:message key="mvnforum.common.order"/></label>
      <select id="order" name="order">
      <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
      <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
      </select>

      <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="SubmitFormGo();" class="liteoption" />
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
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><fmt:message key="mvnforum.common.member.full_name"/></td>
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.status"/></td>
    <td align="center"><fmt:message key="mvnforum.common.member.activation_status"/></td>
  <% if (permission.canAdminSystem()) { %>
    <td align="center"><fmt:message key="mvnforum.common.permission.assign_forum"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission.edit_global_permission"/></td>
    <td align="center"><fmt:message key="mvnforum.common.permission"/></td>
  <% } %>
    <td align="center"><fmt:message key="mvnforum.common.action"/></td>
  <% if (permission.canAdminSystem()) { %>
    <td align="center"><fmt:message key="mvnforum.common.action.delete"/></td>
  <% } %>
  </tr>
<%
Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
//int offset = ((Integer)request.getAttribute("offset")).intValue();
%>
<mvn:cssrows>
<%
  for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
    MemberBean memberBean = (MemberBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="Unknown gender" />
      <%} else {%>
        <%if (memberBean.getMemberGender() == MemberBean.MEMBER_GENDER_MALE) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="Male" />
        <%} else {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="Female" />
        <%}%>
      <%}%>
      <%if (memberBean.getMemberStatus() != MemberBean.MEMBER_STATUS_ENABLE) {%>
        <a class="disabledItem" 
      <%} else {%>
        <a class="memberName" 
      <%}%>
        href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>
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
      <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_ENABLE) {%>
        <span style="color:#008000"><fmt:message key="mvnforum.common.member.status.enabled"/></span>
      <%} else if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_DISABLE) {%>
        <span style="color:#FF0080"><fmt:message key="mvnforum.common.member.status.disabled"/></span>      
      <%} else if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_PENDING) {%>
        <span style="color:blue"><fmt:message key="mvnforum.common.member.status.pending"/></span>
      <%} else {
          throw new AssertionError("Cannot understand MemberStatus = " + memberBean.getMemberStatus());
        } %>
    </td>
    <td align="center">
    <%if ( (memberBean.getMemberActivateCode()!=null) && (memberBean.getMemberActivateCode().length()>0) ) {
        if (memberBean.getMemberActivateCode().equals("activated")) {%>
          <span style="color:#008000"><fmt:message key="mvnforum.common.member.activation_status.activated"/></span>
      <%} else {%>
          <span style="color:blue"><fmt:message key="mvnforum.common.member.activation_status.pending"/></span>
      <%}%>
    <%} else {%>
        <span style="color:#FF0080"><fmt:message key="mvnforum.common.member.activation_status.not_activated"/></span>
    <%}%>
    </td>
  <% if (permission.canAdminSystem()) { %>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "assignforumtomember?memberid=" + memberBean.getMemberID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Forum's Permissions for member : <%=memberBean.getMemberName()%>" /></a>
    </td>
    <td align="center">
      <a href="<%=urlResolver.encodeURL(request, response, "editmemberpermission?memberid=" + memberBean.getMemberID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Assign Permissions for member : <%=memberBean.getMemberName()%>" /></a>
    </td>
    <td align="center">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewmemberpermissions?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.common.action.view"/></a>
    </td>
  <% } %>
    <td align="center">
    <%if (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_ADMIN) {
        if (memberBean.getMemberStatus() == 0) {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "changememberstatusprocess?memberid=" + memberBean.getMemberID() + "&amp;status=" + MemberBean.MEMBER_STATUS_DISABLE + "&amp;offset=" + offset + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.common.action.disable"/></a>   
      <%} else {%>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "changememberstatusprocess?memberid=" + memberBean.getMemberID() + "&amp;status=" + MemberBean.MEMBER_STATUS_ENABLE + "&amp;offset=" + offset + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.common.action.enable"/></a>
      <%}%>
    <%}%>
    </td>
  <% if (permission.canAdminSystem()) { %>
    <td align="center">
      <% if ((memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_ADMIN) &&
             (memberBean.getMemberID()!=0) &&
             (memberBean.getMemberID()!=onlineUser.getMemberID()) &&
             (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_GUEST) &&
             (memberBean.getMemberPostCount()==0)) { %>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletemember?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.common.action.delete"/></a>
      <% } %>
    </td>
  <% } %>
  </tr>
</pg:item>
<%
  }//for
   if (memberBeans.size() == 0) { %>
  <tr class="<mvn:cssrow/>"><td colspan="10" align="center"><fmt:message key="mvnforum.user.listmembers.table.no_member"/></td></tr>
<% }//if %>
</mvn:cssrows>
</table>
<%}//if %>

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
