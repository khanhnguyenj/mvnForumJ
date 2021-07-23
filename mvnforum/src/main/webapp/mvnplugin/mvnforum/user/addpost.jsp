<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/addpost.jsp,v 1.287 2009/11/18 08:03:40 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.287 $
 - $Date: 2009/11/18 08:03:40 $
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
<%@ page import="net.myvietnam.mvncore.interceptor.InterceptorService"%>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MyUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%--
 This page is used to do 2 actions
 1. add new post ( action=addnew ), either new thread or reply to a thread
 2. edit a post  ( action=update )
--%>
<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
int parentPostID = 0;
try {
    parentPostID = ParamUtil.getParameterInt(request, "parent");
} catch (BadInputException e) {
    // do nothing
}
boolean isQuote = ParamUtil.getParameterBoolean(request, "quote");
boolean isPreviewing = ParamUtil.getParameterBoolean(request, "preview");
boolean attachMore = ParamUtil.getParameterBoolean(request, "AttachMore");
boolean addPoll = ParamUtil.getParameterBoolean(request, "AddPoll");
boolean addFavoriteThread = ParamUtil.getParameterBoolean(request, "AddFavoriteParentThread");
boolean addWatchThread = ParamUtil.getParameterBoolean(request, "AddWatchParentThread");
boolean uploadApplet = false;
if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
    uploadApplet = ParamUtil.getParameterBoolean(request, "UploadApplet");
}    
String action = ParamUtil.getAttribute(request, "action");
if (action == null) action = "";

boolean watched = ((Boolean) request.getAttribute("isWatched")).booleanValue();

String mode;
String fullmode;
String replyTopic = "";
String postTopic = "";
String postBody = "";
String postIcon = "";
String previewUrl = "";
int threadPriority = 0;

PostBean postToEdit = null;// use when edit a post
PostBean parentPostBean = null;//use when reply to a post

String previewAction = "addpost";
if (action.equals("addnew")) {
    previewUrl = urlResolver.encodeURL(request, response, "addpost", URLResolverService.ACTION_URL);
    if (parentPostID == 0) {
        mode = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.addpost.mode.addnew");
        fullmode = mode;
        replyTopic = "";
    } else {
        mode = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.addpost.mode.reply");
        // this is the parent of the reply
        parentPostBean = (PostBean)request.getAttribute("ParentPostBean");

        String REPLY_PREFIX = "Re: ";
        String parentPostTopic = parentPostBean.getPostTopic();
        if (parentPostTopic.startsWith(REPLY_PREFIX)) {
            postTopic = parentPostTopic;
        } else {
            postTopic = REPLY_PREFIX + parentPostTopic;
        }
        if (isQuote) {
            postBody = "[quote]" + parentPostBean.getPostBody() + "[/quote]\n";
        }
        replyTopic = parentPostBean.getPostTopic();
        fullmode = mode + " : " + replyTopic;
    }
} else if (action.equals("update")) {
    previewAction = "editpost";
    previewUrl = urlResolver.encodeURL(request, response, "editpost", URLResolverService.ACTION_URL);
    mode = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.addpost.mode.update");
    postToEdit = (PostBean)request.getAttribute("PostToEdit");
    postTopic = postToEdit.getPostTopic();
    postBody = postToEdit.getPostBody();
    postIcon = postToEdit.getPostIcon();
    fullmode = mode + " : " + postTopic;
    threadPriority = ((Integer)request.getAttribute("Priority")).intValue();
    
    parentPostID = postToEdit.getParentPostID();// minhnn: this line code is added on 17 Sep, 2007, not sure if it will cause side effect in Java file or not ???
} else {
    throw new BadInputException("Cannot find the action!");
}

if (isPreviewing) {
    postTopic = ParamUtil.getParameter(request, "PostTopic", true);
    postTopic = DisableHtmlTagFilter.filter(postTopic);// always disable HTML
    //postTopic = InterceptorService.getInstance().validateContent(postTopic);// we cannot call this method because it could throw Exception

    postBody = ParamUtil.getParameter(request, "message", true);// use message instead of MessageBody
    postBody = DisableHtmlTagFilter.filter(postBody);// always disable HTML
    //postBody = InterceptorService.getInstance().validateContent(postBody);// we cannot call this method because it could throw Exception

    postIcon = ParamUtil.getParameter(request, "PostIcon");
    postIcon = DisableHtmlTagFilter.filter(postIcon);// always disable HTML
    
    if (parentPostID == 0) {
        threadPriority = ParamUtil.getParameterInt(request, "ThreadPriority");
    }
}
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <%=fullmode%></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/prettify.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
  ul.tab a {
    padding: 4px 10px 5px;
  }
</style>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/mvncode.js"></script>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prettify/prettify.js"> </script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prototype/prototype.js"></script>
<script type="text/javascript">
//<![CDATA[
var contextPath = "<%=contextPath%>";
var usingBB = true;
var firstLoad = true;
var htmlLoadTimes = 0;

function SubmitForm() {
<%if (isServlet == true) {%> 
  //tinyMCE.execCommand('mceToggleEditor',true,'message');
<%}%>
  if (ValidateForm()) {
    <mvn:servlet>
    document.mvnform.submitbutton.disabled=true;
    document.mvnform.previewbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function PreviewForm() {
  //tinyMCE.execCommand('mceToggleEditor',true,'message');
  if (ValidateForm()) {
    document.mvnform.preview.value='true';
    document.mvnform.action=document.getElementById('previewUrl').value;
    <%=urlResolver.generateFormActionForJavascript(request, response, previewAction, "mvnform")%>
    <mvn:servlet>
    document.mvnform.submitbutton.disabled=true;
    document.mvnform.previewbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function checkChooseAttachMore() {
  if ((document.getElementById("flag_UploadApplet") != null) && (document.getElementById("flag_UploadApplet").value == "true") && 
    (document.mvnform.UploadApplet.checked)) {
    alert(attachfile_screenshot_prompt);
    document.mvnform.AttachMore.checked = false;
  } else if ((document.getElementById("flag_AddPoll") != null) && (document.getElementById("flag_AddPoll").value == "true") &&
    (document.mvnform.AddPoll.checked)) {
    alert(attachfile_poll_prompt);
    document.mvnform.AttachMore.checked = false;
  }
}

function checkChooseUploadApplet() {
  if (document.getElementById("flag_UploadApplet") != null && document.getElementById("flag_UploadApplet").value == "true") {
    if (document.mvnform.AttachMore.checked) {
      alert(attachscreenshot_file_prompt);
      document.mvnform.UploadApplet.checked = false;
    } else if ((document.getElementById("flag_AddPoll") != null) && (document.getElementById("flag_AddPoll").value == "true")) {
      if (document.mvnform.AddPoll.checked) {
        alert(attachscreenshot_poll_prompt);
        document.mvnform.UploadApplet.checked = false;
      }
    }
  }
}

function checkChooseAddPoll() {
  if (document.getElementById("flag_AddPoll") != null && document.getElementById("flag_AddPoll").value == "true") {
    if (document.mvnform.AttachMore.checked) {
      alert(addpoll_file_prompt);
      document.mvnform.AddPoll.checked = false;
    } else if ((document.getElementById("flag_UploadApplet") != null) && (document.getElementById("flag_UploadApplet").value == "true")) {
      if (document.mvnform.UploadApplet.checked) {
        alert(addpoll_screenshot_prompt);
        document.mvnform.AddPoll.checked = false;
      }
    }
  }
}

function ValidateForm() {
  if (isBlank(document.mvnform.PostTopic, "<fmt:message key="mvnforum.common.post.topic"/>")) return false;
  if (usingBB) {
    if (isBlank(document.mvnform.message, "<fmt:message key="mvnforum.common.post.body"/>")) return false;
  } else if (<%=MVNForumConfig.getEnableRichTextEditor()%>) {
      var strTrimmed = trim(document.getElementById('message_ifr').contentWindow.document.body.innerHTML).stripTags();
      if (strTrimmed.length == 0) {
        alert("\"" + "<fmt:message key="mvnforum.common.post.body"/>" + "\" is a required field.");
        return false;
      }
  }
  return true;
}
<%if (MVNForumConfig.getEnableRichTextEditor()) {%>
tinyMCE.init({
    theme : "advanced",
    mode : "none", 
    editor_selector : "mceEditor", 
    relative_urls : false,
    //remove_script_host : false,
    //convert_urls : false,
    document_base_url : "<%=ParamUtil.getServerPath()%>",

    plugins : "bbcode<%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>,spellchecker<%}%>",
        
    theme_advanced_buttons1 : "bold,italic,underline,strikethrough,fontselect,fontsizeselect,forecolor,link,image,bullist,numlist<%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>,spellchecker<%}%>",
    theme_advanced_buttons2 : "",
    theme_advanced_buttons3 : "",
    theme_advanced_toolbar_location : "top",
    theme_advanced_toolbar_align : "center",
    theme_advanced_styles : "Code=codeStyle;Quote=quoteStyle",
<%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>
    spellchecker_rpc_url : "<%=ParamUtil.getServerPath()%>/mvnforum_enterprise/mvnforum/ajaxspellchecker",
    spellchecker_languages : "+English=en,Swedish=sv,Tiếng Việt=vi",
<%}%>
    content_css : "css/bbcode.css",
    entity_encoding : "raw",
    add_unload_trigger : false,
    remove_linebreaks : false,
    inline_styles : false,
    force_hex_style_colors : false,
    theme_advanced_statusbar_location : "bottom",
    theme_advanced_resizing : true,
    convert_fonts_to_spans : false
});
<%}%>
function getInitEditor() {
  var editor = "<%=(String) request.getAttribute("editorMode") %>";  
  if (editor != null && editor != "") {
    if (editor == "bbEditor") {
    //do nothing (load bbeditor as default)    
    } else if (editor == "richTextEditor" && <%=MVNForumConfig.getEnableRichTextEditor()%>) {
      loadWYSIWYGEditor();
    }
  } else {
    //cookies is not set. do nothing (load bbeditor as default)
  }
}

function setCookie(cookieName, value, expiredays) {
  var exdate = new Date();
  exdate.setDate(exdate.getDate() + expiredays);
  document.cookie = cookieName + "=" + escape(value)+ ((expiredays==null) ? "" : ";expires=" + exdate.toGMTString());
}
  
function loadBBEditor() {
  usingBB = true;
  <%if (MVNForumConfig.getEnableRichTextEditor()) {%>
  document.getElementById('wys').className='';
  tinyMCE.get('message').hide();
  document.mvnform.message.style.height = document.getElementById('message_ifr').style.height;
  document.mvnform.message.style.width = document.getElementById('message_tbl').style.width;
  <%}%>
  document.getElementById('bb').className='on';
  document.getElementById('controller').style.display = 'block';
  document.mvnform.message.focus();
  setCookie('mvnforum_editor_mode','bbEditor',30);
}
<%if (MVNForumConfig.getEnableRichTextEditor()) {%>
function loadWYSIWYGEditor() {
  usingBB = false;
  document.getElementById('bb').className='';
  document.getElementById('wys').className='on';
  document.getElementById('controller').style.display = 'none'; 
  if (firstLoad == true) {
    tinyMCE.execCommand('mceAddControl', true, 'message');
    firstLoad = false;
  } else {
    tinyMCE.get('message').show();
  }
  setCookie('mvnforum_editor_mode','richTextEditor',30);
}
<%}%>
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

attachfile_screenshot_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.attachfile_screenshot"/>";
attachfile_poll_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.attachfile_poll"/>";
attachscreenshot_file_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.attachscreenshot_file"/>";
attachscreenshot_poll_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.attachscreenshot_poll"/>";
addpoll_file_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.addpoll_file"/>";
addpoll_screenshot_prompt = "<fmt:message key="mvnforum.user.addpost.js.prompt.addpoll_screenshot"/>";

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
function checkPostIcon() {
    var iconToCheck = "<%=EnableHtmlTagFilter.filter(postIcon)%>";//we must enable html in javascript string
    var currentIcon;
    for (i = 1; i <= 12; i++) {
        currentIcon = document.getElementById("PostIcon" + i);
        if (currentIcon.value == iconToCheck) {
            currentIcon.checked = true;
            break;
        }
    }
}
//]]>
</script>
</mvn:head>
<mvn:body onload="prettyPrint();" onunload="document.mvnform.submitbutton.disabled=false;document.mvnform.previewbutton.disabled=false;">
<%@ include file="header.jsp"%>
<br/>

<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();

int forumID;
if (action.equals("addnew")) {
    if (parentPostID == 0) {// new thread
        forumID = ParamUtil.getParameterInt(request, "forum");
    } else {//reply to a post
        forumID = parentPostBean.getForumID();
    }
} else {//edit mode
    forumID = postToEdit.getForumID();
}
String forumName = forumCache.getBean(forumID).getForumName();
%>
<div class="nav center">
    <%= request.getAttribute("tree") %>
</div>
<br/>

<table width="95%" align="center">
  <tr class="portlet-font"><td>
    <form action="">
<fmt:message key="mvnforum.common.quick_go"/> &raquo; 

<%--
<select name="FastGoForum" onchange="gotoPage(this.options[this.selectedIndex].value)">
<%
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryID, permission) == false) continue; %>
        <option value=""></option>
        <option value="<%=urlResolver.encodeURL(request, response, "listforums")%>"><%=categoryBean.getCategoryName()%></option>
        <option value="">---------------------------------</option>
    <%
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) { %>
        <option value="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumBean.getForumID())%>" <%if (forumBean.getForumID() == forumID) {%>selected="selected"<%}%>>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=forumBean.getForumName()%></option>
<%      } //if
    } // for forum
}// for category %>
      </select>
--%>

<%= request.getAttribute("Result") %>
    </form>
  </td></tr>
</table>
<br/>

<%if (isPreviewing) {
    
    String prePostTopic = (String)request.getAttribute("prePostTopic");
    String prePostBody  = (String)request.getAttribute("prePostBody");
    String prePostIcon  = (String)request.getAttribute("prePostIcon");
    
    MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="portlet-section-body">
    <td width="155" rowspan="2" valign="top">
     <%if (MVNForumConfig.getEnableShowGender()) {%>  
      <%if (memberBean.getMemberGender() == 1) {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>" />
      <%} else {%>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>" />
      <%}
      }%>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>" class="memberName"><%=memberBean.getMemberName()%></a>
      <br/>
      <%=(memberBean.getMemberTitle().length() > 0) ? EnableMVNCodeFilter.filter(memberBean.getMemberTitle()) : MyUtil.getMemberTitle(memberBean.getMemberPostCount())%>
      <br/>
<% if (memberBean.getMemberAvatar().length() > 0) { %>
      <div align="center"><img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" /></div>
<% } else { %>
      <p>
<% } %>
      <br/>
      <% if (MVNForumConfig.getEnableShowCountry()) {%>      
      <%=memberBean.getMemberCountry()%><br/>
      <%}%>
      <% if (MVNForumConfig.getEnableShowJoinDate()) {%>
      <fmt:message key="mvnforum.user.viewthread.joined"/>: <b><%=onlineUser.getGMTDateFormat(memberBean.getMemberCreationDate())%></b><br/>
      <%}%>
      <% if (MVNForumConfig.getEnableShowPostCount()) {%>
      <fmt:message key="mvnforum.common.member.post_count"/>: <b><%=memberBean.getMemberPostCount()%></b>
      <%}%>
    </td>
    <td valign="top">
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td width="100%" rowspan="2" valign="top" class="messageTextBold">
            <%if (prePostIcon.length() > 0) {
                out.print(EnableEmotionFilter.filter(prePostIcon + "&nbsp;&nbsp;", contextPath + MVNForumGlobal.EMOTION_DIR));
            } %>

            <% String threadPriorityIcon = MyUtil.getThreadPriorityIcon(threadPriority);
            if ( ( (threadPriority == ThreadBean.THREAD_PRIORITY_NORMAL) && MVNForumConfig.getEnableNormalPriorityIcon() ) ||
                 ( (threadPriority == ThreadBean.THREAD_PRIORITY_HIGH) && MVNForumConfig.getEnableHighPriorityIcon() ) ) { 
            %>
                <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadPriorityIcon%>" border="0" alt="" />
            <%}%>
            <%=MyUtil.filter(prePostTopic, false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
          </td>
        </tr>
      </table>
      <hr size="1" noshade="noshade">
      <%=MyUtil.filter(prePostBody, false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
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
            &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_profile.gif" border="0" alt="" /></a>
            <% if (MVNForumConfig.getEnablePrivateMessage()) { %>&nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_pm.gif" border="0" alt="<fmt:message key="mvnforum.user.addmessage.title"/>" title="<fmt:message key="mvnforum.user.addmessage.title"/>" /></a><%}%>
            <%if ((memberBean.getMemberEmailVisible() == 1) && MVNForumConfig.getEnableShowEmail()) {%>
              &nbsp;&nbsp;&nbsp;<a href="mailto:<%=memberBean.getMemberEmail()%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<%=memberBean.getMemberEmail()%>" title="<%=memberBean.getMemberEmail()%>" /></a>
            <%}%> 
            <%if (MVNForumConfig.getEnableShowHomepage() && (memberBean.getMemberHomepage().length() > 0) && (memberBean.getMemberHomepage().equals("http://")==false)) {%>&nbsp;&nbsp;&nbsp;<a href="<%=memberBean.getMemberHomepage_http()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt="<%=memberBean.getMemberHomepage()%>" title="<%=memberBean.getMemberHomepage()%>" /></a><%}%>
            <%if (MVNForumConfig.getEnableShowYahoo() &&memberBean.getMemberYahoo().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberIcq()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" title="<%=memberBean.getMemberYahoo()%>" /></a><%}%>
            <%if (MVNForumConfig.getEnableShowAOL() && memberBean.getMemberAol().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="aim:goim?screenname=<%=memberBean.getMemberIcq()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>" /></a><%}%>
            <%if (MVNForumConfig.getEnableShowICQ() &&memberBean.getMemberIcq().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="http://web.icq.com/whitepages/online?icq=<%=memberBean.getMemberIcq()%>&img=5" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>" /></a><%}%>
            <%if ((MVNForumConfig.getEnableShowMSN()) && memberBean.getMemberMsn().length() > 0) {%>&nbsp;&nbsp;&nbsp;<%if (MVNForumConfig.getHaveInternet()) {%><img src="http://osi.techno-st.net:8000/msn/<%=memberBean.getMemberMsn()%>" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/msn.gif" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%}%><%}%>
          </td>
          <td nowrap="nowrap">
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/threat.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.report"/>" title="<fmt:message key="mvnforum.user.viewthread.report"/>" />
            &nbsp;<a href="#"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.go_top"/>" title="<fmt:message key="mvnforum.user.viewthread.go_top"/>" /></a>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<%} // isPreviewing%>

<input type="hidden" name="previewUrl" id="previewUrl" value="<%=previewUrl%>" />

<%if (action.equals("addnew")) {%>
<form action="<%=urlResolver.encodeURL(request, response, "addpostprocess", URLResolverService.ACTION_URL)%>" method="post" name="mvnform">
<%=urlResolver.generateFormAction(request, response, "addpostprocess")%>
<mvn:securitytoken />
<input type="hidden" name="forum" value="<%=ParamUtil.getParameter(request, "forum")%>" />
<input type="hidden" name="parent" value="<%=parentPostID%>" />
<%} else {//update%>
<form action="<%=urlResolver.encodeURL(request, response, "updatepost", URLResolverService.ACTION_URL)%>" method="post" name="mvnform">
<%=urlResolver.generateFormAction(request, response, "updatepost")%>
<mvn:securitytoken />
<input type="hidden" name="post" value="<%=postToEdit.getPostID()%>" />
<input type="hidden" name="parent" value="<%=parentPostID%>" />
<input type="hidden" name="offset" value="<%=ParamUtil.getParameterFilter(request, "offset")%>" />
<%}%>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><%=fullmode%></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="170" nowrap="nowrap"><label for="PostTopic"><fmt:message key="mvnforum.common.post.topic"/></label> <span class="requiredfield">*</span></td>
    <td><input type="text" id="PostTopic" name="PostTopic" value="<%=postTopic%>" size="70" class="bginput" tabindex="1" onkeyup="initTyper(this);" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap">
      <fmt:message key="mvnforum.user.addpost.format"/><br/>
      <input type="radio" onmouseover="stat('norm')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.normalmode"/>" accesskey="n" onclick="setmode(this.value)" checked="checked" value="0" name="mode" class="noborder" /> <fmt:message key="mvnforum.user.addpost.normal_mode"/><br/>
      <input type="radio" onmouseover="stat('enha')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.enhmode"/>" accesskey="e" onclick="setmode(this.value)" value="1" name="mode" class="noborder" /> <fmt:message key="mvnforum.user.addpost.enhanced_mode"/>
    </td>
    <td rowspan="2" valign="top" style="background-color: #FFFFFF;">
      <div class="tab_panel">
        <ul class="tab" style="background: url(<%=contextPath%>/mvnplugin/mvnforum/images/icon/tab-line.gif) repeat-x left bottom;">
          <li><a id="bb" href="javascript:void(0);" title="BB editor mode" class="on" onclick="javscript:loadBBEditor();">BB Editor</a></li>
          <% if (MVNForumConfig.getEnableRichTextEditor()) { %>
          <li><a id="wys" href="javascript:void(0);" title="WYSIWYG (Enhanced) mode" onclick="javscript:loadWYSIWYGEditor();">WYSIWYG Editor</a></li>
          <% } %>
        </ul>
        <div class="editor_wrapper">
          <br />
          <table cellspacing="0" cellpadding="0" class="noborder" id="controller">
            <tr>
              <td nowrap="nowrap">
                <input class="liteoption" style="font-weight: bold;" onmouseover="stat('b')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.boldbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.bold"/>" onclick="mvncode(this.form,'b','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.bold"/> " />
                <input class="liteoption" style="font-style: italic;" onmouseover="stat('i')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.italicbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.italic"/>" onclick="mvncode(this.form,'i','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.italic"/> " />
                <input class="liteoption" style="text-decoration: underline;" onmouseover="stat('u')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.underlbtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.underline"/>" onclick="mvncode(this.form,'u','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.underline"/> " />
                <input class="liteoption" style="text-decoration: line-through;" onmouseover="stat('s')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.strikebtn"/>" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.typeface.strike"/>" onclick="mvncode(this.form,'s','')" type="button" value=" <fmt:message key="mvnforum.user.addpost.button.keycaps.typeface.strike"/> " />&nbsp;
                <select id="sizeselect" name="sizeselect" onmouseover="stat('size')" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'size')">
                  <option value="0" selected="selected"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.title"/></option>
                  <option value="1"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.small"/></option>
                  <option value="3"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.large"/></option>
                  <option value="4"><fmt:message key="mvnforum.user.addpost.dropdown.fontsize.huge"/></option>
                </select>
                <select id="fontselect" name="fontselect" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'font')" onmouseover="stat('font')">
                  <option value="0" selected="selected"><fmt:message key="mvnforum.user.addpost.dropdown.fonttype.title"/></option>
                  <option value="arial">Arial</option>
                  <option value="times new roman">Times</option>
                  <option value="courier new">Courier</option>
                  <option value="century gothic">Century</option>
                </select>
                <select id="colorselect" name="colorselect" onmouseover="stat('color')" onchange="fontformat(this.form,this.options[this.selectedIndex].value,this.options[this.selectedIndex].text,'color')">
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
                <input class="liteoption" onmouseover="stat('url')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.hyperlinkbtn"/>" onclick="namedlink(this.form,'url')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.hyperlink"/>" />
                <input class="liteoption" onmouseover="stat('email')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.emailbtn"/>" onclick="namedlink(this.form,'email')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.email"/>" />
                <input class="liteoption" onmouseover="stat('img')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.imagebtn"/>" onclick="mvncode(this.form,'img','http://')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.image"/>" /> &nbsp;
                <input class="liteoption" onmouseover="stat('code')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.codebtn"/>" onclick="mvncode(this.form,'code','')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.code"/>" />
                <input class="liteoption" onmouseover="stat('list')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.orderlistbtn"/>" onclick="dolist(this.form)" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.orderlist"/>" />
                <input class="liteoption" onmouseover="stat('quote')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.quotebtn"/>" onclick="mvncode(this.form,'quote','')" type="button" value="<fmt:message key="mvnforum.user.addpost.button.keycaps.quote"/>" />
                <br/>
                <input type="text" name="status" value="<fmt:message key="mvnforum.user.addpost.js.message.start"/>" size="70" class="liteoption" style="FONT-SIZE: 7pt" />
              </td>
              <td>&nbsp;</td>
              <td class="portlet-font">
                <input class="liteoption" onmouseover="stat('closecurrent')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.tagclosebtn"/>" style="FONT-WEIGHT: bold; COLOR: red" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.tagclose"/>" onclick="closetag(this.form)" type="button" value=" x " /> <fmt:message key="mvnforum.user.addpost.tagclosebtn.text"/><br/>
                <input class="liteoption" onmouseover="stat('closeall')" title="<fmt:message key="mvnforum.user.addpost.bubblehelp.tagcloseallbtn"/>" style="FONT-WEIGHT: bold; COLOR: red" accesskey="<fmt:message key="mvnforum.user.addpost.button.accesskey.tagcloseall"/>" onclick="closeall(this.form)" type="button" value=" x " /> <fmt:message key="mvnforum.user.addpost.tagcloseallbtn.text"/><br/>
                &nbsp;&nbsp;<a href="javascript: __Open('<%=urlResolver.encodeURL(request, response, "bbcode")%>', 650, 250)" class="linkinfo"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_help.gif" alt="<fmt:message key="mvnforum.common.bbcode.title"/>" title="<fmt:message key="mvnforum.common.bbcode.title"/>" class="noborder" /></a>&nbsp;&nbsp;&nbsp;<a href="javascript: __Open('<%=urlResolver.encodeURL(request, response, "bbcode")%>', 650, 250)" class="linkinfo"><fmt:message key="mvnforum.common.bbcode.title"/></a>
              </td>
            </tr>
          </table>
          <table>
            <tr>
              <td valign="top" style="border: 0px">
                <textarea <%if (((Boolean)request.getAttribute("usePreviousTextAreaSize")).booleanValue()) { %> 
                              style="width: <%=request.getAttribute("textAreaWidth")%>px; height: <%=request.getAttribute("textAreaHeight")%>px" <%
                            } else {%> 
                              style="height:350px; width:540px;" cols="70" <%
                            }%> rows="18" name="message" id="message" class="mceEditor" tabindex="2" onkeyup="initTyper(this);storeCaret(this);" onselect="storeCaret(this);" onclick="storeCaret(this);" onmouseup="storeCaret(this);" onchange="storeCaret(this);"><%=postBody%></textarea>
              </td>  
            </tr>
          </table>  
        </div>
      </div>
    </td>
  </tr>

  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap">
      <label for="message"><fmt:message key="mvnforum.common.post.body"/></label> <span class="requiredfield">*</span>
      <% if (MVNForumConfig.getEnableEmoticon()) { %>
      <table class="noborder" cellspacing="0" cellpadding="3" align="center">
        <tr>
          <td colspan="3" align="center" bgcolor="#F5F5F5" style="BORDER-RIGHT: 1px inset; BORDER-TOP: 1px inset; BORDER-LEFT: 1px inset; BORDER-BOTTOM: 1px inset">
            <span class="messageTextBold"><fmt:message key="mvnforum.user.addpost.smilies"/></span>
          </td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:D]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0" /></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:))]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/laughing.gif" alt="laughing" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:((]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/crying.gif" alt="crying" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/wink.gif" alt="wink" border="0" /></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:&quot;&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/blushing.gif" alt="blushing" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:p]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[B-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0" /></a>&nbsp;</td>
        </tr>
        <tr align="center">
          <td><a href="javascript:smilie('[:x]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[:-/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0" /></a>&nbsp;</td>
          <td><a href="javascript:smilie('[&gt;:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0" /></a>&nbsp;</td>
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
                <td><a href="javascript:smilie('[:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/wink.gif" alt="wink" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:D]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[;;)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/batting_eyelashes.gif" alt="batting eyelashes" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:x]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:&quot;&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/blushing.gif" alt="blushing" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:p]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:*]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/kiss.gif" alt="kiss" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:O]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shock.gif" alt="shock" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[X-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angry.gif" alt="angry" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smug.gif" alt="smug" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[B-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-s]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/worried.gif" alt="worried" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&gt;:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:((]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/crying.gif" alt="crying" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:))]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/laughing.gif" alt="laughing" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/straight_face.gif" alt="straight face" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[/:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/raised_eyebrow.gif" alt="raised eyebrow" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[O:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angel.gif" alt="angel" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-B]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/nerd.gif" alt="nerd" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/talk_to_the_hand.gif" alt="talk to the hand" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[I-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sleep.gif" alt="sleep" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rolling_eyes.gif" alt="rolling eyes" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-&amp;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sick.gif" alt="sick" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-$]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shhh.gif" alt="shhh" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[[-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/not_talking.gif" alt="not talking" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:o)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/clown.gif" alt="clown" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-}]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/silly.gif" alt="silly" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[(:|]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tired.gif" alt="tired" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=P~]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/drooling.gif" alt="drooling" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-?]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/thinking.gif" alt="thinking" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[#-o]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/d_oh.gif" alt="d oh" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=D&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/applause.gif" alt="applause" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&gt;:D&lt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/hugs.gif" alt="hugs" border="0" /></a>&nbsp;</td>
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
                <td><a href="javascript:smilie('[:@)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/pig.gif" alt="pig" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[3:-O]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cow.gif" alt="cow" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:(|)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/monkey.gif" alt="monkey" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[~:&gt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/chicken.gif" alt="chicken" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[@};-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rose.gif" alt="rose" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[%%-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/good_luck.gif" alt="good luck" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[**==]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/flag.gif" alt="flag" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[(~~)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/pumpkin.gif" alt="pumpkin" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[~o)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/coffee.gif" alt="coffee" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[*-:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/idea.gif" alt="idea" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[8-X]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/skull.gif" alt="skull" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[=:)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/alien_1.gif" alt="alien 1" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[&gt;-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/alien_2.gif" alt="alien 2" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:-L]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/frustrated.gif" alt="frustrated" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[&lt;):)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cowboy.gif" alt="cowboy" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[[-o&lt;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/praying.gif" alt="praying" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[@-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/hypnotized.gif" alt="hypnotized" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[$-)]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/money_eyes.gif" alt="money eyes" border="0" /></a>&nbsp;</td>
              </tr>
              <tr align="center">
                <td><a href="javascript:smilie('[:-&quot;]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/whistling.gif" alt="whistling" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:^o]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/liar.gif" alt="liar" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[b-(]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/beat_up.gif" alt="beat up" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[:)&gt;-]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/peace.gif" alt="peace" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[[-X]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shame_on_you.gif" alt="shame on you" border="0" /></a>&nbsp;</td>
                <td><a href="javascript:smilie('[\\:D/]')"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/dancing.gif" alt="dancing" border="0" /></a>&nbsp;</td>
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
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.post.post_icons"/></td>
    <td>
      <input type="radio" name="PostIcon" id="PostIcon1" value="[:)]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" alt="smile" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon2" value="[:(]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/sad.gif" alt="sad" border="0" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon3" value="[:D]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" alt="big grin" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon4" value="[=D&gt;]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/applause.gif" alt="applause" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon5" value="[B-)]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/cool.gif" alt="cool" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon6" value="[@};-]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/rose.gif" alt="rose" border="0" />&nbsp;&nbsp;&nbsp;
      <br/>
      <input type="radio" name="PostIcon" id="PostIcon7" value="[:-/]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/confused.gif" alt="confused" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon8" value="[:x]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/love.gif" alt="love struck" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon9" value="[:p]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/tongue.gif" alt="tongue" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon10" value="[:O]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/shock.gif" alt="shock" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon11" value="[X-(]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/angry.gif" alt="angry" border="0" />&nbsp;&nbsp;&nbsp;
      <input type="radio" name="PostIcon" id="PostIcon12" value="[&gt;:)]" class="noborder" />&nbsp;<img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/devilish.gif" alt="devilish" border="0" />&nbsp;&nbsp;&nbsp;<br/>
      <input type="radio" name="PostIcon" value="" checked="checked" class="noborder" /> [ <fmt:message key="mvnforum.user.addpost.use_none"/> ]
      <script type="text/javascript">
      //<![CDATA[
      checkPostIcon();
      //]]>
      </script>
    </td>
  </tr>
  <% }//enable emoticon %>

<% //@todo: put MVNForumConfig.getEnableFavoriteThread() instead of true-s in next TR
   if ( (MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID)) ||
        ( onlineUser.isMember() && (MVNForumConfig.getEnableWatch() || true) ) ||
        (MVNForumConfig.getEnablePoll() && (permission.canAddPoll(forumID) || permission.canAddOwnThreadPoll(forumID)))
      ) {
     /* can add attachment, and/or watch and/or favorite-thread*/ %>
  <tr class="<mvn:cssrow/>">
    <td valign="top"><fmt:message key="mvnforum.common.option"/></td>
    <td valign="top">
      <% if (MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID)) { %>
      <input type="checkbox" name="AttachMore" onclick = "checkChooseAttachMore();" value="yes" class="noborder" <%if (attachMore) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.addattachment.attach_more"/><br/>
        <%if (MVNForumConfig.getEnableAppletUploadImage()) { %>
          <input type="hidden" id="flag_UploadApplet" name="flag_UploadApplet" value="true" />
          <input type="checkbox" name="UploadApplet" onclick = "checkChooseUploadApplet();" value="yes" class="noborder" <%if (uploadApplet) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.addattachment.attach_screenshot"/><br/>
        <% } %>  
      <% } %>
      <%if (action.equals("addnew") && ParamUtil.getParameter(request, "forum").equals("") == false) {%>
        <% if ((MVNForumConfig.getEnablePoll()) && 
               (permission.canAddPoll(forumID) || permission.canAddOwnThreadPoll(forumID))) { %>
          <input type="hidden" id="flag_AddPoll" name="flag_AddPoll" value="true" />
          <input type="checkbox" name="AddPoll" onclick="checkChooseAddPoll();" value="yes" class="noborder" <%if (addPoll) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.common.poll.command.add_poll"/><br/>
        <% } %>
      <% } %>
      <% if (true) { %>
      <input type="checkbox" name="AddFavoriteParentThread" value="yes" class="noborder" <%if (addFavoriteThread) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.viewthread.link.add_favorite_thread"/>
      <%-- if (!permission.isActivated()) {
           @todo: add a comment that user must activate his email first
           a class="command" href="sendactivationcode" <fmt:message key="mvnforum.user.myprofile.link.activate"/> /a
           } --%>
      <br/>
      <% } %>
      <% if (MVNForumConfig.getEnableWatch() && permission.isActivated() && (watched==false)) { %>
        <% if (MVNForumConfig.getEnableAutoWatching()) { %>           
         <input type="checkbox" name="AddWatchParentThread" value="yes" class="noborder" checked="checked" /> <fmt:message key="mvnforum.user.viewthread.link.watch_this_thread"/>      
        <% } else { %>   
         <input type="checkbox" name="AddWatchParentThread" value="yes" class="noborder" <%if (addWatchThread) {%>checked="checked"<%}%> /> <fmt:message key="mvnforum.user.viewthread.link.watch_this_thread"/>
        <% } %>
        <br/>
      <% } %>
    </td>
  </tr>
<% } %>
<%
if ( (action.equals("addnew") && parentPostID == 0) || (action.equals("update") && postToEdit.getParentPostID() == 0) ) { %>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.thread.priority"/></td>
    <td>
      <%--<input type="radio" name="ThreadPriority" <%if (threadPriority == 1) {%> checked="checked" <%}%> value="1" class="noborder" /> <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_low.gif" alt="" /><fmt:message key="mvnforum.common.thread.priority.low"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
      <input type="radio" name="ThreadPriority" <%if (threadPriority == 0) {%> checked="checked" <%}%> value="0" class="noborder" /> <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_normal.gif" alt="" /><fmt:message key="mvnforum.common.thread.priority.normal"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="ThreadPriority" <%if (threadPriority == 2) {%> checked="checked" <%}%> value="2" class="noborder" /> <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_high.gif" alt="" /><fmt:message key="mvnforum.common.thread.priority.high"/>
    </td>
  </tr>
<% } %>
<%
int threadType = ParamUtil.getParameterInt(request, "ThreadType", ThreadBean.THREAD_TYPE_DEFAULT);
if (action.equals("addnew") && parentPostID == 0 && permission.canModerateThread(forumID)) {/*choose thread type*/ %>
  <tr class="<mvn:cssrow/>">
    <td valign="top" nowrap="nowrap"><fmt:message key="mvnforum.common.thread.type"/></td>
    <td>
      <input type="radio" name="ThreadType" <%if (threadType == 0) {%> checked="checked" <%}%> value="0" class="noborder" /> <fmt:message key="mvnforum.common.thread.type.normal_thread"/><br/>
      <input type="radio" name="ThreadType" <%if (threadType == 1) {%> checked="checked" <%}%> value="1" class="noborder" /> <fmt:message key="mvnforum.common.thread.type.sticky_thread"/><br/>
      <input type="radio" name="ThreadType" <%if (threadType == 2) {%> checked="checked" <%}%> value="2" class="noborder" /> <fmt:message key="mvnforum.common.thread.type.announcement_thread"/>
      <%if (permission.canAdminSystem()) {%>
      <br/><input type="radio" name="ThreadType" <%if (threadType == 3) {%> checked="checked" <%}%> value="3" class="noborder" /> <fmt:message key="mvnforum.common.thread.type.global_announcement_thread"/>
      <%}%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center"><fmt:message key="mvnforum.user.addpost.inform_watched_thread"/></td>
  </tr>
<%}// end of choose thread type%>
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
      <input type="hidden" name="preview" value="" />
      <input type="button" name="submitbutton" value="<%=mode%>" onclick="javascript:SubmitForm()" class="portlet-form-button" />
      <input type="button" name="previewbutton" class="portlet-form-button" value="<fmt:message key="mvnforum.common.action.preview"/>" onclick="javascript:PreviewForm()" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</mvn:cssrows>  
</table>
</form>
<br/>

<%
if (action.equals("addnew") && (parentPostID != 0) ) {
%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><%=fullmode%></td>
  </tr>
  <tr class="portlet-section-body">
    <td width="20%" valign="top">
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(parentPostBean.getMemberName()))%>" class="memberName"><%=parentPostBean.getMemberName()%></a><br/>
      <fmt:message key="mvnforum.user.addpost.post_on"/>: <%=onlineUser.getGMTTimestampFormat(parentPostBean.getPostCreationDate())%>
    </td>
    <td>
    <b><%=MyUtil.filter(parentPostBean.getPostTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></b>
    <br/>
    <%=MyUtil.filter(parentPostBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
  </tr>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2" align="center"><fmt:message key="mvnforum.user.addpost.last_posts_desc"/></td>
  </tr>
  <mvn:cssrows>
<%
  Collection postBeans = (Collection)request.getAttribute("PostBeans");
  for (Iterator iterator = postBeans.iterator(); iterator.hasNext(); ) {
      PostBean postBean = (PostBean)iterator.next();
%>
  <tr class="<mvn:cssrow/>">
    <td width="20%" valign="top">
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(postBean.getMemberName()))%>" class="memberName"><%=postBean.getMemberName()%></a><br/>
      <fmt:message key="mvnforum.user.addpost.post_on"/>: <%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%>
    </td>
    <td>
      <b>
      <%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
      </b><br/>
      <%=MyUtil.filter(postBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    </td>
  </tr>
<%
  }//for
%>
  </mvn:cssrows>
</table>
<%
}// if reply to a topic, not new thread
%>
<br/>
<script type="text/javascript">
//<![CDATA[
  getInitEditor();
//]]>
</script>
<%@ include file="footer.jsp"%>
<%if (isPortlet) { %>
  <script type="text/javascript">
  //<![CDATA[
    prettyPrint();
  //]]>
  </script>
<%} %>
</mvn:body>
</mvn:html>
</fmt:bundle>