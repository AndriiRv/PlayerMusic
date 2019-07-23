var row_index = 0;
$("table").css({"display": "block"});
$("footer").css({"display": "block"});
if (localStorage.getItem('scroll') === null) {
    $("table > tbody > tr").hide().slice(0, 15).show();
} else if (localStorage.getItem('row_index') >= 15) {
    $("table > tbody > tr").hide().slice(0, parseInt(localStorage.getItem("row_index")) + 6).show();
    window.scrollTo(0, parseInt(localStorage.getItem("row_index")));
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
                $('td').click(function () {
                    row_index = $(this).parent().index() + 1;
                    localStorage.setItem("row_index", row_index);
                });
            });
        }
    });
});