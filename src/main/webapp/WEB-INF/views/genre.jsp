<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Genre Management" />
    <jsp:param name="description" value="Genre Management" />
    <jsp:param name="css" value="/GameStore/assets/css/genre.css" />
  </jsp:include>

  <body>
    <main class="flex flex-col h-screen bg-gray-200">
      <jsp:include page="/WEB-INF/partials/navbar.jsp">
        <jsp:param name="title" value="Genre Management" />
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
              placeholder="Search Genres..."
            />
          </div>

          <div class="mt-2 p-2">
            <div class="bg-white p-4 rounded-md">
              <div class="flex justify-between items-center">
                <h2 class="text-gray-500 text-lg font-semibold pb-1">
                  Genre List
                </h2>
                <button
                  class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded mb-2"
                  onclick="openAddGenreModal()"
                >
                  <i class="fas fa-plus"></i>
                  Add Genre
                </button>
              </div>
              <div class="my1-"></div>
              <div
                class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
              ></div>
              <c:choose>
                <c:when test="${empty genres}">
                  <div class="flex justify-center items-center">
                    <p>No Data</p>
                  </div>
                </c:when>
                <c:otherwise>
                  <table id="genreTable" class="w-full table-auto text-sm">
                    <thead>
                      <tr class="text-sm leading-normal">
                        <th
                          class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                        >
                          ID
                        </th>
                        <th
                          class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                        >
                          Name
                        </th>
                        <th
                          class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                          >
                          Description
                        </th>
                        <th
                          class="py-2 px-4 bg-grey-lightest font-bold uppercase text-sm text-grey-light border-b border-grey-light"
                        >
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="genre" items="${genres}">
                        <tr class="hover:bg-grey-lighter">
                          <td class="py-2 px-4 border-b border-grey-light">
                            ${genre.genre_id}
                          </td>
                          <td class="py-2 px-4 border-b border-grey-light">
                            ${genre.genre_name}
                          </td>
                          <td class="py-2 px-4 border-b border-grey-light">
                            ${genre.genre_description}
                          </td>
                          <td class="py-2 px-4 border-b border-grey-light">
                            <button
                              class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded"
                              onclick="openEditGenreModal('${genre.genre_id}')"
                            >
                              Edit
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
      </div>
    </main>
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/genre.js" />
    </jsp:include>
  </body>
</html>
