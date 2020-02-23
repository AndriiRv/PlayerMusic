let scrollPositionOnTitle;
let inputSearchTrack = $("#inputSearchTrack");

$(document).ready(function () {
    let desktopPlayer = $("#player");
    let backToListCounter = 0;
    let counterToScrollToPlayer = 0;
    let counterShowInfoAboutTrack = 0;

    $(".exitFromDiv").on('click', function(){
        $(this).parent().parent().css('display', 'none');
    });

    inputSearchTrack.on('search', function () {
        let inputTitle = $(this).val().toLowerCase();
        $("#mainTableTBody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(inputTitle) > -1);
        });
    });

    $("#showInfoAboutTrack").on('click', function () {
        counterShowInfoAboutTrack++;
        if (counterShowInfoAboutTrack % 2 !== 0) {
            scrollPositionOnTitle = $(document).scrollTop();
            desktopPlayer.css({"display": "flex"});
            desktopPlayer.show();
            counterToScrollToPlayer++;
            // if (counterToScrollToPlayer === 1) {
            $('html, body').animate({
                scrollTop: 1
            }, 700);
            // }
        } else {
            desktopPlayer.css({"display": "none"});
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

    $("#submitFormPath").on('click', function () {
        $("#submitPathToFolderButton").show();
        $("#clearWrotePath").show();
    });
});
