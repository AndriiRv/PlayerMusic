var titleOfTrack = null;
var tableTr = $('table tr');
var rowCount = tableTr.length - 2;
var indexIteration = 0;

$("#playButton").on('click', function () {
    audio.get(0).play();
    $("#playButton").hide();
    $("#pauseButton").show();
});

$("#pauseButton").on('click', function () {
    audio.get(0).pause();
    $("#pauseButton").hide();
    $("#playButton").show();
});

tableTr.on('click', function () {
    titleOfTrack = null;
    $("#nextTrackButton").show();
    $("#playButton").hide();
    $("#pauseButton").show();
    indexIteration = $(this).index();
});
$(".audioClass").bind("ended", function () {
    $(".titleOfTrackInTable").css('color', 'black');
    if (indexIteration >= 0) {
        $("#prevTrackButton").show();
    }
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    titleOfTrack = $(".titleOfTrackInTable").eq(indexIteration);

    $('.titleOfTrackInTable').filter(function () {
        return $(this).text() === titleOfTrack.text();
    }).css('color', 'red');

    $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
    currentTrack.append(titleOfTrack.text());
    indexIteration++;
    if (indexIteration >= rowCount) {
        $("#nextTrackButton").hide();
    }
});

function nextPrevTrackButtons() {
    $(".titleOfTrackInTable").css('color', 'black');
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    titleOfTrack = $(".titleOfTrackInTable").eq(indexIteration);
    $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
    currentTrack.append(titleOfTrack.text());
    if (indexIteration >= rowCount - 1) {
        $("#nextTrackButton").hide();
    }
    $('.titleOfTrackInTable').filter(function () {
        return $(this).text() === titleOfTrack.text();
    }).css('color', 'red');
}

$("#nextTrackButton").on("click", function () {
    $("#playButton").hide();
    $("#pauseButton").show();
    if (indexIteration >= 0) {
        $("#prevTrackButton").show();
    }
    nextPrevTrackButtons(indexIteration);
    indexIteration++;
});

$("#prevTrackButton").on("click", function () {
    $("#playButton").hide();
    $("#pauseButton").show();
    $("#nextTrackButton").show();
    if (indexIteration <= 1) {
        $("#prevTrackButton").hide();
    }
    indexIteration = indexIteration - 2;
    nextPrevTrackButtons(indexIteration);
    indexIteration++;
});

$("#download").on('click', function () {
    window.location.href = 'playInBrowser/' + titleOfTrack.text();
});