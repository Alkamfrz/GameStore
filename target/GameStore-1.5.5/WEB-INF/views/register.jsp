<%-- Document : register Created on : Nov 23, 2023, 10:13:52 PM Author : alkam
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@ taglib
prefix="c" uri="jakarta.tags.core" %> <%@ taglib prefix="fn"
uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Register</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="/GameStore/assets/css/register.css" />
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100">
    <div class="container mx-auto py-8">
      <h1 class="text-2xl font-bold mb-6 text-center">Registration Form</h1>
      <form
        class="w-full max-w-lg mx-auto bg-white p-8 rounded-md shadow-md"
        action="/GameStore/register"
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
        <div
          id="error"
          class="hidden bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
          role="alert"
        >
          <span id="alert-message"></span>
        </div>
        <fieldset class="flex mb-4">
          <div class="mr-2">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="firstName"
              >First Name</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="text"
              id="firstName"
              name="firstName"
              placeholder="John"
              required
              autocomplete="given-name"
              aria-label="First Name"
            />
          </div>
          <div>
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="lastName"
              >Last Name</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="text"
              id="lastName"
              name="lastName"
              placeholder="Doe"
              required
              autocomplete="family-name"
              aria-label="Last Name"
            />
          </div>
        </fieldset>
        <fieldset class="flex mb-4">
          <div class="mr-2">
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="username"
              >Username</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="text"
              id="username"
              name="username"
              placeholder="johndoe"
              required
              autocomplete="username"
              aria-label="Username"
            />
          </div>
          <div>
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="email"
              >Email</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="email"
              id="email"
              name="email"
              placeholder="john@example.com"
              required
              autocomplete="email"
              aria-label="Email"
            />
          </div>
        </fieldset>
        <fieldset class="flex mb-4">
          <div class="mr-2">
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
              autocomplete="new-password"
              aria-label="Password"
              pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
              title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
            />
          </div>
          <div>
            <label
              class="block text-gray-700 text-sm font-bold mb-2"
              for="confirm-password"
              >Confirm Password</label
            >
            <input
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:border-indigo-500"
              type="password"
              id="confirm-password"
              name="confirm-password"
              placeholder="********"
              required
              autocomplete="new-password"
              aria-label="Confirm Password"
            />
          </div>
        </fieldset>
        <button
          class="w-full bg-indigo-500 text-white text-sm font-bold py-2 px-4 rounded-md hover:bg-indigo-600 transition duration-300"
          type="submit"
          aria-label="Register"
        >
          Register
        </button>
      </form>
      <p class="text-center mt-4">
        Already have an account?
        <a href="/GameStore/login" class="text-blue-500">Login</a>
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
    <script src="/GameStore/assets/js/register.js"></script>
  </body>
</html>
