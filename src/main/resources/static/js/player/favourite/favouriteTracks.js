let addToFavouriteButton = $(".addToFavouriteButton");
// let title = $("#showInfoAboutTrack");
let favouriteTrackButton = $("#favouriteTrack");

function changeFavouritePic() {
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

function getCountOfFavouriteByMusicId(musicId) {
    let result = null;
    $.get({
        url: '/favourite/count',
        data: {
            musicId: musicId,
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

addToFavouriteButton.on('click', function () {
    addToFavouriteButton.val(title.text() + ".mp3");
    $.post({
        url: '/favourite',
        data: {
            trackTitle: addToFavouriteButton.val()
        },
        success: function (responseText, statusText, data) {
            if (data.status === 201) {
                title.notify(responseText, {
                    position: 'top left',
                    className: 'success'
                });
                changeFavouritePic();
            } else if (data.status === 200) {
                title.notify(responseText, {
                    position: 'top left',
                    className: 'info'
                });
                changeFavouritePic();
            }
        }
    });
});

favouriteTrackButton.on('click', function () {
    openFavouriteForm();

    getFavourite();
    setTitleToNameOfTab("My favourite");
});

function getFavourite() {
    listOfTrack = null;

    let html = '';
    $.getJSON('/favourite', function (data) {
        if (data.length !== 0) {
            listOfTrack = data;
            countOfTrack = data.length;
            $.each(data, function (i, track) {
                html += '<tr>';
                html += '<td class="commonTd" style="display: flex">';
                html += '<img id="cover" style="width: 74px; height: 74px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '<div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '</td>';
                html += '</tr>';
            });
            $('#mainTableTBody').html(html);
            $('#listOfTrack').removeClass("main");
        } else {
            $("#homeButton").notify("You don't like any music tracks yet", {
                position: 'bottom left',
                className: 'error'
            });
        }
    });
}