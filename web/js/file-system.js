$(function () {
    // 进入文件夹/后退/新建文件夹事件绑定
    $("#all").on("click", "ul [data-folder-id]", enterFolder);
    $("#search").on("click", "ul [data-folder-id]", enterFolder);
    $("#all").on("click", ".disk-file-path-wrapper .disk-file-path li", goBack);
    $("#mkfolder").click(mkfolder);

    // 文件选择相关事件绑定
    $("body:not(#path_modal)").click(resetSelectedState);
    $("#select_all").click(selectAll);// 为全选绑定事件处理函数
    $("#all").on("click", "ul .disk-file-item .select", selectItem);
    $("#all").on("contextmenu", "ul .disk-file-item", rightClickSelectItem);
    $("#recycle").on("click", "ul .disk-file-item .select", selectItem);
    $("#recycle").on("contextmenu", "ul .disk-file-item", rightClickSelectItem);

    // 最近/文档/图片/视频/音乐分类文件
    $("#nav_recent").click(function () {
        getFile.call(this, "recent");
    });
    $("#nav_doc").click(function () {
        getFile.call(this, "doc");
    });
    $("#nav_photo").click(function () {
        getFile.call(this, "photo");
    });
    $("#nav_video").click(function () {
        getFile.call(this, "video");
    });
    $("#nav_audio").click(function () {
        getFile.call(this, "audio");
    });

    // 视图控制事件
    $("#nav_all").click(function () {
        $("#view_control").css("visibility", "visible");
    });
    $("#nav_recycle").click(function () {
        $("#view_control").css("visibility", "hidden");
    });

    // 排序控制事件
    $("#sort_by_alpha").click(function () {
        refreshAllFolder.call(this);
    });
    $("#sort_by_time").click(function () {
        refreshAllFolder.call(this);
    });
    function refreshAllFolder() {
        var currentFolderID = $("#all ul:visible").attr("data-folder-id");
        $("#all ul").remove();
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
        $("#treeNode_root").next().empty()
        showFolderContents(currentFolderID);
    }
});

function showFolderContents(folderId) {
    // 隐藏当前文件夹内容
    $("#all ul").hide();
    // 如果需展示的文件夹之前已经生成过，则显示对应节点，返回
    var node = getFolderNode(folderId);
    if (node != null) {
        node.show();
        return;
    } else {
        createFolderNode(folderId, true);
    }
}
/*
 * 本函数会在两处被调用(1)主界面需要显示的文件夹节点不存在时(2)“选择路径”模态框的文件夹节点不存在时
 * 生成主界面文件夹节点（内含文件节点）的同时会生成“选择路径”模态框的文件夹节点（不含文件节点）
 * 如果show=true则会立刻在主界面显示生成的文件夹节点，否则只生成不显示
 * ---------------------------------------------------------------------------------------------------
 * 注意如果在ajax success事件处理函数中返回一个值供其它函数调用，会出现获得undefined的情况（因为请求是异步的）
 * 考虑同步请求即async=false，但是(1)deprecated(2)阻塞
 * ---------------------------------------------------------------------------------------------------
 * 原先思路：生成节点后始终hide，将节点返回到调用的上一级，由上一级决定是否要显示该节点
 * 但由于请求异步，会导致上一级获得"undefined"的情况
 */
function createFolderNode(folderId, show) {
    // 获取排序类型
    var sortType = getSortType();
    // 生成一个文件夹节点<ul>，设置data-folder-id属性，该属性是该文件夹在数据库存储的ID
    var node = $("<ul></ul>");
    node.attr("data-folder-id", folderId);
    // 向服务器发送异步请求，获取对应folderID的内容
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/folders/" + folderId + "?sort=" + sortType,
        /*
         * 在success的回调函数中访问不到外部的folderId，所以添加下面的参数
         * 修正：访问不到函数的参数，但可以访问函数内部的变量！
         */
        folderId: folderId,
        success: function (result) {
            var folders = result.folders;
            var files = result.files;
            // 生成“选择路径”模态框的文件夹节点
            createDirNode(this.folderId, folders, !show);
            // 必须在参数前面加上this.
            // 生成当前文件夹下所有文件夹节点
            generateFolderNode(folders, node);
            // 生成当前文件夹下所有文件节点
            generateFileNode(files, node);
            if (!show) {
                node.hide();
            }
            node.appendTo($("#all"));
        }
    });
}
function generateFolderNode(folders, parent) {
    for (var i = 0; i < folders.length; i++) {
        var folderId = folders[i].folderId;
        var folderName = folders[i].folderName;
        var modifyTime = getFormattedDateTime(folders[i].modifyTime);
        var folderNode = $('<li class="disk-file-item disk-item"></li>');
        folderNode.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="img/icon/folder.png" class="thumb-icon"></div><div class="file-title"><span class="file-name">' + folderName + '</span></div></div>');
        folderNode.append('<div class="file-info"><span class="file-size"></span><span class="file-time">' + modifyTime + '</span></div>');
        // 设置文件夹ID
        folderNode.attr("data-folder-id", folderId);
        folderNode.appendTo(parent);
    }
}
function generateFileNode(files, parent) {
    for (var i = 0; i < files.length; i++) {
        var fileType = files[i].fileType;
        if (fileType != "") {
            fileType = "." + fileType;
        }
        var fileId = files[i].fileId;
        var fileName = files[i].fileName + fileType;
        var fileSize = getReadableSize(files[i].fileSize);
        var modifyTime = getFormattedDateTime(files[i].modifyTime);
        var fileImg;
        if (isPicture(files[i].fileType)) {
            fileImg = files[i].fileUrl;
        } else {
            fileImg = getFileIcon(getSuffix(fileName));
        }
        var fileNode = $('<li class="disk-file-item disk-item"></li>');
        fileNode.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="' + fileImg + '" class="thumb-icon"></div><div class="file-title"><span class="file-name">' + fileName + '</span></div></div>');
        fileNode.append('<div class="file-info"><span class="file-size">' + fileSize + '</span><span class="file-time">' + modifyTime + '</span></div>');
        fileNode.attr("data-file-id", fileId);

        fileNode.appendTo(parent);
    }
}


/*
 * 单击文件夹后，更新面包屑导航并列出该文件夹下的所有文件
 * 该函数绑定到文件夹<li>节点的click事件上
 */
function enterFolder() {
    // 判断该文件是否是文件夹
    if ($(this).attr("data-folder-id") != undefined) {
        $("#nav_all").tab("show");
        // 生成一个该文件夹的面包屑导航节点
        var breadNode = $('<li></li>');
        // 设置面包屑节点的名字
        var folderName = $(this).find(".file-name").text();
        breadNode.append('<a href="javascript:void(0)">' + folderName + '</a>');
        // 设置面包屑节点对应的文件夹ID
        breadNode.attr("data-folder-id", $(this).attr("data-folder-id"));

        //breadNode.on("click", goBack);
        // 移除之前面包屑节点的active效果，并把当前面包屑节点设置为active
        $("#disk_file_path li").children().removeClass("active");
        breadNode.children().addClass("active");
        // 追加该面包屑到系统路径
        breadNode.appendTo($("#disk_file_path"));
        // 重置当前文件夹所有文件的选中状态
        //$("#all ul:visible .selected").find(".select input").click();
        // 列出该文件夹下的所有文件
        showFolderContents($(this).attr("data-folder-id"));
    }
}
/* 点击文件头部复选框时的事件处理函数 */
function selectItem(e) {
    // 点击复选框的时候右键菜单不会自动隐藏
    $(".bootstrapMenu").hide();
    var itemTag = $(this).parent().parent();
    if ($(this).children().prop("checked")) {
        itemTag.addClass("selected");
        if (itemTag.hasClass("disk-item")) {
            // 判断是否全选
            if (itemTag.parent().children("li:not(.selected)").length == 0) {
                $("#select_all input").prop("checked", true);
            }
        }
    } else {
        itemTag.removeClass("selected");
        if (itemTag.hasClass("disk-item")) {
            // 只要有一个被移除了选中效果，就取消全选
            $("#select_all input").prop("checked", false);
        }
    }

    e.stopPropagation();
}
/* 在文件或文件夹上右击时的事件处理函数 */
function rightClickSelectItem() {
    if (!$(this).find(".select input").prop("checked")) {
        // 取消所有原有的选中效果
        $(this).parent().find(".selected .select input").click();
        // 选中当前的文件
        $(this).find(".select input").prop("checked", true);
        $(this).addClass("selected");
    }
}
/* 单击全选按钮的事件处理函数 */
function selectAll(e) {
    if ($(this).children().prop("checked")) {
        $("#all ul:visible li:not(.selected)").find(".select input").click();
    } else {
        $("#all ul:visible .selected").find(".select input").click();
    }
    e.stopPropagation();
}
/* 取消所有选中状态 */
function resetSelectedState() {
    $("#select_all input").prop("checked", false);
    $(".selected").find(".select input").click();
}
/*
 * 单击面包屑导航时绑定的事件处理函数
 */
function goBack() {
    $(this).nextAll().remove();// 把本节点之后的sibling全部移除
    $(this).children().addClass("active");// 把当前单击的面包屑节点设置为active
    showFolderContents($(this).attr("data-folder-id"));// 显示该面包屑节点对应的文件夹
}
/*
 * 判断folderID对应的文件夹节点是否存在
 * 是则返回该节点
 * 否则返回null
 */
function getFolderNode(folderId) {
    var node = $('ul[data-folder-id="' + folderId + '"]');// ！！标签 + 属性选择器 ！！
    if (node.length != 0) {
        return node;
    } else {
        return null;
    }
}

/*******************************************新建文件夹*******************************************/
function mkfolder() {
    var currentFolder = $("ul[data-folder-id]:visible");

    var folderTag = $('<li class="disk-file-item disk-item"></li>');
    folderTag.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="img/icon/folder.png" class="thumb-icon"></div><div class="file-title" style="display: none;"><span class="file-name"></span></div><span class="fileedit"><input type="text" /></span></div>');
    folderTag.append('<div class="file-info"><span class="file-size"></span><span class="file-time"></span></div>');
    folderTag.find(".fileedit").click(function (e) {
        e.stopPropagation()
    });
    folderTag.find(".fileedit input").blur(confirm);
    folderTag.find(".fileedit input").keydown(confirmByKeyborad);

    // 需要获取的数据
    var id;

    folderTag.prependTo(currentFolder);
    folderTag.find(".fileedit input").focus();


    function confirm() {
        var inputTag = $(this);
        var itemTag = inputTag.parent().parent().parent();
        var nameTag = itemTag.find(".file-name");
        var timeTag = itemTag.find(".file-time");

        // 需要提交的数据
        var folderName = inputTag.val();
        var parentId = itemTag.parent().attr("data-folder-id");
        var newFolder = {
            folderName: folderName,
            parentId: parentId
        }

        if (folderName.length == 0) {
            // TO DO 判断是否全为空格
            alert("名字不能为空");
            itemTag.remove();
        } else if (checkDuplicateName(itemTag, folderName)) {
            alert("文件名产生冲突");
            itemTag.remove();
        } else {
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/folders",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(newFolder),
                success: function (result) {
                    // 模态框同步
                    var folder = [{
                        folderId: result.folderId,
                        folderName: result.folderName
                    }];
                    createDirNode(parentId, folder, false);
                    // 新建文件夹节点
                    itemTag.attr("data-folder-id", result.folderId);
                    timeTag.text(getFormattedDateTime(result.modifyTime));
                    nameTag.text(result.folderName);

                    // 移除文件名编辑框
                    inputTag.parent().remove();

                    nameTag.parent().show();
                }
            });
        }
    }

    function confirmByKeyborad(event) {
        if (event.keyCode == 13) {
            $(this).blur();
        }
    }
}

/*******************************************获取回收站文件*******************************************/
function getRecycleItems() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/folders/3?sort=1",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            var node = $("#recycle_folder");

            var folders = result.folders;
            var files = result.files;

            for (var i = 0; i < folders.length; i++) {
                var folderId = folders[i].folderId;
                var folderName = folders[i].fileName;
                var remainDays = getRemainDays(folders[i].modifyTime);
                var folderNode = $('<li class="disk-file-item recycle-item"></li>');
                folderNode.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="img/icon/folder.png" class="thumb-icon"></div><div class="file-title"><span class="file-name">' + folderName + '</span></div></div>');
                folderNode.append('<div class="file-info"><span class="file-size"></span><span class="file-time">' + remainDays + '</span></div>');
                folderNode.attr("data-folder-id", folderId);
                folderNode.appendTo(node);
            }

            for (var i = 0; i < files.length; i++) {
                var fileType = files[i].fileType;
                if (fileType != "") {
                    fileType = "." + fileType;
                }
                var fileId = files[i].fileId;
                var fileName = files[i].fileName + fileType;
                var fileSize = getReadableSize(files[i].fileSize);
                var remainDays = getRemainDays(files[i].modifyTime);
                var fileImg = getFileIcon(getSuffix(fileName));
                var fileNode = $('<li class="disk-file-item recycle-item"></li>');
                fileNode.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="' + fileImg + '" class="thumb-icon"></div><div class="file-title"><span class="file-name">' + fileName + '</span></div></div>');
                fileNode.append('<div class="file-info"><span class="file-size">' + fileSize + '</span><span class="file-time">' + remainDays + '</span></div>');
                fileNode.attr("data-file-id", fileId);

                fileNode.appendTo(node);
            }
            node.appendTo($("#recycle"));
        }
    });
}
/******************************************文件分类******************************************/
function getFile(type) {
    // 防止无意义的刷新
    if ($(this).parent().hasClass("active")) {
        return;
    }
    // 隐藏视图控制
    $("#view_control").css("visibility", "hidden");

    $("#" + type + " ul").remove();// 删除旧节点
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/" + type,
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            var files = result.files;
            var node = $('<ul></ul>');
            if (type != "photo") {
                generateFileNode(files, node);
                node.find(".disk-file-item").removeClass("disk-item");
                node.appendTo($("#" + type));
            } else {
                for (var i = 0; i < files.length; i++) {
                    var fileName = files[i].fileName + "." + files[i].fileType;
                    var fileImg = files[i].fileUrl;
                    var fileNode = $('<li class="picture"></li>');
                    fileNode.append('<div class="picture-img"><img src="' + fileImg + '"></div>');
                    fileNode.append('<div class="picture-info"><p><span class="glyphicon glyphicon-picture nav-title-icon" style="color: #337ab7;font-size: 18px;"></span><span class="picture-info-title">' + fileName + '</span></p></div>');
                    fileNode.appendTo(node);
                    node.appendTo($('#photo .picture-wall'));
                }
            }
        }
    });
}

/******************************************文件排序******************************************/
/* 获取排序类型，返回0表示按字母排序，返回1表示按时间排序 */
function getSortType() {
    if ($("#sort_by_alpha").hasClass("active")) {
        return 0;
    } else if ($("#sort_by_time").hasClass("active")) {
        return 1;
    } else {
        return null;
    }
}