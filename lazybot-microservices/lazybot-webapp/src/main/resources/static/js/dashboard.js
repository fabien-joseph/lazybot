getMissionCounts();
showCountBotConnected();

ioClient.on("updateTotalMissionDoneWebapp", function (count) {
    console.log("done : " + count);
    $('#counterMissionsSuccess').text(count);
});

ioClient.on("updateTotalMissionFailWebapp", function (count) {
    console.log("fail : " + count);
    $('#counterMissionsFail').text(count);
});

ioClient.on('allBotConnected', function (botUsernamesJson) {
    let botsConnected = JSON.parse(botUsernamesJson);
    showCountBotConnected(botsConnected);
});

function showCountBotConnected(botsConnected) {
    let showBotsConneted = $('#countBotsConnected');
    if (botsConnected == null) {
        showBotsConneted.text(0);
        return;
    }
    showBotsConneted.text(botsConnected.length);
}

function getMissionCounts() {
    ioClient.emit("getMissionCounts");
}