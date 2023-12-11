<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<nav
  class="bg-white text-white shadow w-full p-2 flex items-center justify-between"
>
  <div class="flex items-center">
    <div class="hidden md:flex items-center">
      <img
        src="/GameStore/assets/img/logo.png"
        alt="Logo"
        class="w-28 h-18 mr-2"
      />
      <h2 class="font-bold text-xl text-gray-700">${param.title}</h2>
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
</nav>
