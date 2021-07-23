<%--
  - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/header_album.jsp,v 1.2 2009/11/12 06:45:35 trungth Exp $
  - $Author: trungth $
  - $Revision: 1.2 $
  - $Date: 2009/11/12 06:45:35 $
  -
  - ====================================================================
  -
  - Copyright (C) 2002-2007 by MyVietnam.net
  -
  - MyVietnam.net PROPRIETARY/CONFIDENTIAL PROPERTIES. Use is subject to license terms.
  - You CANNOT use this software unless you receive a written permission from MyVietnam.net
  -
  - @author: MyVietnam.net developers
  -
--%>
<%@ include file="inc_js_checkvalid_myvietnamlib.jsp"%>

<%@ page import="com.mvnforum.MVNForumConfig"%>
<%@ page import="com.mvnforum.mobile.MobileModuleConfig" %>

<div class="topmenu" style="margin:0 auto; width:100%;"> 
<%if (onlineUser.isMember() && MVNForumConfig.getEnablePrivateAlbum() && permission.canUseAlbum()) {%>
                  <a href="<%=urlResolver.encodeURL(request, response, "myalbums")%>" class="topmenu"><fmt:message key="mvnforum.user.listalbumsx.my_albums"/></a>
                  &nbsp;|&nbsp;
                  <%}%> 
  <%if (MVNForumConfig.getEnablePublicAlbum()) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "listpublicalbums")%>" class="topmenu"><fmt:message key="mvnforum.user.listalbumsx.public_albums"/></a>
  <% } %>
</div>
