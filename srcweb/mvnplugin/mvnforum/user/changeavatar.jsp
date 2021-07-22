<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/changeavatar.jsp,v 1.78 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.78 $
 - $Date: 2009/07/16 03:21:13 $
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
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="com.mvnforum.db.*" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.user.changeavatar.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;document.form.button.disabled=false;">
<script type="text/javascript">
//<![CDATA[
function MM_findObj(n, d) { //v3.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;
}
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
 var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
   var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
   if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

//]]>
</script>

<script type="text/javascript">
//<![CDATA[
function SubmitForm() {
  if (ValidateForm()) {
    <mvn:servlet>
      document.submitform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.submitform.submit();
  }
}

function ValidateForm() {
  if (isBlank(document.submitform.PictureFile, "<fmt:message key="mvnforum.user.changeavatar.picture_file"/>")) return false;
  return true;
}

function handleChangeAvatar() {
  <mvn:servlet>
  document.form.button.disabled=true;
  </mvn:servlet>
  document.form.submit();
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "myprofile")%>"><fmt:message key="mvnforum.user.header.my_profile"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.user.changeavatar.title"/>
</div>
<br/>

<%
MemberBean memberBean = (MemberBean)request.getAttribute("MemberBean");
%>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.changeavatar.guide"/>
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.user.changeavatar.predefined.guide"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="16%" height="160" align="center" valign="middle">
      <img name="AvatarPreview"
<% if (memberBean.getMemberAvatar().length() > 0) { %>
      src="<%=memberBean.getMemberAvatar_processed(request, response)%>"
<% } else { %>
      src="<%=contextPath%>/mvnplugin/mvnforum/images/user/no_picture.gif"
<% } %>
      border="0" alt="<fmt:message key="mvnforum.user.changeavatar.avatar_preview"/>" title="<fmt:message key="mvnforum.user.changeavatar.avatar_preview"/>"/>
    </td>
    <td height="160">
      <form name="form" method="post" action="<%=urlResolver.encodeURL(request, response, "updateavatar", URLResolverService.ACTION_URL)%>">
      <%=urlResolver.generateFormAction(request, response, "updateavatar")%>
      <mvn:securitytoken />
      <select name="MemberAvatar" size="10" onchange="MM_swapImage('AvatarPreview', '', this.options[this.selectedIndex].id, 1);">
        <option value="" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/no_picture.gif" selected="selected"><fmt:message key="mvnforum.user.changeavatar.no_picture"/></option>
        <option value="/mvnplugin/mvnforum/images/user/boy1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/boy1.gif">Boy 1</option>
        <option value="/mvnplugin/mvnforum/images/user/christmas1.jpg" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/christmas1.jpg">Christmas 1</option>
        <option value="/mvnplugin/mvnforum/images/user/dog2.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/dog2.gif">Dog 2</option>
        <option value="/mvnplugin/mvnforum/images/user/drummer.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/drummer.gif">Drummer</option>
        <option value="/mvnplugin/mvnforum/images/user/humor.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/humor.gif">Humor Face</option>
        <option value="/mvnplugin/mvnforum/images/user/quake_tux.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/quake_tux.gif">Quake Tux</option>
        <option value="/mvnplugin/mvnforum/images/user/cool_tux.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/cool_tux.gif">Cool Tux</option>
        <option value="/mvnplugin/mvnforum/images/user/man1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/man1.gif">Man 1</option>
        <option value="/mvnplugin/mvnforum/images/user/mickey1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/mickey1.gif">Mickey 1</option>
        <option value="/mvnplugin/mvnforum/images/user/mickey2.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/mickey2.gif">Mickey 2</option>
        <option value="/mvnplugin/mvnforum/images/user/tigerwalk.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/tigerwalk.gif">Tiger walk</option>
        <option value="/mvnplugin/mvnforum/images/user/bearwalk.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/bearwalk.gif">Bear walk</option>
        <option value="/mvnplugin/mvnforum/images/user/elephant_dancing.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/elephant_dancing.gif">Elephant dancing</option>
        <option value="/mvnplugin/mvnforum/images/user/elephant1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/elephant1.gif">Elephant 1</option>
        <option value="/mvnplugin/mvnforum/images/user/bird1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/bird1.gif">Bird 1</option>
        <option value="/mvnplugin/mvnforum/images/user/dog1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/dog1.gif">Dog 1</option>
        <option value="/mvnplugin/mvnforum/images/user/fish1.gif" id="<%=contextPath%>/mvnplugin/mvnforum/images/user/fish1.gif">Fish 1</option>
      </select>
      <br/>
      <input type="button" name="button" value="<fmt:message key="mvnforum.user.changeavatar.button.change_avatar"/>" onclick="javascript:handleChangeAvatar();" class="portlet-form-button"/>
      </form>
    </td>
  </tr>
</mvn:cssrows>
</table>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.user.changeavatar.upload.guide"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td>
      <form method="post" action="<%=urlResolver.encodeURL(request, response, "uploadavatar", URLResolverService.UPLOAD_URL)%>" enctype="multipart/form-data" name="submitform">
      <%=urlResolver.generateFormAction(request, response, "uploadavatar")%>
      <mvn:securitytoken />
      <input type="file" value="<fmt:message key="mvnforum.user.changeavatar.button.browse"/>" name="PictureFile" accept="image/jpeg,image/gif" size="60"/>
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.user.changeavatar.button.upload"/>" onclick="javascript:SubmitForm();" class="portlet-form-button"/>
      </form>
    </td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
