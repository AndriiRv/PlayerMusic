var counterClickOnMuteButton = 0;
var currentValueVolume = 0;
var volumeSliderRange = $("#volumeSlider");
var volumeMax = $("#volumeMax");
var volumeMin = $("#volumeMin");

volumeSliderRange.on('click', function () {
    console.log("Set volume: " + $(this).val());
});

volumeSliderRange.on('change', function () {
    clearInterval(intervalVolumeBoost);
    audio.get(0).muted = false;
    audio.get(0).volume = $(this).val();
    currentValueVolume = audio.get(0).volume;
    if (audio.get(0).volume === 0) {
        volumeMax.css("background-image", "url('../images/volumeMute.svg')");
    } else if (audio.get(0).volume < 0.4) {
        volumeMax.css("background-image", "url('../images/volumeMedium.svg')");
    } else {
        volumeMax.css("background-image", "url('../images/volumeMax.svg')");
    }
});

volumeMin.on('click', function () {
    counterClickOnMuteButton++;
    if (counterClickOnMuteButton % 2 !== 0) {
        audio.get(0).muted = true;
        document.querySelector('input[type=range]').value = 0;
        audio.get(0).volume = 0;
        console.log("Volume mute");
        volumeMax.css("background-image", "url('../images/volumeMute.svg')");
    } else {
        audio.get(0).muted = false;
        if (currentValueVolume === 0) {
            currentValueVolume = 0.1;
        }
        document.querySelector('input[type=range]').value = currentValueVolume;
        audio.get(0).volume = currentValueVolume;
        console.log("Volume unmute: " + audio.get(0).volume);
        if (audio.get(0).volume === 0) {
            volumeMax.css("background-image", "url('../images/volumeMute.svg')");
        } else if (audio.get(0).volume < 0.4) {
            volumeMax.css("background-image", "url('../images/volumeMedium.svg')");
        } else {
            volumeMax.css("background-image", "url('../images/volumeMax.svg')");
        }
    }
});