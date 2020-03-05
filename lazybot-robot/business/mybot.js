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

exports.loadChunkArround = function (bot, ray, offsetX, offsetZ) {
    let blocks = [];
    let xMin = -1 * Number.parseInt(ray) + (offsetX == null ? 0 : offsetX);
    let zMin = -1 * Number.parseInt(ray) + (offsetZ == null ? 0 : offsetZ);
    let x = xMin;
    let z = zMin;

    console.log("=== Rayon : " + ray + ", xMin = " + xMin + ", zMin = " + zMin +" ===");
    let i = 0;
    while (z <= ray + offsetZ) {
        block = bot.blockAt(bot.entity.position.offset(x, -1, z));
        blockString = block.type * 16 + block.metadata;
        console.log(i + ".\t" + x + " ; " + z + "\t: " + block.type);
        blocks.push(blockString);
        x++;
        i++;
        if (x === ray + 1 + offsetX) {
            x = xMin;
            z++;
        }
    }
    console.log('Nombre de blocks : ' + blocks.length);
    return blocks;
};
