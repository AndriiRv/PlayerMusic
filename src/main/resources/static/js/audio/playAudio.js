var titleOfTrackInTable = $(".titleOfTrackInTable");
var audio = $("#audioId");
var durationSelector = $("#duration");
var currentTimeSelector = $("#currentTime");
var download = $("#download");
var hrefTitleForDownload;
var barAllPlayed = $("#barAllPlayed");
var barPlay = $("#barPlay");

titleOfTrackInTable.on('click', function () {
    playButton.hide();
    pauseButton.show();
    titleOfTrackInTable.css({
        "background": "none",
        "color": "white"
    });
    var nameOfTrack = $(this).text();

    for (var i = 0; i <= String(nameOfTrack).length; i++) {
        if (nameOfTrack.includes('[')) {
            nameOfTrack = nameOfTrack.replace('[', '%5B').replace(']', '%5D');
        }
    }

    var currentTrack = $('#titleOfTrackInPlayer');
    currentTrack.empty();
    audio.attr("src", "play/" + nameOfTrack);

    for (var j = 0; j <= String(nameOfTrack).length; j++) {
        if (nameOfTrack.includes('%5B')) {
            nameOfTrack = nameOfTrack.replace('%5B', '[').replace('%5D', ']');
        }
    }

    titleOfTrackInTable.filter(function () {
        return $(this).text() === nameOfTrack;
    }).css({
        "background": "linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(6,151,244,1) 0%, rgba(64,26,186,1) 49%, rgba(172,22,224,1) 86%)",
        "color": "white"
    });

    hrefTitleForDownload = nameOfTrack;
    nameOfTrack = nameOfTrack.replace(".mp3", "");

    $("#titleOfTab").html(nameOfTrack);

    console.log("Manual select: " + nameOfTrack);
    currentTrack.append(nameOfTrack);
    audio.get(0).onloadedmetadata = function () {
        var duration = audio.get(0).duration;
        getTime(duration, durationSelector);
    };
    setInterval(function () {
        var currentTime = audio.get(0).currentTime;
        getTime(currentTime, currentTimeSelector);
        barPlay.width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
    }, 10);
});

barAllPlayed.on('click', function (e) {
    var allWidth = barAllPlayed.width();
    var currentXByDiv = e.pageX - barAllPlayed.offset().left;
    audio.get(0).currentTime = (currentXByDiv * audio.get(0).duration) / allWidth;
    barPlay.width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
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

download.on('click', function () {
    window.location.href = 'download/' + hrefTitleForDownload;
    console.log("Download: " + hrefTitleForDownload);
});