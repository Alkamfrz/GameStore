<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Store" />
    <jsp:param name="description" value="Store" />
    <jsp:param name="css" value="/GameStore/assets/css/store.css" />
  </jsp:include>
  <body>
    <main class="flex flex-col h-screen bg-gray-200">
      <jsp:include page="/WEB-INF/partials/navbar.jsp">
        <jsp:param name="title" value="Store" />
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
            <c:forEach var="game" items="${games}">
              <div class="bg-white p-4 rounded-md">
                <h2 class="text-gray-500 text-lg font-semibold pb-1">
                  ${game.game_name}
                </h2>
                <div class="my-1"></div>
                <div
                  class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mb-6"
                ></div>
                <div
                  class="chart-container"
                  style="position: relative; height: 150px; width: 100%"
                >
                  <img
                    src="${game.image}"
                    alt="${game.game_name}"
                    class="w-full h-auto"
                  />
                </div>
                <div class="text-right mt-4">
                  <p>$${game.price}</p>
                  <a
                    href="/GameStore/store/${game.game_id}"
                    id="buyNow"
                    class="bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-2 px-4 rounded inline-block"
                  >
                    Buy Now
                  </a>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>
    </main>
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/store.js" />
    </jsp:include>
  </body>
</html>
