// let scrollPositionOnTitle;
let inputSearchTrack = $("#inputSearchTrack");

$(document).ready(function () {
    // let backToListCounter = 0;

    $(".exitFromDiv").on('click', function(){
        $.modal.close();
    });

    // $("#backToList").on('click', function () {
    //     backToListCounter++;
    //     if (backToListCounter === 1) {
    //         scrollPositionOnTitle = scrollPositionOnTitle + 1000;
    //     }
    //     if (scrollPositionOnTitle === 0) {
    //         scrollPositionOnTitle = 900;
    //     }
    //     $('html, body').animate({
    //         scrollTop: scrollPositionOnTitle
    //     }, 700);
    // });
    //
    // $("#submitFormPath").on('click', function () {
    //     $("#submitPathToFolderButton").show();
    //     $("#clearWrotePath").show();
    // });
});