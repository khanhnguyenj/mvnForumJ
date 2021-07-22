<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/WEB-INF/mvnplugin/mvnforum/mobile/inc_common.jsp,v 1.5 2009/03/30 08:15:11 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.5 $
 - $Date: 2009/03/30 08:15:11 $
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
<%@ page import="net.myvietnam.mvncore.web.*" %>
<%@ page import="net.myvietnam.mvncore.service.EnvironmentService" %>
<%@ page import="net.myvietnam.mvncore.service.MvnCoreServiceFactory" %>
<%@ page import="net.myvietnam.mvncore.service.URLResolverService" %>
<%@ page import="com.mvnforum.auth.OnlineUser" %>
<%@ page import="com.mvnforum.auth.OnlineUserManager" %>
<%@ page import="com.mvnforum.auth.MVNForumPermission" %>
<%@ page import="com.mvnforum.db.DAOFactory" %>
<%@ page import="com.mvnforum.db.MemberDAO" %>
<%@ page import="com.mvnforum.MVNForumConfig" %>
<%@ page import="com.mvnforum.MyUtil" %>
<%@ page import="com.mvnforum.service.MvnForumInfoService" %>
<%@ page import="com.mvnforum.service.MvnForumServiceFactory" %>
<%@ page import="com.mvnforum.service.MvnForumAdService" %>
<%@ page import="com.mvnforum.common.ThreadIconUtil" %>

<%@ taglib uri="mvntaglib" prefix="mvn" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%
// I have to comment these below line because when back a page,
// the page is reload, so user have to type data again
//response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
//response.setHeader("Pragma", "no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

String contextPath = request.getContextPath();

URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
EnvironmentService environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();

OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
MVNForumPermission permission = onlineUser.getPermission();
String memberName = onlineUser.getMemberName();
int memberID = onlineUser.getMemberID();

String currentLocale = onlineUser.getLocaleName();
if (currentLocale.equals("")) currentLocale = MVNForumConfig.getDefaultLocaleName();
pageContext.setAttribute("currentLocale", currentLocale);
boolean isServlet = (environmentService.isPortlet() == false || request.getAttribute("javax.portlet.request") == null);
boolean isPortlet = !isServlet;
boolean externalUserDatabase = MVNForumConfig.getEnableExternalUserDatabase();
boolean internalUserDatabase = !externalUserDatabase;
MvnForumInfoService mvnForumInfo = MvnForumServiceFactory.getMvnForumService().getMvnForumInfoService();
MvnForumAdService mvnForumAdService = MvnForumServiceFactory.getMvnForumService().getMvnForumAdService();
MemberDAO memberDAO = DAOFactory.getMemberDAO();

String imagePath = contextPath + ThreadIconUtil.getImagePath(onlineUser);
%>
<fmt:setLocale value="${currentLocale}" />
<%
response.setContentType("text/html; charset=utf-8");
%>
