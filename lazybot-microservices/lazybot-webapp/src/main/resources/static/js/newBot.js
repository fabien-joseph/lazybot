getAllBotConnected();

$("#connectBotModal").click(function () {
    $("#modalNewBot").addClass("is-active");
});

$(".modal-background").click(function () {
    closemyModal();
});

$(".closeNewBot").click(function () {
    closemyModal();
});

$("#serverAdress").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        connectBot();
    }
});

$("#botNickname").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        connectBot();
    }
});

$("#botPassword").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        connectBot();
    }
});


function closemyModal() {
    $('#modalNewBot').removeClass("is-active");
}

function connectBot() {
    let nickname = document.getElementById("botNickname").value;
    let password = document.getElementById("botPassword").value;
    let server = document.getElementById("serverAdress").value;
    if (nickname !== '') {
        ioClient.emit('connectBot', {nickname: nickname, password: password, server: server});
        document.getElementById("botNickname").value = '';
        document.getElementById("botPassword").value = '';
        document.getElementById("serverAdress").value = '';
        $('#modalNewBot').removeClass("is-active");
    }
}

function exchange() {
    ioClient.emit('exchange');
}

function disconnectBot(target) {
    let targetName = target != null ? target : getBotUsername();
    ioClient.emit('disconnectBot', JSON.stringify({botUsername: targetName}));
}

function getAllBotConnected() {
    ioClient.emit("getAllBotConnected");
}

ioClient.on('allBotConnected', function (botUsernamesJson) {
    let botUsernames = JSON.parse(botUsernamesJson);
    botUsernames.sort();
    $('.botNav').remove();
    for (let i = 0; i < botUsernames.length; i++) {
        addBotConnected(botUsernames[i]);
    }
});

function addBotConnected(botUsername) {
    $("#accountsRobot").prepend("<li class='botNav'><a href='/" + botUsername + "'>" + botUsername + "</a></li>");
}