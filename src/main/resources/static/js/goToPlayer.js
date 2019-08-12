var counter = 0;
$(document).ready(function () {
    var player = $("#player");
    var listOfTrack = $("#listOfTrack");
    var backToListButton = $("#backToListButton");
    $(".titleOfTrackInTable").on('click', function () {
        listOfTrack.hide();
        backToListButton.show();
        player.css({"display": "flex"});
        player.show();
    });
    $("#backToList").on('click', function () {
        listOfTrack.show();
        backToListButton.hide();
        $('html, body').animate({
            scrollTop: 900
        }, 700);
    });
    $(".showAdditionalTable").on('click', function () {
        counter++;
        if (counter % 2 !== 0) {
            if ($(window).width() > 600) {
                $("#additionalTable").css('display', 'block');
            }
        } else {
            $("#additionalTable").css('display', 'none');
        }
    });
});