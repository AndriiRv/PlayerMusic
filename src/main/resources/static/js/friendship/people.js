let peopleButton = $("#peopleButton");

peopleButton.on("click", function () {
    openPeopleForm();

    getPeopleDiv();
    setTitleToNameOfTab("Search people");
});

function getPeopleDiv() {
    getPeople();
    getFriend();
}

function getPeople() {
    let html = '';

    $.get({
        url: '/people',
        async: false,
        success: function (data) {
            if (data.length !== 0) {
                // defaultList = data;
                listOfTrack = data;
                countOfTrack = data.length;

                html += '<div id="peopleAndFriends" style="display: flex; width: 1175px;">';
                html += '   <div id="playlistList" style="flex-basis: 30%; margin-left: 15px;">';
                html += '   <h3>People</h3>';
                html += '   <table>';
                html += '   <thead></thead>';
                html += '   <tbody>';
                $.each(data, function (i, man) {
                    html += '   <tr>';
                    html += '       <td><div>' + man.name + ' ' + man.surname + '</div></td>';
                    $.get({
                        url: '/friends/isAlready',
                        data: {
                            friendId: man.id
                        },
                        async: false,
                        success: function (data) {
                            if (data === 0) {
                                html += '       <td><button onclick="addFriend(' + man.id + ')">Add friend</button></td>';
                            } else {
                                html += '       <td><button onclick="deleteFriend(' + man.id + ')">Delete friend</button></td>';
                            }
                        }
                    });
                    html += '   </tr>';
                });
                html += '   </tbody></table>';
                html += '   </div>';
                html += '<div id="friends"></div>';
                html += '</div>';
                supportDashboard.html(html);
                closeLoader();
            } else {
                html += '<div style="width: 1175px;">';
                html += '   <h3>Except you is nobody registered in platform</h3>';
                html += '</div>';
                supportDashboard.html(html);
                closeLoader();
            }
        }
    });
}

function getFriend() {
    let html = '';
    $.getJSON("/friends", function (data) {
        if (data.length !== 0) {
            // defaultList = data;
            listOfTrack = data;
            countOfTrack = data.length;

            html += '<h3>My friends</h3>';
            $.each(data, function (i, man) {
                html += '<div style="margin-bottom: 10px;">' + man.name + ' ' + man.surname + '</div>';
            });
            $("#friends").html(html);
        }
        closeLoader();
    });
}

function addFriend(userId) {
    $.post({
        url: '/friends',
        data: {
            possibleFriendId: userId
        },
        success: function () {
            getPeopleDiv();
            getFriend();
        }
    });
}

function deleteFriend(userId) {
    if (confirm("Are you sure delete friend? (Messages between your are will be removed without restore)")) {
        $.ajax({
            type: 'DELETE',
            url: '/friends',
            data: {
                secondUserId: userId
            },
            success: function () {
                getPeopleDiv();
                getFriend();
            }
        });
    }
}