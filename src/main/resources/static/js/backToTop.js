var backToTop = $('#backToTop');
if (backToTop.length) {
    var scrollTrigger = 100,
        doBackToTop = function () {
            var scrollTop = $(window).scrollTop();
            if (scrollTop > scrollTrigger) {
                backToTop.fadeIn();
                backToTop.addClass('show');
            } else {
                backToTop.removeClass('show');
                backToTop.fadeOut();
            }
        };
    doBackToTop();
    $(window).on('scroll', function () {
        doBackToTop();
    });
    backToTop.on('click', function (e) {
        e.preventDefault();
        $('html, body').animate({
            scrollTop: 0
        }, 700);
    });
}