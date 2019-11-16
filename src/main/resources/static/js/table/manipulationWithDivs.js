var scrollPositionOnTitle;
var inputSearchTrack = $("#searchTrack");

$(document).ready(function () {
    var desktopPlayer = $("#player");
    var backToListCounter = 0;
    var counterToScrollToPlayer = 0;

    var smallPlayer = $('#smallPlayer');
    $(window).scroll(function () {
        if ($(document).scrollTop() >= 20) {
            $("#audioId").prop('hidden', false);
            smallPlayer.css({
                "display": "block"
            });
        } else {
            $("#audioId").prop('hidden', true);
            smallPlayer.css({
                "display": "none"
            });
        }
    });

    inputSearchTrack.on("keyup", function () {
        var inputTitle = $(this).val().toLowerCase();
        $("#mainTableTBody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(inputTitle) > -1);
        });
    });

    $(".titleOfTrackInTable").on('click', function () {
        scrollPositionOnTitle = $(document).scrollTop();
        desktopPlayer.css({"display": "flex"});
        desktopPlayer.show();
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

    // var cookie = document.cookie;
    // if (cookie.includes("fav")) {
    //     $("#favouriteTracksButton").css({"display": "block"});
    // } else {
    //     $("#favouriteTracksButton").css({"display": "none"});
    // }
    //
    // if (!cookie.includes("path")) {
    //     $("#idPath").show();
    // }

    $("#submitFormPath").on('click', function () {
        $("#submitPathToFolderButton").show();
        $("#clearWrotePath").show();
    });
});