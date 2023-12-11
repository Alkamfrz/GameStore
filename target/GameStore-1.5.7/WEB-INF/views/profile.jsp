<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/WEB-INF/partials/header.jsp">
    <jsp:param name="title" value="Profile" />
    <jsp:param name="description" value="Update your profile" />
    <jsp:param name="css" value="/GameStore/assets/css/profile.css" />
  </jsp:include>
  <body>
    <main class="bg-gray-200 min-h-screen pb-24">
      <jsp:include page="/WEB-INF/partials/navbar.jsp">
        <jsp:param name="title" value="Profile" />
      </jsp:include>

      <div class="flex-1 flex">
        <jsp:include page="/WEB-INF/partials/sidebar.jsp" />

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
            <li class="mr-8">
              <a href="#profile-info" class="py-4 inline-block">Profile Info</a>
            </li>
            <li class="mr-8 hover:text-gray-900">
              <a href="#security" class="py-4 inline-block">Security</a>
            </li>
            <li class="mr-8 hover:text-gray-900">
              <a href="#billing" class="py-4 inline-block">Billing</a>
            </li>
          </ul>
        </nav>
        <div id="profile-info" style="display: none;">
          <jsp:include page="/WEB-INF/views/profile/profile-info.jsp" />
        </div>
        <div id="security" style="display: none;">
          <jsp:include page="/WEB-INF/views/profile/security.jsp" />
        </div>
      </section>
      </div>
    </main>
    <jsp:include page="/WEB-INF/partials/scripts.jsp">
      <jsp:param name="js" value="/GameStore/assets/js/profile.js" />
    </jsp:include>
  </body>
</html>
