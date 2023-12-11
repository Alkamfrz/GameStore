function deleteUser(userId) {
    $.ajax({
        type: "POST",
        url: "/GameStore/admin/users/delete",
        data: { id: userId },
        success: function (response) {
            if (response.status === "success") {
                console.log("User deleted successfully");
            } else {
                console.error("Failed to delete user");
            }
        },
        error: function (error) {
            console.error("Error:", error);
        },
    });
}

function openEditModal(userId) {
    $.ajax({
        type: "GET",
        url: "/GameStore/admin/users/" + userId,
        success: function (response) {
            var formHtml = `
                <input id="firstName" class="swal2-input" value="${response.firstName}">
                <input id="lastName" class="swal2-input" value="${response.lastName}">
                <input id="username" class="swal2-input" value="${response.username}">
                <input id="email" class="swal2-input" value="${response.email}">
                <select id="role" class="swal2-input">
                    <option value="ADMIN">Admin</option>
                    <option value="CUSTOMER">Customer</option>
                </select>
            `;

            Swal.fire({
                title: 'Edit User',
                html: formHtml,
                confirmButtonText: 'Save',
                footer: '<button id="deleteButton" class="btn btn-danger">Delete</button>',
                preConfirm: function() {
                    return {
                        firstName: document.getElementById('firstName').value,
                        lastName: document.getElementById('lastName').value,
                        username: document.getElementById('username').value,
                        email: document.getElementById('email').value,
                        role: document.getElementById('role').value
                    };
                },
                didOpen: function() {
                    document.getElementById('deleteButton').addEventListener('click', function() {
                        deleteUser(userId);
                    });
                }
            }).then(function(result) {
                if (result.isConfirmed) {
                    $.ajax({
                        type: "POST",
                        url: "/GameStore/admin/users/edit",
                        data: result.value,
                        success: function (response) {
                            if (response.status === "success") {
                                console.log("User updated successfully");
                            } else {
                                console.error("Failed to update user");
                            }
                        },
                        error: function (error) {
                            console.error("Error:", error);
                        },
                    });
                }
            });
        },
        error: function (error) {
            console.error("Error:", error);
        },
    });
}