// Store references to DOM elements
const dropdown = document.querySelector(".dropdown");
const dropdownMenu = dropdown.querySelector(".dropdown-menu");
const adminButton = document.querySelector("#adminButton");
const storeButton = document.querySelector("#storeButton");
const photo = document.querySelector("#photo");
const photoInput = document.querySelector("#photoInput");
const deletePhotoButton = document.querySelector("#deletePhotoButton");
const profileForm = document.querySelector("#profileForm");
const passwordChangeForm = document.querySelector("#passwordChangeForm");

// Store sections in an object instead of an array
const sections = {
  "profile-info": document.getElementById("profile-info"),
  security: document.getElementById("security"),
};

// Helper functions
const redirectTo = (url) => (window.location.href = url);

const getAnimationSettings = (isHidden, dropdownMenu) => ({
  targets: dropdownMenu,
  translateY: isHidden ? [-70, 0] : [0, -70],
  opacity: isHidden ? [0, 1] : [1, 0],
  scale: isHidden ? [0.9, 1] : [1, 0.9],
  duration: 700,
  easing: isHidden ? "easeOutExpo" : "easeInExpo",
  begin: isHidden ? () => dropdownMenu.style.display = 'block' : undefined,
  complete: isHidden ? undefined : () => dropdownMenu.style.display = 'none',
  delay: anime.stagger(100),
});

const setupDropdownAnimation = () => {
  dropdown.addEventListener('click', (event) => {
    event.stopPropagation();
    const isHidden = dropdownMenu.style.display === 'none';
    const animationSettings = getAnimationSettings(isHidden, dropdownMenu);
    anime(animationSettings);
  });

  document.addEventListener('click', () => {
    if (dropdownMenu.style.display !== 'none') {
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
    photo.src = objectUrl;

    const imageLoaded = new Promise((resolve) => {
      photo.addEventListener('load', function () {
        URL.revokeObjectURL(objectUrl);
        resolve();
      });
    });

    imageLoaded.then(() => {
      deletePhotoButton.style.display = 'block';
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
  deletePhotoButton.style.display = 'none';
  photoInput.value = "";
  photo.src = "/GameStore/assets/user_profile/default.png";
};

const updateAriaCurrent = (element) => {
  document.querySelectorAll('nav[role="navigation"] a')
    .forEach(el => {
      el.removeAttribute("aria-current");
      el.classList.remove("text-gray-900", "border-b-2", "border-gray-800");
    });
  element.setAttribute("aria-current", "page");
  element.classList.add("text-gray-900", "border-b-2", "border-gray-800");
};

let originalFormData = null;

const formDataToObject = (formData) => Object.fromEntries(formData);

const validateFormData = (formData, fields) => {
  for (let field of fields) {
    if (!formData.get(field)) {
      Swal.fire({
        icon: "info",
        title: `Field ${field} is required`,
      });
      return false;
    }
  }
  return true;
};

const sendRequest = async (url, formData) => {
  const response = await fetch(url, {
    method: 'POST',
    body: formData,
  });

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  return await response.json();
};

const handleResponse = (response, successMessage, errorMessage) => {
  if (response.status === "success") {
    Swal.fire({
      icon: "success",
      title: successMessage,
    });
    originalFormData = new FormData(profileForm);
  } else {
    throw new Error(errorMessage);
  }
};

const updateProfile = async (e) => {
  e.preventDefault();

  const currentFormData = new FormData($("#profileForm")[0]);

  const originalData = formDataToObject(originalFormData);
  const currentData = formDataToObject(currentFormData);

  originalData.photoToDelete = false;
  currentData.photoToDelete = photoToDelete;

  const isChanged = Object.keys(originalData).some((key) => {
    if (key === "photo") {
      return originalData[key].name !== currentData[key].name;
    } else {
      return originalData[key] !== currentData[key];
    }
  });

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
    const response = await sendRequest("/GameStore/profile", currentFormData);
    handleResponse(response, "Profile updated successfully", "Failed to update profile");
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

  if (!validateFormData(passwordFormData, ["currentPassword", "newPassword", "confirmPassword"])) {
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
    const response = await sendRequest("/GameStore/profile", passwordFormData);
    handleResponse(response, "Password updated successfully", "Failed to update password");
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
document.addEventListener('DOMContentLoaded', (event) => {
  setupDropdownAnimation();

  if(adminButton) {
    adminButton.addEventListener('click', () => redirectTo("/GameStore/admin/dashboard"));
  }

  if(storeButton) {
    storeButton.addEventListener('click', () => redirectTo("/GameStore/store"));
  }

  document.querySelector('a[href="#profile-info"]').addEventListener('click', function (e) {
    e.preventDefault();
    showSection("profile-info");
    updateAriaCurrent(this);
  });

  document.querySelector('a[href="#security"]').addEventListener('click', function (e) {
    e.preventDefault();
    showSection("security");
    updateAriaCurrent(this);
  });

  if (photo.src.endsWith("/GameStore/assets/user_profile/default.png")) {
    deletePhotoButton.style.display = "none";
  } else {
    deletePhotoButton.style.display = "block";
  }

  photo.onload = function () {
    if (photo.src.endsWith("/GameStore/assets/user_profile/default.png")) {
      deletePhotoButton.style.display = "none";
    } else {
      deletePhotoButton.style.display = "block";
    }
  };

  deletePhotoButton.removeEventListener("click", deletePhoto);
  deletePhotoButton.addEventListener("click", (e) => {
    e.preventDefault();
    deletePhoto();
  });

  originalFormData = new FormData(profileForm);

  profileForm.addEventListener('submit', updateProfile);
  passwordChangeForm.addEventListener('submit', updatePassword);

  if (sessionStorage.getItem("profileUpdate") === "success") {
    Swal.fire({
      icon: "success",
      title: "Profile updated successfully",
    });
    sessionStorage.removeItem("profileUpdate");
  }

  showSection("profile-info");
  updateAriaCurrent(document.querySelector('a[href="#profile-info"]'));
});