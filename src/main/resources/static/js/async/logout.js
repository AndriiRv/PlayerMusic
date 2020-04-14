let logout = $("#logout");

logout.on("click", function () {
    $.get({
        url: '/logout',
        success: function () {
            location.reload();
        },
    });
});