var notification = $("#notification");
$("#uploadId").on("click", function () {
    $("#uploadSendButton").css({"display": "block"});
});

$("#uploadSendButton").on("click", function () {
    notification.fadeIn();
    notification.show();
    notification.html("Upload!");
    setTimeout(function () {
        $("#notification").hide();
    }, 2000);
});