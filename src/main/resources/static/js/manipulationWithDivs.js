$(document).ready(function () {
    var player = $("#player");
    var scrollPositionOnTitle;
    var backToListCounter = 0;
    var counterToScrollToPlayer = 0;
    var notification = $("#notification");

    $(".titleOfTrackInTable").on('click', function () {
        scrollPositionOnTitle = $(document).scrollTop();
        player.css({"display": "flex"});
        player.show();
        counterToScrollToPlayer++;
        if (counterToScrollToPlayer === 1) {
            $('html, body').animate({
                scrollTop: 1
            }, 700);
        }
    });

    $("#backToList").on('click', function () {
        backToListCounter++;
        if (backToListCounter === 1) {
            scrollPositionOnTitle = scrollPositionOnTitle + 1000;
        }
        if (scrollPositionOnTitle === 0) {
            scrollPositionOnTitle = 900;
        }
        $('html, body').animate({
            scrollTop: scrollPositionOnTitle
        }, 700);
    });

    $(".setFavouriteTdClass").on('click', function () {
        scrollPositionOnTitle = $(document).scrollTop();
        sessionStorage.setItem("scrollPositionOnClickFavourite", scrollPositionOnTitle);
        notification.fadeIn();
        notification.show();
        notification.html("Added to favourite!");
        setTimeout(function () {
            $("#notification").hide();
        }, 2000);
    });

    window.scrollTo(0, parseInt(sessionStorage.getItem("scrollPositionOnClickFavourite")));
    scrollPositionOnTitle = 0;
    sessionStorage.setItem("scrollPositionOnClickFavourite", scrollPositionOnTitle);

    var cookie = document.cookie;
    if (cookie.includes("fav")) {
        $("#favouriteTracksButton").css({"display": "block"});
    } else {
        $("#favouriteTracksButton").css({"display": "none"});
    }
});