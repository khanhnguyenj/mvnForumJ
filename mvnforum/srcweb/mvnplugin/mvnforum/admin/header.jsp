<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/header.jsp,v 1.136 2009/05/05 09:16:05 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.136 $
 - $Date: 2009/05/05 09:16:05 $
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
<%@ page import="net.myvietnam.mvncore.util.ParamUtil" %>
<%@ page import="com.mvnforum.user.UserModuleConfig" %>
<%@ page import="com.mvnforum.MVNForumConfig"%>
<%@ include file="inc_js_checkvalid_myvietnamlib.jsp"%>

<%  
if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) { %>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/domLib.js"> </script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/domMenu.js"> </script>
<%} %>
<table width="95%" class="noborder" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td width="252" rowspan="2">
      <a href="<%=MVNForumConfig.getLogoUrl()%>"><img src="<%=onlineUser.getLogoPath()%>" height="50" border="0" alt="mvnForum Homepage" /></a>
    </td>
    <td width="100%" height="25" align="right" class="portlet-font">
      <%boolean enableLogin = MVNForumConfig.getEnableLogin();
        if (onlineUser.isMember()) {%>
        <span class="welcomeHeader"> <fmt:message key="mvnforum.user.header.welcome"/></span> <font color="#FF0000"><%=memberName%></font>
        <%int newMessageCount = onlineUser.getNewMessageCount();
          if ( (newMessageCount > 0) && isServlet ) { %>
          (<a href="<%=urlResolver.encodeURL(request, response, "mymessage", URLResolverService.RENDER_URL, "view" /*portlet mode */)%>"><font size="1"><%=onlineUser.getNewMessageCount()%> <fmt:message key="mvnforum.common.message.header.new_private_message"/></font></a>)
        <%}%>
        <%if (enableLogin) {%>
        &nbsp;|&nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "logout")%>"><fmt:message key="mvnforum.common.action.logout"/></a>
        <%}%>
        <br/>
        <% if (MVNForumConfig.getEnableShowLastLogin()) {%>
          <fmt:message key="mvnforum.common.member.last_login"/>:&nbsp;<%=onlineUser.getGMTTimestampFormat(onlineUser.getLastLogonTimestamp())%>
          <fmt:message key="mvnforum.common.from_ip"/>&nbsp;<%=onlineUser.getLastLogonIP()%>
        <%}%>
      <%} else if ((memberName!=null) && (memberName.length()>0)) {%>
        <fmt:message key="mvnforum.user.header.welcome"/> <%=memberName%>
        <%if (enableLogin) {%>
        &nbsp;&nbsp;|&nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "login")%>"><fmt:message key="mvnforum.common.action.login"/></a>
        <%}%>
      <%} else {%>
        <fmt:message key="mvnforum.user.header.welcome"/> <%=MVNForumConfig.getDefaultGuestName()%>
        <%if (enableLogin) {%>
        &nbsp;&nbsp;|&nbsp;<a class="command" href="<%=urlResolver.encodeURL(request, response, "login")%>"><fmt:message key="mvnforum.common.action.login"/></a>
        <%}%>
      <%}%>
    </td>
  </tr>
  <tr>
    <td height="40" colspan="3" align="right" valign="bottom" nowrap="nowrap" class="zoneTitle"><fmt:message key="mvnforum.admin.header.admin_zone"/></td>
  </tr>
</table>

<% if (MVNForumConfig.getEnableUsePopupMenuInViewThread()) { %>
<script type="text/javascript">
//<![CDATA[
<%
  int menuID = 1; 
  int submenuID =1;
  double menuBarWidth = 0;
%>
  domMenu_data.set('domMenu_main', new Hash(
<%if (isServlet /*@see admin/inc_common.jsp */ ) { menuBarWidth +=12.5; %>  
    <%=menuID++%>, new Hash(
        'contents', 'Forum Index',
        'contentsHover', 'Forum Index',
        'uri', '<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view" /*portlet mode */)%>',
        'statusText', 'Mojavelinux.cozm homepages'   
        ),
<%}%>
    <%menuBarWidth +=12.5;%>
    <%=menuID++%>, new Hash(
        'contents', 'Admin Index',
        'contentsHover', 'Admin Index',
        'uri', '<%=urlResolver.encodeURL(request, response, "index")%>',
        'statusText', 'About mojavelinux.com'
        <%if (permission.canAdminSystem()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents', 'Test System Configuration',
            'uri', '<%=urlResolver.encodeURL(request, response, "testsystem")%>'         
        )
        <%}%>
        <%if (permission.canAdminSystem()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents', 'Miscellaneous Tasks',
            'uri',  '<%=urlResolver.encodeURL(request, response, "misctasks")%>',
            'statusText', 'Learn what powers mojavelinux.com'
        )
        <%}%>
        <%if (permission.canEditAtLeastOneForum() || permission.canAddForum()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents', 'Forum Management',
            'uri', '<%=urlResolver.encodeURL(request, response, "forummanagement")%>',
            'statusText', 'Read about Dan'
        )
        <%}%>
        <%if (permission.canAdminSystem()) { %>
        ,<%=submenuID++%>, new Hash(
            'contents','User Management',
            'uri', '<%=urlResolver.encodeURL(request, response, "usermanagement")%>',
            'statusText', 'Read about Dan'
        )
        <% } %>
        <%if (permission.canAdminSystem()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents','Group Management',
            'uri','<%=urlResolver.encodeURL(request, response, "groupmanagement")%>',
            'statusText', 'Read about Dan'
        )
        <%}%>
        <%if (permission.canAdminSystem()) {%>
        , <%=submenuID++%>, new Hash(
            'contents','Log File Management',
            'uri', '<%=urlResolver.encodeURL(request, response, "viewlogsystem")%>',
            'statusText', 'Read about Dan'
        )
        <%}%>
        <%if (permission.canSendMail()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents','Send Mail',
            'uri', '<%=urlResolver.encodeURL(request, response, "sendmail")%>',
            'statusText', 'Read about Dan'
        )
        <%}%>
        <%if (permission.canAdminSystem()) {%>
        ,<%=submenuID++%>, new Hash(
            'contents','Config mvnForum',
            'uri', '<%=urlResolver.encodeURL(request, response, "configindex")%>',
            'statusText', 'Read about Dan'
        )
        <%}%>)
<%
int index = 1;
if (permission.canAdminSystem()) {%>
    <%menuBarWidth +=12.5;%>
    ,<%=menuID++%>, new Hash(
        'contents', 'Miscellaneous Tasks',
        'contentsHover', 'Miscellaneous Tasks',
        'uri', '<%=urlResolver.encodeURL(request, response, "misctasks")%>',
        'statusText', 'Demo Sites',
        <%=index++%>, new Hash(
            'contents','Test System Configuration',
            'uri', '<%=urlResolver.encodeURL(request, response, "testsystem")%>',
            'statusText', 'Dynamic hierarchial menu using the DOM'
        ),
        <%=index++%>, new Hash(
            'contents', 'Config mvnForum', 
            'uri', '<%=urlResolver.encodeURL(request, response, "configindex")%>',
            'statusText', 'Dynamic tooltips using the DOM'
        ),
        <%=index++%>, new Hash(
            'contents', 'Edit Email Templates',
            'uri', '<%=urlResolver.encodeURL(request, response, "edittemplate")%>',
            'statusText', 'Date selector popup calendar'
        ),
        <%=index++%>, new Hash(
            'contents','List Event Logs',
            'uri', '<%=urlResolver.encodeURL(request, response, "listeventlogs")%>',
            'statusText', 'Read about Dan'
        ),
        <%=index++%>, new Hash(
            'contents','Edit banned emails',
            'uri', '<%=urlResolver.encodeURL(request, response, "listbannedemails")%>',
            'statusText', 'Read about Dan'
         ),        
        <%=index++%>, new Hash(
            'contents','IP Address Filter',
            'uri', '<%=urlResolver.encodeURL(request, response, "listbannedips")%>',
            'statusText', 'Read about Dan'
      ),
        <%=index++%>, new Hash(
            'contents','Banned User Names List',
            'uri', '<%=urlResolver.encodeURL(request, response, "listbannedusernames")%>',
            'statusText', 'Read about Dan'
      ),
      <%=index++%>, new Hash(
            'contents','Bad Words List',
            'uri', '<%=urlResolver.encodeURL(request, response, "listbannedwords")%>',
            'statusText', 'Read about Dan'
      ), 
      <%=index++%>, new Hash(
            'contents','Rebuild Search Index',
            'uri', '<%=urlResolver.encodeURL(request, response, "misctasks#rebuildindex")%>',
            'statusText', 'Read about Dan'
      ) 
      )
<%}%>
<%if ( permission.canEditAtLeastOneForum() || permission.canAddForum() ) {
     index=1;%>
     <%menuBarWidth +=12.5;%>
    ,<%=menuID++%>, new Hash(
        'contents', 'Forum Management',
        'contentsHover','Forum Management',
        'uri', '<%=urlResolver.encodeURL(request, response, "forummanagement")%>'     
        <%if (permission.canAddCategory()) {%>
        ,<%=index++%>, new Hash(
            'contents', 'Add new Category',
            'uri', '<%=urlResolver.encodeURL(request, response, "addcategory")%>',
            'statusText', 'Creating hash tables in javascript'
        )
        <%}%>
        <%if (permission.canAddForum()) {%>
        ,<%=index++%>, new Hash(
            'contents', 'Add new Forum',
            'uri', '<%=urlResolver.encodeURL(request, response, "addforum")%>',
            'statusText', 'Understanding Events in javascript'
        )
        <%}%>
        )
<%}%>
<%
int i = 0;
if (permission.canAdminSystem()) { %>
     <%menuBarWidth +=12.5;%>
    ,<%=menuID++%>, new Hash(
        'contents', 'User Management',
        'contentsHover', 'User Management',
        'uri', '<%=urlResolver.encodeURL(request, response, "usermanagement")%>',   
        <%= ++i %>, new Hash(
            'contents', 'Add new Member',
            'uri', '<%=urlResolver.encodeURL(request, response, "addmember")%>',
            'statusText', 'An artist portfolio site'
        ), 
        <%= ++i %>, new Hash(
            'contents', 'Search Member',
            'uri', '<%=urlResolver.encodeURL(request, response, "searchmember")%>',
            'statusText', 'Of course I designed my own site'
        ),
        <%= ++i %>, new Hash(
            'contents', 'Permission Summary',
            'uri', '<%=urlResolver.encodeURL(request, response, "permissionsummary")%>',
            'statusText', 'Of course I designed my own site'
        ),
        <%= ++i %>, new Hash(
            'contents', 'Rank Management',
            'uri', '<%=urlResolver.encodeURL(request, response, "rankmanagement")%>',
            'statusText', 'Of course I designed my own site'
      ),
      <%= ++i %>, new Hash(
            'contents', 'Send activation mail to all non-activated members',
            'uri', '<%=urlResolver.encodeURL(request, response, "sendactivatemailtoallprocess")%>',
            'statusText', 'Of course I designed my own site'
     ))
<% } %>
<%if (permission.canAdminSystem()) {%>         
    <%menuBarWidth +=12.5;%>
    ,<%=menuID++%>, new Hash(
        'contents', 'Group Management',
        'contentsHover', 'Group Management',
        'uri', '<%=urlResolver.encodeURL(request, response, "groupmanagement")%>',
        'statusText', 'Recommended reading',
        1, new Hash(
            'contents', 'Add new Group', 
            'uri', '<%=urlResolver.encodeURL(request, response, "addgroup")%>',
            'statusText', 'Recommended tech books'
    ))
<%}%>
<%if (permission.canSendMail()) {%>
    <%menuBarWidth +=12.5;%>
    ,<%=menuID++%>, new Hash(
        'contents', 'Send Mail',
        'contentsHover', 'Send Mail',
        'uri', '<%=urlResolver.encodeURL(request, response, "sendmail")%>',
        'statusText', 'Recommended reading'      
    )
<%}%>
));

// }}}
// {{{ domMenu_main: settings

domMenu_settings.set('domMenu_main', new Hash(
    'menuBarWidth', '<%=menuBarWidth%>%',
    'subMenuWidthCorrection', -1,
    'verticalSubMenuOffsetX', -1,
    'verticalSubMenuOffsetY', -1,
    'horizontalSubMenuOffsetX', domLib_isOpera ? 0 : 1,
    'horizontalSubMenuOffsetY', domLib_isOpera ? -1 : 0,
    'openMouseoverMenuDelay', 100,
    'closeMouseoutMenuDelay', 300,
    'distributeSpace', false,
    'expandMenuArrowUrl', 'arrow.gif',
    'menuBarClass', 'domMenu_menuBar_admin',
    'menuElementClass', 'domMenu_menuElement_admin',
    'menuElementHoverClass', 'domMenu_menuElementHover_admin',
    'menuElementActiveClass', 'domMenu_menuElementHover_admin',
    'subMenuBarClass', 'domMenu_subMenuBar_admin',
    'subMenuElementClass', 'domMenu_subMenuElement_admin',
    'subMenuElementHoverClass', 'domMenu_subMenuElementHover_admin',
    'subMenuElementActiveClass', 'domMenu_subMenuElementHover_admin'
));
//]]>
</script>
<link href="<%=contextPath%>/mvnplugin/mvnforum/css/menu.css" rel="stylesheet" type="text/css">
<div width="95%" align="center">
  <div id="domMenu_main"></div>
</div>  
     <script type="text/javascript">
      //<![CDATA[
        domMenu_activate('domMenu_main');
      //]]>
     </script>  
<%}else{%>
<div class="topmenu">
  <%if (isServlet /*@see admin/inc_common.jsp */ ) {%>
      <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view" /*portlet mode */)%>" class="topmenu"><fmt:message key="mvnforum.admin.header.forum_index"/></a>
      &nbsp;|&nbsp;
   <%}%>
      <a href="<%=urlResolver.encodeURL(request, response, "index")%>" class="topmenu"><fmt:message key="mvnforum.admin.header.admin_index"/></a>
   <%if (permission.canAdminSystem()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>" class="topmenu"><fmt:message key="mvnforum.admin.misctasks.title"/></a>
   <%}%>
   <%if ( permission.canEditAtLeastOneForum() || permission.canAddForum() ) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "forummanagement")%>" class="topmenu"><fmt:message key="mvnforum.admin.forummanagement.title"/></a>
   <%}%>
   <%if (permission.canModerateUser()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "usermanagement")%>" class="topmenu"><fmt:message key="mvnforum.admin.usermanagement.title"/></a>
   <%}%>
   <%if (permission.canAdminSystem()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "groupmanagement")%>" class="topmenu"><fmt:message key="mvnforum.admin.groupmanagement.title"/></a>
   <%}%>
   <%if (MVNForumConfig.getEnableVote() && permission.canManageVote() && (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) && isServlet) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "listperiod")%>" class="topmenu"><fmt:message key="mvnforum.admin.listperiodx.title"/></a>
   <%}%>
   <%if (permission.canSendMail()) {%>
      &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "sendmail")%>" class="topmenu"><fmt:message key="mvnforum.admin.sendmail.title"/></a>
   <%}%>
</div>
<%}%>

