<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/inc_pages.jsp,v 1.1 2008/05/30 04:04:56 tbtrung Exp $
 - $Author: tbtrung $
 - $Revision: 1.1 $
 - $Date: 2008/05/30 04:04:56 $
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
 - @author: Trung Tang    
 --%>
<%if (postCount > memberPostsPerPage) {%>
    <br/>
    <fmt:message key="mvnforum.common.page"/>:
  <%if (totalPages <= 12) {
      for (int i = 0; i < totalPages; i++) {%>
    &nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadID + "&offset=" + (i*memberPostsPerPage))%>" class="messageTopic"><%=i + 1%></a>
    <%}
    } else {
      for (int i = 0; i < 5; i++) {%>
    &nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadID + "&offset=" + (i*memberPostsPerPage))%>" class="messageTopic"><%=i + 1%></a>
    <%}%>
    . . .
    <%for (int i = totalPages - 5; i < totalPages; i++) {%>
    &nbsp;<a href="<%=urlResolver.encodeURL(request, response, "viewthread?thread=" + threadID + "&offset=" + (i*memberPostsPerPage))%>" class="messageTopic"><%=i + 1%></a>
    <%}%>
  <%}
  }%>