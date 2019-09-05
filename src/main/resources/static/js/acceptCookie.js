if (!getCookie("acceptedCookie")) {
    $("#acceptCookie").show();
} else {
    $("#acceptCookie").hide();
}

$("#acceptCookieButton").on('click', function () {
    $("#acceptCookie").hide();
    var getDateForCookie = new Date(Date.now() + 31536000e3);
    getDateForCookie = getDateForCookie.toUTCString();
    document.cookie = "acceptedCookie=true; expires=" + getDateForCookie;
});

$("#declineCookieButton").on('click', function () {
    var cookies = document.cookie.split(";");
    for (var i = 0; i < cookies.length; i++) {
        var splitCookies = cookies[i].split("=");
        document.cookie = splitCookies[0] + "=;expires=Thu, 21 Sep 1979 00:00:01 UTC;";
    }
    $("#acceptCookie").hide();
});

function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()\[\]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}