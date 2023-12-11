<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
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
      <div class="py-4 px-8 w-full md:w-full">
        <label for="currentPassword" class="text-sm text-gray-600"
          >Current Password</label
        >
        <input
          class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
          type="password"
          name="currentPassword"
        />
      </div>
      <div class="py-4 px-8 w-full md:w-full">
        <label for="newPassword" class="text-sm text-gray-600"
          >New Password</label
        >
        <input
          class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
          type="password"
          name="newPassword"
        />
      </div>
      <div class="py-4 px-8 w-full md:w-full">
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