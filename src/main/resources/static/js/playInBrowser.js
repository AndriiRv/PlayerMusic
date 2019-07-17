$(document).ready(function () {
    $("span").click(function () {
        var title = $(this).text();
        $("#audioId").attr("src", "play/" + title);
    });
});