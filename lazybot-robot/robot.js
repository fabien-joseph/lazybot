const io = require("socket.io-client"),
    ioMaster = io.connect("http://localhost:9090");

var botId;
var mybot = require('./business/mybot');
var mineflayer = require('mineflayer');
var bot = mybot.connect(process.argv, process.env.BOT_USERNAME, process.env.BOT_PASSWORD);
connection();

function connection () {
    botId = Math.floor(Math.random() * (999999 - 100000) + 100000);
    ioMaster.emit('connectBot', botId);
}



bot.on('end', function () {
    ioMaster.emit('disconnect', botId);
});

bot.on('chat', function(username, message) {
    if (username === bot.username) return;
    if (message === 'chunk') {
        let blocks = mybot.loadChunkArround(bot, 10, 0, 0);
        //ioMaster.emit('loadChunk', mybot.loadChunkArround(bot, 1));
        return;
    } else if (message === 'pos') {
        console.log(bot.entity.position);
        return;
    }
    bot.chat(message);
});

bot.on('health', function () {
    ioMaster.emit("healthChange", bot.health);
});

ioMaster.on('test', function () {
    console.log("J'ai reçu le retour");
});

ioMaster.on('sendMessage', function (message) {
    bot.chat(message);
});