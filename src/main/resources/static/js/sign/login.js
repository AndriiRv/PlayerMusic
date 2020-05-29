let submitSignIn = $("#submitSignIn");
let usernamePassword = $("#signInUsername");
let passwordField = $("#signInPassword");

let passwordCheckbox = $("#inputCheckbox");

submitSignIn.on('click', function () {
    $.post({
        url: '/login',
        data: {
            username: usernamePassword.val(),
            password: passwordField.val(),
        },
        success: function () {
            $.ajax({
                url: '/getAuthorizedUser',
                async: false,
                success: function (userObject) {
                    currentUserName.text(userObject.name);
                    currentUserUsername.text(userObject.username);
                    currentUserSurname.text(userObject.surname);
                    currentUserEmail.text(userObject.email);
                }
            });
            signButton.hide();
            peopleButton.show();
            chatButton.show();
            currentUserUsername.show();
            getDashboard();
            $("#closeModal").click();
        },
        error: function (data) {
            highlightErrorInput(usernamePassword);
            highlightErrorInput(passwordField);
            usernamePassword.notify(data.responseText, {
                position: 'top',
                className: 'error'
            });
        }
    });
});

let checkboxActive = true;
passwordCheckbox.on('click', function () {
    if (checkboxActive) {
        passwordField.get(0).type = 'text';
        checkboxActive = false;
    } else {
        passwordField.get(0).type = 'password';
        checkboxActive = true;
    }
});