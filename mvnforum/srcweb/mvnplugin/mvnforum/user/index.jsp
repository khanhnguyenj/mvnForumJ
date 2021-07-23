<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/index.jsp,v 1.173 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.173 $
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
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.common.*" %>
<%@ page import="com.mvnforum.auth.OnlineUserAction" %>
<%@ page import="com.mvnforum.MVNForumConstant" %>

<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="net.myvietnam.mvncore.util.StringUtil" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.common.forum.homepage"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/listforums.js" type="text/javascript"></script>
<link href="<%=onlineUser.getCssPath(request)%>" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/prettify.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
//<![CDATA[
function unload() {
  if ( (document.submitform != null) && (document.submitform.submitbutton != null) ) {
    document.submitform.submitbutton.disabled=false;
  }
}
//]]>
</script>
</mvn:head>
<mvn:body onload="prettyPrint()" onunload="unload();">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prettify/prettify.js"></script>
<%@ include file="header.jsp"%>
<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {
  var agt=navigator.userAgent.toLowerCase();

  // Maybe, Opera make an onClick event when user press enter key
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13) 
    SubmitForm();
}
function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberMatkhau, document.submitform.md5pw);
    }
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    return document.submitform.submit();
  }
  return false;
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberName, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;
  if (isBlank(document.submitform.MemberMatkhau, "<fmt:message key="mvnforum.common.member.password"/>")) return false;
  //Check Password's length
  if (document.submitform.MemberMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
<br/>

<div class="nav center">
  <%= request.getAttribute("tree") %>
</div>
<br/>

<table align="center" border="0" cellpadding="0" cellspacing="0" width="95%">
  <tr>
    <!-- Left section -->
    <td width="25%" valign="top">
      <!-- Browse Forum Category -->
    <%if (onlineUser.isMember()) {%>
      <table class="tborder" border="0" cellspacing="0" cellpadding="4" width="100%">
      <mvn:cssrows>
        <tr>
          <td align="center" class="portlet-section-header"><fmt:message key="mvnforum.user.header.welcome"/> 
            <font color="#FF0000"><%=memberName%></font>
          </td>
        </tr>
        <tr>
          <td class="<mvn:cssrow/>">
            <fmt:message key="mvnforum.user.index.current_time"/>: <%=onlineUser.getGMTTimestampFormat(DateUtil.getCurrentGMTTimestamp())%>
            <% if (isPortlet == false) { %>
              <br/><a href="<%=urlResolver.encodeURL(request, response, "logout")%>" class="command"><fmt:message key="mvnforum.common.action.logout"/></a>
            <% } %>
          </td>
        </tr>
      </mvn:cssrows>
      </table>

    <%} else { %>
      <table class="tborder" border="0" cellspacing="0" cellpadding="4" width="100%">
      <mvn:cssrows>
        <tr>
          <td align="center" class="portlet-section-header">
          <fmt:message key="mvnforum.user.header.welcome"/> <%= ((memberName!=null) && (memberName.length()>0)) ? memberName : MVNForumConfig.getDefaultGuestName()%>
          </td>
        </tr>
        <%if (MVNForumConfig.getEnableLogin()) {%>
        <tr>
          <td class="<mvn:cssrow/>">
            <form action="<%=urlResolver.encodeURL(request, response, "loginprocess", URLResolverService.ACTION_URL)%>" method="post" name="submitform" onsubmit="return false;">
              <%=urlResolver.generateFormAction(request, response, "loginprocess")%>
              <mvn:securitytoken />
              <input type="hidden" name="FromLoginPage" value="true" />
              <input type="hidden" name="md5pw" value="" />
              <label for="MemberName"><fmt:message key="mvnforum.common.member.login_name"/></label><br/>
              <input type="text" id="MemberName" name="MemberName" size="15" /><br/>
              <label for="MemberMatkhau"><fmt:message key="mvnforum.common.member.password"/></label><br/>
              <input type="password" id="MemberMatkhau" name="MemberMatkhau" size="15" onkeypress="checkEnter(event);" /><br/><br/>
              <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.login"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" /><br/>
            </form>
            <a href="<%=urlResolver.encodeURL(request, response, "iforgotpasswords")%>" class="command"><fmt:message key="mvnforum.user.login.forgot_password"/></a>
          </td>
        </tr>
        <% } %>
      </mvn:cssrows>
      </table>
    <% } %>
<%
//Copy code from listonlineusers.jsp
Collection onlineUserActions = (Collection)request.getAttribute("OnlineUserActions");

if (MVNForumConfig.getEnableOnlineUsers()) {
    int guestCount = 0;
    int memberCount = 0;
    int invisibleMemberCount = 0;
    int visibleMemberCount = 0;
    boolean enableInvisible = MVNForumConfig.getEnableInvisibleUsers();
    for (Iterator countIterator = onlineUserActions.iterator(); countIterator.hasNext(); ) {
        OnlineUserAction onlineUserAction = (OnlineUserAction)countIterator.next();
        boolean invisible = onlineUserAction.isInvisibleMember();
        int mID = onlineUserAction.getMemberID();
        if ( (mID==0) || (mID==MVNForumConstant.MEMBER_ID_OF_GUEST) ) {
            guestCount++;
        } else if (enableInvisible) {
            if (invisible) {
               invisibleMemberCount++;
            } else {
               memberCount++;
            }
        } else { // disable invisible feature
            memberCount++;
        }
    } // end of for
    visibleMemberCount = memberCount - invisibleMemberCount;
    %>
      <br/>
      <table class="tborder" border="0" cellspacing="0" cellpadding="4" width="100%">
      <mvn:cssrows>
        <tr>
          <td align="center" class="portlet-section-header"><fmt:message key="mvnforum.user.header.who_online"/></td>
        </tr>
        <tr>
          <td class="<mvn:cssrow/>">
            <fmt:message key="mvnforum.common.there_are"/> <b><%=onlineUserActions.size()%></b> <fmt:message key="mvnforum.common.online_users"/>
            (<b><%=guestCount%></b> <%if (guestCount>1) {%><fmt:message key="mvnforum.common.guests"/><%} else {%><fmt:message key="mvnforum.common.guest"/><%}%>,
            <b><%=memberCount%></b> <%if (memberCount>1) {%><fmt:message key="mvnforum.common.members"/><%} else {%><fmt:message key="mvnforum.common.member"/><%}%>
            <%if (enableInvisible) {%>
               ,<%-- last comma if folling exists --%>
               <b><%=invisibleMemberCount%></b> <%if (invisibleMemberCount>1) {%><fmt:message key="mvnforum.common.member.online.invisible_member"/><%} else {%><fmt:message key="mvnforum.common.member.online.invisible_member"/><%}%>
            <%}%>
            )<%if (visibleMemberCount > 0) {%><br/> <%-- second bracket --%><fmt:message key="mvnforum.user.header.who_online"/>:<%}%>
            <%boolean canAdmin = permission.canAdminSystem();
              for (Iterator iterator = onlineUserActions.iterator(); iterator.hasNext(); ) {
                OnlineUserAction onlineUserAction = (OnlineUserAction)iterator.next();
                boolean userInvisible = onlineUserAction.isInvisibleMember();
                int mID = onlineUserAction.getMemberID();
                String mName = onlineUserAction.getMemberName();
                if ((mID!=0) && (mID!=MVNForumConstant.MEMBER_ID_OF_GUEST)) { %>
                <%if ( enableInvisible && userInvisible && !canAdmin ) { %>
                    <span class="memberName"><fmt:message key="mvnforum.common.member.online.invisible_member"/></span>
                <%} else {%>
                    <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(mName))%>" class="memberName"><%=mName%></a>
                    <%if (enableInvisible && userInvisible && canAdmin) { %>(<fmt:message key="mvnforum.common.member.online.invisible"/>)<%}%>
                <%}%>
                <%if (onlineUserAction.getSessionCount() > 1) { %>
                    (<%=onlineUserAction.getSessionCount()%> <fmt:message key="mvnforum.common.member.online.sessions"/>)
                <%}%>
             <% } /*if (not guest)*/%>
            <%} /*for*/%>
            <br/>
            <a href="<%=urlResolver.encodeURL(request, response, "listonlineusers")%>" class="command"><fmt:message key="mvnforum.user.listonlineusers.table.what_are_doing"/></a>
          </td>
        </tr>
      </mvn:cssrows>
      </table>
<br/>
<%}// end if enable online user%>

<%if (MVNForumConfig.getEnableMostActiveThreads()) {
    Collection mostActiveThreads = (Collection)request.getAttribute("MostActiveThreads");
%>
      <table class="tborder" border="0" cellspacing="0" cellpadding="3" width="100%">
      <mvn:cssrows>
        <tr>
          <td align="center" nowrap="nowrap" class="portlet-section-header"><fmt:message key="mvnforum.common.most_active_threads_since_last_week"/></td>
        </tr>
<%  for (Iterator iter = mostActiveThreads.iterator(); iter.hasNext(); ) {
      ActiveThread thread = (ActiveThread)iter.next();
      int postCount = thread.getLastPostCount(); %>
        <tr class="<mvn:cssrow/>">
          <td class="portlet-font">
          <% 
            String typeImage = MyUtil.getThreadTypeIcon(thread.getThreadType());
          %>
          <% if (thread.getThreadType() != ThreadBean.THREAD_TYPE_DEFAULT) { %>
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/<%=typeImage%>" alt="" />
          <% }%>
          <% if (thread.getAttachCount() > 0) {%>
            <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" title="<%=thread.getAttachCount()%> <% if (thread.getAttachCount() == 1) {%><fmt:message key="mvnforum.common.attachment"/><% } else {%><fmt:message key="mvnforum.common.attachments"/><%}%>" alt="" />
          <% } %>
            <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + thread.getThreadID())%>" class="command"><%=MyUtil.filter(thread.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
            (<b><%=postCount%></b> <% if (postCount == 1) {%><fmt:message key="mvnforum.common.new_post"/><% } else { %><fmt:message key="mvnforum.common.new_posts"/><% } %>)
          </td>
        </tr>
<% } //for %>
    <% if (mostActiveThreads.size() == 0) { %>
        <tr class="<mvn:cssrow/>">
          <td align="center"><fmt:message key="mvnforum.common.no_active_threads"/></td>
        </tr>
    <% }// if no active threads %>
      </mvn:cssrows>
      </table>
<br/>
<%} // end enable most active threads %>
<%if (MVNForumConfig.getEnableMostActiveMembers() ) {%>
      <table class="tborder" border="0" cellspacing="0" cellpadding="3" width="100%">
      <mvn:cssrows>
        <tr>
          <td align="center" nowrap="nowrap" class="portlet-section-header"><fmt:message key="mvnforum.common.most_active_members_since_last_week"/></td>
        </tr>
<%Collection mostActiveMembers = (Collection)request.getAttribute("MostActiveMembers");
  for (Iterator iter = mostActiveMembers.iterator(); iter.hasNext(); ) {
        ActiveMember member = (ActiveMember)iter.next();
        int postCount = member.getLastPostCount(); %>
        <tr class="<mvn:cssrow/>">
          <td class="portlet-font">
            <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(member.getMemberName()))%>" class="command"><%=member.getMemberName()%></a>
            (<%=postCount%>
            <% if (postCount == 1) {%><fmt:message key="mvnforum.common.new_post"/><% } else { %><fmt:message key="mvnforum.common.new_posts"/><% } %>)
          </td>
        </tr>
<% } %>
      <% if (mostActiveMembers.size() == 0) { %>
        <tr class=" <mvn:cssrow/>">
          <td align="center"><fmt:message key="mvnforum.common.no_active_members"/></td>
        </tr>
      <% }// if no active members %>
      </mvn:cssrows>
      </table>
<br/>
<%} // end if enable most active members
CategoryCache categoryCache = CategoryCache.getInstance();
ForumCache forumCache = ForumCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
Collection forumBeans = forumCache.getBeans();
if (MVNForumConfig.getEnableSiteStatisticsOverview()) {

    int numberOfThreads = MyUtil.getViewableThreads(forumBeans, permission);
    int numberOfPosts = MyUtil.getViewablePosts(forumBeans, permission);
%>
      <table class="tborder" border="0" cellspacing="0" cellpadding="3" width="100%">
       <mvn:cssrows>
        <tr>
          <td align="center" class="portlet-section-header"><fmt:message key="mvnforum.common.statistic"/></td>
        </tr>
        <tr>
          <td class="<mvn:cssrow/>">
            <fmt:message key="mvnforum.common.totalcategories"/>: <b><%=MyUtil.getViewableCategories(categoryBeans, permission)%></b><br/>
            <fmt:message key="mvnforum.common.totalforum"/>: <b><%=MyUtil.getViewableForums(forumBeans, permission)%></b><br/>
            <fmt:message key="mvnforum.common.totaltopic"/>: <b><%=numberOfThreads%></b><br/>
            <fmt:message key="mvnforum.common.totalpost"/>: <b><%=numberOfPosts%></b><br/>
            
         <% if (MVNForumConfig.getEnableListNewMembersInRecentDays()) {
              Collection memberBeans = (Collection) request.getAttribute("MemberBeans");
              long numberOfMembers = ((Integer)request.getAttribute("NumberOfMembers")).intValue();
              MemberBean lastMember = (MemberBean) request.getAttribute("MemberBean"); %>
              <fmt:message key="mvnforum.common.totalmember"/>: <b><%=numberOfMembers%></b><br/>
              <fmt:message key="mvnforum.common.latestmember"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(lastMember.getMemberName()))%>"><%=lastMember.getMemberName()%></a><br/>
                 <%if (memberBeans.size() == 0) { %>
                   <fmt:message key="mvnforum.common.norecentdaysmembers">
                     <fmt:param><%=MVNForumConfig.getDaysToShowRecentMembers()%></fmt:param>
                   </fmt:message>
               <%} else { %>
                   <fmt:message key="mvnforum.common.recentdaysmembers">
                     <fmt:param><%=MVNForumConfig.getDaysToShowRecentMembers()%></fmt:param>
                   </fmt:message>
               <%  for (Iterator iterator = memberBeans.iterator(); iterator.hasNext(); ) {
                     MemberBean memberBean = (MemberBean) iterator.next();
                     String mName = memberBean.getMemberName();
                     if (mName.equals(MVNForumConfig.getDefaultGuestName())) { %>
                       <%=mName%>
                   <%} else { %>
                        <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(mName))%>"><%=mName%></a>
                  <% }
                     if (iterator.hasNext()) { %>
                       ,
                   <%} //if
                   } //for
                 } // else
               } // list members in recent days %>
          </td>
        </tr>
      </mvn:cssrows>
      </table>
      <br/>
<%} // end if enable show forum statistics%>

<%--
      <table class="tborder" border="0" cellspacing="1" cellpadding="3" width="100%">
        <tr>
          <td align="center" class="portlet-section-header"><a href="<%=urlResolver.encodeURL(request, response, "listforums")%>" class="portlet-section-header"><fmt:message key="mvnforum.user.listforums.title"/></a></td>
        </tr>
<%  int categoryCount = 0;
    for (Iterator categoryIterator = categoryBeans.iterator(); categoryIterator.hasNext(); ) {
      CategoryBean categoryBean = (CategoryBean)categoryIterator.next();
      if (MyUtil.canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission) == false) continue;
        categoryCount++;
%>
        <tr>
          <td align="center" class="portlet-section-subheader">
            <b><a href="<%=urlResolver.encodeURL(request, response, "listforums?category=" + categoryBean.getCategoryID())%>"><%=categoryBean.getCategoryName()%></a></b>
            <a onclick="showhide('category_<%=categoryBean.getCategoryID()%>');return false" href="javascript:void(0)">
            <img align="middle" border="0" height="13" width="14" src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/arrow-subnav-down.gif" alt="" /></a>
          </td>
        </tr>
        <tbody id="category_<%=categoryBean.getCategoryID()%>">
<%      int i = 0;
        for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
          ForumBean forumBean = (ForumBean)forumIterator.next();
          if (forumBean.getCategoryID() == categoryBean.getCategoryID()) {
            if (permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) { %>
            <tr>
              <td align="center" class="<%=MyUtil.getRowCSS(i++)%>"><a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumBean.getForumID())%>"><%=forumBean.getForumName()%></a></td>
            </tr>
          <%}
          }
        }//for %>
        </tbody>
<%  }// end loop on category
    if (categoryCount == 0) {%>
        <tr>
          <td align="center" class="portlet-section-body">
          <fmt:message key="mvnforum.user.listforums.table.no_category"/>
          </td>
        </tr>
<%  } %>
      </table>
      
 --%>   
<%= request.getAttribute("Result") %>
      
    </td>
    <!-- End of left section -->
    <td style="width: 5px;">&nbsp;&nbsp;</td>
    <!-- Center section -->
<%
Collection coLastPosts = (Collection)request.getAttribute("LastPosts");
%>
    <td width="80%" valign="top">
      <table class="tborder" border="0" cellpadding="4" cellspacing="0" width="100%">
        <tr>
          <td class="portlet-section-header"><fmt:message key="mvnforum.user.index.last_post_in_each_forum"/></td>
        </tr>
      </table>
<%
  int i = 0;

  for (Iterator postIterator = coLastPosts.iterator(); postIterator.hasNext(); ) {
    List infoList = (List)postIterator.next();
    PostBean postBean = (PostBean)infoList.get(0);
    ThreadBean threadBean = (ThreadBean)infoList.get(1);
    ForumBean forumBean = forumCache.getBean(threadBean.getForumID());
    CategoryBean categoryBean = categoryCache.getBean(forumBean.getCategoryID());
      if (permission.canReadPost(postBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
        i++;
      %>
      <table class="tborder" border="0" cellpadding="4" cellspacing="0" width="100%">
      <mvn:cssrows>
        <tr class="<mvn:cssrow/>">
          <td>
            <fmt:message key="mvnforum.common.category"/>: <a href="<%=urlResolver.encodeURL(request, response, "listforums?category=" + categoryBean.getCategoryID())%>"><%=categoryBean.getCategoryName()%></a> &raquo;
            <fmt:message key="mvnforum.common.forum"/>: <a href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + threadBean.getForumID())%>"><%=forumBean.getForumName()%></a> &raquo;
            <fmt:message key="mvnforum.common.thread"/>: <a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadBean.getThreadID())%>"><%=MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
          </td>
        </tr>
        <tr class="<mvn:cssrow autoIncrease="false"/>">
          <td>
            <fmt:message key="mvnforum.common.post.post_by"/> <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(postBean.getMemberName()))%>" class="memberName"><%=postBean.getMemberName()%></a>
            <fmt:message key="mvnforum.common.at"/> <%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%>
          </td>
        </tr>
        <tr class="<mvn:cssrow autoIncrease="false"/>">
          <td>
            <b><%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, false/*URL*/)%></b><br/>
            <%=MyUtil.filter(StringUtil.getShorterString(postBean.getPostBody(), MVNForumConfig.getMaxCharsInLongSummary()), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
          </td>
        </tr>
        <tr class="<mvn:cssrow autoIncrease="false"/>">
          <td>
            <fmt:message key="mvnforum.common.reply_count"/>: <%=threadBean.getThreadReplyCount()%> ::
            <% if (MVNForumConfig.getEnableNewPost() && (onlineUser.isGuest()||permission.canAddPost(forumBean.getForumID())) && (threadBean.getThreadStatus()==ThreadBean.THREAD_STATUS_DEFAULT) && (forumBean.getForumStatus()==ForumBean.FORUM_STATUS_DEFAULT) ) { %>
            <a href="<%=urlResolver.encodeURL(request, response, "addpost?parent=" + postBean.getPostID())%>" class="command"><fmt:message key="mvnforum.user.viewthread.link.reply_post"/></a>
            <% }//if can new post%>
          </td>
        </tr>
      </mvn:cssrows>
      </table>
      <br/>
   <% }
  }%>
<% if (i == 0) { %>
      <table class="tborder" border="0" cellpadding="4" cellspacing="0" width="100%">
      <mvn:cssrows>
        <tr class="<mvn:cssrow/>">
          <td align="center"><fmt:message key="mvnforum.user.index.no_post"/></td>
        </tr>
      </mvn:cssrows>
      </table>
 <%}%>
    </td>
    <!-- End of Center section -->
    <%--
    [minhnn]: I comment out this as a default for most site, if you need a
    right column for logo advertisement, please uncomment it.

    <td style="width: 5px;">&nbsp;&nbsp;</td>
    <!-- Right section -->
    <td width="30%" valign="top">
      <table class="tborder" border="0" cellspacing="1" cellpadding="4" width="100%">
        <tr>
          <td align="center" class="portlet-section-header">Advertisement</td>
        </tr>
        <tr>
          <td class="portlet-section-body">
            <a href="http://www.mvnforum.com"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/logo.gif" border="0" alt="mvnForum" title="mvnForum" /></a>
          </td>
        </tr>
        <tr>
          <td class="portlet-section-alternate">
            <a href="http://www.mvnforum.com"><img src="<%=contextPath%>/mvnplugin/mvnforum/images/logo.gif" border="0" alt="mvnForum" title="mvnForum" /></a>
          </td>
        </tr>
      </table>
    </td>
    <!-- End of Right section -->
    --%>
  </tr>
</table>

<br/>
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
