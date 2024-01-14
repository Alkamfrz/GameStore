const getAnimationSettings = (isHidden, dropdownMenu) => ({
  targets: dropdownMenu,
  translateY: isHidden ? [-70, 0] : [0, -70],
  opacity: isHidden ? [0, 1] : [1, 0],
  scale: isHidden ? [0.9, 1] : [1, 0.9],
  duration: 700,
  easing: isHidden ? "easeOutExpo" : "easeInExpo",
  begin: isHidden ? () => (dropdownMenu.style.display = "block") : undefined,
  complete: isHidden ? undefined : () => (dropdownMenu.style.display = "none"),
  delay: anime.stagger(100),
});

const setupDropdownAnimation = () => {
  const dropdown = document.querySelector(".dropdown");
  const dropdownMenu = dropdown.querySelector(".dropdown-menu");

  dropdown.addEventListener("click", (event) => {
    event.stopPropagation();
    const isHidden = dropdownMenu.style.display === "none";
    const animationSettings = getAnimationSettings(isHidden, dropdownMenu);
    anime(animationSettings);
  });

  document.addEventListener("click", () => {
    if (dropdownMenu.style.display !== "none") {
      const animationSettings = getAnimationSettings(false, dropdownMenu);
      anime(animationSettings);
    }
  });
};

const makeAjaxRequest = async (type, url, data) => {
  try {
    const response = await fetch(url, {
      method: type,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    return response.json();
  } catch (error) {
    console.error("Error:", error);
  }
};

const deleteGame = async (game_id) => {
  const response = await makeAjaxRequest(
    "POST",
    `/GameStore/admin/game/delete`,
    { game_id: game_id }
  );
  if (response.status === "success") {
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({ type: "success", message: "Game deleted successfully" })
    );
    location.reload();
  } else {
    console.error("Failed to delete game");
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({ type: "error", message: response.message })
    );
    location.reload();
  }
};

async function fetchPublishers() {
  const response = await fetch('/GameStore/admin/publisher/all');
  const data = await response.json();
  return data;
}

async function fetchGenres() {
  const response = await fetch('/GameStore/admin/genre/all');
  const data = await response.json();
  return data;
}

const openAddGameModal = async () => {
  const publishers = await fetchPublishers();
  const genres = await fetchGenres();

  const publisherOptions = publishers.map(p => `<option value="${p.id}">${p.name}</option>`).join('');
  const genreOptions = genres.map(g => `<option value="${g.id}">${g.name}</option>`).join('');
  var formHtml = `
                <div>
                        <label for="name" class="swal2-label">Game Name</label>
                        <input id="name" class="swal2-input" placeholder="Game Name">
                        <label for="release_date" class="swal2-label">Game Release Date</label>
                        <input id="release_date" class="swal2-input" type="date" placeholder="Game Release Date">
                        <label for="rating" class="swal2-label">Game Rating</label>
                        <input id="rating" class="swal2-input" type="number" placeholder="Game Rating">
                        <label for="price" class="swal2-label">Game Price</label>
                        <input id="price" class="swal2-input" type="number" placeholder="Game Price">
                        <label for="description" class="swal2-label">Game Description</label>
                        <textarea id="description" class="swal2-input" placeholder="Game Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;"></textarea>
                        <label for="image" class="swal2-label">Game Image</label>
                        <input id="image" class="swal2-input" placeholder="Game Image URL">
                        <label for="publisher" class="swal2-label">Game Publisher</label>
                        <select id="publisher" class="swal2-input">${publisherOptions}</select>
                        <label for="genres" class="swal2-label">Game Genres</label>
                        <select id="genres" class="swal2-input" multiple>${genreOptions}</select>
                </div>
        `;

  Swal.fire({
    title: "Add Game",
    html: formHtml,
    confirmButtonText: "Add",
    showCancelButton: true,
    didOpen: () => {
      new Choices('#genres', {
        removeItemButton: true,
        maxItemCount: 5,
        searchResultLimit: 5,
        renderChoiceLimit: 5
      });
      new Choices('#publisher', {
        removeItemButton: true,
        maxItemCount: 1,
        searchResultLimit: 5,
        renderChoiceLimit: 5
      });
    },
    preConfirm: function () {
      var name = document.getElementById("name").value;
      var release_date = new Date(document.getElementById("release_date").value).getTime();
      var rating = document.getElementById("rating").value;
      var price = document.getElementById("price").value;
      var description = document.getElementById("description").value;
      var image = document.getElementById("image").value;
      var publisher = document.getElementById("publisher").value;
      var genres = Array.from(document.getElementById("genres").selectedOptions).map(option => option.value);

      if (!name || !release_date || !rating || !price || !image || !publisher || !genres.length) {
        Swal.showValidationMessage("All fields are required");
        return false;
      }

      return {
        name: name,
        release_date: release_date,
        rating: rating,
        price: price,
        description: description,
        image: image,
        publisher: publisher,
        genres: genres,
      };
    },
  }).then(async function (result) {
    if (result.isConfirmed) {
      const response = await makeAjaxRequest(
        "POST",
        "/GameStore/admin/game/add",
        result.value
      );
      if (response.status === "success") {
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({
            type: "success",
            message: "Game added successfully",
          })
        );
        location.reload();
      } else {
        console.error("Failed to add game");
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({ type: "error", message: response.message })
        );
        location.reload();
      }
    }
  });
};

const openEditGameModal = async (game_id) => {
  try {
    const response = await makeAjaxRequest(
      "GET",
      `/GameStore/admin/game/${game_id}`
    );

    const publishers = await fetchPublishers();
    const genres = await fetchGenres();

    const publisherOptions = publishers.map(p => `<option value="${p.id}" ${p.id === response.publisher ? 'selected' : ''}>${p.name}</option>`).join('');
    const genreOptions = genres.map(g => `<option value="${g.id}" ${response.genres.includes(g.id) ? 'selected' : ''}>${g.name}</option>`).join('');

    const originalFormData = {
      name: response.name,
      release_date: response.release_date,
      rating: response.rating,
      price: response.price,
      description: response.description,
      image: response.image,
      publisher: response.publisher,
      genres: response.genres,
    };

    var formHtml = `
            <div>
                <label for="name" class="swal2-label">Game Name</label>
                <input id="name" class="swal2-input" value="${response.name}" placeholder="Game Name">
                <label for="release_date" class="swal2-label">Game Release Date</label>
                <input id="release_date" class="swal2-input" type="date" value="${response.release_date}" placeholder="Game Release Date">
                <label for="rating" class="swal2-label">Game Rating</label>
                <input id="rating" class="swal2-input" type="number" value="${response.rating}" placeholder="Game Rating">
                <label for="price" class="swal2-label">Game Price</label>
                <input id="price" class="swal2-input" type="number" value="${response.price}" placeholder="Game Price">
                <label for="description" class="swal2-label">Game Description</label>
                <textarea id="description" class="swal2-input" placeholder="Game Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;">${response.description}</textarea>
                <label for="image" class="swal2-label">Game Image</label>
                <input id="image" class="swal2-input" value="${response.image}" placeholder="Game Image URL">
                <label for="publisher" class="swal2-label">Game Publisher</label>
                <select id="publisher" class="swal2-input">${publisherOptions}</select>
                <label for="genres" class="swal2-label">Game Genres</label>
                <select id="genres" class="swal2-input" multiple>${genreOptions}</select>
            </div>
        `;

    Swal.fire({
      title: "Edit Game",
      html: formHtml,
      confirmButtonText: "Save",
      showCancelButton: true,
      cancelButtonText: "Delete",
      didOpen: () => {
        new Choices('#genres', {
          removeItemButton: true,
          maxItemCount: 5,
          searchResultLimit: 5,
          renderChoiceLimit: 5
        });
        new Choices('#publisher', {
          removeItemButton: true,
          maxItemCount: 1,
          searchResultLimit: 5,
          renderChoiceLimit: 5
        });
      },
      preConfirm: function () {
        var name = document.getElementById("name").value;
        var release_date = document.getElementById("release_date").value;
        var rating = document.getElementById("rating").value;
        var price = document.getElementById("price").value;
        var description = document.getElementById("description").value;
        var image = document.getElementById("image").value;
        var publisher = document.getElementById("publisher").value;
        var genres = Array.from(document.getElementById("genres").selectedOptions).map(option => option.value);

        if (!name || !release_date || !rating || !price || !image || !publisher || !genres.length) {
          Swal.showValidationMessage("All fields are required");
          return false;
        }

        const currentFormData = {
          name,
          release_date,
          rating,
          price,
          description,
          image,
          publisher,
          genres,
        };
        if (
          JSON.stringify(originalFormData) === JSON.stringify(currentFormData)
        ) {
          Swal.fire({
            icon: "info",
            title: "No changes detected",
          });
          return false;
        }

        return {
          game_id: game_id,
          name: name,
          release_date: release_date,
          rating: rating,
          price: price,
          description: description,
          image: image,
          publisher: publisher,
          genres: genres,
        };
      },
    }).then(async function (result) {
      if (result.isConfirmed) {
        const response = await makeAjaxRequest(
          "POST",
          "/GameStore/admin/game/edit",
          result.value
        );
        if (response.status === "success") {
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({
              type: "success",
              message: "Game edited successfully",
            })
          );
          location.reload();
        } else {
          console.error("Failed to edit game");
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({ type: "error", message: response.message })
          );
          location.reload();
        }
      } else if (result.dismiss === "cancel") {
        Swal.fire({
          title: "Are you sure?",
          html: `You are about to delete the game:<br><br>
                     <b>${response.name}</b><br><br>You won't be able to revert this!<br><br>
                     <input id="confirmGameName" class="swal2-input" placeholder="Type game name to confirm" style="width: 70%;">`,
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Yes, delete it!",
          preConfirm: function () {
            var confirmGameName =
              document.getElementById("confirmGameName").value;
            if (!confirmGameName || confirmGameName !== response.name) {
              Swal.showValidationMessage(
                "Please type the correct game name to confirm deletion"
              );
              return false;
            }
            return true;
          },
        }).then((result) => {
          if (result.isConfirmed) {
            deleteGame(game_id);
          }
        });
      }
    });
  } catch (error) {
    console.error("Error:", error);
  }
};

document.addEventListener("DOMContentLoaded", () => {
  const userOperation = JSON.parse(sessionStorage.getItem("userOperation"));
  if (userOperation) {
    Swal.fire({
      icon: userOperation.type,
      title: userOperation.message,
      showConfirmButton: true,
      confirmButtonText: "OK",
      timer: 1500,
    });
    sessionStorage.removeItem("userOperation");
  }
  setupDropdownAnimation();
  $(document).ready(function () {
    var table = $("#gameTable").DataTable({
      pagingType: "full_numbers",
      order: [[1, "desc"]],
      language: {
        emptyTable: "No game found",
      },
      lengthMenu: [
        [10, 25, 50, -1],
        [10, 25, 50, "All"],
      ],
      columnDefs: [{ orderable: false, targets: [2] }],
    });

    $("#searchInput").on("keyup", function () {
      table.search(this.value).draw();
    });
  });
})