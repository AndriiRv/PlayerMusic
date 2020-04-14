let listOfTrack = null;
let countOfTrack = 0;

let defaultList = null;

let countInPage = 20;

let lastList;

$(document).ready(function () {
    let defaultList = "/sort/title=ASC";
    getList(defaultList);
    getRandomTrackInSearchPlaceholder();
    lastList = defaultList;
    countInPage = 20;
});

$(".shuffleButton").on("click", function () {
    setTitleToNameOfTab("Shuffle tracks");
    let isShuffle = true;
    let urlShuffle = "/shuffle/" + isShuffle;
    getList(urlShuffle);
    getRandomTrackInSearchPlaceholder();
    lastList = "/shuffle/" + (!isShuffle);
    countInPage = 20;
});

function sort(url) {
    openListOfTrack();

    getList(url);
    getRandomTrackInSearchPlaceholder();
    lastList = url;
    countInPage = 20;
}

function getList(url) {
    getLoader();

    listOfTrack = null;

    url = url + '?page=' + countInPage;

    let html = '';
    $.getJSON(url, function (data) {
        if (data.length !== 0) {
            defaultList = data;
            listOfTrack = data;
            countOfTrack = data.length;

            $.each(data, function (i, track) {
                html += '<tr>';
                html += '<td class="commonTd" style="display: flex">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '   <div title="Count of played from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div style="align-self: flex-end;">' + getCountOfPlayedByMusicId(track.id) + '</div>';
                html +=     '</div>';
                html += '   <div title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div style="align-self: flex-end;">' + getCountOfFavouriteByMusicId(track.id) + '</div>';
                html +=     '</div>';
                html += '</td>';
                html += '</tr>';
            });
            $('#mainTableTBody').html(html);
            $('#listOfTrack').addClass("main");
            closeLoader();
        }
    });
}

$("#listOfTrack").scroll(function () {
    if ($("#listOfTrack").hasClass("main")) {
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
            countInPage = countInPage + 50;
            getList(lastList);
        }
    }
});