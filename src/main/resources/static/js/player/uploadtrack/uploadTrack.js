let uploadMusic = $("#uploadMusic");
let uploadDiv = $('#uploadDiv');

uploadMusic.on("click", function () {
    openUploadForm();

    getUploadFrom();
});

function getUploadFrom() {
    setTitleToNameOfTab("Upload music track");
    getUploadedTrack();

    uploadDiv.css({
        "display": "block"
    });

    let html = '';
    html +=
        '<div id="uploadDiv">' +
        '   <h3 style="color: white;">You can upload your own music tracks to our database</h3>' +
        '   <form method="post" id="uploadForm" enctype="multipart/form-data">' +
        '      <input type="file" id="uploadId" name="musicTrackAsFile" accept=".mp3"/><br/>' +
        '      <button id="uploadSendButton">Send</button>' +
        '   </form>' +
        '</div>';

    html += '<div id="uploadedTrack" style="display: block"></div>';

    supportDashboard.html(html);

    $("#uploadSendButton").on("click", function (e) {
        upload(e);
    });

}

function getUploadedTrack() {
    let html = '';
    $.getJSON('/upload', function (data) {
        if (data.length !== 0) {
            html += '<h3>Your uploaded music tracks</h3>';
            $.each(data, function (i, track) {
                html += '<div id="uploadedMusic" style="display: flex; margin-bottom: 25px">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" alt="cover" style="width: 64px; height: 64px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '      <div style="display: block; margin-left: 15px;">';
                html += '   <div class="titleOfTrackInTable" style="height: auto;">' + track.fullTitle + '</div>';
                html += '   <div id="meta" style="display: block">';
                html += '   <div id="statistic" style="display: flex; width: 100%">';
                html += '      <div id="played" title="Count of played from all users" style="align-self: flex-end; display: flex;">';
                html += '         <div style="background-image: url(../../../images/play.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '         <div id="countOfPlayedByMusic" style="align-self: flex-end;">' + getCountOfPlayedByMusicId(track.id) + '</div>';
                html += '      </div>';
                html += '      <div id="allFavourite" title="Count of liked from all users" style="align-self: flex-end; display: flex;">';
                html += '         <div style="background-image: url(../../../images/favourite/favourite.svg); background-size: 15px 15px; height: 15px; width: 15px;"></div>';
                html += '         <div id="countOfFavourite" style="align-self: flex-end;">' + getCountOfFavouriteByMusicId(track.id) + '</div>';
                html += '      </div>';
                html += '   </div>';
                html += '   <div class="length"><b>Length</b>: ' + track.length + '</div>';
                if (track.albumTitle !== null && track.albumTitle !== '') {
                    html += '<div class="albumTitle"><b>Album</b>: ' + track.albumTitle + '</div>';
                }
                if (track.year !== null && track.year !== '') {
                    html += '<div class="year"><b>Year</b>: ' + track.year + '</div>';
                }
                if (track.genre !== null && track.genre !== '') {
                    html += '<div class="genre"><b>Genre</b>: ' + track.genre + '</div>';
                }
                html += '</div>';
                html += '</div>';
                html += '   </div>';
            });
            $("#uploadedTrack").html(html);
        }
    });
}

function clearUploadForm() {
    $('#uploadId').val("");
}

function upload(e) {
    let form = $('#uploadForm')[0];
    let uploadingTrack = new FormData(form);

    if ($('#uploadId').get(0).files.length !== 0) {
        e.preventDefault();
    }

    $.ajax({
        type: 'POST',
        url: '/upload',
        enctype: 'multipart/form-data',
        cache: false,
        contentType: false,
        processData: false,
        data: uploadingTrack,
        success: function (responseText) {
            clearUploadForm();
            $.notify(responseText, {
                position: 'top left',
                className: 'success'
            });
            getUploadFrom();
        },
        error: function (response) {
            clearUploadForm();
            $.notify(response.responseText, {
                position: 'top left',
                className: 'info'
            });
            getUploadFrom();
        }
    });
}