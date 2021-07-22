<%@ page import="com.mvnforum.service.MvnForumAdService"%>
<%--
 - This file could be used to add advertisement to mvnForum
 --%>
<%
  if (mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_AD_POSTITION_HEART) > 0) {
%>
<br />
<table align="center" cellpadding="3" cellspacing="0" width="95%" class="noborder">
   <tr>
     <td align="center"><%=mvnForumAdService.getZone(mvnForumAdService.getAdZone(MvnForumAdService.ZONE_NAME_FORUM_AD_POSTITION_HEART))%></td>
   </tr>
</table>
<br />
<%}%>
