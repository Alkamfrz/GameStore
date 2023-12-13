$(document).ready(() => {
  setupDropdownAnimation();
  setupCharts();
  setupMenuButton();
  setupSearchFilter();
});

const setupSearchFilter = () => {
  $("#searchInput").on("keyup", function () {
    var value = $(this).val().toLowerCase();
    $("table").each(function () {
      $(this)
        .find("tbody tr")
        .filter(function () {
          $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
  });
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

const setupCharts = () => {
  const totalUsers = $(".chart-container").data("total-users");
  const newUsersThisMonth = $(".chart-container").data("new-users");
  createChart(
    "usersChart",
    ["New (This Month)", "Registered"],
    [newUsersThisMonth, totalUsers],
    ["#00F0FF", "#8B8B8D"]
  );
  createChart(
    "commercesChart",
    ["Nuevos", "Registrados"],
    [60, 40],
    ["#FEC500", "#8B8B8D"]
  );
};

const createChart = (elementId, labels, data, backgroundColors) => {
  new Chart($(`#${elementId}`), {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [
        {
          data: data,
          backgroundColor: backgroundColors,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        position: "bottom",
      },
    },
  });
};

const setupMenuButton = () => {
  $("#menuBtn").on("click", () => {
    const sideNav = $("#sideNav");
    const isHidden = sideNav.hasClass("hidden");
    const animationSettings = getMenuAnimationSettings(isHidden);
    anime(animationSettings);

    if (isHidden) {
      sideNav.animate({ width: "toggle" }, 350);
    } else {
      sideNav.animate({ width: "toggle" }, 350);
    }

    sideNav.toggleClass("hidden");
  });
};

const getMenuAnimationSettings = (isHidden) => ({
  targets: "#menuBtn",
  translateX: isHidden ? [0, 100] : [100, 0],
  scale: isHidden ? [1, 1.2] : [1.2, 1],
  duration: 500,
  easing: "easeInOutQuad",
  begin: isHidden ? () => $("#menuBtn").addClass("open") : undefined,
  complete: isHidden ? undefined : () => $("#menuBtn").removeClass("open"),
  delay: isHidden ? 0 : 500,
});
