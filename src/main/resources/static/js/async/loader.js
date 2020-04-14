let preloader = null;
let svgAnm = null;

function getLoader() {
    $('#preloader').empty();
    let html = '';
    html += '<div class="mainPreloader">\n' +
        '        <span class="animation"></span>\n' +
        '        <br>Please wait<br>\n' +
        '        <small>Music Player is loading...</small>\n' +
        '    </div>';
    $('#preloader').html(html);
    preloader = $('#preloader');
    svgAnm = preloader.find('.animation');
    svgAnm.show();
    preloader.show();
}

function closeLoader() {
    svgAnm.fadeOut();
    preloader.fadeOut('slow');
}