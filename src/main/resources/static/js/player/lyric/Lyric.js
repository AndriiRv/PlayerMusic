let title = $("#showInfoAboutTrack");
let lyricDiv = $("#lyric");

let Lyric = function () {
    this.singer = Lyric.prototype.getSinger();
    this.title = Lyric.prototype.getTitle();
    this.isOpenLyricDiv = true;
};

Lyric.prototype.setSinger = function (singer) {
    this.singer = singer;
};

Lyric.prototype.getSinger = function () {
    return this.singer;
};

Lyric.prototype.setTitle = function (title) {
    this.title = title;
};

Lyric.prototype.getTitle = function () {
    return this.title;
};

Lyric.prototype.setIsOpenLyricDiv = function (isOpenLyricDiv) {
    this.isOpenLyricDiv = isOpenLyricDiv;
};

Lyric.prototype.getIsOpenLyricDiv = function () {
    return this.isOpenLyricDiv;
};

Lyric.prototype.newInstance = function (lyricObj) {
    $(".lyricButton").on('click', function () {
        getLyric(lyricObj);
    });
};

function getLyric(lyricObj) {
    if (lyricObj.getIsOpenLyricDiv()) {
        $.get({
            url: "/lyric",
            data: {
                nameOfTrack: lyricObj.getTitle(),
                artistOfTrack: lyricObj.getSinger()
            },
            success: function (lyric) {
                lyricObj.setIsOpenLyricDiv(false);
                lyricDiv.css({
                    "display": "block"
                });
                lyricDiv.text(lyric);
            },
            error: function (errorMessage) {
                lyricObj.setIsOpenLyricDiv(true);
                messageLyricNotFound(errorMessage.responseText);
            }
        });
    } else {
        lyricObj.setIsOpenLyricDiv(true);
        lyricDiv.css({
            "display": "none"
        });
    }
}

function messageLyricNotFound(errorMessage) {
    lyricDiv.css({
        "display": "none"
    });
    title.notify(errorMessage, {
        position: 'top left',
        className: 'error'
    });
}