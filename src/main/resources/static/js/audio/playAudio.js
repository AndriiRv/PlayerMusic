var audio = $("#audioId");
var durationSelector = $("#duration");
var currentTimeSelector = $("#currentTime");
var titleToHref = null;
$(".titleOfTrackInTable").on('click', function () {
    $(".titleOfTrackInTable").css('color', 'black');
    var nameOfTrack = $(this).text();
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    audio.attr("src", "playInBrowser/" + nameOfTrack);
    $('.titleOfTrackInTable').filter(function () {
        return $(this).text() === nameOfTrack;
    }).css('color', 'red');
    titleToHref = nameOfTrack;
    currentTrack.append(nameOfTrack);
    audio.get(0).onloadedmetadata = function () {
        var duration = audio.get(0).duration;
        getTime(duration, durationSelector);
    };
    setInterval(function () {
        var currentTime = audio.get(0).currentTime;
        getTime(currentTime, currentTimeSelector);
    }, 10);
});

function getTime(time, selector) {
    var hr = Math.floor(time / 3600).toString();
    var min = Math.floor((time - (hr * 3600)) / 60).toString();
    var sec = Math.floor(time - (hr * 3600) - (min * 60)).toString();
    if (min.length === 1) {
        min = "0" + min;
    }
    if (sec.length === 1) {
        sec = "0" + sec;
    }
    if (hr.length === 1) {
        hr = "0" + hr;
    }
    selector.html(hr + ":" + min + ':' + sec);
}

$("#download").on('click', function () {
    window.location.href = 'playInBrowser/' + titleToHref;
});