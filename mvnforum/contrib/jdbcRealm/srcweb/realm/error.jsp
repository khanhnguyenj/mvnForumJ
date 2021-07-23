<html>
<head>
  <title>Demo Single Sign On, JDBC Realm - Login Error Message</title>
</head>
<body>
<form method="POST" action="<%= response.encodeURL("j_security_check") %>">
<table width="100%" height="100%">
  <tr>
    <td>
      <table width="50%" class="tborder" cellspacing="1" cellpadding="3" align="center">
        <tr>
          <td align="left" class="portlet-section-header">Demo Single Sign On, JDBC Realm - Login Error Message</td>
        </tr>
        <tr>
          <td class="portlet-section-body">
            You entered invalid username and/or password, please try
            <a class="command" href="<%= response.encodeURL("login.jsp") %>">again</a>?
            <br/>
        </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</form>
</body>
</html>
