<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/editmember.jsp,v 1.98 2009/08/25 06:34:40 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.98 $
 - $Date: 2009/08/25 06:34:40 $
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

<%@ page import="java.util.Date"%>
<%@ page import="java.util.Locale" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.common.MemberMapping" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.editmember.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
MemberMapping mapping = MemberMapping.getInstance();
%>

<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {
  var agt = navigator.userAgent.toLowerCase();

  // Maybe, Opera make an onclick event when user press enter key
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
<% if ( MVNForumConfig.getEnableShowFirstName() && MVNForumConfig.isRequireRegisterFirstname() &&
        (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFirstname())) ) { %>
  if (isBlank(document.submitform.MemberFirstname, "<fmt:message key="mvnforum.common.member.first_name"/>")) return false;
<%}%>

<% if ( MVNForumConfig.getEnableShowLastName() && MVNForumConfig.isRequireRegisterLastname() &&
        (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberLastname())) ) { %>
  if (isBlank(document.submitform.MemberLastname, "<fmt:message key="mvnforum.common.member.last_name"/>")) return false;
<%}%>

<% if ( MVNForumConfig.getEnableShowBirthday() && MVNForumConfig.isRequireRegisterBirthday() &&
        (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberBirthday())) ) { %>
  if (isBlank(document.submitform.MemberBirthday, "<fmt:message key="mvnforum.common.member.birthday"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowGender() && MVNForumConfig.isRequireRegisterGender() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberGender())) ) {%>
  if (document.submitform.MemberGender.value == -1) {
    alert("<fmt:message key="mvnforum.common.member.gender"/> <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>"); 
    return false;
  }
<%}%>
  
<%if ( MVNForumConfig.getEnableShowAddress() && MVNForumConfig.isRequireRegisterAddress() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberAddress())) ) {%>
    if (isBlank(document.submitform.MemberAddress, "<fmt:message key="mvnforum.common.member.address"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowCity() && MVNForumConfig.isRequireRegisterCity() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCity())) ) {%>
    if (isBlank(document.submitform.MemberCity, "<fmt:message key="mvnforum.common.member.city"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowState() && MVNForumConfig.isRequireRegisterState() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberState())) ) {%>
    if (isBlank(document.submitform.MemberState, "<fmt:message key="mvnforum.common.member.state"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowCountry() && MVNForumConfig.isRequireRegisterCountry() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCountry())) ) {%>
    if (isBlank(document.submitform.MemberCountry, "<fmt:message key="mvnforum.common.member.country"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowPhone() && MVNForumConfig.isRequireRegisterPhone() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberPhone())) ) {%>
    if (isBlank(document.submitform.MemberPhone, "<fmt:message key="mvnforum.common.member.phone"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowMobile() && MVNForumConfig.isRequireRegisterMobile() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberMobile())) ) {%>
    if (isBlank(document.submitform.MemberMobile, "<fmt:message key="mvnforum.common.member.mobile"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowFax() && MVNForumConfig.isRequireRegisterFax() &&
        (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFax())) ) {%>
    if (isBlank(document.submitform.MemberFax, "<fmt:message key="mvnforum.common.member.fax"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowCareer() && MVNForumConfig.isRequireRegisterCareer() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCareer())) ) {%>
    if (isBlank(document.submitform.MemberCareer, "<fmt:message key="mvnforum.common.member.career"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowHomepage() && MVNForumConfig.isRequireRegisterHomepage() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberHomepage())) ) {%>
    if (isBlank(document.submitform.MemberHomepage, "<fmt:message key="mvnforum.common.member.homepage"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowYahoo() && MVNForumConfig.isRequireRegisterYahoo() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberYahoo())) ) {%>
    if (isBlank(document.submitform.MemberYahoo, "<fmt:message key="mvnforum.common.member.yahoo"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowAOL() && MVNForumConfig.isRequireRegisterAol() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberAol())) ) {%>
    if (isBlank(document.submitform.MemberAol, "<fmt:message key="mvnforum.common.member.aol"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowICQ() && MVNForumConfig.isRequireRegisterIcq() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberIcq())) ) {%>
    if (isBlank(document.submitform.MemberIcq, "<fmt:message key="mvnforum.common.member.icq"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowMSN() && MVNForumConfig.isRequireRegisterMsn() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberMsn())) ) {%>
    if (isBlank(document.submitform.MemberMsn, "<fmt:message key="mvnforum.common.member.msn"/>")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowCoolLink1() && MVNForumConfig.isRequireRegisterLink1() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCoolLink1())) ) {%>
    if (isBlank(document.submitform.MemberCoolLink1, "<fmt:message key="mvnforum.common.member.cool_link"/> 1")) return false;
<%}%>

<%if ( MVNForumConfig.getEnableShowCoolLink2() && MVNForumConfig.isRequireRegisterLink2() &&
       (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCoolLink2())) ) {%>
    if (isBlank(document.submitform.MemberCoolLink2, "<fmt:message key="mvnforum.common.member.cool_link"/> 2")) return false;
<%}%>
  
  if (isBlank(document.submitform.MemberCurrentMatkhau, "<fmt:message key="mvnforum.common.prompt.current_password"/>")) {
      return false;
  }
  if (document.submitform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberCurrentMatkhau.focus();
    return false;
  }
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
  <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.editmember.title"/>
</div>

<br/>
<form action="<%=urlResolver.encodeURL(request, response, "updatemember", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "updatemember")%>
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<input type="hidden" name="memberid" value="<%=memberBean.getMemberID()%>" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.editmember.title"/>:</td>
  </tr>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberStatus())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberStatus"><fmt:message key="mvnforum.common.member.status"/></label></td>
    <td>
      <select id="MemberStatus" name="MemberStatus" size="1">
        <option value="<%=MemberBean.MEMBER_STATUS_ENABLE%>" <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_ENABLE) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.status.enabled"/></option>
        <option value="<%=MemberBean.MEMBER_STATUS_DISABLE%>" <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_DISABLE) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.status.disabled"/></option>
        <% if (MVNForumConfig.getDefaultStatusOfRegisteredMember() == MemberBean.MEMBER_STATUS_PENDING) { %>
        <option value="<%=MemberBean.MEMBER_STATUS_PENDING%>" <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_PENDING) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.member.status.pending"/></option>
        <% } %>
      </select>
    </td>
  </tr>
  <%}%>
<% if (MVNForumConfig.getEnableShowFirstName()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFirstname())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberFirstname"><fmt:message key="mvnforum.common.member.first_name"/><% if (MVNForumConfig.isRequireRegisterFirstname()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberFirstname" name="MemberFirstname" value="<%=memberBean.getMemberFirstname()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowLastName()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberLastname())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberLastname"><fmt:message key="mvnforum.common.member.last_name"/><% if (MVNForumConfig.isRequireRegisterLastname()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberLastname" name="MemberLastname" value="<%=memberBean.getMemberLastname()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowBirthday()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberBirthday())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberBirthday"><fmt:message key="mvnforum.common.member.birthday"/> (dd/mm/yyyy)<% if (MVNForumConfig.isRequireRegisterBirthday()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberBirthday" name="MemberBirthday" value="<%=DateUtil.getDateDDMMYYYY(memberBean.getMemberBirthday())%>"/>
    <%--
    minhnn: if use this, need a way to set the correct current value
    <select name="MemberBirthdayDate" size="1">
      <%for (int i=1;i<32;i++) {%>
       <option><%=i%></option>
       <%}%>
    </select>
    <select name="MemberBirthdayMonth" size="1">
      <%for (int i=1;i<13;i++) {%>
       <option><%=i%></option>
       <%}%>
    </select>
    <input type="text" name = "MemberBirthdayYear" size=4 />
    --%>
    </td>
  </tr>
  <% } %>
<%} %>
<% if (MVNForumConfig.getEnableShowGender()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberGender())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberGender"><fmt:message key="mvnforum.common.member.gender"/><% if (MVNForumConfig.isRequireRegisterGender()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td>
      <select id="MemberGender" name="MemberGender" size="1">
        <option value="-1"></option>
        <option value="1"<%if (memberBean.getMemberGender() == 1) {%> selected="selected"<%}%>><fmt:message key="mvnforum.common.member.male"/></option>
        <option value="0"<%if (memberBean.getMemberGender() == 0) {%> selected="selected"<%}%>><fmt:message key="mvnforum.common.member.female"/></option>
      </select>
    </td>
  </tr>
  <%} %>  
<% } %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberEmailVisible())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberEmailVisible"><fmt:message key="mvnforum.common.member.show_email"/></label></td>
    <td>
      <input type="checkbox" id="MemberEmailVisible" name="MemberEmailVisible" value="yes" class="noborder"
      <%if (memberBean.getMemberEmailVisible() == 1) {%>checked="checked"<%}%>/>
    </td>
  </tr>
  <%} %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberNameVisible())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberNameVisible"><fmt:message key="mvnforum.common.member.name_visible"/></label></td>
    <td>
      <input type="checkbox" id="MemberNameVisible" name="MemberNameVisible" value="yes" class="noborder"
      <%if (memberBean.getMemberNameVisible() == 1) {%>checked="checked"<%}%>/>
    </td>
  </tr>
  <%} %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberPostsPerPage())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberPostsPerPage"><fmt:message key="mvnforum.common.member.posts_per_page"/></label></td>
    <td>
      <select id="MemberPostsPerPage" name="MemberPostsPerPage">
      <%
      int postPageArray[] = {5, 10, 15, 20, 30, 50};
      for (int i=0; i<postPageArray.length; i++) { %>
        <option value="<%=postPageArray[i]%>"<%if (postPageArray[i]==memberBean.getMemberPostsPerPage()) {%> selected="selected"<%}%>>
          <%=postPageArray[i]%>
        </option>
      <% }//for %>
      </select>
    </td>
  </tr>
  <%} %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberLanguage())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberLanguage"><fmt:message key="mvnforum.common.member.language"/></label></td>
    <td>
      <select id="MemberLanguage" name="MemberLanguage" size="1">
        <option value=""><fmt:message key="mvnforum.common.member.default_language"/></option>
<%
Locale[] locales = MVNForumConfig.getSupportedLocales();
for (int i = 0; i < locales.length; i++) {
    Locale locale = locales[i]; %>
        <option value="<%=locale.toString()%>"<%if (memberBean.getMemberLanguage().equals(locale.toString())) {%> selected="selected"<%}%>>
          <%=locale.getDisplayName(locale)%>
        </option>
<% } %>
      </select>
    </td>
  </tr>
  <% } %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberTimeZone())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="timeZoneSelectName"><fmt:message key="mvnforum.common.member.time_zone"/></label></td>
    <td>
<%
double selectedTimeZone = memberBean.getMemberTimeZone();
String timeZoneSelectName = "MemberTimeZone";
%>
<%@ include file="inc_timezone_option.jsp"%>
    </td>
  </tr>
  <%} %>
  <%--
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.skin"/></td>
    <td><input type="text" size="60" name="MemberSkin" readonly="readonly" value="<%=memberBean.getMemberSkin()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.member_option"/></td>
    <td><input type="text" size="60" name="MemberOption" readonly value="<%=memberBean.getMemberOption()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.private_option"/></td>
    <td><input type="text" size="60" name="MemberMessageOption" readonly value="<%=memberBean.getMemberMessageOption()%>" /></td>
  </tr>
  --%>
<% if (MVNForumConfig.getEnableShowAddress()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberAddress())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberAddress"><fmt:message key="mvnforum.common.member.address"/><% if (MVNForumConfig.isRequireRegisterAddress()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberAddress" name="MemberAddress" value="<%=memberBean.getMemberAddress()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowCity()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCity())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCity"><fmt:message key="mvnforum.common.member.city"/><% if (MVNForumConfig.isRequireRegisterCity()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberCity" name="MemberCity" value="<%=memberBean.getMemberCity()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowState()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberState())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberState"><fmt:message key="mvnforum.common.member.state"/><% if (MVNForumConfig.isRequireRegisterState()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberState" name="MemberState" value="<%=memberBean.getMemberState()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowCountry()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCountry())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCountry"><fmt:message key="mvnforum.common.member.country"/><% if (MVNForumConfig.isRequireRegisterCountry()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberCountry" name="MemberCountry" value="<%=memberBean.getMemberCountry()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowPhone()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberPhone())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberPhone"><fmt:message key="mvnforum.common.member.phone"/><% if (MVNForumConfig.isRequireRegisterPhone()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberPhone" name="MemberPhone" value="<%=memberBean.getMemberPhone()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowMobile()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberMobile())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMobile"><fmt:message key="mvnforum.common.member.mobile"/><% if (MVNForumConfig.isRequireRegisterMobile()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberMobile" name="MemberMobile" value="<%=memberBean.getMemberMobile()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowFax()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFax())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberFax"><fmt:message key="mvnforum.common.member.fax"/><% if (MVNForumConfig.isRequireRegisterFax()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberFax" name="MemberFax" value="<%=memberBean.getMemberFax()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowCareer()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCareer())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCareer"><fmt:message key="mvnforum.common.member.career"/><% if (MVNForumConfig.isRequireRegisterCareer()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberCareer" name="MemberCareer" value="<%=memberBean.getMemberCareer()%>" onkeyup="initTyper(this);"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowHomepage()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberHomepage())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberHomepage"><fmt:message key="mvnforum.common.member.homepage"/><% if (MVNForumConfig.isRequireRegisterHomepage()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberHomepage" name="MemberHomepage" value="<%=memberBean.getMemberHomepage()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowCoolLink1()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCoolLink1())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCoolLink1"><fmt:message key="mvnforum.common.member.cool_link"/> 1<% if (MVNForumConfig.isRequireRegisterLink1()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberCoolLink1" name="MemberCoolLink1" value="<%=memberBean.getMemberCoolLink1()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowCoolLink2()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCoolLink2())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCoolLink2"><fmt:message key="mvnforum.common.member.cool_link"/> 2<% if (MVNForumConfig.isRequireRegisterLink2()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberCoolLink2" name="MemberCoolLink2" value="<%=memberBean.getMemberCoolLink2()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowYahoo()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberYahoo())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberYahoo"><fmt:message key="mvnforum.common.member.yahoo"/><% if (MVNForumConfig.isRequireRegisterYahoo()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberYahoo" name="MemberYahoo" value="<%=memberBean.getMemberYahoo()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowAOL()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberAol())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberAol"><fmt:message key="mvnforum.common.member.aol"/><% if (MVNForumConfig.isRequireRegisterAol()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberAol" name="MemberAol" value="<%=memberBean.getMemberAol()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowICQ()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberIcq())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberIcq"><fmt:message key="mvnforum.common.member.icq"/><% if (MVNForumConfig.isRequireRegisterIcq()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberIcq" name="MemberIcq" value="<%=memberBean.getMemberIcq()%>"/></td>
  </tr>
  <%} %>
<% } %>
<% if (MVNForumConfig.getEnableShowMSN()) { %>
  <%if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberMsn())) {%>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberMsn"><fmt:message key="mvnforum.common.member.msn"/><% if (MVNForumConfig.isRequireRegisterMsn()) { %><span class="requiredfield"> *</span><% } %></label></td>
    <td><input type="text" size="60" id="MemberMsn" name="MemberMsn" value="<%=memberBean.getMemberMsn()%>"/></td>
  </tr>
  <%} %>
<% } %>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberCurrentMatkhau"><fmt:message key="mvnforum.common.prompt.current_password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" size="30" onkeypress="checkEnter(event);" /></td>
  </tr>
<%if (currentLocale.equals("vi")) {/*vietnamese here*/%>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.vietnamese_type"/>:</td>
    <td>
      <input type="radio" name="vnselector" id="TELEX" value="TELEX" onclick="setTypingMode(1);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.telex"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VNI" value="VNI" onclick="setTypingMode(2);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.vni"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VIQR" value="VIQR" onclick="setTypingMode(3);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.VIQR"/><br/>
      <input type="radio" name="vnselector" id="NOVN" value="NOVN" onclick="setTypingMode(0);" class="noborder" /> <fmt:message key="mvnforum.common.vietnamese_type.not_use"/>
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>