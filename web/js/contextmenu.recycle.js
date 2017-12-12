$(function () {
    $("#clear_recycle").click(function () {
        $("#recycle_folder .recycle-item").addClass("selected");
        shred();
    });
});
var recycle_menu = new BootstrapMenu(".recycle-item", {
    actionsGroups: [
        ["restore", "shred"]
    ],
    actions: {
        restore: {
            name: "还原",
            iconClass: "fa-reply",
            classNames: "right-click-menu",
            onClick: restore
        },
        shred: {
            name: "永久删除",
            iconClass: "fa-minus-square",
            classNames: "right-click-menu",
            onClick: shred
        }
    }
});

function shred() {
    var selectedItems = getSelectedItems();
    /* 需要提交的数据 */
    var fileIDArray = new Array();
    var folderIDArray = new Array();
    selectedItems.each(function () {
        var itemTag = $(this);
        if (itemTag.attr("data-folder-id") != undefined) {
            var id = itemTag.attr("data-folder-id");
            folderIDArray.push(id);
        } else {
            var id = itemTag.attr("data-file-id");
            fileIDArray.push(id);
        }
    });
    var shredData = {
        files: fileIDArray,
        folders: folderIDArray
    };

    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/recycle",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(shredData),
        success: function (result) {

            for (var i = 0; i < folderIDArray.length; i++) {
                $('.treeNode-info[data-folder-id="' + folderIDArray[i] + '"]').parent().remove();
                $('#recycle_folder .recycle-item[data-folder-id="' + folderIDArray[i] + '"]').remove();
            }
            for (var i = 0; i < fileIDArray.length; i++) {
                $('#recycle_folder .recycle-item[data-file-id="' + fileIDArray[i] + '"]').remove();
            }

            // 更新用户存储空间
            var cap = result.usedSize;
            sessionStorage.setItem("user_usedSize", cap);
            var percentage = getUsedPercentage(cap, sessionStorage.getItem("user_memorySize"));
            $("#user_capacity").css("width", percentage).text(percentage);
        }
    });
}

function restore() {
    var selectedItems = getSelectedItems();
    // 需要提交的数据
    var fileIDArray = new Array();
    var folderIDArray = new Array();
    selectedItems.each(function () {
        var itemTag = $(this);
        if (itemTag.attr("data-folder-id") != undefined) {
            var id = itemTag.attr("data-folder-id");
            folderIDArray.push(id);
        } else {
            var id = itemTag.attr("data-file-id");
            fileIDArray.push(id);
        }
    });
    var restoreData = {
        files: fileIDArray,
        folders: folderIDArray,
        dest: 1
    };

    $.ajax({
        type: "PATCH",
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/move",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(restoreData),
        success: function (result) {

            for (var i = 0; i < folderIDArray.length; i++) {
                var newParentDir = $('.treeNode-info[data-folder-id="1"]');
                /* 注意设置padding */
                var paddingParam = parseInt(newParentDir.css("padding-left"), 10) + 20 + "px";
                var moveDir = $('.treeNode-info[data-folder-id="' + folderIDArray[i] + '"]');
                var paddingBefore = parseInt(moveDir.css("padding-left"));
                moveDir.css("padding-left", paddingParam);
                var paddingAfter = parseInt(moveDir.css("padding-left"));
                var paddingAdjustment = paddingAfter - paddingBefore;
                var moveSubDirs = moveDir.next().find(".treeNode-info");
                moveSubDirs.each(function () {
                    var paddingParam = parseInt($(this).css("padding-left"), 10) + paddingAdjustment + "px";
                    $(this).css("padding-left", paddingParam);
                });
                moveDir.parent().appendTo(newParentDir.next());

                moveDir.parent().show();

                var itemTag = $('li[data-folder-id="' + folderIDArray[i] + '"]');
                itemTag.removeClass("recycle-item");
                itemTag.addClass("disk-item");
                itemTag.find(".file-time").text(getFormattedDateTime(result.folders[i].modifyTime));
                itemTag.appendTo($('ul[data-folder-id="1"]'));
            }
            for (var i = 0; i < fileIDArray.length; i++) {
                var itemTag = $('li[data-file-id="' + fileIDArray[i] + '"]');
                itemTag.removeClass("recycle-item");
                itemTag.addClass("disk-item");
                itemTag.find(".file-time").text(getFormattedDateTime(result.files[i].modifyTime));
                itemTag.appendTo($('ul[data-folder-id="1"]'));
            }
        }
    });
}