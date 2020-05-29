let signButton = $("#signButton");

signButton.on("click", function () {
    getSignModalWindow();
});

function getSignModalWindow() {
    dashboardCenter.css("z-index", 0);
    dashboardSide.css("z-index", 0);
    $("#container").modal({
        fadeDuration: 350
    });
}