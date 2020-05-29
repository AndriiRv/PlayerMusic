$("#playlist").on("click", function () {
    openPlaylistForm();

    getPlaylists();
});

supportDashboard.on("click", "#createPlaylistButton", function () {
    let inputValue = $("#createPlaylist").val();
    addPlaylist(inputValue);
});

function getPlaylists() {
    getLoader();
    setTitleToNameOfTab("My playlists");
    supportDashboard.empty();

    let html = '';
    html += '<input id="createPlaylist" placeholder="Input name new playlist" type="text"/>';
    html += '<button id="createPlaylistButton">Create playlist</button>';
    supportDashboard.html(html);

    $.getJSON('/playlist', function (data) {
        if (data.length !== 0) {
            html += '<h2 style="margin-left: 15px; margin-top: 15px;">Playlists</h2>';
            html += '<div id="playlist" style="display: flex; width: 1175px;">';
            html += '<div id="playlistList" style="flex-basis: 30%; margin-left: 15px;">';
            $.each(data, function (i, playlist) {
                html += '<div style="display: block; width: 150px;">';
                html += '   <div class="titleOfPlaylistInTable" onclick="showTrackInSelectPlaylist(\'' + playlist.title + '\')">' + playlist.title + '</div>';
                html += '   <div class="menuElement" onclick="removePlaylist(' + playlist.id + ')">Remove</div>';
                html += '</div>';
            });
            html += '</div>';
            html += '<div style="width: 80%; display: grid; grid-template-columns: 200px 200px 200px 200px;" id="listMusicOfPlaylist"></div>';
            supportDashboard.html(html);
            closeLoader();
        } else {
            closeLoader();
            $.notify("You not created any playlists", {
                position: 'top left',
                className: 'error'
            });
        }
    });
}

function addPlaylist(titleOfPlaylist) {
    if (titleOfPlaylist !== '') {
        $.post({
            url: '/playlist',
            data: {
                titleOfPlaylist: titleOfPlaylist
            },
            success: function () {
                $.notify(titleOfPlaylist + " - created new playlist", {
                    position: 'top left',
                    className: 'success'
                });
                getPlaylists();
                console.log(titleOfPlaylist + " - created new playlist");
            },
            error: function () {
                $.notify("Name of playlist can not be empty", {
                    position: 'top left',
                    className: 'error'
                });
                getPlaylists();
            }
        });
    }
}

let isSelectingPlaylistOpen = true;

function selectPlaylist() {
    if (currentUserUsername.text() !== "") {
        if (isSelectingPlaylistOpen) {
            isSelectingPlaylistOpen = false;

            let html = '';
            $.getJSON('/playlist', function (data) {
                if (data.length !== 0) {
                    $("#selectPlaylistId").show();
                    html += '<select onchange="addMusicToPlaylist(this.value, musicTrackId)">';
                    html += '<option value=""></option>';
                    $.each(data, function (i, playlist) {
                        html += '<option value="' + playlist.title + '">' + playlist.title + '</option>';
                    });
                    $("#selectPlaylistId").html(html);
                } else {
                    $.notify("You not created any playlists", {
                        position: 'top left',
                        className: 'error'
                    });
                }
            });
        } else {
            $("#selectPlaylistId").hide();
            isSelectingPlaylistOpen = true;
        }
    } else {
        getSignModalWindow();
    }
}

function showTrackInSelectPlaylist(titleOfPlaylist) {
    let html = '';
    $.getJSON('/playlist/music', {titleOfPlaylist: titleOfPlaylist}, function (data) {
        if (data.length !== 0) {
            $.each(data, function (i, track) {
                html += '<div style="max-width: 173px;">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" alt="cover" style="width: 173px; height: 173px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden; height: auto;" class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '   <div class="menuElement" onclick="removeTrackFromPlaylist(\'' + titleOfPlaylist + '\',\'' + track.id + '\');">Remove</div>';
                html += '</div>';
            });
            $("#listMusicOfPlaylist").html(html);
        } else {
            $.notify("You has not added track to playlist", {
                position: 'top left',
                className: 'error'
            });
        }
    });
}

function addMusicToPlaylist(titleOfPlaylist, trackId) {
    $.post({
        url: '/playlist/addMusic',
        data: {
            titleOfPlaylist: titleOfPlaylist,
            trackId: trackId
        },
        success: function () {
            $.notify("Add to playlist: \"" + titleOfPlaylist + "\n", {
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
                getPlaylists();
                console.log(playlistId + ", deleted playlist")
            }
        });
    }
}

function removeTrackFromPlaylist(titleOfPlaylist, trackId) {
    if (confirm("Are you sure delete music from playlist \"" + titleOfPlaylist + "\"?")) {
        $.ajax({
            type: 'DELETE',
            url: '/playlist/music',
            data: {
                playlistTitle: titleOfPlaylist,
                trackId: trackId
            },
            success: function () {
                getPlaylists();
                console.log(trackId + ", deleted music from playlist")
            }
        });
    }
}