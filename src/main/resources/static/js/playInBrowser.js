var audio = $("#audioId");
var durationSelector = $("#duration");
var currentTimeSelector = $("#currentTime");
var titleToHref = null;
$(".titleOfTrackInTable").on('click', function () {
    var nameOfTrack = $(this).text();
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    audio.attr("src", "playInBrowser/" + nameOfTrack);
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

$("#volumeSlider").change(function () {
    audio.get(0).volume = $(this).val();
});

$("#download").on('click', function () {
    window.location.href = 'playInBrowser/' + titleToHref;
});

$("#volumeMin").on('click', function () {
    audio.get(0).muted = true;
    $("#volumeMin").hide();
    $("#volumeMute").show();
    $("#volumeMax").css("background-image", "url('../images/volumeMute.svg')");
});

$("#volumeMute").on('click', function () {
    audio.get(0).muted = false;
    $("#volumeMute").hide();
    $("#volumeMin").show();
    $("#volumeMax").css("background-image", "url('../images/volumeMax.svg')");
});