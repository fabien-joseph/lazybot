getAllBotConnected();

$("#connectBotModal").click(function () {
    $("#myModal").addClass("is-active");
});

$(".modal-background").click(function () {
    closeModalNewBot();
});

$(".closeNewBot").click(function () {
    closeModalNewBot();
});

function closeModalNewBot() {
    $('#myModal').removeClass("is-active");
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
        $('#myModal').removeClass("is-active");
    }
}

function disconnectBot(botId) {
    ioClient.emit('disconnectBot', botId);
}

function getAllBotConnected () {
    ioClient.emit("getAllBotConnected");
}

ioClient.on('allBotConnected', function (botUsernamesJson) {
    let botUsernames = JSON.parse(botUsernamesJson);
    console.log("BEFORE : " + botUsernames);
    botUsernames.sort();
    console.log("AFTER : " + botUsernames);
    $('.botNav').remove();
   for (let i = 0; i < botUsernames.length; i++) {
       addBotConnected(botUsernames[i]);
   }
});

function addBotConnected(botUsername) {
    $("#accountsRobot").prepend("<li class='botNav'><a href='/" + botUsername + "'>" + botUsername + "</a></li>");
}