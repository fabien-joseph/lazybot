const io = require("socket.io-client"),
    ioMaster = io.connect("http://localhost:9090"),
    //ioMission = io.connect("http://localhost:9091"),
    ioMap = io.connect("http://localhost:9092");

var botId;
var mybot = require('./business/mybot');
var mineflayer = require('mineflayer');
var bot = mybot.connect(process.argv, process.env.BOT_USERNAME, process.env.BOT_PASSWORD);

bot.on('login', function () {
    botId = Math.floor(Math.random() * (999999 - 100000) + 100000);
    ioMap.emit('connectBot', botId);
    ioMaster.emit('connectBot', botId);
});

bot.on('end', function () {
    ioMaster.emit('disconnect', botId);
});

bot.on('chat', function(username, message) {
    if (username === bot.username) return;
    if (message === 'chunk') {
        let blocks = mybot.loadChunkArround(bot, 10, 0, 0);
        //ioMaster.emit('loadChunk', mybot.loadChunkArround(bot, 1));
        return;
    }
    bot.chat(message);
});


ioMap.on('test', function (ray, offsetX, offsetZ) {
    console.log("Test reçu")
    bot.chat("Recu");
});