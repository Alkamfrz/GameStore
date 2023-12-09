<%-- Document : dashboard Created on : Dec 1, 2023, 7:52:37 PM Author : alkam
--%> <%@page contentType="text/html" pageEncoding="UTF-8"%> <%@ taglib
prefix="c" uri="jakarta.tags.core" %> <%@ taglib prefix="fn"
uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Dashboard</title>
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
    <link rel="stylesheet" href="/GameStore/assets/css/dashboard.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body>
    <div class="flex flex-col h-screen bg-gray-100">
      <div
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
      </div>

      <div class="flex-1 flex">
        <div
          class="p-2 bg-white w-60 flex flex-col hidden md:flex"
          id="sideNav"
        >
          <nav>
            <a
              class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
              href="dashboard"
            >
              <i class="fas fa-home mr-2"></i>Dashboard
            </a>
            <a
              class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
              href="#"
            >
              <i class="fas fa-file-alt mr-2"></i>Reports
            </a>
            <a
              class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
              href="#"
            >
              <i class="fas fa-users mr-2"></i>Users
            </a>
            <a
              class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
              href="#"
            >
              <i class="fas fa-store mr-2"></i>Store
            </a>
            <a
              class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
              href="#"
            >
              <i class="fas fa-exchange-alt mr-2"></i>Transactions
            </a>
          </nav>

          <a
            class="block text-gray-500 py-2.5 px-4 my-2 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white mt-auto"
            href="/GameStore/logout"
          >
            <i class="fas fa-sign-out-alt mr-2"></i>Logout
          </a>

          <div
            class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mt-2"
          ></div>

          <p class="mb-1 px-5 py-3 text-left text-xs text-indigo-500">ACE</p>
        </div>

        <div class="flex-1 p-4">
          <div class="relative max-w-md w-full">
            <div class="absolute top-1 left-2 inline-flex items-center p-2">
              <i class="fas fa-search text-gray-400"></i>
            </div>
            <input
              id="searchInput"
              class="w-full h-10 pl-10 pr-4 py-1 text-base placeholder-gray-500 border rounded-full focus:shadow-outline"
              type="search"
              placeholder="Search..."
            />
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-2 p-2">
            <div class="bg-white p-4 rounded-md">
              <h2 class="text-gray-500 text-lg font-semibold pb-1">
                Total Users
              </h2>
              <div class="my1-"></div>
              <div
                class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
              ></div>
              <div
                class="chart-container"
                style="position: relative; height: 150px; width: 100%"
                data-total-users="${totalUsers}"
                data-new-users="${newUsersThisMonth}"
              >
                <canvas id="usersChart"></canvas>
              </div>
            </div>

            <div class="bg-white p-4 rounded-md">
              <h2 class="text-gray-500 text-lg font-semibold pb-1">
                Comercios
              </h2>
              <div class="my-1"></div>
              <div
                class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
              ></div>
              <div
                class="chart-container"
                style="position: relative; height: 150px; width: 100%"
              >
                <canvas id="commercesChart"></canvas>
              </div>
            </div>

            <div class="bg-white p-4 rounded-md">
              <h2 class="text-gray-500 text-lg font-semibold pb-4">
                Latest Users
              </h2>
              <div class="my-1"></div>
              <div
                class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
              ></div>
              <table class="w-full table-auto text-sm">
                <thead>
                  <tr class="text-sm leading-normal">
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                    >
                      Portrait
                    </th>
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                    >
                      Name
                    </th>
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                    >
                      Role
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="user" items="${users}" varStatus="status">
                    <c:if test="${status.index < 10}">
                      <tr class="hover:bg-grey-lighter text-center">
                        <td
                          class="py-2 px-4 border-b border-grey-light flex justify-center items-center"
                        >
                          <img src="${empty user.profilePhoto ?
                          '/GameStore/assets/img/users/default.png' :
                          '/GameStore/assets/img/users/'.concat(user.id.toString().replace("-",
                          "").substring(0,
                          10)).concat('/').concat(user.profilePhoto)}"
                          alt="Profile Photo" class="rounded-full h-10 w-10" />
                        </td>
                        <td class="py-2 px-4 border-b border-grey-light">
                          ${user.firstName} ${user.lastName}
                        </td>
                        <td class="py-2 px-4 border-b border-grey-light">
                          <c:set
                            var="role"
                            value="${user.role.name().toLowerCase().split('_')}"
                          />
                          <c:forEach var="word" items="${role}">
                            ${fn:substring(word, 0,
                            1).toUpperCase()}${fn:substring(word, 1,
                            fn:length(word))}
                          </c:forEach>
                        </td>
                      </tr>
                    </c:if>
                  </c:forEach>
                </tbody>
              </table>
              <div class="text-right mt-4">
                <button
                  class="bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-2 px-4 rounded"
                >
                  See more
                </button>
              </div>
            </div>

            <div class="bg-white p-4 rounded-md">
              <h2 class="text-gray-500 text-lg font-semibold pb-4">
                Transactions
              </h2>
              <div class="my-1"></div>
              <div
                class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
              ></div>
              <table class="w-full table-auto text-sm">
                <thead>
                  <tr class="text-sm leading-normal">
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                    >
                      Name
                    </th>
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                    >
                      Date
                    </th>
                    <th
                      class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light text-right"
                    >
                      Total
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Carlos Sánchez
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      27/07/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $1500
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Ana Torres
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      28/07/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $2000
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Juan Ramírez
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      29/07/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $1800
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      María Gómez
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      30/07/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $2100
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Luis González
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      31/07/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $1700
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Laura Pérez
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      01/08/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $2400
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Pedro Hernández
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      02/08/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $1950
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Sara Ramírez
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      03/08/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $1850
                    </td>
                  </tr>
                  <tr class="hover:bg-grey-lighter">
                    <td class="py-2 px-4 border-b border-grey-light">
                      Daniel Torres
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light">
                      04/08/2023
                    </td>
                    <td class="py-2 px-4 border-b border-grey-light text-right">
                      $2300
                    </td>
                  </tr>
                </tbody>
              </table>
              <div class="text-right mt-4">
                <button
                  class="bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-2 px-4 rounded"
                >
                  See more
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
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
    <script src="/GameStore/assets/js/dashboard.js"></script>
  </body>
</html>
