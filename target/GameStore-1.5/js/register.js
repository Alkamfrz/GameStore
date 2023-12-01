$(document).ready(function () {
  anime({
    targets: "form",
    translateY: [-100, 0],
    opacity: [0, 1],
    duration: 1000,
    easing: "easeOutCubic",
  });

  $("form").on("submit", function (event) {
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();
    var username = $("#username").val();
    var email = $("#email").val();
    var password = $("#password").val();
    var confirmPassword = $("#confirm-password").val();

    if (
      firstName === "" ||
      lastName === "" ||
      username === "" ||
      email === "" ||
      password === "" ||
      confirmPassword === ""
    ) {
      showAlert("All fields must be filled out");
      event.preventDefault();
    } else if (password !== confirmPassword) {
      showAlert("Passwords do not match");
      event.preventDefault();
    }
  });

  function showAlert(message) {
    $("#alert-message").text(message);
    $("#error").removeClass("hidden");

    anime({
      targets: "#error",
      translateY: [-100, 0],
      opacity: [0, 1],
      duration: 1000,
      easing: "easeOutCubic",
    });
  }
});
