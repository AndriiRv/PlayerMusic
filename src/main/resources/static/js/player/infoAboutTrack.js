// let titleInController = $("#showInfoAboutTrack");
//
// let isOpenAboutTrackDiv = true;
// titleInController.on("click", function () {
//     listOfTrackDiv.css({
//         "display": "none"
//     });
//
//     if (isOpenAboutTrackDiv) {
//         isOpenAboutTrackDiv = false;
//         let musicTrack = null;
//
//         $('body').append('<div id="infoAboutTrackDiv"></div>');
//         let infoAboutTrackDiv = $("#infoAboutTrackDiv");
//
//         for (let i = 0; i < listOfTrack.length; i++) {
//             if (listOfTrack[i].fullTitle === fullTitle) {
//                 musicTrack = listOfTrack[i];
//             }
//         }
//
//         infoAboutTrackDiv.css({
//             "width": "50%",
//             "position": "absolute",
//             "left": "30%",
//             "top": "15%",
//             "color": "white",
//             "display": "flex",
//             "margin-left": "auto",
//             "margin-right": "auto",
//             "z-index": "1"
//         });
//         let html = '';
//         html += '<img class="picture" style="height: 500px; width: 500px" src="data:image/jpeg;charset=utf-8;base64,' + musicTrack.byteOfPicture + '">';
//         html += '<div id="wrapperInfoTrack" style="display: flex; flex-direction: column">';
//         html += '<h2>' + 'Singer: ' + musicTrack.singer + '</h2>';
//         html += '<h2>' + 'Title: ' + musicTrack.title + '</h2>';
//         html += '<h2>' + 'Length: ' + musicTrack.length + '</h2>';
//         if (musicTrack.albumTitle !== null && musicTrack.albumTitle !== '') {
//             html += '<h2>' + 'Album: ' + musicTrack.albumTitle + '</h2>';
//         }
//         if (musicTrack.year !== null && musicTrack.year !== '') {
//             html += '<h2>' + 'Year: ' + musicTrack.year + '</h2>';
//         }
//         if (musicTrack.genre !== null && musicTrack.genre !== '') {
//             html += '<h2>' + 'Genre: ' + musicTrack.genre + '</h2>';
//         }
//         html += '</div>';
//         infoAboutTrackDiv.html(html);
//     } else {
//         isOpenAboutTrackDiv = true;
//         infoAboutTrackDiv.remove();
//         listOfTrackDiv.css({
//             "display": "block"
//         });
//     }
// });