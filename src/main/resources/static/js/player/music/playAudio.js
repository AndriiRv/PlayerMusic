let titleOfTrackInPlayer = $('.titleOfTrackInPlayer');
let numberOfTrack = null;

let audio = $("#audioId");

let durationSelector = $(".duration");
let currentTimeSelector = $(".currentTime");

let hrefTitleForDownload;

let barAllPlayed = $(".barAllPlayed");
let barPlay = $(".barPlay");

let intervalVolumeBoost = null;

// let artistTrack = null;
// let songTrack = null;
// let fullTitle = null;

// function getSingerAndTitle(fullTitle) {
//     let indexDash = fullTitle.indexOf(" - ");
//    let indexAfterTitle = fullTitle.indexOf(".mp3");
//
//     if (indexDash !== -1) {
//         artistTrack = fullTitle.substring(0, indexDash);
//         songTrack = fullTitle.substring(indexDash + 3, indexAfterTitle);
//     }
// }

function highlightSelectTrack(titleOfTrackInTable, nameOfTrack) {
    titleOfTrackInTable.filter(function () {
        return $(this).text() === nameOfTrack;
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

function setTrackToHistoryToUser(nameOfTrack) {
    let currentUserName = $("#currentUserName").text();
    if (currentUserName) {
        addTrackToHistory(nameOfTrack);
    }
}

function setTitleToNameOfTab(newNameOfTab) {
    $("#titleOfTab").html(newNameOfTab + " | Music Player");
}

$("body").on("click", ".titleOfTrackInTable", function () {
    let nameOfTrack = $(this).text();

    let musicTrack = new Track(nameOfTrack);
    // musicTrack.playMusicTrack();

    let lyric = new Lyric();
    lyric.setSinger(musicTrack.getSinger());
    lyric.setTitle(musicTrack.getTitle());
    Lyric.prototype.newInstance(lyric);

    // let lyricObject = new Lyric();
    // lyricObject.openLyric(lyricObject);

    $("#bottomController").css({
        "display": "flex"
    });

    let titleOfTrackInTable = $(".titleOfTrackInTable");

    // volumeBoost();

    playButton.hide();
    pauseButton.show();

    clearSelectTrack(titleOfTrackInTable);

    // let nameOfTrack = $(this).text();
    fullTitle = nameOfTrack;

    numberOfTrack = $(this).siblings("#musicId").text();
    numberOfTrack = numberOfTrack.replace(fullTitle, '');

    addCountOfPlayedByMusicId(numberOfTrack);

    let srcCoverTrack = $(this).prevAll('img').first().attr("src");
    getPicture(srcCoverTrack);

    for (let i = 0; i <= String(nameOfTrack).length; i++) {
        if (nameOfTrack.includes('[')) {
            nameOfTrack = nameOfTrack.replace('[', '%5B').replace(']', '%5D');
        }
        if (nameOfTrack.includes('#')) {
            nameOfTrack = nameOfTrack.replace('#', '%23');
        }
    }
    if (!currentUserName.text().includes("guest")) {
        setTrackToHistoryToUser(nameOfTrack);
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

    highlightSelectTrack(titleOfTrackInTable, nameOfTrack);

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
