document.addEventListener('DOMContentLoaded', () => {
  anime({
    targets: '.container',
    translateY: [100, 0],
    opacity: [0, 1],
    duration: 1000,
    easing: 'easeOutCubic',
  });

  const errorMessages = document.querySelector('#error-messages');
  if (errorMessages) {
    anime({
      targets: '#error-messages',
      translateX: [{value: -10}, {value: 10}, {value: -10}, {value: 10}, {value: 0}],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  }

  const successMessages = document.querySelector('#success-messages');
  if (successMessages) {
    anime({
      targets: '#success-messages',
      scale: [0, 1],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  }

  const submitButtons = document.querySelectorAll('button[type="submit"]');
  submitButtons.forEach(button => {
    button.addEventListener('mouseover', () => {
      anime({
        targets: button,
        scale: [1, 1.05],
        duration: 200,
        easing: 'easeInOutQuad',
      });
    });

    button.addEventListener('mouseout', () => {
      anime({
        targets: button,
        scale: [1.05, 1],
        duration: 200,
        easing: 'easeInOutQuad',
      });
    });
  });

  const inputs = document.querySelectorAll('input');
  inputs.forEach(input => {
    input.addEventListener('focus', () => {
      anime({
        targets: input,
        borderColor: ['#ddd', '#4a56e2'],
        duration: 500,
        easing: 'easeInOutQuad',
      });
    });

    input.addEventListener('blur', () => {
      anime({
        targets: input,
        borderColor: ['#4a56e2', '#ddd'],
        duration: 500,
        easing: 'easeInOutQuad',
      });
    });
  });
});