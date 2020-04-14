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
            location.reload();
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