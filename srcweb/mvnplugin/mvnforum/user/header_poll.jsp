<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/header_poll.jsp,v 1.5 2009/11/12 06:45:35 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.5 $
 - $Date: 2009/11/12 06:45:35 $
 -
 - ====================================================================
 -
 - Copyright (C) 2005 by MyVietnam.net
 -
 - MyVietnam.net PROPRIETARY/CONFIDENTIAL PROPERTIES. Use is subject to license terms.
 - You CANNOT use this software unless you receive a written permission from MyVietnam.net
 -
 - @author: MyVietnam.net developers
 -
 --%>
<%@ include file="inc_js_checkvalid_myvietnamlib.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<div class="topmenu" style="margin: 0pt auto; width: 100%;">  
  <a href="<%=urlResolver.encodeURL(request, response, "", URLResolverService.ACTION_URL)%>" class="topmenu"><fmt:message key="mvnforum.user.header.index"/></a>
  &nbsp;|&nbsp;<a href="<%=urlResolver.encodeURL(request, response, "listpolls")%>" class="topmenu"><fmt:message key="mvnforum.user.listpollsx.title"/></a>
</div>
</fmt:bundle>
