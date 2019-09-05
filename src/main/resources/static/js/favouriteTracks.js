var scrollPositionOnTitle;
var notification = $("#notification");

$(".setFavourite").on('click', function () {
    scrollPositionOnTitle = $(document).scrollTop();
    sessionStorage.setItem("scrollPositionOnClickFavourite", scrollPositionOnTitle);
    notification.fadeIn();
    notification.show();
    notification.html("Added to favourite!");
    setTimeout(function () {
        $("#notification").hide();
    }, 4000);
});

window.scrollTo(0, parseInt(sessionStorage.getItem("scrollPositionOnClickFavourite")));