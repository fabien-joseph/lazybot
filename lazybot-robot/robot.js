const io = require("socket.io-client"),
    ioMaster = io.connect("http://localhost:9090");

var botId;
var mybot = require('./business/mybot');
const mineflayer = require('mineflayer');
const navigatePlugin = require('mineflayer-navigate')(mineflayer);
var bot = mybot.connect(process.argv, process.env.BOT_USERNAME, process.env.BOT_PASSWORD);
navigatePlugin(bot);
connection();

function connection () {
    botId = Math.floor(Math.random() * (999999 - 100000) + 100000);
    ioMaster.emit('connectBot', botId);
}

bot.on('chat', function(username, message) {
    if (username === bot.username) return;
    if (message === 'chunk') {
        let blocks = mybot.loadChunkArround(bot, 10, 0, 0);
        //ioMaster.emit('loadChunk', mybot.loadChunkArround(bot, 1));
        return;
    } else if (message === 'pos') {
        console.log(bot.entity.position);
        return;
    } else if (message === 'inv') {
        console.log(bot.inventory);
        return;
    } else if (message === 'spawn') {
        bot.navigate.to(bot.players['Styleure'].entity.position);
    }
    bot.chat(message);
});

bot.on('health', function () {
    ioMaster.emit("healthChange", mybot.jsonBot(botId, bot));
});

ioMaster.on('getLoadMap', function (ray) {
    let blocks = mybot.loadChunkArround(bot, ray, 0, 0);
    ioMaster.emit("returnLoadMap", blocks);
});

ioMaster.on('test', function () {
    console.log("J'ai reçu le retour");
});

ioMaster.on('sendMessage', function (message) {
    bot.chat(message);
});