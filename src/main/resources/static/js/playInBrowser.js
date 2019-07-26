$(document).ready(function () {
    $("span").click(function () {
        var currentTrack = $('#titleOfTrack');
        currentTrack.empty();
        var title = $(this).text();
        $("#audioId").attr("src", "playInBrowser/" + title);
        currentTrack.append(title);
    });
});