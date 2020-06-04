const botManager = require('./botManager');

exports.updateBot = function (actualMissionName, actualMissionStep, bot, ioMaster) {
    console.log("> update lancée");
    ioMaster.emit("updateBot", JSON.stringify(botManager.jsonBot(actualMissionName, actualMissionStep, bot)));
};