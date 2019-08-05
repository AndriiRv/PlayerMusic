$(document).ready(function () {
    var preloader = $('#preloader'),
        svg_anm = preloader.find('.animation');
    svg_anm.fadeOut();
    preloader.delay(250).fadeOut('slow');
});