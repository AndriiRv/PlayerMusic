let peopleButton = $("#peopleButton");

let peopleDiv = $('#peopleDiv');
let friendDiv = $('#friendDiv');

peopleButton.on("click", function () {
    openPeopleForm();

    getPeopleDiv();
    setTitleToNameOfTab("Search people");
});

function getPeopleDiv() {
    $('#listOfTrack').removeClass("main");
    getPeople();
    getFriend();
}

function getPeople() {
    $("#mainTableTBody").empty();

    let html = '';

    $.get({
        url: '/people',
        async: false,
        success: function (data) {
            if (data.length !== 0) {
                defaultList = data;
                listOfTrack = data;
                countOfTrack = data.length;

                html += '<h3>People</h3>';
                $.each(data, function (i, man) {
                    html += '<div style="display: flex">';
                    html += '<div>' + man.name + ' ' + man.surname + '</div>';
                    $.get({
                        url: '/friends/isAlready',
                        data: {
                            friendId: man.id
                        },
                        async: false,
                        success: function (data) {
                            if (data === 0) {
                                html += '<button onclick="addFriend(' + man.id + ')">Add friend</button>';
                            } else {
                                html += '<button onclick="deleteFriend(' + man.id + ')">Delete friend</button>';
                            }
                            html += '</div>';
                            $('#peopleDiv').html(html);
                            closeLoader();
                        }
                    });
                });
            } else {
                html += '<h3>Except you is nobody registered in platform</h3>';
                $('#peopleDiv').html(html);
                closeLoader();
            }
        }
    });
}

function getFriend() {
    let html = '';
    $.getJSON("/friends", function (data) {
        if (data.length !== 0) {
            defaultList = data;
            listOfTrack = data;
            countOfTrack = data.length;

            html += '<h3>My friend</h3>';
            $.each(data, function (i, man) {
                html += '<div style="display: flex">';
                html += '    <div>' + man.name + ' ' + man.surname + '</div>';
                html += '</div>';
            });
            $('#friendDiv').html(html);
            closeLoader();
        }
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
    if (confirm("Are you sure delete friend? (Messages will be removed without restore)")) {
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