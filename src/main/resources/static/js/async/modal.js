$("#signButton").on("click", function () {

    $("#menu").css({
        "position": "inherit"
    });

    $("#container").modal({
        fadeDuration: 350,
        showClose: false
    });
});
