const botManager = require('./botManager');

exports.updateBot = function (botId, bot, ioMaster) {
    console.log("> update lancée");
    ioMaster.emit("updateBot", JSON.stringify(botManager.jsonBot(botId, bot)));
};