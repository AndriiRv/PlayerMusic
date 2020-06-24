function getAllGenre() {
    let genres = [];
    $.get({
        url: '/dashboard/genre',
        async: false,
        success: function (listOfGenres) {
            for (let i = 0; i < listOfGenres.length; i++) {
                genres.push(listOfGenres[i]);
            }
        }
    });
    return genres;
}

function getMusicByGenre() {
    $('#byGenre').empty();
    let genres = getAllGenre();
    for (let i = 0; i < genres.length; i++) {
        createDivOnGenre("/dashboard/byGenre", genres[i]);
    }
}

function createDivOnGenre(url, titleOfGenre) {
    let html = '';
    $.getJSON(url, {genre: titleOfGenre}, function (data) {
        html += '<h2>' + titleOfGenre + '</h2>';
        html += '<div style="display: flex; overflow-x: auto;">';
        $.each(data, function (i, track) {
            html += '<div style="min-width: 173px; margin-right: 1%;">';
            html += '   <div>';
            html += '      <div id="musicId" hidden>' + track.id + '</div>';
            html += '      <img id="cover" style="width: 173px; height: 173px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
            html += '      <div class="titleOfTrackInTable" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">' + track.fullTitle + '</div>';
            html += '   </div>';
            html += '</div>'
        });
        html += '</div>';
        $('#byGenre').append(html);
    });
}