<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addmember.jsp,v 1.144 2009/12/15 10:41:00 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.144 $
 - $Date: 2009/12/15 10:41:00 $
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
 - @author: Igor Manic   
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="java.util.Locale" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.addmember.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
  <% if (MVNForumConfig.getRequireActivation()) {%>
    alert("<fmt:message key="mvnforum.user.addmember.javascript_prompt"/>");
  <% } %>
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  var field,temp;
  field = document.submitform.MemberName;
  temp = field.value;
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;
  if (!checkGoodName(temp, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;

  field=document.submitform.MemberMatkhau;
  temp=field.value
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.password"/>")) return false;
  //Check Password's length
  if (temp.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    field.focus();
    return false;
  }

  field=document.submitform.MemberMatkhauConfirm;
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.password"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;

  if (temp!=field.value) {
    alert("<fmt:message key="mvnforum.common.member.password"/> <fmt:message key="mvnforum.common.js.prompt.notmatch"/>"); 
    return false;
  }

  field=document.submitform.MemberEmail;
  temp=field.value
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.email"/>")) return false;
  if (!isEmail(field, "<fmt:message key="mvnforum.common.member.email"/>")) return false;

  field=document.submitform.MemberEmailConfirm;
  if (isBlank(field, "<fmt:message key="mvnforum.common.member.email"/> (<fmt:message key="mvnforum.common.retype"/>)")) return false;
  if (!isEmail(field, "<fmt:message key="mvnforum.common.member.email"/>")) return false;

  if (temp!=field.value) {
    alert("<fmt:message key="mvnforum.common.member.email"/> <fmt:message key="mvnforum.common.js.prompt.notmatch"/>");
    return false;
  }
<%if (MVNForumConfig.getEnableShowFirstName() && MVNForumConfig.isRequireRegisterFirstname()) {%>
    if (isBlank(document.submitform.MemberFirstname, "<fmt:message key="mvnforum.common.member.first_name"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowLastName() && MVNForumConfig.isRequireRegisterLastname()) {%>
    if (isBlank(document.submitform.MemberLastname, "<fmt:message key="mvnforum.common.member.last_name"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowGender() && MVNForumConfig.isRequireRegisterGender()) {%>
    if (document.submitform.MemberGender.value == -1) {
      alert("<fmt:message key="mvnforum.common.member.gender"/> <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>"); 
      return false;
    }
<%}%>

<%if (MVNForumConfig.getEnableShowBirthday() && MVNForumConfig.isRequireRegisterBirthday()) {%>
    if (document.submitform.day.value=="") {
      alert("<fmt:message key="mvnforum.common.member.birthday"/> <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>"); 
      return false;
    }
    if (document.submitform.month.value=="") {
      alert("<fmt:message key="mvnforum.common.member.birthday"/> <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>"); 
      return false;
    }
    if (document.submitform.year.value=="") {
      alert("<fmt:message key="mvnforum.common.member.birthday"/> <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>"); 
      return false;
    }
<%}%>

<%if (MVNForumConfig.getEnableShowAddress() && MVNForumConfig.isRequireRegisterAddress()) {%>
    if (isBlank(document.submitform.MemberAddress, "<fmt:message key="mvnforum.common.member.address"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowCity() && MVNForumConfig.isRequireRegisterCity()) {%>
    if (isBlank(document.submitform.MemberCity, "<fmt:message key="mvnforum.common.member.city"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowState() && MVNForumConfig.isRequireRegisterState()) {%>
    if (isBlank(document.submitform.MemberState, "<fmt:message key="mvnforum.common.member.state"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowCountry() && MVNForumConfig.isRequireRegisterCountry()) {%>
    if (isBlank(document.submitform.MemberCountry, "<fmt:message key="mvnforum.common.member.country"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowPhone() && MVNForumConfig.isRequireRegisterPhone()) {%>
    if (isBlank(document.submitform.MemberPhone, "<fmt:message key="mvnforum.common.member.phone"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowMobile() && MVNForumConfig.isRequireRegisterMobile()) {%>
    if (isBlank(document.submitform.MemberMobile, "<fmt:message key="mvnforum.common.member.mobile"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowFax() && MVNForumConfig.isRequireRegisterFax()) {%>
    if (isBlank(document.submitform.MemberFax, "<fmt:message key="mvnforum.common.member.fax"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowCareer() && MVNForumConfig.isRequireRegisterCareer()) {%>
    if (isBlank(document.submitform.MemberCareer, "<fmt:message key="mvnforum.common.member.career"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowHomepage() && MVNForumConfig.isRequireRegisterHomepage()) {%>
    if (isBlank(document.submitform.MemberHomepage, "<fmt:message key="mvnforum.common.member.homepage"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowYahoo() && MVNForumConfig.isRequireRegisterYahoo()) {%>
    if (isBlank(document.submitform.MemberYahoo, "<fmt:message key="mvnforum.common.member.yahoo"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowAOL() && MVNForumConfig.isRequireRegisterAol()) {%>
    if (isBlank(document.submitform.MemberAol, "<fmt:message key="mvnforum.common.member.aol"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowICQ() && MVNForumConfig.isRequireRegisterIcq()) {%>
    if (isBlank(document.submitform.MemberIcq, "<fmt:message key="mvnforum.common.member.icq"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowMSN() && MVNForumConfig.isRequireRegisterMsn()) {%>
    if (isBlank(document.submitform.MemberMsn, "<fmt:message key="mvnforum.common.member.msn"/>")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowCoolLink1() && MVNForumConfig.isRequireRegisterLink1()) {%>
    if (isBlank(document.submitform.MemberCoolLink1, "<fmt:message key="mvnforum.common.member.cool_link"/> 1")) return false;
<%}%>

<%if (MVNForumConfig.getEnableShowCoolLink2() && MVNForumConfig.isRequireRegisterLink2()) {%>
    if (isBlank(document.submitform.MemberCoolLink2, "<fmt:message key="mvnforum.common.member.cool_link"/> 2")) return false;
<%}%>

<%if (MVNForumConfig.getEnableCaptcha()) {%>
    if (isBlank(document.submitform.CaptchaResponse, "<fmt:message key="mvnforum.common.captcha.response"/>")) return false;
<%}%>

  return true;
}

var type;

function validate(_type) {
   type = _type;
   var idField;
   if (_type == "user") {
     idField = document.submitform.MemberName;
     url = "ajaxvalidate?action=1&userName=" + encodeURIComponent(idField.value);
   }   
   if (_type == "email") {
     idField = document.submitform.MemberEmail;
     if (!isEmail(idField, "<fmt:message key="mvnforum.common.member.email"/>")) {
       document.getElementById(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.not_good_email"/></ div>";
       return false;
     }
     url = "ajaxvalidate?action=2&emailAddress=" + encodeURIComponent(idField.value);
   }   
  new Ajax.Request(url, {
    method: 'get',
    onSuccess: function(transport) {
      var message = transport.responseXML.getElementsByTagName("message")[0];
      setMessage(message.childNodes[0].nodeValue);
    },
    onFailure: function() {
      alert('connection time out, please try again later');
    }
  });
}

function setMessage(message) {
  if (type == "user") {
    if (message == "empty") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.empty_username"/></ div>";
    } else if (message == "not_good_name") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.not_good_name"/></ div>";
    } else if (message == "banned_username") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.banned_username"/></ div>";
    } else if (message == "existed") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.registered_username"/></ div>";
    } else if (message == "valid") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:green\"><fmt:message key="mvnforum.user.addmember.ajax.valid_username"/></ div>";
    }
  } 
  if (type == "email") {
    if (message == "empty") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.empty_email"/></ div>";
    } else if (message == "banned_email") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.banned_email"/></ div>";
    } else if (message == "existed") {
       $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:red\"><fmt:message key="mvnforum.user.addmember.ajax.registered_email"/></ div>";
    } else if (message == "valid") {
        $(type + 'ValidateResult').innerHTML = "<div style=\"display: inline; color:green\"><fmt:message key="mvnforum.user.addmember.ajax.valid_email"/></ div>";
    }
  } 
}

jQuery(document).ready(function($){

    $("#reloadCaptchaButton").click(function(event){
        $("#captchaimg").attr('src', '<%=urlResolver.encodeURL(request, response, "captchaimage?reload=true&amp;time=")%>'+new Date().getTime());
    });
    
    $("#resetButton").click(function(event){
        $('#submitform').each(function(){
            this.reset();
        });
            
        initVNTyperMode();
        event.preventDefault();
        
    });
    
});
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.addmember.title"/>
</div>

<br/>

<form action="<%=urlResolver.encodeURL(request, response, "registermemberprocess", URLResolverService.ACTION_URL)%>" method="post" id="submitform" name="submitform">
<%=urlResolver.generateFormAction(request, response, "registermemberprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.addmember.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="Member_Name"><fmt:message key="mvnforum.common.member.login_name"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" size="60" id="Member_Name" name="MemberName" />&nbsp;&nbsp;
      <input type="button" class="liteoption" onclick="validate('user');" value="Check this username" />&nbsp;&nbsp;<span id="userValidateResult" style="display: inline;" ></span>&nbsp;&nbsp;
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="Member_Matkhau"><fmt:message key="mvnforum.common.member.password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" size="60" id="Member_Matkhau" name="MemberMatkhau" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberMatkhauConfirm"><fmt:message key="mvnforum.common.member.password"/></label> (<fmt:message key="mvnforum.common.retype"/>)<span class="requiredfield"> *</span></td>
    <td><input type="password" size="60" id="MemberMatkhauConfirm" name="MemberMatkhauConfirm" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberEmail"><fmt:message key="mvnforum.common.member.email"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" size="60" id="MemberEmail" name="MemberEmail" />&nbsp;&nbsp;
      <input type="button" class="liteoption" onclick="validate('email');" value="Check this email" />&nbsp;&nbsp;<span id="emailValidateResult" style="display: inline;" ></span>  
    <% if (MVNForumConfig.getRequireActivation()) {%>
      <br/><span class="warning"><fmt:message key="mvnforum.user.addmember.correct_email_remind"/></span>
    <% } %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberEmailConfirm"><fmt:message key="mvnforum.common.member.email"/> (<fmt:message key="mvnforum.common.retype"/>)<span class="requiredfield"> *</span></label></td>
    <td><input type="text" size="60" id="MemberEmailConfirm" name="MemberEmailConfirm" /></td>
  </tr>
<% if (MVNForumConfig.getEnableShowFirstName()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberFirstname"><fmt:message key="mvnforum.common.member.first_name"/><% if (MVNForumConfig.isRequireRegisterFirstname()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberFirstname" name="MemberFirstname" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowLastName()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberLastname"><fmt:message key="mvnforum.common.member.last_name"/><% if (MVNForumConfig.isRequireRegisterLastname()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberLastname" name="MemberLastname" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowGender()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberGender"><fmt:message key="mvnforum.common.member.gender"/><% if (MVNForumConfig.isRequireRegisterGender()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td>
    <select id="MemberGender" name="MemberGender" size="1">
      <option value="-1"></option>
      <option value="1"><fmt:message key="mvnforum.common.member.male"/></option>
      <option value="0"><fmt:message key="mvnforum.common.member.female"/></option>
    </select>
    </td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowBirthday()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <fmt:message key="mvnforum.common.member.birthday"/> (dd/mm/yyyy)<% if (MVNForumConfig.isRequireRegisterBirthday()) { %><span class="requiredfield"> *</span><% } %>
    </td>
    <td>
<%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
<% } %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberEmailVisible"><fmt:message key="mvnforum.common.member.show_email"/></label></td>
    <td><input type="checkbox" checked="checked" size="60" id="MemberEmailVisible" name="MemberEmailVisible" value="yes" class="noborder" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberNameVisible"><fmt:message key="mvnforum.common.member.name_visible"/></label></td>
    <td><input type="checkbox" checked="checked" size="60" id="MemberNameVisible" name="MemberNameVisible" value="yes" class="noborder" <%if (MVNForumConfig.getEnableInvisibleUsers()==false) {%> disabled="disabled"<%}%> /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberPostsPerPage"><fmt:message key="mvnforum.common.member.posts_per_page"/></label></td>
    <td>
      <select id="MemberPostsPerPage" name="MemberPostsPerPage">
        <option value="5">5</option>
        <option value="10" selected="selected">10</option>
        <option value="15">15</option>
        <option value="20">20</option>
        <option value="30">30</option>
        <option value="50">50</option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="MemberLanguage"><fmt:message key="mvnforum.common.member.language"/></label></td>
    <td>
      <select id="MemberLanguage" name="MemberLanguage" size="1">
        <option value=""><fmt:message key="mvnforum.common.member.default_language"/></option>
<%
Locale[] locales = MVNForumConfig.getSupportedLocales();
for (int i = 0; i < locales.length; i++) {
    Locale locale = locales[i]; %>
        <option value="<%=locale.toString()%>"><%=locale.getDisplayName(locale)%></option>
<% } %>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="timeZoneSelectName"><fmt:message key="mvnforum.common.member.time_zone"/></label></td>
    <td>
<%
double selectedTimeZone = MVNForumConfig.getDefaultGuestTimeZone();
String timeZoneSelectName = "MemberTimeZone";
%>
<%@ include file="inc_timezone_option.jsp"%>
    </td>
  </tr>
<%-- @todo: Should we add the following row:
  <tr class="portlet-section-header">
    <td colspan="2">Personal data (will not be revealed or given to third parties... or some similiar message? And maybe a link to some privacy statement?</td>
  </tr>
--%>
<% if (MVNForumConfig.getEnableShowAddress()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberAddress"><fmt:message key="mvnforum.common.member.address"/><% if (MVNForumConfig.isRequireRegisterAddress()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberAddress" name="MemberAddress" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowCity()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberCity"><fmt:message key="mvnforum.common.member.city"/><% if (MVNForumConfig.isRequireRegisterCity()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberCity" name="MemberCity" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowState()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberState"><fmt:message key="mvnforum.common.member.state"/><% if (MVNForumConfig.isRequireRegisterState()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberState" name="MemberState" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowCountry()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberCountry"><fmt:message key="mvnforum.common.member.country"/><% if (MVNForumConfig.isRequireRegisterCountry()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberCountry" name="MemberCountry" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowPhone()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberPhone"><fmt:message key="mvnforum.common.member.phone"/><% if (MVNForumConfig.isRequireRegisterPhone()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberPhone" name="MemberPhone" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowMobile()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberMobile"><fmt:message key="mvnforum.common.member.mobile"/><% if (MVNForumConfig.isRequireRegisterMobile()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberMobile" name="MemberMobile" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowFax()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberFax"><fmt:message key="mvnforum.common.member.fax"/><% if (MVNForumConfig.isRequireRegisterFax()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberFax" name="MemberFax" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowCareer()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberCareer"><fmt:message key="mvnforum.common.member.career"/><% if (MVNForumConfig.isRequireRegisterCareer()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberCareer" name="MemberCareer" onkeyup="initTyper(this);" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowHomepage()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberHomepage"><fmt:message key="mvnforum.common.member.homepage"/><% if (MVNForumConfig.isRequireRegisterHomepage()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberHomepage" name="MemberHomepage" value="http://" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowYahoo()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberYahoo"><fmt:message key="mvnforum.common.member.yahoo"/><% if (MVNForumConfig.isRequireRegisterYahoo()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberYahoo" name="MemberYahoo" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowAOL()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberAol"><fmt:message key="mvnforum.common.member.aol"/><% if (MVNForumConfig.isRequireRegisterAol()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberAol" name="MemberAol" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowICQ()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberIcq"><fmt:message key="mvnforum.common.member.icq"/><% if (MVNForumConfig.isRequireRegisterIcq()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberIcq" name="MemberIcq" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowMSN()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberMsn"><fmt:message key="mvnforum.common.member.msn"/><% if (MVNForumConfig.isRequireRegisterMsn()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberMsn" name="MemberMsn" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowCoolLink1()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberCoolLink1"><fmt:message key="mvnforum.common.member.cool_link"/> 1<% if (MVNForumConfig.isRequireRegisterLink1()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberCoolLink1" name="MemberCoolLink1" value="http://" /></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowCoolLink2()) { %>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap">
      <label for="MemberCoolLink2"><fmt:message key="mvnforum.common.member.cool_link"/> 2<% if (MVNForumConfig.isRequireRegisterLink2()) { %><span class="requiredfield"> *</span><% } %></label>
    </td>
    <td><input type="text" size="60" id="MemberCoolLink2" name="MemberCoolLink2" value="http://" /></td>
  </tr>
<% } %>
<%if (MVNForumConfig.getEnableCaptcha()) {%>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><fmt:message key="mvnforum.common.captcha.challenge"/></td>
    <td>
      <img id="captchaimg" src="<%=urlResolver.encodeURL(request, response, "captchaimage?time="+System.currentTimeMillis())%>" alt="<fmt:message key="mvnforum.common.captcha.desc"/>" title="<fmt:message key="mvnforum.common.captcha.desc"/>" border="0" />
      &nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_refresh.gif" alt="<fmt:message key="mvnforum.common.captcha.refresh"/>" title="<fmt:message key="mvnforum.common.captcha.refresh"/>" border="0" id="reloadCaptchaButton" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td nowrap="nowrap"><label for="CaptchaResponse"><fmt:message key="mvnforum.common.captcha.response"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" size="60" id="CaptchaResponse" name="CaptchaResponse" value="" />
    </td>
  </tr>

<%}// end if captcha%>
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
    <input type="button" name="submitbutton" class="portlet-form-button" value="<fmt:message key="mvnforum.common.action.register"/>" onclick="javascript:SubmitForm();" />
    <input type="reset" id="resetButton" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
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
