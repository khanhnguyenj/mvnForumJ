<%--
 - $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/srcweb/loginbyvbulletin.jsp,v 1.3 2009/12/04 07:04:30 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.3 $
 - $Date: 2009/12/04 07:04:30 $
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
<%@ page errorPage="mvnplugin/mvnforum/user/fatalerror.jsp"%>

<%@ page import="net.myvietnam.mvncore.util.*" %>
<%@ page import="net.myvietnam.mvncore.filter.DisableHtmlTagFilter" %>

<%@ page import="net.myvietnam.mvncore.service.EnvironmentService" %>
<%@ page import="net.myvietnam.mvncore.service.MvnCoreServiceFactory" %>
<%@ page import="net.myvietnam.mvncore.service.URLResolverService" %>

<%@ page import="com.mvnforum.auth.OnlineUser" %>
<%@ page import="com.mvnforum.auth.OnlineUserManager" %>
<%@ page import="com.mvnforum.auth.MVNForumPermission" %>
<%@ page import="com.mvnforum.db.DAOFactory" %>
<%@ page import="com.mvnforum.db.MemberDAO" %>
<%@ page import="com.mvnforum.MVNForumConfig" %>
<%@ page import="com.mvnforum.MyUtil" %>
<%@ page import="com.mvnforum.service.MvnForumInfoService" %>
<%@ page import="com.mvnforum.service.MvnForumServiceFactory" %>
<%@ page import="com.mvnforum.service.MvnForumAdService" %>
<%@ page import="com.mvnforum.common.ThreadIconUtil" %>

<%@ page import="java.sql.*" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>
<%@ page import="net.myvietnam.mvncore.db.DBUtils" %>
<%@ page import="net.myvietnam.mvncore.db.DBUtils2" %>
<%@ page import="net.myvietnam.mvncore.exception.DatabaseException" %>
<%@ page import="net.myvietnam.mvncore.exception.ObjectNotFoundException" %>
<%@ page import="net.myvietnam.mvncore.misc.Base64" %>
<%@ page import="net.myvietnam.mvncore.security.Encoder" %>

<%@ taglib uri="mvntaglib" prefix="mvn" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%
// I have to comment these below line because when back a page,
// the page is reload, so user have to type data again
//response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
//response.setHeader("Pragma", "no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

String contextPath = request.getContextPath();

URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
EnvironmentService environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();

OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
MVNForumPermission permission = onlineUser.getPermission();
String memberName = onlineUser.getMemberName();
int memberID = onlineUser.getMemberID();

String currentLocale = onlineUser.getLocaleName();
if (currentLocale.equals("")) currentLocale = MVNForumConfig.getDefaultLocaleName();
pageContext.setAttribute("currentLocale", currentLocale);
boolean isServlet = (environmentService.isPortlet() == false || request.getAttribute("javax.portlet.request") == null);
boolean isPortlet = !isServlet;
boolean externalUserDatabase = MVNForumConfig.getEnableExternalUserDatabase();
boolean internalUserDatabase = !externalUserDatabase;
MemberDAO memberDAO = DAOFactory.getMemberDAO();

String imagePath = contextPath + ThreadIconUtil.getImagePath(onlineUser);
%>

<%@page import="com.mvnforum.user.UserModuleConfig"%><fmt:setLocale value="${currentLocale}" />
<%
response.setContentType("text/html; charset=utf-8");
%>

<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
  <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - Migrate password from vBulletin to mvnForum</mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body onunload="document.submitform.submitbutton.disabled=false;">
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/md5.js"></script>
<script type="text/javascript" src="<%=contextPath%>/mvnplugin/mvnforum/js/myvietnam.js"></script>
<script type="text/javascript">
//<![CDATA[
function isBlank(field, strBodyHeader) {
    strTrimmed = trim(field.value);
    if (strTrimmed.length > 0) return false;
    alert("\"" + strBodyHeader + "\" <fmt:message key="mvnforum.common.js.prompt.fieldrequired"/>");
    field.focus();
    return true;
}
function checkGoodName(str, desc) {
    s = trim(str);
    len = s.length; 
    for (i = 0; i < len; i++) {
        b = s.charCodeAt(i);
        if ((b >= toInt('a')) && (b <= toInt('z'))) { // not a cast, it's a function.
            // low chars
        } else if ((b >= toInt('A')) && (b <= toInt('Z'))) {
            // up chars
        } else if ((b >= toInt('0')) && (b <= toInt('9'))) {
            // numbers 
        } else if (( (b==toInt('_')) || (b==toInt('.')) || (b==toInt('@')) ) && (i != 0)) {
            // very litte special chars
        } else {
            alert(desc + " \"" + s +  "\" " + "<fmt:message key="mvnforum.common.js.prompt.not_goodname"/>. <fmt:message key="mvnforum.common.js.prompt.invalid_char_is"/> \"" + s.charAt(i) + "\""); 
            return false;
            // not good name error
        }
    }
    return true;
}
function toInt(c) {
  return c.charCodeAt(0);
}
function checkEnter(event) {
  var agt=navigator.userAgent.toLowerCase();
  
  // Maybe, Opera make an onClick event when user press enter key 
  // on the text field of the form
  if (agt.indexOf('opera') >= 0) return;

  // enter key is pressed
  if (getKeyCode(event) == 13)
    SubmitForm();
}

function SubmitForm() {
  if (ValidateForm()) {
    var enableEncrypted = <%=MVNForumConfig.getEnableEncryptPasswordOnBrowser()%>;
    if (enableEncrypted) {
      pw2md5(document.submitform.MemberMatkhau, document.submitform.md5pw);
    }
    document.submitform.submitbutton.disabled=true;
    return document.submitform.submit();
  }
  return false;
}

function ValidateForm() {
  if (isBlank(document.submitform.MemberName, "<fmt:message key="mvnforum.common.member.login_name"/>")) return false;
  if (isBlank(document.submitform.MemberMatkhau, "<fmt:message key="mvnforum.common.member.password"/>")) return false;
  //Check Password's length
  if (document.submitform.MemberMatkhau.value.length < 3) {
    alert("<fmt:message key="mvnforum.common.js.prompt.invalidlongpassword"/>");
    document.submitform.MemberMatkhau.focus();
    return false;
  }
  return true;
}
//]]>
</script>
<%
final String VBULLETIN_TABLE_NAME = "user"; 
class VBulletinUserBean {
    
    private String userName;
    private String password;
    private String salt;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
class VBulletinUserDAOImplJDBC {
     
    VBulletinUserBean getUser(String userName) throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT UserName, Password, Salt");
        sql.append(" FROM " + VBULLETIN_TABLE_NAME);
        sql.append(" WHERE UserName = ?");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, userName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find user in table 'vBulletinUser' where user name = " + userName);
            }
            VBulletinUserBean bean = new VBulletinUserBean();
            bean.setUserName(resultSet.getString("UserName"));
            bean.setPassword(resultSet.getString("Password"));
            bean.setSalt(resultSet.getString("Salt"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in VBulletinUserDAOImplJDBC.getUser(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }
    
    void updatePassword(String userName, String password) throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + VBULLETIN_TABLE_NAME + " SET Password = ?");
        sql.append(" WHERE UserName = ?");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            // // column(s) to update
            statement.setString(1, password);

            // primary key column(s)
            statement.setString(2, userName);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot find user in table 'vBulletinUser' where user name = " + userName);
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in VBulletinUserDAOImplJDBC.updatePassword.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }
}
String errorMessage = "";
String successMessage = "";
String loginMemberName = ParamUtil.getParameter(request, "MemberName");
if (loginMemberName != null && loginMemberName.length() > 0) {
    try {
        String memberPasswordMD5 = ParamUtil.getParameter(request, "md5pw", false);
        String memberPassword = "";
        if (memberPasswordMD5.length() == 0 || (memberPasswordMD5.endsWith("==") == false)) {
            // md5 is not valid, try to use unencoded password method
            memberPassword  = ParamUtil.getParameterPassword(request, "MemberMatkhau", 3, 0);
            AssertionUtil.doAssert(memberPassword.length() != 0, "Cannot allow memberPassword's length is 0. Serious Assertion Failed.");
        }
    
        VBulletinUserDAOImplJDBC vBulletinUserDAOImplJDBC = new VBulletinUserDAOImplJDBC();
        VBulletinUserBean vBulletinUserBean = vBulletinUserDAOImplJDBC.getUser(loginMemberName);
        String vbulletinSalt = vBulletinUserBean.getSalt();
        String vbulletinEncodedPasswordFromDB = vBulletinUserBean.getPassword();
        if (vbulletinEncodedPasswordFromDB.length() == 0) {
            errorMessage = "User name \"" + loginMemberName + "\" was migrated from vBulletin to mvnForum.";
        } else {
            String vbulletinEncodedPassword = Encoder.getMD5_Hex(new String(Hex.encodeHex(Base64.decode(memberPasswordMD5.getBytes()))) + vbulletinSalt);
            if ( (memberPasswordMD5.length() == 0) || (memberPasswordMD5.endsWith("==") == false) ) {
                vbulletinEncodedPassword = Encoder.getMD5_Hex(Encoder.getMD5_Hex(memberPassword) + vbulletinSalt);
            }
           
            if (vbulletinEncodedPasswordFromDB.equals(vbulletinEncodedPassword)) {
                String newMvnforumPassword = memberPasswordMD5;
                if ( (memberPasswordMD5.length() == 0) || (memberPasswordMD5.endsWith("==") == false) ) {
                    newMvnforumPassword = Encoder.getMD5_Base64(memberPassword);
                }
                Timestamp passwordExpireDate = null;
                if (MVNForumConfig.getMaxPasswordDays() > 0) {
                    passwordExpireDate = DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
                }
                int loginMemberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(loginMemberName);
                DAOFactory.getMemberDAO().updatePassword(loginMemberID, newMvnforumPassword, passwordExpireDate);
                vBulletinUserDAOImplJDBC.updatePassword(loginMemberName, "");
                successMessage = "User name \"" + loginMemberName + "\" has been migrated from vBulletin to mvnForum successfully.<br />" +
                                "Now you can log in to mvnForum by click <a href='" + contextPath + UserModuleConfig.getUrlPattern() + "/login'  class='command'>here</a>";
            } else {
                errorMessage = "You have type wrong password";
            }
        }
    } catch (ObjectNotFoundException e) {
        errorMessage = "User name \"" + loginMemberName + "\" doesnot exist in vBulletin database.";
    }
}
%>
<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="<%=contextPath + UserModuleConfig.getUrlPattern()%>/index"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  Migrate password from vBulletin to mvnForum
</div>
<br />
<% if (successMessage.length() > 0) { %>
<div class="pagedesc">
  <%=successMessage%><br/>
</div>
<br />
<% } %>
<% if (errorMessage.length() > 0) { %>
<div class="pagedesc">
  <fmt:message key="mvnforum.user.login.message"/>: <span class="warning"><%=errorMessage%></span>
  <br/>
</div>
<br />
<% } %>

<form action="loginbyvbulletin.jsp" method="post" name="submitform" onsubmit="return false;">
<table class="tborder" width="95%" align="center" cellspacing="0" cellpadding="0">
<mvn:securitytoken />
<input type="hidden" name="md5pw" value="" />
<mvn:cssrows>
  <tr class="portlet-section-header">
    <td colspan="2">Please enter your vBulletin information</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td width="30%"><label for="Name"><fmt:message key="mvnforum.common.member.login_name"/><span class="requiredfield"> *</span></label></td>
    <td><input type="text" id="Name" name="MemberName" value="<%=ParamUtil.getAttribute(request, "MemberName")%>" /></td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td><label for="Matkhau"><fmt:message key="mvnforum.common.member.password"/><span class="requiredfield"> *</span></label></td>
    <td><input type="password" id="Matkhau" name="MemberMatkhau" onkeypress="checkEnter(event);" /></td>
  </tr>
  <tr class="portlet-section-footer">
    <td colspan="2" align="center">
      <input type="button" name="submitbutton" value="Migrate" onclick="javascript:SubmitForm();" class="portlet-form-button" />
    </td>
  </tr>
</mvn:cssrows>
</table>
</form>

</mvn:body>
</mvn:html>
</fmt:bundle>