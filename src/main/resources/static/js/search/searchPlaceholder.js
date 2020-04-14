function getRandomTrackInSearchPlaceholder() {
    $.get({
        url: '/searchPlaceholder',
        success: function (fullTitleTrack) {
            $("#inputSearchTrack").attr("placeholder", "Find, example: " + fullTitleTrack);
        }
    });
}