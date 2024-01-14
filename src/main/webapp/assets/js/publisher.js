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

const deletePublisher = async (publisher_id) => {
  const response = await makeAjaxRequest(
    "POST",
    `/GameStore/admin/publisher/delete`,
    { publisher_id: publisher_id }
  );
  if (response.status === "success") {
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({
        type: "success",
        message: "Publisher deleted successfully",
      })
    );
    location.reload();
  } else {
    console.error("Failed to delete publisher");
    sessionStorage.setItem(
      "userOperation",
      JSON.stringify({ type: "error", message: response.message })
    );
    location.reload();
  }
};

const openAddPublisherModal = () => {
  var formHtml = `
                <div>
                        <label for="name" class="swal2-label">Publisher Name</label>
                        <input id="name" class="swal2-input" placeholder="Publisher Name">
                        <label for="country" class="swal2-label">Publisher Country</label>
                        <input id="country" class="swal2-input" placeholder="Publisher Country">
                        <label for="city" class="swal2-label">Publisher City</label>
                        <input id="city" class="swal2-input" placeholder="Publisher City">
                        <label for="address" class="swal2-label">Publisher Address</label>
                        <input id="address" class="swal2-input" placeholder="Publisher Address">
                        <label for="phone" class="swal2-label">Publisher Phone</label>
                        <input id="phone" class="swal2-input" placeholder="Publisher Phone">
                        <label for="email" class="swal2-label">Publisher Email</label>
                        <input id="email" class="swal2-input" placeholder="Publisher Email">
                        <label for="website" class="swal2-label">Publisher Website</label>
                        <input id="website" class="swal2-input" placeholder="Publisher Website">
                        <label for="description" class="swal2-label">Publisher Description</label>
    <textarea id="description" class="swal2-input" placeholder="Publisher Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;"></textarea>
  </div>
        `;

  Swal.fire({
    title: "Add Publisher",
    html: formHtml,
    confirmButtonText: "Add",
    showCancelButton: true,
    preConfirm: function () {
      var name = document.getElementById("name").value;
      var country = document.getElementById("country").value;
      var city = document.getElementById("city").value;
      var address = document.getElementById("address").value;
      var phone = document.getElementById("phone").value;
      var email = document.getElementById("email").value;
      var website = document.getElementById("website").value;
      var description = document.getElementById("description").value;

      if (!name || !country || !city || !address || !phone || !email) {
        Swal.showValidationMessage(
          "All fields except website and description are required"
        );
        return false;
      }

      return {
        name: name,
        country: country,
        city: city,
        address: address,
        phone: phone,
        email: email,
        website: website,
        description: description,
      };
    },
  }).then(async function (result) {
    if (result.isConfirmed) {
      const response = await makeAjaxRequest(
        "POST",
        "/GameStore/admin/publisher/add",
        result.value
      );
      if (response.status === "success") {
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({
            type: "success",
            message: "Publisher added successfully",
          })
        );
        location.reload();
      } else {
        console.error("Failed to add publisher");
        sessionStorage.setItem(
          "userOperation",
          JSON.stringify({ type: "error", message: response.message })
        );
        location.reload();
      }
    }
  });
};

const openEditPublisherModal = async (publisher_id) => {
  try {
    const response = await makeAjaxRequest(
      "GET",
      `/GameStore/admin/publisher/${publisher_id}`
    );

    const originalFormData = {
      name: response.name,
      country: response.country,
      city: response.city,
      address: response.address,
      phone: response.phone,
      email: response.email,
      website: response.website,
      description: response.description,
    };

    var formHtml = `
            <div>
                <label for="name" class="swal2-label">Publisher Name</label>
                <input id="name" class="swal2-input" value="${response.name}" placeholder="Publisher Name">
                <label for="country" class="swal2-label">Publisher Country</label>
                <input id="country" class="swal2-input" value="${response.country}" placeholder="Publisher Country">
                <label for="city" class="swal2-label">Publisher City</label>
                <input id="city" class="swal2-input" value="${response.city}" placeholder="Publisher City">
                <label for="address" class="swal2-label">Publisher Address</label>
                <input id="address" class="swal2-input" value="${response.address}" placeholder="Publisher Address">
                <label for="phone" class="swal2-label">Publisher Phone</label>
                <input id="phone" class="swal2-input" value="${response.phone}" placeholder="Publisher Phone">
                <label for="email" class="swal2-label">Publisher Email</label>
                <input id="email" class="swal2-input" value="${response.email}" placeholder="Publisher Email">
                <label for="website" class="swal2-label">Publisher Website</label>
                <input id="website" class="swal2-input" value="${response.website}" placeholder="Publisher Website">
                <label for="description" class="swal2-label">Publisher Description</label>
    <textarea id="description" class="swal2-input" placeholder="Publisher Description" style="min-height: 100px; width: 100%; border: 1px solid #ccc; padding: 10px; overflow: auto; resize: none;">${response.description}</textarea>
  </div>
        `;

    Swal.fire({
      title: "Edit Publisher",
      html: formHtml,
      confirmButtonText: "Save",
      showCancelButton: true,
      cancelButtonText: "Delete",
      preConfirm: function () {
        var name = document.getElementById("name").value;
        var country = document.getElementById("country").value;
        var city = document.getElementById("city").value;
        var address = document.getElementById("address").value;
        var phone = document.getElementById("phone").value;
        var email = document.getElementById("email").value;
        var website = document.getElementById("website").value;
        var description = document.getElementById("description").value;

        if (!name || !country || !city || !address || !phone || !email) {
          Swal.showValidationMessage(
            "All fields except website and description are required"
          );
          return false;
        }

        const currentFormData = {
          name,
          country,
          city,
          address,
          phone,
          email,
          website,
          description,
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
          publisher_id: publisher_id,
          name: name,
          country: country,
          city: city,
          address: address,
          phone: phone,
          email: email,
          website: website,
          description: description,
        };
      },
    }).then(async function (result) {
      if (result.isConfirmed) {
        const response = await makeAjaxRequest(
          "POST",
          "/GameStore/admin/publisher/edit",
          result.value
        );
        if (response.status === "success") {
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({
              type: "success",
              message: "Publisher edited successfully",
            })
          );
          location.reload();
        } else {
          console.error("Failed to edit publisher");
          sessionStorage.setItem(
            "userOperation",
            JSON.stringify({ type: "error", message: response.message })
          );
          location.reload();
        }
      } else if (result.dismiss === "cancel") {
        Swal.fire({
          title: "Are you sure?",
          html: `You are about to delete the publisher:<br><br>
                     <b>${response.name}</b><br><br>You won't be able to revert this!<br><br>
                     <input id="confirmPublisherName" class="swal2-input" placeholder="Type publisher name to confirm" style="width: 70%;">`,
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Yes, delete it!",
          preConfirm: function () {
            var confirmPublisherName = document.getElementById(
              "confirmPublisherName"
            ).value;
            if (
              !confirmPublisherName ||
              confirmPublisherName !== response.name
            ) {
              Swal.showValidationMessage(
                "Please type the correct publisher name to confirm deletion"
              );
              return false;
            }
            return true;
          },
        }).then((result) => {
          if (result.isConfirmed) {
            deletePublisher(publisher_id);
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
    var table = $("#publisherTable").DataTable({
      pagingType: "full_numbers",
      order: [[1, "desc"]],
      language: {
        emptyTable: "No publisher found",
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
