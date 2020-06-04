const mineflayer = require('mineflayer');
const navigatePlugin = require('mineflayer-navigate')(mineflayer);

exports.connect = function (argv) {
    let username = '';
    let password = '';
    let server = '';

    if (parseArgName(argv[2]) !== 'username' && parseArgName(argv[3]) !== 'password' && parseArgName(argv[4]) !== 'server') {
        throw new Error('The given arguments seems incorrect. Are they realy name \'username\' and \'password\' ?');
    }

    username = parseArgValue(argv[2]);
    password = parseArgValue(argv[3]);
    server = parseArgValue(argv[4]);

    // === If it's an offline account
    if (password == null || password === 'undefined' || password === '')
        return mineflayer.createBot({
            host: server, // optional
            port: 25565,       // optional
            username: username, // email and password are required only for
            version: false                 // false corresponds to auto version detection (that's the default), put for example "1.8.8" if you need a specific version
        });

    // === If it's a premium account
    let bot = mineflayer.createBot({
        host: server, // optional
        port: 25565,       // optional
        username: username, // email and password are required only for
        password: password, // email and password are required only for
        version: false                 // false corresponds to auto version detection (that's the default), put for example "1.8.8" if you need a specific version
    });
    navigatePlugin(bot);
    return bot;
};

exports.loadChunkArround = function (bot, ray, offsetX, offsetZ) {
    let blocks = [];
    let xMin = -1 * Number.parseInt(ray) + (offsetX == null ? 0 : offsetX);
    let zMin = -1 * Number.parseInt(ray) + (offsetZ == null ? 0 : offsetZ);
    let x = xMin;
    let z = zMin;

    //console.log("=== Rayon : " + ray + ", xMin = " + xMin + ", zMin = " + zMin +" ===");
    let i = 0;
    while (z <= ray + offsetZ) {
        block = bot.blockAt(bot.entity.position.offset(x, -1, z));
        blockString = block.type * 16 + block.metadata;
        //console.log(i + ".\t" + x + " ; " + z + "\t: " + block.type);
        blocks.push(blockString);
        x++;
        i++;
        if (x === ray + 1 + offsetX) {
            x = xMin;
            z++;
        }
    }
    //console.log('Nombre de blocks : ' + blocks.length);
    return blocks;
};

exports.jsonBot = function (actualMissionName, actualMissionStep, bot) {
    return {
        "actualMissionName": actualMissionName,
        "actualMissionTep": actualMissionStep,
        "username": bot.username,
        "host": bot._client.socket.remoteAddress,
        "position": {x: bot.entity.position.x, y:bot.entity.position.y, z: bot.entity.position.z},
        "health": bot.health,
        "food": bot.food,
        "inventory": bot.inventory
    };
};

exports.jsonInventory = function (bot) {
    return {
        "inventory": bot.inventory
    }
};

function parseArgName(arg) {
    if (arg == null) {
        throw new Error('The given argument is null');
    }
    const words = arg.split("=");

    if (words[0] === '' || words[0] == null) {
        throw new Error('The argument as no name');
    }
    return words[0].replace('--', '');
}

function parseArgValue(arg) {
    if (arg == null) {
        throw new Error('The given argument is null');
    }
    const words = arg.split("=");
    return words[1];
}