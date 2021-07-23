<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/inc_date_option.jsp,v 1.4 2009/04/09 09:27:47 trungth Exp $
 - $Author: trungth $
 - $Revision: 1.4 $
 - $Date: 2009/04/09 09:27:47 $
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
<%-- not localized yet --%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="net.myvietnam.mvncore.util.DateUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Calendar"%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<%
int dd   = 0;
int mm   = 0;
int yyyy = 0;
Calendar calendar = Calendar.getInstance();
Timestamp now = DateUtil.getCurrentGMTTimestamp();

calendar.setTime(now);
if (currentTimestamp != null) {
    calendar.setTime(currentTimestamp);
}

dd = calendar.get(Calendar.DAY_OF_MONTH);
mm = calendar.get(Calendar.MONTH) + 1;
yyyy = calendar.get(Calendar.YEAR);
%>
<%-- Day --%>
<select name="<%=prefixName%>_Day">
<%for (int d = 1; d <= 31; d++) {%>
  <option value="<%=d%>"<%if (d == dd) {%> selected="selected"<%} %>><%=d%></option>
<%}%>  
</select>
<%-- Month --%>
<select name="<%=prefixName%>_Month">
<%DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(onlineUser.getLocale());
  String[] months = dateFormatSymbols.getMonths();
  for (int m = 0; m <= months.length - 2; m++) {%>
  <option value="<%=m+1%>"<%if (mm == m + 1) {%> selected="selected"<%} %>><%=months[m]%></option>
<%}%>  
</select>
<%-- Year --%>
<select name="<%=prefixName%>_Year">
<%-- startYear < 0: years from (2008 - |startYear|) to (2008 - |startYear| + 5)--%>
<%-- startYear >= 0: years from (2008 + startYear) to (2008 + startYear + 5)--%>
<%
int currentYear = (int) startYear + 2008;
for (int y = 0; y < 5; currentYear++, y++ ) {%>
  <option value="<%=currentYear%>" <%if (yyyy == currentYear) {%> selected="selected"<%} %>><%=currentYear%></option>
<%} %>
</select>
</fmt:bundle>