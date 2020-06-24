let titleOfTrackInPlayer = $('.titleOfTrackInPlayer');
let numberOfTrack = null;

let audio = $("#audioId");

let durationSelector = $(".duration");
let currentTimeSelector = $(".currentTime");

let hrefTitleForDownload;

let barAllPlayed = $(".barAllPlayed");
let barPlay = $(".barPlay");

let intervalVolumeBoost = null;

let musicTrackId = 0;

let musicTrackObject;

let lyricObj = new Lyric();

let currentMusicTrackInListOfTrack;

function highlightByElement(element, title) {
    element.filter(function () {
        element.css("background", "transparent");
        return $(this).text() === title;
    }).css({
        "background": "linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(6,151,244,1) 0%, rgba(64,26,186,1) 49%, rgba(172,22,224,1) 86%)",
        "color": "white"
    });
}

function clearSelectTrack(titleOfTrackInTable) {
    titleOfTrackInTable.css({
        "background": "none",
        "color": "white"
    });
}

function setTitleToNameOfTab(newNameOfTab) {
    $("#titleOfTab").html(newNameOfTab + " | Music Player");
}

function getLyricByTrack(musicTrack) {
    lyricObj.setSinger(musicTrack.getSinger());
    lyricObj.setTitle(musicTrack.getTitle());
}

function getCounter(that, parentElement, childElement) {
    let playedDiv = $(that).siblings(parentElement);
    return playedDiv.find(childElement);
}

$("body").on("click", ".titleOfTrackInTable", function () {
    currentMusicTrackInListOfTrack = $(this);
    let nameOfTrack = $(this).text();
    let trackId = $(this).siblings("#musicId").text();

    musicTrackObject = new Track(nameOfTrack);
    musicTrackObject.setId(trackId);

    musicTrackId = musicTrackObject.getId();

    getLyricByTrack(musicTrackObject);

    $("#bottomController").css({
        "display": "flex"
    });

    let titleOfTrackInTable = $(".titleOfTrackInTable");

    // volumeBoost();

    playButton.hide();
    pauseButton.show();

    clearSelectTrack(titleOfTrackInTable);

    numberOfTrack = $(this).siblings("#musicId").text();

    addCountOfPlayedByMusicId(numberOfTrack);
    getCounter(currentMusicTrackInListOfTrack, "#played", "#countOfPlayedByMusic")
        .text(getCountOfPlayedByMusicId(musicTrackId));

    getPictureByTrackId(trackId);

    for (let i = 0; i <= String(nameOfTrack).length; i++) {
        if (nameOfTrack.includes('[')) {
            nameOfTrack = nameOfTrack.replace('[', '%5B').replace(']', '%5D');
        }
        if (nameOfTrack.includes('#')) {
            nameOfTrack = nameOfTrack.replace('#', '%23');
        }
    }
    if (!currentUserName.text().includes("guest")) {
        addTrackToHistory(nameOfTrack);
        changeFavouritePic();
    }

    titleOfTrackInPlayer.empty();
    audio.attr("src", "play/" + nameOfTrack);

    for (let j = 0; j <= String(nameOfTrack).length; j++) {
        if (nameOfTrack.includes('%5B')) {
            nameOfTrack = nameOfTrack.replace('%5B', '[').replace('%5D', ']');
        }
        if (nameOfTrack.includes('%23')) {
            nameOfTrack = nameOfTrack.replace('%23', '#');
        }
    }

    highlightByElement(titleOfTrackInTable, nameOfTrack);

    hrefTitleForDownload = nameOfTrack;
    nameOfTrack = nameOfTrack.replace(".mp3", "");

    setTitleToNameOfTab(nameOfTrack);

    console.log("Manual select: " + nameOfTrack);

    titleOfTrackInPlayer.append(nameOfTrack);

    audio.get(0).onloadedmetadata = function () {
        let duration = audio.get(0).duration;
        getTime(duration, durationSelector);
    };
    setInterval(function () {
        let currentTime = audio.get(0).currentTime;
        getTime(currentTime, currentTimeSelector);
        barPlay.width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
    }, 10);
});

barAllPlayed.on('click', function (e) {
    let allWidth = barAllPlayed.width();
    let currentXByDiv = e.pageX - barAllPlayed.offset().left;
    audio.get(0).currentTime = (currentXByDiv * audio.get(0).duration) / allWidth;
    barPlay.width((audio.get(0).currentTime / audio.get(0).duration) * 100 + '%');
});

barAllPlayed.hover(function () {
        let div = '';
        div += '<div id="hoverOnBarPlay" style="height:100%; width: 5px; background-color: black; float: right; border-style: double;"></div>';
        barPlay.append(div)
    },
    function () {
        $("#hoverOnBarPlay").remove();
    }
);

function getTime(time, selector) {
    let hr = Math.floor(time / 3600).toString();
    let min = Math.floor((time - (hr * 3600)) / 60).toString();
    let sec = Math.floor(time - (hr * 3600) - (min * 60)).toString();
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