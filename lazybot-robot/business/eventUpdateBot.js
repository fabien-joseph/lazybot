const botManager = require('./botManager');

exports.updateBot = function (eventName, actualMissionId, bot, ioMaster) {
    console.log("> " + eventName + " lancé");
    ioMaster.emit(eventName, JSON.stringify(botManager.jsonBot(actualMissionId, bot)));
};