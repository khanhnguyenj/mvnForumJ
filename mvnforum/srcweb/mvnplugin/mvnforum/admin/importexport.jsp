<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/admin/importexport.jsp,v 1.76 2009/07/16 03:21:12 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.76 $
 - $Date: 2009/07/16 03:21:12 $
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
 - @author: Igor Manic   
 --%>
<%-- not localized yet --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp" %>
<%@ page import="java.util.Vector" %>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - <fmt:message key="mvnforum.admin.importexport.title"/></mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css" />
</mvn:head>
<mvn:body onunload="document.restoreform.submitbutton.disabled=false;document.migrateform.submitbutton.disabled=false;document.backupform.submitbutton.disabled=false;">

<script type="text/javascript">
//<![CDATA[
function Submit_RestoreForm() {
  if (Validate_RestoreForm()) {
    <mvn:servlet>
      document.restoreform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.restoreform.submit();
  }
}
function Submit_MigrateForm() {
  if (Validate_MigrateForm()) {
    <mvn:servlet>
      document.migrateform.submitbutton.disabled=true;
    </mvn:servlet>
    document.migrateform.submit();
  }
}
function Submit_BackupForm() {
  if (Validate_BackupForm()) {
    <mvn:servlet>
      document.backupform.submitbutton.disabled=true;
    </mvn:servlet>  
    document.backupform.submit();
  }
}

function Validate_RestoreForm() {
  //@todo: if value of restoreform.ServerImportFile is empty, then
  //       value of field restoreform.ClientImportFile should be non-empty
  //return true;
  if (isBlank(document.restoreform.ClientImportFile, "<fmt:message key="mvnforum.admin.importexport.import_input_file"/>"))
    return false;
  else
    return true;
}

function Validate_MigrateForm() {
  if (isBlank(document.migrateform.ClientImportFile, "<fmt:message key="mvnforum.admin.importexport.import_input_file"/>")) return false;
  if (isBlank(document.migrateform.JiveAdmin, "<fmt:message key="mvnforum.admin.importexport.admin_name"/>")) return false;
  if (isBlank(document.migrateform.JiveGuest, "<fmt:message key="mvnforum.admin.importexport.guest_name"/>")) return false;
  if (isBlank(document.migrateform.RootCategory, "<fmt:message key="mvnforum.admin.importexport.root_category_name"/>")) return false;
  return true;
}

function Validate_BackupForm() {
  return true;
}
//]]>
</script>

<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <%if (isServlet) {%>
  <a href="<%=urlResolver.encodeURL(request, response, "index", URLResolverService.RENDER_URL, "view")%>"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  <%}%>
  <a href="<%=urlResolver.encodeURL(request, response, "index")%>"><fmt:message key="mvnforum.admin.index.title"/></a>&nbsp;&raquo;&nbsp;
  <a href="<%=urlResolver.encodeURL(request, response, "misctasks")%>"><fmt:message key="mvnforum.admin.misctasks.title"/></a>&nbsp;&raquo;&nbsp;
  <fmt:message key="mvnforum.admin.importexport.title"/>
</div>
<br/>

<div class="pagedesc">
  <fmt:message key="mvnforum.admin.importexport.info1"/> <b><%= mvnForumInfo.getProductDesc()%></b>
  <fmt:message key="mvnforum.admin.importexport.info2"/>
  <%if (permission.canAdminSystem()) {%>
    <a href="#importrestore" class="command"><fmt:message key="mvnforum.admin.importexport.import_restore_data"/></a><br/>
    <a href="#importmigrate" class="command"><fmt:message key="mvnforum.admin.importexport.migrate_from_jive"/></a><br/>
    <a href="#exportbackup" class="command"><fmt:message key="mvnforum.admin.importexport.backup_data"/></a><br/>
  <%}%>
</div>
<br/>

<%-- IMPORTANT: these three forms have a couple of same fields. Those fields MUST
     have same names in all forms, so a WebHandler can handle all of them at once. --%>

<%-- ==================================================================
     ====== RESTORE (import from mvnForum XML, or mvnForum ZIP) =======
     ================================================================== --%>
<%
Vector backupFilesOnServer = (Vector)request.getAttribute("BackupFilesOnServer");
%>
<a name="importrestore"/>
<form method="post" action="<%=urlResolver.encodeURL(request, response, "importprocess", URLResolverService.ACTION_URL)%>" enctype="multipart/form-data" name="restoreform">
<%=urlResolver.generateFormAction(request, response, "importprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.importexport.restore_data"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="left" valign="top">
      <mvn:cssrows>
      <table width="100%" align="center" class="noborder">
        <tr class="<mvn:cssrow/>">
          <td width="50%" align="left"><fmt:message key="mvnforum.admin.importexport.choose_backup_file"/>:</td>
          <td width="50%" align="right"><a href="javascript:location.reload();" class="command"><fmt:message key="mvnforum.admin.importexport.refresh_file_list"/></a></td>
        </tr>
      </table>
      <table width="95%" align="center" class="noborder">
        <%
        int i=0;
        if (backupFilesOnServer!=null) {
          for (i=0; i<backupFilesOnServer.size(); i++) {
            String serverFilename=(String)backupFilesOnServer.elementAt(i);%>
            <tr class="<mvn:cssrow/>">
              <td align="left">
                <input type="radio" name="ServerImportFile" value="<%=serverFilename%>" class="noborder" />
                <b><%=serverFilename%></b>
              </td>
              <td align="center" width="100">
                <a href="<%=urlResolver.encodeURL(request, response, "getexportprocess?filename=" + serverFilename)%>" class="command"><fmt:message key="mvnforum.common.action.download"/></a>
              </td>
              <td align="center" width="100">
                <a href="<%=urlResolver.encodeURL(request, response, "deleteexportprocess?filename=" + serverFilename)%>" class="command"><fmt:message key="mvnforum.common.action.delete"/></a>
              </td>
            </tr>
        <%} //for
        } /*if (backupFilesOnServer!=null)*/%>
        <tr class="<mvn:cssrow/>">
          <td align="left" colspan="3">
            <input type="radio" name="ServerImportFile" value="" checked="checked" class="noborder" />
            <fmt:message key="mvnforum.admin.importexport.other"/>:&nbsp;<input type="file" value="<fmt:message key="mvnforum.admin.importexport.browse"/>" name="ClientImportFile" size="52"/>
          </td>
        </tr>
      </table>
      </mvn:cssrows>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="left">
      <fmt:message key="mvnforum.admin.importexport.backup_file_type"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ImportType" value="<%=MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML%>" checked="checked" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.backup_file_type1"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ImportType" value="<%=MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP%>" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.backup_file_type2"/><br/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="left">
      <fmt:message key="mvnforum.admin.importexport.incase_error"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ClearIfError" value="1" checked="checked" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.incase_error1"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ClearIfError" value="0" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.incase_error2"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="center">
      <select name="MessageLevel" size="1">
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_ALL_MESSAGES%>" selected="selected"><fmt:message key="mvnforum.admin.importexport.output1"/></option>
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_IMPORTANT_MESSAGES%>"><fmt:message key="mvnforum.admin.importexport.output2"/></option>
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_ONLY_ERRORS%>"><fmt:message key="mvnforum.admin.importexport.output3"/></option>
      </select>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.importexport.import_restores"/>" onclick="javascript:Submit_RestoreForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>
<br/>

<%-- ===================================================================
     ============== MIGRATE (import from Jive Forums XML) ==============
     =================================================================== --%>
<a name="importmigrate"/>
<form method="post" action="<%=urlResolver.encodeURL(request, response, "importprocess", URLResolverService.ACTION_URL)%>" enctype="multipart/form-data" name="migrateform">
<%=urlResolver.generateFormAction(request, response, "importprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td colspan="2"><fmt:message key="mvnforum.admin.importexport.migrate"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center" valign="top">
      <fmt:message key="mvnforum.admin.importexport.input_file"/>
      <input type="file" value="Browse" name="ClientImportFile" size="52" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="left">
      <label for="ImportType"><fmt:message key="mvnforum.admin.importexport.import_file_type"/></label><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" id="ImportType" name="ImportType" value="<%=MVNForumConfig.IMPORTEXPORT_TYPE_JIVE_XML%>" checked="checked" class="noborder" />
      Jive XML (xmlversion="1.0", http://www.jivesoftware.com/jive.dtd)
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top">
      <label for="JiveAdmin"><fmt:message key="mvnforum.admin.importexport.enter_jive_admin"/></label>
    </td>
    <td width="35%" align="left" valign="top">
      <input type="text" value="Admin" id="JiveAdmin" name="JiveAdmin" size="32" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top">
      <label for="JiveGuest"><fmt:message key="mvnforum.admin.importexport.enter_mvnforum_virtual"/></label>
    </td>
    <td width="35%" align="left" valign="top">
      <input type="text" value="<%=MVNForumConfig.getDefaultGuestName()%>" id="JiveGuest" name="JiveGuest" size="32" />
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top">
      <label for="ForumPasswords"><fmt:message key="mvnforum.admin.importexport.set_all_forum"/></label>
    </td>
    <td width="35%" align="left" valign="top">
      <input type="text" value="" id="ForumPasswords" name="ForumPasswords" size="32"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top">
      <label for="RootCategory"><fmt:message key="mvnforum.admin.importexport.put_all_jive"/></label>
    </td>
    <td width="35%" align="left" valign="top">
      <input type="text" value="General" id="RootCategory" name="RootCategory" size="32"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="right" valign="top">
      <label for="RootCategoryDesc"><fmt:message key="mvnforum.admin.importexport.root_category"/></label>
    </td>
    <td width="35%" align="left" valign="top">
      <input type="text" value="Forums imported from Jive" id="RootCategoryDesc" name="RootCategoryDesc" size="40"/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="left">
      <fmt:message key="mvnforum.admin.importexport.incase_error"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ClearIfError" value="1" checked="checked" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.incase_error1"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ClearIfError" value="0" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.incase_error2"/>
      <br/>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2" align="center">
      <select name="MessageLevel" size="1">
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_ALL_MESSAGES%>" selected="selected"><fmt:message key="mvnforum.admin.importexport.output1"/></option>
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_IMPORTANT_MESSAGES%>"><fmt:message key="mvnforum.admin.importexport.output2"/></option>
        <option value="<%=MVNForumConfig.MESSAGE_LEVEL_ONLY_ERRORS%>"><fmt:message key="mvnforum.admin.importexport.output3"/></option>
      </select>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.admin.importexport.import_migrate"/>" onclick="javascript:Submit_MigrateForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.common.action.reset"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>
<br/>

<%-- ==================================================================
     ======= BACKUP (export to mvnForum XML, or mvnForum ZIP) =========
     ================================================================== --%>
<a name="exportbackup"/>
<form method="post" action="<%=urlResolver.encodeURL(request, response, "exportprocess", URLResolverService.ACTION_URL)%>" name="backupform">
<%=urlResolver.generateFormAction(request, response, "exportprocess")%>
<mvn:securitytoken />
<mvn:cssrows>
<table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
  <tr class="portlet-section-header">
    <td><fmt:message key="mvnforum.admin.importexport.import_back"/></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td align="left">
      <fmt:message key="mvnforum.admin.importexport.chose_destination"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ExportType" value="<%=MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML%>" checked="checked" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.xml"/><br/>
      &nbsp;&nbsp;&nbsp;<input type="radio" name="ExportType" value="<%=MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP%>" class="noborder" />
      <fmt:message key="mvnforum.admin.importexport.zip"/>
    </td>
  </tr>
  <tr class="portlet-section-footer">
    <td align="center">
      <input type="button" name="submitbutton" value="<fmt:message key="mvnforum.common.action.reset"/>" onclick="javascript:Submit_BackupForm();" class="portlet-form-button" />
      <input type="reset" value="<fmt:message key="mvnforum.admin.importexport.import_backup"/>" class="liteoption" />
    </td>
  </tr>
</table>
</mvn:cssrows>
</form>

<br/>

<%@ include file="footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
