const io = require("socket.io-client"),
    ioMaster = io.connect("http://localhost:9090");

const mineflayer = require('mineflayer');
const v = require('vec3');
const navigatePlugin = require('mineflayer-navigate')(mineflayer);
const botManager = require('./business/botManager');
const eventChat = require("./business/eventTchat");
const eventUpdateBot = require("./business/eventUpdateBot");

// LOCAL VARIABLES
let lastX = 0;
let lastZ = 0;
let lastY = 0;

let actualMissionId;

// === BOT INITIALIZATION ===
let bot = botManager.connect(process.argv);
navigatePlugin(bot);
connectionMSMaster();

function connectionMSMaster() {
    console.log(bot.username);
    ioMaster.emit('registerBot', bot.username);
}

// === CHAT CONTROL ===
bot.on('chat', function (username, message) {
    eventChat.chatControl(bot, username, message);
});

// === EVENT UPDATE THE BOT
bot.on('health', function () {
    console.log("health");
    eventUpdateBot.updateBot("updateBot", actualMissionId, bot, ioMaster);
});

bot.on('entityMoved', (entity) => {
    // bot.lookAt(entity.position, true, null);
    //console.log(bot.entity.yaw);
})

bot.on('move', function () {
    if (Math.floor(bot.entity.position.x) !== lastX || Math.floor(bot.entity.position.y) !== lastY || Math.floor(bot.entity.position.z) !== lastZ) {
        console.log("move");
        eventUpdateBot.updateBot("updateBot", actualMissionId, bot, ioMaster);
    }
    lastX = Math.floor(bot.entity.position.x);
    lastY = Math.floor(bot.entity.position.y);
    lastZ = Math.floor(bot.entity.position.z);
});

bot.on('playerCollect', function () {
    eventUpdateBot.updateBot("updateBot", actualMissionId, bot, ioMaster);
});

// === Events mission done ===
bot.navigate.on('arrived', function () {
    console.log("MissionID envoyé : " + actualMissionId);
    eventUpdateBot.missionDoneOrFail("missionDone", actualMissionId, ioMaster);

});

// === Events mission fail ===
bot.navigate.on('cannotFind', function () {
    eventUpdateBot.missionDoneOrFail("missionFail", actualMissionId, ioMaster);

});

bot.navigate.on('interrupted', function () {
    eventUpdateBot.missionDoneOrFail("missionFail", actualMissionId, ioMaster);

});


// === MASTER MS REQUESTS
ioMaster.on('missionStatus', function (missionId) {
    actualMissionId = missionId;
    console.log("Mission ID reçu " + actualMissionId);
})

ioMaster.on('getLoadMap', function (ray) {
    let blocks = botManager.loadChunkArround(bot, ray, 0, 0);
    ioMaster.emit("returnLoadMap", blocks);
});

ioMaster.on('drop', function (itemsJson) {
    let items = JSON.parse(itemsJson);
    for (let i = 0; i < items.length; i++) {
        let type = Math.round(items[i].type);
        let metadata = Math.round(items[i].metadata);
        let count = Math.round(items[i].count);
        sleep(200);
        bot.toss(type, metadata, count, null);
    }
    if (actualMissionId != null)
        eventUpdateBot.missionDoneOrFail("missionDone", actualMissionId, ioMaster);

});

ioMaster.on('test', function () {
    console.log("J'ai reçu le retour");
});

ioMaster.on('sendMessage', function (message) {
    bot.chat(JSON.parse(message));
});

ioMaster.on('goToPos', function (positionJson) {
    let position = (JSON.parse(positionJson));
    if (position.x != null && position.y != null && position.z != null) {
        let positionToGo = v(position.x, position.y, position.z);
        bot.navigate.to(positionToGo);
    } else {
        if (actualMissionId != null) {
            eventUpdateBot.missionDoneOrFail("missionFail", actualMissionId, ioMaster);
        }
    }
});

ioMaster.on('look', function (lookJson) {
    let look = JSON.parse(lookJson);
    let yaw = look.yaw * 3.2 / 180;
    let pitch = look.pitch * 3.2 / 180;
    bot.look(yaw, pitch, true, null);
    if (actualMissionId !== null)
        eventUpdateBot.missionDoneOrFail("missionDone", actualMissionId, ioMaster);
});

ioMaster.on('getUpdateBot', function () {
    eventUpdateBot.updateBot("updateBot", actualMissionId, bot, ioMaster);
});

ioMaster.on('connectionSuccess', function () {
    console.log("Connection effectuée !");
});

ioMaster.on('exit', function () {
    exit();
});

// === Events bot stops ===
ioMaster.on('quit', function () {
    exit()
});
process.on('SIGINT', function () {
    exit();
});
process.on('SIGUSR1', function () {
    exit();
});
process.on('SIGUSR2', function () {
    exit();
});

// Function bot stops
function exit() {
    process.exit();
}

// Function to execute when the bot stops
process.on('exit', function () {
    ioMaster.emit("unregisterBot", bot.username);
});

function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
}