<% // never called because of realm's configurations
String redirectURL = "http://localhost:8080/mvnforum";
if (request.getParameter("logoff") != null) {
    session.invalidate();
    response.sendRedirect("login.jsp");
}
String user = request.getRemoteUser();
if (user != null) {
    if ( (redirectURL.length() > 0) && (request.getParameter("logoff") == null) ) {
        response.sendRedirect(redirectURL);
    }
}
%>
<html>
<head>
  <title>Demo Single Sign On, JDBC Realm - Login</title>
</head>
<body>
<form method="post" action="<%= response.encodeURL("j_security_check") %>">
<table width="100%" height="100%">
  <tr>
    <td>
      <table width="70%" class="tborder" cellspacing="1" cellpadding="3" align="center" valign="center">
        <tr>
          <td class="portlet-section-header" colspan="2" align="left">Demo Single Sign On, Realm - Login</td>
        </tr>
      <% if (user != null) { %>
        <tr class="portlet-section-body">
          <td align="center" colspan="2">
           You are logged in Realm as user "<b><%=user%></b>".<br/>
           You want to log in again with new account ?
          </td>
        </tr>
      <% } %>
        <tr class="portlet-section-alternate">
          <td>Username:</td>
          <td><input type="text" name="j_username"></td>
        </tr>
        <tr class="portlet-section-body">
          <td>Password:</td>
          <td><input type="password" name="j_password"></td>
        </tr>
        <tr class="portlet-section-footer">
          <td colspan="2" align="center">
            <input type="submit" value="Log In" class="mainoption">&nbsp;
            <% if (request.getRemoteUser() != null) { %>
              <a href="login.jsp?logoff=true" class="command">Logout</a>
            <% } %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form>
</body>
</html>
