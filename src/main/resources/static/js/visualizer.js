var counter = 0;

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
                ctx.fillStyle = ctx.createPattern(image, "no-repeat");
                ctx.rect(0, 0, WIDTH, HEIGHT);
                ctx.fillRect(0, 0, WIDTH, HEIGHT);

                for (var i = 0; i < bufferLength; i++) {
                    barHeight = dataArray[i];

                    var r = barHeight + (25 * (i / bufferLength));
                    var g = 250 * (i / bufferLength);
                    var b = 50;

                    ctx.fillStyle = "rgb(" + r + "," + g + "," + b + ")";
                    ctx.fillRect(x, HEIGHT - barHeight, barWidth, barHeight);
                    ctx.rect(1200, 100, 1200, 100);
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