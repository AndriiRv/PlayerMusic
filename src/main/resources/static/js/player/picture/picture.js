let body = $("body");

body.css({
    "background-image": "url('../images/player.jpg')"
});

function getPictureByTrackId(musicTrackId) {
    $.get({
        url: '/picture',
        data: {
            musicId: musicTrackId
        },
        success: function (data) {
            getPicture(data.byteOfPicture);
        }
    });
}

function getPicture(srcCoverTrack) {
    let src = "data:image/jpeg;charset=utf-8;base64," + srcCoverTrack;
    $(".picture").attr("src", src);
    body.css({
        'background': 'linear-gradient(rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.8)),url(' + src + ') no-repeat center center fixed',
        'background-attachment': 'fixed',
        'background-size': 'cover'
    });
}