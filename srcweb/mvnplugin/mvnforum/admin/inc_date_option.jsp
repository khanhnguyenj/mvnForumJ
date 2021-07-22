<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/inc_date_option.jsp,v 1.3 2008/10/28 08:28:23 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.3 $
 - $Date: 2008/10/28 08:28:23 $
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
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.mvnforum.auth.OnlineUserManager" %>
<%@ page import="net.myvietnam.mvncore.util.DateUtil"%>

<%!
// class variables
private String m_dateDayName = "day";
private String m_dateMonthName = "month";
private String m_dateYearName = "year";
private DateFormatSymbols dfs;
private String[] monthNames;
%>
<%
  int dd   = 0;
  int mm   = 0;
  int yyyy = 0;
  if (request.getAttribute("TimeToDisplay") != null) {
      
    Timestamp timeToDisplay = (Timestamp)request.getAttribute("TimeToDisplay");
    Calendar cld = Calendar.getInstance();
    cld.setTime(timeToDisplay);
    dd = cld.get(Calendar.DAY_OF_MONTH);
    mm = cld.get(Calendar.MONTH) + 1;
    yyyy = cld.get(Calendar.YEAR);
  }
%>
<select name="<%=m_dateDayName%>">
  <option value="">--</option>
  <%
  for (int d = 1; d <= 31; d++) {
  %>
    <option value="<%=d%>" <%if (d == dd) {%>selected="selected"<%} %>><%=d%></option>
  <%}%>  
</select>
<select name="<%=m_dateMonthName%>"><option value="">--</option>
<%
  dfs = new DateFormatSymbols(OnlineUserManager.getInstance().getOnlineUser(request).getLocale()); // get month names in current web User's locale settings...
  monthNames = dfs.getMonths();
  //I will remove UNDECIMBER (only for lunar calendars) 
  for (int m = 0; m <= monthNames.length - 2; m++) { %>
    <option value="<%=m+1%>" <%if (mm == m + 1) {%>selected="selected"<%} %>><%=monthNames[m]%></option>
<%
  }//for
%>
<%
Calendar calendar = Calendar.getInstance();
Timestamp now = DateUtil.getCurrentGMTTimestamp();
calendar.setTimeInMillis(now.getTime());
int currentYear = calendar.get(Calendar.YEAR);
%>
</select>
<select name="<%=m_dateYearName%>">
  <option value="">--</option>
  <%-- max: 10 years--%>
  <%for (int i = 0; i < 10; i++) {%>
    <option value="<%=currentYear + i%>" <%if (yyyy == currentYear + i) {%>selected="selected"<%} %>><%=currentYear + i%></option>
  <%} %>
</select>