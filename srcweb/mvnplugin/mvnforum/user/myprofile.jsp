<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/myprofile.jsp,v 1.138 2010/08/10 11:52:50 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.138 $
 - $Date: 2010/08/10 11:52:50 $
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

<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.myprofile.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.user.myprofile.title"/>
</div>

<br/>
<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
%>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.myprofile.title"/></td>
  </tr>
</table>
<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
  <tr class="pagedesc">
<% if (MVNForumConfig.getEnableAvatar() && memberDAO.isSupportUpdateAvatar()) { %>
    <td width="150" align="center" valign="middle">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "changeavatar")%>">
    <% if (memberBean.getMemberAvatar().length() > 0) { %>
        <img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.user.myprofile.has_avatar"/>" title="<fmt:message key="mvnforum.user.myprofile.has_avatar"/>"/>
    <% } else { %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/user/no_picture.gif" border="0" alt="<fmt:message key="mvnforum.user.myprofile.no_avatar"/>" title="<fmt:message key="mvnforum.user.myprofile.no_avatar"/>"/>
    <% } %>
      </a>
    </td>
<% } //enable avatar%>
    <td valign="top" nowrap="nowrap">
      <% if (memberDAO.isSupportUpdate()) { %>  
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "editmember")%>">
      <fmt:message key="mvnforum.user.editmember.title"/></a><br/>
      <% } %>
      <% if (MVNForumConfig.getEnableAvatar() && memberDAO.isSupportUpdateAvatar()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "changeavatar")%>"><fmt:message key="mvnforum.user.changeavatar.title"/></a><br/>
      <% } %>
      <% if (memberDAO.isSupportUpdateSignature()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "changesignature")%>"><fmt:message key="mvnforum.user.changesignature.title"/></a><br/>
      <%} %>
      <% if (internalUserDatabase && MVNForumConfig.getEnableLoginInfoInOpenID()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "myopenid")%>"><fmt:message key="mvnforum.user.myopenidx.title" /></a>
      <% } %>
    </td>
    <td width="50%" valign="top" nowrap="nowrap">
      <% if (internalUserDatabase) { %>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "changeemail")%>"><fmt:message key="mvnforum.user.changeemail.title"/></a><br/>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "changemypassword")%>"><fmt:message key="mvnforum.user.changemypassword.title"/></a><br/>
      <% } %>  
      <% if (MVNForumConfig.getEnableDeleteCookie()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletecookieprocess?mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.user.myprofile.link.delete_cookie"/></a><br/>
      <% } %>  
      <% if (!permission.isActivated() && internalUserDatabase) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "sendactivationcode?member=" + Encoder.encodeURL(memberBean.getMemberName()) + "&amp;email=" + memberBean.getMemberEmail())%>"><fmt:message key="mvnforum.user.myprofile.link.activate"/></a>
      (<fmt:message key="mvnforum.user.myprofile.current_activation_status"/>: 
      <%if (memberBean.getMemberActivateCode().length() > 0) {%>
        <span style="color:blue">Pending</span>
      <%} else {%> 
        <span style="color:red"><fmt:message key="mvnforum.common.member.activation_status.not_activated"/></span>
      <%}%>
      )<br/>
      <% } %>
    </td>
  </tr>
</table>
<br/>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.user.header.my_control_panel"/></td>
  </tr>
</table>
<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
  <tr class="pagedesc">
    <td valign="top" nowrap="nowrap">
      <% if (MVNForumConfig.getEnableWatch()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "mywatch")%>"><fmt:message key="mvnforum.user.mywatch.title"/></a><br/>
      <% } %>
      <% if (MVNForumConfig.getEnablePrivateMessage()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.mymessage.title"/></a><br/>
      <% } %>
      <% if (MVNForumConfig.getEnableRequestPrivateForum()) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "myprivateforums")%>"><fmt:message key="mvnforum.user.myprivateforumsx.title"/></a>
      <% } %>
    </td>
    <td valign="top" nowrap="nowrap">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "myfavoritethread")%>"><fmt:message key="mvnforum.user.myfavoritethread.title"/></a><br/>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "search?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><fmt:message key="mvnforum.user.myprofile.link.find_all_posts"/></a><br/>
    </td>
  </tr>
</table>
<br/>

<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="0">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.myprofile.prompt"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td><b><%=memberBean.getMemberName()%></b></td>
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
    <td><a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a></td>
  </tr>
  <% if (MVNForumConfig.getEnableShowGender()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.gender"/></td>
    <td>
      <%if (MVNForumConfig.getEnableShowGender()) {%>  
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>"/> <fmt:message key="mvnforum.common.member.male"/>
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>"/> <fmt:message key="mvnforum.common.member.female"/>
      <%}
      }%>
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
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.show_email"/></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=memberBean.getMemberEmailVisible()==1 ? "yes" : "no"%>.gif" alt=""/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.name_visible"/></td>
    <td><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=memberBean.getMemberNameVisible()==1 ? "yes" : "no"%>.gif" alt=""/></td>
  </tr>

<% if (MVNForumConfig.getEnableShowPostsPerPage()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.posts_per_page"/></td>
    <td><%=memberBean.getMemberPostsPerPage()%></td>
  </tr>
<% } %>
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
    <td><fmt:message key="mvnforum.common.member.yahoo" /></td>
    <td><%if (memberBean.getMemberYahoo().length() > 0) {%><a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberYahoo()%>&amp;.src=pg"><%if (MVNForumConfig.getHaveInternet()) { %><img src="http://mail.opi.yahoo.com/online?u=<%=memberBean.getMemberYahoo()%>&m=g&t=0" border="0" alt="<%=memberBean.getMemberYahoo()%>"<% } else { %><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" title="<%=memberBean.getMemberYahoo()%>"/><%} %></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberYahoo()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowAOL()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.aol" /></td>
    <td><%if (memberBean.getMemberAol().length() > 0) {%><a href="aim:goim?screenname=<%=memberBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><%if (MVNForumConfig.getHaveInternet()) {%><img src="http://big.oscar.aol.com/<%=memberBean.getMemberAol()%>?on_url=http://www.aol.com:80/aim/gr/online.gif&off_url=http://www.aol.com:80/aim/gr/offline.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>"/><%}%></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberAol()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowICQ()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.icq" /></td>
    <td><%if (memberBean.getMemberIcq().length() > 0) {%><a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icq.gif" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>"/></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberIcq()%><%}%></td>
  </tr>
  <% } %>
  <% if (MVNForumConfig.getEnableShowMSN()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.msn" /></td>
    <td><%if (memberBean.getMemberMsn().length() > 0) {%><%if (MVNForumConfig.getHaveInternet()) {%><img src="http://osi.techno-st.net:8000/msn/<%=memberBean.getMemberMsn()%>" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/msn.gif" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%}%>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberMsn()%><%}%></td>
  </tr>
  <% } %>
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
    <td><%=MyUtil.filter(memberBean.getMemberSignature(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></td>
  </tr>
<% } %>
</mvn:cssrows>
</table>
<%--
@todo: show these information
MemberOption, MemberStatus, MemberMessageCount, MemberMessageOption
MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle
MemberTimeZone, MemberSkin, MemberLanguage, MemberFax,
--%>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
