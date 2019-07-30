$(".titleOfTrackInTable").click(function () {
    var currentTrack = $('#titleOfTrack');
    var duration = $("#duration");
    var currentTime = $("#currentTime");
    var nameOfTrack = $(this).text();
    var audio = $("#audioId");
    audio.attr("src", "playInBrowser/" + nameOfTrack);
    currentTrack.append(nameOfTrack);
    $('#audioId').get(0).onloadedmetadata = function () {
        var length = $('#audioId').get(0).duration;
        var hr = Math.floor(length / 3600).toString();
        var min = Math.floor((length - (hr * 3600)) / 60).toString();
        var sec = Math.floor(length - (hr * 3600) - (min * 60)).toString();
        if (min.length === 1) {
            min = "0" + min;
        }
        if (sec.length === 1) {
            sec = "0" + sec;
        }
        if (hr.length === 1) {
            hr = "0" + hr;
        }
        duration.html(hr + ":" + min + ':' + sec);
    };
    setInterval(function () {
        var hr = Math.floor($('#audioId').get(0).currentTime / 3600).toString();
        var min = Math.floor(($('#audioId').get(0).currentTime - (hr * 3600)) / 60).toString();
        var sec = Math.floor($('#audioId').get(0).currentTime - (hr * 3600) - (min * 60)).toString();
        if (min.length === 1) {
            min = "0" + min;
        }
        if (sec.length === 1) {
            sec = "0" + sec;
        }
        if (hr.length === 1) {
            hr = "0" + hr;
        }
        currentTime.html(hr + ":" + min + ':' + sec);
    }, 10);
});

$("#play").click(function () {
    $('#audioId').get(0).play();
    $("#play").hide();
    $("#pause").show();
});

$("#pause").click(function () {
    $('#audioId').get(0).pause();
    $("#pause").hide();
    $("#play").show();
});

$("#volumeSlider").change(function(){
    $('#audioId').get(0).volume = $(this).val();
});