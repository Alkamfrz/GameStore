<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Login" />
    <jsp:param name="description" value="Login to your account" />
    <jsp:param name="css" value="/GameStore/assets/css/login.css" />
  </jsp:include>
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
          <c:remove var="errorMessage" scope="session" />
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
          <c:remove var="successMessage" scope="session" />
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
              autofocus
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
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/login.js" />
    </jsp:include>
  </body>
</html>
