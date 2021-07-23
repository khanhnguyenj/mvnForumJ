<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/search.jsp,v 1.118 2009/10/27 10:51:00 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.118 $
 - $Date: 2009/10/27 10:51:00 $
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
<%@ page import="net.myvietnam.mvncore.util.StringUtil" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>
<%@ page import="net.myvietnam.mvncore.filter.EnableEmotionFilter" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>
<%@ page import="com.mvnforum.db.*" %>
<%@ page import="com.mvnforum.MVNForumGlobal" %>
<%@ page import="com.mvnforum.MVNForumResourceBundle" %>
<%@ page import="com.mvnforum.search.post.PostSearchQuery" %>

<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<%
  String key = ParamUtil.getParameterFilter(request, "key");// always disable HTML
  String highlightKey = ParamUtil.getParameter(request, "key", false);
  String member = ParamUtil.getParameterFilter(request, "member");// always disable HTML
  
  // below parameters are checked in the Handler
  String scopeInPost = ParamUtil.getParameterFilter(request, "scopeinpost");
  String forum = ParamUtil.getParameterFilter(request, "forum");
  String rows = ParamUtil.getParameterFilter(request, "rows");
  String sort = ParamUtil.getParameterFilter(request, "sort");
  String date = ParamUtil.getParameterFilter(request, "date");
  String beforeafter = ParamUtil.getParameterFilter(request, "beforeafter");
  String minattach = ParamUtil.getParameterFilter(request, "minattach");
  
  CategoryCache categoryCache = CategoryCache.getInstance();
  ForumCache forumCache = ForumCache.getInstance();
  Collection categoryBeans = categoryCache.getBeans();
  Collection forumBeans = forumCache.getBeans();
%>
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.searchresult.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script src="<%=contextPath%>/mvnplugin/mvnforum/js/vietuni.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/searchhi.js"></script>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">

<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>
    document.submitform.submit();
  }
}
function ValidateForm() {
  if (document.submitform.key.value == '' && document.submitform.member.value == '' && document.submitform.minattach.options[document.submitform.minattach.selectedIndex].value == "0") {
    alert("<fmt:message key="mvnforum.user.search.js.prompt.at_least_one_item"/> (<fmt:message key="mvnforum.user.search.search_text"/>, <fmt:message key="mvnforum.user.search.by_member"/>, <fmt:message key="mvnforum.user.search.by_attachment"/>)");
    return false;
  }
  return true;
}

function InitParam() {
  var date =        document.submitform.date;
  var beforeafter = document.submitform.beforeafter;
  var minattach =   document.submitform.minattach;
  <% if (scopeInPost.length() > 0) {%> 
    var id = document.getElementById("scope<%=scopeInPost%>");
    if (id != null) id.selected = true;
  <% }%>
  <% if (date.length() > 0) {%> 
    var id = document.getElementById("date<%=date%>");
    if (id != null) id.selected = true;
  <% }%>
  <% if (forum.length() > 0) {%>
    document.submitform.forum.value = <%=forum%>;
  <% }%>
  <% if (rows.length() > 0) {%>
    document.submitform.rows.value = <%=rows%>;
  <% }%>
  <% if (sort.length() > 0) {%>
    var id = document.getElementById("sort<%=sort%>");
    if (id != null) id.selected = true;
  <% }%>
  <% if (beforeafter.length() > 0) {%> 
    var id = document.getElementById("beforeafter<%=beforeafter%>");
    if (id != null) id.selected = true;
  <% }%>  
  <% if (minattach.length() > 0) {%> 
    var id = document.getElementById("minattach<%=minattach%>");
    if (id != null) id.selected = true;
  <% }%>  
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "search")%>"><fmt:message key="mvnforum.user.search.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.searchresult.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "search", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "search")%>
<input type="hidden" name="offset" value="0"/>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.search.prompt"/>:</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="key"><fmt:message key="mvnforum.user.search.search_text"/></label></td>
    <td><input type="text" id="key" name="key" value="<%=key%>" size="60" onkeyup="initTyper(this);" onkeydown="if (event.keyCode==13) SubmitForm();"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="member"><fmt:message key="mvnforum.user.search.by_member"/></label></td>
    <td><input type="text" id="member" name="member" value="<%=member%>" size="60"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="scopeinpost"><fmt:message key="mvnforum.user.search.scope_in_post"/></label></td>
    <td>
      <select id="scopeinpost" name="scopeinpost" class="noborder">
        <option id="scope3" value="3"><fmt:message key="mvnforum.user.search.scope_in_post.both_of_them"/></option>
        <option id="scope1" value="1"><fmt:message key="mvnforum.user.search.scope_in_post.only_title"/></option>
        <option id="scope2" value="2"><fmt:message key="mvnforum.user.search.scope_in_post.only_body"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="forum"><fmt:message key="mvnforum.user.search.in_category_forum"/></label></td>
    <td>
<%--    
      <select name="forum">
        <option value="0" selected><fmt:message key="mvnforum.user.search.all_forums"/></option>
<%
for (Iterator catIter = categoryBeans.iterator(); catIter.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)catIter.next();
    int categoryID = categoryBean.getCategoryID();
    if (MyUtil.canViewAtLeastOneForumInCategory(categoryID, permission) == false) continue; %>
        <option value=""></option>
        <option id="forum-<%=categoryID%>" value="-<%=categoryID%>"><%=categoryBean.getCategoryName()%></option>
        <option value="">---------------------------------</option>
    <%
    for (Iterator forumIter = forumBeans.iterator(); forumIter.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIter.next();
        if (forumBean.getCategoryID() != categoryID) continue;
        if (permission.canReadPost(forumBean.getForumID()) && (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) { %>
        <option id="forum<%=forumBean.getForumID()%>" value="<%=forumBean.getForumID()%>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=forumBean.getForumName()%></option>
<%      } //if
    } // for forum
}// for category %>
      </select>
--%>
    <%= request.getAttribute("Result") %>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="date"><fmt:message key="mvnforum.user.search.by_date"/></label></td>
    <td>
      <select id="date" name="date">
        <option id="date0" value="0"><fmt:message key="mvnforum.common.search.date.any_date"/></option>
        <option id="date1" value="1"><fmt:message key="mvnforum.common.search.date.yesterday"/></option>
        <option id="date7" value="7"><fmt:message key="mvnforum.common.search.date.a_week"/></option>
        <option id="date14" value="14"><fmt:message key="mvnforum.common.search.date.2_weeks"/></option>
        <option id="date30" value="30"><fmt:message key="mvnforum.common.search.date.a_month"/></option>
        <option id="date90" value="90"><fmt:message key="mvnforum.common.search.date.3_months"/></option>
        <option id="date180" value="180"><fmt:message key="mvnforum.common.search.date.6_months"/></option>
        <option id="date365" value="365"><fmt:message key="mvnforum.common.search.date.a_year"/></option>
      </select>
      <select name="beforeafter">
        <option id="beforeafter1" value="1"><fmt:message key="mvnforum.common.search.date.newer"/></option>
        <option id="beforeafter2" value="2"><fmt:message key="mvnforum.common.search.date.older"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="minattach"><fmt:message key="mvnforum.user.search.by_attachment"/></label></td>
    <td>
      <fmt:message key="mvnforum.user.search.attachment.has_at_least"/>
      &nbsp;
      <select id="minattach" name="minattach" class="noborder">
        <option id="minattach0" value="0">0</option>
        <option id="minattach1" value="1">1</option>
        <option id="minattach2" value="2">2</option>
        <option id="minattach3" value="3">3</option>
        <option id="minattach4" value="4">4</option>
        <option id="minattach5" value="5">5</option>
        <option id="minattach6" value="6">6</option>
        <option id="minattach7" value="7">7</option>
        <option id="minattach8" value="8">8</option>
        <option id="minattach9" value="9">9</option>
      </select>
      &nbsp;
      <fmt:message key="mvnforum.common.attachments"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="sort"><fmt:message key="mvnforum.common.sort_by"/></label></td>
    <td>
      <select id="sort" name="sort">
        <% String default_search_order_by = Integer.toString(MVNForumConfig.getDefaultSearchOrderBy());%>
        <option id="sort0" value="<%=PostSearchQuery.SEARCH_SORT_DEFAULT%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_DEFAULT).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>><fmt:message key="mvnforum.user.search.sort.default"/></option>
        <option id="sort1" value="<%=PostSearchQuery.SEARCH_SORT_TIME_DESC%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_TIME_DESC).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>><fmt:message key="mvnforum.user.search.sort.time_desc"/></option>
        <option id="sort2" value="<%=PostSearchQuery.SEARCH_SORT_TIME_ASC%>" <%= (String.valueOf(PostSearchQuery.SEARCH_SORT_TIME_ASC).equals(default_search_order_by)) ? "selected=\"selected\"" : "" %>><fmt:message key="mvnforum.user.search.sort.time_asc"/></option>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="rows"><fmt:message key="mvnforum.user.search.result_per_page"/></label></td>
    <td>
      <select id="rows" name="rows" size="1">
        <option value="10">10</option>
        <option value="20" selected="selected">20</option>
        <option value="30">30</option>
        <option value="50">50</option>
        <option value="100">100</option>
      </select>
    </td>
  </tr>
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
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.search"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption"/>
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>

<br/>

<%
Collection postBeans = (Collection) request.getAttribute("PostBeans");
int totalPosts = 0;
if (postBeans != null) {
  int resultPerPage = ((Integer)request.getAttribute("rows")).intValue();
  totalPosts = ((Integer)request.getAttribute("TotalPosts")).intValue();
%>

<pg:pager
  url="search"
  items="<%= totalPosts %>"
  maxPageItems="<%= resultPerPage %>"
  isOffset="true"
  export="offset,currentPageNumber=pageNumber"
  scope="request">
<% String rowsType = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.common.numberof.posts"); %>
<%-- keep track of preference --%>
<pg:param name="key"/>
<pg:param name="member"/>
<pg:param name="scopeinpost"/>
<pg:param name="forum"/>
<pg:param name="sort"/>
<pg:param name="date"/>
<pg:param name="beforeafter"/>
<pg:param name="minattach"/>
<pg:param name="rows"/>

<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>

<table id="searcharea" class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td class="nosearchhi" colspan="2"><fmt:message key="mvnforum.common.post"/></td>
    <td class="nosearchhi"><fmt:message key="mvnforum.common.forum"/></td>
    <td class="nosearchhi" align="center"><fmt:message key="mvnforum.common.post.post_date"/></td>
  </tr>
<%
  String _highlightKey = "";
  if (highlightKey.length() > 0) {
    _highlightKey = "&amp;hl=" + Encoder.encodeURL(highlightKey);
  }
  
  for (Iterator iterator = postBeans.iterator(); iterator.hasNext(); ) {
    PostBean postBean = (PostBean)iterator.next();
%>
<pg:item>
  <tr class="<mvn:cssrow/>">
    <td width="16"><%=EnableEmotionFilter.filter(postBean.getPostIcon(), contextPath + MVNForumGlobal.EMOTION_DIR)%></td>
    <td>
    <% if (postBean.getPostAttachCount() > 0) { %>
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/icon/attach.gif" alt=""/>
    <% } %>
      <a class="messageTopic" href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + postBean.getThreadID() + _highlightKey + "#" + postBean.getPostID())%>">
      <%=MyUtil.filter(postBean.getPostTopic(), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%></a>
      <br/>
      <%=MyUtil.filter(StringUtil.getShorterString(postBean.getPostBody(), MVNForumConfig.getMaxCharsInShortSummary()), false/*html*/, true/*emotion*/, false/*mvnCode*/, false/*newLine*/, false/*URL*/)%>
    </td>
    <td><a class="messageTopic" href="<%=urlResolver.encodeURL(request, response, "listthreads?forum=" + postBean.getForumID())%>"><%=forumCache.getBean(postBean.getForumID()).getForumName()%></a></td>
    <td align="center">
      <%=onlineUser.getGMTTimestampFormat(postBean.getPostCreationDate())%><br/><fmt:message key="mvnforum.common.by"/>
      <a href="<%=urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(postBean.getMemberName()))%>" class="memberName"><%=postBean.getMemberName()%></a>
    </td>
  </tr>
</pg:item>
<%} //for
  if (totalPosts == 0) {
%>
  <tr class="<mvn:cssrow/>"><td class="nosearchhi" colspan="4" align="center"><fmt:message key="mvnforum.user.searchresult.no_post"/></td></tr>
<%}%>
</mvn:cssrows>
</table>
<table width="95%" align="center">
  <tr>
    <td>
      <%@ include file="inc_pager.jsp"%>
    </td>
  </tr>
</table>
</pg:pager>
<%
} // end (postBeans != null)
%>  
<br/>
<%@ include file="footer.jsp"%>
<script type="text/javascript">
//<![CDATA[
    InitParam();
  <%if (highlightKey.length() > 0) {%>    
    searchhi.highlightWords(document.getElementById("searcharea"),"<%=StringUtil.escapeJavaScript(highlightKey)%>");
  <%}%>    
//]]>
</script>
</mvn:body>
</mvn:html>
</fmt:bundle>
