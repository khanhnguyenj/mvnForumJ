<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/inc_timezone_option.jsp,v 1.11 2009/03/26 06:49:59 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.11 $
 - $Date: 2009/03/26 06:49:59 $
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
<select name="<%=timeZoneSelectName%>" id="timeZoneSelectName">
<option value="-12"<% if (selectedTimeZone == -12) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_12"/></option>
<option value="-11"<% if (selectedTimeZone == -11) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_11"/></option>
<option value="-10"<% if (selectedTimeZone == -10) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_10"/></option>
<option value="-9"<% if (selectedTimeZone == -9) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_9"/></option>
<option value="-8"<% if (selectedTimeZone == -8) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_8"/></option>
<option value="-7"<% if (selectedTimeZone == -7) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_7"/></option>
<option value="-6"<% if (selectedTimeZone == -6) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_6"/></option>
<option value="-5"<% if (selectedTimeZone == -5) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_5"/></option>
<option value="-4"<% if (selectedTimeZone == -4) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_4"/></option>
<option value="-3"<% if (selectedTimeZone == -3) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_3"/></option>
<option value="-2"<% if (selectedTimeZone == -2) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_2"/></option>
<option value="-1"<% if (selectedTimeZone == -1) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_minus_1"/></option>
<option value="0"<% if (selectedTimeZone == 0) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt"/></option>
<option value="1"<% if (selectedTimeZone == 1) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_1"/></option>
<option value="2"<% if (selectedTimeZone == 2) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_2"/></option>
<option value="3"<% if (selectedTimeZone == 3) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_3"/></option>
<option value="4"<% if (selectedTimeZone == 4) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_4"/></option>
<option value="5"<% if (selectedTimeZone == 5) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_5"/></option>
<option value="6"<% if (selectedTimeZone == 6) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_6"/></option>
<option value="7"<% if (selectedTimeZone == 7) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_7"/></option>
<option value="8"<% if (selectedTimeZone == 8) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_8"/></option>
<option value="9"<% if (selectedTimeZone == 9) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_9"/></option>
<option value="10"<% if (selectedTimeZone == 10) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_10"/></option>
<option value="11"<% if (selectedTimeZone == 11) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_11"/></option>
<option value="12"<% if (selectedTimeZone == 12) { %> selected="selected"<% } %>><fmt:message key="mvnforum.common.timezone.gmt_plus_12"/></option>
</select>

