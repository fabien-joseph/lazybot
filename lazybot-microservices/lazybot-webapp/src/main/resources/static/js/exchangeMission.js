$("#openExchangeModal").click(function () {
    $("#modalExchangeMission").addClass("is-active");
});

$(".modal-background").click(function () {
    closemyModalExchange();
});

function closemyModalExchange() {
    $('#modalExchangeMission').removeClass("is-active");
}