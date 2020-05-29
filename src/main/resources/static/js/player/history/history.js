let historyList = null;
let historyTrackButton = $("#historyTrack");

function addTrackToHistory(fullTitleTrack) {
    if (currentUserUsername.text() !== "") {
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
}

historyTrackButton.on("click", function () {
    openHistoryForm();

    getHistory();
    setTitleToNameOfTab("My History");
});

function getHistory() {
    getLoader();
    listOfTrack = null;

    let html = '';
    $.getJSON('/history', function (data) {
        if (data.length !== 0) {
            historyList = data;
            listOfTrack = data;
            countOfTrack = data.length;
            html += '<h2>History</h2>';
            html += '<div style="display: grid; grid-template-columns: 200px 200px 200px 200px 200px 175px; grid-auto-rows: 250px;">';
            $.each(data, function (i, track) {
                html += '<div style="max-width: 173px;">';
                html += '   <div id="musicId" hidden>' + track.id + '</div>';
                html += '   <img id="cover" style="width: 173px; height: 173px;" src="data:image/jpeg;charset=utf-8;base64,' + track.byteOfPicture + '"/>';
                html += '   <div class="titleOfTrackInTable" style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden; height: auto">' + track.fullTitle + '</div>';
                html += '   <div class="menuElement" onclick="removeTrackFromHistory(' + track.id + ')">Remove</div>';
                html += '</div>'
            });
            supportDashboard.html(html);
            closeLoader();
        } else {
            closeLoader();
            $.notify("You not play any tracks", {
                position: 'top left',
                className: 'error'
            });
            getList(lastUrl);
        }
    });
}

function removeTrackFromHistory(trackId) {
    if (currentUserUsername.text() !== "") {
        $.ajax({
            type: 'DELETE',
            url: '/history',
            data: {
                trackId: trackId
            },
            success: function (fullTitle) {
                $.notify(fullTitle + " deleted from your history", {
                    position: 'top left',
                    className: 'info'
                });
                console.log(fullTitle + ", deleted from history");
                getHistory();
            }
        });
    }
}