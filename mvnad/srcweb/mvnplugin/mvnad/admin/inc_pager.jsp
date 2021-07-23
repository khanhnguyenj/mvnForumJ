<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/inc_pager.jsp,v 1.2 2008/06/03 16:58:14 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.2 $
 - $Date: 2008/06/03 16:58:14 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2008 by MyVietnam.net
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
 - @author: MyVietnam.net developers
 -
 --%>
<pg:index export="pages, items">
<table cellspacing="0" cellpadding="0">
  <tr class="pager">
    <td align="right" nowrap="nowrap">
      <%= rowsType %>: <%= items %>&nbsp;&nbsp;&nbsp;<fmt:message key="mvnad.common.numberof.pages"/>: <%= pages %>&nbsp;&nbsp;&nbsp;[
      <pg:prev export="pageUrl"><a href="<%=urlResolver.encodeURL(request, response, pageUrl)%>" class="pager"><fmt:message key="mvnad.common.previous"/></a> |</pg:prev>
      <pg:pages>
        <% if (pageNumber == currentPageNumber) { %>
          <span class="pagerCurrent"><%= pageNumber %></span>
        <% } else { %>
          <a href="<%=urlResolver.encodeURL(request, response, pageUrl)%>" class="pager"><%= pageNumber%></a>
        <% } %>
      </pg:pages>
      <pg:next export="pageUrl">| <a href="<%=urlResolver.encodeURL(request, response, pageUrl)%>" class="pager"><fmt:message key="mvnad.common.next"/></a></pg:next> ]
    </td>
  </tr>
</table>
</pg:index>
