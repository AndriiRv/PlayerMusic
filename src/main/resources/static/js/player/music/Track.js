let Track = function (fullTitle) {
    this.id = null;
    this.fullTitle = fullTitle;
    Track.prototype.parseFullTitle(fullTitle);
    // this.singer = null;
    // this.title = null;
};

Track.prototype.getId = function () {
    return this.id;
};

Track.prototype.setId = function (id) {
    this.id = id;
};

Track.prototype.getFullTitle = function () {
    return this.fullTitle;
};

Track.prototype.setFullTitle = function (fullTitle) {
    this.fullTitle = fullTitle;
};

Track.prototype.setSinger = function (singer) {
    this.singer = singer;
};

Track.prototype.getSinger = function () {
    return this.singer;
};

Track.prototype.setTitle = function (title) {
    this.title = title;
};

Track.prototype.getTitle = function () {
    return this.title;
};

Track.prototype.playMusicTrack = function () {
    audio.attr("src", "play/" + this.fullTitle);
};

Track.prototype.parseFullTitle = function (fullTitle) {
    let indexDash = fullTitle.indexOf(" - ");
    let indexAfterTitle = fullTitle.indexOf(".mp3");

    if (indexDash !== -1) {
        Track.prototype.setSinger(fullTitle.substring(0, indexDash));
        Track.prototype.setTitle(fullTitle.substring(indexDash + 3, indexAfterTitle));
    }
    return '';
};