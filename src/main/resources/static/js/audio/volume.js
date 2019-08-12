var counter = 0;
$("#volumeSlider").on('click', function () {
    console.log("Set volume: " + $(this).val());
});
$("#volumeSlider").on('change', function () {
    audio.get(0).muted = false;
    audio.get(0).volume = $(this).val();
    if (audio.get(0).volume === 0) {
        $("#volumeMax").css("background-image", "url('../images/volumeMute.svg')");
    } else if (audio.get(0).volume < 0.4) {
        $("#volumeMax").css("background-image", "url('../images/volumeMedium.svg')");
    } else {
        $("#volumeMax").css("background-image", "url('../images/volumeMax.svg')");
    }
});

$("#volumeMin").on('click', function () {
    counter++;
    if (counter % 2 !== 0) {
        audio.get(0).muted = true;
        document.querySelector('input[type=range]').value = 0;
        audio.get(0).volume = 0;
        console.log("Volume mute");
        $("#volumeMax").css("background-image", "url('../images/volumeMute.svg')");
    } else {
        audio.get(0).muted = false;
        document.querySelector('input[type=range]').value = 0.5;
        audio.get(0).volume = 0.5;
        console.log("Volume unmute: " + audio.get(0).volume);
        $("#volumeMax").css("background-image", "url('../images/volumeMax.svg')");
    }
});