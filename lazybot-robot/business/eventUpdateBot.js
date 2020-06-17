const botManager = require('./botManager');

exports.updateBot = function (eventName, actualMissionId, bot, ioMaster) {
    ioMaster.emit(eventName, JSON.stringify(botManager.jsonBot(actualMissionId, bot)));
};

exports.missionDoneOrFail = function (eventName, actualMissionId, ioMaster) {
    ioMaster.emit(eventName, JSON.stringify(actualMissionId));
};