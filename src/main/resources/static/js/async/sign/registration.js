let signUpButton = $('#signUp');
let signInButton = $('#signIn');
let container = $('#container');

signUpButton.on('click', function () {
    container.addClass('right-panel-active');
});

signInButton.on('click', function () {
    container.removeClass('right-panel-active');
});

$("#submitSignUp").on('click', function () {
    $.post({
        url: '/sign',
        data: {
            name: $("input[name=name]").val(),
            surname: $("input[name=surname]").val(),
            email: $("input[name=email]").val(),
            username: $("input[name=username]").val(),
            password: $("input[name=password]").val(),
            passwordConfirmation: $("input[name=passwordConfirmation]").val(),
        },
        success: function () {
            container.removeClass('right-panel-active');
            signUpButton.notify($("input[name=username]").val() + " was sign up!", {
                position: 'bottom',
                className: 'success'
            });
            $("input[name=name]").val('');
            $("input[name=surname]").val('');
            $("input[name=email]").val('');
            $("input[name=username]").val('');
            $("input[name=password]").val('');
            $("input[name=passwordConfirmation]").val('');
            $("#errorSignUp").empty();
        },
        error: function (data) {
            $("#errorSignUp").empty();
            $('#errorSignUp').append('<ul>');
            $.each(data.responseJSON.errors, function (index, error) {
                $('#errorSignUp').append('<li>' + error + '</li>');
            });
            $('#errorSignUp').append('</ul>');
        }
    });
});
