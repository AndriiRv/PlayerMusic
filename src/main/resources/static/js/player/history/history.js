let historyList = null;
let historyTrackButton = $("#historyTrack");

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

historyTrackButton.on("click", function () {
    openHistoryForm();

    getHistory();
    setTitleToNameOfTab("My History");
});

function getHistory() {
    listOfTrack = null;

    let html = '';
    $.getJSON('/history', function (data) {
        if (data.length !== 0) {
            historyList = data;
            listOfTrack = data;
            countOfTrack = data.length;
            $.each(data, function (i, track) {
                html += '<tr>';
                html += '<td class="commonTd" style="display: flex">';
                html += '<img id="cover" style="width: 74px; height: 74px; margin-right: 3%" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '<div class="titleOfTrackInTable">' + track.fullTitle + '</div>';
                html += '<div class="menuElement" onclick="removeTrackFromHistory(' + track.id + ')">Remove</div>';
                html += '</td>';
                html += '</tr>';
            });
            $('#mainTableTBody').html(html);
            $('#listOfTrack').removeClass("main");
        } else {
            $("#homeButton").notify("You not play any tracks", {
                position: 'bottom left',
                className: 'error'
            });
            getList(lastList);
        }
    });
}

function removeTrackFromHistory(trackId) {
    $.ajax({
        type: 'DELETE',
        url: '/history',
        data: {
            trackId: trackId
        },
        success: function (fullTitle) {
            $.notify(fullTitle + " deleted from your history", {
                position: 'top right',
                className: 'info'
            });
            console.log(fullTitle + ", deleted from history");
            getHistory();
        }
    });
}