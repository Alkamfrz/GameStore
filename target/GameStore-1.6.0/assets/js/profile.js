// Store references to DOM elements
const dropdown = $(".dropdown");
const dropdownMenu = dropdown.children(".dropdown-menu");
const adminButton = $("#adminButton");
const storeButton = $("#storeButton");
const photo = $("#photo");
const photoInput = $("#photoInput");
const deletePhotoButton = $("#deletePhotoButton");
const profileForm = $("#profileForm");
const passwordChangeForm = $("#passwordChangeForm");

// Store sections in an object instead of an array
const sections = {
  "profile-info": document.getElementById("profile-info"),
  security: document.getElementById("security"),
};

// Helper functions
const redirectTo = (url) => (window.location.href = url);

const getAnimationSettings = (isHidden, dropdownMenu) => ({
  targets: dropdownMenu[0],
  translateY: isHidden ? [-70, 0] : [0, -70],
  opacity: isHidden ? [0, 1] : [1, 0],
  scale: isHidden ? [0.9, 1] : [1, 0.9],
  duration: 700,
  easing: isHidden ? "easeOutExpo" : "easeInExpo",
  begin: isHidden ? () => dropdownMenu.show() : undefined,
  complete: isHidden ? undefined : () => dropdownMenu.hide(),
  delay: anime.stagger(100),
});

const setupDropdownAnimation = () => {
  dropdown.click((event) => {
    event.stopPropagation();
    const isHidden = dropdownMenu.is(":hidden");
    const animationSettings = getAnimationSettings(isHidden, dropdownMenu);
    anime(animationSettings);
  });

  $(document).click(() => {
    if (!dropdownMenu.is(":hidden")) {
      const animationSettings = getAnimationSettings(false, dropdownMenu);
      anime(animationSettings);
    }
  });
};

// Main functions
const showSection = (id) => {
  Object.keys(sections).forEach((section) => {
    sections[section].style.display = section === id ? "block" : "none";
  });
};

const loadFile = (event) => {
  const selectedFile = event.target.files[0];
  if (selectedFile) {
    const objectUrl = URL.createObjectURL(selectedFile);
    photo.attr("src", objectUrl);

    const imageLoaded = new Promise((resolve) => {
      photo.on("load", function () {
        URL.revokeObjectURL(objectUrl);
        resolve();
      });
    });

    imageLoaded.then(() => {
      deletePhotoButton.show();
      photoSaved = false;
    });
  }
};

let photoToDelete = false;
let photoSaved = true;

const deletePhoto = () => {
  if (photoSaved) {
    photoToDelete = true;
  }
  deletePhotoButton.hide();
  photoInput.val("");
  photo.attr("src", "/GameStore/assets/user_profile/default.png");
};

const updateAriaCurrent = (element) => {
  $('nav[role="navigation"] a')
    .removeAttr("aria-current")
    .removeClass("text-gray-900 border-b-2 border-gray-800");
  $(element)
    .attr("aria-current", "page")
    .addClass("text-gray-900 border-b-2 border-gray-800");
};

let originalFormData = null;

const updateProfile = async (e) => {
  e.preventDefault();

  const currentFormData = new FormData($("#profileForm")[0]);

  const originalData = Object.fromEntries(originalFormData);
  const currentData = Object.fromEntries(currentFormData);

  originalData.photoToDelete = false;
  currentData.photoToDelete = photoToDelete;

  let isChanged = false;
  for (let key in originalData) {
    if (key === "photo") {
      if (originalData[key].name !== currentData[key].name) {
        isChanged = true;
        break;
      }
    } else {
      if (originalData[key] !== currentData[key]) {
        isChanged = true;
        break;
      }
    }
  }

  if (!isChanged) {
    Swal.fire({
      icon: "info",
      title: "No changes detected",
    });
    return;
  }

  if (photoToDelete) {
    currentFormData.append("deletePhoto", "true");
  }

  currentFormData.append("action", "updateProfile");

  try {
    const response = await $.ajax({
      type: "POST",
      url: "/GameStore/profile",
      data: currentFormData,
      processData: false,
      contentType: false,
      action: "updateProfile",
    });

    if (response.status === "success") {
      photoToDelete = false;
      photoSaved = true;

      $("#firstName").val(currentFormData.get("firstName"));
      $("#lastName").val(currentFormData.get("lastName"));
      $("#email").val(response.email || $("#email").val());
      $("#username").val(response.username || $("#username").val());

      originalFormData = new FormData($("#profileForm")[0]);

      Swal.fire({
        icon: "success",
        title: "Profile updated successfully",
      });
    } else {
      throw new Error("Failed to update profile");
    }
  } catch (error) {
    console.error(error);
    Swal.fire({
      icon: "error",
      title: "Oops...",
      text: "Failed to update profile!",
    });
  }
};

const updatePassword = async (e) => {
  e.preventDefault();

  const passwordFormData = new FormData();
  passwordFormData.append("currentPassword", $("#currentPassword").val());
  passwordFormData.append("newPassword", $("#newPassword").val());
  passwordFormData.append("confirmPassword", $("#confirmPassword").val());

  // Validate the inputs
  if (
    !passwordFormData.get("currentPassword") ||
    !passwordFormData.get("newPassword") ||
    !passwordFormData.get("confirmPassword")
  ) {
    Swal.fire({
      icon: "info",
      title: "All fields are required",
    });
    return;
  }

  if (
    passwordFormData.get("newPassword") !==
    passwordFormData.get("confirmPassword")
  ) {
    Swal.fire({
      icon: "info",
      title: "New password and confirm password do not match",
    });
    return;
  }

  passwordFormData.append("action", "changePassword");

  try {
    const response = await $.ajax({
      type: "POST",
      url: "/GameStore/profile",
      data: passwordFormData,
      processData: false,
      contentType: false,
      action: "changePassword",
    });

    if (response.status === "success") {
      Swal.fire({
        icon: "success",
        title: "Password updated successfully",
      }).then(() => {
        redirectTo("/GameStore/logout");
      });
    } else {
      throw new Error("Failed to update password");
    }
  } catch (error) {
    console.error(error);
    Swal.fire({
      icon: "error",
      title: "Oops...",
      text: "Failed to update password!",
    });
  }
};

// Event handlers
$(document).ready(() => {
  setupDropdownAnimation();

  adminButton.click(() => redirectTo("/GameStore/admin/dashboard"));
  storeButton.click(() => redirectTo("/GameStore/store"));

  $('a[href="#profile-info"]').click(function (e) {
    e.preventDefault();
    showSection("profile-info");
    updateAriaCurrent(this);
  });
  $('a[href="#security"]').click(function (e) {
    e.preventDefault();
    showSection("security");
    updateAriaCurrent(this);
  });

  if (photo.attr("src") === "/GameStore/assets/user_profile/default.png") {
    deletePhotoButton.hide();
  }

  deletePhotoButton.off("click").click((e) => {
    e.preventDefault();
    deletePhoto();
  });

  originalFormData = new FormData($("#profileForm")[0]);

  profileForm.submit(updateProfile);
  passwordChangeForm.submit(updatePassword);

  if (sessionStorage.getItem("profileUpdate") === "success") {
    Swal.fire({
      icon: "success",
      title: "Profile updated successfully",
    });
    sessionStorage.removeItem("profileUpdate");
  }

  showSection("profile-info");
  updateAriaCurrent($('a[href="#profile-info"]'));
});
