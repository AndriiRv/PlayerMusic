let inputSearchTrack = $("#inputSearchTrack");

inputSearchTrack.bind("enterKey", function () {
    let inputTitle = $(this).val().toLowerCase();
    inputTitle = inputTitle.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");

    setTitleToNameOfTab("Search by: " + inputTitle);

    openSearch();
    searchTracks(inputTitle);
});
inputSearchTrack.keyup(function (e) {
    if (e.keyCode === 13) {
        $(this).trigger("enterKey");
    }
});

function clearSearch() {
    inputSearchTrack.val("");
}

function searchTracks(searchInput) {
    getLoader();

    setTitleToNameOfTab("Searched tracks by: " + searchInput);
    let html = '';
    $.get({
        url: '/search',
        data: {
            searchString: searchInput
        },
        success: function (data) {
            if (data.length !== 0) {
                listOfTrackObj.setListOfTrack(data);
                countOfTrack = data.length;

                html += '<div id="searchList" style="display: block; width: 1175px; z-index: 0">';
                html += '<h2>Searched tracks by: ' + searchInput + '</h2>';
                $.each(data, function (i, track) {
                    html += '<div style="display: flex; margin-left: 15px; margin-bottom: 15px;">';
                    html += '   <div id="musicId" hidden>' + track.id + '</div>';
                    html += '   <img id="cover" style="width: 173px; height: 173px;" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                    html += '      <div style="display: block; margin-left: 15px;">';
                    html += '         <div id="musicId" hidden>' + track.id + '</div>';
                    html += '         <div class="titleOfTrackInTable" style="height: auto">' + track.fullTitle + '</div>';
                    html += '         <div id="statistic" style="display: flex; width: 100%">';
                    html += '            <div id="played" title="Count of played from all users" style="align-self: flex-end; display: flex;">';
                    html += '               <div style="background-image: url(../../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                    html += '               <div id="countOfPlayedByMusic" style="align-self: flex-end;">' + getCountOfPlayedByMusicId(track.id) + '</div>';
                    html += '            </div>';
                    html += '            <div id="allFavourite" title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
                    html += '               <div style="background-image: url(../../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                    html += '               <div id="countOfFavourite" style="align-self: flex-end;">' + getCountOfFavouriteByMusicId(track.id) + '</div>';
                    html += '            </div>';
                    html += '         </div>';
                    html += '         <div class="singer"><b>Singer</b>: ' + track.singer + '</div>';
                    html += '         <div class="title"><b>Title</b>: ' + track.title + '</div>';
                    html += '         <div class="length"><b>Length</b>: ' + track.length + '</div>';
                    if (track.albumTitle !== null && track.albumTitle !== '') {
                        html += '     <div class="albumTitle"><b>Album</b>: ' + track.albumTitle + '</div>';
                    }
                    if (track.year !== null && track.year !== '') {
                        html += '     <div class="year"><b>Year</b>: ' + track.year + '</div>';
                    }
                    if (track.genre !== null && track.genre !== '') {
                        html += '     <div class="genre"><b>Genre</b>: ' + track.genre + '</div>';
                    }
                    html += '      </div>';
                    html += '</div>';
                    html += '</div>';
                });
                html += '</div>';
                supportDashboard.html(html);
                closeLoader();
            } else {
                html += '<div id="resultNotFoundDiv" style="min-width: 1175px;"></div>';
                supportDashboard.html(html);
                let resultNotFoundDiv = $("#resultNotFoundDiv");
                resultNotFoundDiv.css({
                    "background-image": "url('../images/notFound.png')",
                    "background-size": "569px 200px",
                    "width": "569px",
                    "height": "200px",
                    "background-repeat": "no-repeat",
                    "z-index": "0",
                    "margin-left": "25%",
                    "margin-top": "10%"
                });
                closeLoader();
            }
        }
    });
}