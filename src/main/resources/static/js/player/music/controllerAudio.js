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

$("#listOfTrack").on("click", 'table tbody tr', function () {
    countOfIterationByListOfTrack = 0;
    indexIteration = $(this).index();
    countOfIterationByListOfTrack = countOfIterationByListOfTrack + indexIteration;

    disableEnableNextButton();
    disableEnablePrevButton();
});

function playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrack, process) {
    titleOfTrackInPlayer.empty();
    let titleOfTrackInTable = $(".titleOfTrackInTable");

    clearSelectTrack(titleOfTrackInTable);

    let nextTrackTitle;

    if (listOfTrack[countOfIterationByListOfTrack].fullTitle || listOfTrack[countOfIterationByListOfTrack].fullTitle !== undefined) {
        nextTrackTitle = listOfTrack[countOfIterationByListOfTrack].fullTitle;
    } else {
        nextTrackTitle = listOfTrack[countOfIterationByListOfTrack];
    }

    getPictureByTrackId(listOfTrack[countOfIterationByListOfTrack].id);

    if (!currentUserName.text().includes("guest")) {
        numberOfTrack = listOfTrack[countOfIterationByListOfTrack].id;
        changeFavouritePic();
    }

    highlightByElement(titleOfTrackInTable, nextTrackTitle);
    titleOfTrackInPlayer.append(nextTrackTitle.replace(".mp3", ""));

    setTitleToNameOfTab(nextTrackTitle);

    console.log(process + nextTrackTitle);

    musicTrackObject.setFullTitle(nextTrackTitle);

    musicTrackId = listOfTrack[countOfIterationByListOfTrack].id;

    let musicTrack = new Track(listOfTrack[countOfIterationByListOfTrack].fullTitle);
    getLyricByTrack(musicTrack);

    audio.attr("src", "play/" + nextTrackTitle);
}

$(".audioClass").bind("ended", function () {
    countOfIterationByListOfTrack++;

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrackObj.getListOfTrack(), "After ended: ");
    disableEnableNextButton();
    disableEnablePrevButton();
});

nextTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();
    prevTrackButton.css("opacity", "1");
    prevTrackButton.prop('disabled', false);

    countOfIterationByListOfTrack++;

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrackObj.getListOfTrack(), "Next: ");

    disableEnableNextButton();
});

prevTrackButton.on("click", function () {
    playButton.hide();
    pauseButton.show();
    nextTrackButton.css("opacity", "1");
    nextTrackButton.prop('disabled', false);

    countOfIterationByListOfTrack = countOfIterationByListOfTrack - Number("1");

    playMusic(titleOfTrackInPlayer, countOfIterationByListOfTrack, listOfTrackObj.getListOfTrack(), "Prev: ");

    disableEnablePrevButton();
});

function disableEnableNextButton() {
    if (countOfIterationByListOfTrack < (countOfTrack - 1)) {
        nextTrackButton.css("opacity", "1");
        nextTrackButton.prop('disabled', false);
    } else if (countOfIterationByListOfTrack >= (countOfTrack - 1)) {
        nextTrackButton.css("opacity", "0.5");
        nextTrackButton.prop('disabled', true);
    }
}

function disableEnablePrevButton() {
    if (countOfIterationByListOfTrack <= 0) {
        prevTrackButton.css("opacity", "0.5");
        prevTrackButton.prop('disabled', true);
    } else if (countOfIterationByListOfTrack >= 1) {
        prevTrackButton.css("opacity", "1");
        prevTrackButton.prop('disabled', false);
    }
}

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
