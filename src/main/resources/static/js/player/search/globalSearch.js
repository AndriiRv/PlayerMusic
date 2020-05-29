let inputSearchTrack = $("#inputSearchTrack");

inputSearchTrack.bind("enterKey", function () {
    let inputTitle = $(this).val().toLowerCase();

    setTitleToNameOfTab("Search by: " + inputTitle);

    openSearch();
    searchTracks(inputTitle);
});
inputSearchTrack.keyup(function (e) {
    if (e.keyCode === 13) {
        $(this).trigger("enterKey");
    }
});

function searchTracks(searchInput) {
    let html = '';
    $.get({
        url: '/search',
        data: {
            searchString: searchInput
        },
        success: function (data) {
            if (data.length !== 0) {
                listOfTrack = data;
                countOfTrack = data.length;

                html += '<div id="searchList" style="display: block; width: 1175px; z-index: 0">';
                html += '<h2>Searched tracks by: ' + searchInput + '</h2>';
                $.each(data, function (i, track) {
                    html += '<div style="display: flex; margin-left: 15px; margin-bottom: 15px;">';
                    html += '   <div id="musicId" hidden>' + track.id + '</div>';
                    html += '   <img id="cover" style="width: 173px; height: 173px;" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                    html += '       <div style="display: block;">';
                    html += '           <div id="musicId" hidden>' + track.id + '</div>';
                    html += '           <div class="titleOfTrackInTable" style="height: auto">' + track.fullTitle + '</div>';
                    html += '           <div class="singer">Singer: ' + track.singer + '</div>';
                    html += '           <div class="title">Title: ' + track.title + '</div>';
                    html += '           <div class="length">Length: ' + track.length + '</div>';
                    if (track.albumTitle !== null && track.albumTitle !== '') {
                        html += '       <div class="albumTitle">Album: ' + track.albumTitle + '</div>';
                    }
                    if (track.year !== null && track.year !== '') {
                        html += '       <div class="year">Year: ' + track.year + '</div>';
                    }
                    if (track.genre !== null && track.genre !== '') {
                        html += '       <div class="genre">Genre: ' + track.genre + '</div>';
                    }
                    html += '       </div>';
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
            }
        }
    });
}