<%--
 - $Header: /cvsroot/mvnforum/mvnad/srcweb/mvnplugin/mvnad/admin/uploadmedia.jsp,v 1.4 2009/02/20 07:00:12 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.4 $
 - $Date: 2009/02/20 07:00:12 $
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

<script type="text/javascript">
//<![CDATA[  
var attNum = 0;
function callAdd(currentAttNum) {
  document.getElementById("SpanAtt" + currentAttNum).innerHTML='';
  resetUploadButton();
}
function addAtt(){  
  attNum = (attNum + 1);
  st = " <span id='SpanAtt" + attNum + "'><input type='file' name='AttachFilename" + attNum + "' id ='AttachFilename" + attNum + "' size='50' onchange=\"validateFile(this)\"/>&nbsp;<a href='#' onclick='return callAdd(" + attNum + ");'><fmt:message key="mvnad.common.action.remove"/></a><span id=\"msg_AttachFilename" + attNum + "\" class=\"portlet-msg-error\"></span></span><div id=aDiv" + attNum + "></div>";
  document.getElementById('aDiv' + (attNum -1)).innerHTML = st;
  idoffile = "AttachFilename" + (attNum - 1) ;
  document.getElementById(idoffile).focus();
}
function ValidateUploadForm() {
  var badfilecount = 0;
  var totalfilecount = 0;
  for (var i = 0; i < attNum + 1; i++) {
    if ( document.getElementById("SpanAtt" + i).innerHTML != '' &&
         document.getElementById("AttachFilename" + i).value != '' ) {
      totalfilecount++;
      if (verify(document.getElementById("AttachFilename" + i).value) == false) {
        badfilecount++;
      }
    }   
  }
  if (totalfilecount == 0) {
    alert('<fmt:message key="mvnad.admin.uploadmedia.js.upload_media"/>');
    return false;
  }
  if (badfilecount != 0) {
    alert('<fmt:message key="mvnad.admin.uploadmedia.js.not_valid_media_files"/>');
    return false;
  }
  <mvn:servlet>
    document.getElementById("upload").disabled = true;
  </mvn:servlet>
  return true;
}
function SubmitUploadForm() {
  if (ValidateUploadForm() == true) {
    document.uploadmedia.submit();
  }
}
function validateFile(field) {
  var notwhitespace = field.value.search(/\S+/g);
  if (notwhitespace >= 0) {
    if (verify(field.value)) {
      clearDisplayErrors(field);
      resetUploadButton();
    } else {
      invalidFileError(field);
    }
  } else {
    if (document.getElementById("msg_" + field.name).innerHTML != "") {
      clearDisplayErrors(field);
    }
  }
}
function verify(str) {
  if (str.search(/\S+/g) >= 0) {
    var ext = str.toLowerCase().match(/\.[^.]+$/); // Match file extension.
    if ((ext) && (ext.length > 0)) {
      ext = ext.toString().slice(1); // Strip the "." character.
      var extensions = ["swf", "jpeg", "jpg", "png", "gif", "mov", "mpg", "wmv", "avi"];
      for (var i = 0; i < extensions.length; i++) {
        if (ext == extensions[i]) {
          return true;
        }
      }
    }
  }
  return false;
}
function invalidFileError(field) {  
  field.style.color = "#FF0000";
  document.getElementById("msg_" + field.name).innerHTML = "&nbsp;* <fmt:message key="mvnad.admin.uploadmedia.js.not_media_file"/>";
  document.getElementById("upload").disabled = true;
}
function clearDisplayErrors(field) {
  if (field) {
    document.getElementById("msg_" + field.name).innerHTML = "";
    field.style.color = "#000000";
  } else {
    for (var i = 0; i < attNum + 1; i++) {     
      if ( document.getElementById("SpanAtt" + i).innerHTML != '' &&
           document.getElementById("msg_AttachFilename" + i).innerHTML != "") {
        document.getElementById("msg_AttachFilename" + i).innerHTML = "";
        document.getElementById("AttachFilename" + i).style.color = "#000000";
      }
    }
  }
}
function resetForm() {
  var f = document.getElementById("uploadmedia");
  f.reset();
  clearDisplayErrors();
  document.getElementById("upload").disabled = false;
}
function onloadUploadForm() {
  if ( document.getElementById("AttachFilename0").value != '' &&
       verify(document.getElementById("AttachFilename0").value) == false) {
    invalidFileError(document.getElementById("AttachFilename0"));
    document.uploadmedia.upload.disabled=true;
  }
}
function resetUploadButton() {
  var haveErrors = false;
  for (var i = 0; i < attNum + 1; i++) {
    if ( document.getElementById("SpanAtt" + i).innerHTML != '' &&
         document.getElementById("AttachFilename" + i).value != '' && 
         verify(document.getElementById("AttachFilename" + i).value) == false ) {
        haveErrors = true;      
    }   
  }
  if (haveErrors == false) {
    document.getElementById("upload").disabled = false;
  }
}
//]]>
</script>

<form action="<%=urlResolver.encodeURL(request, response, "uploadmediaprocess", URLResolverService.ACTION_URL)%>" method="post" id="uploadmedia" name="uploadmedia" enctype="multipart/form-data">
<%=urlResolver.generateFormAction(request, response, "uploadmediaprocess")%>
<mvn:securitytoken />
<input type="hidden" name="frompage" value="<%=frompage%>" />
<table class="tborder" align="center" cellpadding="3" cellspacing="0" width="95%">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnad.common.ad.upload_media"/></td>
  </tr>
  <tr class="portlet-section-body">
    <td valign="top"><fmt:message key="mvnad.common.ad.media"/></td>
    <td>
      <div id="file-browsers">
        <span id="SpanAtt0">
          <input id="AttachFilename0" type="file" name="AttachFilename0" size="50" onchange="validateFile(this)" />
          <span id="msg_AttachFilename0" class="portlet-msg-error"></span>
        </span>
        <div id="aDiv0"></div>
        <a href="#" onclick="addAtt()"><fmt:message key="mvnad.common.action.attach_more"/></a>
      </div>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input id="upload" name="upload" type="button" value="<fmt:message key="mvnad.common.action.upload"/>" class="portlet-form-button" onclick="SubmitUploadForm()" />
      <input type="reset" value="<fmt:message key="mvnad.common.action.reset"/>" class="liteoption" onclick="resetForm()" />
    </td>
  </tr>
</table>
</form>
<br />

<script type="text/javascript">
//<![CDATA[ 
onloadUploadForm();
//]]>
</script>