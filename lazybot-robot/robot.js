const io = require("socket.io-client"),
    ioMaster = io.connect("http://localhost:9090");

var botId;
var mybot = require('./business/mybot');
const mineflayer = require('mineflayer');
const v = require('vec3');
const navigatePlugin = require('mineflayer-navigate')(mineflayer);
var bot = mybot.connect(process.argv, process.env.BOT_USERNAME, process.env.BOT_PASSWORD);
navigatePlugin(bot);
connection();

function connection() {
    botId = Math.floor(Math.random() * (999999 - 100000) + 100000);
    ioMaster.emit('connectBot', botId);
}

bot.on('chat', function (username, message) {
    if (username === bot.username) return;
    if (message === 'chunk') {
        let blocks = mybot.loadChunkArround(bot, 10, 0, 0);
        //ioMaster.emit('loadChunk', mybot.loadChunkArround(bot, 1));
        return;
    } else if (message === 'pos') {
        console.log(bot.entity.position);
        return;
    } else if (message === 'inv') {
        console.log(bot.inventory.slots[36].nbt);
        return;
    } else if (message === 'spawn') {
        const pos = bot.players['Styleure'].entity.position;
        bot.navigate.to(pos);

        console.log(bot.players['Styleure'].entity.position);
    }
    bot.chat(message);
});

bot.on('health', function () {
    //console.log(JSON.stringify(mybot.jsonBot(botId, bot)));
    ioMaster.emit("healthChange", JSON.stringify(mybot.jsonBot(botId, bot)));
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

ioMaster.on('goToPos', function (x, y, z) {
    let positionToGo = v(x, y, z);
    console.log(positionToGo);
    bot.navigate.to(positionToGo);
});

bot.navigate.on('arrived', function () {
    bot.chat('Je suis arrivé');
});

ioMaster.on('exchange', function (botToExchange, items) {
    bot.navigate.to(botToExchange.entity.position);
});