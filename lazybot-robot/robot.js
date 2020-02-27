const io = require("socket.io-client"),
    ioClient = io.connect("http://localhost:9092");

var mybot = require('./business/mybot');
var mineflayer = require('mineflayer');
var bot = mybot.connect(process.env.BOT_USERNAME, process.env.BOT_PASSWORD);

bot.on('chat', function(username, message) {
    if (username === bot.username) return;
    var test = [1, 5, 2];
    if (message === 'chunk') {
        ioClient.emit('loadChunk', mybot.loadChunkArround(bot, 1));
        return;
    }

    bot.chat(message);
});

ioClient.on('loadChunk', function (ray) {

});