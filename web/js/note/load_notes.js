$(document).ready(function () {
    $.ajax({
        type: "GET",
        contentType: "application/json;charset=UTF-8",
        url: "/users/" + sessionStorage.getItem("user_username") + "/notes",
        success: function (data) {
            $("#content").text(data.content);
        }
    });
});