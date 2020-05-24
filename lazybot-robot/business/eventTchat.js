exports.chatControl = function (bot, username, message) {
    if (username === bot.username) return;
    if (message === 'chunk') {
        let blocks = mybot.loadChunkArround(bot, 10, 0, 0);
        //ioMaster.emit('loadChunk', mybot.loadChunkArround(bot, 1));
    } else if (message === 'pos') {
        console.log(bot.entity.position);
    } else if (message === 'inv') {
        console.log(bot.inventory.slots[36].nbt);
    } else if (message === 'spawn') {
        const pos = bot.players['Styleure'].entity.position;
        bot.navigate.to(pos);
        console.log(bot.players['Styleure'].entity.position);
    } else if (message === 'username') {
        bot.chat(bot._client.socket.remoteAddress);
    } else if (message === 'exit') {
        process.exit();
    }
};