$("#playlist").on("click", function () {
    getPlaylists();
});

$("#mainTableTBody").on("click", "#createPlaylistButton", function () {
    let inputValue = $("#createPlaylist").val();
    addPlaylist(inputValue);
});

function getPlaylists() {
    setTitleToNameOfTab("My playlists");
    $("#mainTableTBody").empty();

    let html = '';
    html += '<label style="color:white" for="createPlaylist">Input name new playlist:</label>';
    html += '<input id="createPlaylist" type="text"/>';
    html += '<button id="createPlaylistButton">Create playlist</button>';
    $("#mainTableTBody").html(html);

    $.getJSON('/playlist', function (data) {
        if (data.length !== 0) {
            html += '<table class="table table-bordered table-hover">';
            html += '<thead><tr><th style="border-color: white; border-style:solid;">Your playlists</th></tr></thead><tbody id="playlistTBody">';

            $.each(data, function (i, playlist) {
                html += '<tr>';
                html += '<td>';
                html += '<div class="titleOfPlaylistInTable" onclick="showTrackInSelectPlaylist(\'' + playlist.title + '\')">' + playlist.title + '</div>';
                html += '<div class="menuElement" onclick="removePlaylist(' + playlist.id + ')">Remove</button>';
                html += '</td>';
                html += '</tr>';
            });
            html += '</tbody></table>';
            $("#mainTableTBody").html(html);
        } else {
            $("#showHidePlaylists").notify("You not created any playlists", {
                position: 'top left',
                className: 'error'
            });
        }
    });
}

function addPlaylist(titleOfPlaylist) {
    $.post({
        url: '/playlist',
        data: {
            titleOfPlaylist: titleOfPlaylist
        },
        success: function () {
            $("#currentUserUsername").notify(titleOfPlaylist + " - created new playlist", {
                position: 'bottom left',
                className: 'success'
            });
            getPlaylists();
            console.log(titleOfPlaylist + " - created new playlist");
        },
        error: function () {
            $("#currentUserUsername").notify("Name of playlist can not be empty", {
                position: 'bottom left',
                className: 'error'
            });
            getPlaylists();
        }
    });
}

let isSelectingPlaylistOpen = true;

function selectPlaylist() {
    if (isSelectingPlaylistOpen) {
        isSelectingPlaylistOpen = false;

        let html = '';
        $.getJSON('/playlist', function (data) {
            if (data.length !== 0) {
                $.each(data, function (i, playlist) {
                    html += '<button onclick="addMusicToPlaylist(\'' + playlist.title + '\',\'' + fullTitle + '\');">to "' + playlist.title + '"</button>';
                });
                $("#selectPlaylistDiv").html(html);
            } else {
                $("#selectPlaylistButton").notify("You not created any playlists", {
                    position: 'top left',
                    className: 'error'
                });
            }
        });
    } else {
        $("#selectPlaylistDiv").empty();
        isSelectingPlaylistOpen = true;
    }
}

function showTrackInSelectPlaylist(titleOfPlaylist) {
    $("#mainTableTBody").empty();

    let html = '';
    $.getJSON('/playlist/music', {titleOfPlaylist: titleOfPlaylist}, function (data) {
        if (data.length !== 0) {
            $.each(data, function (i, track) {
                html += '<tr>';
                html += '<td class="commonTd" style="display: flex">';
                html += '<img id="cover" style="width: 50px; height: 50px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '<div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '<div class="menuElement" onclick="removeTrackFromPlaylist(\'' + titleOfPlaylist + '\',\'' + track.id + '\');">Remove</div>';
                html += '</td>';
                html += '</tr>';
            });
            $("#mainTableTBody").html(html);
            $('#listOfTrack').removeClass("main");
        } else {
            $("#currentUserUsername").notify("You has not added track to playlist", {
                position: 'bottom left',
                className: 'error'
            });
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
            $("#selectPlaylistButton").notify("Add to \"" + titleOfPlaylist + "\n", {
                position: 'top left',
                className: 'success'
            });
            console.log(titleOfPlaylist + " - add to \"" + titleOfPlaylist + "\n");
        }
    });
}

function removePlaylist(playlistId) {
    if (confirm("Are you sure delete playlist " + playlistId + "?")) {
        $.ajax({
            type: 'DELETE',
            url: '/playlist',
            data: {
                playlistId: playlistId
            },
            success: function () {
                $("#deletePlaylistButton").notify(playlistId + " deleted your playlist", {
                    position: 'top left',
                    className: 'info'
                });
                getPlaylists();
                console.log(playlistId + ", deleted playlist")
            }
        });
    }
}

function removeTrackFromPlaylist(titleOfPlaylist, trackId) {
    if (confirm("Are you sure delete music " + titleOfTrack + "from playlist \"" +  "\"?")) {
        $.ajax({
            type: 'DELETE',
            url: '/playlist/music',
            data: {
                playlistTitle: titleOfPlaylist,
                trackId: trackId
            },
            success: function () {
                $("#deletePlaylistButton").notify(titleOfTrack + " deleted from your playlist", {
                    position: 'top left',
                    className: 'info'
                });
                getPlaylists();
            }
        });
    }
}