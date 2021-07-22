<%-- Tomcat 4 does not allow multiple contentType definition @ page contentType="text/html;charset=utf-8" --%>
<%@ page import="com.mvnforum.service.MvnForumCMSService" %>
<%--
 - This file could be used to add news to mvnForum
 --%>
<%
MvnForumCMSService mvnForumCMSService = MvnForumServiceFactory.getMvnForumService().getMvnForumCMSService();
%>
<table width="95%" border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td>
      <%if (MVNForumConfig.getCMSNewsViewMode() != MVNForumConfig.CMS_NEWS_VIEW_MODE_DISABLED && mvnForumCMSService.canRunNewsInMvnForum()) {%>
      <table width="<%=MVNForumConfig.getCMSNewsTableWidth()%>%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <br/>
            <%=mvnForumCMSService.getNewsInMvnForum()%>
          </td>
        </tr>
      </table>
      <%}%>
    </td>
  </tr>
</table>
