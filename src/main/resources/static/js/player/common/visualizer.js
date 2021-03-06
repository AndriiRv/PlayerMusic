var counter = 0;
var addR = 25;
var addG = 250;
var addB = 50;
$("#soundWaveColorSlider").on('click', function () {
    addR = Math.round(Math.random() * 144) + 1;
    addG = Math.round(Math.random() * 250) + 1;
    addB = Math.round(Math.random() * 64) + 1;
});

window.onload = function () {
    var soundWaveButton = document.getElementById("soundWaveButton");
    var audio = document.getElementById("audioId");
    soundWaveButton.onclick = function () {
        counter++;
        soundWaveButton.style.opacity = "1";
        if (counter % 2 !== 0) {
            $("#canvas").css('display', 'block');
            $("#soundWaveColorSlider").css('display', 'none');
            var context = new AudioContext();
            var src = context.createMediaElementSource(audio);
            var analyser = context.createAnalyser();

            var canvas = document.getElementById("canvas");
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            var ctx = canvas.getContext("2d");

            src.connect(analyser);
            analyser.connect(context.destination);

            analyser.fftSize = 256;

            var bufferLength = analyser.frequencyBinCount;

            var dataArray = new Uint8Array(bufferLength);

            var WIDTH = canvas.width;
            var HEIGHT = canvas.height;

            var barWidth = (WIDTH / bufferLength) * 2.5;
            var barHeight;
            var x = 0;

            function renderFrame() {
                requestAnimationFrame(renderFrame);
                x = 0;
                analyser.getByteFrequencyData(dataArray);

                var image = new Image();
                image.src = "../images/player.jpg";
                ctx.fillStyle = ctx.createPattern(image, "repeat");

                ctx.fillRect(0, 0, WIDTH, HEIGHT);
                for (var i = 0; i < bufferLength; i++) {
                    barHeight = dataArray[i];

                    var r = barHeight + (addR * (i / bufferLength));
                    var g = addG * (i / bufferLength);
                    var b = addB;

                    ctx.fillStyle = "rgb(" + r + "," + g + "," + b + ")";
                    ctx.fillRect(x, HEIGHT - barHeight, barWidth, barHeight);

                    x += barWidth + 1;
                }
            }

            renderFrame();
        } else {
            soundWaveButton.style.opacity = "0.5";
            $("#canvas").css('display', 'none');
            $("#soundWaveColorSlider").css('display', 'none');
        }
    };
};