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
                html += '<div id="uploadedMusic" style="display: flex">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" alt="cover" style="width: 64px; height: 64px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '</div>';
            });
            $("#uploadedTrack").html(html);
        }
    });
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
        success: function () {
            $('#uploadId').val("");
            $.notify("Music track successful upload to system!", {
                position: 'top left',
                className: 'success'
            });
            getUploadFrom();
        }
    });
}