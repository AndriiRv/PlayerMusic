let counter = 0;
let addR = 107;
let addG = 1;
let addB = 125;

window.onload = function () {
    let soundWaveButton = document.getElementById("soundWaveButton");
    let audio = document.getElementById("audioId");
    soundWaveButton.onclick = function () {
        counter++;
        soundWaveButton.style.opacity = "1";
        if (counter % 2 !== 0) {
            $("#canvas").css('display', 'block');
            $("#soundWaveColorSlider").css('display', 'none');
            $("#listOfTrack").css('position', 'absolute');
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

                ctx.clearRect(0,0,WIDTH,HEIGHT);

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
            soundWaveButton.style.opacity = "0.5";
            $("#canvas").css('display', 'none');
            $("#soundWaveColorSlider").css('display', 'none');
            $("#listOfTrack").css('position', 'none');
        }
    };
};
