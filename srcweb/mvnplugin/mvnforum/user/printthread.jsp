<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/printthread.jsp,v 1.66 2009/05/13 09:14:14 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.66 $
 - $Date: 2009/05/13 09:14:14 $
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="net.myvietnam.mvncore.util.AssertionUtil" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<%
ThreadBean threadBean = (ThreadBean)request.getAttribute("ThreadBean");
Collection postBeans = (Collection)request.getAttribute("PostBeans");

int numberOfPosts = ((Integer)request.getAttribute("NumberOfPosts")).intValue();
int previousTopic = ((Integer)request.getAttribute("PreviousTopic")).intValue();
int nextTopic     = ((Integer)request.getAttribute("NextTopic")).intValue();

int maxPageItems = onlineUser.getPostsPerPage();
boolean printAll = ((Boolean)request.getAttribute("printall")).booleanValue();
if (printAll) {
  maxPageItems = numberOfPosts;     
}
%>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.printthread.title"/> - <%=threadBean.getThreadTopic()%></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/prettify.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/prettify/prettify.js"> </script>
</mvn:head>
<mvn:body onload="prettyPrint()">
<br/>
<table width="98%" class="noborder" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td>
    <img src="<%=contextPath%>/mvnplugin/mvnforum/images/logo.gif" height="50" border="0" alt="<fmt:message key="mvnforum.common.forum.homepage"/>" title="<fmt:message key="mvnforum.common.forum.homepage"/>" />
    </td>
  </tr>
</table>
<br/>

<%
// current offset, use to remember offset state after we update any post
int offset1 = 0;
try {
    offset1 = ((Integer)request.getAttribute("pager.offset")).intValue();
} catch (Exception e) {
    // do nothing
}

ForumCache forumCache = ForumCache.getInstance();

int forumID = threadBean.getForumID();
String forumName = forumCache.getBean(forumID).getForumName();

int threadID = ((Integer)request.getAttribute("thread")).intValue();
AssertionUtil.doAssert(threadID == threadBean.getThreadID(), "2 threadID are not the same.");
%>

<pg:pager
  url="printthread"
  items="<%= numberOfPosts %>"
  maxPageItems="<%= maxPageItems %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.posts"); %>
<%-- keep track of preference --%>
<pg:param name="thread" value="<%=String.valueOf(threadID)%>"/>

<table width="98%" cellspacing="0" cellpadding="5" align="center">
  <tr class="nav">
    <td width="100%" colspan="2" nowrap="nowrap">
    <a class="nav" href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a><br/>
    &raquo;&nbsp;
    <a class="nav" href="<%=urlResolver.encodeURL(request, response, "listforums")%>"><fmt:message key="mvnforum.user.listforums.title"/></a><br/>
    &raquo;&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.common.forum"/>: <a class="nav" href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID)%>"><%=forumName%></a><br/>
    &raquo;&nbsp;&raquo;&nbsp;&raquo;&nbsp;
    <fmt:message key="mvnforum.common.thread"/>: <a class="nav" href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadID)%>"><%=MyUtil.filter(threadBean.getThreadTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
    </td>
  </tr>
  <tr class="portlet-font">
    <td valign="bottom">
      <fmt:message key="mvnforum.user.printthread.print_at"/> <b><%=onlineUser.getGMTTimestampFormat(DateUtil.getCurrentGMTTimestamp())%></b>
    </td>
    <td align="right">
      <%@ include file="inc_pager.jsp"%>
    <% if (!printAll) { %>
      <a class="pager" href="<%=urlResolver.encodeURL(request, response, "printthread?thread=" + threadID + "&amp;printall=yes")%>"><fmt:message key="mvnforum.user.printthread.print_all_posts"/></a>
    <% } %>
    </td>
  </tr>
</table>
<%
int i = 0;
for (Iterator iterator = postBeans.iterator(); iterator.hasNext(); ) {
    PostBean postBean = (PostBean)iterator.next();
    MemberBean  memberBean = postBean.getMemberBean();
    AssertionUtil.doAssert((memberBean == null) || (postBean.getMemberID() == memberBean.getMemberID()), "Member info and Post info don't not match!");
    /*i++;
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
<table class="tborder" width="98%" cellspacing="0" cellpadding="5" align="center" border="0">
<mvn:cssrows>
  <tr class="<mvn:cssrow/>">
    <td>
      <fmt:message key="mvnforum.common.post.post_by"/> <b><%=postBean.getMemberName()%></b>
      <fmt:message key="mvnforum.common.at"/> <b><%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%></b>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td valign="top">
      <%if (postBean.getPostIcon().length() > 0) {
          out.print(EnableEmotionFilter.filter(postBean.getPostIcon() + "&nbsp;&nbsp;", contextPath + MVNForumGlobal.EMOTION_DIR));
      }%>
      <b><%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, false/*URL*/)%></b><br/>
      <%=MyUtil.filter(postBean.getPostBody(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/)%>
      <%
      Collection attachBeans = postBean.getAttachmentBeans();
      if (attachBeans != null) {
        for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
            AttachmentBean attachBean = (AttachmentBean)attachIter.next();
        %>
          ----------------------------------------<br/>
          <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" border="0" alt="<fmt:message key="mvnforum.common.attachment"/>" />
          <a class="command" href="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>"><%=attachBean.getAttachFilename()%></a>
          (<%=attachBean.getAttachFileSize()%> bytes)
          (<fmt:message key="mvnforum.common.attachment.download_count"/>: <%=attachBean.getAttachDownloadCount()%>)
          <%if (attachBean.getAttachDesc().length() > 0) {%>(<%=MyUtil.filter(attachBean.getAttachDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, false/*newLine*/, true/*URL*/)%>)<%}%><br/>
          <%if (attachBean.getAttachMimeType().startsWith("image/") && permission.canGetAttachment(forumID)) {%><img src="<%=urlResolver.encodeURL(request, response, "getattachment?attach=" + attachBean.getAttachID(), URLResolverService.ACTION_URL)%>" alt="<%=attachBean.getAttachFilename()%>" title="<%=attachBean.getAttachFilename()%>" border="0" /><%}%>
          <br/>
        <%
        }
      }
      %>
      <%if (memberBean!=null) {
          String signature = MyUtil.filter(memberBean.getMemberSignature(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
          if ( (signature.length() > 0) && MVNForumConfig.getEnableShowSignature() ) { %>
            ----------------------------------------<br/>
            <%=signature%>
          <%}
        }%>
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>
<% }//for %>

<table width="98%" align="center">
  <tr>
    <td align="right">
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>

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
