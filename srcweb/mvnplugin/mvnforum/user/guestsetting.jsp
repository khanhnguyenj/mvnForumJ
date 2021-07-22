<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/guestsetting.jsp,v 1.20 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.20 $
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

<%@ page import="java.util.Date"%>
<%@ page import="java.util.Locale" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title>Guest Setting</mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[
           

function SubmitForm() {
  <mvn:servlet>
    document.submitform.submitbutton.disabled=true;
    if (document.submitform.chkSaveCookie.checked == true) {
        document.submitform.SaveCookie.value = 'true';
    } else {
      document.submitform.SaveCookie.value = 'false';
    }
       
  </mvn:servlet>  
    document.submitform.submit();
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
    <fmt:message key="mvnforum.user.header.guestsetting"/>
</div>

<br/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="welcomeHeader">
    <td>
        <%if ("true".equals(request.getAttribute("SettingSuccsess"))) {%>
            <fmt:message key="mvnforum.user.guestsetting.success"/>
        <%} else {%>
            <fmt:message key="mvnforum.user.guestsetting.warning"/>
        <%} %>
    </td>
  </tr>
</table>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "guestsettingprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "guestsettingprocess")%>
<mvn:securitytoken />
  <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
    <mvn:cssrows>
    <tr class="portlet-section-header">
      <td colspan="2"><fmt:message key="mvnforum.user.header.guestsetting"/></td>
    </tr>
    <tr class="<mvn:cssrow/>">
    <td><label for="GuestPostsPerPage"><fmt:message key="mvnforum.common.member.posts_per_page"/></label></td>
    <td>
      <select id="GuestPostsPerPage" name="GuestPostsPerPage">
      <%
      int postPageArray[] = {5, 10, 15, 20, 30, 50};
      for (int i=0; i<postPageArray.length; i++) { %>
        <option value="<%=postPageArray[i]%>"<%if (postPageArray[i]==onlineUser.getPostsPerPage()) {%> selected="selected"<%}%>>
          <%=postPageArray[i]%>
        </option>
      <% }//for %>
      </select>
    </td>
  </tr>
    <tr class="<mvn:cssrow/>">
    <td><label for="GuestLanguage"><fmt:message key="mvnforum.common.member.language"/></label></td>
    <td>
      <select id="GuestLanguage" name="GuestLanguage" size="1">
        <option value=""><fmt:message key="mvnforum.common.member.default_language"/></option>
          <%
          Locale[] locales = MVNForumConfig.getSupportedLocales();
          for (int i = 0; i < locales.length; i++) {
              Locale locale = locales[i]; %>
                  <option value="<%=locale.toString()%>"<%if (locale.toString().equals(onlineUser.getLocaleName().toString())) {%> selected="selected"<%}%>>
                    <%=locale.getDisplayName(locale)%>
                  </option>
          <% } %>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
      <td><label for="timeZoneSelectName"><fmt:message key="mvnforum.common.member.time_zone"/></label></td>
      <td>
           <% String timeZoneSelectName = "GuestTimeZone";
           double selectedTimeZone = onlineUser.getTimeZone();%> 
           <%@ include file="inc_timezone_option.jsp"%>
      </td>
  </tr>
   
  <tr class="<mvn:cssrow/>">
    <td><label for="chkSaveCookie"><fmt:message key="mvnforum.common.savecookie"/></label></td>
    <td>
      <input type="hidden" name="SaveCookie" value="true"/>  
      <input type="checkbox" id="chkSaveCookie" name="chkSaveCookie" value="yes" class="noborder" checked="checked"/>
    </td>
  </tr>

  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.save"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
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

