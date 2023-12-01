$(document).ready(function() {
  anime({
    targets: '.container',
    translateY: [100, 0],
    opacity: [0, 1],
    duration: 1000,
    easing: 'easeOutCubic',
  });

  if ($('#error-messages').length) {
    anime({
      targets: '#error-messages',
      translateX: [{value: -10}, {value: 10}, {value: -10}, {value: 10}, {value: 0}],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  }

  if ($('#success-messages').length) {
    anime({
      targets: '#success-messages',
      scale: [0, 1],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  }

  $('button[type="submit"]').hover(
    function() {
      anime({
        targets: this,
        scale: [1, 1.05],
        duration: 200,
        easing: 'easeInOutQuad',
      });
    },
    function() {
      anime({
        targets: this,
        scale: [1.05, 1],
        duration: 200,
        easing: 'easeInOutQuad',
      });
    }
  );

  $('input').focus(function() {
    anime({
      targets: this,
      borderColor: ['#ddd', '#4a56e2'],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  }).blur(function() {
    anime({
      targets: this,
      borderColor: ['#4a56e2', '#ddd'],
      duration: 500,
      easing: 'easeInOutQuad',
    });
  });
});