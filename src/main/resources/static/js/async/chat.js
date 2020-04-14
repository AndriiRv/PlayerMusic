let chatButton = $("#chatButton");
let messageDiv = $("#messageDiv");

let isOpenChat = true;
chatButton.on("click", function () {
    if (isOpenChat) {
        getChats();
        setTitleToNameOfTab("Select chat");
        isOpenChat = false;
    } else {
        messageDiv.css({
            "display": "none"
        });
        $("#chat").empty();
        $("#message").empty();
        isOpenChat = true;
    }
});

function getChats() {
    let html = '';
    $.get({
        url: "/chat",
        success: function (data) {
            if (data.length >= 1) {

                messageDiv.css({
                    "display": "flex"
                });

                $.each(data, function (i, chat) {
                    html += '<div style="display: block">';
                    html += '   <div id="chatId" hidden>' + chat.id + '</div>';
                    html += '   <div style="cursor:pointer; text-decoration: underline;" onclick="setChatId(\'' + chat.id + '\')">' + chat.title + '</div>';
                    html += '</div>';
                });
                $("#chat").html(html);
            } else {
                $.notify("You're haven't any friends", {
                    position: 'top right',
                    className: 'error'
                });
            }
        }
    });
}

let globalChatId = null;

function setChatId(chatId) {
    globalChatId = chatId;
    getMessages();
}

function getMessages() {
    let html = '';
    $.get({
        url: "/message",
        data: {
            chatId: globalChatId
        },
        success: function (data) {
            $.each(data, function (i, message) {
                html += '<div id="friendAndDatetime" style="display: flex;">';
                html += '   <div hidden>' + message.id + '</div>';
                html += '   <div id="friendName" style="display: flex; flex-basis: 90%;">';
                html += '       <div>' + message.name + ' ' + message.surname + ' (' + message.datetime + ')' + '</div>';
                html += '   </div>';
                html += '</div>';
                html += '<div style="overflow-wrap: break-word; background-color: rebeccapurple;">' + message.message + '</div>';
            });
            html += '<div id="sendMessageDiv">';
            html += '   <input id="messageText" style="width: 100%;" type="text" placeholder="Write a message..."/>';
            html += '   <div class="menuElement" id="sendMessageButton" onclick="sendMessage()">SEND</div>';
            html += '</div>';
            $("#message").html(html);
            $("#messageText")[0].scrollIntoView();
        }
    });
}

function sendMessage() {
    let messageText = $("#messageText").val();
    if (messageText !== '') {
        $.post({
            url: "/message",
            data: {
                chatId: globalChatId,
                message: messageText
            },
            success: function () {
                getMessages();
            }
        });
    }
}