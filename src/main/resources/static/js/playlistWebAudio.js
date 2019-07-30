var titleOfTrack = null;
var rowIndex = 0;
var rowCount = $('table tr').length - 1;
$('table tr').click(function () {
    $(".nextPrevButtons").show();
    if (rowIndex <= 0) {
        $("#prevTrackId").hide();
    }
    var indexIteration = $(this).index();
    rowIndex = indexIteration;
    var currentTrack = $('#titleOfTrack');
    $(".audioClass").bind("ended", function () {
        if (rowIndex >= 0) {
            $("#prevTrackId").show();
        }
        currentTrack.empty();
        indexIteration = indexIteration + 1;
        rowIndex = indexIteration;
        titleOfTrack = $(".titleOfTrackInTable").eq(rowIndex);
        $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
        currentTrack.append(titleOfTrack.text());
        if (rowIndex >= rowCount - 1) {
            $("#nextTrackId").hide();
        }
    });
});

$("#nextTrackId").on("click", function () {
    $("#play").hide();
    if (rowIndex >= 0) {
        $("#prevTrackId").show();
    }
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    rowIndex++;
    titleOfTrack = $(".titleOfTrackInTable").eq(rowIndex);
    $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
    currentTrack.append(titleOfTrack.text());
    if (rowIndex >= rowCount - 1) {
        $("#nextTrackId").hide();
    }
});

$("#prevTrackId").on("click", function () {
    $("#play").hide();
    $("#nextTrackId").show();
    if (rowIndex <= 1) {
        $("#prevTrackId").hide();
    }
    var currentTrack = $('#titleOfTrack');
    currentTrack.empty();
    rowIndex--;
    titleOfTrack = $(".titleOfTrackInTable").eq(rowIndex);
    $(".audioClass").attr("src", "playInBrowser/" + titleOfTrack.text());
    currentTrack.append(titleOfTrack.text());
    if (rowIndex >= rowCount - 1) {
        $("#nextTrackId").hide();
    }
});