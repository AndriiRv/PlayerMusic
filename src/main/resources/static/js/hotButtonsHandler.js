var counter = 0;
document.addEventListener("keydown", function (event) {
    var keyButton = event.code;

    if (keyButton === "Space") {
        event.preventDefault();
        counter++;
        if (counter % 2 !== 0) {
            audio.get(0).pause();
            playButton.show();
            pauseButton.hide();
            $("#icon").attr("href", "/images/iconPause.ico");
        } else {
            audio.get(0).play();
            playButton.hide();
            pauseButton.show();
            $("#icon").attr("href", "/images/iconPlay.ico");
        }
    }

    if (keyButton === "ArrowRight") {
        audio.get(0).currentTime = audio.get(0).currentTime + 5;
    } else if (keyButton === "ArrowLeft") {
        audio.get(0).currentTime = audio.get(0).currentTime - 5;
    }
});