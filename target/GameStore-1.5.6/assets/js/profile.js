function showSection(id) {
  document.getElementById("profile-info").style.display = "none";
  document.getElementById("security").style.display = "none";

  document.getElementById(id).style.display = "block";
}

function loadFile(event) {
  var image = document.getElementById("photo");
  image.src = URL.createObjectURL(event.target.files[0]);
}

$(document).ready(() => {
  setupDropdownAnimation();
  const adminButton = $("#adminButton");
  const storeButton = $("#storeButton");

  adminButton.click(() => redirectTo("/GameStore/admin/dashboard"));
  storeButton.click(() => redirectTo("/GameStore/store"));

  $("#profileForm").ajaxForm({
    beforeSubmit: function () {
      $('#profileForm input[type="submit"]').prop("disabled", true);
    },
    success: function (response) {
      alert("Profile updated successfully");
      $('#profileForm input[type="submit"]').prop("disabled", false);
    },
    error: function (error) {
      alert("There was an error updating the profile");
      $('#profileForm input[type="submit"]').prop("disabled", false);
    },
  });

  $('a[href="#profile-info"]').click(function(e) {
    e.preventDefault();
    showSection('profile-info');
    updateAriaCurrent(this);
  });
  $('a[href="#security"]').click(function(e) {
    e.preventDefault();
    showSection('security');
    updateAriaCurrent(this);
  });

  showSection('profile-info');
  updateAriaCurrent($('a[href="#profile-info"]'));
});

function updateAriaCurrent(element) {
  $('nav[role="navigation"] a').removeAttr('aria-current').removeClass('text-gray-900 border-b-2 border-gray-800');
  $(element).attr('aria-current', 'page').addClass('text-gray-900 border-b-2 border-gray-800');
}

const redirectTo = (url) => {
  window.location.href = url;
};

const setupDropdownAnimation = () => {
  const dropdown = $(".dropdown");
  const dropdownMenu = dropdown.children(".dropdown-menu");

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
