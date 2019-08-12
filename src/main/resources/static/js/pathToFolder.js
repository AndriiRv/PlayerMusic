if (getCookie("pathToFolder")) {
    $("#firstComing").css("display", "none");
} else {
    $("#firstComing").css("display", "block");
}

function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()\[\]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}