// === Send message functions ===
function sendMessage() {
    let message = document.getElementById("myMessage").value;
    if (message !== '') {
        emitMessage(message);
    }
}

$("#myMessage").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        let message = document.getElementById("myMessage").value;
        if (message !== '') {
            emitMessage(message);
        }
    }
});

function emitMessage(message) {
    ioClient.emit('sendMessageTest', {botUsername: getBotUsername(), data: message});
    document.getElementById("myMessage").value = '';
}

// === Go to pos functions ===
function goToPos() {
    emitGoToPos();
}

$("#posToGo").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        emitGoToPos();
    }
});

function emitGoToPos() {
    let x = parseInt(document.getElementById("xPos").value);
    let y = parseInt(document.getElementById("yPos").value);
    let z = parseInt(document.getElementById("zPos").value);
    ioClient.emit('goToPos', JSON.stringify({botUsername: getBotUsername(), data: {x: x, y: y, z: z}}));
    document.getElementById("xPos").value = '';
    document.getElementById("yPos").value = '';
    document.getElementById("zPos").value = '';
}

// === Unused functions (probably soon deleted)

function loadMap() {
    let rayon = parseInt(document.getElementById("rayonLoadMap").value);
    ioClient.emit('loadMap', rayon);
    document.getElementById("rayonLoadMap").value = '';
}

function exitBot() {

}

// === Pratical functions
function getBotUsername() {
    return window.location.pathname !== '/' ? window.location.pathname : '/*';
}

