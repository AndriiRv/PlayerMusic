$(document).ready(function () {
    $(".titleOfTrackInTable").on('click', function () {
        var player = $("#player");
        $("#listOfTrack").hide();
        $("#backToListButton").show();
        player.css({"display": "flex"});
        player.show();
    });
    $("#backToList").on('click', function () {
        $("#listOfTrack").show();
        $("#backToListButton").hide();
        $('html, body').animate({
            scrollTop: 900
        }, 700);
    });
});