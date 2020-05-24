function sendMessage() {
    let message = document.getElementById("myMessage").value;
    alert(message);
    if (message !== '') {
        ioClient.emit('sendMessageTest', message);
        document.getElementById("myMessage").value = '';
    }
}

function goToPos() {
    let x = parseInt(document.getElementById("xPos").value);
    let y = parseInt(document.getElementById("yPos").value);
    let z = parseInt(document.getElementById("zPos").value);
    var jsonObject = {x: x, y: y, z: z};
    ioClient.emit('goToPos', jsonObject);
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