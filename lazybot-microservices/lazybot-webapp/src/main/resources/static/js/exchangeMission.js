getUpdateBot();

$("#openExchangeModal").click(function () {
    getAllBotConnected();
    $("#modalExchangeMission").addClass("is-active");
});

$(".modal-background").click(function () {
    closemyModalExchange();
});

$(".closeExchangeMission").click(function () {
    closemyModalExchange();
});


$('.itemExchange').click(function (event) {
    if (event.target.dataset.itemType === 'undefined' || event.target.dataset.itemType === '' || event.target.dataset.itemType == null)
        return;
    if (!$(this).hasClass("selected")) {
        let items = $('.selected').removeClass('selected');
        $(this).addClass("selected");

    } else {
        $(this).removeClass("selected");
    }
})/*
$('.itemExchange').click(function (event) {
    if (event.target.dataset.itemType === 'undefined' || event.target.dataset.itemType === '' || event.target.dataset.itemType == null)
        return;
    if (!$(this).hasClass("selected")) {
        $(this).addClass("selected");
    } else {
        $(this).removeClass("selected");
    }
})*/

function executeExchange() {
    let datas;
    var all = $(".selected .itemImg img");
    var allSelected = $(".selected");

    console.log("Datas : " + all[0].dataset);

    let jsonRequest = '{bot1Username:' + window.location.pathname.substring(1) + ', bot2Username:' + $('#selectBotExchange option:selected').text() +  ',';
    jsonRequest += "itemsGiveByBot1: [";
    for ( let i = 0; i < all.length; i++) {
        allSelected.removeClass("selected");
        jsonRequest += "{type:" + all[i].dataset.itemType + ", ";
        jsonRequest += "count:" + all[i].dataset.itemCount + ", ";
        jsonRequest += "metadata:" + all[i].dataset.itemMetadata + ", ";
        jsonRequest += "name:" + "'" + all[i].dataset.itemName + "'" + ", ";
        jsonRequest += "displayName:" + "'" + all[i].dataset.itemDisplayname + "'" + ", ";
        jsonRequest += "stackSize:" + all[i].dataset.itemStacksize + ", ";
        jsonRequest += "slot:" + all[i].dataset.itemSlot;
        jsonRequest += "}";
        if (i !== all.length - 1)
            jsonRequest += ",";
    }
    jsonRequest += "]}";

    console.log("Request : " + jsonRequest);

    ioClient.emit("exchange", jsonRequest);
    closemyModalExchange();
}

function closemyModalExchange() {
    $('#modalExchangeMission').removeClass("is-active");
}

ioClient.on("updateBot", function (jsonBot) {
    let bot = JSON.parse(jsonBot);
    if (window.location.pathname.includes(bot.username)) {
        loadInventoryExchange(bot);
    }
});

ioClient.on('allBotConnected', function (botUsernamesJson) {
    let botUsernames = JSON.parse(botUsernamesJson);
    let botsList = $('#selectBotExchange');
    botsList.empty();
    for (let i = 0; i < botUsernames.length; i++)
    if (!window.location.pathname.includes(botUsernames[i]))
            botsList.append(new Option(botUsernames[i], botUsernames[i]));
});

function loadInventoryExchange(bot) {
    let itemsQuantity = 0;
    for (let i = 0; i < bot.inventory.slots.length; i++) {
        let idInventorySlot = "slotExchange" + i;
        if (bot.inventory.slots[i] == null) {
            document.getElementById(idInventorySlot).innerHTML = '';
        } else {
            let slot = $("#" + idInventorySlot).find("span");
            slot.empty();
            slot.wrapInner("<img src='/getMCObject/" + bot.inventory.slots[i].name + "' " +
                "data-item-type='" + bot.inventory.slots[i].type + "' " +
                "data-item-count='" + bot.inventory.slots[i].count + "' " +
                "data-item-metadata='" + bot.inventory.slots[i].metadata + "' " +
                "data-item-name='" + bot.inventory.slots[i].name + "' " +
                "data-item-displayName='" + bot.inventory.slots[i].displayName + "' " +
                "data-item-stackSize='" + bot.inventory.slots[i].stackSize + "' " +
                "data-item-slot='" + bot.inventory.slots[i].slot + "' " +
                " />");
            itemsQuantity++;
        }
    }
    document.getElementById("inventorySlots").innerHTML = itemsQuantity.toString();
    document.getElementById("inventorySize").innerHTML = bot.inventory.slots.length;
}

