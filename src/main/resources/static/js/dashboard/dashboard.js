let checkDashboard = true;
let dashboardAlreadyOpen = 0;
let dashboardDiv = $("#dashboard");
let supportDashboard = $("#support");
let dashboardCenter = $("#dashboardCenter");
let dashboardSide = $("#dashboardSide");

let countInPageByCountOfPlayed = 10;
let countInPageByCountOfFavourite = 10;

$(document).ready(function () {
    turnDashboard();
    if ($("#usernameThymelyeaf").text() !== "") {
        currentUserUsername.show();
        signButton.hide();
        peopleButton.show();
        chatButton.show();
    }
});

function turnDashboard() {
    if (checkDashboard) {
        currentUserUsername.text($("#usernameThymelyeaf").text());
        $("#listOfTrack").hide();
        $('#dashboard').show();
        getDashboard();
        checkDashboard = false;
        dashboardAlreadyOpen++;
    } else {
        getDefaultList();
        $('#dashboard').hide();
        $("#listOfTrack").show();
        showFilterOption();
        checkDashboard = true;
    }
}

function hideDashboardDivs() {
    showDashboard();
    dashboardCenter.hide();
    dashboardSide.hide();
}

function showDashboardDivs() {
    showDashboard();
    dashboardCenter.show();
    dashboardSide.show();
}

function showDashboard() {
    dashboardDiv.css("display", "flex");
}

function hideDashboard() {
    dashboardDiv.css("display", "none");
}

function getDashboard() {
    showDashboard();
    showDashboardDivs();
    openDashboard();

    getBy("countOfPlayed", "Top by count of played", "/dashboard/countOfPlayed");
    getBy("countOfFavourite", "Top by count of favourite", "/dashboard/countOfFavourite");
    getMusicByGenre();

    if (currentUserUsername.text() !== "") {
        getDashboardSideFavouriteAndHistory();
        getListOnDashboard();
    } else {
        getListOnDashboard();
    }
}

function getBy(idTitle, title, url) {
    getLoader();

    url = url + '?page=' + countInPage;

    listOfTrack = getListFromUrl(url);

    let html = '';
    $.getJSON(url, function (listOfTrack) {
        html += '<h2>' + title + '</h2>';
        html += '<div style="display: flex; overflow-x: auto;">';
        $.each(listOfTrack, function (i, track) {
            html += '<div style="min-width: 173px; margin-right: 1%;">';
            html += '    <div>';
            html += '        <div id="musicId" hidden>' + track.id + '</div>';
            html += '        <img id="cover" style="width: 173px; height: 173px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
            html += '        <div class="titleOfTrackInTable" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">' + track.fullTitle + '</div>';
            html += '    </div>';
            html += '</div>'
        });
        html += '</div>';
        $('#' + idTitle + '').html(html);
        getRandomTrackInSearchPlaceholder();
        closeLoader();
    });
}