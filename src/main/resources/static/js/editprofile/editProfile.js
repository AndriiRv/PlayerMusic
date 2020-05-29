let editProfile = $("#editProfile");

editProfile.on("click", function () {
    openEditForm();
    getEditProfile();
});

function getEditProfile() {
    setTitleToNameOfTab("Edit Profile");

    let html = '';
    html += '<div id="editForm">' +
        '       <h2>Edit ' + currentUserUsername.text() + ' account </h2>\n' +
        '        <p class="lead">Please do logout from account for apply changing</p>' +
        '                <div class="mb-3"> ' +
        '                    <input class="form-control" type="password" name="password" id="editProfilePassword" ' +
        '                           placeholder="Input new password"/>' +
        '                </div>' +
        '                <div class="mb-3">' +
        '                    <input class="form-control" type="text" name="name" id="editProfileName" placeholder="First name"' +
        '                           value="' + currentUserName.text() + '"/>' +
        '                </div>' +
        '                <div class="mb-3">' +
        '                    <input class="form-control" type="text" name="surname" id="editProfileSurname" placeholder="Last name"' +
        '                           value="' + currentUserSurname.text() + '"/>' +
        '                </div>' +
        '                <div class="mb-3">' +
        '                    <input class="form-control" type="email" name="email" id="editProfileEmail" placeholder="Email"' +
        '                           value="' + currentUserEmail.text() + '"/>' +
        '                </div>' +
        '<button id="editButton" class="btn btn-primary btn-lg btn-block">Change data</button>' +
        '</div>';

    supportDashboard.html(html);

    $("#editButton").on("click", function () {
        updateProfile();
    });
}

let editProfilePassword = null;
let editProfileName = null;
let editProfileSurname = null;
let editProfileEmail = null;

function setDto() {
    supportDashboard.on('input', $("#editProfilePassword"), function () {
        editProfilePassword = $("#editProfilePassword");
    });
    supportDashboard.on('input', $("#editProfileName"), function () {
        editProfileName = $("#editProfileName");
    });
    supportDashboard.on('input', $("#editProfileSurname"), function () {
        editProfileSurname = $("#editProfileSurname");
    });
    supportDashboard.on('input', $("#editProfileEmail"), function () {
        editProfileEmail = $("#editProfileEmail");
    });
}

function updateProfile() {
    setDto();
    $.post({
        url: '/edit',
        data: {
            surname: $("#editProfileSurname").val(),
            name: $("#editProfileName").val(),
            email: $("#editProfileEmail").val(),
            password: $("#editProfilePassword").val(),
            username: currentUserUsername.text()
        },
        success: function () {
            $.notify(currentUserUsername.text() + " was updated!", {
                position: 'top left',
                className: 'success'
            });
        },
        error: function (data) {
            $.notify(data.responseText, {
                position: 'top left',
                className: 'error'
            });
        }
    });
}