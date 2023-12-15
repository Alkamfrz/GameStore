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

const makeAjaxRequest = async (type, url, data) => {
  try {
    const response = await $.ajax({ type, url, data });
    return response;
  } catch (error) {
    console.error("Error:", error);
  }
};

const deleteUser = async (user_id) => {
  const response = await makeAjaxRequest(
    "POST",
    `/GameStore/admin/users/delete`,
    { user_id: user_id }
  );
  if (response.status === "success") {
    sessionStorage.setItem("userOperation", "User deleted successfully");
    location.reload();
  } else {
    console.error("Failed to delete user");
  }
};

const openEditModal = async (user_id) => {
  try {
    const response = await makeAjaxRequest(
      "GET",
      `/GameStore/admin/users/${user_id}`
    );

    const originalFormData = {
      firstName: response.firstName,
      lastName: response.lastName,
      username: response.username,
      email: response.email,
      role: response.role,
    };

    var formHtml = `
      <div>
        <label for="firstName" class="swal2-label">First Name</label>
        <input id="firstName" class="swal2-input" value="${response.firstName}" placeholder="First Name">
      </div>

      <div>
        <label for="lastName" class="swal2-label">Last Name</label>
        <input id="lastName" class="swal2-input" value="${response.lastName}" placeholder="Last Name">
      </div>

      <div>
        <label for="username" class="swal2-label">Username</label>
        <input id="username" class="swal2-input" value="${response.username}" placeholder="Username">
      </div>

      <div>
        <label for="email" class="swal2-label">Email</label>
        <input id="email" class="swal2-input" value="${response.email}" placeholder="Email">
      </div>

      <div>
        <label for="role" class="swal2-label">Role</label>
        <select id="role" class="swal2-input">
          <option value="ADMIN">Admin</option>
          <option value="CUSTOMER">Customer</option>
        </select>
      </div>
    `;

    Swal.fire({
      title: "Edit User",
      html: formHtml,
      confirmButtonText: "Save",
      showCancelButton: true,
      cancelButtonText: "Delete",
      preConfirm: function () {
        var firstName = document.getElementById("firstName").value;
        var lastName = document.getElementById("lastName").value;
        var username = document.getElementById("username").value;
        var email = document.getElementById("email").value;
        var role = document.getElementById("role").value;

        if (!firstName || !lastName || !username || !email || !role) {
          Swal.showValidationMessage("All fields are required");
          return false;
        }

        const currentFormData = { firstName, lastName, username, email, role };
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
          user_id: user_id,
          firstName: firstName,
          lastName: lastName,
          username: username,
          email: email,
          role: role,
        };
      },
      didOpen: function () {
        var roleSelect = document.getElementById("role");
        for (var i = 0; i < roleSelect.options.length; i++) {
          if (roleSelect.options[i].value === response.role) {
            roleSelect.options[i].selected = true;
            break;
          }
        }
      },
    }).then(async function (result) {
      if (result.isConfirmed) {
        const response = await makeAjaxRequest(
          "POST",
          "/GameStore/admin/users/edit",
          result.value
        );
        if (response.status === "success") {
          sessionStorage.setItem("userOperation", "User updated successfully");
          location.reload();
        } else {
          console.error("Failed to update user");
        }
      } else if (result.dismiss === "cancel") {
        Swal.fire({
          title: "Are you sure?",
          html: `You are about to delete the user with the following details:<br><br><b>Full Name:</b> ${response.firstName} ${response.lastName}<br><b>Username:</b> ${response.username}<br><b>Email:</b> ${response.email}<br><br>You won't be able to revert this!`,
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Yes, delete it!",
        }).then((result) => {
          if (result.isConfirmed) {
            deleteUser(user_id);
          }
        });
      }
    });
  } catch (error) {
    console.error("Error:", error);
  }
};

$(document).ready(() => {
  setupDropdownAnimation();

  const userOperation = sessionStorage.getItem("userOperation");
  if (userOperation) {
    Swal.fire({
      icon: "success",
      title: userOperation,
      showConfirmButton: true,
      confirmButtonText: "OK",
      timer: 1500,
    });
    sessionStorage.removeItem("userOperation");
  }
});
