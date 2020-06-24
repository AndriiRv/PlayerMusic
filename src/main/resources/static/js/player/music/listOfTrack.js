let ListOfTrack = function () {
    this.listOfTrack = null;
};

ListOfTrack.prototype.getListOfTrack = function () {
    return this.listOfTrack;
};

ListOfTrack.prototype.setListOfTrack = function (listOfTrack) {
    this.listOfTrack = listOfTrack;
};

let listOfTrackObj = new ListOfTrack();

let countOfTrack = 0;
let countInPage = 20;
let lastUrl;
let listOfTrackDiv = $('#listOfTrack');
let mainTableTBody = $('#mainTableTBody');

function getDefaultList() {
    let defaultUrl = "/sort/title=ASC";
    getList(defaultUrl);
    lastUrl = defaultUrl;
    countInPage = 20;
}

$(".shuffleButton").on("click", function () {
    setTitleToNameOfTab("Shuffle tracks");
    let urlShuffle = "/shuffle";
    getList(urlShuffle);
    lastUrl = "/shuffle";
    countInPage = 20;
});

function sort(url) {
    openListOfTrack();

    getList(url);
    lastUrl = url;
    countInPage = 20;
}

function filter(url) {
    openListOfTrack();

    getList(url);
    lastUrl = url;
    countInPage = 20;
}

let isCurrentListOfTrackHide = true;
$("#showInfoAboutTrack").on("click", function () {
    if (isCurrentListOfTrackHide) {
        showCurrentListOfTrack();
        $("#showInfoAboutTrack").css({
            "cursor": "pointer",
            "text-decoration": "underline"
        });
        isCurrentListOfTrackHide = false;
    } else {
        $("#currentListOfTrack").hide();
        $("#showInfoAboutTrack").css({
            "cursor": "pointer",
            "text-decoration": "none"
        });
        isCurrentListOfTrackHide = true;
    }
});

function showCurrentListOfTrack() {
    $("#currentListOfTrack").show();
    let html = '';
    $.each(listOfTrackObj.getListOfTrack(), function (i, track) {
        html += '<div id="musicId" hidden>' + track.id + '</div>';
        html += '<div class="titleOfTrackInTable" style="height: auto">' + track.fullTitle + '</div>'
    });
    $("#currentListOfTrack").html(html);
}

let firstLoadListOfTrack = false;

function getList(url) {
    mainTableTBody.empty();
    getLoader();

    let html = '';
    $.getJSON(url + '?page=' + countInPage, function (data) {
        if (data.length !== 0) {
            listOfTrackObj.setListOfTrack(data);
            countOfTrack = data.length;

            $.each(data, function (i, track) {
                html += '<tr>';
                html += '<td class="commonTd" style="display: flex">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                if (track.byteOfPicture !== null) {
                    html += '<img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                } else {
                    html += '<img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                }
                html += '   <div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '   <div id="played" title="Count of played from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div id="countOfPlayedByMusic" style="align-self: flex-end;">' + getCountOfPlayedByMusicId(track.id) + '</div>';
                html += '   </div>';
                html += '   <div id="allFavourite" title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div id="countOfFavourite" style="align-self: flex-end;">' + getCountOfFavouriteByMusicId(track.id) + '</div>';
                html += '</div>';
                html += '</td>';
                html += '</tr>';
            });
            mainTableTBody.html(html);
            getRandomTrackInSearchPlaceholder();
            closeLoader();
            if (firstLoadListOfTrack) {
                listOfTrackDiv.scrollTop(listOfTrackDiv[0].scrollHeight - 1500);
            }
            firstLoadListOfTrack = true;
        }
    });
}

listOfTrackDiv.scroll(function () {
    if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
        countInPage = countInPage + 13;
        getList(lastUrl);
    }
});