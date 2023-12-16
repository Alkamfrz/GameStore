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

document.addEventListener('DOMContentLoaded', () => {
  animateElement(".container", { translateY: [100, 0], opacity: [0, 1] }, DURATION_LONG, "easeOutCubic");

  if (document.querySelector("#error-messages")) {
    animateElement("#error-messages", { translateX: [{ value: -10 }, { value: 10 }, { value: -10 }, { value: 10 }, { value: 0 }] }, DURATION_SHORT, EASING_QUAD);
  }

  if (document.querySelector("#success-messages")) {
    animateElement("#success-messages", { scale: [0, 1] }, DURATION_SHORT, EASING_QUAD);
  }

  document.querySelectorAll('button[type="submit"]').forEach(button => {
    button.addEventListener('mouseover', () => {
      animateElement(button, { scale: [1, 1.05] }, 200, EASING_QUAD);
    });
    button.addEventListener('mouseout', () => {
      animateElement(button, { scale: [1.05, 1] }, 200, EASING_QUAD);
    });
  });

  document.querySelectorAll("input").forEach(input => {
    input.addEventListener('focus', () => {
      animateElement(input, { borderColor: ["#ddd", "#4a56e2"] }, DURATION_SHORT, EASING_QUAD);
    });
    input.addEventListener('blur', () => {
      animateElement(input, { borderColor: ["#4a56e2", "#ddd"] }, DURATION_SHORT, EASING_QUAD);
    });
  });

  document.querySelector("form").addEventListener('submit', (event) => {
    const enteredPassword = document.querySelector("#password").value;
    const confirmedPassword = document.querySelector("#confirmPassword").value;

    if (enteredPassword !== confirmedPassword) {
      event.preventDefault();
      const errorMessage = "<li>Passwords do not match.</li>";
      const passwordErrorList = document.querySelector("#passwordError ul");
      passwordErrorList.innerHTML = '';
      passwordErrorList.innerHTML = errorMessage;
      document.querySelector("#passwordError").style.display = 'block';

      animateElement("#passwordError", { translateX: [{ value: -10 }, { value: 10 }, { value: -10 }, { value: 10 }, { value: 0 }] }, DURATION_SHORT, EASING_QUAD);
    } else {
      document.querySelector("#passwordError ul").innerHTML = '';
      document.querySelector("#passwordError").style.display = 'none';
    }
  });
});