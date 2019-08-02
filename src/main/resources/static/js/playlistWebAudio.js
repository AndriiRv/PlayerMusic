var titleOfTrack = null;
var tableTr = $('table tr');
var rowCount = tableTr.length - 1;
var indexIteration = 0;
tableTr.on('click', function () {
    $("#playButton").hide();
    $("#pauseButton").show();
    $(".nextPrevButtons").show();
    indexIteration = $(this).index();
    $(".audioClass").bind("ended", function () {
        if (indexIteration >= 0) {
            $("#prevTrackButton").show();
        }
        var currentTrack = $('#titleOfTrack');
        currentTrack.empty();
        indexIteration++;
        titleOfTrack = $(".titleOfTrackInTable").eq(indexIteration);
        $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
        currentTrack.append(titleOfTrack.text());
        if (indexIteration >= rowCount - 1) {
            $("#nextTrackButton").hide();
        }
    });
});

function nextPrevTrackButtons() {
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    titleOfTrack = $(".titleOfTrackInTable").eq(indexIteration);
    $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
    currentTrack.append(titleOfTrack.text());
    if (indexIteration >= rowCount - 1) {
        $("#nextTrackButton").hide();
    }
}

$("#nextTrackButton").on("click", function () {
    $("#playButton").hide();
    $("#pauseButton").show();
    if (indexIteration >= 0) {
        $("#prevTrackButton").show();
    }
    indexIteration++;
    nextPrevTrackButtons();
});

$("#prevTrackButton").on("click", function () {
    $("#playButton").hide();
    $("#pauseButton").show();
    $("#nextTrackButton").show();
    if (indexIteration <= 1) {
        $("#prevTrackButton").hide();
    }
    indexIteration--;
    nextPrevTrackButtons();
});

$("#download").on('click', function () {
    window.location.href = 'playInBrowser/' + titleOfTrack.text();
});