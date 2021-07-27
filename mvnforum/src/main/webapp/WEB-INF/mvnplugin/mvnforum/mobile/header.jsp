<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/WEB-INF/mvnplugin/mvnforum/mobile/header.jsp,v 1.12 2009/04/29 08:09:58 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.12 $
 - $Date: 2009/04/29 08:09:58 $
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
 - @author: Nhan Luu Duy  
 --%>
<%@page import="com.mvnforum.MVNForumConfig"%>
<div id="header">
  <h1><img src="<%=contextPath%>/mvnplugin/mvnforum/images/mobile/logo.gif" alt="" /></h1>
  <h2><fmt:message key="mvnforum.user.header.welcome"/>
      <%if (onlineUser.isMember()){%> 
          <span class="log_name"><%=memberName%></span>
          <%if (MVNForumConfig.getEnableLogin()) {%>
            &nbsp;|&nbsp;<a href="logout"><fmt:message key="mvnforum.common.action.logout"/></a> 
          <%}%> 
      <%} else {%>
          <span class="log_name"><%=MVNForumConfig.getDefaultGuestName()%></span>
          <%if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {%>
          <a href="login"><fmt:message key="mvnforum.common.action.login"/></a>
          <%}%>
      <%}%>
  </h2>
</div>