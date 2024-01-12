<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<aside class="p-2 bg-white w-60 flex flex-col hidden md:flex" id="sideNav">
  <nav>
    <c:if test="${role == 'admin'}">
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="/GameStore/admin/dashboard"
      >
        <i class="fas fa-home mr-2"></i>Dashboard
      </a>
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="/GameStore/admin/users"
      >
        <i class="fas fa-users mr-2"></i>Users
      </a>
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="/GameStore/admin/games"
      >
        <i class="fas fa-gamepad mr-2"></i>Games
      </a>
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="/GameStore/admin/genres"
      >
        <i class="fas fa-tags mr-2"></i>Genres
      </a>
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="#"
      >
        <i class="fas fa-exchange-alt mr-2"></i>Transactions
      </a>
    </c:if>
    <c:if test="${role == 'customer'}">
      <a
        class="block text-gray-500 py-2.5 px-4 my-4 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white"
        href="/GameStore/store"
      >
        <i class="fas fa-store mr-2"></i>Store
      </a>
    </c:if>
  </nav>

  <a
    class="block text-gray-500 py-2.5 px-4 my-2 rounded transition duration-200 hover:bg-gradient-to-r hover:from-indigo-400 hover:to-indigo-300 hover:text-white mt-auto"
    href="/GameStore/logout"
  >
    <i class="fas fa-sign-out-alt mr-2"></i>Logout
  </a>

  <div class="bg-gradient-to-r from-indigo-300 to-indigo-500 h-px mt-2"></div>
  <p class="mb-1 px-5 py-3 text-left text-xs text-indigo-500">ACE</p>
</aside>
