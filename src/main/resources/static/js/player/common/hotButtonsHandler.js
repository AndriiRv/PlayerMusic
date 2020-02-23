// let counterForPause = 0;
// let counterFocus;
//
// titleOfTrackInTable.on('click', function () {
//     counterFocus = false;
// });
//
// inputSearchTrack.focus(function () {
//     counterFocus = true;
// });
//
// inputSearchTrack.focusout(function () {
//     counterFocus = false;
// });
//
// document.addEventListener("keydown", function (event) {
//     if (counterFocus === false) {
//
//         let keyButton = event.code;
//
//         if (keyButton === "Space") {
//             event.preventDefault();
//             counterForPause++;
//             if (counterForPause % 2 !== 0) {
//                 audio.get(0).pause();
//                 playButton.show();
//                 pauseButton.hide();
//                 hideCurrentTimeTrack();
//                 $("#icon").attr("href", "/images/iconPause.ico");
//             } else {
//                 audio.get(0).play();
//                 playButton.hide();
//                 pauseButton.show();
//
//                 showCurrentTimeTrack();
//
//                 $("#icon").attr("href", "/images/iconPlay.ico");
//             }
//         }
//
//         if (keyButton === "ArrowRight") {
//             audio.get(0).currentTime = audio.get(0).currentTime + 5;
//         } else if (keyButton === "ArrowLeft") {
//             audio.get(0).currentTime = audio.get(0).currentTime - 5;
//         }
//     }
// });
