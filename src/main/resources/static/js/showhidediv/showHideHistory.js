let showHideHistoryTrack = $("#showHideHistoryTrack");
let historyDiv = $("#historyTable");

function showHistory() {
    historyDiv.css('display', 'block');
    historyDiv.attr("class", "showHistoryTable table table-bordered table-hover");
}

function hideHistory() {
    historyDiv.attr("class", "hideHistoryTable table table-bordered table-hover");

    setTimeout(() => {
        historyDiv.css('display', 'none');
    }, 500);
}

$(document).ready(function () {
    let counter = 0;

    showHideHistoryTrack.on('click', function () {
        counter++;
        if (counter % 2 !== 0) {
            showHistory();
        } else {
            hideHistory();
        }
    });
});
