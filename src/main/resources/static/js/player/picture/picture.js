let body = $("body");

body.css({
    "background-image": "url('../images/player.jpg')"
});

function getPicture(srcCoverTrack) {
    $(".picture").attr("src", srcCoverTrack);
    body.css({
        'background': 'linear-gradient(rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.8)),url(' + srcCoverTrack + ') no-repeat center center fixed',
        'background-attachment': 'fixed',
        'background-size': 'cover'
    });
}