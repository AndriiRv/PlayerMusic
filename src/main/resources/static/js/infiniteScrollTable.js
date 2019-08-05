$("table").css({"display": "block"});
$("footer").css({"display": "block"});
if ($(window).width() > 600) {
    $("table > tbody > tr").hide().slice(0, 30).show();
    $(document).ready(function () {
        var i = 0;
        $(window).scroll(function () {
            if ($(window).scrollTop() >= $(document).height() - $(window).height() - 10) {
                i = i + 30;
                $("table > tbody > tr").hide().slice(0, i).show();
            }
        });
    });
} else {
    $("table > tbody > tr").hide().slice(0, 30).show();
    $(document).ready(function () {
        var i = 10;
        $(window).on("touchstart", function () {
            if ($(window).scrollTop() >= $('body').height() - $(window).height() - 10) {
                i = i + 3;
                $("table > tbody > tr").hide().slice(0, i).show();
            }
        });
    });
}