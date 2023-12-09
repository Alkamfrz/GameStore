<%-- Document : profile Created on : Dec 8, 2023, 9:48:51 PM Author : alkam --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="jakarta.tags.core" %> <%@ taglib prefix="fn" uri="jakarta.tags.functions"
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Profile</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
      integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:ital,wght@0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="/GameStore/assets/css/profile.css" />
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body>
    <main class="bg-gray-200 min-h-screen pb-24">
      <header
        class="bg-white text-white shadow w-full p-2 flex items-center justify-between"
      >
        <div class="flex items-center">
          <div class="hidden md:flex items-center">
            <img
              src="https://www.emprenderconactitud.com/img/POC%20WCS%20(1).png"
              alt="Logo"
              class="w-28 h-18 mr-2"
            />
            <h2 class="font-bold text-xl text-gray-700">GameStore</h2>
          </div>
          <div class="md:hidden flex items-center">
            <button id="menuBtn">
              <i class="fas fa-bars text-gray-500 text-lg"></i>
            </button>
          </div>
        </div>

        <div class="space-x-5 select-none">
          <button class="p-2 rounded hover:bg-gray-200">
            <i class="fas fa-bell text-gray-500 text-lg"></i>
          </button>
          <div class="dropdown inline-block relative">
            <button
              class="text-gray-500 text-lg inline-flex items-center p-2 rounded hover:bg-gray-200"
            >
              <i class="fas fa-user"></i>
            </button>
            <div
              class="dropdown-menu absolute right-0 hidden text-gray-700 pt-1 bg-white w-32 shadow-lg rounded"
            >
              <p
                class="rounded-t py-2 px-4 block whitespace-no-wrap border-b border-gray-200"
              >
                Hello, ${username}
              </p>
              <a
                class="py-2 px-4 block whitespace-no-wrap hover:bg-gray-100"
                href="/GameStore/profile"
                >Profile</a
              >
              <a
                class="rounded-b py-2 px-4 block whitespace-no-wrap hover:bg-gray-100"
                href="/GameStore/logout"
                >Logout</a
              >
            </div>
          </div>
        </div>
      </header>
      <section class="container max-w-5xl mt-8 px-6 mx-auto">
        <h1
          class="text-2xl font-bold text-gray-700 px-6 md:px-0 flex items-center"
        >
          <c:choose>
            <c:when test="${role eq 'admin'}">
              <button id="adminButton" class="p-2 rounded hover:bg-gray-200">
                <i class="fas fa-arrow-left text-gray-500 text-lg"></i>
              </button>
            </c:when>
            <c:otherwise>
              <button id="storeButton" class="p-2 rounded hover:bg-gray-200">
                <i class="fas fa-arrow-left text-gray-500 text-lg"></i>
              </button>
            </c:otherwise>
          </c:choose>
          Account Settings
        </h1>
        <nav
          class="flex border-b border-gray-300 text-sm font-medium text-gray-600 mt-3 px-6 md:px-0"
          role="navigation"
          aria-label="Profile Navigation"
        >
          <ul class="list-none flex">
            <li class="mr-8 text-gray-900 border-b-2 border-gray-800">
              <a
                href="#profile-info"
                class="py-4 inline-block"
                aria-current="page"
                >Profile Info</a
              >
            </li>
            <li class="mr-8 hover:text-gray-900">
              <a href="#security" class="py-4 inline-block">Security</a>
            </li>
            <li class="mr-8 hover:text-gray-900">
              <a href="#billing" class="py-4 inline-block">Billing</a>
            </li>
          </ul>
        </nav>
        <form
          action="/GameStore/profile"
          method="POST"
          enctype="multipart/form-data"
        >
          <article
            id="profile-info"
            class="w-full bg-white rounded-lg mx-auto mt-8 flex overflow-hidden rounded-b-none"
          >
            <aside class="w-1/3 bg-gray-100 p-8 hidden md:inline-block">
              <h2 class="font-medium text-md text-gray-700 mb-4 tracking-wide">
                Profile Info
              </h2>
              <p class="text-xs text-gray-500">
                Update your basic profile information such as Email Address,
                Name, and Image.
              </p>
              <p class="text-xs text-gray-500">
                Registered at: <c:out value="${createdAt}" />
              </p>
            </aside>
            <div class="md:w-2/3 w-full flex flex-wrap">
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="firstName" class="text-sm text-gray-600"
                  >First Name</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="text"
                  value="${firstName}"
                  name="firstName"
                />
              </div>
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="lastName" class="text-sm text-gray-600"
                  >Last Name</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="text"
                  value="${lastName}"
                  name="lastName"
                />
              </div>
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="email" class="text-sm text-gray-600"
                  >Email Address</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="email"
                  name="email"
                  value="${email}"
                  disabled
                />
              </div>
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="username" class="text-sm text-gray-600"
                  >Username</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="text"
                  name="username"
                  value="${username}"
                  disabled
                />
              </div>
              <div class="py-4 px-8 w-full clearfix overflow-hidden">
                <label for="photo" class="text-sm text-gray-600 w-full block"
                  >Portrait</label
                >
                <img class="rounded-full w-16 h-16 border-4 mt-2 border-gray-200
                float-left" id="photo" src="${empty user.profilePhoto ?
                '/GameStore/assets/img/users/default.png' :
                '/GameStore/assets/img/users/'.concat(user.id.toString().replace("-",
                "").substring(0, 10)).concat('/').concat(user.profilePhoto)}"
                alt="photo" />
                <div
                  class="bg-gray-200 text-gray-500 text-xs mt-5 ml-3 font-bold px-4 py-2 rounded-lg float-left hover:bg-gray-300 hover:text-gray-600 relative overflow-hidden cursor-pointer"
                >
                  <input
                    type="file"
                    name="photo"
                    onchange="loadFile(event)"
                    class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                  />
                  Change Photo
                </div>
              </div>
            </div>
          </article>
          <footer
            class="flex items-center justify-between p-16 py-8 bg-gray-300 rounded-b-lg border-t border-gray-200"
          >
            <p class="text-xs text-gray-500 mt-2">
              Click on Save to update your Profile Info
            </p>
            <button
              type="submit"
              class="bg-indigo-500 text-white text-sm font-medium px-6 py-2 rounded uppercase cursor-pointer"
            >
              Save
            </button>
          </footer>
        </form>
        <form action="/changepassword" method="post">
          <article
            id="security"
            class="w-full bg-white rounded-lg mx-auto mt-8 flex overflow-hidden rounded-b-none"
          >
            <aside class="w-1/3 bg-gray-100 p-8 hidden md:inline-block">
              <h2 class="font-medium text-md text-gray-700 mb-4 tracking-wide">
                Security
              </h2>
              <p class="text-xs text-gray-500">Update your password.</p>
            </aside>
            <div class="md:w-2/3 w-full flex flex-wrap">
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="currentPassword" class="text-sm text-gray-600"
                  >Current Password</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="password"
                  name="currentPassword"
                />
              </div>
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="newPassword" class="text-sm text-gray-600"
                  >New Password</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="password"
                  name="newPassword"
                />
              </div>
              <div class="py-4 px-8 w-full md:w-1/2">
                <label for="confirmPassword" class="text-sm text-gray-600"
                  >Confirm New Password</label
                >
                <input
                  class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
                  type="password"
                  name="confirmPassword"
                />
              </div>
            </div>
          </article>
          <footer
            class="flex items-center justify-between p-16 py-8 bg-gray-300 rounded-b-lg border-t border-gray-200"
          >
            <p class="text-xs text-gray-500 mt-2">
              Click on Save to update your Security Info
            </p>
            <button
              type="submit"
              class="bg-indigo-500 text-white text-sm font-medium px-6 py-2 rounded uppercase cursor-pointer"
            >
              Save
            </button>
          </footer>
        </form>
      </section>
    </main>
    <script
      src="https://code.jquery.com/jquery-3.7.1.min.js"
      integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
      crossorigin="anonymous"
    ></script>
    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js"
      integrity="sha512-YUkaLm+KJ5lQXDBdqBqk7EVhJAdxRnVdT2vtCzwPHSweCzyMgYV/tgGF4/dCyqtCC2eCphz0lRQgatGVdfR0ww=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    ></script>
    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/animejs/3.2.2/anime.min.js"
      integrity="sha512-aNMyYYxdIxIaot0Y1/PLuEu3eipGCmsEUBrUq+7aVyPGMFH8z0eTP0tkqAvv34fzN6z+201d3T8HPb1svWSKHQ=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    ></script>
    <script src="/GameStore/assets/js/profile.js"></script>
  </body>
</html>
