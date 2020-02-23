let historyList = null;

function addTrackToHistory(fullTitleTrack) {
    $.post({
        url: '/history',
        data: {
            fullTrackTitle: fullTitleTrack
        },
        success: function () {
            console.log(fullTitleTrack + " - added to history");
        }
    });

}

$("#showHideHistoryTrack").on("click", function () {
    getHistory();
});

function getHistory() {
    listOfTrack = null;
    $.get({
        url: '/history',
        success: function () {
            let rawJson;

            let html = '';
            $.getJSON('/history', function (data) {
                if (data.length !== 0) {
                    historyList = data;
                    listOfTrack = data;
                    countOfTrack = data.length;

                    rawJson = data;
                    html += '<table class="table table-bordered table-hover">';
                    html += '<thead><tr><th style="border-color: white; border-style:solid;">History</th></tr></thead><tbody id="historyTBody">';
                    $.each(rawJson, function (i, track) {
                        html += '<tr><td><div class="titleOfTrackInTable">' + track + '</div>';
                        html += '</tr></td>';
                    });
                    html += '</tbody></table>';
                    $("#historyTitle").html(html);
                } else {
                    $("#showHideHistoryTrack").notify("You not play any tracks", {
                        position: 'top left',
                        className: 'error'
                    });
                }
            });
        }
    });
}

function removeTrackToHistory(fullTitleTrack) {
    $.ajax({
        type: 'DELETE',
        url: '/history',
        data: {
            fullTrackTitle: fullTitleTrack
        },
        success: function () {
            $("#showHideHistoryTrack").notify(fullTitleTrack + " deleted from your history", {
                position: 'top left',
                className: 'error'
            });
            console.log(fullTitleTrack + ", deleted from history")
        }
    });
}
