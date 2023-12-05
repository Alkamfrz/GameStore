var totalUsers = $('.chart-container').data('total-users');
var newUsersThisMonth = $('.chart-container').data('new-users');

var usersChart = new Chart($('#usersChart'), {
    type: 'doughnut',
    data: {
        labels: ['New (This Month)', 'Registered'],
        datasets: [{
            data: [newUsersThisMonth, totalUsers],
            backgroundColor: ['#00F0FF', '#8B8B8D'],
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom'
        }
    }
});

var commercesChart = new Chart($('#commercesChart'), {
    type: 'doughnut',
    data: {
        labels: ['Nuevos', 'Registrados'],
        datasets: [{
            data: [60, 40],
            backgroundColor: ['#FEC500', '#8B8B8D'],
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom'
        }
    }
});

$('#menuBtn').on('click', function() {
    $('#sideNav').toggleClass('hidden');
});