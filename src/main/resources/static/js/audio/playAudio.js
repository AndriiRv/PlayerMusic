var titleOfTrackInTable = $(".titleOfTrackInTable");
var audio = $("#audioId");
var durationSelector = $("#duration");
var currentTimeSelector = $("#currentTime");
var download = $("#download");
var hrefTitleForDownload;

// $("#searchTrack").keyup(function () {
//     var trackTitle = $(this).val();
//     $("#listOfTrack").load("/search?trackTitle=" + trackTitle);
//     url = "/search?trackTitle=" + trackTitle;
// });

titleOfTrackInTable.on('click', function () {
    playButton.hide();
    pauseButton.show();
    titleOfTrackInTable.css({
        "background": "none",
        "color": "black"
    });
    var nameOfTrack = $(this).text();
    $("#titleOfTab").html(nameOfTrack);
    var currentTrack = $('#titleOfTrackInPlayer');
    currentTrack.empty();
    audio.attr("src", "play/" + nameOfTrack);
    console.log("Manual select: " + nameOfTrack);
    titleOfTrackInTable.filter(function () {
        return $(this).text() === nameOfTrack;
    }).css({
        "background": "-webkit-gradient(linear, left top, right top, from(#007fd1), to(#c600ff))",
        "color": "white"
    });
    hrefTitleForDownload = nameOfTrack;
    currentTrack.append(nameOfTrack);
    audio.get(0).onloadedmetadata = function () {
        var duration = audio.get(0).duration;
        getTime(duration, durationSelector);
    };
    setInterval(function () {
        var currentTime = audio.get(0).currentTime;
        getTime(currentTime, currentTimeSelector);
        $("#barPlay").width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
    }, 10);
});

$("#barAllPlayed").on('click', function (e) {
    var allWidth = $("#barAllPlayed").width();
    var currentXByDiv = e.pageX - $("#barAllPlayed").offset().left;
    audio.get(0).currentTime = (currentXByDiv * audio.get(0).duration) / allWidth;
    $("#barPlay").width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
});

// var isDragging = false;
//
// $("#barAllPlayed").on('touchmove mousemove', function (e) {
//     if (isDragging) {
//         var allWidth = $("#barAllPlayed").width();
//         var currentXByDiv = e.pageX - $("#barAllPlayed").offset().left;
//         console.log(allWidth);
//         console.log(currentXByDiv);
//         var vid = document.getElementById("myVideo");
//         audio.get(0).currentTime = currentXByDiv * audio.get(0).duration / allWidth;
//         // audio.get(0).currentTime = currentXByDiv * audio.get(0).duration / allWidth;
//         $("#barPlay").width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
//     }
// });
//
// $('#barAllPlayed').on('touchstart mousedown', function () {
//     isDragging = true;
// });
// $('#barAllPlayed').on('touchend mouseup', function () {
//     isDragging = false;
// });

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