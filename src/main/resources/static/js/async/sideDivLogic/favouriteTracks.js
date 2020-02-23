let addToFavouriteButton = $(".addToFavouriteButton");
let title = $("#showInfoAboutTrack");
let showHideFavouriteButton = $("#showHideFavouriteTrack");

let favouriteList = null;

addToFavouriteButton.on('click', function () {
    addToFavouriteButton.val(title.text() + ".mp3");
    $.post({
        url: '/favourite/',
        data: {
            trackTitle: addToFavouriteButton.val()
        },
        success: function () {
            title.notify(addToFavouriteButton.val() + "\n added to your musictracks", {
                position: 'top left',
                className: 'success'
            });
        }
    });
});

showHideFavouriteButton.on('click', function () {
    getFavourite();
});

function getFavourite() {
    listOfTrack = null;
    $.get({
        url: '/favourite',
        success: function () {
            let rawJson;

            let html = '';
            $.getJSON('/favourite', function (data) {
                if (data.length !== 0) {
                    favouriteList = data;
                    listOfTrack = data;
                    countOfTrack = data.length;

                    rawJson = data;
                    html += '<table class="table table-bordered table-hover">';
                    html += '<thead><tr><th style="border-color: white; border-style:solid;">Likes</th></tr></thead><tbody id="favouriteTBody">';
                    $.each(rawJson, function (i, track) {
                        html += '<tr><td><div class="titleOfTrackInTable">' + track + '</div>';
                        html += '</tr></td>';
                    });
                    html += '</tbody></table>';
                    $("#favouriteTitle").html(html);
                } else {
                    showHideFavouriteButton.notify("You don't like any music tracks yet", {
                        position: 'top left',
                        className: 'error'
                    });
                }
            });
        }
    });
}
