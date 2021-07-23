<html>
<head>
  <title>Demo Single Sign On, JDBC Realm - Welcome Message</title>
</head>
<body>
<table width="100%" height="100%">
  <tr>
    <td>
      <table width="70%" class="tborder" cellspacing="1" cellpadding="3" align="center" valign="center">
        <tr>
          <td class="portlet-section-header" align="center">Demo Single Sign On, Realm - Welcome</>
        </>
        <tr>
          <td class="portlet-section-body" align="center">
          <% String user = request.getRemoteUser();
             if (user != null) { %>
              You are logged in Realm as remote user <b><%= request.getRemoteUser() %></b><br/>
              These is a JDBC realm demo web-based application.
              <br/>
              <a class="command" href="<%= response.encodeURL("login.jsp?logoff=true")%>">Logout</a>
          <% } else { %>
              You are viewing this page with a Realm user is null. 
              If you ensure that you have already logged in successfully, it is very likely
              that you have not config Realm correctly in the web.xml
          <% } %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>
