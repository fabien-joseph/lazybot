const botManager = require('./mybot');

exports.updateBot = function (botId, bot, ioMaster) {
    ioMaster.emit("updateBot", JSON.stringify(botManager.jsonBot(botId, bot)));
};