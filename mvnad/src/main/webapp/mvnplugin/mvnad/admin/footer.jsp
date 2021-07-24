<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/footer.jsp,v 1.8 2010/06/10 04:32:32 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.8 $
 - $Date: 2010/06/10 04:32:32 $
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
<%@page import="net.myvietnam.mvncore.util.DateUtil"%>
<hr class="hrfooter"/>

<table width="95%" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td align="right" class="pageFooter">
      <fmt:message key="mvnad.common.footer.timezone"/><%if (onlineUser.getTimeZone() != 0) {%> <fmt:message key="mvnad.common.footer.timezone.gmt"/><%}%> <%=onlineUser.getTimeZoneFormat()%><br/>    
      <%--<fmt:message key="mvnad.common.footer.currenttime"/>--%><%=onlineUser.getGMTTimestampFormat(DateUtil.getCurrentGMTTimestamp())%>
    </td>
  </tr>
</table>

<hr class="hrfooter"/>
<br/>

<div align="center" class="pageFooter">
  <!--
  We request you retain the full copyright notice below including the link to www.mvnForum.com.
  This not only gives respect to the large amount of time given freely by the developers
  but also helps build interest, traffic and use of mvnAd. If you cannot (for good
  reason) retain the full copyright we request you at least leave in place the
  "Powered by mvnAd" line, with mvnAd linked to http://www.mvnForum.com.
  These conditions are parts of the licence this software is released under.
  For detailed information about mvnForum License Agreement, please look for the license 
  information in our site www.mvnforum.com to know more about the license of your software

  NOTE: In case you need to remove this "Powered by mvnForum", please contact us at 
  web site www.MyVietnam.net to order a special "Copyright notice Removal" license 
  with a small license fee. After receiving this special license, you can remove 
  the copyright notice in this footer.jsp file.

  MyVietnam.net Group
  -->
  Powered by <a href="http://www.MyVietnam.net" target="_blank"><%= mvnAdInfo.getProductDesc()%></a> (Build: <%= mvnAdInfo.getProductReleaseDate()%>)<br />
  Copyright &copy; 2002-2010 by <a href="http://www.MyVietnam.net" target="_blank">MyVietnam.net</a>
  <p>&nbsp;</p>
</div>
