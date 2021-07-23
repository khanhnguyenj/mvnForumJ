<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/viewmember.jsp,v 1.117 2010/07/28 04:31:23 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.117 $
 - $Date: 2010/07/28 04:31:23 $
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
<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.viewmember.title"/>: <%=memberBean.getMemberName()%></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%if ( (isPollPortlet == false) && (isAlbumPortlet == false) ) {%>
<%@ include file="header.jsp"%>
<br/>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.user.viewmember.title"/>: <font color="Red"><%=memberBean.getMemberName()%></font>
</div>
<%} else {%>
  <%@ include file="header_poll.jsp"%>
<br/>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "", URLResolverService.ACTION_URL)%>"><fmt:message key="mvnforum.user.header.index"/></a>&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.user.viewmember.title"/>: <font color="Red"><%=memberBean.getMemberName()%></font>
</div>
<%} %>
<table width="95%" align="center">
<% if (MVNForumConfig.getEnableAvatar()) { %>
  <tr>
    <td align="center">
    <% if (memberBean.getMemberAvatar().length() > 0) { %>
    <img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>"/>
    <% } else { %>
    <img src="<%=contextPath%>/mvnplugin/mvnforum/images/user/no_picture.gif" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.no_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.no_avatar"/>"/>
    <% } %>
    </td>
  </tr>
<% } //enable avatar%>
  <%if ( (isPollPortlet == false) && (isAlbumPortlet == false) ) {%>
  <tr>
    <td align="center">
    <a href="<%=urlResolver.encodeURL(request, response, "search?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>" class="command"><fmt:message key="mvnforum.user.viewmember.link.find_all_posts"/></a>
    </td>
  </tr>
  <%} %>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
<mvn:cssrows>
  <%if (MVNForumConfig.getEnableShowViewCount() && MVNForumConfig.getEnableShowPostCount()) {%>
  <tr class="portlet-section-header">
    <td colspan="2" align="center">
      <fmt:message key="mvnforum.user.viewmember.has_been_viewed_and_posted">
        <fmt:param><%=memberBean.getMemberViewCount()%></fmt:param>
        <fmt:param><%=memberBean.getMemberPostCount()%></fmt:param>
      </fmt:message>
    </td>
  </tr>
  <%}%>
  <tr class="<mvn:cssrow/>">
    <td width="25%"><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td>
    <b><%=memberBean.getMemberName()%></b>
    <%if (memberBean.getMemberTitle().length() > 0) {%>
        (<%=EnableMVNCodeFilter.filter(memberBean.getMemberTitle())%>)
    <%}%>
    <%boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
      boolean invisible = memberBean.isInvisible();
      boolean online = onlineUserManager.isUserOnline(memberBean.getMemberName());
      if ( MVNForumConfig.getEnableShowOnlineStatus() && online && (!enableInvisible || !invisible)  ) {%>
        <%--
        (S)he is online now, the "online" text show only when the Invisible feature is disabled
        or his status is visible ( not invisable ). Otherwise, show "offline" text
        --%>
        <font color="#008000">
           (<fmt:message key="mvnforum.common.member.online"/>)
        </font>
      <%} else if (online && permission.canAdminSystem()) {%>
        <font color="#008000">
        (<fmt:message key="mvnforum.common.member.online.invisible_member"/>)
        </font>
      <%} else {%>
           (<fmt:message key="mvnforum.common.member.offline"/>)
      <%}%>
      <%if ( (isPollPortlet == false) && (isAlbumPortlet == false) ) {%>
        <% if (MVNForumConfig.getEnablePrivateMessage()) { %>
        <a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_pm.gif" border="0" alt="<fmt:message key="mvnforum.user.addmessage.title"/>" title="<fmt:message key="mvnforum.user.addmessage.title"/>"/></a>
        <%}%>
      <%} %>
    </td>
  </tr>
  <% if (MVNForumConfig.getEnableShowFirstName()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.first_name"/></td>
    <td><%=memberBean.getMemberFirstname()%></td>
  </tr>
  <%} %>
  <% if (MVNForumConfig.getEnableShowLastName()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.last_name"/></td>
    <td><%=memberBean.getMemberLastname()%></td>
  </tr>  
  <%} %>
  
  <% if (MVNForumConfig.getEnableShowEmail()) { %>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.email"/></td>
    <td>
  <%if (memberBean.showEmail()) { %>
    <%if (onlineUser.isGuest()) { %>
        <fmt:message key="mvnforum.common.member.email.hidden_to_guest"/>    
    <%} else {%>
        <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
    <%} %>      
  <%} else if (permission.canAdminSystem()) {//end of show email%>
      <a href="mailto:<%=memberBean.getMemberEmail()%>"><%=memberBean.getMemberEmail()%></a>
  <%} else { %>
      <fmt:message key="mvnforum.common.member.email.dont_want_to_show"/>
  <%} %>
  <% if (permission.canAdminSystem() && (memberBean.getMemberEmail().equalsIgnoreCase(memberBean.getMemberFirstEmail()) == false) && (memberBean.getMemberFirstEmail().length() > 0)) {%>
   (<fmt:message key="mvnforum.common.member.first_email"/>: <a href="mailto:<%=memberBean.getMemberFirstEmail()%>"><%=memberBean.getMemberFirstEmail()%></a>)
  <% } // let to show the first email%>
  <%if ( (isPollPortlet == false) && (isAlbumPortlet == false) ) {%>
    <a href="<%=urlResolver.encodeURL(request, response, "sendmail?ToMember=" + memberBean.getMemberName())%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<fmt:message key="mvnforum.user.sendmail.title"/>" title="<fmt:message key="mvnforum.user.sendmail.title"/>"/></a>
  <%} %>
    </td>
  </tr>
  <%} %>
  
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
      <%if (MVNForumConfig.getEnableShowBirthday()) {%>
      <% if (memberBean.getMemberBirthday() != null) {%>
         <%=onlineUser.getGMTDateFormat(memberBean.getMemberBirthday(), false)%>
      <% }
      }%>  
    </td>
  </tr>
<% } %>
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
    <td><%if (memberBean.getMemberAol().length() > 0) {%><a href="aim:goim?screenname=<%=memberBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><%if (MVNForumConfig.getHaveInternet()) {%><img src="http://big.oscar.aol.com/<%=memberBean.getMemberAol()%>?on_url=http://www.aol.com:80/aim/gr/online.gif&off_url=http://www.aol.com:80/aim/gr/offline.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>"><%}%></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberAol()%><%}%></td>
  </tr>
<% } %>            
<% if (MVNForumConfig.getEnableShowICQ()) { %>       
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.icq" /></td>
    <td><%if (memberBean.getMemberIcq().length() > 0) {%><a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><%if (MVNForumConfig.getHaveInternet()) {%> <img src="http://status.icq.com/online.gif?icq=<%=memberBean.getMemberIcq()%>&img=5" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icq.gif" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>"><%}%></a>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberIcq()%><%}%></td>
  </tr>
<% } %>              
<% if (MVNForumConfig.getEnableShowMSN()) { %>       
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnforum.common.member.msn" /></td>
    <td><%if (memberBean.getMemberMsn().length() > 0) {%><%if (MVNForumConfig.getHaveInternet()) {%><img src="http://osi.techno-st.net:8000/msn/<%=memberBean.getMemberMsn()%>" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/msn.gif" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"><%}%>&nbsp;&nbsp;&nbsp;<%=memberBean.getMemberMsn()%><%}%></td>
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
  <%} %>
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
</mvn:cssrows>  
</table>

<br/><br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
