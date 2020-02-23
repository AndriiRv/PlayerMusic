function addPlaylist(titleOfPlaylist) {
    $.post({
        url: '/playlist',
        data: {
            titleOfPlaylist: titleOfPlaylist
        },
        success: function () {
            console.log(titleOfPlaylist + " - created new playlist");
        }
    });
}

function addMusicToPlaylist(titleOfPlaylist, trackTitle) {
    $.post({
        url: '/playlist/addMusic',
        data: {
            titleOfPlaylist: titleOfPlaylist,
            trackTitle: trackTitle
        },
        success: function () {
            console.log(titleOfPlaylist + " - created new playlist");
        }
    });
}

$("#showHidePlaylists").on("click", function () {
    getPlaylists();
});

function getPlaylists() {
    $.get({
        url: '/playlist',
        success: function () {
            let rawJson;

            let html = '';
            $.getJSON('/playlist', function (data) {
                if (data.length !== 0) {
                    rawJson = data;

                    html += '<table class="table table-bordered table-hover">';
                    html += '<thead><tr><th style="border-color: white; border-style:solid;">Your playlists</th></tr></thead><tbody id="playlistTBody">';
                    $.each(rawJson, function (i, titleOfPlaylist) {
                        html += '<tr><td><div class="titleOfPlaylistInTable">' + titleOfPlaylist + '</div>';
                        html += '</tr></td>';
                    });
                    html += '</tbody></table>';
                    $("#playlistTitle").html(html);
                } else {
                    $("#showHidePlaylists").notify("You not created any playlists", {
                        position: 'top left',
                        className: 'error'
                    });
                }
            });
        }
    });
}

// function removeTrackToHistory(fullTitleTrack) {
//     $.ajax({
//         type: 'DELETE',
//         url: '/history',
//         data: {
//             fullTrackTitle: fullTitleTrack
//         },
//         success: function () {
//             $("#showHideHistoryTrack").notify(fullTitleTrack + " deleted from your history", {
//                 position: 'top left',
//                 className: 'error'
//             });
//             console.log(fullTitleTrack + ", deleted from history")
//         }
//     });
// }
