let download = $(".downloadButton");

download.on('click', function () {
    if (currentUserUsername.text() !== "") {
        window.location.href = 'download/' + hrefTitleForDownload;
        console.log("Download: " + hrefTitleForDownload);
    } else {
        getSignModalWindow();
    }
});
