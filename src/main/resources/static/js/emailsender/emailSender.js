let emailSenderButton = $("#emailSender");
let inputEmailDiv = $("#inputEmailDiv");
let inputEmail = $("#inputEmail");
let submitSendEmail = $("#submitSendEmail");

let emailSenderDivActive = true;
emailSenderButton.on("click", function () {
    if (emailSenderDivActive) {
        inputEmailDiv.css({
            "display": "block"
        });
        emailSenderDivActive = false;
    } else {
        inputEmailDiv.css({
            "display": "none"
        });
        emailSenderDivActive = true;
    }
});

submitSendEmail.on("click", function () {
    $.post({
        url: '/email/passwordRecover',
        data: {
            email: inputEmail.val()
        },
        success: function () {
            $.notify("Message with new password \nwill send to your email", {
                position: 'top right',
                className: 'info'
            });
        },
        error: function (data) {
            $.notify(data.responseText, {
                position: 'top right',
                className: 'error'
            });
        }
    });
});