var scrollPositionOnTitle;

$(document).ready(function () {
    var player = $("#player");
    var backToListCounter = 0;
    var counterToScrollToPlayer = 0;

    $("#searchTrack").on("keyup", function () {
        var inputTitle = $(this).val().toLowerCase();
        $("#mainTableTBody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(inputTitle) > -1);
        });
    });

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

    var cookie = document.cookie;
    if (cookie.includes("fav")) {
        $("#favouriteTracksButton").css({"display": "block"});
    } else {
        $("#favouriteTracksButton").css({"display": "none"});
    }

    if (!cookie.includes("path")) {
        $("#idPath").show();
    }

    $("#submitFormPath").on('click', function () {
        $("#submitPathToFolderButton").show();
        $("#clearWrotePath").show();
        $("#idPath").show();
    });
});