function getAllGenre() {
    let genres = [];
    $.get({
        url: '/dashboard/genre',
        async: false,
        success: function (genre) {
            genres.push(genre)
        }
    });
    return genres;
}

function getMusicByGenre() {
    let genres = getAllGenre();
    // let listFromUrl = getListFromUrl("/sort/title=ASC?page=900");
    for (let i = 0; i < genres.length; i++) {
        // if (isGenreContainInListOfTrack(genres, listFromUrl[i].genre)) {
        let html = '';
        html += '<h2>' + genres[i] + '</h2>';
        html += '<div id="' + genres[i] + 'Id' + '" style="display: flex; overflow-x: auto;"></div>';
        $('#byGenre').append(html);
        // }
    }
}

function isGenreContainInListOfTrack(genres, genreOfTrack) {
    if (genreOfTrack !== null && genres.indexOf(genreOfTrack)) {
        return true;
    }
    return false;
}

// function createDivOnGenre(url, titleOfGenre) {
//     getLoader();
//
//     let html = '';
//     $.getJSON(url, function (data) {
//         html += '<h2>' + titleOfGenre + '</h2>';
//         html += '<div style="display: flex; overflow-x: auto;">';
//         $.each(data, function (i, track) {
//             html += '<div style="min-width: 173px; margin-right: 1%;">';
//             html += '    <div>';
//             html += '        <div id="musicId" hidden>' + track.id + '</div>';
//             html += '        <img id="cover" style="width: 173px; height: 173px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
//             html += '        <div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
//             html += '    </div>';
//             html += '</div>'
//         });
//         html += '</div>';
//         $('#byGenre').html(html);
//         getRandomTrackInSearchPlaceholder();
//         closeLoader();
//     });
// }