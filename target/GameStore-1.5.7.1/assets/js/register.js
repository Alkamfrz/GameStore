const EASING_QUAD = "easeInOutQuad";
const DURATION_SHORT = 500;
const DURATION_LONG = 1000;

function animateElement(targets, properties, duration, easing) {
  anime({
    targets: targets,
    ...properties,
    duration: duration,
    easing: easing,
  });
}

$(document).ready(function () {
  animateElement(".container", { translateY: [100, 0], opacity: [0, 1] }, DURATION_LONG, "easeOutCubic");

  if ($("#error-messages").length) {
    animateElement("#error-messages", { translateX: [{ value: -10 }, { value: 10 }, { value: -10 }, { value: 10 }, { value: 0 }] }, DURATION_SHORT, EASING_QUAD);
  }

  if ($("#success-messages").length) {
    animateElement("#success-messages", { scale: [0, 1] }, DURATION_SHORT, EASING_QUAD);
  }

  $('button[type="submit"]').hover(
    function () {
      animateElement(this, { scale: [1, 1.05] }, 200, EASING_QUAD);
    },
    function () {
      animateElement(this, { scale: [1.05, 1] }, 200, EASING_QUAD);
    }
  );

  $("input")
    .focus(function () {
      animateElement(this, { borderColor: ["#ddd", "#4a56e2"] }, DURATION_SHORT, EASING_QUAD);
    })
    .blur(function () {
      animateElement(this, { borderColor: ["#4a56e2", "#ddd"] }, DURATION_SHORT, EASING_QUAD);
    });

  $("form").on("submit", function (event) {
    var enteredPassword = $("#password").val();
    var confirmedPassword = $("#confirmPassword").val();

    if (enteredPassword !== confirmedPassword) {
      event.preventDefault();
      var errorMessage = "<li>Passwords do not match.</li>";
      $("#passwordError ul").empty();
      $("#passwordError ul").append(errorMessage);
      $("#passwordError").show();

      animateElement("#passwordError", { translateX: [{ value: -10 }, { value: 10 }, { value: -10 }, { value: 10 }, { value: 0 }] }, DURATION_SHORT, EASING_QUAD);
    } else {
      $("#passwordError ul").empty();
      $("#passwordError").hide();
    }
  });
});