<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/mymessage.jsp,v 1.111 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.111 $
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

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.mymessage.title"/></mvn:title>
  <%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
  <link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.form.go.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function handleGo() {
  <mvn:servlet>
  document.form.go.disabled=true;
  </mvn:servlet>
  document.form.submit();
}
//]]>
</script>
<%@ include file="header.jsp"%>
<br/>

<%
String sort  = ParamUtil.getParameterFilter(request, "sort");
String order = ParamUtil.getParameterFilter(request, "order");
String folder = ParamUtil.getParameterFilter(request, "folder");
if (sort.length() == 0) sort = "MessageCreationDate";
if (order.length() == 0) order = "DESC";
if (folder.length() == 0) folder = MVNForumConstant.MESSAGE_FOLDER_INBOX;
%>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.mymessage.title"/> (<fmt:message key="mvnforum.common.private_message.folder"/>: <%=MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), folder)%>)
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.user.mymessage.guide"/><br/><br/>
  <fmt:message key="mvnforum.common.prompt.choose_tasks"/><br/>
  <a class="command" href="<%=urlResolver.encodeURL(request, response, "addmessage")%>"><fmt:message key="mvnforum.user.addmessage.title"/></a><br/>
  <a class="command" href="<%=urlResolver.encodeURL(request, response, "mymessagefolder")%>"><fmt:message key="mvnforum.common.messagefolder.title"/></a><br/>
</div>
<br/>

<%
Collection messageBeans = (Collection) request.getAttribute("MessageBeans");
Collection messageFolderBeans = (Collection) request.getAttribute("MessageFolderBeans");
Collection messagePublicBeans = null;
boolean enablePublicMessage = MVNForumConfig.getEnablePublicMessage();
if (enablePublicMessage) {
  messagePublicBeans = (Collection)request.getAttribute("MessagePublicBeans");
}
int memberMessagesPerPage = onlineUser.getMessagesPerPage();
int totalMessages = ((Integer) request.getAttribute("TotalMessages")).intValue();
%>
<script type="text/javascript">
//<![CDATA[
function isCheckAll(checkboxes) {
  if (checkboxes == null) return true;
  if (checkboxes.length > 1) { // more than one checkboxes
    for (i = 0; i < checkboxes.length; i++) {
      if (checkboxes[i].checked == false) return false;
    }
  } else { // only one checkbox
    if (checkboxes.checked == false) return false;
  }
  return true;
}

function checkAll(control, checkboxes) {
  if (checkboxes == null) return;
  if (checkboxes.length > 1) { // more than one checkboxes
    for (i = 0; i< checkboxes.length; i ++) {
      checkboxes[i].checked = control.checked;
    }
  } else { // only one checkbox
    checkboxes.checked = control.checked;
  }
}

function change(control, checkboxes) {
  if (isCheckAll(checkboxes)) {
    control.checked = true;
  } else {
    control.checked = false;
  }
}

function SubmitMovingForm() {
  if (ValidateMovingForm()) {
    //document.mvnform.submitbutton.disabled=true;
    document.mvnform.btnMoveFolder.value=1;
    document.mvnform.submit();
  }
}

function ValidateMovingForm() {
  if (document.mvnform.DestinationFolder.value == '<%=folder%>' ) {
    alert('<fmt:message key="mvnforum.user.mymessage.error.same_folder"/>');
    return false;
  }
  if (isBlank(document.mvnform.DestinationFolder, "<fmt:message key="mvnforum.user.mymessage.destination_folder"/>")) return false;
  return true;
}
//]]>
</script>

<table width="95%" align="center" class="portlet-font">
  <tr>
    <td valign="bottom" nowrap="nowrap" class="portlet-font">
      <form name="form" action="<%=urlResolver.encodeURL(request, response, "mymessage", URLResolverService.ACTION_URL)%>" <mvn:method/>>
        <%=urlResolver.generateFormAction(request, response, "mymessage")%>
        <input type="hidden" name="folder" value="<%=folder%>"/>
        <label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label>
        <select id="sort" name="sort">
          <option value="MessageCreationDate" <%if (sort.equals("MessageCreationDate")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.message.sent_date"/></option>
          <option value="MessageSenderName" <%if (sort.equals("MessageSenderName")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.message.sender"/></option>
          <option value="MessageTopic" <%if (sort.equals("MessageTopic")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.message.subject"/></option>
          <option value="MessageReadStatus" <%if (sort.equals("MessageReadStatus")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.message.status"/></option>
        </select>
        <label for="order"><fmt:message key="mvnforum.common.order"/></label>
        <select id="order" name="order">
          <option value="ASC" <%if (order.equals("ASC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.ascending"/></option>
          <option value="DESC" <%if (order.equals("DESC")) {%>selected="selected"<%}%>><fmt:message key="mvnforum.common.descending"/></option>
        </select>
        <input type="button" name="go" value="<fmt:message key="mvnforum.common.go"/>" onclick="javascript:handleGo();" class="liteoption"/>
      </form>
    </td>
<%
double leftWidth = ((Double)request.getAttribute("QuotaRatio")).doubleValue();
final double BAR_WIDTH = 300;
final double BAR_BORDER = 5;
String percentImage = "";
if (leftWidth <= 30) {
  percentImage = "percent_low.gif";
} else if (leftWidth <= 70) {
  percentImage = "percent_med.gif";
} else {
  percentImage = "percent_high.gif";
}
%>
    <td width="<%=BAR_WIDTH + BAR_BORDER%>" align="right">
      <table width="<%=BAR_WIDTH + BAR_BORDER%>" class="noborder" cellspacing="<%=BAR_BORDER%>" cellpadding="1" align="center" bgcolor="#AABBCC">
        <tr bgcolor="#FFFFFF">
          <td align="left" height="17" nowrap="nowrap" class="portlet-font">
            <img align="left" width="<%=Math.floor(leftWidth*(BAR_WIDTH/100))%>" height="15" src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=percentImage%>" vspace="0" hspace="0" alt=""/>
          </td>
        </tr>
        <tr><td height="12" align="center" class="portlet-msg-status"><%=Math.round(leftWidth)%>% <fmt:message key="mvnforum.common.of"/> <%=MVNForumConfig.getMaxPrivateMessages()%> <fmt:message key="mvnforum.common.private_messages"/></td></tr>
      </table>
    </td>
  </tr>
</table>
  
<pg:pager
  url="mymessage"
  items="<%= totalMessages %>"
  maxPageItems="<%= memberMessagesPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.private_messages"); %>
<%-- keep track of preference --%>
<pg:param name="sort"/>
<pg:param name="order"/>
<pg:param name="folder"/>
  
<table border="0" cellpadding="0" cellspacing="0" width="95%" align="center">
<mvn:cssrows>
  <tr valign="top">
    <td><%-- col 1 --%></td>
    <td>&nbsp;&nbsp;<%-- col 2 --%></td>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
  <tr valign="top">
    <td width="1%"><%-- col 1 --%>
      <table border="0" class="tborder" width="150" cellspacing="0" cellpadding="3" align="center">
          <tr class="portlet-section-header">
            <td colspan="2" align="left"><fmt:message key="mvnforum.common.private_message.folders"/></td>
          </tr>
<%
int messageTotal = 0;
int unreadMessageTotal = 0;
String addressBookImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/message_send.gif";
String foldersImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/message_send.gif";
for (Iterator iterator = messageFolderBeans.iterator(); iterator.hasNext(); ) {
  MessageFolderBean messageFolderBean = (MessageFolderBean)iterator.next();
  int messageCount = messageFolderBean.getMessageCount();
  int unreadMessageCount = messageFolderBean.getUnreadMessageCount();
  messageTotal += messageCount;
  unreadMessageTotal += unreadMessageCount;
  String folderName = messageFolderBean.getFolderName();
  String folderImageSource = "";
  if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_INBOX)) {
    folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_inbox.gif";
  } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
    folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_draft.gif";
  } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_TRASH)) {
    folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_trash.gif";
  } else if (folderName.equals(MVNForumConstant.MESSAGE_FOLDER_SENT)) {
    folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_sent.gif";
  } else {
    folderImageSource = contextPath + "/mvnplugin/mvnforum/images/icon/folder_default.gif";
  } %>
          <tr class="<mvn:cssrow/>">
            <td width="100%" class="portlet-font">
              <img src="<%=folderImageSource%>" alt="<%=folderName%>" title="<%=folderName%>" style="vertical-align:middle;" border="0" height="19" width="19"/>
              <%if (folder.equals(folderName)) {%><b><%}%>
              <a href="<%=urlResolver.encodeURL(request, response, "mymessage?folder=" + folderName)%>"><%=MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), folderName)%></a>
              <%if (folder.equals(folderName)) {%></b><%}%>
            </td>
            <td align="right" nowrap="nowrap" class="portlet-font">
              <%if (unreadMessageCount > 0) {%>
                <b><%=unreadMessageCount%></b>/<%=messageCount%>
              <%} else {%>
                <%=messageCount%>
              <%}%>&nbsp;
            </td>
          </tr>
<% }//for %>
          <tr class="portlet-section-footer">
            <td width="100%" align="right" class="portlet-font">
              <fmt:message key="mvnforum.common.total"/>
            </td>
            <td align="right" nowrap="nowrap" class="portlet-font">
            <%if (unreadMessageTotal > 0) {%>
              <b><%=unreadMessageTotal%></b>/<%=messageTotal%>
            <%} else {%>
              <%=messageTotal%>
            <%}%>&nbsp;
            </td>
          </tr>
      </table>
    </td><%-- end col 1 --%>
    <td><%-- col 2 --%></td>
    <td>
      <%-- col 3 --%>
      <form action="<%=urlResolver.encodeURL(request, response, "processmessage", URLResolverService.ACTION_URL)%>" method="post" name="mvnform">
      <%=urlResolver.generateFormAction(request, response, "processmessage")%>
      <mvn:securitytoken />
      <table class="tborder" width="100%" cellspacing="0" cellpadding="3" align="center">
        <tr class="portlet-section-header">
          <td width="16" align="center"><input type="checkbox" name="checkall" class="noborder" onclick="checkAll(document.forms.mvnform.elements['checkall'], document.forms.mvnform.elements['selectedmessage']);"/></td>
          <td width="16"></td>
          <td><fmt:message key="mvnforum.common.message.subject"/></td>
          <td align="center"><fmt:message key="mvnforum.common.message.sender"/></td>
          <td align="center"><fmt:message key="mvnforum.common.message.sent_date"/></td>
        </tr>
<% 
boolean hasAnAttachment = false;
boolean hasAnUnreadMessage = false;
boolean hasAReadMessage = false;
if (enablePublicMessage) {%>
        <tr class="portlet-section-subheader"><td colspan="5"><fmt:message key="mvnforum.common.private_message.type.public_message"/></td></tr>
<%for (Iterator iterator = messagePublicBeans.iterator(); iterator.hasNext(); ) {
    MessageBean messageBean = (MessageBean)iterator.next(); 
    if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) && (messageBean.getMemberID() != onlineUser.getMemberID())) continue;
 %>
        <tr class="<mvn:cssrow/>">
          <td width="16" align="center">
          <%if (permission.canAdminSystem()) {%>
            <input type="checkbox" name="selectedmessage" value="<%=messageBean.getMessageID()%>" class="noborder" onclick="change(document.forms.mvnform.elements['checkall'], document.forms.mvnform.elements['selectedmessage']);"/>
          <%}%>
          </td>
          <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt=""/></td>
          <td>
          <% if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) { %>
            [<fmt:message key="mvnforum.common.messagefolder.name.draft"/>]
          <% } %>
          <% if (messageBean.getMessageAttachCount() > 0) { 
                 hasAnAttachment = true; %>
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" alt=""/>
          <% } %>
            <%=EnableEmotionFilter.filter(messageBean.getMessageIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%>
            <a href="<%=urlResolver.encodeURL(request, response, "viewmessage?message=" + messageBean.getMessageID())%>"><%=messageBean.getMessageTopic()%></a>
          </td>
          <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(messageBean.getMessageSenderName()))%>"><%=messageBean.getMessageSenderName()%></a></td>
          <td align="center"><%=onlineUser.getGMTTimestampFormat(messageBean.getMessageCreationDate())%></td>
        </tr>
<%}//for public messages
  if (messagePublicBeans.size() == 0) { %>
        <tr class="<mvn:cssrow/>"><td colspan="5" align="center"><fmt:message key="mvnforum.user.mymessage.table.no_public_messages"/></td></tr>
<%} //if size == 0
}//enablePublicMessage%>
        <tr class="portlet-section-subheader"><td colspan="5"><fmt:message key="mvnforum.common.private_message.type.private_message"/></td></tr>
<% 
   for (Iterator iterator = messageBeans.iterator(); iterator.hasNext(); ) {
     MessageBean messageBean = (MessageBean)iterator.next(); 
%>
        <pg:item>
        <tr class="<mvn:cssrow/>">
          <td width="16" align="center"><input type="checkbox" name="selectedmessage" value="<%=messageBean.getMessageID()%>" class="noborder" onclick="change(document.forms.mvnform.elements['checkall'], document.forms.mvnform.elements['selectedmessage']);"/></td>
          <td width="16">
          <% if (messageBean.getMessageReadStatus() == MessageBean.MESSAGE_READ_STATUS_DEFAULT ) {
                hasAnUnreadMessage = true; %>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_unread.gif" border="0" alt=""/>
          <% } else {
                hasAReadMessage = true; %>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_read.gif" border="0" alt=""/>
          <% } %>
          </td>
          <td>
          <% if (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC) { %>
            <b>[<fmt:message key="mvnforum.common.private_message.type.public_message"/>]</b>
          <% } %>
          <% if (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_QUOTE) { %>
            <b>[<fmt:message key="mvnforum.common.private_message.type.quote_message"/>]</b>
          <% } %>
          <% if (messageBean.getMessageAttachCount() > 0) { 
                  hasAnAttachment = true; %>
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" alt=""/>
          <% } %>
            <%=EnableEmotionFilter.filter(messageBean.getMessageIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%>
            <a href="<%=urlResolver.encodeURL(request, response, "viewmessage?message=" + messageBean.getMessageID())%>">
          <% if (messageBean.getMessageReadStatus() == MessageBean.MESSAGE_READ_STATUS_DEFAULT ) { %>
            <b><%=messageBean.getMessageTopic()%></b>
          <% } else {%>
            <%=messageBean.getMessageTopic()%>
          <%}%>
            </a>
          </td>
          <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(messageBean.getMessageSenderName()))%>"><%=messageBean.getMessageSenderName()%></a></td>
          <td align="center"><%=onlineUser.getGMTTimestampFormat(messageBean.getMessageCreationDate())%></td>
        </tr>
        </pg:item>
<% }//for
   if (messageBeans.size() == 0) { %>
        <tr class="<mvn:cssrow/>"><td colspan="5" align="center"><fmt:message key="mvnforum.user.mymessage.table.no_messages"/></td></tr>
<% } 
   int publicSizeWithPermission = 0;
   if (permission.canAdminSystem()) {
      publicSizeWithPermission = messagePublicBeans.size();
   }
   if ((messageBeans.size() + publicSizeWithPermission) > 0) { // also fix column width bug in IE %>
        <tr class="portlet-section-footer">
          <td colspan="5">
            <input type="submit" name="btnDelete" value="<fmt:message key="mvnforum.user.addmessage.delete_message"/>" class="liteoption"/>
  <% if (messageBeans.size() > 0) { %>
            <input type="submit" name="btnUnreadMark" value="<fmt:message key="mvnforum.user.addmessage.mark_unread_message"/>" class="liteoption"/>
            <br/>
            <input type="button" name="btnMoveFolder1" value="<fmt:message key="mvnforum.user.addmessage.button.move_to_another_folder"/>" onclick="javascript:SubmitMovingForm();" class="liteoption"/>
            <input type="hidden" name="btnMoveFolder"/>
            <select name="DestinationFolder">
              <option value=""><fmt:message key="mvnforum.user.mymessage.destination_folder"/></option>
     <% for (Iterator iterator = messageFolderBeans.iterator(); iterator.hasNext(); ) {
          MessageFolderBean messageFolderBean = (MessageFolderBean)iterator.next();
          String folderName = messageFolderBean.getFolderName();
          if ( folderName.equalsIgnoreCase(folder) == false &&
               folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_SENT) == false &&
               folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) == false ) { %>
              <option value="<%=folderName%>"><%=MyUtil.getLocalizedMessageFolderName(onlineUser.getLocale(), folderName)%></option>
        <%}
        }//for %>
            </select>
  <% } // end if private size == 0 %>
          </td>
        </tr>
<% } %>
      </table>
      </form>

      <table width="100%" align="center">
        <tr>
          <td colspan="2">
            <%@ include file="inc_pager.jsp"%>
          </td>
        </tr>
      <% if (enablePublicMessage && messagePublicBeans.size() > 0) { %>
        <tr class="portlet-font">
          <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt=""/></td>
          <td><fmt:message key="mvnforum.common.legend.message.public_message"/></td>
        </tr>
      <%}%>
      <% if (hasAnUnreadMessage) { %>
        <tr class="portlet-font">
          <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_unread.gif" border="0" alt=""/></td>
          <td><fmt:message key="mvnforum.common.legend.message.unread"/></td>
        </tr>
      <%}%>
      <% if (hasAReadMessage) {%>
        <tr class="portlet-font">
          <td width="16"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/message_read.gif" border="0" alt=""/></td>
          <td><fmt:message key="mvnforum.common.legend.message.read_already"/></td>
        </tr>
      <%}%>
      <% if (hasAnAttachment) { %>  
        <tr class="portlet-font">
          <td width="16" align="center"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" alt=""/></td>
          <td><fmt:message key="mvnforum.common.legend.message.has_attachment"/></td>
        </tr>
      <%}%>
      </table>
      <br/>
    </td><%-- end col 3 --%>
  </tr>
</mvn:cssrows>
</table>
</pg:pager>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
