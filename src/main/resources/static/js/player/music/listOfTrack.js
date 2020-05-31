let listOfTrack = null;
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

function getListFromUrl(url) {
    let list = null;
    $.get({
        url: url,
        async: false,
        success: function (data) {
            list = data;
        }
    });
    return list;
}

function filter(url) {
    openListOfTrack();

    getList(url);
    lastUrl = url;
    countInPage = 20;
}

function getList(url) {
    mainTableTBody.empty();
    getLoader();

    let html = '';
    let data = getListFromUrl(url + '?page=' + countInPage);
    if (data.length !== 0) {
        listOfTrack = data;
        countOfTrack = data.length;

        $.each(data, function (i, track) {
            html += '<tr>';
            html += '<td class="commonTd" style="display: flex">';
            html += '   <div id="musicId" hidden>' + track.id + '</div>';
            html += '   <img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
            html += '   <div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
            html += '   <div title="Count of played from all users" style="align-self: flex-end; display: flex;">';
            html += '       <div style="background-image: url(../../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
            html += '       <div style="align-self: flex-end;">' + track.countOfPlayed + '</div>';
            html += '</div>';
            html += '   <div title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
            html += '       <div style="background-image: url(../../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
            html += '       <div style="align-self: flex-end;">' + track.countOfFavourite + '</div>';
            html += '</div>';
            html += '</td>';
            html += '</tr>';
        });
        mainTableTBody.html(html);
        getRandomTrackInSearchPlaceholder();
        closeLoader();
    }
}

listOfTrackDiv.scroll(function () {
    if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
        countInPage = countInPage + 50;
        getList(lastUrl);
    }
});