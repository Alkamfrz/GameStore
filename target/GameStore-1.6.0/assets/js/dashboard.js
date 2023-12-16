document.addEventListener('DOMContentLoaded', () => {
  setupDropdownAnimation();
  setupCharts();
  setupMenuButton();
  setupSearchFilter();
});

const setupSearchFilter = () => {
  const searchInput = document.querySelector("#searchInput");

  searchInput.addEventListener("keyup", function () {
    const value = this.value.toLowerCase();
    let hasVisibleRows = false;

    const tables = document.querySelectorAll("table");
    tables.forEach((table) => {
      const rows = table.querySelectorAll("tbody tr");
      rows.forEach((row) => {
        const isVisible = row.textContent.toLowerCase().includes(value);
        row.style.display = isVisible ? "" : "none";
        if (isVisible) {
          hasVisibleRows = true;
        }
      });
    });

    const noDataMessage = document.querySelector("#noDataMessage");
    const seeMore = document.querySelector("#seeMore");

    if (hasVisibleRows) {
      tables.forEach((table) => table.style.display = "");
      noDataMessage.style.display = "none";
      seeMore.style.display = "";
    } else {
      tables.forEach((table) => table.style.display = "none");
      noDataMessage.style.display = "";
      seeMore.style.display = "none";
    }
  });
};

const setupDropdownAnimation = () => {
  const dropdown = document.querySelector(".dropdown");
  const dropdownMenu = dropdown.querySelector(".dropdown-menu");

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

const setupCharts = () => {
  const totalUsers = document.querySelector(".chart-container").dataset.totalUsers;
  const newUsersThisMonth = document.querySelector(".chart-container").dataset.newUsers;
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
  new Chart(document.getElementById(elementId), {
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
  const menuBtn = document.querySelector("#menuBtn");
  const sideNav = document.querySelector("#sideNav");

  menuBtn.addEventListener('click', () => {
    const isHidden = sideNav.classList.contains("hidden");
    const animationSettings = getMenuAnimationSettings(isHidden);
    anime(animationSettings);

    if (isHidden) {
      sideNav.style.width = '0';
    } else {
      sideNav.style.width = 'auto';
    }

    sideNav.classList.toggle("hidden");
  });
};

const getMenuAnimationSettings = (isHidden) => ({
  targets: "#menuBtn",
  translateX: isHidden ? [0, 100] : [100, 0],
  scale: isHidden ? [1, 1.2] : [1.2, 1],
  duration: 500,
  easing: "easeInOutQuad",
  begin: isHidden ? () => document.querySelector("#menuBtn").classList.add("open") : undefined,
  complete: isHidden ? undefined : () => document.querySelector("#menuBtn").classList.remove("open"),
  delay: isHidden ? 0 : 500,
});