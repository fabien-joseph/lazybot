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
