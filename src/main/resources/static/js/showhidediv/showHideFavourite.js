let favouriteDiv = $("#favouriteTable");

function showFavourite() {
    favouriteDiv.css('display', 'block');
    favouriteDiv.attr("class", "showFavouriteTable table table-bordered table-hover");
}

function hideFavourite() {
    favouriteDiv.attr("class", "hideFavouriteTable table table-bordered table-hover");

    setTimeout(() => {
        favouriteDiv.css('display', 'none');
    }, 500);
}

$(document).ready(function () {
    let counter = 0;

    showHideFavouriteButton.on('click', function () {
        counter++;
        if (counter % 2 !== 0) {
            showFavourite();
        } else {
            hideFavourite();
        }
    });
});
