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
    if ($("#usernameThymeleaf").text() !== "") {
        currentUserUsername.show();
        signButton.hide();
        peopleButton.show();
        chatButton.show();
    }
});

function turnDashboard() {
    countInPage = 20;
    clearSearch();
    getRandomTrackInSearchPlaceholder();
    if (checkDashboard) {
        if ($("#usernameThymeleaf").text() !== "") {
            currentUserUsername.text($("#usernameThymeleaf").text());
        }
        $("#listOfTrack").hide();
        $('#dashboard').show();
        getDashboard();
        checkDashboard = false;
        dashboardAlreadyOpen++;
    } else {
        setTitleToNameOfTab("All list of music track");
        getDefaultList();
        $('#dashboard').hide();
        supportDashboard.empty();
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
    setTitleToNameOfTab("Explore!");
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

    let html = '';
    $.getJSON(url, function (data) {
        if (data.length !== 0) {
            listOfTrackObj.setListOfTrack(data);
            countOfTrack = data.length;

            html += '<h2>' + title + '</h2>';
            html += '<div style="display: flex; overflow-x: auto;">';
            $.each(data, function (i, track) {
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
            closeLoader();
        }
    });
}