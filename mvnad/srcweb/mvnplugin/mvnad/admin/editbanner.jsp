<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/editbanner.jsp,v 1.17 2009/08/13 10:53:58 thonh Exp $
 - $Author: thonh $
 - $Revision: 1.17 $
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
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page errorPage="fatalerror.jsp"%>

<%@ page import="java.util.*" %>
<%@ page import="java.io.File"%>
<%@ page import="com.mvnsoft.mvnad.MVNAdConfig"%>
<%@ page import="com.mvnsoft.mvnad.db.*"%>
<%@ page import="net.myvietnam.mvncore.util.DateUtil" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<%
  BannerBean adBannerBean = (BannerBean) request.getAttribute("bannerBean");
  File[] bannerFiles = (File[]) request.getAttribute("BannerFiles");
%>
<fmt:bundle basename="i18n/mvnad/mvnad_i18n">
<mvn:html>
<mvn:head>
  <mvn:title><fmt:message key="mvnad.common.ad.title_name"/> - <fmt:message key="mvnad.admin.editbanner.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnad/meta.jsp"%>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnad/js/md5.js"></script>
<link href="<%=contextPath%>/mvnplugin/mvnad/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//<![CDATA[
function checkEnter(event) {   
  var agt=navigator.userAgent.toLowerCase();
  
  // Maybe, Opera make an onClick event when user press enter key 
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitForm();
}
function endsWith(imageURL, extension) {
  if (imageURL.lastIndexOf(extension) == -1) return false;
  if ((imageURL.lastIndexOf(extension) + extension.length) != imageURL.length) return false;
  return true; 
}
function changeBannerImageURL(newBannerImageURL) {
  if (newBannerImageURL.indexOf("/") == -1) {
    newBannerImageURL = "<%=onlineUser.getMemberName().toLowerCase()+"/"%>"+newBannerImageURL;
  }
  document.getElementById("BannerImageURL").value=newBannerImageURL;
  newBannerImageURL='<%=ParamUtil.getServerPath() + ParamUtil.getContextPath() + MVNAdConfig.getWebUploadFolder() + "/"%>' + newBannerImageURL;
  
  if (endsWith(newBannerImageURL,".jpg") || endsWith(newBannerImageURL,".jpeg") || endsWith(newBannerImageURL,".gif") || endsWith(newBannerImageURL,".png")) {
    document.getElementById("preview").innerHTML = "<img src='" + newBannerImageURL + "' alt='Preview Image'>";
  } else if (endsWith(newBannerImageURL,".swf")) {
    document.getElementById("preview").innerHTML = "<embed src='" + newBannerImageURL + "' id='previewFlash' quality='high' scale='noborder' wmode='transparent' bgcolor='#000000' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash'>";
  } else if (endsWith(newBannerImageURL,".avi") || endsWith(newBannerImageURL,".wmv") || endsWith(newBannerImageURL,".mpg")) {
    document.getElementById("preview").innerHTML = "<embed src='" + newBannerImageURL + "' height='200' EnableContextMenu='0' AutoStart='1' loop='1' ShowStatusBar='0' ShowControls='0'></embed>";
  } else if (endsWith(newBannerImageURL,".mov")) {
    document.getElementById("preview").innerHTML = "<OBJECT classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' height='200' codebase='http://www.apple.com/qtactivex/qtplugin.cab'><param name='src' value='" + newBannerImageURL + "'><param name='autoplay' value='true'><param name='controller' value='false'><param name='loop' value='true'><EMBED src='" + newBannerImageURL + "' height='200' autoplay='true' controller='false' loop='true' pluginspage='http://www.apple.com/quicktime/download/'></EMBED></OBJECT>";
  } else {
    document.getElementById("preview").innerHTML = ""; 
  }
}
function showBannerImageURL(radioID) {
  document.getElementById("BannerImageURL_2").style.display = '';
  document.getElementById("BannerTargetURL_2").style.display = '';
  document.getElementById("previewDiv").style.display = '';
  document.getElementById("BannerHTMLCode").style.display = 'none';
}
function showBannerHTMLCode() {
  document.getElementById("BannerImageURL_2").style.display = 'none';
  document.getElementById("BannerTargetURL_2").style.display = 'none';
  document.getElementById("previewDiv").style.display = 'none';
  document.getElementById("BannerHTMLCode").style.display = '';
}
function ValidateForm() {  
  if (isBlank(document.editform.MemberName, "<fmt:message key="mvnad.common.banner.member_name"/>")) return false;
  if (isBlank(document.editform.BannerName, "<fmt:message key="mvnad.common.banner.name"/>")) return false;
  if (document.getElementById("BannerHTMLCode").style.display == "none") {
    if (isBlank(document.editform.BannerTargetURL, "<fmt:message key="mvnad.common.banner.target_url"/>")) return false;
    if (isBlank(document.editform.BannerImageURL, "<fmt:message key="mvnad.common.banner.image_url"/>")) return false;
  } else {
    if (isBlank(document.editform.BannerHTMLCode, "<fmt:message key="mvnad.common.banner.html_code"/>")) return false;
  }
  if (!isUnsignedInteger(document.editform.BannerWidth, "<fmt:message key="mvnad.common.banner.width"/>")) return false;
  if (!isUnsignedInteger(document.editform.BannerHeight, "<fmt:message key="mvnad.common.banner.height"/>")) return false;
  if (document.editform.MemberCurrentMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnad.common.js.prompt.invalidlongpassword"/>");
    document.editform.MemberCurrentMatkhau.focus();
    return false;
  }
  return true;
}
function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.editform.MemberCurrentMatkhau, document.editform.md5pw);
    }
    document.editform.submitbutton.disabled=true;
    document.editform.submit();
  }
}
//]]>
</script>
</mvn:head>
<mvn:body onunload="document.editform.submitbutton.disabled=false;">

<%@ include file="header.jsp"%>
<br />

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnad.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "listbanners")%>"><fmt:message key="mvnad.admin.listbanners.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnad.admin.editbanner.title"/>
</div>
<br />

<div class="pagedesc">
  <fmt:message key="mvnad.admin.editbanner.info"/>
</div>
<br />

<div id="previewDiv"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%> style="display:none"<%}%>>
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

<form name="editform" action="<%=urlResolver.encodeURL(request, response, "editbannerprocess", URLResolverService.ACTION_URL)%>" method="post" onsubmit="return false;">
<%=urlResolver.generateFormAction(request, response, "editbannerprocess")%>
<mvn:securitytoken />
<input type="hidden" name="BannerID" value="<%=adBannerBean.getBannerID()%>" />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.admin.editbanner.title"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="MemberName"><fmt:message key="mvnad.common.banner.member_name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="MemberName" name="MemberName" size="70" value="<%=adBannerBean.getMemberName()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerName"><fmt:message key="mvnad.common.banner.name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="BannerName" name="BannerName" size="70" value="<%=adBannerBean.getBannerName()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerDesc"><fmt:message key="mvnad.common.banner.description"/></label></td>
    <td><input type="text" id="BannerDesc" name="BannerDesc" size="70" value="<%=adBannerBean.getBannerDesc()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.type"/><span class="requiredfield"> *</span></td>
    <td>
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_IMAGE%>" id="ImageType" onclick="showBannerImageURL();" class="noborder"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_IMAGE) {%> checked="checked"<%}%> /><fmt:message key="mvnad.common.banner.type.image"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_FLASH%>" id="FlashType" onclick="showBannerImageURL();" class="noborder"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_FLASH) {%> checked="checked"<%}%> /><fmt:message key="mvnad.common.banner.type.flash"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_MOVIE%>" id="MovieType" onclick="showBannerImageURL();" class="noborder"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_MOVIE) {%> checked="checked"<%}%> /><fmt:message key="mvnad.common.banner.type.movie"/><br />
      <input type="radio" name="BannerType" value="<%=BannerBean.BANNER_TYPE_HTML%>" id="HTMLType" onclick="showBannerHTMLCode();" class="noborder"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%> checked="checked"<%}%> /><fmt:message key="mvnad.common.banner.type.html"/><br />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerHTMLCode"<%if (adBannerBean.getBannerType() != BannerBean.BANNER_TYPE_HTML) {%> style="display:none;"<%}%>>
    <td><label for="BannerHTMLCode"><fmt:message key="mvnad.common.banner.html_code"/><span class="requiredfield"> *</span></label></td>
    <td valign="top"><textarea cols="70" id="BannerHTMLCode" name="BannerHTMLCode" rows="18"><%=adBannerBean.getBannerHtmlCode()%></textarea></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerTargetURL_2"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%> style="display:none;"<%}%>>
    <td><label for="BannerTargetURL"><fmt:message key="mvnad.common.banner.target_url"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="BannerTargetURL" name="BannerTargetURL" size="70" value="<%=adBannerBean.getBannerTargetURL()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>" id="BannerImageURL_2"<%if (adBannerBean.getBannerType() == BannerBean.BANNER_TYPE_HTML) {%> style="display:none;"<%}%>>
    <td><label for="BannerImageURL"><fmt:message key="mvnad.common.banner.image_url"/><span class="requiredfield"> *</span></label></td>
    <td>   
      <input type="text" id="BannerImageURL" name="BannerImageURL" value="<%=adBannerBean.getBannerImageURL()%>" size="70" /><br />
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
    <td><input type="text" id="BannerWidth" name="BannerWidth" size="13" value="<%=adBannerBean.getBannerWidth()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerHeight"><fmt:message key="mvnad.common.banner.height"/></label></td>
    <td><input type="text" id="BannerHeight" name="BannerHeight" size="13" value="<%=adBannerBean.getBannerHeight()%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerWeight"><fmt:message key="mvnad.common.banner.weight"/></label></td>
    <td><input type="text" id="BannerWeight" name="BannerWeight" size="13" value="1" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerZonePositionX"><fmt:message key="mvnad.common.banner.zone_position_x"/></label></td>
    <td><input type="text" id="BannerZonePositionX" name="BannerZonePositionX" size="13" value="0" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="BannerZonePositionY"><fmt:message key="mvnad.common.banner.zone_position_y"/></label></td>
    <td><input type="text" id="BannerZonePositionY" name="BannerZonePositionY" size="13" value="0" readonly="readonly" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.start_date"/></td>
    <td>
      <%prefixName = "BannerStartDate";
        float startYear = 0;
        Timestamp currentTimestamp = adBannerBean.getBannerStartDate();
      %>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><fmt:message key="mvnad.common.banner.end_date"/></td>
    <td>
      <%prefixName = "BannerEndDate";
        currentTimestamp = adBannerBean.getBannerEndDate();
      %>
      <%@ include file="inc_date_option.jsp"%>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><label for="MemberCurrentMatkhau"><fmt:message key="mvnad.common.ad.your_pass"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="MemberCurrentMatkhau" name="MemberCurrentMatkhau" onkeypress="checkEnter(event)"/></td>
  </tr>
  <tr class="portlet-section-footer" align="center">
    <td colspan="2">
      <input type="button" value="<fmt:message key="mvnad.common.action.save"/>" class="portlet-form-button" onclick="SubmitForm()" name="submitbutton" />
      <input type="reset" value="<fmt:message key="mvnad.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>
<br />

<script lang="JavaScript" type="text/javascript">
//<![CDATA[
<%if (adBannerBean.getBannerType() != BannerBean.BANNER_TYPE_HTML) {%>
changeBannerImageURL("<%=adBannerBean.getBannerImageURL()%>");
<%}%>
//]]>
</script>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
