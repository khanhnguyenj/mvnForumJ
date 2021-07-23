<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/moderatependingposts.jsp,v 1.86 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.86 $
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
 - @author: Igor Manic   
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.AssertionUtil" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
ThreadBean threadBean  = (ThreadBean)request.getAttribute("ThreadBean");
PostBean firstPostBean = (PostBean)request.getAttribute("FirstPostBean");
Collection postBeans = (Collection)request.getAttribute("PostBeans");
int numberOfPosts = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
%>

<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.moderatependingposts.title"/> - <%=threadBean.getThreadTopic()%></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/> 
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  <mvn:servlet>
    document.submitform.submitbutton.disabled=true;
  </mvn:servlet>  
  document.submitform.submit();
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>
<%
ForumCache forumCache = ForumCache.getInstance();
Collection forumBeans = forumCache.getBeans();

int forumID = threadBean.getForumID();
String forumName = forumCache.getBean(forumID).getForumName();

int threadID = ParamUtil.getParameterInt(request, "thread");
AssertionUtil.doAssert(threadID == threadBean.getThreadID(), "2 threadID are not the same.");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "modcp")%>"><fmt:message key="mvnforum.user.modcp.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.forum"/>: <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID)%>"><%=forumName%></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listthreadswithpendingposts?forum=" + forumID)%>"><fmt:message key="mvnforum.user.listthreadswithpendingposts.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.moderatependingposts.title"/>: <%=threadBean.getThreadTopic()%>
</div>
<br/>

<table width="95%" align="center">
  <tr>
    <td valign="bottom">
      <span class="highlight"><fmt:message key="mvnforum.user.moderatependingposts.total_posts"/>: <%=numberOfPosts%></span>
    </td>
  </tr>
</table>

<form action="<%=urlResolver.encodeURL(request, response, "moderatependingpostsprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform">
<%=urlResolver.generateFormAction(request, response, "moderatependingpostsprocess")%>
<mvn:securitytoken />
<input type="hidden" name="thread" value="<%=threadID%>" />
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="portlet-section-header">
    <td width="155" align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.post.author"/></td>
    <td align="center">
    </td>
  </tr>
</table>

<mvn:cssrows>
<%
int i = 0;
PostBean postBean = null;
boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
for (Iterator iterator = postBeans.iterator(); iterator.hasNext(); ) {
    if (i == 0) {
        postBean = firstPostBean;
    } else {
        postBean = (PostBean)iterator.next();
    }
    MemberBean  memberBean = postBean.getMemberBean();
    AssertionUtil.doAssert((memberBean==null) || (postBean.getMemberID()==memberBean.getMemberID()), "Member info and Post info don't match!");
    i++;
    /*
    String color;
    if (postBean.getParentPostID() == 0) {
      color = "portlet-section-subheader";
    } else {
        if ((i%2) != 0) {
          color = "portlet-section-body";
        } else {
          color = "portlet-section-alternate";
        }
    }*/
%>
<a name="<%=postBean.getPostID()%>"></a>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="<mvn:cssrow/>">
    <td width="155" rowspan="3" valign="top" nowrap="nowrap">
      <%if ( (memberBean!=null) && (memberBean.getMemberID()!=0) && (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_GUEST) ) {%>
       <%if (MVNForumConfig.getEnableShowGender()) {%>  
        <%if (memberBean.getMemberGender()==1) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>" />
        <%} else {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>" />
        <%}
       }//if show gender %>
        <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>" class="memberName"><%=memberBean.getMemberName()%></a>
        <br/>
        <%=(memberBean.getMemberTitle().length() > 0) ? EnableMVNCodeFilter.filter(memberBean.getMemberTitle()) : MyUtil.getMemberTitle(memberBean.getMemberPostCount())%>
        <br/>
        <% if ((memberBean.getMemberAvatar().length() > 0) && MVNForumConfig.getEnableAvatar() ) { %>
        <div align="center"><img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" /></div>
        <% } else { %>
        <p></p>
        <% } %>
        <br/>
        <%=memberBean.getMemberCountry()%><br/>
        <fmt:message key="mvnforum.user.viewthread.joined"/>: <b><%=onlineUser.getGMTDateFormat(memberBean.getMemberCreationDate())%></b><br/>
        <fmt:message key="mvnforum.common.member.post_count"/>: <b><%=memberBean.getMemberPostCount()%></b><br/>
        <fmt:message key="mvnforum.common.member.online_status"/>:
        <%boolean invisible = memberBean.isInvisible();
          boolean online = onlineUserManager.isUserOnline(memberBean.getMemberName());
          if ( online && (!enableInvisible || !invisible)  ) {%>
          <%--
          (S)he is online now, the "online" text show only when the Invisible feature is disabled
          or his status is visible (not invisable). Otherwise, show "offline" text
          --%>
            <font color="#008000"><fmt:message key="mvnforum.common.member.online"/></font>
        <%} else if (online && permission.canAdminSystem()) {%>
            <font color="#008000"><fmt:message key="mvnforum.common.member.online.invisible_member"/></font>
        <%} else {%>
            <fmt:message key="mvnforum.common.member.offline"/>
        <%}%>
      <%} else {%>
        <%-- @todo: replace alt string in next <img> --%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>" />
        <%if (memberBean==null) {%>
          <%=MVNForumConfig.getDefaultGuestName()%>
        <%} else {%>
          <span class="memberName"><%=memberBean.getMemberName()%></span>
          <br/>
          <%if (memberBean.getMemberTitle()!=null) {%><%=EnableMVNCodeFilter.filter(memberBean.getMemberTitle())%><%}%>
          <br/>
          <%if ((memberBean.getMemberAvatar().length() > 0) && MVNForumConfig.getEnableAvatar() ) { %>
            <div align="center"><img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" /></div>
          <%}%>
        <%}%>
      <%}%>
    </td>
    <td valign="top">
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td width="100%" valign="top" class="messageTextBold">
            <% if (postBean.getParentPostID() != 0) { // replied post %>
              <a href="<%=urlResolver.encodeURL(request, response, "editpost?post=" + postBean.getPostID() + "&amp;offset=0")%>"><img src="<%=imagePath%>/button_edit.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.edit_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.edit_post"/>" /></a>&nbsp;&nbsp;
            <% } %>
            <%if (postBean.getPostIcon().length() > 0) {
                out.print(EnableEmotionFilter.filter(postBean.getPostIcon() + "&nbsp;&nbsp;", contextPath + MVNForumGlobal.EMOTION_DIR));
            }%>
            <%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
          </td>
        </tr>
      </table>
      <hr size="1" noshade="noshade" />
      <%=MyUtil.filter(postBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
      <%
      Collection attachBeans = postBean.getAttachmentBeans();
      if (attachBeans != null) {
        for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
            AttachmentBean attachBean = (AttachmentBean)attachIter.next(); %>
          ----------------------------------------<br/>
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "deleteattachment?attach=" + attachBean.getAttachID())%>">[<fmt:message key="mvnforum.common.action.delete"/>]</a>&nbsp;
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>" />
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID())%>"><%=attachBean.getAttachFilename()%></a>
          (<%=attachBean.getAttachFileSize()%> bytes)
          (<fmt:message key="mvnforum.common.attachment.download_count"/>: <%=attachBean.getAttachDownloadCount()%>)
          <%if (attachBean.getAttachDesc().length() > 0) {%>(<%=MyUtil.filter(attachBean.getAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>)<%}%><br/>
          <%if (attachBean.getAttachMimeType().startsWith("image/") && permission.canGetAttachment(forumID)) {%><img src="getattachment?attach=<%=attachBean.getAttachID()%>" alt="<%=attachBean.getAttachFilename()%>" title="<%=attachBean.getAttachFilename()%>" border="0" /><%}%>
          <br/>
        <%
        }
      } %>
      <%if (memberBean!=null) {
          String signature = MyUtil.filter(memberBean.getMemberSignature(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
          if ( (signature.length() > 0) && MVNForumConfig.getEnableShowSignature() ) { %>
            ----------------------------------------<br/>
            <%=signature%>
          <%}
        }%>

      <%if (postBean.getPostEditCount() > 0) {%>
        ----------------------------------------<br/>
        [<fmt:message key="mvnforum.user.viewthread.edit"/> <%=postBean.getPostEditCount()%> <fmt:message key="mvnforum.user.viewthread.times"/>,
        <fmt:message key="mvnforum.user.viewthread.last_edit_by"/> <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(postBean.getLastEditMemberName()))%>" class="memberName"><%=postBean.getLastEditMemberName()%></a> <fmt:message key="mvnforum.common.at"/> <%=onlineUser.getGMTTimestampFormat(postBean.getPostLastEditDate())%>]
      <%}%>
    </td>
  </tr>
  <tr class="<mvn:cssrow autoIncrease="false"/>">
    <td>
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td nowrap="nowrap">
          [<%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%>]
          <%if (permission.canAdminSystem()) {%>
            <%if (postBean.getPostEditCount() > 0) { /* has edited*/%>
              [<fmt:message key="mvnforum.common.member.first_ip"/>: <font color="red"><%=postBean.getPostCreationIP()%></font> - <fmt:message key="mvnforum.common.member.last_ip"/>: <font color="red"><%=postBean.getPostLastEditIP()%></font>]
            <%} else {/* never been edited*/%>
              [<font color="red"><%=postBean.getPostCreationIP()%></font>]
            <%}%>
          <%}%>
          </td>
          <td width="100%">
            <%if (memberBean!=null) {%>
              &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_profile.gif" border="0" alt="" /></a>
              <% if (MVNForumConfig.getEnablePrivateMessage()) { %>&nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_pm.gif" border="0" alt="<fmt:message key="mvnforum.user.addmessage.title"/>" title="<fmt:message key="mvnforum.user.addmessage.title"/>" /></a><%}%>
              <%if (memberBean.getMemberEmailVisible() == 1) {%>
                &nbsp;&nbsp;&nbsp;<a href="mailto:<%=memberBean.getMemberEmail()%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<%=memberBean.getMemberEmail()%>" title="<%=memberBean.getMemberEmail()%>" /></a>
              <%}%>
              <%if ((memberBean.getMemberHomepage().length() > 0) && (memberBean.getMemberHomepage().equals("http://")==false)) {%>&nbsp;&nbsp;&nbsp;<a href="<%=memberBean.getMemberHomepage_http()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt="<%=memberBean.getMemberHomepage()%>" title="<%=memberBean.getMemberHomepage()%>" /></a><%}%>
              <%if (memberBean.getMemberYahoo().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberYahoo()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" title="<%=memberBean.getMemberYahoo()%>" /></a><%}%>
              <%if (memberBean.getMemberAol().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="aim:goim?screenname=<%=memberBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>" /></a><%}%>
              <%if (memberBean.getMemberIcq().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="http://web.icq.com/whitepages/online?icq=<%=memberBean.getMemberIcq()%>&amp;img=5" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>" /></a><%}%>
            <%}%>
          </td>
          <td nowrap="nowrap">
            <a href="#"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.go_top"/>" title="<fmt:message key="mvnforum.user.viewthread.go_top"/>" /></a>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
<% if (postBean.getParentPostID() != 0) { // replied post %>
  <tr class="<mvn:cssrow autoIncrease="false"/>">
    <td nowrap="nowrap" class="messageTextBold"><fmt:message key="mvnforum.common.action"/>:
      <input type="radio" name="modpostaction_<%=postBean.getPostID()%>" value="approve" class="noborder" /><fmt:message key="mvnforum.common.action.approve"/> &nbsp;
      <input type="radio" name="modpostaction_<%=postBean.getPostID()%>" value="delete" class="noborder" /><fmt:message key="mvnforum.common.action.delete"/> &nbsp;
      <input type="radio" name="modpostaction_<%=postBean.getPostID()%>" value="ignore" checked="checked" class="noborder" /><fmt:message key="mvnforum.common.action.ignore"/> &nbsp;
    </td>
  </tr>
<% } %>
</table>
<% }//for %>
</mvn:cssrows>
<% if (postBeans.size() == 0) { %>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td align="center"><fmt:message key="mvnforum.user.moderatependingposts.no_pending_posts"/></td>
  </tr>
</mvn:cssrows>
</table>
<% } else { %>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="portlet-section-footer">
    <td align="center">
    <input name="submitbutton" type="button" class="portlet-form-button" value="<fmt:message key="mvnforum.user.moderatependingposts.button.moderate_pending_posts"/>" onclick="javascript:SubmitForm();" />
    <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
<% } %>
</form>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
