<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/assigngrouptoforum.jsp,v 1.87 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.87 $
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
<%@ page import="com.mvnforum.auth.AbstractPermission" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.assigngrouptoforum.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/overlib.js"></script>
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
  if (document.submitform.group.value == -1) {
    alert("Please choose a group to continue.");
    return false;
  }
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<%
ForumBean forumBean = (ForumBean)request.getAttribute("ForumBean");
Collection groupsBeans = (Collection) request.getAttribute("GroupsBeans");
Collection groupForumBeans = (Collection) request.getAttribute("GroupForumBeans");
HashMap memberMap = (HashMap)request.getAttribute("MemberForumPermission");
HashMap groupMap = (HashMap)request.getAttribute("GroupForumPermission");
%>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "forummanagement")%>"><fmt:message key="mvnforum.admin.forummanagement.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.common.forum"/>: <%=forumBean.getForumName()%>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.assigngrouptoforum.title"/>
</div>
<br/>

<form action="<%=urlResolver.encodeURL(request, response, "editgroupforumpermission", URLResolverService.ACTION_URL)%>" name="submitform" <mvn:method/>>
<%=urlResolver.generateFormAction(request, response, "editgroupforumpermission")%>
<input type="hidden" name="forum" value="<%=forumBean.getForumID()%>" />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td width="50%" align="center"><fmt:message key="mvnforum.common.forum.forum_name"/></td>
    <td width="50%" align="center"><fmt:message key="mvnforum.common.group"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="center"><b><%=forumBean.getForumName()%></b></td>
    <td align="center">
    <select name="group">
      <option value="-1"><fmt:message key="mvnforum.common.prompt.choose_group"/></option>
      <%
      for (Iterator iterator = groupsBeans.iterator(); iterator.hasNext(); ) {
          GroupsBean groupsBean = (GroupsBean)iterator.next();
      %>
      <option value="<%=groupsBean.getGroupID()%>"><%=groupsBean.getGroupName()%></option>
      <%}%>
    </select>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.assigngrouptoforum.title"/>" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br/>

<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.common.permission.current_group_permission_in_forum"/> "<%=forumBean.getForumName()%>":</td>
  </tr>
</table>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td width="40%" align="center"><fmt:message key="mvnforum.common.group.group_name"/></td>
    <td width="30%" align="center"><fmt:message key="mvnforum.common.permission.combined_permission"/></td>
    <td width="30%" align="center"><fmt:message key="mvnforum.common.permission.individual_permission"/></td>
  </tr>
<%
if (groupMap.size() == 0) {
%>
  <tr class="<mvn:cssrow/>">
    <td colspan="3" align="center"><fmt:message key="mvnforum.common.permission.no_permission"/></td>
  </tr>
<%
} else {
    Set set = groupMap.keySet();
    for (Iterator iter = set.iterator(); iter.hasNext();) {
        String name = (String)iter.next();
        int id = -1;
        for (Iterator iterator = groupForumBeans.iterator(); iterator.hasNext(); ) {
            GroupForumBean groupForumBean = (GroupForumBean)iterator.next();
            if (groupForumBean.getGroupsBean().getGroupName().equals(name)) {
                id = groupForumBean.getGroupsBean().getGroupID();
                break;
            }
        }
        ArrayList[] pers = (ArrayList[])groupMap.get(name);
        StringBuffer combinedP = new StringBuffer();
        Collection combinedPerms = pers[0];
        for (Iterator it = combinedPerms.iterator(); it.hasNext(); ) {
            combinedP.append(it.next()).append("&lt;br /&gt;");
        }

        ArrayList individualPerms = pers[1];
        StringBuffer individualP = new StringBuffer();
        for (Iterator it = individualPerms.iterator(); it.hasNext(); ) {
            individualP.append(it.next()).append("&lt;br /&gt;");
        }
        %>
      <tr class="<mvn:cssrow/>">
        <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editgroupforumpermission?forum="+forumBean.getForumID()+ "&group=" + id)%>" class="command"><%=name%></a></td>
        <%if (combinedPerms.size() > 0) { %>
        <td align="center"><a href="#" onmouseover="return overlib('<%=combinedP%>')" onmouseout="return nd();"><fmt:message key="mvnforum.common.permission.combined_permission"/></a></td>
        <%} else { %>
        <td align="center"><fmt:message key="mvnforum.common.permission.no_combined_permission"/></td>
        <%}%>
        <%if (individualPerms.size() > 0) { %>
        <td align="center"><a href="#" onmouseover="return overlib('<%=individualP%>')" onmouseout="return nd();"><fmt:message key="mvnforum.common.permission.individual_permission"/></a></td>
        <%} else { %>
        <td align="center"><fmt:message key="mvnforum.common.permission.no_invidual_permission"/></td>
        <%}%>
      </tr>
<%  }//for
}%>
</mvn:cssrows> 
</table>

<br/><br/>
<table width="95%" align="center">
  <tr>
    <td class="highlight"><fmt:message key="mvnforum.common.permission.current_member_permission_in_forum"/> "<%=forumBean.getForumName()%>":</td>
  </tr>
  <tr>
    <td class="highlight"><a href="<%=urlResolver.encodeURL(request, response, "assignmembertoforum?forum=" + forumBean.getForumID())%>" class="command"><fmt:message key="mvnforum.admin.assigngrouptoforum.assign_member_to_this_forum"/></a></td>
  </tr>
</table>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td width="40%" align="center"><fmt:message key="mvnforum.common.member.login_name"/></td>
    <td width="30%" align="center"><fmt:message key="mvnforum.common.permission.combined_permission"/></td>
    <td width="30%" align="center"><fmt:message key="mvnforum.common.permission.individual_permission"/></td>
  </tr>
<%
if (memberMap.size() == 0) {
%>
  <tr class="<mvn:cssrow/>">
    <td colspan="3" align="center"><fmt:message key="mvnforum.common.permission.no_permission"/></td>
  </tr>
<%
} else {
    Set set = memberMap.keySet();
    for (Iterator iter = set.iterator(); iter.hasNext();) {
        String name = (String)iter.next();
        ArrayList [] pers = (ArrayList [])memberMap.get(name);
        StringBuffer combinedP = new StringBuffer();
        ArrayList combinedPerms = pers[0];
        for (Iterator it = combinedPerms.iterator(); it.hasNext(); ) {
            combinedP.append(it.next()).append("&lt;br /&gt;");
        }
        StringBuffer individualP = new StringBuffer();
        ArrayList individualPerms = pers[1];
        for (Iterator it = individualPerms.iterator(); it.hasNext(); ) {
            individualP.append(it.next()).append("&lt;br /&gt;");
        }
        %>
      <tr class="<mvn:cssrow/>">
        <td align="center"><a href="<%=urlResolver.encodeURL(request, response, "editmemberforumpermission?forum="+forumBean.getForumID()+"&member=" + name)%>" class="command"><%=name%></a></td>
        <%if (combinedPerms.size() > 0) { %>
        <td align="center"><a href="#" onmouseover="return overlib('<%=combinedP%>')" onmouseout="return nd();"><fmt:message key="mvnforum.common.permission.combined_permission"/></a></td>
        <%} else { %>
        <td align="center"><fmt:message key="mvnforum.common.permission.no_combined_permission"/></td>
        <%}%>
        <%if (individualPerms.size() > 0) { %>
        <td align="center"><a href="#" onmouseover="return overlib('<%=individualP%>')" onmouseout="return nd();"><fmt:message key="mvnforum.common.permission.individual_permission"/></a></td>
        <%} else { %>
        <td align="center"><fmt:message key="mvnforum.common.permission.no_invidual_permission"/></td>
        <%}%>
      </tr>
<%  }//for
}%>
</mvn:cssrows> 
</table>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>