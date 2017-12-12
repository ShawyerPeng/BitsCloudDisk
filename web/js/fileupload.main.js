/* 实现文件上传功能 */
var capacity = sessionStorage.getItem("user_memorySize");
$(function () {
    $("#btn_transfer").click(function () {
        $('[role="presentation"][class="active"]').removeClass("active");
        $("#view_control").css("visibility", "hidden");
    });
    $("#fileupload").fileupload({
        url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/files",
        maxChunkSize: 102400,
        add: function (e, data) {
            $("#modal_btn_submit").click(addFile.bind(null, e, data, this));
            // 调用带参函数的正确写法
        },
        progress: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            data.context.find(".progress-bar").css("width", progress + "%").text(progress + "%");
        },
        always: function (e, data) {
            console.log(data.result);
            if (data.result == undefined) {
                data.result = data.formData.result;
            }
            if (data.result != undefined) {
                switch (data.result.status_code) {
                    case 111:
                        generateCompletedMissionNode(data);
                        break;
                    case 222:
                    case 333:
                        finishUpload(data);
                        break;
                }
            }
        }
    }).on('fileuploadchunksend', function (e, data) {
        if (data.formData.abortFlag) {
            return false;
        }
    }).on("fileuploadchunkdone", function (e, data) {
        if (data.result.status_code == 111 || data.result.status_code == 222) {
            data.formData.abortFlag = true;
        }
        if (data.result.status_code == 111 || data.result.status_code == 222 || data.result.status_code == 333) {
            data.formData.result = data.result;
        }
    });
    /* 取消全部按钮功能实现 */
    $("#cancel_all_mission").click(function () {
        $("#loading_mission_list").find(".upload-cancel").click();
    });
    /* 清空记录按钮功能实现 */
    $("#remove_all_record").click(function () {
        $("#complete_mission_list").find(".remove-record").click();
    });
});
/* 更新进行中任务的计数 */
function updateLoadingCount(plus) {
    var loadingCount = $(".loading_mission_count");
    if (plus) {
        if (loadingCount.first().text() == "") {
            loadingCount.text(1);
        } else {
            loadingCount.text(parseInt(loadingCount.first().text()) + 1);
        }
    } else {
        if (parseInt(loadingCount.first().text()) - 1 == 0) {
            loadingCount.empty();
        } else {
            loadingCount.text(parseInt(loadingCount.first().text()) - 1);
        }
    }
}
/* 更新已完成任务的计数 */
function updateCompleteCount(plus) {
    var completeCount = $("#complete_mission_count");
    if (plus) {
        completeCount.text(parseInt(completeCount.text()) + 1);
    } else {
        completeCount.text(parseInt(completeCount.text()) - 1);
    }
}
/* 
 * 绑定在模态框上传按钮click上的事件处理函数
 */
function addFile(e, data, marker) {
    if ((data.files[0].size + Number(sessionStorage.getItem("user_usedSize"))) > capacity) {
        alert("容量超出上限，无法上传");
        data.abort();
        return;
    }
    // 获取该文件的必要信息，用于提交到服务器
    var fileInfo = {
        userId: sessionStorage.getItem("user_id"),
        fileName: getFilenameWithoutSuffix(data.files[0].name),
        fileType: getSuffix(data.files[0].name),
        parentId: $("#dirbox_path").attr("data-folder-id")
    };
    // 生成一个“准备中”的任务节点
    var fileName = data.files[0].name;
    var fileImg = getFileIcon(getSuffix(fileName));
    var fileSize = getReadableSize(data.files[0].size);
    var node = $("<li></li>");
    node.append('<div class="mission-head"><div class="mission-icon-wrapper"><span class="glyphicon glyphicon-upload" style="color: #337ab7;"></span></div><div class="thumb"><img src="' + fileImg + '" class="thumb-icon"></div><div class="mission-info"><span class="mission-file-name">' + fileName + '</span><span class="mission-file-size">' + fileSize + '<span class="mission-file-preparing">&nbsp;&nbsp;&nbsp;准备中...</span></span></div></div>');
    node.append('<div class="progress mission-progress"><div class="progress-bar progress-bar-striped progress-bar-primary active" role="progressbar" style="width: 0%">0%</div></div>');
    node.append('<div class="mission-control"><a class="upload-pause" style="display: none;"><span class="glyphicon glyphicon-pause"></span></a><a class="upload-cancel"><span class="glyphicon glyphicon-remove"></span></a></div>');
    data.context = node;
    node.appendTo("#loading_mission_list");
    // 更新任务计数
    updateLoadingCount(true);
    // 显示“进行中”的标题栏
    $("#dynamic_title_loading").show();
    // 为取消上传按钮绑定对应事件
    data.context.find(".upload-cancel").click(function () {
        data.abortFlag = true;
        data.abort();
        data.context.remove();
        updateLoadingCount(false);
        // 如果没有正在进行的任务，则隐藏“进行中”标题栏
        if ($(".loading_mission_count").first().text() == "") {
            $("#dynamic_title_loading").hide();
        }
    });
    browserMD5File(data.files[0], function (err, fileMd5) {
        if (data.abortFlag) {
            return;
        }
        fileInfo.fileMd5 = fileMd5;
        data.formData = fileInfo;

        // 隐藏“准备中”
        data.context.find(".mission-file-preparing").hide();
        // 显示暂停按钮
        data.context.find(".upload-pause").show();
        data.submit();

        // 更新取消上传按钮绑定的对应事件，取消的同时删除服务器上传到一半的文件
        data.context.find(".upload-cancel").off("click");
        data.context.find(".upload-cancel").click(function () {
            data.abort();
            // 发送请求删除服务器上传到一半的文件
            $.ajax({
                type: "DELETE",
                url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/files?cancel=" + data.formData.fileMd5,
                success: function (message) {
                    data.context.remove();
                    updateLoadingCount(false);
                    // 如果没有正在进行的任务，则隐藏标题栏
                    if ($(".loading_mission_count").first().text() == "") {
                        $("#dynamic_title_loading").hide();
                    }
                }
            });
        });
        /* 为暂停按钮添加断点续传功能 */
        data.context.find(".upload-pause").click(function () {
            var icon = $(this).children();
            if (icon.hasClass("glyphicon-pause")) {
                // 暂停逻辑
                icon.removeClass("glyphicon-pause");
                icon.addClass("glyphicon-play");
                data.abort();
            } else {
                // 继续逻辑
                icon.removeClass("glyphicon-play");
                icon.addClass("glyphicon-pause");
                // 获取已经上传的字节数，从断点继续
                $.ajax({
                    type: "GET",
                    url: "http://localhost:8080/users/" + sessionStorage.getItem("user_username") + "/disk/files?resume=" + data.formData.fileMd5,
                    success: function (uploadedBytes) {
                        // 断点续传的核心代码
                        data.uploadedBytes = Number(uploadedBytes);// 获取断点
                        $.blueimp.fileupload.prototype.options.add.call(marker, e, data);// 续传
                    }
                });
            }
        });
    });
}
/**
 *  生成“已完成”的任务节点
 */
function generateCompletedMissionNode(data) {
    // 更新“进行中”区域
    data.context.remove();
    updateLoadingCount(false);
    if ($(".loading_mission_count").first().text() == "") {
        $("#dynamic_title_loading").css("display", "none");
    }
    // 更新“已完成”区域
    updateCompleteCount(true);
    $("#dynamic_title_complete").css("display", "block");
    var missionNode = $("<li></li>");
    missionNode.append('<div class="mission-head"><div class="mission-icon-wrapper"><span class="glyphicon glyphicon-upload" style="color: #337ab7;"></span></div><div class="thumb"><img src="' + getFileIcon(data.result.file.fileType) + '" class="thumb-icon"></div><div class="mission-info"><span class="mission-file-name">' + data.files[0].name + '</span><span class="mission-file-size">' + getReadableSize(data.files[0].size) + '</span></div></div>');
    missionNode.append('<div class="mission-complete"></div>');
    missionNode.append('<div class="mission-control"><a class="remove-record"><span class="glyphicon glyphicon-remove"></span></a></div>');
    data.context = missionNode;
    // 移除记录按钮功能实现
    data.context.find(".remove-record").click(function () {
        data.context.remove();
        updateCompleteCount(false);
        if ($("#complete_mission_count").text() == "0") {
            $("#dynamic_title_complete").css("display", "none");
        }
    });

    missionNode.appendTo("#complete_mission_list");
}
/* 上传结束后，生成所有所需的节点 */
function finishUpload(data) {
    // 修改用户容量
    var cap = data.result.user.usedSize;
    sessionStorage.setItem("user_usedSize", cap);
    var percentage = getUsedPercentage(cap, sessionStorage.getItem("user_memorySize"));
    $("#user_capacity").css("width", percentage).text(percentage);

    generateCompletedMissionNode(data);
    // 生成主界面的文件节点
    var fileName = data.result.file.fileName + "." + data.result.file.fileType;
    var fileImg;
    if (isPicture(data.result.file.fileType)) {
        fileImg = data.result.file.fileUrl;
    } else {
        fileImg = getFileIcon(data.result.file.fileType);
    }
    var fileSize = getReadableSize(data.result.file.fileSize);
    var modifyTime = getFormattedDateTime(data.result.file.modifyTime);
    var id = data.result.file.originFileId;
    var fileNode = $('<li class="disk-file-item disk-item"></li>');
    fileNode.append('<div class="file-head"><div class="select"><input type="checkbox"></div><div class="thumb"><img src="' + fileImg + '" class="thumb-icon"></div><div class="file-title"><span class="file-name">' + fileName + '</span></div></div>');
    fileNode.append('<div class="file-info"><span class="file-size">' + fileSize + '</span><span class="file-time">' + modifyTime + '</span></div>');
    fileNode.attr("data-file-id", id);

    var parent = data.result.file.parent;
    var parentNode = $('ul[data-folder-id="' + parent + '"]');
    fileNode.appendTo(parentNode);
}