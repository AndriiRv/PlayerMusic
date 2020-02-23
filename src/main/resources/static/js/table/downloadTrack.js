let download = $(".downloadButton");

download.on('click', function () {
    window.location.href = 'download/' + hrefTitleForDownload;
    console.log("Download: " + hrefTitleForDownload);
});
