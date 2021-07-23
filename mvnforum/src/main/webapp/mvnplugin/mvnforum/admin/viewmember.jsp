<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/viewmember.jsp,v 1.134 2010/07/28 04:31:24 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.134 $
 - $Date: 2010/07/28 04:31:24 $
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

<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.viewmember.title"/></mvn:title>
  <%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
int watchCount = ((Integer)request.getAttribute("WatchCount")).intValue();
%>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
    <%}%>
    <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
    <a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.common.member"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?memberid=" + memberBean.getMemberID())%>"><%=memberBean.getMemberName()%></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.admin.viewmember.title"/>
</div>

<br/>

<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
  <tr class="pagedesc">
    <td width="150" align="center" valign="middle">
    <% if (memberBean.getMemberAvatar().length() > 0) {
         String avatar = memberBean.getMemberAvatar_processed(request, response);
         if (avatar.startsWith("getavatar")) {
           //avatar = userUrl + "/" + avatar;// it is from the user area, not admin area
           avatar = urlResolver.encodeURL(request, response, avatar, URLResolverService.RENDER_URL, "view");
         } %>
    <img src="<%=avatar%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" />
    <% } else { %>
    <img src="<%=contextPath%>/mvnplugin/mvnforum/images/user/no_picture.gif" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.no_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.no_avatar"/>" />
    <% } %>
    </td>
    <td valign="top">
    <% if (internalUserDatabase) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editmember?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.editmember.title"/></a><br/>
    <% } %>  
    <% if ( permission.canAdminSystem() && MVNForumConfig.getEnableAdminCanChangePassword() && (memberBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_ADMIN) ) {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "changepassword?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.changepassword.title"/></a><br/>
    <% } %>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "editmembertitle?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.editmembertitle.title"/></a><br/>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "resetsignatureprocess?memberid=" + memberBean.getMemberID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.reset_member_signature"/></a><br/>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "resetavatarprocess?memberid=" + memberBean.getMemberID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.reset_member_avatar"/></a><br/>
    <%if (internalUserDatabase) {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "resetactivationprocess?memberid=" + memberBean.getMemberID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.reset_member_activation"/></a><br/>
    <%}%>
    </td>
    <td valign="top">
      <% if (watchCount > 0) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletewatch?memberid=" + memberBean.getMemberID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.delete_all_watch"/></a> (<%=watchCount%> <% if (watchCount == 1) { %><fmt:message key="mvnforum.common.watch"/><% } else { %><fmt:message key="mvnforum.common.watches"/><% } %>)<br/>
      <% } %>
      
  <% if (permission.canAdminSystem()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editmemberpermission?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.editmemberpermission.title"/></a><br/>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "assignforumtomember?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.assignforumtomember.title"/></a><br/>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewmemberpermissions?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.viewmemberpermissions.title"/></a><br/>
      <% if ((memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_ADMIN) &&
             (memberBean.getMemberID()!=0) &&
             (memberBean.getMemberID()!=onlineUser.getMemberID()) &&
             (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_GUEST) &&
             (memberBean.getMemberPostCount()==0)) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletemember?memberid=" + memberBean.getMemberID())%>"><fmt:message key="mvnforum.admin.viewmember.delete_member_nopost"/></a> <span class="warning"><b><fmt:message key="mvnforum.common.warning"/></b></span><br/>
      <% } %>
  <% } %>
      
      <span class="messageTextBold">
      <span style="color:blue"><fmt:message key="mvnforum.common.member.status"/>: </span>
        <%if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_ENABLE) {%>
      <span style="color:#008000"> <fmt:message key="mvnforum.common.member.status.enabled"/></span>
        <%} else if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_DISABLE) {%>
      <span style="color:#FF0080"> <fmt:message key="mvnforum.common.member.status.disabled"/></span>
        <%} else if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_PENDING) {%>
      <span style="color:#FF0080"> <fmt:message key="mvnforum.common.member.status.pending"/></span>
        <%} %>
      </span>
    <% if (memberBean.getMemberStatus() == MemberBean.MEMBER_STATUS_ENABLE) {%>
      <% if (memberBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_ADMIN) {%>
    (<a class="command" href="<%=urlResolver.encodeURL(request, response, "changememberstatusprocess?memberid=" + memberBean.getMemberID() + "&amp;status=" + MemberBean.MEMBER_STATUS_DISABLE + "&amp;viewmember=true&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.disable_this_member"/></a>)
    <% } %>
    <% } else {//disabled%>
    (<a class="command" href="<%=urlResolver.encodeURL(request, response, "changememberstatusprocess?memberid=" + memberBean.getMemberID() + "&amp;status=" + MemberBean.MEMBER_STATUS_ENABLE + "&amp;viewmember=true&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.admin.viewmember.enable_this_member"/></a>)
    <% } %>
    </td>
  </tr>
</table>
<br/>
<form action="<%=urlResolver.encodeURL(request, response, "viewmember", URLResolverService.ACTION_URL)%>" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "viewmember")%>
<input name="memberid" type="hidden" value="<%=memberBean.getMemberID()%>"/>
<mvn:cssrows>
<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.viewmember.message_statistics_info"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.admin.viewmember.from_date"/></td>
    <td>
      <input type="text" size="2" name="fromDay" value="<%=ParamUtil.getParameterFilter(request, "fromDay")%>" /> -
      <input type="text" size="2" name="fromMonth" value="<%=ParamUtil.getParameterFilter(request, "fromMonth")%>" /> -
      <input type="text" size="4" name="fromYear" value="<%=ParamUtil.getParameterFilter(request, "fromYear")%>" />
      (dd-mm-yyyy)
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.admin.viewmember.to_date"/></td>
    <td>
      <input type="text" size="2" name="toDay" value="<%=ParamUtil.getParameterFilter(request, "toDay")%>"/> -
      <input type="text" size="2" name="toMonth" value="<%=ParamUtil.getParameterFilter(request, "toMonth")%>"/> -
      <input type="text" size="4" name="toYear" value="<%=ParamUtil.getParameterFilter(request, "toYear")%>"/>
      (dd-mm-yyyy)
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.admin.viewmember.total_sent_message"/></td>
    <td><b><%= request.getAttribute("FromMessageCount") %></b></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.admin.viewmember.total_received_message"/></td>
    <td><b><%= request.getAttribute("ToMessageCount") %></b></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center"><input value="<fmt:message key="mvnforum.admin.viewmember.update_statistics"/>" type="submit" class="portlet-form-button"/></td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>
<mvn:cssrows>
<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.common.member.header.member_info"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td>
      <b><%=memberBean.getMemberName()%></b>
      <%if (memberBean.getMemberTitle().length() > 0) {%>(<%=EnableMVNCodeFilter.filter(memberBean.getMemberTitle())%>)<%}%>
      <%if ( onlineUserManager.isUserOnline(memberBean.getMemberName()) ) {%><font color="#008000">(Online)</font><%} else {%>(Offline)<%}%>
      <%if ( (memberBean.getMemberActivateCode()!=null) && (memberBean.getMemberActivateCode().length()>0) ) {
          if (memberBean.getMemberActivateCode().equals("activated")) {%>
            <span style="color:#008000">(<fmt:message key="mvnforum.common.member.activation_status.activated"/>)</span>
        <%} else {%>
            <span style="color:blue">(<fmt:message key="mvnforum.common.member.activation_status.pending"/>)</span>
        <%}%>
      <%} else {%>
        <span style="color:red">(<fmt:message key="mvnforum.common.member.activation_status.not_activated"/>)</span>
      <%}%>
    </td>
  </tr>
  
  <% if (MVNForumConfig.getEnableShowFirstName()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.first_name"/></td>
    <td><%=memberBean.getMemberFirstname()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowLastName()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.last_name"/></td>
    <td><%=memberBean.getMemberLastname()%></td>
  </tr>
  <% } %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
      <%  if ( (memberBean.getMemberEmail().equalsIgnoreCase(memberBean.getMemberFirstEmail()) == false) && (memberBean.getMemberFirstEmail().length() > 0) ) {%>
            (<fmt:message key="mvnforum.common.member.first_email"/>: <a href="mailto:<%=memberBean.getMemberFirstEmail()%>"><%=memberBean.getMemberFirstEmail()%></a>)
      <%  }
        }%>
    </td>
  </tr>
  <% if (MVNForumConfig.getEnableShowGender()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.gender"/></td>
    <td>
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="Male" /> <fmt:message key="mvnforum.common.member.male"/>
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="Female" /> <fmt:message key="mvnforum.common.member.female"/>
      <%}%>
    </td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowBirthday()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.birthday"/></td>
    <td>
      <% if (memberBean.getMemberBirthday() != null) {%>
         <%=onlineUser.getGMTDateFormat(memberBean.getMemberBirthday(), false)%>
      <% } %>  
    </td>
  </tr>
  <% } %>
<% if (MVNForumConfig.getEnableShowEmailVisible()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.show_email"/></td>
    <td>
      <%if ((memberBean.getMemberID()==0) || (memberBean.getMemberID()==MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>
      <fmt:message key="mvnforum.common.not_applicable"/>
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=memberBean.getMemberEmailVisible()==1 ? "yes" : "no"%>.gif" alt=""/>
      <%}%>
    </td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowNameVisible()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.name_visible"/></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=memberBean.getMemberNameVisible()==1 ? "yes" : "no"%>.gif" alt=""/></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowPostsPerPage()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.posts_per_page"/></td>
    <td><%=memberBean.getMemberPostsPerPage()%></td>
  </tr>
<% } %>

<%if ((memberBean.getMemberID()!=0) && (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_GUEST)) {%>

  <% if (MVNForumConfig.getEnableShowAddress()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.address"/></td>
    <td><%=memberBean.getMemberAddress()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowCity()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.city"/></td>
    <td><%=memberBean.getMemberCity()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowState()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.state"/></td>
    <td><%=memberBean.getMemberState()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowCountry()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.country"/></td>
    <td><%=memberBean.getMemberCountry()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowPhone()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.phone"/></td>
    <td><%=memberBean.getMemberPhone()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowMobile()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.mobile"/></td>
    <td><%=memberBean.getMemberMobile()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowFax()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.fax"/></td>
    <td><%=memberBean.getMemberFax()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowCareer()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.career"/></td>
    <td><%=memberBean.getMemberCareer()%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowHomepage()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.homepage"/></td>
    <td><a href="<%=memberBean.getMemberHomepage_http()%>"><%=memberBean.getMemberHomepage()%></a></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowCoolLink1()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.cool_link"/> 1</td>
    <td><a href="<%=memberBean.getMemberCoolLink1_http()%>"><%=memberBean.getMemberCoolLink1()%></a></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowCoolLink2()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.cool_link"/> 2</td>
    <td><a href="<%=memberBean.getMemberCoolLink2_http()%>"><%=memberBean.getMemberCoolLink2()%></a></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowYahoo()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.yahoo"/></td>
    <td><%if (memberBean.getMemberYahoo().length() > 0) {%><a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberYahoo()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" /></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberYahoo()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowAOL()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.aol"/></td>
    <td><%if (memberBean.getMemberAol().length() > 0) {%><a href="aim:goim?screenname=<%=memberBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" /></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberAol()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowICQ()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.icq"/></td>
    <td><%if (memberBean.getMemberIcq().length() > 0) {%><a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="http://web.icq.com/whitepages/online?icq=<%=memberBean.getMemberIcq()%>&img=5" border="0" alt="<%=memberBean.getMemberIcq()%>" /></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberIcq()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowMSN()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.msn"/></td>
    <td><%=memberBean.getMemberMsn()%></td>
  </tr>
  <% } %>

<%} /*if (!guest)*/%>
<% if (MVNForumConfig.getEnableShowJoinDate()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.join_date"/></td>
    <td><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberCreationDate())%></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowLastLogin()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.last_login"/></td>
    <td><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberLastLogon())%></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowModifiedDate()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.last_profile_update"/></td>
    <td><%=onlineUser.getGMTTimestampFormat(memberBean.getMemberModifiedDate())%></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowPostCount()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.post_count"/></td>
    <td><%=memberBean.getMemberPostCount()%></td>
  </tr>
<% } %>
<% if (MVNForumConfig.getEnableShowViewCount()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.view_count"/></td>
    <td><%=memberBean.getMemberViewCount()%></td>
  </tr>
<% } %>
<%if (permission.canAdminSystem()) {%>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.first_ip"/></td>
    <td><%=memberBean.getMemberFirstIP()%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.last_ip"/></td>
    <td><%=memberBean.getMemberLastIP()%></td>
  </tr>
<%}%>
<% if (MVNForumConfig.getEnableShowSignature()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.signature"/></td>
    <td><%=memberBean.getMemberSignature()%></td>
  </tr>
<% } %>
</table>
</mvn:cssrows>
<%--
@todo: show these information
MemberOption, MemberStatus, MemberMessageCount, MemberMessageOption
MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle
MemberTimeZone, MemberSkin, MemberLanguage,
MemberPhone, MemberMobile, MemberFax,
not available to public: MemberPostsPerPage
--%>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>