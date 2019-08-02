$("table").css({"display": "block"});
$("footer").css({"display": "block"});
var checkEnableInfiniteLoad = true;
$("#disableInfiniteLoadButton").on("click", function () {
    var i = 1000000;
    checkEnableInfiniteLoad = true;
    $("table > tbody > tr").hide().slice(0, i).show();
    sessionStorage.setItem("infiniteLoad", checkEnableInfiniteLoad);
    $("#disableInfiniteLoadButton").hide();
    $("#enableInfiniteLoadButton").css({"display": "block"});
});
$("#enableInfiniteLoadButton").on("click", function () {
    window.sessionStorage.clear();
    $("#disableInfiniteLoadButton").show();
    $("#enableInfiniteLoadButton").css({"display": "none"});
    location.reload();
});
checkEnableInfiniteLoad = true;

var row_index = 0;
if (sessionStorage.getItem('scroll') === null) {
    $("table > tbody > tr").hide().slice(0, 15).show();
} else if (sessionStorage.getItem('row_index') >= 15) {
    $("table > tbody > tr").hide().slice(0, parseInt(sessionStorage.getItem("row_index")) + 6).show();
    window.scrollTo(0, parseInt(sessionStorage.getItem("row_index")));
} else {
    $("table > tbody > tr").hide().slice(0, 15).show();
}

$(document).ready(function () {
    var i = 0;
    $(window).scroll(function () {
        if ($(window).scrollTop() >= $(document).height() - $(window).height() - 10) {
            i = i + 30;
            $("table > tbody > tr").hide().slice(0, i).show();
            $(".playInJava").on("click", function () {
                $('td').on('click', function () {
                    row_index = $(this).parent().index() + 1;
                    sessionStorage.setItem("row_index", row_index);
                });
            });
        }
    });
});