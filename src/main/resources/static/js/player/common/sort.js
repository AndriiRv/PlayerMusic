let idSort = $("#idSort");

idSort.on('change', function () {
    $(this).children("option").prop('disabled', false);
    $(this).children("option:first").prop('disabled', true);
    $(this).children("option:selected").prop('disabled', true);
    $(this).css({
        "color": "white"
    });
    $(this).blur();
});

idSort.focus(function () {
    $(this).css({
        "color": "black"
    });
}).focusout(function () {
    $(this).css({
        "color": "white"
    });
});