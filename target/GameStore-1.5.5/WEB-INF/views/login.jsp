<%-- Document : login Created on : Nov 23, 2023, 10:04:45 PM Author : alkam --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="jakarta.tags.core" %> <%@ taglib prefix="fn" uri="jakarta.tags.functions"
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Login</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="/GameStore/assets/css/profile.css" />
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="container mx-auto py-8">
      <h1 class="text-2xl font-bold mb-6 text-center">Login Form</h1>
      <form
        id="login-form"
        class="w-full max-w-md mx-auto bg-white p-8 rounded-md shadow-md"
        action="/GameStore/login"
        method="post"
      >
        <c:if test="${not empty errorMessage}">
          <div id="error-messages">
            <div
              class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
              role="alert"
            >
              <ul>
                <c:forEach items="${errorMessage}" var="errorMessage">
                  <li>${errorMessage}</li>
                </c:forEach>
              </ul>
            </div>
          </div>
          <% session.removeAttribute("errorMessage"); %>
        </c:if>
        <c:if test="${not empty successMessage}">
          <div id="success-messages">
            <div
              class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4"
              role="alert"
            >
              <span>${successMessage}</span>
            </div>
          </div>
          <% session.removeAttribute("successMessage"); %>
        </c:if>
        <div
          id="error"
          class="hidden bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
          role="alert"
        >
          <span id="alert-message"></span>
        </div>
        <fieldset class="mb-4">
          <div>
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="usernameOrEmail"
              >Username or Email</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="text"
              id="usernameOrEmail"
              name="usernameOrEmail"
              placeholder="johndoe"
              required
              autocomplete="username"
              aria-label="Username"
            />
          </div>
          <div class="mt-4">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="password"
              >Password</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="password"
              id="password"
              name="password"
              placeholder="********"
              required
              autocomplete="current-password"
              aria-label="Password"
            />
          </div>
        </fieldset>
        <button
          class="w-full bg-indigo-500 text-white text-sm font-bold py-2 px-4 rounded-md hover:bg-indigo-600 transition duration-300"
          type="submit"
          aria-label="Login"
        >
          Login
        </button>
      </form>
      <p class="text-center mt-4">
        Don't have an account?
        <a href="/GameStore/register" class="text-blue-500">Register</a>
      </p>
    </div>
    <script
      src="https://code.jquery.com/jquery-3.7.1.min.js"
      integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
      crossorigin="anonymous"
    ></script>
    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/animejs/3.2.2/anime.min.js"
      integrity="sha512-aNMyYYxdIxIaot0Y1/PLuEu3eipGCmsEUBrUq+7aVyPGMFH8z0eTP0tkqAvv34fzN6z+201d3T8HPb1svWSKHQ=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    ></script>
    <script src="/GameStore/assets/js/login.js"></script>
  </body>
</html>