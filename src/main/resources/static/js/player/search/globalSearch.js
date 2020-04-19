inputSearchTrack.bind("enterKey", function () {
    let inputTitle = $(this).val().toLowerCase();
    searchTracks(inputTitle);
});
inputSearchTrack.keyup(function (e) {
    if (e.keyCode === 13) {
        $(this).trigger("enterKey");
    }
});

function searchTracks(searchInput) {
    let html = '';
    $.get({
        url: '/search',
        data: {
            searchString: searchInput
        },
        success: function (data) {
            if (data.length !== 0) {
                defaultList = data;
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
                closeLoader();
            } else {
                html += '<div id="resultNotFoundDiv"></div>';
                $('#mainTableTBody').html(html);
                let resultNotFoundDiv = $("#resultNotFoundDiv");
                resultNotFoundDiv.css({
                    "background-image": "url('../images/notFound.png')",
                    "background-size": "569px 200px",
                    "width": "569px",
                    "height": "200px",
                    "background-repeat": "no-repeat",
                    "z-index": "2",
                    "margin-left": "auto",
                    "margin-right": "auto",
                    "display": "block"
                });
                $('#mainTableTBody').css({});
                $('#listOfTrack').removeClass("main");
            }
        }
    });
}