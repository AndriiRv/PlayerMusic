let idSort = $("#idSort");
let idFilter = $("#idFilter");

$(document).ready(function () {
    sortOrFilter(idSort);
    sortOrFilter(idFilter);
});

function sortOrFilter(element) {
    element.on('change', function () {
        $(this).children("option").prop('disabled', false);
        $(this).children("option:first").prop('disabled', true);
        $(this).children("option:selected").prop('disabled', true);
        $(this).css({
            "color": "white"
        });
        $(this).blur();
    });

    element.focus(function () {
        $(this).css({
            "color": "black"
        });
    }).focusout(function () {
        $(this).css({
            "color": "white"
        });
    });
}

function showFilterOption() {
    let allGenre = getAllGenre();

    let html = '';
    html += '<option id="emptyFilterOption" value="" selected>Choose filter</option>';
    $.each(allGenre, function (i, genre) {
        html += '<option value="/filter/' + genre + '">' + genre + '</option>';
    });
    idFilter.html(html);
}