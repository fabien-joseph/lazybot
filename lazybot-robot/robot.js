var app = require('express')(),
    server = require('http').createServer(app),
    io = require('socket.io').listen(server);

var socket = io('http://localhost:9092');
var mybot = require('./business/mybot');
var mineflayer = require('mineflayer');
var bot = mybot.connect(process.env.BOT_USERNAME, process.env.BOT_PASSWORD);

bot.on('chat', function(username, message) {
    if (username === bot.username) return;
    bot.chat(message);
    socket.emit('chatevent', 'Pseudo', 'Mon beau message');
});

io.sockets.on('connection', function (socket) {
    console('Quelquun se connecte');
    socket.on('foo', function () {
        console('Someone told mi \'Hi\' ! :D')
    })
});

bot.on('error', err => console.log(err));
server.listen(8080);