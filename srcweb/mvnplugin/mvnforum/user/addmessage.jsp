<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addmessage.jsp,v 1.136 2009/10/27 10:51:01 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.136 $
 - $Date: 2009/10/27 10:51:01 $
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
<%@ page import="net.myvietnam.mvncore.exception.BadInputException" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableHtmlTagFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
int parentMessageID = 0; // Get ParentMessage ID for reply mode
try {
    parentMessageID = ParamUtil.getParameterInt(request, "parent");
} catch (BadInputException e) {
    // do nothing
}

boolean isPreviewing = ParamUtil.getParameterBoolean(request, "preview");
boolean attachMore = ParamUtil.getParameterBoolean(request, "AttachMore");
boolean isQuote = ParamUtil.getParameterBoolean(request, "quote");
boolean isForward = ParamUtil.getParameterBoolean(request, "forward");
boolean sendAll = false;
boolean enablePublicMessage = MVNForumConfig.getEnablePublicMessage();
if (enablePublicMessage && permission.canAdminSystem()) {
  sendAll = ParamUtil.getParameterBoolean(request, "sendall");
}
String  replymode = ParamUtil.getParameterFilter(request, "reply");// possible value are "all" and "sender"

boolean addToSentFolder = ParamUtil.getParameterBoolean(request, "AddToSentFolder");
boolean trackMessage = ParamUtil.getParameterBoolean(request, "TrackMessage");

boolean buttonDisable = false; // it's use for Netmama

String messageToList = "";
String messageCcList = "";
String messageBccList = "";
String messageTopic = "";
String messageBody = "";
String messageIcon = "";

messageCcList = ParamUtil.getParameterSafe(request, "MessageCcList", false);
messageCcList = DisableHtmlTagFilter.filter(messageCcList);// always disable HTML

messageBccList = ParamUtil.getParameterSafe(request, "MessageBccList", false);
messageBccList = DisableHtmlTagFilter.filter(messageBccList);// always disable HTML

messageIcon = ParamUtil.getParameter(request, "MessageIcon");
messageIcon = DisableHtmlTagFilter.filter(messageIcon);// always disable HTML

messageTopic = ParamUtil.getParameter(request, "Topic");
messageTopic = DisableHtmlTagFilter.filter(messageTopic);// always disable HTML

if (isPreviewing) { // Preview mode
    messageToList = ParamUtil.getParameterSafe(request, "MessageToList", !(sendAll));
    messageToList = DisableHtmlTagFilter.filter(messageToList);// always disable HTML

    messageTopic = ParamUtil.getParameter(request, "MessageTopic", true);
    messageTopic = DisableHtmlTagFilter.filter(messageTopic);// always disable HTML

    messageBody = ParamUtil.getParameter(request, "message", true);// use message instead of MessageBody
    messageBody = DisableHtmlTagFilter.filter(messageBody);// always disable HTML
} else {
    messageToList = ParamUtil.getParameterSafe(request, "MessageToList", false);
    messageToList = DisableHtmlTagFilter.filter(messageToList);// always disable HTML
}

if (parentMessageID != 0) { // forward
    MessageBean messageBean = (MessageBean) request.getAttribute("ParentMessageBean");
    String REPLY_PREFIX = "Re: ";
    String parentMessageTopic = messageBean.getMessageTopic();
    if (parentMessageTopic.startsWith(REPLY_PREFIX)) {
        messageTopic = parentMessageTopic;
    } else {
        messageTopic = REPLY_PREFIX + parentMessageTopic;
    }
    if (replymode.equals("all")) {
        String toList = "";
        String[] toListArray = messageBean.getMessageToList().split(";");
        for (int i = 0; i < toListArray.length; i++) {
            String to = toListArray[i];
            if (onlineUser.getMemberName().equals(to) == false) {
                toList += ";" + to;
            }
        }
        messageToList += messageBean.getMessageSenderName();
        if (toList.length() > 0) {
            messageToList += toList;
        }
        
        String ccList = "";
        String[] ccListArray = messageBean.getMessageCcList().split(";");
        for (int i = 0; i < ccListArray.length; i++) {
            String cc = ccListArray[i];
            if (onlineUser.getMemberName().equals(cc) == false) {
                if (ccList.length() > 0) {
                    ccList += ";";
                }
                ccList += cc;
            }
        }
        messageCcList = ccList;
    } else if (replymode.equals("sender")) {
        messageToList = messageBean.getMessageSenderName();
    }

    if (isQuote) {
        messageBody = "[quote]" + messageBean.getMessageBody() + "[/quote]";
    }

    if (isForward) {
        String FORWARD_PREFIX = "Fwd: ";
        if (parentMessageTopic.startsWith(FORWARD_PREFIX)) {
            messageTopic = parentMessageTopic;
        } else {
            messageTopic = FORWARD_PREFIX + parentMessageTopic;
        }
    }
}
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.addmessage.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/mvncode.js"></script>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[
var usingBB = true;

function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
    document.mvnform.submitbutton.disabled=true;
    document.mvnform.previewbutton.disabled=true;
    document.mvnform.draftbutton.disabled=true;
    </mvn:servlet>  
    document.mvnform.preview.value='';
    document.mvnform.draft.value='';
    document.mvnform.submit();
  }
}

function PreviewForm() {
  if (ValidateForm()) {
    document.mvnform.draft.value=''; // draft == false
    document.mvnform.preview.value='true';
    document.mvnform.action=document.getElementById('previewUrl').value;
    <%=urlResolver.generateFormActionForJavascript(request, response, "addmessage", "mvnform")%>
    <mvn:servlet>
    document.mvnform.submitbutton.disabled=true;
    document.mvnform.previewbutton.disabled=true;
    document.mvnform.draftbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function DraftForm() {
  if (ValidateForm()) {
    document.mvnform.draft.value='true';
    document.mvnform.preview.value=''; // preview == false
    <mvn:servlet>
    document.mvnform.submitbutton.disabled=true;
    document.mvnform.previewbutton.disabled=true;
    document.mvnform.draftbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function ValidateForm() {
  if ((document.mvnform.sendall == null) || (document.mvnform.sendall.checked == false)) {
    if (isBlank(document.mvnform.MessageToList, "<fmt:message key="mvnforum.common.message.to"/>")) return false;
  }
  if (isBlank(document.mvnform.MessageTopic, "<fmt:message key="mvnforum.common.message.subject"/>")) return false;
  if (isBlank(document.mvnform.message, "<fmt:message key="mvnforum.common.message.body"/>")) return false;
  return true;
}
//]]>
</script>
<!-- Language specific string constants used in mvncode.js -->
<script type="text/javascript">
//<![CDATA[
// DO NOT ADD LINE-BREAKS BETWEEN THE "...." QUOTES!

// MINI-HELP MESSAGES
b_text = "<fmt:message key="mvnforum.user.addpost.js.message.boldtext"/>";
i_text = "<fmt:message key="mvnforum.user.addpost.js.message.italictext"/>";
u_text = "<fmt:message key="mvnforum.user.addpost.js.message.ulinetext"/>";
s_text = "<fmt:message key="mvnforum.user.addpost.js.message.stricetext"/>";

size_text = "<fmt:message key="mvnforum.user.addpost.js.message.sizetext"/>";
font_text = "<fmt:message key="mvnforum.user.addpost.js.message.fonttext"/>";
color_text = "<fmt:message key="mvnforum.user.addpost.js.message.colortext"/>";

url_text = "<fmt:message key="mvnforum.user.addpost.js.message.urltext"/>";
email_text = "<fmt:message key="mvnforum.user.addpost.js.message.emailtext"/>";
img_text = "<fmt:message key="mvnforum.user.addpost.js.message.imgtext"/>";

code_text = "<fmt:message key="mvnforum.user.addpost.js.message.codetext"/>";
php_text = "<fmt:message key="mvnforum.user.addpost.js.message.phptext"/>";
list_text = "<fmt:message key="mvnforum.user.addpost.js.message.listtext"/>";
quote_text = "<fmt:message key="mvnforum.user.addpost.js.message.quotetext"/>";

norm_text = "<fmt:message key="mvnforum.user.addpost.js.message.normaltext"/>";
enha_text = "<fmt:message key="mvnforum.user.addpost.js.message.enhancedtext"/>";

closecurrent_text = "<fmt:message key="mvnforum.user.addpost.js.message.tagclose"/>";
closeall_text = "<fmt:message key="mvnforum.user.addpost.js.message.tagcloseall"/>";

// ERROR MESSAGES
enhanced_only_text = "<fmt:message key="mvnforum.user.addpost.js.message.enhanconlytext"/>";
no_tags_text = "<fmt:message key="mvnforum.user.addpost.js.message.notagstext"/>";
already_open_text = "<fmt:message key="mvnforum.user.addpost.js.message.alreadyopentext"/>";

// TEXT FOR POP-UP PROMPTS
tag_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.taginput"/>";
font_formatter_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.fontformat"/>";

link_text_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.linktext"/>";
link_url_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.linkurl"/>";
link_email_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.email"/>";

list_type_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.listtype"/>";
list_item_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.listitem"/>";
//]]>
</script>

<script type="text/javascript">
//<![CDATA[
function showMoreEmotion() {
    document.getElementById("MoreEmotion").style.display = '';
    document.getElementById("showlink").style.display = 'none';
    document.getElementById("hidelink").style.display = '';
}
function hideMoreEmotion() {
    document.getElementById("MoreEmotion").style.display = 'none';
    document.getElementById("showlink").style.display = '';
    document.getElementById("hidelink").style.display = 'none';
}
function checkMessageIcon() {
    var messageIcon = "<%=EnableHtmlTagFilter.filter(messageIcon)%>";//we must enable html in javascript string
    var icon = document.getElementById("MessageIcon" + messageIcon);
    if (icon != null) icon.checked = true;
}
function updateReceiverOption() {
    // if send all is on, we hide the following fields: To, Cc, Bcc
    var disp = document.getElementById("sendall").checked ? 'none' : '';
    document.getElementById("To").style.display = disp;
    document.getElementById("Cc").style.display = disp;
    document.getElementById("Bcc").style.display = disp;
}
//]]>
</script>
</mvn:head>
<mvn:body onunload="document.mvnform.submitbutton.disabled=false;document.mvnform.previewbutton.disabled=false;document.mvnform.draftbutton.disabled=false;">
<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "mymessage")%>"><fmt:message key="mvnforum.user.mymessage.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.addmessage.title"/>
</div>
<br />

<%if (isPreviewing) {
    MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
<mvn:cssrows>
  <tr class="portlet-section-body">
    <td width="155" rowspan="2" valign="top" nowrap="nowrap">
      <%if (MVNForumConfig.getEnableShowGender()) {%>  
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>"/>
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>"/>
      <%}
      }%>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>" class="memberName"><%=memberBean.getMemberName()%></a>
      <br/>
      <%=(memberBean.getMemberTitle().length() > 0) ? EnableMVNCodeFilter.filter(memberBean.getMemberTitle()) : MyUtil.getMemberTitle(memberBean.getMemberPostCount())%>
      <br/>
<% if (memberBean.getMemberAvatar().length() > 0) { %>
      <div align="center"><img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>"/></div>
<% } else { %>
      <p>
<% } %>
      <br/>      
<% if (MVNForumConfig.getEnableShowCountry()) {%>      
      <%=memberBean.getMemberCountry()%><br/>
<%} %>      
<% if (MVNForumConfig.getEnableShowJoinDate()) {%>      
      <fmt:message key="mvnforum.user.viewthread.joined"/>: <b><%=onlineUser.getGMTDateFormat(memberBean.getMemberCreationDate())%></b><br/>
<%} %>
<% if (MVNForumConfig.getEnableShowPostCount()) {%>      
      <fmt:message key="mvnforum.common.member.post_count"/>: <b><%=memberBean.getMemberPostCount()%></b>
<%}%>      
    </td>
    <td valign="top">
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td width="100%" rowspan="2" valign="top" class="messageTextBold">
            <%if (messageIcon.length() > 0) {
                out.print(EnableEmotionFilter.filter(messageIcon + "&nbsp;&nbsp;", contextPath + MVNForumGlobal.EMOTION_DIR));
            } %>
            <%=MyUtil.filter(messageTopic, false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
          </td>
        </tr>
      </table>
      <hr size="1" noshade="noshade">
      <%=MyUtil.filter(messageBody, false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
      <%
      String signature = MyUtil.filter(memberBean.getMemberSignature(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
      if ( (signature.length() > 0) && MVNForumConfig.getEnableShowSignature() ) { %>
        ----------------------------------------<br/>
        <%=signature%>
      <%}%>
    </td>
  </tr>
  <tr class="portlet-section-body">
    <td>
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td width="100%">
            &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_profile.gif" border="0" alt=""/></a>
            <%if ((memberBean.getMemberEmailVisible() == 1) && MVNForumConfig.getEnableShowEmail()) {%>
              &nbsp;&nbsp;&nbsp;<a href="mailto:<%=memberBean.getMemberEmail()%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<%=memberBean.getMemberEmail()%>" title="<%=memberBean.getMemberEmail()%>"/></a>
            <%}%>
            <%if (MVNForumConfig.getEnableShowHomepage() && (memberBean.getMemberHomepage().length() > 0) && (memberBean.getMemberHomepage().equals("http://")==false)) {%>&nbsp;&nbsp;&nbsp;<a href="<%=memberBean.getMemberHomepage_http()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt="<%=memberBean.getMemberHomepage()%>" title="<%=memberBean.getMemberHomepage()%>"/></a><%}%>
            <%if (MVNForumConfig.getEnableShowYahoo() && memberBean.getMemberYahoo().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberIcq()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" title="<%=memberBean.getMemberYahoo()%>"></a><%}%>
            <%if (MVNForumConfig.getEnableShowAOL() && memberBean.getMemberAol().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="aim:goim?screenname=<%=memberBean.getMemberIcq()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>"/></a><%}%>
            <%if (MVNForumConfig.getEnableShowICQ() && memberBean.getMemberIcq().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="http://web.icq.com/whitepages/online?icq=<%=memberBean.getMemberIcq()%>&img=5" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>"/></a><%}%>
          </td>
          <td nowrap="nowrap">
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/threat.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.report"/>" title="<fmt:message key="mvnforum.user.viewthread.report"/>"/>
            &nbsp;<a href="#"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.go_top"/>" title="<fmt:message key="mvnforum.user.viewthread.go_top"/>"/></a>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
</mvn:cssrows>
</table>
<br />
<%} // isPreviewing%>

<input type="hidden" name="previewUrl" id="previewUrl" value="<%=urlResolver.encodeURL(request, response, "addmessage", URLResolverService.ACTION_URL)%>" />
<form action="<%=urlResolver.encodeURL(request, response, "addmessageprocess", URLResolverService.ACTION_URL)%>" method="post" name="mvnform">
<%=urlResolver.generateFormAction(request, response, "addmessageprocess")%>
<mvn:securitytoken />
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.addmessage.title"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="To">
    <td width="170" nowrap="nowrap"><label for="MessageToList"><fmt:message key="mvnforum.common.message.to"/></label> <span class="requiredfield">*</span></td>
    <td><input type="text" id="MessageToList" name="MessageToList" value="<%=messageToList%>" size="70" class="bginput" tabindex="1"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="Cc">
    <td width="170" nowrap="nowrap"><label for="MessageCcList"><fmt:message key="mvnforum.common.message.cc"/></label></td>
    <td><input type="text" id="MessageCcList" name="MessageCcList" value="<%=messageCcList%>" size="70" class="bginput" tabindex="1"/></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="Bcc">
    <td width="170" nowrap="nowrap"><label for="MessageBccList"><fmt:message key="mvnforum.common.message.bcc"/></label></td>
    <td><input type="text" id="MessageBccList" name="MessageBccList" value="<%=messageBccList%>" size="70" class="bginput" tabindex="1"/></td>
  </tr>
  <%if ( enablePublicMessage && permission.canAdminSystem() ) {%>
  <tr class="<mvn:cssrow/>">
    <td width="170" nowrap="nowrap"><label for="sendall"><fmt:message key="mvnforum.common.message.sendall"/></label></td>
    <td>
      <input type="checkbox" id="sendall" name="sendall" onclick="updateReceiverOption();" <%if (sendAll) {%>checked="checked"<%}%> class="noborder"/>
      <fmt:message key="mvnforum.common.message.sendall"/> (<fmt:message key="mvnforum.common.private_message.type.public_message"/>)
      <script type="text/javascript">
      //<![CDATA[
      updateReceiverOption();
      //]]>
      </script>
    </td>
  </tr>
  <%}%>
  <tr class="<mvn:cssrow/>">
    <td width="170" nowrap="nowrap"><label for="MessageTopic"><fmt:message key="mvnforum.common.message.subject"/></label> <span class="requiredfield">*</span></td>
    <td><input type="text" id="MessageTopic" name="MessageTopic" value="<%=messageTopic%>" size="70" class="bginput" tabindex="1" onkeyup="initTyper(this);"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap">
      <fmt:message key="mvnforum.user.addpost.format"/><br/>
      <input type="radio" onmouseover="stat('norm')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.normalmode"/>" accesskey="n" onclick="setmode(this.value)" checked="checked" value="0" name="mode" class="noborder"/> <fmt:message key="mvnforum.user.addpost.normal_mode"/><br/>
      <input type="radio" onmouseover="stat('enha')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.enhmode"/>" accesskey="e" onclick="setmode(this.value)" value="1" name="mode" class="noborder"/> <fmt:message key="mvnforum.user.addpost.enhanced_mode"/>
    </td>
    <td>
      <table class="noborder" cellspacing="0" cellpadding="0">
        <tr>
          <td nowrap="nowrap">
            <input class="liteoption" style="font-weight: bold;" onmouseover="stat('b')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.boldbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.bold"/>" onclick="mvncode(this.form,'b','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.bold"/> "/>
            <input class="liteoption" style="font-style: italic;" onmouseover="stat('i')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.italicbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.italic"/>" onclick="mvncode(this.form,'i','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.italic"/> "/>
            <input class="liteoption" style="text-decoration: underline;" onmouseover="stat('u')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.underlbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.underline"/>" onclick="mvncode(this.form,'u','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.underline"/> "/>
            <input class="liteoption" style="text-decoration: line-through;" onmouseover="stat('s')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.strikebtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.strike"/>" onclick="mvncode(this.form,'s','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.strike"/> "/>&nbsp;
            <select id="sizeselect" onmouseover="stat('size')" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'size')">
              <option value="0" selected="selected"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.title"/></option>
              <option value="1"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.small"/></option>
              <option value="3"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.large"/></option>
              <option value="4"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.huge"/></option>
            </select>
            <select id="fontselect" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'font')" onmouseover="stat('font')">
              <option value="0" selected="selected"><fmt:message key="mvnforum.user.addpost.dropdown.fonttype.title"/></option>
              <option value="arial">Arial</option>
              <option value="times new roman">Times</option>
              <option value="courier new">Courier</option>
              <option value="century gothic">Century</option>
            </select>
            <select id="colorselect" onmouseover="stat('color')" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'color')">
              <option value="0" selected="selected"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.title"/></option>
              <option style="COLOR: skyblue" value="skyblue"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.skyblue"/></option>
              <option style="COLOR: royalblue" value="royalblue"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.royalblue"/></option>
              <option style="COLOR: blue" value="blue"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.blue"/></option>
              <option style="COLOR: darkblue" value="darkblue"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.darkblue"/></option>
              <option style="COLOR: orange" value="orange"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.orange"/></option>
              <option style="COLOR: orangered" value="orangered"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.orangered"/></option>
              <option style="COLOR: crimson" value="crimson"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.crimson"/></option>
              <option style="COLOR: red" value="red"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.red"/></option>
              <option style="COLOR: firebrick" value="firebrick"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.firebrick"/></option>
              <option style="COLOR: darkred" value="darkred"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.darkred"/></option>
              <option style="COLOR: green" value="green"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.green"/></option>
              <option style="COLOR: limegreen" value="limegreen"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.limegreen"/></option>
              <option style="COLOR: seagreen" value="seagreen"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.seagreen"/></option>
              <option style="COLOR: deeppink" value="deeppink"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.deeppink"/></option>
              <option style="COLOR: tomato" value="tomato"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.tomato"/></option>
              <option style="COLOR: coral" value="coral"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.coral"/></option>
              <option style="COLOR: purple" value="purple"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.purple"/></option>
              <option style="COLOR: indigo" value="indigo"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.indigo"/></option>
              <option style="COLOR: burlywood" value="burlywood"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.burlywood"/></option>
              <option style="COLOR: sandybrown" value="sandybrown"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.sandybrown"/></option>
              <option style="COLOR: sienna" value="sienna"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.sienna"/></option>
              <option style="COLOR: chocolate" value="chocolate"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.chocolate"/></option>
              <option style="COLOR: teal" value="teal"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.teal"/></option>
              <option style="COLOR: silver" value="silver"><fmt:message key="mvnforum.user.addpost.dropdown.textcolor.silver"/></option>
            </select>
            <br/>
            <input class="liteoption" onmouseover="stat('url')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.hyperlinkbtn"/>" onclick="namedlink(this.form,'url')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.hyperlink"/>"/>
            <input class="liteoption" onmouseover="stat('email')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.emailbtn"/>" onclick="namedlink(this.form,'email')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.email"/>"/>
            <input class="liteoption" onmouseover="stat('img')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.imagebtn"/>" onclick="mvncode(this.form,'img','http://')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.image"/>"/> &nbsp;
            <input class="liteoption" onmouseover="stat('code')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.codebtn"/>" onclick="mvncode(this.form,'code','')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.code"/>"/>
            <input class="liteoption" onmouseover="stat('list')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.orderlistbtn"/>" onclick="dolist(this.form)" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.orderlist"/>"/>
            <input class="liteoption" onmouseover="stat('quote')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.quotebtn"/>" onclick="mvncode(this.form,'quote','')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.quote"/>"/>
            <br/>
            <input type="text" name="status" value="<fmt:message key="mvnforum.user.addpost.js.message.start"/>" size="70" class="liteoption" style="FONT-SIZE: 7pt"/>
          </td>
          <td>&nbsp;</td>
          <td class="portlet-font">
            <input class="liteoption" onmouseover="stat('closecurrent')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.tagclosebtn"/>" style="FONT-WEIGHT: bold; COLOR: red" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.tagclose"/>" onclick="closetag(this.form)" type="button" value=" x "/> <fmt:message key="mvnforum.user.addpost.tagclosebtn.text"/><br/>
            <input class="liteoption" onmouseover="stat('closeall')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.tagcloseallbtn"/>" style="FONT-WEIGHT: bold; COLOR: red" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.tagcloseall"/>" onclick="closeall(this.form)" type="button" value=" x "/> <fmt:message key="mvnforum.user.addpost.tagcloseallbtn.text"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>

  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap">
      <label for="message"><fmt:message key="mvnforum.common.message.body"/></label> <span class="requiredfield">*</span>
      <% if (MVNForumConfig.getEnableEmoticon()) { %>
      <table class="noborder" cellspacing="0" cellpadding="3" align="center">
        <tr>
          <td colspan="3" align="center" bgcolor="#F5F5F5" style="BORDER-RIGHT: 1px inset; BORDER-TOP: 1px inset; BORDER-LEFT: 1px inset; BORDER-BOTTOM: 1px inset">
            <span class="messageTextBold"><fmt:message key="mvnforum.user.addpost.smilies"/></span>
          </td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:D]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0"/></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:))]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/laughing.gif" alt="laughing" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:((]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/crying.gif" alt="crying" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/wink.gif" alt="wink" border="0"/></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:&quot;&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/blushing.gif" alt="blushing" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:p]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[B-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0"/></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:x]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:-/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0"/></a>&nbsp;</td>
          <td><a href="javascript:smilie('[&gt;:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0"/></a>&nbsp;</td>
        </tr>
        <tr>
          <td colspan="3" align="center" bgcolor="#F5F5F5" style="BORDER-RIGHT: 1px inset; BORDER-TOP: 1px inset; BORDER-LEFT: 1px inset; BORDER-BOTTOM: 1px inset">
            <span class="portlet-font"><fmt:message key="mvnforum.user.addpost.smilies_showing"/></span>
            <table>
              <tr id="showlink">
                <td align="center"><a href="javascript:showMoreEmotion()" class="messageTextBold"><fmt:message key="mvnforum.user.addpost.show_more"/></a></td>
              </tr>
              <tr id="hidelink" style="display: none;">
                <td align="center"><a href="javascript:hideMoreEmotion()" class="messageTextBold"><fmt:message key="mvnforum.user.addpost.hide_more"/></a></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <% }//enable emoticon %>
    </td>
    <td valign="top"><textarea cols="70" rows="18" id="message" name="message" tabindex="2" onkeyup="initTyper(this);storeCaret(this);" onselect="storeCaret(this);" onclick="storeCaret(this);" onmouseup="storeCaret(this);" onchange="storeCaret(this);"><%=messageBody%></textarea></td>
  </tr>

  <% if (MVNForumConfig.getEnableEmoticon()) { %>
  <tr class="<mvn:cssrow/>" id="MoreEmotion" style="display: none;">
    <td colspan="2">
      <table class="noborder" width="100%">
        <tr>
          <td width="60%">
            <table class="noborder" cellspacing="0" cellpadding="3" align="center" bgcolor="#FFFFFF">
              <tr>
                <td colspan="9" align="center" bgcolor="#F5F5F5" style="BORDER-RIGHT: 1px inset; BORDER-TOP: 1px inset; BORDER-LEFT: 1px inset; BORDER-BOTTOM: 1px inset">
                  <span class="messageTextBold"><fmt:message key="mvnforum.user.addpost.standard_smilies"/></span>
                </td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/wink.gif" alt="wink" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:D]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[;;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/batting_eyelashes.gif" alt="batting eyelashes" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:x]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:&quot;&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/blushing.gif" alt="blushing" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:p]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:*]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/kiss.gif" alt="kiss" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:O]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shock.gif" alt="shock" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[X-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angry.gif" alt="angry" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smug.gif" alt="smug" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[B-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-s]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/worried.gif" alt="worried" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&gt;:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:((]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/crying.gif" alt="crying" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:))]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/laughing.gif" alt="laughing" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/straight_face.gif" alt="straight face" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[/:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/raised_eyebrow.gif" alt="raised eyebrow" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[O:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angel.gif" alt="angel" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-B]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/nerd.gif" alt="nerd" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/talk_to_the_hand.gif" alt="talk to the hand" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[I-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sleep.gif" alt="sleep" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rolling_eyes.gif" alt="rolling eyes" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-&amp;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sick.gif" alt="sick" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-$]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shhh.gif" alt="shhh" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[[-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/not_talking.gif" alt="not talking" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:o)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/clown.gif" alt="clown" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-}]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/silly.gif" alt="silly" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[(:|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tired.gif" alt="tired" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=P~]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/drooling.gif" alt="drooling" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-?]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/thinking.gif" alt="thinking" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[#-o]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/d_oh.gif" alt="d oh" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=D&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/applause.gif" alt="applause" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&gt;:D&lt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/hugs.gif" alt="hugs" border="0"/></a>&nbsp;</td>
              </tr>
            </table>
          </td>
          <td>
            <table class="noborder" cellspacing="0" cellpadding="3" align="center" bgcolor="#FFFFFF">
              <tr>
                <td colspan="6" align="center" bgcolor="#F5F5F5" style="BORDER-RIGHT: 1px inset; BORDER-TOP: 1px inset; BORDER-LEFT: 1px inset; BORDER-BOTTOM: 1px inset">
                  <span class="messageTextBold"><fmt:message key="mvnforum.user.addpost.hidden_smilies"/></span>
                </td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:@)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/pig.gif" alt="pig" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[3:-O]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cow.gif" alt="cow" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:(|)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/monkey.gif" alt="monkey" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[~:&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/chicken.gif" alt="chicken" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[@};-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rose.gif" alt="rose" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[%%-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/good_luck.gif" alt="good luck" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[**==]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/flag.gif" alt="flag" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[(~~)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/pumpkin.gif" alt="pumpkin" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[~o)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/coffee.gif" alt="coffee" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[*-:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/idea.gif" alt="idea" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-X]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/skull.gif" alt="skull" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/alien_1.gif" alt="alien 1" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[&gt;-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/alien_2.gif" alt="alien 2" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-L]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/frustrated.gif" alt="frustrated" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&lt;):)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cowboy.gif" alt="cowboy" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[[-o&lt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/praying.gif" alt="praying" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[@-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/hypnotized.gif" alt="hypnotized" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[$-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/money_eyes.gif" alt="money eyes" border="0"/></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:-&quot;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/whistling.gif" alt="whistling" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:^o]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/liar.gif" alt="liar" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[b-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/beat_up.gif" alt="beat up" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:)&gt;-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/peace.gif" alt="peace" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[[-X]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shame_on_you.gif" alt="shame on you" border="0"/></a>&nbsp;</td>
                <td><a href="javascript:smilie('[\\:D/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/dancing.gif" alt="dancing" border="0"/></a>&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <% }//enable emoticon %>

  <% if (MVNForumConfig.getEnableEmoticon()) { %>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.message.message_icon"/></td>
    <td>
      <input type="radio" name="MessageIcon" id="MessageIconSmile" value="[:)]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconSad" value="[:(]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconBiggrin" value="[:D]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconApplause" value="[=D&gt;]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/applause.gif" alt="applause" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconCool" value="[B-)]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconRose" value="[@};-]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rose.gif" alt="rose" border="0"/>&nbsp;&nbsp;&nbsp;
      <br/>
      <input type="radio" name="MessageIcon" id="MessageIconConfused" value="[:-/]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconLove" value="[:x]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconTongue" value="[:p]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconShock" value="[:O]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shock.gif" alt="shock" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconAngry" value="[X-(]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angry.gif" alt="angry" border="0"/>&nbsp;&nbsp;&nbsp;
      <input type="radio" name="MessageIcon" id="MessageIconDevilish" value="[&gt;:)]" class="noborder"/>&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0"/>&nbsp;&nbsp;&nbsp;<br/>
      <input type="radio" name="MessageIcon" value="" checked="checked" class="noborder"/> [ <fmt:message key="mvnforum.user.addpost.use_none"/> ]
      <script type="text/javascript">
      //<![CDATA[
      checkMessageIcon();
      //]]>
      </script>
    </td>
  </tr>
  <% }//enable emoticon %>

  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.option"/></td>
    <td valign="top">
      <%if (permission.canAddMessageAttachment()) {%>
      <input type="checkbox" name="AttachMore" value="yes" class="noborder" <%if (attachMore) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.addattachment.attach_more"/><br/>
      <%}%>
      <!-- <input type="checkbox" name="TrackMessage" value="yes" class="noborder" <%if (trackMessage) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.addmessage.track_message"/><br/> -->
      <input type="checkbox" name="AddToSentFolder" value="yes" class="noborder" <%if (addToSentFolder) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.addmessage.add_to_sent_folder"/><br/>
    </td>
  </tr>
  <%Collection attachBeans = (Collection) request.getAttribute("AttachBeans");%>
  <%if (isForward && (parentMessageID != 0) && (attachBeans != null)) {%>
  <input type="hidden" name="parent" value="<%=parentMessageID%>"/>
  <input type="hidden" name="forward" value="yes"/>
  <%if (attachBeans.size() > 0) {%>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.user.addattachment.title"/></td>
    <td valign="top">
        <%
        for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
            PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
        %>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>"/>
        <a class="command" href="<%=urlResolver.encodeURL(request, response, "getpmattachment?message=" + parentMessageID + "&amp;attach=" + pmAttachBean.getPmAttachID())%>"><%=pmAttachBean.getPmAttachFilename()%></a>
        <%if (pmAttachBean.getPmAttachDesc().length() > 0) {%>(<%=MyUtil.filter(pmAttachBean.getPmAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>)<%}%><br/>
       <%}%>
    </td>
  </tr>
  <%}// if there are at least one attachments
  }//if forward %>
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
      <input type="hidden" name="preview" value=""/>
      <input type="hidden" name="draft" value=""/>
      <input type="button" name="previewbutton" value="<fmt:message key="mvnforum.common.action.preview"/>" <%= (buttonDisable ? "disabled" : "")%> onclick="javascript:PreviewForm()" class="liteoption"/>
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.addmessage.button.add_message"/>" <%= (buttonDisable ? "disabled" : "")%>  onclick="javascript:SubmitForm()" class="portlet-form-button"/>
      <input type="button" name="draftbutton" value="<fmt:message key="mvnforum.user.addmessage.button.add_to_draft_folder"/>" <%= (buttonDisable ? "disabled" : "")%> onclick="javascript:DraftForm()" class="liteoption"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" <%= (buttonDisable ? "disabled" : "")%> class="liteoption"/>
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
