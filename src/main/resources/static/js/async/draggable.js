$(function () {
    $("#historyTable").draggable({
        containment: 'window',
        scroll: false
    });

    $("#favouriteTable").draggable({
        containment: 'window',
        scroll: false
    });

    $("#playlistTable").draggable({
        containment: 'window',
        scroll: false
    });

    $("#lyric").draggable({
        containment: 'window',
        scroll: false
    });
});
