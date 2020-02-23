$.get({
    url: '/searchPlaceholder',
    success: function (fullTitleTrack) {
        $("#inputSearchTrack").attr("placeholder", "Find, example: " + fullTitleTrack);
        console.log(fullTitleTrack + ", put in search placeholder");
    }
});
