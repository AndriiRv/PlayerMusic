let uploadInput = $("#uploadId");

uploadInput.on("click", function () {
    $("#uploadSendButton").css({"display": "block"});
});

$("#uploadSendButton").on("click", function (e) {
    e.preventDefault();

    let form = $('#uploadForm')[0];

    let data = new FormData(form);

    $.ajax({
        type: 'POST',
        url: '/upload',
        enctype: 'multipart/form-data',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function () {
            $("#currentUserName").notify("Music track successful upload to system!", {
                position: 'bottom left',
                className: 'success'
            });
        },
        error: function () {
            $("#currentUserName").notify("Not uploaded.", {
                position: 'bottom left',
                className: 'error'
            });
        }
    });
});
