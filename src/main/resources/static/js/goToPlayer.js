$(document).ready(function () {
    $(".titleOfTrackInTable").on('click', function () {
        var player = $(".wrapper");
        $(".list").hide();
        $("#backToList").show();
        player.css({"display": "flex"});
        player.show();
    });
    $("#backToList").on('click', function () {
        $(".list").show();
        $("#backToList").hide();
    });
});