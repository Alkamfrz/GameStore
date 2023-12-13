function openEditModal(userId) {
  $.ajax({
    type: "GET",
    url: "/GameStore/admin/users/" + userId,
    success: function (response) {
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

          return {
            user_id: userId,
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
      }).then(function (result) {
        if (result.isConfirmed) {
          $.ajax({
            type: "POST",
            url: "/GameStore/admin/users/edit",
            data: result.value,
            success: function (response) {
              if (response.status === "success") {
                console.log("User updated successfully");
                location.reload();
              } else {
                console.error("Failed to update user");
              }
            },
            error: function (error) {
              console.error("Error:", error);
            },
          });
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
              deleteUser(userId);
            }
          });
        }
      });
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}

function deleteUser(userId) {
  $.ajax({
    type: "POST",
    url: "/GameStore/admin/users/delete",
    data: { user_id: userId },
    success: function (response) {
      if (response.status === "success") {
        console.log("User deleted successfully");
        location.reload();
      } else {
        console.error("Failed to delete user");
      }
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}

$(document).ready(function () {
  $("#userTable").DataTable({
    ajax: {
      url: "/GameStore/admin/users",
      type: "GET",
      datatype: "json",
    },
    columns: [
      { data: "user_id", autoWidth: true },
      {
        data: null,
        render: function (data, type, row, meta) {
          return data.firstName + " " + data.lastName;
        },
        autoWidth: true,
      },
      { data: "updatedAt", autoWidth: true },
      { data: "createdAt", autoWidth: true },
      { data: "lastLogin", autoWidth: true },
      { data: "role.name", autoWidth: true },
      {
        data: "user_id",
        render: function (data, type, row, meta) {
          return `<button class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded" onclick="openEditModal('${data}')">Edit</button>`;
        },
      },
    ],
  });
});
