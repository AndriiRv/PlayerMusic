$(document).ready(function () {
    $("span").click(function () {
        $(".list").hide();
        $(".wrapper").css({"display": "flex"});
        $(".wrapper").show();
    });
    $("#backToList").click(function () {
        $(".list").show();
        $("body").css({"background": ""});
        $("#backToList").hide();
    });
});