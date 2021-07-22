<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/viewthread.jsp,v 1.393 2010/08/10 11:56:31 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.393 $
 - $Date: 2010/08/10 11:56:31 $
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

<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableMVNCodeFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="net.myvietnam.mvncore.security.SecurityUtil" %>
<%@ page import="net.myvietnam.mvncore.util.AssertionUtil" %>
<%@ page import="net.myvietnam.mvncore.util.FriendlyURLParamUtil"%>
<%@ page import="net.myvietnam.mvncore.util.GenericParamUtil"%>
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="net.myvietnam.mvncore.util.StringUtil"%>
<%@ page import="com.mvnforum.LocaleMessageUtil" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.common.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.service.MvnForumService"%>
<%@ page import="com.mvnforum.auth.OnlineUserAction" %>
<%@ page import="com.mvnforum.user.UserModuleConfig"%>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>


<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
PollBean pollBean = null;
Collection pollAnswerBeans = null;
int totalVotes = 0;
Map resultMap = null;
DecimalFormat format = null;
int ONE_HUNDRES_PERCENT_IN_PIXEL = 300;
int NUMBER_COLORS = 6;
boolean canChangeVote = false;
boolean onlyCanViewLimit = false;
boolean onlyCanViewLimitAndVote = false;
Collection yourAnswer = null;
boolean isInEdittingStatus = false;
boolean hasPoll = false;
boolean hasExpired = false;
if ((request.getAttribute("HasPollInThisThread") != null) && MVNForumConfig.getEnablePoll()) {
  hasPoll = ((Boolean)request.getAttribute("HasPollInThisThread")).booleanValue();
}
if (hasPoll) {
  //This thread has a poll
  pollBean = (PollBean) request.getAttribute("PollBean");
  Timestamp endDate = pollBean.getPollEndDate();
  Timestamp startDate = pollBean.getPollStartDate();
  Timestamp now = DateUtil.getCurrentGMTTimestamp();
  if ((endDate.compareTo(startDate) > 0) && (endDate.compareTo(now) <= 0)) {
    hasExpired = true;
  }
  if (request.getAttribute("PollIsInEdittingStatus") != null) {
    isInEdittingStatus = true;
  } else if (request.getAttribute("ViewPollResult") != null) {
      if (request.getAttribute("CanChangeVote") != null) {
          canChangeVote = true;
      }
      //only show poll result
      format = new DecimalFormat("##.##");
      pollAnswerBeans = (Collection) request.getAttribute("PollAnswerBeans");
      totalVotes = ((Integer) request.getAttribute("TotalVotes")).intValue();
      resultMap = (HashMap) request.getAttribute("ResultMap");
  } else {
      if (request.getAttribute("OnlyCanViewLimit") != null) {
          onlyCanViewLimit = true;
      }
      if (request.getAttribute("OnlyCanViewLimitAndVote") != null) {
          onlyCanViewLimitAndVote = true;
          yourAnswer = (ArrayList)request.getAttribute("YourAnswer");
      }
      pollAnswerBeans = (Collection) request.getAttribute("PollAnswerBeans");
  }
} 

ThreadBean threadBean = (ThreadBean)request.getAttribute("ThreadBean");
Collection postBeans = (Collection)request.getAttribute("PostBeans");
int numberOfPosts = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
int previousTopic = ((Integer)request.getAttribute("PreviousTopic")).intValue();
int nextTopic     = ((Integer)request.getAttribute("NextTopic")).intValue();
int pendingPostCount   = ((Integer)request.getAttribute("PendingPostCount")).intValue();
int memberPostsPerPage = onlineUser.getPostsPerPage();

PostBean lastPostBean = (PostBean)request.getAttribute("lastpostBean");
String replyTopic = "";
String postTopic = "";
String postBody = "";

String lastPostTopic = lastPostBean.getPostTopic();
if (lastPostTopic.indexOf("Re: ") == -1) {
    lastPostTopic = "Re: " + lastPostTopic;
}

boolean watched = false;
if (MVNForumConfig.getEnableEasyWatching()) {
  watched = ((Boolean) request.getAttribute("isWatched")).booleanValue();
}

String highlightKey = (String) request.getAttribute("HighlightKey");
boolean _highlightKey = highlightKey.length() > 0;
%>

<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.viewthread.title"/> - <%=threadBean.getThreadTopic()%></mvn:title>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/domLib.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/domMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prettify/prettify.js"></script>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/searchhi.js"></script>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/menu.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/prettify.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.mvnform.submitbutton.disabled=true;
      document.mvnform.previewbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function ValidateForm() {
  document.mvnform.message.value = document.mvnform.input.value;
  if (isBlank(document.mvnform.input, "<fmt:message key="mvnforum.common.post.body"/>")) return false;
  return true;
}

function PreviewForm() {
  if (ValidateForm()) {
    document.mvnform.preview.value='true';
    document.mvnform.action=document.getElementById('previewUrl').value;
    <%=urlResolver.generateFormActionForJavascript(request, response, "addpost", "mvnform")%>
    <mvn:servlet>
      document.mvnform.submitbutton.disabled=true;
      document.mvnform.previewbutton.disabled=true;
    </mvn:servlet>
    document.mvnform.submit();
  }
}

function QuickReply(parentId) {
  var topic = document.getElementById("posttopic_" + parentId).innerHTML;
  if (document.getElementById("input")) {
    document.getElementById("input").focus();
    document.getElementById("quickreplymessage").innerHTML = "<fmt:message key="mvnforum.user.viewthread.quickreply_to_post"/> [" + parentId + ", " + topic + "]";
  }
  if (topic.indexOf("Re: ") == -1) {
    topic = "Re: " + topic;
  }
  document.mvnform.parent.value=parentId;
  document.mvnform.PostTopic.value=topic;
}

<%if (MVNForumConfig.getEnablePoll()) {%>
//handle vote poll form
function SubmitPollVoteForm() {
  if (ValidatePollVoteForm()) {
    <mvn:servlet>
      document.pollvoteform.pollvotebutton.disabled=true;
    </mvn:servlet>  
    document.pollvoteform.submit();
  }
}

function ValidatePollVoteForm() {
  document.getElementById("pollvotebutton").disabled = true;
  var params = "";
  var inputArray = document.getElementsByTagName("input");
  
  if (inputArray != null) {
    var youhaschecked = false;
    for (var i = 0; i < inputArray.length; i++) {
      if ((inputArray[i] != null) && (inputArray[i].getAttribute("name") != null )) {
        if (inputArray[i].getAttribute("name").indexOf("option_") != -1) {
          if (inputArray[i].checked) {
            youhaschecked = true;
            //inputArray[i].getAttribute("name"): option_{0,1,2,...} 
            //inputArray[i].getAttribute("value"): ${pollanswerid}
            //your_opinion_${pollanswerid}
            
            pollanswerid = inputArray[i].getAttribute("value");
            if (document.getElementById("your_opinion_" + pollanswerid) != null) {
              youropinion = trim(document.getElementById("your_opinion_" + pollanswerid).value);
              if (youropinion.length == 0) {
                alert("<fmt:message key="mvnforum.user.votepollx.other_opinion.must_enter_your_opinion"/>");
                document.getElementById("pollvotebutton").disabled = false;
                return false;
              }  
            }            
          }
        }
      }
    }
  }
  if (youhaschecked == true) {
    return true;
  } else {
      alert("<fmt:message key="mvnforum.user.votepollx.other_opinion.must_choose_your_option"/>");
      document.getElementById("pollvotebutton").disabled = false;
      return false;
  }  
}

function ViewPollResult(viewresulturl) {
  showDialog(viewresulturl, 450, 170);
}

function showDialog(url, width, height) {
  return showWindow(url, false, false, true, false, false, false, true, true, width, height, 0, 0);
}

function showWindow(url, isStatus, isResizeable, isScrollbars, isToolbar, isLocation, isFullscreen, isTitlebar, isCentered, width, height, top, left) {
  if (isCentered) {
    top = (screen.height - height) / 2;
    left = (screen.width - width) / 2;
  }

  open(url, 'Result', 'status=' + (isStatus ? 'yes' : 'no') + ','
  + 'resizable=' + (isResizeable ? 'yes' : 'no') + ','
  + 'scrollbars=' + (isScrollbars ? 'yes' : 'no') + ','
  + 'toolbar=' + (isToolbar ? 'yes' : 'no') + ','
  + 'location=' + (isLocation ? 'yes' : 'no') + ','
  + 'fullscreen=' + (isFullscreen ? 'yes' : 'no') + ','
  + 'titlebar=' + (isTitlebar ? 'yes' : 'no') + ','
  + 'height=' + height + ',' + 'width=' + width + ','
  + 'top=' + top + ',' + 'left=' + left);
}

<%}// end enable poll%>  

function handleUnload() {
  if (document.mvnform) {
    if (document.mvnform.submitbutton) {
      document.mvnform.submitbutton.disabled=false;
    }
    if (document.mvnform.previewbutton) {
      document.mvnform.previewbutton.disabled=false;
    }
  }    
<%if (MVNForumConfig.getEnablePoll()) {%>
  if (document.pollvoteform && document.pollvoteform.pollvotebutton) {
    document.pollvoteform.pollvotebutton.disabled=false;
  }    
<%}// end enable poll%>  
}
function changeActionForm(actionName) {
  //document.pollvoteform.action=actionName;
  document.write(location.href);
  //document.pollvoteform.submit();
}
//]]>
</script>
</mvn:head>
<mvn:body onload="prettyPrint()" onunload="handleUnload()">
<%@ include file="header.jsp"%>
<br/>

<%
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();

int forumID = threadBean.getForumID();
ForumBean currentForumBean = forumCache.getBean(forumID);
String forumName = currentForumBean.getForumName();

int threadID = ((Integer)request.getAttribute("thread")).intValue();
AssertionUtil.doAssert(threadID == threadBean.getThreadID(), "2 threadID are not the same.");
%>

<pg:pager
  url="viewthread"
  items="<%= numberOfPosts %>"
  maxPageItems="<%= memberPostsPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">

<%-- keep track of preference --%>
<pg:param name="thread" value="<%=String.valueOf(threadID)%>"/>
<% 
String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.posts");
  if (_highlightKey) {
%>
<pg:param name="hl" value="<%=Encoder.encodeURL(highlightKey)%>"/>
<%}%>

<%
// current offset, use to remember offset state after we update any post
//int offset = 0;
try {
    offset = new Integer((String)request.getAttribute("offset"));
} catch (Exception e) {
    // do nothing
}
%>
<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>
<br/>

<table width="95%" align="center">
  <tr class="portlet-font">
    <td>
      <form action="">
        <label for="FastGoForum"><fmt:message key="mvnforum.common.quick_go"/></label> &raquo;
        <%= request.getAttribute("Result") %>
      </form>
    </td>
  </tr>
</table>
<br/>
<%if (MVNForumConfig.getEnablePoll()) {%>
  <%if (hasPoll) {%>
    <% if (isInEdittingStatus) { %><%-- This poll is in Editing Status --%>
      <div class="pagedesc center warning">
        <fmt:message key="mvnforum.common.poll.info.editing_status"/>
      </div>
    <% } else if (request.getAttribute("ViewPollResult") != null) { %> <%-- End Editing Status--%>
      <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
      <% if (canChangeVote) { %>
        <form action="<%=urlResolver.encodeURL(request, response, "votepollprocess", URLResolverService.ACTION_URL)%>" method="post" name="pollvoteform">
        <%=urlResolver.generateFormAction(request, response, "votepollprocess")%>
          <mvn:securitytoken />
          <input type="hidden" name="pollid" value="<%=pollBean.getPollID()%>"/> 
          <input type="hidden" name="typeOfPoll" value="<%=PollBean.THREAD%>"/> 
      <% } %><%-- End canChangeVote --%>
      <mvn:cssrows>
        <tr class="portlet-section-header">
          <td colspan="<%if (canChangeVote) {%>4<%} else {%>3<%}%>"><fmt:message key="mvnforum.common.poll.show_results"/>:&nbsp;<%=pollBean.getPollQuestion()%></td>
        </tr>
        <%
        String type;
        if (pollBean.getPollMultiple() == PollBean.POLL_MULTIPLE_YES) {
          type = " type=\"checkbox\" ";
        } else {
          type = " type=\"radio\" ";
        }
        int i = 0;
        int j = 0;
        for (Iterator iterator = pollAnswerBeans.iterator(); iterator.hasNext();) {
          PollAnswerBean pollAnswerBean = (PollAnswerBean) iterator.next();
        %>
        <tr class="<mvn:cssrow/>">
          <%if (canChangeVote) { %>
            <td><input <%= type %> name="option_<% if (pollBean.getPollMultiple() == PollBean.POLL_MULTIPLE_YES) {%><%=++j%><%}%>" value="<%=pollAnswerBean.getPollAnswerID()%>" class="noborder"/></td>    
          <%} %>      
          <td width="25%">
            <%=pollAnswerBean.getPollAnswerText()%>
            <%if ((pollAnswerBean.getPollAnswerType() == PollAnswerBean.POLL_ANSWER_TYPE_NEED_YOUR_OPINION) && (canChangeVote)) { %>
              <br/>
              &nbsp;<label for="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>"><fmt:message key="mvnforum.common.poll.your_opinion"/></label> <input type="text" id="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>" name="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>"/>
            <% } %>  
          </td>
          <td width="65%">
            <%if (totalVotes > 0) {%>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/bars/bar<%=(i%NUMBER_COLORS)+1%>-l.gif" width="3" alt="*" /><img src="<%=contextPath%>/mvnplugin/mvnforum/images/bars/bar<%=(i%NUMBER_COLORS)+1%>.gif" alt="*" width="<%=(int)((float)(((Integer)resultMap.get(new Integer(pollAnswerBean.getPollAnswerID()))).intValue()) /totalVotes*ONE_HUNDRES_PERCENT_IN_PIXEL)%>" height="9" /><img src="<%=contextPath%>/mvnplugin/mvnforum/images/bars/bar<%=(i%NUMBER_COLORS)+1%>-r.gif" alt="*" />
              &nbsp;[<%=format.format((((Integer)resultMap.get(new Integer(pollAnswerBean.getPollAnswerID()))).intValue())*100.0/totalVotes)%>%]
            <%} else {%>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/bars/bar<%=(i%NUMBER_COLORS)+1%>-l.gif" width="3" alt="*" /><img src="<%=contextPath%>/mvnplugin/mvnforum/images/bars/bar<%=(i%NUMBER_COLORS)+1%>-r.gif" alt="*" />
            <%}%>
          </td>
          <td width="10%" align="center"><b><%=(((Integer)resultMap.get(new Integer(pollAnswerBean.getPollAnswerID()))).intValue())%></b></td>
        </tr>
        <%i++; %>
        <%}%><%-- End for pollanswer--%>
        <tr class="<mvn:cssrow/>">
          <td colspan="<%if (canChangeVote) { %>4<%} else {%>3<%}%>" align="center">
            <fmt:message key="mvnforum.common.poll.numberof.voters"/>:&nbsp;<b><%=totalVotes%></b>&nbsp;
          </td>
        </tr>
        <%if (hasExpired) {%>
          <tr class="pagedesc">
            <td align="center" class="warning" colspan="4"><fmt:message key="mvnforum.common.poll.info.expired"/></td>
          </tr>
        <%} else {%>
          <%if (canChangeVote && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT)) {%>    
            <tr class="portlet-section-footer">
              <td align="center" colspan="4">
                 <input type="button" id="pollvotebutton" name="pollvotebutton" value="<fmt:message key="mvnforum.common.poll.button.vote"/>" onclick="javascript:SubmitPollVoteForm()" class="portlet-form-button"/>
              </td>
            </tr>
          <%}%>
        <%}%>  
      </mvn:cssrows>
      <% if (canChangeVote) { %>
        </form>
      <% } %> 
      </table>
    <%} else {%> <%-- End Check ViewPollResult--%>
      <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
      <%if (onlyCanViewLimit == false) { %>
        <form action="<%=urlResolver.encodeURL(request, response, "votepollprocess", URLResolverService.ACTION_URL)%>" method="post" name="pollvoteform">
        <%=urlResolver.generateFormAction(request, response, "votepollprocess")%>
          <mvn:securitytoken />
          <input type="hidden" name="pollid" value="<%=pollBean.getPollID()%>"/> 
      <%} %>
      <mvn:cssrows>
        <tr class="portlet-section-header">
          <td><fmt:message key="mvnforum.common.poll.vote_poll"/></td>
        </tr>
        <tr class="<mvn:cssrow/>">
          <td>
            <b><%= pollBean.getPollQuestion() %></b>
            <ol>
            <%
            String type;
            if (pollBean.getPollMultiple() == PollBean.POLL_MULTIPLE_YES) {
              type = " type=\"checkbox\" ";
            } else {
              type = " type=\"radio\" ";
            }
            int i = 0;
            for (Iterator pollIter = pollAnswerBeans.iterator(); pollIter.hasNext(); ) {
              PollAnswerBean pollAnswerBean = (PollAnswerBean) pollIter.next();
            %>    
              <li>
                <%if (onlyCanViewLimit == false) { %>
                <input <%= type %> name="option_<% if (pollBean.getPollMultiple() == PollBean.POLL_MULTIPLE_YES) {%><%=++i%><%}%>" value="<%=pollAnswerBean.getPollAnswerID()%>" class="noborder"/>
                <%} %>
                <%if (onlyCanViewLimitAndVote && yourAnswer.contains(new Integer(pollAnswerBean.getPollAnswerID()))) {%>
                  <b>
                <%} %>
                <%= pollAnswerBean.getPollAnswerText() %>
                <% if (pollAnswerBean.getPollAnswerType() == PollAnswerBean.POLL_ANSWER_TYPE_NEED_YOUR_OPINION && onlyCanViewLimit == false) { %>
                  <br/>
                  &nbsp;<label for="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>"><fmt:message key="mvnforum.common.poll.your_opinion"/></label> <input type="text" id="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>" name="your_opinion_<%=pollAnswerBean.getPollAnswerID()%>"/>
                <% } %>  
                <%if (onlyCanViewLimitAndVote && yourAnswer.contains(new Integer(pollAnswerBean.getPollAnswerID()))) {%>
                  </b>
                <%} %>
              </li>    
            <%
            }//End for PollAnswer
            %>
            </ol>
          </td>
        </tr>
        <%if ((onlyCanViewLimit == false) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %> 
          <tr class="portlet-section-footer">
            <td align="center">
              <input type="button" id="pollvotebutton" name="pollvotebutton" value="<fmt:message key="mvnforum.common.poll.button.vote"/>" onclick="javascript:SubmitPollVoteForm()" class="portlet-form-button"/>
              <%if (request.getAttribute("ShowResultButton") != null) { %>
                <input type="button" id="viewpollresultbutton" name="viewpollresultbutton" value="<fmt:message key="mvnforum.common.poll.button.view"/>" onclick="javascript:ViewPollResult('<%=urlResolver.encodeURL(request, response, "viewpoll_result?pollid=" + pollBean.getPollID(), URLResolverService.ACTION_URL)%>')" class="portlet-form-button"/>             
              <%} %>
            </td>
          </tr>
        <%} %>
      </mvn:cssrows>
      <% if (onlyCanViewLimit == false) { %>
        </form>
      <% } %>
      </table>
    <%}%>
    <% if ((threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
      <%if ( permission.canDeletePoll(forumID) || permission.canEditPoll(forumID) || ((onlineUser.isMember()) && (pollBean != null) && (pollBean.getMemberName().equals(onlineUser.getMemberName())))) {%>
        <table width="95%" cellspacing="0" cellpadding="3" align="center">
          <tr>
            <td align="right">
            <%if ( permission.canEditPoll(forumID) || ((onlineUser.isMember()) && (pollBean.getMemberName().equals(onlineUser.getMemberName())))) {%>
              <a class="command" href="<%=urlResolver.encodeURL(request, response, "editthreadpoll?poll=" + pollBean.getPollID(), URLResolverService.RENDER_URL)%>"><img src="<%=imagePath%>/button_edit_poll.gif" border="0" alt="<fmt:message key="mvnforum.common.poll.command.edit_poll"/>" title="<fmt:message key="mvnforum.common.poll.command.edit_poll"/>"/></a>&nbsp;
            <%}%>
            <%if ( permission.canDeletePoll(forumID) || ((onlineUser.isMember()) && (pollBean != null) && (pollBean.getMemberName().equals(onlineUser.getMemberName())))) {%>
              <a class="command" href="<%=urlResolver.encodeURL(request, response, "deletethreadpoll?poll=" + pollBean.getPollID(), URLResolverService.RENDER_URL)%>"><img src="<%=imagePath%>/button_delete_poll.gif" border="0" alt="<fmt:message key="mvnforum.user.deletepollx.title"/>" title="<fmt:message key="mvnforum.user.deletepollx.title"/>"/></a>
            <%}%>     
            </td>
          </tr>
        </table>  
      <%} %>  
    <%} %>  
<br/>
<%} %>
<%}%><%-- End EnablePoll--%>

<% if (MVNForumConfig.getEnableListUsersBrowsingThread() && MVNForumConfig.getEnableOnlineUsers()) { %>
<table width="95%" align="center">
  <tr>
    <td class="portlet-font">
      <% 
      Collection userActions = (Collection) request.getAttribute("UserActions");
      if (userActions.size() == 0) { %>              
          <fmt:message key="mvnforum.common.no_online_users_in_thread"/>    
    <%} else { %>
          <fmt:message key="mvnforum.common.online_users_in_thread"/>    
      <%
          for (Iterator iter = userActions.iterator(); iter.hasNext(); ) {
              OnlineUserAction onlineUserAction = (OnlineUserAction) iter.next();
              String userName = onlineUserAction.getMemberName();
      %>
              <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(userName))%>" class="memberName"><%=userName%></a>
      <%
              if (iter.hasNext()) { %>
                  , 
            <%}
          }// for
      }// else
      %>
    </td>
  </tr>
</table>
<% } %>

<table width="95%" align="center">
  <tr>
    <td valign="bottom">
      <span class="portlet-font"><fmt:message key="mvnforum.common.thread.status"/>: <span class="<%=(threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT)?"":"warning"%>"><%=LocaleMessageUtil.getThreadStatusDescFromInt(onlineUser.getLocale(), threadBean.getThreadStatus())%></span></span><br/>
      <%if (threadBean.getThreadType()!=ThreadBean.THREAD_TYPE_DEFAULT) {%>
      <span class="portlet-font"><fmt:message key="mvnforum.common.thread.type"/>: <%=LocaleMessageUtil.getThreadTypeDescFromInt(onlineUser.getLocale(), threadBean.getThreadType())%></span><br/>
      <% } %>
      <%if (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_DEFAULT) {%>
      <span class="portlet-font"><fmt:message key="mvnforum.common.forum.status"/>: <span class="warning"><%=LocaleMessageUtil.getForumStatusDescFromInt(onlineUser.getLocale(), currentForumBean.getForumStatus())%></span></span><br/>
      <% } %>
      <span class="highlight"><fmt:message key="mvnforum.user.viewthread.total_posts"/>: <%=numberOfPosts%></span>
      <%@ include file="inc_pager.jsp"%>
    </td>
<%if (MVNForumConfig.getEnableUsePopupMenuInViewThread() == false) {%>
    <td valign="bottom" align="right">
    <%if ((MVNForumConfig.getEnablePoll()) && (hasPoll == false)) {%>
      <%if ((threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT)) { %>
        <%if ((permission.canAddPoll(forumID)) ||
              (permission.canAddOwnThreadPoll(forumID) && onlineUser.isMember() && threadBean.getMemberName().equals(onlineUser.getMemberName()))) { %>
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "addthreadpoll?thread=" + threadID, URLResolverService.RENDER_URL)%>"><img src="<%=imagePath%>/button_add_poll.gif" border="0" alt="<fmt:message key="mvnforum.common.poll.command.add_poll"/>" title="<fmt:message key="mvnforum.common.poll.command.add_poll"/>"/></a>
        <%}%>        
      <%}%>
    <%}%>
<%if (permission.canModerateThread(forumID) && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
  <%if (pendingPostCount > 0) {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "moderatependingposts?thread=" + threadID)%>">
    [<fmt:message key="mvnforum.user.viewthread.link.moderate_pending_posts"/> <span class="pendingyes">(<%=pendingPostCount%>)</span>]
    </a>
  <%}%>
    &nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "editthreadstatus?thread=" + threadID)%>"><img src="<%=imagePath%>/button_change_status.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.edit_thread_status"/>" title="<fmt:message key="mvnforum.user.viewthread.link.edit_thread_status"/>"/></a>
    &nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "editthreadtype?thread=" + threadID)%>"><img src="<%=imagePath%>/button_change_type.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.edit_thread_type"/>" title="<fmt:message key="mvnforum.user.viewthread.link.edit_thread_type"/>"/></a>
<%}%>
<%if (permission.canDeletePost(forumID) && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
<%-- @todo: Isn't there canDeleteThread() ? --%>
    &nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "deletethread?thread=" + threadID)%>"><img src="<%=imagePath%>/button_delete_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.delete_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.delete_thread"/>"/></a>
    &nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "movethread?thread=" + threadID)%>"><img src="<%=imagePath%>/button_move_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.move_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.move_thread"/>"/></a>

<%--
<!-- ZT ADD MERGE BUTTON START -->

<!-- Now function mergethread work not very well, we will update this function in the future -->
<% if (isServlet == true) {%>
    &nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "mergethread?thread=" + threadID)%>"><img src="<%=imagePath%>/button_merge_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.merge_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.merge_thread"/>"/></a>
<%}%>

<!-- ZT ADD MERGE BUTTON END -->
--%>

<%}%>
<%if ( ( permission.canModerateThread(forumID) && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) ||
       ( permission.canDeletePost(forumID) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_LOCKED) )) {%>
    <br/>
<%}%>
<%if (MVNForumConfig.getEnableEasyWatching() && onlineUser.isMember()) { %>
 <% if (watched) { %>
      <span class="portlet-font"><fmt:message key="mvnforum.user.viewthread.link.watching_this_thread"/></span> <%-- <a class="command" href="<%=urlResolver.encodeURL(request, response, "mywatch")%>"><fmt:message key="mvnforum.user.viewthread.link.unwatch_this_thread"/></a>--%>
 <% } else { %>
      <span class="portlet-font"><fmt:message key="mvnforum.user.viewthread.link.not_watching_this_thread"/></span> <a class="command" href="<%=urlResolver.encodeURL(request, response, "addwatchprocess?thread=" + threadID + "&amp;WatchSelector=3&amp;WatchType=" + WatchBean.WATCH_TYPE_DEFAULT + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><img src="<%=imagePath%>/button_watch.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.watch_this_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.watch_this_thread"/>"/></a>&nbsp;
 <% } %>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "addfavoritethreadprocess?thread=" + threadID + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>"><img src="<%=imagePath%>/button_add_favorites.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.add_favorite_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.add_favorite_thread"/>"/></a>&nbsp;
<%} // if online user %>
<%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE && isServlet == true) {%>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "viewthreadtree?thread=" + threadID)%>"><img src="<%=imagePath%>/button_tree_mode.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.view_thread_tree"/>" title="<fmt:message key="mvnforum.user.viewthread.link.view_thread_tree"/>"/></a>&nbsp;
<%}%>
    <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddThread(forumID)) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
    <a class="command" href="<%=urlResolver.encodeURL(request, response, "addpost?forum=" + forumID)%>"><img src="<%=imagePath%>/button_new_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.new_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.new_thread"/>"/></a>
    <% if (MVNForumConfig.getEnableVote()) { %>
      <%=MvnForumServiceFactory.getMvnForumService().getVotePeriodBuilderService().drawThreadVote(threadID, request, response)%>
    <% } %>
    <%}//if can new post%>
    </td>
<%}else {%>
    <%int subTopMenuData = 1; %>
    <td valign="bottom" align="right">
      <div id="domMenu_topmenu"></div>
        <script type="text/javascript">
        //<![CDATA[
          domMenu_data.set('domMenu_topmenu', new Hash(
                     1, new Hash(
                       'contents', '&nabla'
          <%if ( permission.canModerateThread(forumID) && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
              <%if (pendingPostCount > 0) {%>
                      ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.moderate_pending_posts")%>',
                       'contentsHover','<%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.moderate_pending_posts")%>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "moderatependingposts?thread=" + threadID)%>'
                       )
              <%}%>
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_status_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.edit_thread_status")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_status_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.edit_thread_status")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "editthreadstatus?thread=" + threadID)%>'
                       )
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_type_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.edit_thread_type")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_type_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.edit_thread_type")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "editthreadtype?thread=" + threadID)%>'
                       )
              <%if ( permission.canDeletePost(forumID) && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.delete_thread")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.delete_thread")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "deletethread?thread=" + threadID)%>'
                       )
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_move_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.move_thread")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_move_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.move_thread")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "movethread?thread=" + threadID)%>'
                       )

              <%}%>
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_add_favorites.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.add_favorite_thread")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_add_favorites.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.add_favorite_thread")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "addfavoritethreadprocess?thread=" + threadID + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>'
                       )
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_watch.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.watch_this_thread")%></div>',
                       'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_watch.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.watch_this_thread")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "addwatchprocess?thread=" + threadID + "&WatchSelector=3&WatchType=" + WatchBean.WATCH_TYPE_DEFAULT + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL)%>'
                       )
              <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddThread(forumID)) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
                       ,<%=subTopMenuData++%>, new Hash(
                       'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_new_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.new_thread")%></div>',
                       'contentsHover', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_new_thread.gif" border="0" alt=""/><%=MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.viewthread.link.new_thread")%></div>',
                       'uri', '<%=urlResolver.encodeURL(request, response, "addpost?forum=" + forumID)%>'
                       )
              <% }//if can new post%>
          <%}%>
        )));
        
        domMenu_settings.set('domMenu_topmenu', new Hash(
        'subMenuWidthCorrection', -1,
         'verticalSubMenuOffsetX', -70,
         'verticalSubMenuOffsetY', 2,
         'horizontalSubMenuOffsetX',100,
         'horizontalSubMenuOffsetY', 3,
         'openMouseoverMenuDelay', 100,
         'closeMouseoutMenuDelay', 300,
         'expandMenuArrowUrl', 'arrow.gif',
         'distributeSpace', false
        ));

        domMenu_activate('domMenu_topmenu');
      //]]>
      </script>
    </td>
<%} %>
  </tr>
</table>

<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="portlet-section-header">
    <td width="155" align="center" nowrap="nowrap"><fmt:message key="mvnforum.common.post.author"/></td>
    <td align="center">
      <table width="100%" class="noborder" cellpadding="0" cellspacing="0">
        <tr>
          <td nowrap="nowrap">
            <%if (previousTopic != 0) {%>
            <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + previousTopic)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/left.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.previous_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.previous_thread"/>"/></a>
            <%}%>
          </td>
          <td class="portlet-section-header" width="100%" align="center">
            <fmt:message key="mvnforum.user.viewthread.has_been_viewed"/> <font color="Aqua"><%=threadBean.getThreadViewCount()+1%></font> <fmt:message key="mvnforum.user.viewthread.times"/> <fmt:message key="mvnforum.user.viewthread.has"/> <font color="Yellow"><%=numberOfPosts-1%></font>
            <%if (numberOfPosts-1 == 1) {%><fmt:message key="mvnforum.user.viewthread.reply"/><%} else {%><fmt:message key="mvnforum.user.viewthread.replies"/><%}%>
          </td>
          <td nowrap="nowrap">
            <%if (nextTopic != 0) {%>
            <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + nextTopic)%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/right.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.next_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.next_thread"/>"/></a>
            <%}%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<mvn:cssrows>
<%

boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
int functionID = 0;
int lastPostCount = 1;
int indexCount = 0;
for (Iterator iterator = postBeans.iterator(); iterator.hasNext(); lastPostCount++) {
    PostBean postBean = (PostBean) iterator.next();
    MemberBean memberBean = postBean.getMemberBean();
    AssertionUtil.doAssert((memberBean == null) || (postBean.getMemberID() == memberBean.getMemberID()), "Member info and Post info don't match!");
%>
<% if (MVNForumConfig.getEnableUsePopupMenuInViewThread()) { %>
<script type="text/javascript">
//<![CDATA[
domMenu_settings.set('domMenu_main<%=functionID%>', new Hash(
      'subMenuWidthCorrection', -1,
       'verticalSubMenuOffsetX', -86,
       'verticalSubMenuOffsetY', 2,
       'horizontalSubMenuOffsetX',100,      
       'horizontalSubMenuOffsetY', 3,
       'openMouseoverMenuDelay', 100,
       'closeMouseoutMenuDelay', 300,     
       'expandMenuArrowUrl', 'arrow.gif',
       'distributeSpace', false
));

<% int menuData=1; int subMenuData=1;%>
function createMenuData<%=functionID%>(parentId,topic, replyURL, replyQuoteURL, addAttachmentURL,uploadImageURL,editPostURL, deleteURL) {   
     var quickReplyStr = 'javascript:QuickReply('+"'" +parentId+"'"+')';
     domMenu_data.set('domMenu_main<%=functionID%>', new Hash(
       <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddPost(forumID)) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { 
            if (!onlineUser.isGuest()) {%>
              <%=menuData++%>, new Hash(
                            'contents','<div>Quick Reply</div>',
                            'uri', quickReplyStr
              ),
            <%}else{%>
               <%=menuData++%>, new Hash(
                            'contents','<div>Reply</div>',
                            'uri', replyURL
              ),
            <%}%>
        <%}%>
        <%=menuData++%>, new Hash(
            'contents', '&nabla'
         <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddPost(forumID)) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
            ,<%=subMenuData++%>, new Hash(
                             'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_reply.gif" border="0" alt=""/>Reply to this Post</div>',
                             'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_reply.gif" border="0" alt=""/>Reply to this Post</div>',
                             'uri', replyURL
                             ),
           <%=subMenuData++%>, new Hash(
                            'contents','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_quote.gif" border="0" alt=""/>Reply with Quote</div>',
                            'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_quote.gif" border="0" alt=""/>Reply with Quote</div>',
                            'uri',replyQuoteURL
                            )
         <% }//if can new post%>
         <%if ( ( (permission.canEditPost(forumID)) || ((memberBean!=null) && (memberBean.getMemberID()==memberID) && permission.canEditOwnPost(forumID)) )
                && (threadBean.getThreadStatus()!=ThreadBean.THREAD_STATUS_LOCKED)
                && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>

         <% if ( MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID) ) { %>
            ,<%=subMenuData++%>, new Hash(
                             'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_attach.gif" border="0" alt=""/>Add Attachment</div>',
                             'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_attach.gif" border="0" alt=""/>Add Attachment</div>',
                             'uri', addAttachmentURL
                             )
         <%if (MVNForumConfig.getEnableAppletUploadImage()) { %>
            ,<%=subMenuData++%>, new Hash(
                             'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_attach_screenshot.gif" border="0" alt=""/>Upload Image</div>',
                             'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_attach_screenshot.gif" border="0" alt=""/>Upload Image</div>',
                             'uri', uploadImageURL
                             )
         <%} %>
        <%}%>
         ,<%=subMenuData++%>, new Hash(
                             'contents', '<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_edit.gif" border="0" alt=""/>Edit Post</div>',
                             'contentsHover','<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_edit.gif" border="0" alt=""/>Edit Post</div>',
                             'uri', editPostURL
                             )
       <%}%>
       <%if ( (permission.canDeletePost(forumID) || ((memberBean!=null) && (memberBean.getMemberID()==memberID)))
        && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
                     ,<%=subMenuData++%>, new Hash(
                             'contents',<%if (postBean.getParentPostID() == 0) {%>'<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_thread.gif" border="0" alt="" />Delete Thread</div>',<%}else{%>'<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_post.gif" border="0" alt="" />Delete Post</div>',<%}%>
                             'contentsHover',<%if (postBean.getParentPostID() == 0) {%>'<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_thread.gif" border="0" alt="" />Delete Thread</div>',<%}else{%>'<div><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icon_delete_post.gif" border="0" alt="" />Delete Post</div>',<%}%>
                             'uri', deleteURL
                             )
        <%}%>
   )));
}
//]]>
</script>
<%} //end of popup menu %>
<pg:item>
<a name="<%=postBean.getPostID()%>"></a>
<%if (lastPostCount == postBeans.size()) {%>
  <a name="lastpost"></a>
<%}%>
<table class="tborder" width="95%" cellspacing="0" cellpadding="5" align="center">
  <tr class="<mvn:cssrow wantToReturn="<%=(postBean.getParentPostID() == 0)%>" valueToReturn="trow0"/>">
    <td width="155" rowspan="2" valign="top">
      <%if ( (memberBean!=null) && (memberBean.getMemberID()!=0) && (memberBean.getMemberID()!=MVNForumConstant.MEMBER_ID_OF_GUEST) ) {%>
        <%if (MVNForumConfig.getEnableShowGender()) {%>  
          <%if (memberBean.getMemberGender()==1) {%>
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/male.gif" border="0" alt="<fmt:message key="mvnforum.common.member.male"/>" title="<fmt:message key="mvnforum.common.member.male"/>" />
          <%} else {%>
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/female.gif" border="0" alt="<fmt:message key="mvnforum.common.member.female"/>" title="<fmt:message key="mvnforum.common.member.female"/>" />
          <%}
          }%>
        <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>" class="memberName"><%=memberBean.getMemberName()%></a>
        <br/>
        <%=(memberBean.getMemberTitle().length() > 0) ? EnableMVNCodeFilter.filter(memberBean.getMemberTitle()) : MyUtil.getMemberTitle(memberBean.getMemberPostCount())%>
        <br/>
        <% if ((memberBean.getMemberAvatar().length() > 0) && MVNForumConfig.getEnableAvatar() ) { %>
        <div align="center"><img src="<%=memberBean.getMemberAvatar_processed(request, response)%>" border="0" alt="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" title="<fmt:message key="mvnforum.common.member.avatar.has_avatar"/>" /></div>
        <% } else { %>
        <br/><br/>
        <% } %>
        <br/>
        <%if (MVNForumConfig.getEnableShowCountry()) {%>
        <%=memberBean.getMemberCountry()%><br/>
        <%}%>
         <% if (MVNForumConfig.getEnableShowJoinDate()) {%>     
        <fmt:message key="mvnforum.user.viewthread.joined"/>: <b><%=onlineUser.getGMTDateFormat(memberBean.getMemberCreationDate())%></b><br/>
        <%} %>
        <% if (MVNForumConfig.getEnableShowPostCount()) {%>     
        <fmt:message key="mvnforum.common.member.post_count"/>: <b><%=memberBean.getMemberPostCount()%></b><br/>
        <%}%>

        <% if (MVNForumConfig.getEnableShowOnlineStatus()) {%>
          <fmt:message key="mvnforum.common.member.online_status"/>:
          <%
          boolean invisible = memberBean.isInvisible();
          boolean online = onlineUserManager.isUserOnline(memberBean.getMemberName());
        
          if ( online && (!enableInvisible || !invisible) ) {%>
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
        <%}%>
      <%} else {%>
        <%-- @todo: replace alt string in next <img> --%>
        <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/nogender.gif" border="0" alt="<fmt:message key="mvnforum.common.member.no_gender"/>" title="<fmt:message key="mvnforum.common.member.no_gender"/>" />
        <%if (memberBean == null) {
            String name = postBean.getMemberName();
            if (name.equalsIgnoreCase(MVNForumConstant.MEMBER_NAME_OF_GUEST)) {
              name = MVNForumConfig.getDefaultGuestName();
            }
            out.print(name);
          } else {%>
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
          <td width="80%" valign="top" class="messageTextBold">
            <%-- Note that if user don't have permission to edit own post, then he cannot add attachment --%>
            <%if ( ( (permission.canEditPost(forumID)) || ((memberBean!=null) && (memberBean.getMemberID()==memberID) && permission.canEditOwnPost(forumID)) )
                   && (threadBean.getThreadStatus()!=ThreadBean.THREAD_STATUS_LOCKED)
                   && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
              <% if ( MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID) ) { %>
              <a href="<%=urlResolver.encodeURL(request, response, "addattachment?post=" + postBean.getPostID() + "&amp;offset=" + offset)%>"><img src="<%=imagePath%>/button_attach.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.attach_file"/>" title="<fmt:message key="mvnforum.user.viewthread.link.attach_file"/>" /></a>&nbsp;
                 <% if (MVNForumConfig.getEnableAppletUploadImage()) { %>
                <a href="<%=urlResolver.encodeURL(request, response, "uploadimage?post=" + postBean.getPostID() + "&amp;offset=" + offset)%>"><img src="<%=imagePath%>/button_upload.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.paste_file"/>" title="<fmt:message key="mvnforum.user.viewthread.link.paste_file"/>" /></a>&nbsp;
                 <% } %>
              <% } %>
              <a href="<%=urlResolver.encodeURL(request, response, "editpost?post=" + postBean.getPostID() + "&amp;offset=" + offset)%>"><img src="<%=imagePath%>/button_edit.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.edit_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.edit_post"/>" /></a>&nbsp;
            <%}%>
            <%if (MVNForumConfig.getEnableNewPost() && permission.canAddPost(forumID) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT)) {%>
              <a href="#message" onclick="QuickReply(<%=postBean.getPostID()%>);"><img src="<%=imagePath%>/button_quick_reply.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.quickreply_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.quickreply_post"/>" /></a>&nbsp;
            <%} %>  
            <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddPost(forumID)) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
               <a href="<%=urlResolver.encodeURL(request, response, "addpost?parent=" + postBean.getPostID())%>"><img src="<%=imagePath%>/button_reply.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.reply_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.reply_post"/>" /></a>&nbsp;
               <a href="<%=urlResolver.encodeURL(request, response, "addpost?parent=" + postBean.getPostID() + "&amp;quote=yes")%>"><img src="<%=imagePath%>/button_quote.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.reply_with_quote"/>" title="<fmt:message key="mvnforum.user.viewthread.link.reply_with_quote"/>" /></a>&nbsp;
            <% }//if can new post%>
              <%if ( (permission.canDeletePost(forumID) || ((memberBean!=null) && (memberBean.getMemberID()==memberID)))
                && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {%>
              <%if (postBean.getParentPostID() == 0) {%>
                <a href="<%=urlResolver.encodeURL(request, response, "deletethread?thread=" + threadID)%>"><img src="<%=imagePath%>/button_delete_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.delete_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.delete_thread"/>" /></a>
              <%} else {%>
                <a href="<%=urlResolver.encodeURL(request, response, "deletepost?post=" + postBean.getPostID())%>"><img src="<%=imagePath%>/button_delete_post.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.delete_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.delete_post"/>" /></a>&nbsp;
                <%if ( (MVNForumConfig.getEnableSplitThread() == true) && permission.canDeletePost(forumID)) {%>
                <a href="<%=urlResolver.encodeURL(request, response, "splitthread?post=" + postBean.getPostID())%>"><img src="<%=imagePath%>/button_split_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.split_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.split_thread"/>" /></a>
                <%}
                } %>
            <%}%>
            <% if (MVNForumConfig.getEnableVote()) { %>
              <%=MvnForumServiceFactory.getMvnForumService().getVotePeriodBuilderService().drawPostVote(postBean.getPostID(), request, response)%>
            <% } %>
            </td>
          <%if (MVNForumConfig.getEnableUsePopupMenuInViewThread()) {
              String deleteURL="";
          %>
          <td align="right" valign="top" nowrap="nowrap" width="100">
            <div id="domMenu_main<%=functionID%>"></div>
            <script type="text/javascript">
            //<![CDATA[
              <%if ( (permission.canDeletePost(forumID) || ((memberBean!=null) && (memberBean.getMemberID()==memberID)))
                  && (currentForumBean.getForumStatus()!=ForumBean.FORUM_STATUS_LOCKED) ) {
                  if (postBean.getParentPostID() == 0) {
                    deleteURL = urlResolver.encodeURL(request, response, "deletethread?thread=" + threadID);
                  } else {
                    deleteURL = urlResolver.encodeURL(request, response, "deletepost?post=" + postBean.getPostID()); 
                  }
                }%>
              createMenuData<%=functionID%>('<%=postBean.getPostID()%>','<%=postBean.getPostTopic()%>','<%=urlResolver.encodeURL(request, response, "addpost?parent=" + postBean.getPostID())%>','<%=urlResolver.encodeURL(request, response, "addpost?parent=" + postBean.getPostID() + "&quote=yes")%>','<%=urlResolver.encodeURL(request, response, "addattachment?post=" + postBean.getPostID() + "&offset=" + offset)%>','<%=urlResolver.encodeURL(request, response, "uploadimage?post=" + postBean.getPostID() + "&offset=" + offset)%>','<%=urlResolver.encodeURL(request, response, "editpost?post=" + postBean.getPostID() + "&offset=" + offset)%>','<%=deleteURL%>');
              domMenu_activate('domMenu_main<%=functionID++%>');
            //]]>
            </script>
          </td>
          <%} %>
        </tr>
        <tr>
          <td width="100%" colspan="2" valign="top" class="messageTextBold" align="left">
            <%if (postBean.getPostIcon().length() > 0) {%>
                <%= EnableEmotionFilter.filter(postBean.getPostIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%> 
            <%}%>

            <%if (postBean.getParentPostID() == 0) {
                String threadPriorityIcon = MyUtil.getThreadPriorityIcon(threadBean.getThreadPriority());
                if ( ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_LOW) && MVNForumConfig.getEnableLowPriorityIcon() ) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_NORMAL) && MVNForumConfig.getEnableNormalPriorityIcon()) || ( (threadBean.getThreadPriority() == ThreadBean.THREAD_PRIORITY_HIGH) && MVNForumConfig.getEnableHighPriorityIcon() ) ) { 
            %>
                  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=threadPriorityIcon%>" border="0" alt="" /> 
            <%  }
              } %>
            <span id="posttopic_<%=postBean.getPostID()%>">
              <%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
            </span>
          </td>
        </tr>   
      </table>
      <hr size="1" noshade="noshade"/>
    <%if (_highlightKey) {%>
      <span class="searchname"><%=MyUtil.filterWithWordWrap(postBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%></span>
    <%} else {%>
      <%=MyUtil.filterWithWordWrap(postBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
    <%}%>
      <%
      Collection attachBeans = postBean.getAttachmentBeans();
      if (attachBeans != null) {
        for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
            AttachmentBean attachBean = (AttachmentBean)attachIter.next();
      %>
          ----------------------------------------<br/>
<%if (permission.canDeletePost(forumID)) {%>
<%-- @todo: or it should be canEditPost(forumID) ? --%>
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "deleteattachment?attach=" + attachBean.getAttachID() + "&amp;offset=" + offset)%>">[<fmt:message key="mvnforum.common.action.delete"/>]</a>&nbsp;
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "editattachment?attach=" + attachBean.getAttachID() + "&amp;offset=" + offset)%>">[<fmt:message key="mvnforum.common.action.edit"/>]</a>&nbsp;
<%}%>
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>" />
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>"><%=attachBean.getAttachFilename()%></a>
          (<%=attachBean.getAttachFileSize()%> bytes)
          (<fmt:message key="mvnforum.common.attachment.download_count"/>: <%=attachBean.getAttachDownloadCount()%>)
          <%
          String enablethumbnail = "";
          if (MVNForumConfig.getEnableThumbnail()) {
              enablethumbnail = "thumbnail=yes&amp;";
          }
          %>
          <%if (attachBean.getAttachDesc().length() > 0) {%>(<%=MyUtil.filter(attachBean.getAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>)<%}%><br/>
          <%if (attachBean.getAttachMimeType().startsWith("image/") && (permission.canGetAttachment(forumID) || MVNForumConfig.getEnableGuestViewImageAttachment()) ) {%>
              <a class="command" href="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>">
                <img src="<%=urlResolver.encodeURL(request, response, "getattachment?" + enablethumbnail + "attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>" alt="<%=attachBean.getAttachFilename()%>" title="<%=attachBean.getAttachFilename()%>" border="0" />
              </a>
          <%}%>
          <br/>
        <%
        }
      }
      %>
      <%if (memberBean != null) {
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
      <%if ( (postBean.getParentPostID() == 0) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST) > 0) ) {%>
        <br/>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST))%>
      <%}%>
      <%if ( (lastPostCount%memberPostsPerPage == 1) && (postBean.getParentPostID() != 0) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST_PAGE_2) > 0) ) {%>
        <br/>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_FIRST_POST_PAGE_2))%>
      <%}%>
      <%if ( (postBeans.size() > 1) && (lastPostCount == postBeans.size()) ) {
          if ( (lastPostCount%2 == 0) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_ODD_POST) > 0) ) {%>
        <br/>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_ODD_POST))%>
        <%}
          if ( (lastPostCount%2 == 1) && (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_EVEN_POST) > 0) ) {%>
        <br/>
        <%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_LAST_EVEN_POST))%>
        <%}
        }%>
    </td>
  </tr>
  <tr class="<mvn:cssrow wantToReturn="<%=(postBean.getParentPostID() == 0)%>" valueToReturn="trow0" autoIncrease="false"/>">
    <td>
      <table class="noborder" width="100%" cellpadding="0" cellspacing="0">
        <tr class="<mvn:cssrow wantToReturn="<%=(postBean.getParentPostID() == 0)%>" valueToReturn="trow0" autoIncrease="false"/>">
          <td nowrap="nowrap">
          [<%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%>]
          <%if (permission.canAdminSystem()) {%>
            <%if (postBean.getPostEditCount() > 0) { /* has edited*/%>
              [<fmt:message key="mvnforum.common.member.first_ip"/>: <font color="red"><%=postBean.getPostCreationIP()%></font> - <fmt:message key="mvnforum.common.member.last_ip"/>: <font color="red"><%=postBean.getPostLastEditIP()%></font>]
            <%} else {/* never been edited*/%>
              [<font color="red"><%=postBean.getPostCreationIP()%></font>]
            <%}%>
          <%}%>
            <a href="<%=urlResolver.encodeURL(request, response, "printpost?post=" + postBean.getPostID())%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/printer.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.print_post"/>" title="<fmt:message key="mvnforum.user.viewthread.link.print_post"/>" /></a>
          </td>
          <td width="100%">
            <%if (memberBean!=null) {%>
              &nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_profile.gif" border="0" alt="<fmt:message key="mvnforum.user.viewmember.title"/>" title="<fmt:message key="mvnforum.user.viewmember.title"/>" /></a>
              <%if (MVNForumConfig.getEnablePrivateMessage()) { %>&nbsp;&nbsp;&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "addmessage?MessageToList=" + Encoder.encodeURL(memberBean.getMemberName()))%>"><img src="<%=imagePath%>/button_pm.gif" border="0" alt="<fmt:message key="mvnforum.user.addmessage.title"/>" title="<fmt:message key="mvnforum.user.addmessage.title"/>" /></a><%}%>
              <%if ((memberBean.getMemberEmailVisible() == 1) && MVNForumConfig.getEnableShowEmail()) {
                  if (onlineUser.isGuest()) {%>
                    &nbsp;&nbsp;&nbsp;<img src="<%=imagePath%>/button_email.gif" border="0" alt="<fmt:message key="mvnforum.common.member.email.hidden_to_guest"/>" title="<fmt:message key="mvnforum.common.member.email.hidden_to_guest"/>" />
                <%} else { %>
                    &nbsp;&nbsp;&nbsp;<a href="mailto:<%=memberBean.getMemberEmail()%>"><img src="<%=imagePath%>/button_email.gif" border="0" alt="<%=memberBean.getMemberEmail()%>" title="<%=memberBean.getMemberEmail()%>" /></a>
                <%}
                } %>
               
              <%if ((MVNForumConfig.getEnableShowHomepage()) && (memberBean.getMemberHomepage().length() > 0) && (memberBean.getMemberHomepage().equals("http://")==false)) {%>&nbsp;&nbsp;&nbsp;<a href="<%=memberBean.getMemberHomepage_http()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/www.gif" border="0" alt="<%=memberBean.getMemberHomepage()%>" title="<%=memberBean.getMemberHomepage()%>" /></a><%}%>
              <%if ((MVNForumConfig.getEnableShowYahoo()) && memberBean.getMemberYahoo().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://edit.yahoo.com/config/send_webmesg?.target=<%=memberBean.getMemberYahoo()%>&amp;.src=pg"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/yim.gif" border="0" alt="<%=memberBean.getMemberYahoo()%>" title="<%=memberBean.getMemberYahoo()%>" /></a><%}%>
              <%if ((MVNForumConfig.getEnableShowAOL()) && memberBean.getMemberAol().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="aim:goim?screenname=<%=memberBean.getMemberAol()%>&amp;message=Hello+Are+you+there?"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/aim.gif" border="0" alt="<%=memberBean.getMemberAol()%>" title="<%=memberBean.getMemberAol()%>" /></a><%}%>
              <%if ((MVNForumConfig.getEnableShowICQ()) && memberBean.getMemberIcq().length() > 0) {%>&nbsp;&nbsp;&nbsp;<a href="http://wwp.icq.com/scripts/search.dll?to=<%=memberBean.getMemberIcq()%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/icq.gif" border="0" alt="<%=memberBean.getMemberIcq()%>" title="<%=memberBean.getMemberIcq()%>" /></a><%}%>
              <%if ((MVNForumConfig.getEnableShowMSN()) && memberBean.getMemberMsn().length() > 0) {%>&nbsp;&nbsp;&nbsp;<%if (MVNForumConfig.getHaveInternet()) {%><img src="http://osi.techno-st.net:8000/msn/<%=memberBean.getMemberMsn()%>" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%} else {%><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/msn.gif" border="0" alt="<%=memberBean.getMemberMsn()%>" title="<%=memberBean.getMemberMsn()%>"/><%}%><%}%>
            <%} else { /*guest*/%>
                &nbsp;
            <%}%>
          </td>
          <td nowrap="nowrap">
            <a href="#<%=postBean.getPostID()%>">[<fmt:message key="mvnforum.user.viewthread.current_link"/>]</a>
            <%if (onlineUser.isGuest()) {%>
              <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/threat.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.report_need_login"/>" title="<fmt:message key="mvnforum.user.viewthread.report_need_login"/>" />
            <%} else { 
                if (MVNForumConfig.getEnableEmailThreateningContent()) {
            %>  
            <a href="<%=urlResolver.encodeURL(request, response, "sendmail?ToAdmin=true&amp;Subject=Report threatening content (ThreadID = " + postBean.getThreadID() + " and PostID = " + postBean.getPostID()+ ")")%>"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/threat.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.report_threatening"/>" title="<fmt:message key="mvnforum.user.viewthread.link.report_threatening"/>" /></a>
              <%} else { %>
            <a href="mailto:<%=MVNForumConfig.getWebMasterEmail()%>?subject=Report threaten message (threadid = <%=postBean.getThreadID()%> and postid = <%=postBean.getPostID()%>)"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/threat.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.report"/>" title="<fmt:message key="mvnforum.user.viewthread.report"/>" /></a>
              <%}            
            } %>
            &nbsp;<a href="#"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.go_top"/>" title="<fmt:message key="mvnforum.user.viewthread.go_top"/>" /></a>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</pg:item>
<% }//for %>
</mvn:cssrows>
<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
    <td align="right">
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "printthread?thread=" + threadID)%>"><img src="<%=imagePath%>/button_printer_friendly_version.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.print_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.print_thread"/>" /></a>&nbsp;
    <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddThread(forumID)) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
      <a class="command" href="<%=urlResolver.encodeURL(request, response, "addpost?forum=" + forumID)%>"><img src="<%=imagePath%>/button_new_thread.gif" border="0" alt="<fmt:message key="mvnforum.user.viewthread.link.new_thread"/>" title="<fmt:message key="mvnforum.user.viewthread.link.new_thread"/>" /></a>
    <% }//if can new post%>
    </td>
  </tr>
</table>
</pg:pager>
<input type="hidden" name="previewUrl" id="previewUrl" value="<%=urlResolver.encodeURL(request, response, "addpost", URLResolverService.ACTION_URL)%>" />
<%if (MVNForumConfig.getEnableNewPost() && permission.canAddPost(forumID) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (currentForumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT)) {%>
<form action="<%=urlResolver.encodeURL(request, response, "addpostprocess", URLResolverService.ACTION_URL)%>" method="post" name="mvnform">
<%=urlResolver.generateFormAction(request, response, "addpostprocess")%>
<mvn:securitytoken />
<input type="hidden" name="parent" id="parent" value="<%= lastPostBean.getPostID()%>"/>
<input type="hidden" name="PostTopic" value="<%=lastPostTopic%>"/>
<input type="hidden" name="message" value=""/>
<table width="95%" align="center">
  <tr> 
    <td>
      <span id="quickreplymessage" class="highlight"><fmt:message key="mvnforum.user.viewthread.quickreply_to_post"/> [<%=lastPostBean.getPostID()%>, <%=lastPostTopic%>]</span>
      <br/><textarea name="input" id="input" rows="7" cols="65" onkeyup="initTyper(this);"></textarea>
    </td>
  </tr>     
  <%if (currentLocale.equals("vi")) {/*vietnamese here*/%>
  <tr>
    <td>
      <input type="radio" name="vnselector" id="TELEX" value="TELEX" onclick="setTypingMode(1);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.telex"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VNI" value="VNI" onclick="setTypingMode(2);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.vni"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="VIQR" value="VIQR" onclick="setTypingMode(3);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.VIQR"/>&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="radio" name="vnselector" id="NOVN" value="NOVN" onclick="setTypingMode(0);" class="noborder"/> <fmt:message key="mvnforum.common.vietnamese_type.not_use"/>
      <script type="text/javascript">
      //<![CDATA[
      initVNTyperMode();
      //]]>
      </script>
    </td>
  </tr>
  <%}// end if vietnamese%>
  <tr>
    <td colspan="2" class="left">
      <input type="hidden" name="preview" value=""/>
      <input type="button" name="previewbutton" class="portlet-form-button" value="<fmt:message key="mvnforum.common.action.preview"/>" onclick="javascript:PreviewForm()"/>
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.addpost.mode.reply"/>" onclick="javascript:SubmitForm()" class="portlet-form-button"/>
    </td>
  </tr>
</table>
</form>
<%}// if have permission for quick reply%>

<br/>
<%@ include file="footer.jsp"%>
<%if (isPortlet) { %>
  <script type="text/javascript">
  //<![CDATA[
    prettyPrint();
  //]]>
  </script>
<%} %>
<%if (_highlightKey) {%>
<script type="text/javascript">
//<![CDATA[
  var searchnames = document.getElementsByTagName("span");
  for (i = 0; i < searchnames.length; i++)
    if (searchnames[i].className.match(/\bsearchname\b/))
        searchhi.highlightWords(searchnames[i],"<%=StringUtil.escapeJavaScript(highlightKey)%>");
//]]>
</script>
<%} %>
</mvn:body>
</mvn:html>
</fmt:bundle>