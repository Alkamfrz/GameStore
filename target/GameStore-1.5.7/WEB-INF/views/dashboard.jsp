<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Dashboard" />
    <jsp:param name="description" value="Dashboard" />
    <jsp:param name="css" value="/GameStore/assets/css/dashboard.css" />
  </jsp:include>
  <body>
    <main class="flex flex-col h-screen bg-gray-200">
      <jsp:include page="/WEB-INF/partials/navbar.jsp">
        <jsp:param name="title" value="Dashboard" />
      </jsp:include>

      <div class="flex-1 flex">
        <jsp:include page="/WEB-INF/partials/sidebar.jsp" />

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
                          '/GameStore/assets/user_profile/default.png' :
                          '/GameStore/assets/user_profile/'.concat(user.user_id.toString().replace("-",
                          "").substring(0,
                          10)).concat('/images/').concat(user.profilePhoto)}"
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
    </main>
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/dashboard.js" />
    </jsp:include>
  </body>
</html>
