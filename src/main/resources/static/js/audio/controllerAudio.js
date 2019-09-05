var currentTitle;
var playButton = $("#playButton");
var pauseButton = $("#pauseButton");
var nextTrackButton = $("#nextTrackButton");
var prevTrackButton = $("#prevTrackButton");
var loopButton = $("#loopButton");
var tableTr = $('table tbody tr');
var rowCount = tableTr.length - 1;
var indexIteration = 0;
var counterForLoop = 0;

nextTrackButton.prop('disabled', false);
prevTrackButton.prop('disabled', false);

playButton.on('click', function () {
    audio.get(0).play();
    $("#icon").attr("href", "/images/iconPlay.ico");
    playButton.hide();
    pauseButton.show();
});

pauseButton.on('click', function () {
    audio.get(0).pause();
    $("#icon").attr("href", "/images/iconPause.ico");
    pauseButton.hide();
    playButton.show();
});

tableTr.on('click', function () {
    indexIteration = $(this).index();
    currentTitle = null;
    nextTrackButton.show();
    playButton.hide();
    pauseButton.show();
});

$(".audioClass").bind("ended", function () {
    titleOfTrackInTable.css({
        "background": "none",
        "color": "white"
    });

    showCurrentTrackAndPlayInPlayer();

    titleOfTrackInTable.filter(function () {
        return $(this).text() === currentTitle.text();
    }).css({
        "background": "linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(6,151,244,1) 0%, rgba(64,26,186,1) 49%, rgba(172,22,224,1) 86%)",
        "color": "white"
    });
    console.log("After ended: " + currentTitle.text());
    indexIteration++;

    if (indexIteration >= rowCount) {
        nextTrackButton.css("opacity", "0.5");
        nextTrackButton.prop('disabled', true);
    }
});

function showCurrentTrackAndPlayInPlayer() {
    var currentTrack = $('#titleOfTrackInPlayer');
    currentTrack.empty();
    currentTitle = titleOfTrackInTable.eq(indexIteration);

    if (indexIteration >= rowCount) {
        currentTitle = null;
    }

    audio.attr("src", "play/" + currentTitle.text());
    hrefTitleForDownload = currentTitle.text();
    currentTrack.append(currentTitle.text().replace(".mp3", ""));
    $("#titleOfTab").html(currentTitle.text().replace(".mp3", ""));
}

function nextPrevTrackButtons() {
    titleOfTrackInTable.css({
        "background": "none",
        "color": "white"
    });

    showCurrentTrackAndPlayInPlayer();

    if (indexIteration >= rowCount - 1) {
        nextTrackButton.css("opacity", "0.5");
        nextTrackButton.prop('disabled', true);
        playButton.css("opacity", "0.5");
        playButton.prop('disabled', true);
    }

    titleOfTrackInTable.filter(function () {
        return $(this).text() === currentTitle.text();
    }).css({
        "background": "linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(6,151,244,1) 0%, rgba(64,26,186,1) 49%, rgba(172,22,224,1) 86%)",
        "color": "white"
    });
}

nextTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();

    if (indexIteration >= 0) {
        prevTrackButton.css("opacity", "1");
        prevTrackButton.prop('disabled', false);
    }

    nextPrevTrackButtons();
    console.log("Next: " + currentTitle.text());
    indexIteration++;
});

prevTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();
    nextTrackButton.css("opacity", "1");
    nextTrackButton.prop('disabled', false);

    if (indexIteration <= 1) {
        prevTrackButton.css("opacity", "0.5");
        prevTrackButton.prop('disabled', true);
    }

    indexIteration = indexIteration - 2;
    nextPrevTrackButtons();
    console.log("Previous: " + currentTitle.text());
    indexIteration++;
});

loopButton.on("click", function () {
    counterForLoop++;

    if (counterForLoop % 2 !== 0) {
        audio.attr("loop", "-1");
        loopButton.css("opacity", "1");
        console.log("Loop track: " + hrefTitleForDownload);
    } else {
        audio.removeAttr("loop");
        loopButton.css("opacity", "0.5");
        console.log("Remove loop from: " + hrefTitleForDownload);
    }
});