exports.chatControl = function (bot, username, message) {
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
    } else if (message === 'exit') {
        process.exit();
    }
    bot.chat(message);
};