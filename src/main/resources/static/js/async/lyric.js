let counterForLyricShow = 0;

$(".lyricButton").on('click', function () {
    counterForLyricShow++;
    if (counterForLyricShow % 2 !== 0) {
        $.post({
            url: '/lyric',
            data: {
                url: "https://orion.apiseeds.com/api/music/lyric/",
                nameOfTrack: songTrack,
                artistOfTrack: artistTrack,
                apiKey: "mVoBkkq8qCPSyOdCG8tNrZ2jvbs0EU5mzSrT2KqTzR9yExymX1qoakiEybGSH5RC"
            },
            success: function (data) {
                $.get({
                    url: data,
                    success: function (json) {
                        $("#lyric").css({
                            "display": "block"
                        });
                        $("#lyric").text(json.result.track.text);
                        console.log(json.result.track.text);
                        console.log(json.result.artist.name + " - " + json.result.track.name + " get from APISeeds Lyrics");
                    },
                    error: function () {
                        counterForLyricShow = 0;
                        $("#lyric").css({
                            "display": "none"
                        });
                        title.notify("Lyric of track not found via API", {
                            position: 'top left',
                            className: 'error'
                        });
                        console.log("Lyric of track not found via API");
                    }
                });
            }
        });
    } else {
        $("#lyric").css({
            "display": "none"
        });
    }
});
