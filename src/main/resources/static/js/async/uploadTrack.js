let uploadMusic = $("#uploadMusic");

let uploadForm = $('#uploadForm');

uploadMusic.on("click", function () {
    openUploadForm();

    getUploadFrom();
});

function getUploadFrom() {
    setTitleToNameOfTab("Upload music track");
    $('#mainTableTBody').empty();

    uploadForm.css({
        "display": "block"
    });

    let html = '';
    html +=
        '<h2 style="color: white;">You can upload your own music tracks to our database</h2>' +
        '<form method="post" id="uploadForm" enctype="multipart/form-data">' +
        '   <input type="file" id="uploadId" name="musicTrackAsFile" accept=".mp3"/><br/>' +
        '   <button id="uploadSendButton">Send</button>' +
        '</form>';

    uploadForm.html(html);
    $('#listOfTrack').removeClass("main");
    let uploadMusicDiv = $("#uploadMusicDiv");
    uploadMusicDiv.css({
        "color": "white"
    });
}

let uploadInput = $("#uploadId");

uploadInput.on("click", function () {
    $("#uploadSendButton").css({"display": "block"});
});

$("#uploadForm").on("click", $("#uploadSendButton"), function (e) {
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
            $("#currentUserName").notify("Music track successful upload to system!", {
                position: 'bottom left',
                className: 'success'
            });
        }
    });
});