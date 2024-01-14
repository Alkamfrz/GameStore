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

const deleteGenre = async (genre_id) => {
  const response = await makeAjaxRequest(
    "POST",
    `/GameStore/admin/genre/delete`,
    { genre_id: genre_id }
  );
  if (response.status === "success") {
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({ type: "success", message: "Genre deleted successfully" })
    );
    location.reload();
  } else {
    console.error("Failed to delete genre");
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({ type: "error", message: response.message })
    );
    location.reload();
  }
};

const openAddGenreModal = () => {
  var formHtml = `
        <div>
            <label for="name" class="swal2-label">Genre Name</label>
            <input id="name" class="swal2-input" placeholder="Genre Name">
            <label for="description" class="swal2-label">Publisher Description</label>
    <textarea id="description" class="swal2-input" placeholder="Publisher Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;"></textarea>
  </div>
    `;

  Swal.fire({
    title: "Add Genre",
    html: formHtml,
    confirmButtonText: "Add",
    showCancelButton: true,
    preConfirm: function () {
      var name = document.getElementById("name").value;
      var description = document.getElementById("description").value;

      if (!name) {
        Swal.showValidationMessage("Genre name is required");
        return false;
      }

      return {
        name: name,
        description: description,
      };
    },
  }).then(async function (result) {
    if (result.isConfirmed) {
      const response = await makeAjaxRequest(
        "POST",
        "/GameStore/admin/genre/add",
        result.value
      );
      if (response.status === "success") {
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({
            type: "success",
            message: "Genre added successfully",
          })
        );
        location.reload();
      } else {
        console.error("Failed to add genre");
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({ type: "error", message: response.message })
        );
        location.reload();
      }
    }
  });
};

const openEditGenreModal = async (genre_id) => {
  try {
    const response = await makeAjaxRequest(
      "GET",
      `/GameStore/admin/genre/${genre_id}`
    );

    const originalFormData = {
      name: response.name,
      description: response.description,
    };

    var formHtml = `
      <div>
        <label for="name" class="swal2-label">Genre Name</label>
        <input id="name" class="swal2-input" value="${
          response.name
        }" placeholder="Genre Name">
        <label for="description" class="swal2-label">Publisher Description</label>
        <textarea id="description" class="swal2-input" placeholder="Publisher Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;">${response.description}</textarea>
      </div>
    `;

    Swal.fire({
      title: "Edit Genre",
      html: formHtml,
      confirmButtonText: "Save",
      showCancelButton: true,
      cancelButtonText: "Delete",
      preConfirm: function () {
        var name = document.getElementById("name").value;
        var description = document.getElementById("description").value;

        if (!name) {
          Swal.showValidationMessage("Genre name is required");
          return false;
        }

        const currentFormData = { name, description };
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
          genre_id: genre_id,
          name: name,
          description: description,
        };
      },
    }).then(async function (result) {
      if (result.isConfirmed) {
        const response = await makeAjaxRequest(
          "POST",
          "/GameStore/admin/genre/edit",
          result.value
        );
        if (response.status === "success") {
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({
              type: "success",
              message: "Genre edited successfully",
            })
          );
          location.reload();
        } else {
          console.error("Failed to edit genre");
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({ type: "error", message: response.message })
          );
          location.reload();
        }
      } else if (result.dismiss === "cancel") {
        Swal.fire({
          title: "Are you sure?",
          html: `You are about to delete the genre:<br><br>
           <b>${response.name}</b><br><br>You won't be able to revert this!<br><br>
           <input id="confirmGenreName" class="swal2-input" placeholder="Type genre name to confirm" style="width: 70%;">`,
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Yes, delete it!",
          preConfirm: function () {
            var confirmGenreName =
              document.getElementById("confirmGenreName").value;
            if (!confirmGenreName || confirmGenreName !== response.name) {
              Swal.showValidationMessage(
                "Please type the correct genre name to confirm deletion"
              );
              return false;
            }
            return true;
          },
        }).then((result) => {
          if (result.isConfirmed) {
            deleteGenre(genre_id);
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
    var table = $("#genreTable").DataTable({
      pagingType: "full_numbers",
      order: [[1, "desc"]],
      language: {
        emptyTable: "No genre found",
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
});
