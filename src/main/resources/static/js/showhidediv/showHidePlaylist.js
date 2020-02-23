let playlistDiv = $("#playlistTable");
let showHidePlaylists = $("#showHidePlaylists");

function showPlaylist() {
    playlistDiv.css('display', 'block');
    playlistDiv.attr("class", "showPlaylistTable table table-bordered table-hover");
}

function hidePlaylist() {
    playlistDiv.attr("class", "hidePlaylistTable table table-bordered table-hover");

    setTimeout(() => {
        playlistDiv.css('display', 'none');
    }, 500);
}

$(document).ready(function () {
    let counter = 0;

    showHidePlaylists.on('click', function () {
        counter++;
        if (counter % 2 !== 0) {
            showPlaylist();
        } else {
            hidePlaylist();
        }
    });
});
