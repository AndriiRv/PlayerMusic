let listOfTrack = null;
let countOfTrack = 0;

let defaultList = null;

$(document).ready(function () {
    getList("/sort/title=ASC");
});

$(".shuffleButton").on("click", function () {
    getList("shuffle");
});

function sort(url) {
    getList(url);
}

function getList(url) {
    listOfTrack = null;
    $(".eachTrClass").empty();

    let rawJson;

    let html = '';
    $.getJSON(url, function (data) {
        if (data.length !== 0) {
            defaultList = data;
            listOfTrack = data;
            countOfTrack = data.length;

            rawJson = data;
            $.each(rawJson, function (i, track) {
                html += '<tr class="eachTrClass" id="eachTr" th:each="track: ${trackList}">';
                html += '<td class="commonTd" colspan="3" style="text-align: right; vertical-align: middle;">';
                html += '<div class="titleOfTrackInTable" value="' + track.fullTitle + '}">' + track.fullTitle + '</div>';
                html += '<div>' + track.length + '</div>';
                html += '<div>' + track.date + ' ' + track.time + '</div>';
                html += '<div>' + track.size + ' MB' + '</div>';
                html += '</td>';
                html += '</tr>';
            });
            $('#mainTableTBody').html(html);
        }
    });
}
