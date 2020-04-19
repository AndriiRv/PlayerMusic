let currentUserName = $("#currentUserName");
let dropdownContent = $(".dropdown-content");

let currentUserUsername = $("#currentUserUsername");
let currentUserSurname = $("#currentUserSurname");
let currentUserEmail = $("#currentUserEmail");

let counterMenu = 0;

currentUserUsername.on("click", function () {
    counterMenu = 0;
    if (counterMenu === 0) {
        dropdownContent.css({
            "height": "100%",
            "color": "orange",
            "display": "block",
            "box-shadow": "0 8px 16px 0 rgba(0, 0, 0, 0.2)"
        });
        counterMenu = 0;
    }
});

$(document).on("click", function () {
    if (counterMenu === 1) {
        dropdownContent.css({
            "height": "100%",
            "color": "orange",
            "display": "none",
            "box-shadow": "0 8px 16px 0 rgba(0, 0, 0, 0.2)"
        });
    }
    counterMenu = 1;
});