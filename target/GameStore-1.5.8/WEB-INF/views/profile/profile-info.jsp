<%@ include file="/WEB-INF/partials/taglibs.jsp" %>
<form action="/GameStore/profile" method="POST" enctype="multipart/form-data">
  <article
    id="profile-info"
    class="w-full bg-white rounded-lg mx-auto mt-8 flex overflow-hidden rounded-b-none"
  >
    <aside class="w-1/3 bg-gray-100 p-8 hidden md:inline-block">
      <h2 class="font-medium text-md text-gray-700 mb-4 tracking-wide">
        Profile Info
      </h2>
      <p class="text-xs text-gray-500">
        Update your basic profile information such as Email Address, Name, and
        Image.
      </p>
      <p class="text-xs text-gray-500 font-bold mt-2">
        Registered at: <c:out value="${createdAt}" />
      </p>
      <p class="text-xs text-gray-500 font-bold mt-2">
        Last Login: <c:out value="${lastLogin}" />
    </aside>
    <div class="md:w-2/3 w-full flex flex-wrap">
      <div class="py-4 px-8 w-full md:w-1/2">
        <label for="firstName" class="text-sm text-gray-600">First Name</label>
        <input
          class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
          type="text"
          value="${firstName}"
          name="firstName"
        />
      </div>
      <div class="py-4 px-8 w-full md:w-1/2">
        <label for="lastName" class="text-sm text-gray-600">Last Name</label>
        <input
          class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
          type="text"
          value="${lastName}"
          name="lastName"
        />
      </div>
      <div class="py-4 px-8 w-full md:w-1/2">
        <label for="email" class="text-sm text-gray-600">Email Address</label>
        <input
          class="mt-2 border-2 border-gray-200 px-3 py-2 block w-full rounded-lg text-base text-gray-900 focus:outline-none focus:border-indigo-500"
          type="email"
          name="email"
          value="${email}"
          disabled
        />
      </div>
      <div class="py-4 px-8 w-full md:w-1/2">
        <label for="username" class="text-sm text-gray-600">Username</label>
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
        '/GameStore/assets/user_profile/default.png' :
        '/GameStore/assets/user_profile/'.concat(user.user_id.toString().replace("-",
        "").substring(0, 10)).concat('/images/').concat(user.profilePhoto)}"
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
        <button id="deletePhotoButton" class="bg-red-500 text-white text-xs mt-5 ml-3 font-bold px-4 py-2 rounded-lg float-left hover:bg-red-700 cursor-pointer">
          Delete Photo
        </button>
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
