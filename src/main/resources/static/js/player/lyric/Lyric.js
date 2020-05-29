let title = $("#showInfoAboutTrack");
let lyricDiv = $("#lyric");

class Lyric {
    constructor() {
        this.isOpenLyricDiv = true;
    }

    setSinger = function (singer) {
        this.singer = singer;
    };

    getSinger = function () {
        return this.singer;
    };

    setTitle = function (title) {
        this.title = title;
    };

    getTitle = function () {
        return this.title;
    };

    setIsOpenLyricDiv = function (isOpenLyricDiv) {
        this.isOpenLyricDiv = isOpenLyricDiv;
    };

    getIsOpenLyricDiv = function () {
        return this.isOpenLyricDiv;
    };
}

$(".lyricButton").on('click', function () {
    getLyric(lyricObj);
});

getLyric = function (lyricObj) {
    if (currentUserUsername.text() !== "") {
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
                    lyricDiv.text(replaceAll(lyric, "<br/>", "\n"));
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
    } else {
        getSignModalWindow();
    }
};

messageLyricNotFound = function (errorMessage) {
    lyricDiv.css({
        "display": "none"
    });
    title.notify(errorMessage, {
        position: 'top left',
        className: 'error'
    });
};

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(find, 'g'), replace);
}