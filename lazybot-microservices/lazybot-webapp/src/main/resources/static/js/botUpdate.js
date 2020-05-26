let lastHealthValue = 20;

ioClient.on("updateBot", function (jsonBot) {
    let bot = JSON.parse(jsonBot);
    if (window.location.pathname.includes(bot.username)) {
        document.getElementById("botUsername").innerHTML = bot.username;
        document.getElementById("botHost").innerHTML = bot.host;
        document.getElementById("healthValue").innerHTML = Math.round(bot.health);
        document.getElementById("foodValue").innerHTML = Math.round(bot.food);
        document.getElementById("botXPos").innerHTML = Math.round(bot.position.x);
        document.getElementById("botZPos").innerHTML = Math.round(bot.position.z);
        colorizeHealth(lastHealthValue, bot.health);
        lastHealthValue = bot.health;
        loadInventory(bot);
    }
});

function loadInventory(bot) {
    let itemsQuantity = 0;
    for (let i = 0; i < bot.inventory.slots.length; i++) {
        let idInventorySlot = "slot" + i;
        if (bot.inventory.slots[i] == null) {
            document.getElementById(idInventorySlot).innerHTML = '';
        } else {
            let slot = $("#" + idInventorySlot);
            slot.empty();
            slot.wrapInner("<img src='/getMCObject/" + bot.inventory.slots[i].name + "' />");
            itemsQuantity++;
        }
    }

    console.log(document.getElementById("inventorySlots"));
    document.getElementById("inventorySlots").innerHTML = itemsQuantity.toString();
    document.getElementById("inventorySize").innerHTML = bot.inventory.slots.length;
}

function colorizeHealth(lastHealth, health) {
    if (lastHealthValue > health) {
        document.getElementById("healthValue").style.color = "#ff0000";
    }
    if (lastHealthValue > health) {
        setTimeout(function () {
            document.getElementById("healthValue").style.color = "#000000";
        }, 500);
    }
}