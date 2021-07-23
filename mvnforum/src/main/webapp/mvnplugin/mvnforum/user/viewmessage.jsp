<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/viewmessage.jsp,v 1.100 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.100 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
MessageBean messageBean = (MessageBean)request.getAttribute("MessageBean");
MemberBean memberSenderBean = (MemberBean)request.getAttribute("MemberSenderBean");
Collection messageFolderBeans = (Collection) request.getAttribute("MessageFolderBeans");
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.viewmessage.title"/> - <%=messageBean.getMessageTopic()%></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.mymessage.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.private_message.folder"/>: <a href="<%=urlResolver.encodeURL(request, response, "mymessage?folder=" + messageBean.getFolderName())%>"><%=MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), messageBean.getFolderName())%></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.viewmessage.title"/>
</div>
<br/>
<table width="95%" align="center">
  <tr class="portlet-font">
    <td>
      <fmt:message key="mvnforum.common.message.jump_to_folder"/> &raquo;
      <select name="FastGoFolder" onchange="gotoPage(this.options[this.selectedIndex].value)">
        <option value=""><fmt:message key="mvnforum.common.prompt.choose_folder"/></option>
        <%for (Iterator iterator = messageFolderBeans.iterator(); iterator.hasNext(); ) {
            MessageFolderBean messageFolderBean = (MessageFolderBean)iterator.next();
            String folderName = messageFolderBean.getFolderName();
            String localizedMessageFolderName = MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), folderName);
        %>
        <option value="<%=urlResolver.encodeURL(request, response, "mymessage?folder=" + folderName)%>"><%=localizedMessageFolderName%></option>
        <%}%>
      </select>
    </td>
  </tr>
</table>
<br/>

<%if (messageBean.getMessageType() != MessageBean.MESSAGE_TYPE_PUBLIC) {%>
<table width="60%" align="center">
  <tr class="portlet-font">
  <%if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {%>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "sendmessageprocess?message=" + messageBean.getMessageID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_send.gif" alt="<fmt:message key="mvnforum.common.message.button.send"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessageattachment?message=" + messageBean.getMessageID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_attach.gif" alt="<fmt:message key="mvnforum.common.message.button.attach"/>" border="0" /></a></td>
  <%}%>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_compose.gif" alt="<fmt:message key="mvnforum.common.message.button.add_new_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?parent=" + messageBean.getMessageID() + "&amp;reply=sender")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_reply.gif" alt="<fmt:message key="mvnforum.common.message.button.reply_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?parent=" + messageBean.getMessageID() + "&amp;quote=yes&amp;reply=sender")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_reply_quote.gif" alt="<fmt:message key="mvnforum.common.message.button.reply_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?parent=" + messageBean.getMessageID() + "&amp;reply=all")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_reply_all.gif" alt="<fmt:message key="mvnforum.common.message.button.reply_all_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?parent=" + messageBean.getMessageID() + "&amp;quote=yes&amp;reply=all")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_reply_all_quote.gif" alt="<fmt:message key="mvnforum.common.message.button.reply_all_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?parent=" + messageBean.getMessageID() + "&amp;quote=yes&amp;forward=yes")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_forward.gif" alt="<fmt:message key="mvnforum.common.message.button.forward_message"/>" border="0" /></a></td>
    <td width="11%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "deletemessageprocess?message=" + messageBean.getMessageID() + "&amp;folder=" + messageBean.getFolderName() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_trash.gif" alt="<fmt:message key="mvnforum.common.message.button.delete"/>" border="0" /></a></td>
  </tr>
  <tr class="portlet-font">
  <%if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {%>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.send"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.attach"/></td>
  <%}%>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.add_new_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.reply_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.reply_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.reply_all_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.reply_all_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.forward_message"/></td>
    <td align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.message.button.delete"/></td>
  </tr>
</table>
<%} else { // a public message %>
<%if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {%>
<table align="center">
  <tr class="portlet-font">
    <td width="33%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "sendmessageprocess?message=" + messageBean.getMessageID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_send.gif" alt="<fmt:message key="mvnforum.common.message.button.send"/>" border="0" /></a></td>
    <td width="33%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "addmessageattachment?message=" + messageBean.getMessageID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_attach.gif" alt="<fmt:message key="mvnforum.common.message.button.attach"/>" border="0" /></a></td>
    <td width="33%" align="center" nowrap="nowrap"><a href="<%=urlResolver.encodeURL(request, response, "deletemessageprocess?message=" + messageBean.getMessageID() + "&amp;folder=" + messageBean.getFolderName())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_trash.gif" alt="<fmt:message key="mvnforum.common.message.button.delete"/>" border="0" /></a></td>
  </tr>
  <tr class="portlet-font">
    <td align="center" nowrap="nowrap">&nbsp;&nbsp;<fmt:message key="mvnforum.common.message.button.send"/>&nbsp;&nbsp;</td>
    <td align="center" nowrap="nowrap">&nbsp;&nbsp;<fmt:message key="mvnforum.common.message.button.attach"/>&nbsp;&nbsp;</td>
    <td align="center" nowrap="nowrap">&nbsp;&nbsp;<fmt:message key="mvnforum.common.message.button.delete"/>&nbsp;&nbsp;</td>
  </tr>
</table>
<br/>
<%}%>
<div class="pagedesc center portlet-font">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" align="middle" alt="" />
  <fmt:message key="mvnforum.common.legend.message.public_message"/>
</div>
<%}%>
<br/>
<%if (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_QUOTE) {%>
<div class="pagedesc center warning">
  <fmt:message key="mvnforum.common.private_message.type.quote_message"/>
</div>
<br/>
<%}%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td width="155" rowspan="2" valign="top" nowrap="nowrap">
      <%if (memberSenderBean != null) {%>
        <%if (memberSenderBean.getMemberGender()==1) {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>" />
        <%} else {%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>" />
        <%}%>
        <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberSenderBean.getMemberName()))%>" class="memberName"><%=memberSenderBean.getMemberName()%></a>
        <br/>
        <%=(memberSenderBean.getMemberTitle().length() > 0) ? EnableMVNCodeFilter.filter(memberSenderBean.getMemberTitle()) : MyUtil.getMemberTitle(memberSenderBean.getMemberPostCount())%>
        <br/>
        <% if ((memberSenderBean.getMemberAvatar().length() > 0) && MVNForumConfig.getEnableAvatar() ) { %>
        <div align="center"><img src="<%=memberSenderBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" /></div>
        <% } else { %>
        <p>
        <% } %>
        <br/>
        <%=memberSenderBean.getMemberCountry()%><br/>
        <fmt:message key="mvnforum.user.viewthread.joined"/>: <b><%=onlineUser.getGMTDateFormat(memberSenderBean.getMemberCreationDate())%></b><br/>
        <%if (MVNForumConfig.getEnableShowPostCount()) { %>
          <fmt:message key="mvnforum.common.member.post_count"/>: <b><%=memberSenderBean.getMemberPostCount()%></b><br/>
        <% } %>
        <fmt:message key="mvnforum.common.member.online_status"/>:
        <%boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
          boolean invisible = memberSenderBean.isInvisible();
          boolean online = onlineUserManager.isUserOnline(memberSenderBean.getMemberName());
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
      <% } //if member has not been deleted %>
    </td>
    <td valign="top">
      <table width="100%" cellpadding="0" cellspacing="2" class="noborder portlet-font">
        <tr>
          <td align="right" valign="top" nowrap="nowrap">&nbsp;<b><fmt:message key="mvnforum.common.message.subject"/>:</b></td>
          <td width="100%">&nbsp;&nbsp;
            <%if (messageBean.getMessageIcon().length() > 0) {
              out.print(EnableEmotionFilter.filter(messageBean.getMessageIcon() + "&nbsp;&nbsp;", contextPath + MVNForumGlobal.EMOTION_DIR));
            }%>
            <b><%=MyUtil.filter(messageBean.getMessageTopic(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, false/*URL*/)%></b>
          </td>
        </tr>
        <tr>
          <td align="right" valign="top" nowrap="nowrap">&nbsp;<b><fmt:message key="mvnforum.common.message.sender"/>:</b></td>
          <td>&nbsp;&nbsp; <%=messageBean.getMessageSenderName()%></td>
        </tr>
        <tr>
          <td align="right" valign="top" nowrap="nowrap">&nbsp;<b><fmt:message key="mvnforum.common.message.sent_date"/>:</b></td>
          <td>&nbsp;&nbsp; <%=onlineUser.getGMTTimestampFormat(messageBean.getMessageCreationDate())%></td>
        </tr>
        <tr>
          <td align="right" valign="top" nowrap="nowrap">&nbsp;<b><fmt:message key="mvnforum.common.message.to"/>:</b></td>
          <td>&nbsp;&nbsp; <%=messageBean.getMessageToList()%></td>
        </tr>
      <%if (messageBean.getMessageCcList().length() > 0) {%>
        <tr>
          <td align="right" valign="top" nowrap="nowrap">&nbsp;<b><fmt:message key="mvnforum.common.message.cc"/>:</b></td>
          <td>&nbsp;&nbsp; <%=messageBean.getMessageCcList()%></td>
        </tr>
      <%}%>
      </table>
      <hr size="1" noshade="noshade" />
      <%=MyUtil.filter(messageBean.getMessageBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
      <%
      // Code to list attachments of the message
        Collection attachBeans = messageBean.getAttachmentBeans();
        if (attachBeans != null) {
            for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
      %>
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>" />
            <a class="command" href="<%=urlResolver.encodeURL(request, response, "getpmattachment?message=" + messageBean.getMessageID() + "&amp;attach=" + pmAttachBean.getPmAttachID(), URLResolverService.ACTION_URL)%>"><%=pmAttachBean.getPmAttachFilename()%></a>
            (<%=pmAttachBean.getPmAttachFileSize()%> bytes)
          <%if (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC) {%>
            (<fmt:message key="mvnforum.common.attachment.download_count"/>: <%=pmAttachBean.getPmAttachDownloadCount()%>)
          <%}%>
            <%if (pmAttachBean.getPmAttachDesc().length() > 0) {%>(<%=MyUtil.filter(pmAttachBean.getPmAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>)<%}%><br/>
            <%if (pmAttachBean.getPmAttachMimeType().startsWith("image/")) {%><img src="<%=urlResolver.encodeURL(request, response, "getpmattachment?message=" + messageBean.getMessageID() + "&attach=" + pmAttachBean.getPmAttachID(), URLResolverService.ACTION_URL)%>" alt="<%=pmAttachBean.getPmAttachFilename()%>" title="<%=pmAttachBean.getPmAttachFilename()%>" border="0" /><%}%>
            <br/>
        <%  }//for
        }//if %>
      <%
      if (memberSenderBean != null) {
         String signature = MyUtil.filter(memberSenderBean.getMemberSignature(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
         if ( (signature.length() > 0) && MVNForumConfig.getEnableShowSignature() ) { %>
            ----------------------------------------<br/>
            <%=signature%>
      <% }
      } %>
      <%if ( mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_VIEW_MESSAGE) > 0) {%>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_VIEW_MESSAGE))%>
      <%}%>
    </td>
  </tr>
  <tr class="<mvn:cssrow autoIncrease="false"/>">
    <td>
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td nowrap="nowrap">
          [<%=onlineUser.getGMTTimestampFormat(messageBean.getMessageCreationDate())%>]
          </td>
          <td width="100%">
          <%if (memberSenderBean != null) {%>
            &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberSenderBean.getMemberName()))%>"><img src="<%=imagePath%>/button_profile.gif" border="0" alt="" /></a>
            <%if (memberSenderBean.getMemberEmailVisible() == 1) {%>
              &nbsp;&nbsp;&nbsp;<a href="mailto:<%=memberSenderBean.getMemberEmail()%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<%=memberSenderBean.getMemberEmail()%>" title="<%=memberSenderBean.getMemberEmail()%>" /></a>
            <%}%>
            <%if ((memberSenderBean.getMemberHomepage().length() > 0) && (memberSenderBean.getMemberHomepage().equals("http://")==false)) {%>&nbsp;&nbsp;&nbsp;<a href="<%=memberSenderBean.getMemberHomepage_http()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt="<%=memberSenderBean.getMemberHomepage()%>" title="<%=memberSenderBean.getMemberHomepage()%>" /></a><%}%>
            <%if (memberSenderBean.getMemberYahoo().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberSenderBean.getMemberYahoo()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberSenderBean.getMemberYahoo()%>" title="<%=memberSenderBean.getMemberYahoo()%>" /></a><%}%>
            <%if (memberSenderBean.getMemberAol().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="aim:goim?screenname=<%=memberSenderBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberSenderBean.getMemberAol()%>" title="<%=memberSenderBean.getMemberAol()%>" /></a><%}%>
            <%if (memberSenderBean.getMemberIcq().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberSenderBean.getMemberIcq()%>"><img src="http://web.icq.com/whitepages/online?icq=<%=memberSenderBean.getMemberIcq()%>&amp;img=5" border="0" alt="<%=memberSenderBean.getMemberIcq()%>" title="<%=memberSenderBean.getMemberIcq()%>" /></a><%}%>
          <%}%>
          </td>
          <td nowrap="nowrap">
            &nbsp;<a href="#"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.go_top"/>" title="<fmt:message key="mvnforum.user.viewthread.go_top"/>" /></a>&nbsp;
          </td>
        </tr>          
      </table>
    </td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html> 
</fmt:bundle>
