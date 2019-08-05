$("#volumeSlider").change(function () {
    audio.get(0).muted = false;
    audio.get(0).volume = $(this).val();
    if (audio.get(0).volume === 0) {
        $("#volumeMax").css("background-image", "url('../images/volumeMute.svg')");
    } else {
        $("#volumeMax").css("background-image", "url('../images/volumeMax.svg')");
    }
});

$("#volumeMin").on('click', function () {
    audio.get(0).muted = true;
    $("#volumeMin").hide();
    $("#volumeMute").show();
    $("#volumeMax").css("background-image", "url('../images/volumeMute.svg')");
});

$("#volumeMute").on('click', function () {
    audio.get(0).muted = false;
    $("#volumeMute").hide();
    $("#volumeMin").show();
    $("#volumeMax").css("background-image", "url('../images/volumeMax.svg')");
});