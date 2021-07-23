<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/addbanner.jsp,v 1.22 2009/08/13 10:53:58 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.22 $
 - $Date: 2009/08/13 10:53:58 $
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
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.io.File"%>
<%@ page import="com.mvnsoft.mvnad.db.BannerBean"%>
<%@ page import="com.mvnsoft.mvnad.MVNAdConfig"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
File[] bannerFiles = (File[]) request.getAttribute("BannerFiles");
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.addbanner.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
  <link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//<![CDATA[
  var attNum = 0;
  function endsWith(imageURL, extension) {
    if (imageURL.lastIndexOf(extension) == -1) return false;
    if ((imageURL.lastIndexOf(extension) + extension.length) != imageURL.length) return false;
    return true; 
  }
  function changeBannerImageURL(newBannerImageURL) {
    document.getElementById("BannerImageURL").value = newBannerImageURL;
    newBannerImageURL = '<%=ParamUtil.getServerPath() + ParamUtil.getContextPath() + MVNAdConfig.getWebUploadFolder() + "/" + onlineUser.getMemberName().toLowerCase() + "/"%>' + newBannerImageURL;
    if (endsWith(newBannerImageURL,".jpg") || endsWith(newBannerImageURL,".jpeg") || endsWith(newBannerImageURL,".gif") || endsWith(newBannerImageURL,".png")) {
      document.getElementById("preview").innerHTML = "<img src='" + newBannerImageURL + "' alt='Preview Image'/>";
    } else if (endsWith(newBannerImageURL,".swf")) {
      document.getElementById("preview").innerHTML = "<embed src='" + newBannerImageURL + "' id='previewFlash' quality='high' scale='noborder' wmode='transparent' bgcolor='#000000' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash'></embed>";
    } else if (endsWith(newBannerImageURL,".avi") || endsWith(newBannerImageURL,".wmv") || endsWith(newBannerImageURL,".mpg")) {
      document.getElementById("preview").innerHTML = "<embed src='" + newBannerImageURL + "' height='200' EnableContextMenu='0' AutoStart='1' loop='1' ShowStatusBar='0' ShowControls='0'></embed>";
    } else if (endsWith(newBannerImageURL,".mov")) {
      document.getElementById("preview").innerHTML = "<OBJECT classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' height='200' codebase='http://www.apple.com/qtactivex/qtplugin.cab'><param name='src' value='" + newBannerImageURL + "'><param name='autoplay' value='true'><param name='controller' value='false'><param name='loop' value='true'><embed src='" + newBannerImageURL + "' height='200' autoplay='true' controller='false' loop='true' pluginspage='http://www.apple.com/quicktime/download/'></embed></OBJECT>";
    } else {
      document.getElementById("preview").innerHTML = ""; 
    }
  }
  function showBannerImageURL(radioID) {
    document.getElementById("BannerImageURL_2").style.display = "";
    document.getElementById("BannerTargetURL_2").style.display = "";
    document.getElementById("previewDiv").style.display = "";
    document.getElementById("BannerHTMLCode").style.display = "none";
  }
  function showBannerHTMLCode() {
    document.getElementById("BannerImageURL_2").style.display = "none";
    document.getElementById("BannerTargetURL_2").style.display = "none";
    document.getElementById("previewDiv").style.display = "none";
    document.getElementById("BannerHTMLCode").style.display = "";
  }
  function ValidateForm() {  
    if (isBlank(document.adform.MemberName, "<fmt:message key="mvnad.common.banner.member_name"/>")) return false;
    if (isBlank(document.adform.BannerName, "<fmt:message key="mvnad.common.banner.name"/>")) return false;
    if (document.getElementById("BannerHTMLCode").style.display == "none") {
      if (isBlank(document.adform.BannerTargetURL, "<fmt:message key="mvnad.common.banner.target_url"/>")) return false;
      if (isBlank(document.adform.BannerImageURL, "<fmt:message key="mvnad.common.banner.image_url"/>")) return false;
    } else {
      if (isBlank(document.adform.BannerHTMLCode, "<fmt:message key="mvnad.common.banner.html_code"/>")) return false;
    }
    if (!isUnsignedInteger(document.adform.BannerWidth, "<fmt:message key="mvnad.common.banner.width"/>")) return false;
    if (!isUnsignedInteger(document.adform.BannerHeight, "<fmt:message key="mvnad.common.banner.height"/>")) return false;
    return true;
  }
  function SubmitFormAddBanner() {
    if (ValidateForm()) {
      document.adform.submitbutton.disabled=true;
      document.adform.submit();
    }
  }
//]]>
</script>
</mvn:head>
<mvn:body onunload="document.adform.submitbutton.disabled=false;">

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listbanners")%>"><fmt:message key="mvnad.admin.listbanners.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.addbanner.title"/>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnad.admin.addbanner.info"/>
</div>

<br />
<%if (permission.canUploadMedia()) {%>
<%String frompage = "addbanner"; %>
<%@ include file="uploadmedia.jsp"%>
<%}%>

<div id="previewDiv">
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.common.banner.preview_image_url"/></td>
  </tr>
  <tr class="portlet-section-body">
    <td align="center" id="preview"><fmt:message key="mvnad.admin.addbanner.preview_image_url_desc"/></td>
  </tr>
</table>
<br />
</div>

<form action="<%=urlResolver.encodeURL(request, response, "addbannerprocess", URLResolverService.ACTION_URL)%>" name="adform" method="post">
<%=urlResolver.generateFormAction(request, response, "addbannerprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.addbanner.title"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberName"><fmt:message key="mvnad.common.banner.member_name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="MemberName" name="MemberName" size="33" value="<%=memberName%>" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerName"><fmt:message key="mvnad.common.banner.name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="BannerName" name="BannerName" size="33" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerDesc"><fmt:message key="mvnad.common.banner.description"/></label></td>
    <td><textarea id="BannerDesc" name="BannerDesc" cols="70" rows="3"></textarea></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.type"/><span class="requiredfield"> *</span></td>
    <td>
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_IMAGE%>" id="ImageType" onclick="showBannerImageURL();" class="noborder" checked="checked"/><fmt:message key="mvnad.common.banner.type.image"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_FLASH%>" id="FlashType" onclick="showBannerImageURL();" class="noborder" /><fmt:message key="mvnad.common.banner.type.flash"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_MOVIE%>" id="MovieType" onclick="showBannerImageURL();" class="noborder" /><fmt:message key="mvnad.common.banner.type.movie"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_HTML%>" id="HTMLType" onclick="showBannerHTMLCode();" class="noborder" /><fmt:message key="mvnad.common.banner.type.html"/><br />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerHTMLCode" style="display: none;">
    <td><label for="BannerHTMLCode"><fmt:message key="mvnad.common.banner.html_code"/><span class="requiredfield"> *</span></label></td>
    <td valign="top"><textarea cols="70" id="BannerHTMLCode" name="BannerHTMLCode" rows="18"></textarea></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerTargetURL_2">
    <td><label for="BannerTargetURL"><fmt:message key="mvnad.common.banner.target_url"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="BannerTargetURL" name="BannerTargetURL" size="70" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerImageURL_2">
    <td><label for="BannerImageURL"><fmt:message key="mvnad.common.banner.image_url"/><span class="requiredfield"> *</span></label></td>
    <td>
      <input type="text" id="BannerImageURL" name="BannerImageURL" size="70" value="" /><br />
      <label for="BannerImageFileName"><fmt:message key="mvnad.admin.addbanner.or_choose_media"/></label>
      <select id="BannerImageFileName" name="BannerImageFileName" onchange="changeBannerImageURL(this.options[this.selectedIndex].value)">
        <option value=""></option>
    <%for (int i = 0; i < bannerFiles.length; i++) {
        File bannerFile = bannerFiles[i];%>
        <option value="<%=bannerFile.getName()%>"><%=bannerFile.getName()%></option>
    <%}%>
      </select>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerWidth"><fmt:message key="mvnad.common.banner.width"/></label></td>
    <td><input type="text" id="BannerWidth" name="BannerWidth" value="100" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerHeight"><fmt:message key="mvnad.common.banner.height"/></label></td>
    <td><input type="text" id="BannerHeight" name="BannerHeight" value="100" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerWeight"><fmt:message key="mvnad.common.banner.weight"/></label></td>
    <td><input type="text" id="BannerWeight" name="BannerWeight" value="1" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerZonePositionX"><fmt:message key="mvnad.common.banner.zone_position_x"/></label></td>
    <td><input type="text" id="BannerZonePositionX" name="BannerZonePositionX" value="0" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerZonePositionY"><fmt:message key="mvnad.common.banner.zone_position_y"/></label></td>
    <td><input type="text" id="BannerZonePositionY" name="BannerZonePositionY" value="0" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.start_date"/></td>
    <td>
      <%prefixName = "BannerStartDate";
        float startYear = 0;
        Timestamp currentTimestamp = null;
      %>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.end_date"/></td>
    <td>
      <%prefixName = "BannerEndDate";%>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="portlet-section-footer" align="center">
    <td colspan="2">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnad.common.action.add"/>" onclick="javascript:SubmitFormAddBanner()" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnad.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br />
<%@ include file="footer.jsp"%>

</mvn:body>
</mvn:html>
</fmt:bundle>