<%-- 
    Document   : register
    Created on : Nov 23, 2023, 10:13:52 PM
    Author     : alkam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
    </head>
    <body>
        <h1>Register Page</h1>
        <form action="register" method="post">
            <table>
                <tr>
                    <td>First Name:</td>
                    <td><input type="text" name="firstName" required/></td>
                </tr>
                <tr>
                    <td>Last Name:</td>
                    <td><input type="text" name="lastName" required/></td>
                </tr>
                <tr>
                    <td>Email:</td>
                    <td><input type="email" name="email" required/></td>
                </tr>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" required/></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" required/></td>
                </tr>
                <tr>
                    <td>Confirm Password:</td>
                    <td><input type="password" name="confirmPassword" required/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Register"/></td>
                </tr>
            </table>
        </form>
        <p>Already have an account? <a href="login">Login</a></p>
    </body>
</html>
