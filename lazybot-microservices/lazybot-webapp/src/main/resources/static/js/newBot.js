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
    if (nickname !== '') {
        ioClient.emit('connectBot', {nickname: nickname, password: password});
        document.getElementById("botNickname").value = '';
        document.getElementById("botPassword").value = '';
        $('#myModal').removeClass("is-active");
    }
}

function disconnectBot(botId) {
    ioClient.emit('disconnectBot', botId);
}

