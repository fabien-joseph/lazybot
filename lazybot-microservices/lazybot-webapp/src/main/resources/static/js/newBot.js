$("button").click(function () {
    $("#myModal").addClass("is-active");
});

$(".modal-background").click(function () {
    $('#myModal').removeClass("is-active");
});

$(".closeNewBot").click(function () {
    $('#myModal').removeClass("is-active");
});
