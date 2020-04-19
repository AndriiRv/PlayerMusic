// let lyricDiv = $("#lyric");
// let bufferLyricText = null;
// let bufferSongTrack = null;
//
// let isOpenLyricDiv = true;
// $(".lyricButton").on('click', function () {
//
//     if (isOpenLyricDiv) {
//         if (bufferSongTrack === songTrack && bufferLyricText !== "") {
//             lyricDiv.css({
//                 "display": "block"
//             });
//             lyricDiv.text(bufferLyricText);
//             isOpenLyricDiv = false;
//         } else if (bufferSongTrack === songTrack && bufferLyricText === "") {
//             messageLyricNotFound();
//         } else {
//             isOpenLyricDiv = false;
//             $.post({
//                 url: '/lyric',
//                 data: {
//                     url: "https://orion.apiseeds.com/api/music/lyric/",
//                     nameOfTrack: songTrack,
//                     artistOfTrack: artistTrack
//                 },
//                 success: function (data) {
//                     $.get({
//                         url: data,
//                         success: function (json) {
//                             lyricDiv.css({
//                                 "display": "block"
//                             });
//                             bufferSongTrack = songTrack;
//                             let getJsonText = json.result.track.text;
//                             lyricDiv.text(getJsonText);
//                             bufferLyricText = getJsonText;
//                         },
//                         error: function () {
//                             isOpenLyricDiv = true;
//                             bufferSongTrack = songTrack;
//                             messageLyricNotFound();
//                         }
//                     });
//                 }
//             });
//         }
//     } else {
//         isOpenLyricDiv = true;
//         lyricDiv.css({
//             "display": "none"
//         });
//     }
// });
//
// function messageLyricNotFound() {
//     lyricDiv.css({
//         "display": "none"
//     });
//     title.notify("Lyric of track not found via API", {
//         position: 'top left',
//         className: 'error'
//     });
//     bufferLyricText = "";
// }