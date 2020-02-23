let playButton = $(".playButton");
let pauseButton = $(".pauseButton");

let nextTrackButton = $(".nextTrackButton");
let prevTrackButton = $(".prevTrackButton");

let loopButton = $(".loopButton");

let indexIteration = 0;
let counterForLoop = 0;
let counterForTrackIsPlay = 0;

let intervalTransparentColorCurrentTime = null;
let intervalCommonColorCurrentTime = null;

let countOfIterationByListOfTrack = 0;

nextTrackButton.prop('disabled', false);
prevTrackButton.prop('disabled', false);

playButton.on('click', function () {
    audio.get(0).play();
    $("#icon").attr("href", "/images/iconPlay.ico");
    playButton.hide();
    pauseButton.show();
});

pauseButton.on('click', function () {
    counterForTrackIsPlay++;
    audio.get(0).pause();
    $("#icon").attr("href", "/images/iconPause.ico");
    pauseButton.hide();
    playButton.show();
    // hideCurrentTimeTrack();
});

$(document).on("click", 'table tbody tr', function () {
    countOfIterationByListOfTrack = 0;
    indexIteration = $(this).index();
    countOfIterationByListOfTrack = countOfIterationByListOfTrack + indexIteration;

    // disableEnableNextButton(countOfIterationByListOfTrack);
    // disableEnablePrevButton(countOfIterationByListOfTrack);
});

function playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrack, process) {
    titleOfTrackInPlayer.empty();
    let titleOfTrackInTable = $(".titleOfTrackInTable");

    clearSelectTrack(titleOfTrackInTable);

    let nextTrackTitle = null;

    if (listOfTrack[countOfIterationByListOfTrack].fullTitle || listOfTrack[countOfIterationByListOfTrack].fullTitle !== undefined) {
        nextTrackTitle = listOfTrack[countOfIterationByListOfTrack].fullTitle;
    } else {
        nextTrackTitle = listOfTrack[countOfIterationByListOfTrack];
    }

    showSelectTrack(titleOfTrackInTable, nextTrackTitle);
    titleOfTrackInPlayer.append(nextTrackTitle.replace(".mp3", ""));

    console.log(process + nextTrackTitle);

    audio.attr("src", "play/" + nextTrackTitle);
}

$(".audioClass").bind("ended", function () {
    countOfIterationByListOfTrack++;

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrack, "After ended: ");
    // disableEnableNextButton(countOfIterationByListOfTrack);
});

nextTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();
    prevTrackButton.css("opacity", "1");
    prevTrackButton.prop('disabled', false);

    countOfIterationByListOfTrack++;

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrack, "Next: ");

    // disableEnableNextButton(countOfIterationByListOfTrack);
});

prevTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();
    nextTrackButton.css("opacity", "1");
    nextTrackButton.prop('disabled', false);

    countOfIterationByListOfTrack = countOfIterationByListOfTrack - Number("1");

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrack, "Prev: ");

    // disableEnablePrevButton(countOfIterationByListOfTrack);
});

// function disableEnableNextButton(countOfIterationByListOfTrack) {
//     if (countOfIterationByListOfTrack >= 1) {
//         prevTrackButton.css("opacity", "1");
//         prevTrackButton.prop('disabled', false);
//     }
//     if (countOfIterationByListOfTrack < (countOfTrack - 1)) {
//         nextTrackButton.css("opacity", "1");
//         nextTrackButton.prop('disabled', false);
//     } else if (countOfIterationByListOfTrack >= (countOfTrack - 1)) {
//         nextTrackButton.css("opacity", "0.5");
//         nextTrackButton.prop('disabled', true);
//     }
// }
//
// function disableEnablePrevButton(countOfIterationByListOfTrack) {
//     if (countOfIterationByListOfTrack <= 0) {
//         prevTrackButton.css("opacity", "0.5");
//         prevTrackButton.prop('disabled', true);
//     } else if (countOfIterationByListOfTrack >= 1) {
//         prevTrackButton.css("opacity", "1");
//         prevTrackButton.prop('disabled', false);
//     }
// }

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
