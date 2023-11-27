<%-- Document : login Created on : Nov 23, 2023, 10:04:45 PM Author : alkam --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Login Page</title>
  </head>
  <body>
    <c:if test="${not empty error}">
      <div class="alert alert-danger">${error}</div>
    </c:if>
    <h1>Login Page</h1>
    <form action="login" method="post">
      <table>
        <tr>
          <td>Username:</td>
          <td><input type="text" name="usernameOrEmail" required /></td>
        </tr>
        <tr>
          <td>Password:</td>
          <td><input type="password" name="password" required /></td>
        </tr>
        <tr>
          <td><input type="submit" value="Login" /></td>
        </tr>
      </table>
    </form>
    <p>Don't have an account? <a href="register">Register</a></p>
  </body>
</html>
