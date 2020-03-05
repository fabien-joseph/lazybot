var mineflayer = require('mineflayer');

exports.connect = function (username, password) {
    if (password == null || password === 'undefined')
        return mineflayer.createBot({
            host: "localhost", // optional
            port: 25565,       // optional
            username: username, // email and password are required only for
            version: false                 // false corresponds to auto version detection (that's the default), put for example "1.8.8" if you need a specific version
        });

    return mineflayer.createBot({
        host: "localhost", // optional
        port: 25565,       // optional
        username: username, // email and password are required only for
        password: password, // email and password are required only for
        version: false                 // false corresponds to auto version detection (that's the default), put for example "1.8.8" if you need a specific version
    });
};

exports.loadChunkArround = function (bot, ray) {
    let blocks = [];
    let xMin = -1 * Number.parseInt(ray);
    let zMin = -1 * Number.parseInt(ray);
    let x = xMin;
    let z = zMin;

    while (x <= ray && z <= ray) {
        block = bot.blockAt(bot.entity.position.offset(x, -1, z));
        blockString = block.type * 16 + block.metadata;
        blocks.push(blockString);
        x++;
        if (x === ray + 1) {
            x = xMin;
            z++;
        }
    }
    return blocks;
};
