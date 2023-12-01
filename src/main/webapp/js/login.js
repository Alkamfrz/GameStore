$(document).ready(function () {
  anime({
    targets: "form",
    translateY: [-100, 0],
    opacity: [0, 1],
    duration: 1000,
    easing: "easeOutCubic",
  });

  $("form").on("submit", function (event) {
    var username = $("#username").val();
    var password = $("#password").val();

    if (username === "" || password === "") {
      showAlert("All fields must be filled out");
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
