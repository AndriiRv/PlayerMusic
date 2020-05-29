let signUpButton = $('#signUp');
let signInButton = $('#signIn');
let container = $('#container');

let signUpName = $("#signUpName");
let signUpSurname = $("#signUpSurname");
let signUpEmail = $("#signUpEmail");
let signUpUsername = $("#signUpUsername");
let signUpPassword = $("#signUpPassword");
let signUpPassConf = $("#signUpPassConf");

signUpButton.on('click', function () {
    container.addClass('right-panel-active');
});

signInButton.on('click', function () {
    container.removeClass('right-panel-active');
});

function highlightErrorInput(object) {
    if (object.val().length >= 1) {
        removeErrorHighlightByElement(object);
    } else {
        setBorderToInputByElement(object);
    }
}

function highlightDataIfItAlreadyInUse(errorString) {
    if (errorString.includes("Username is already in use")) {
        signUpUsername.css({
            "border-color": "red",
            "border-style": "solid"
        });
    }
    if (errorString.includes("Email is already in use")) {
        signUpEmail.css({
            "border-color": "red",
            "border-style": "solid"
        });
    }
    if (errorString.includes("Password must match")) {
        signUpPassConf.css({
            "border-color": "red",
            "border-style": "solid"
        });
    }
    if (errorString.includes("Email is incorrect")) {
        signUpEmail.css({
            "border-color": "red",
            "border-style": "solid"
        });
    }
}

function setBorderToInputByElement(object) {
    object.css({
        "border-color": "red",
        "border-style": "solid"
    });
}

function removeErrorHighlightByElement(object) {
    object.css({
            "border-color": "transparent",
            "border-style": "none"
        }
    )
}

function removeAllErrorHighlights() {
    $("input").css({
        "border-color": "transparent",
        "border-style": "none"
    });
}

function cleanAllInputAfterSuccessfulSignUp() {
    signUpName.val("");
    signUpSurname.val("");
    signUpEmail.val("");
    signUpUsername.val("");
    signUpPassword.val("");
    signUpPassConf.val("");
    removeAllErrorHighlights();
}

$("#submitSignUp").on('click', function () {
    $.post({
        url: '/sign',
        data: {
            name: signUpName.val(),
            surname: signUpSurname.val(),
            email: signUpEmail.val(),
            username: signUpUsername.val(),
            password: signUpPassword.val(),
            passwordConfirmation: signUpPassConf.val(),
        },
        success: function () {
            container.removeClass('right-panel-active');
            signUpButton.notify(signUpUsername.val() + " was sign up!", {
                position: 'bottom',
                className: 'success'
            });
            sendWelcomeEmailLetter(
                signUpUsername.val(),
                signUpName.val(),
                signUpSurname.val(),
                signUpEmail.val()
            );
            cleanAllInputAfterSuccessfulSignUp();
        },
        error: function (data) {
            let stringBuilderErrors = "";
            $.each(data.responseJSON.errors, function (index, error) {
                stringBuilderErrors = stringBuilderErrors + error + "\n";
            });

            highlightErrorInput(signUpName);
            highlightErrorInput(signUpSurname);
            highlightErrorInput(signUpEmail);
            highlightErrorInput(signUpUsername);
            highlightErrorInput(signUpPassword);
            highlightErrorInput(signUpPassConf);

            highlightDataIfItAlreadyInUse(stringBuilderErrors);

            signInButton.notify(stringBuilderErrors, {
                position: 'bottom',
                className: 'error'
            });
        }
    });
});

function converterToJson(username, name, surname, email) {
    return {
        username: username,
        name: name,
        surname: surname,
        email: email
    }
}

function sendWelcomeEmailLetter(username, name, surname, email) {
    $.post({
        url: '/email/welcomeLetter',
        data: JSON.stringify(converterToJson(username, name, surname, email)),
        contentType: "application/json",
        processData: false
    });
}