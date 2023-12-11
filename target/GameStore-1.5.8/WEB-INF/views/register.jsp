<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Register" />
    <jsp:param name="description" value="Register for an account" />
    <jsp:param name="css" value="/GameStore/assets/css/register.css" />
  </jsp:include>
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
          <c:remove var="errorMessage" scope="session" />
        </c:if>
        <div id="passwordError" style="display: none">
          <div
            class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
            role="alert"
          >
            <ul></ul>
          </div>
        </div>
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
              id="confirmPassword"
              name="confirmPassword"
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
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/register.js" />
    </jsp:include>
  </body>
</html>
