<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/invalidsession.jsp,v 1.6 2008/06/13 09:23:25 phuongpdd Exp $
 - $Author: phuongpdd $
 - $Revision: 1.6 $
 - $Date: 2008/06/13 09:23:25 $
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
 --%>
<%@ taglib uri="mvntaglib" prefix="mvn" %>
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title>Invalid Session</mvn:title>
</mvn:head>
<mvn:body>
<table width="100%" height="100%">
  <tr>
    <td>
      <table width="80%" border="5" cellspacing="0" align="center" cellspadding="3">
        <tr>
          <td align="center">
            <font size="+1">
            &nbsp;<br/>
            Invalid Session. Please close your browser and try again.<br/>
            <br/>
            &nbsp;<br/>
            </font>
            Reason: one servlet session but be accessed by more than one IP.
            <br />
            Powered by <a href="http://www.mvnforum.com" target="_blank">mvnForum</a>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</mvn:body>
</mvn:html>
