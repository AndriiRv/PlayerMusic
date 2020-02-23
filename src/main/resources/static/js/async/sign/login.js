let counterForMenu = 0;

$("#currentUserName").on("click", function () {
    counterForMenu++;

    if (counterForMenu % 2 !== 0) {
        $(".dropdown .dropdown-content").show();
    } else {
        $(".dropdown .dropdown-content").hide();
    }
});

$("#submitSignIn").on('click', function () {
    $.post({
        url: '/login',
        data: {
            username: $("#loginUsername").val(),
            password: $("#loginPassword").val(),
        },
        success: function () {
            location.reload();
        },
        error: function (data) {
            $("#errorSignIn").empty();
            $('#errorSignIn').append('<ul>');
            $('#errorSignIn').append(data.responseText);
            $('#errorSignIn').append('</ul>');
        }
    });
});
