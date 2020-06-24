let isVisualizerTurnOff = true;

$(document).ready(function () {
    let soundWaveButton = $("#soundWaveButton");
    let audio = document.getElementById("audioId");
    soundWaveButton.on('click', function () {
        soundWaveButton.css("opacity", "1");
        if (isVisualizerTurnOff) {
            isVisualizerTurnOff = false;

            let addR = getRandomInt(255);
            let addG = getRandomInt(255);
            let addB = getRandomInt(255);

            $("#canvas").css('display', 'block');
            let context = new AudioContext();
            let src = context.createMediaElementSource(audio);
            let analyser = context.createAnalyser();

            let canvas = document.getElementById("canvas");
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            let ctx = canvas.getContext("2d");

            src.connect(analyser);
            analyser.connect(context.destination);

            analyser.fftSize = 256;

            let bufferLength = analyser.frequencyBinCount;

            let dataArray = new Uint8Array(bufferLength);

            let WIDTH = canvas.width;
            let HEIGHT = canvas.height;

            let barWidth = (WIDTH / bufferLength) * 2.5;
            let barHeight;
            let x = 0;

            function renderFrame() {
                requestAnimationFrame(renderFrame);
                x = 0;
                analyser.getByteFrequencyData(dataArray);

                ctx.clearRect(0, 0, WIDTH, HEIGHT);

                for (let i = 0; i < bufferLength; i++) {
                    barHeight = dataArray[i];

                    let r = barHeight + (addR * (i / bufferLength));
                    let g = addG * (i / bufferLength);

                    ctx.fillStyle = "rgb(" + r + "," + g + "," + addB + ")";
                    ctx.fillRect(x, HEIGHT - barHeight * 3, barWidth, barHeight * 6);

                    x += barWidth + 1;
                }
            }

            renderFrame();
        } else {
            isVisualizerTurnOff = true;
            soundWaveButton.css("opacity", "0.5");
            $("#canvas").css('display', 'none');
        }
    });
});

function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}