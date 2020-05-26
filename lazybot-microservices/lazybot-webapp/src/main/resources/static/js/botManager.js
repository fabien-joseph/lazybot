function sendMessage() {
    let message = document.getElementById("myMessage").value;
    if (message !== '') {
        ioClient.emit('sendMessageTest', {botUsername: window.location.pathname, data: message});
        document.getElementById("myMessage").value = '';
    }
}

function goToPos() {
    let x = parseInt(document.getElementById("xPos").value);
    let y = parseInt(document.getElementById("yPos").value);
    let z = parseInt(document.getElementById("zPos").value);
    ioClient.emit('goToPos', JSON.stringify({botUsername: window.location.pathname, data:{x: x, y: y, z: z}}));
    document.getElementById("xPos").value = '';
    document.getElementById("yPos").value = '';
    document.getElementById("zPos").value = '';
}

function loadMap() {
    let rayon = parseInt(document.getElementById("rayonLoadMap").value);
    ioClient.emit('loadMap', rayon);
    document.getElementById("rayonLoadMap").value = '';
}

function exitBot() {

}