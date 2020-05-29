let countOfFavourite = 0;
let limitSideElementInDiv = 3;

function getDashboardSideFavouriteAndHistory() {
    getListOnDashboardSide("/favourite", '#dashboardFavourite', getCountOfFavourite(), "Your favourite", "../../images/favourite/favourite.svg");
    getListOnDashboardSide("/history", '#dashboardHistory', getCountOfHistory(), "Your history", "../../images/play.svg");
}

function getListOnDashboardSide(url, divId, countOf, divTitle, picturePath) {
    listOfTrack = null;

    let html = '';
    $.getJSON(url, function (data) {
        if (data.length !== 0) {
            listOfTrack = data;
            countOfTrack = data.length;
            html += '<div style="display: flex">';
            html += '   <div style="display: flex; flex-basis: 100%;">';
            html += '       <div style="background-image: url(' + picturePath + '); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
            html += '       <div style="align-self: flex-end;">' + countOf + '</div>';
            html += '   </div>';
            html += '   <div style="width: 100%;">' + divTitle + '</div>';
            html += '</div>';
            $.each(data, function (i, track) {
                html += '<div style="display: flex; margin-bottom: 10px;">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div class="titleOfTrackInTable" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">' + track.fullTitle + '</div>';
                html += '   <div title="Count of played from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div style="align-self: flex-end;">' + track.countOfPlayed + '</div>';
                html += '   </div>';
                html += '   <div title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
                html += '       <div style="background-image: url(../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '       <div style="align-self: flex-end;">' + track.countOfFavourite + '</div>';
                html += '   </div>';
                html += '</div>';
                i++;
                return i < limitSideElementInDiv;
            });
            $(divId).html(html);
        } else {
            html += '<h3>You dont like any music tracks yet</h3>';
            $(divId).html(html);
        }
    });
}

function getListOnDashboard() {
    // $("#footerInDashboardSide").detach();
    // $("#dashboardFavourite").detach();
    // $("#dashboardHistory").detach();
    //
    // let favouriteHtml = '<div>Sign in for access to your favourite music</div>';
    // let historyHtml = '<div>List of history may be here, please sign in</div>';
    //
    // $("#dashboardFavourite").append(favouriteHtml);
    // $("#dashboardHistory").append(historyHtml);

    let footerHtml = '<footer id="footerInDashboardSide" style="text-align: center">\n' +
        '        <p>Music Player, by <a href="https://www.linkedin.com/in/andrii-reva-795200141/">Andrii\n' +
        '            Reva</a></p>\n' +
        '       </footer>';

    dashboardSide.append(footerHtml);
}

function getCountOfFavourite() {
    let result = null;
    if (currentUserUsername.text() !== "") {
        $.get({
            url: '/favourite/count',
            async: false,
            success: function (data) {
                result = data;
            }
        });
    }
    return result;
}

function getCountOfHistory() {
    let result = null;
    if (currentUserUsername.text() !== "") {
        $.get({
            url: '/history/count',
            async: false,
            success: function (data) {
                result = data;
            }
        });
    }
    return result;
}