let descriptionCookieText = "We use cookies to enhance your experience on our site. By accepting our cookie policy you\n" +
    "    agreeing to our use of\n" +
    "    cookies.";

let acceptCookieDiv = $("#acceptCookieDiv");
let descriptionCookie = $("#descriptionCookie");
descriptionCookie.text(descriptionCookieText);

let acceptCookieButton = $("#acceptCookieButton");
let declineCookieButton = $("#declineCookieButton");

if (!getCookie("acceptedCookie")) {
    acceptCookieDiv.show();
} else {
    acceptCookieDiv.hide();
}

acceptCookieButton.on('click', function () {
    $("#acceptCookieDiv").hide();
    let getDateForCookie = new Date(Date.now() + 31536000e3);
    getDateForCookie = getDateForCookie.toUTCString();
    document.cookie = "acceptedCookie=true; expires=" + getDateForCookie;
});

declineCookieButton.on('click', function () {
    let cookies = document.cookie.split(";");
    for (let i = 0; i < cookies.length; i++) {
        let splitCookies = cookies[i].split("=");
        document.cookie = splitCookies[0] + "=;expires=Thu, 21 Sep 1979 00:00:01 UTC;";
    }
    acceptCookieDiv.hide();
});

function getCookie(name) {
    let matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([.$?*|{}()\[\]\\\/+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}