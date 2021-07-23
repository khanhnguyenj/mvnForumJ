<%--
 - This file could be used to add advertisement to mvnForum
 --%>
<%--
 - This file could be used to add advertisement to mvnForum
 --%>
</br>
<%!
int viewCount;
%>
<table class="tborder" width="95%" align="center" cellpadding="3" cellspacing="1">
  <tr class="trow1">
<%--
    <td align="center">
      <a href="http://www.mamnon.com" target="_blank">
      <img src="<%=contextPath%>/ad/mamnon.gif" alt="Tre em, Tr&#7867; em, mamnon, mam non, m&#7847;m non, Children, ch&#259;m s&#243;c, gi&#7843;i tr&#237;, gi&#7843;ng d&#7841;y, tr&#7867; th&#417;, thi&#7871;u nhi, gi&#225;o d&#7909;c, dien dan giao duc mam non, gi&#225;o &#225;n, giao an" border="0">
      </a>
    </td>
--%>

    <td align="center">
    <%@ taglib uri = "/WEB-INF/taglibs-ad.tld" prefix="page" %>
    <page:GetBannerByID bannerID="1"/>
    </td>


    <td align="center">
    <%@ taglib uri = "/WEB-INF/taglibs-ad.tld" prefix="page" %>
    <page:GetBannerByID bannerID="6"/>
    </td>

    <td align="center">
    <%@ taglib uri = "/WEB-INF/taglibs-ad.tld" prefix="page" %>
    <page:GetBannerByID bannerID="5"/>
    </td>

  </tr>
</table>
<br/>
