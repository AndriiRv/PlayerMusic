let addToFavouriteButton = $(".addToFavouriteButton");
let favouriteTrackButton = $("#favouriteTrack");

function changeFavouritePic() {
    if (currentUserUsername.text() !== "") {
        $.get({
            url: '/favourite/already',
            data: {
                musicId: numberOfTrack,
            },
            success: function (data) {
                if (data === 1) {
                    addToFavouriteButton.css({
                        "background-image": "url('../../images/favourite/favouriteActive.svg')"
                    });
                } else {
                    addToFavouriteButton.css({
                        "background-image": "url('../../images/favourite/favourite.svg')"
                    });
                }
            }
        });
    }
}

addToFavouriteButton.on('click', function () {
    if (currentUserUsername.text() !== "") {
        $.post({
            url: '/favourite',
            data: {
                trackId: musicTrackId
            },
            success: function (responseText, statusText, data) {
                if (data.status === 201) {
                    $.notify(responseText, {
                        position: 'top left',
                        className: 'success'
                    });
                    getCounter(currentMusicTrackInListOfTrack, "#allFavourite", "#countOfFavourite")
                        .text(getCountOfFavouriteByMusicId(musicTrackId));
                    changeFavouritePic();
                } else if (data.status === 200) {
                    $.notify(responseText, {
                        position: 'top left',
                        className: 'info'
                    });
                    changeFavouritePic();
                }
            }
        });
    } else {
        getSignModalWindow();
    }
});

favouriteTrackButton.on('click', function () {
    openFavouriteForm();

    getFavourite();
    setTitleToNameOfTab("My favourite");
});

function getFavourite() {
    getLoader();

    let html = '';
    $.getJSON('/favourite', function (data) {
        if (data.length !== 0) {
            listOfTrackObj.setListOfTrack(data);
            countOfTrack = data.length;

            html += '<h2>Likes</h2>';
            html += '<div style="display: grid; grid-template-columns: 200px 200px 200px 200px 200px 175px; grid-auto-rows: 250px;">';
            $.each(data, function (i, track) {
                html += '<div style="max-width: 173px;">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" style="width: 173px; height: 173px;" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div class="titleOfTrackInTable" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden; height: auto">' + track.fullTitle + '</div>';
                html += '</div>'
            });
            html += '</div>';
            supportDashboard.html(html);
            closeLoader();
        } else {
            closeLoader();
            $.notify("You don't like any music tracks yet", {
                position: 'top left',
                className: 'error'
            });
        }
    });
}