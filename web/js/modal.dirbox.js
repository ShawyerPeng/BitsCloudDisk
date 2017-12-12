$(function(){
	// 为根节点绑定事件处理函数
	$(".dirbox-body .treeNode-info").click(showSubDir);
});
/*
 * 单击模态框的文件夹节点时绑定的事件处理函数
 */
function showSubDir() {
	var dirId = $(this).attr("data-folder-id");
	// 把操作路径的文件夹ID设为当前点击文件夹的ID
	$("#dirbox_path").attr("data-folder-id", dirId);

	// 判断节点是否存在，如果不存在则生成，存在则显示
	if (getFolderNode(dirId) == null ) {
		createFolderNode(dirId, false);
	} else {
		toggleExistSubDir($(this));
	}
}
/*
 * 显示/隐藏已经生成过的子文件夹并更新操作路径
 */
function toggleExistSubDir(dir) {
	// 更新文件夹图标
	var icon = dir.find(".glyphicon");
	if (icon.hasClass("glyphicon-folder-open")) {
		icon.removeClass("glyphicon-folder-open");
		icon.addClass("glyphicon-folder-close");
	} else {
		icon.removeClass("glyphicon-folder-close");
		icon.addClass("glyphicon-folder-open");			
	}
	// 显示或隐藏子文件夹
	dir.next().slideToggle("fast");
	// 更新操作路径
	var path = dir.find(".treeNode-info-name").text();
	if (path != "我的网盘") {
		var parentNode = dir;
		do {
			parentNode = parentNode.parent().parent().prev();
			var parent = parentNode.find(".treeNode-info-name").text();
			path = parent + " \\ " + path;
		} while (parent != "我的网盘");
	}
	$("#dirbox_path").text(path);
}
/*
 * 供file-system.js中的createFolderNode()函数和创建文件夹时调用
 * 生成主页面文件夹节点的同时会生成模态框文件夹节点
 */ 
function createDirNode(folderId, folders, show) {
	var dir = $('div[data-folder-id="' + folderId + '"]');
	var subDirs = dir.next();
	var paddingParam = parseInt(dir.css("padding-left"), 10) + 20 + "px";
	for (var i = 0; i < folders.length; i++) {
        var id = folders[i].folderId;
        var folderName = folders[i].folderName;
		var subNode = $("<li></li>");
		subNode.append('<div class="treeNode-info"><span class="glyphicon glyphicon-folder-close"></span><span class="treeNode-info-name">' + folderName + '</span></div>');
		subNode.append('<ul style="display: none;"></ul>');
		subNode.find(".treeNode-info").css("padding-left", paddingParam);
		subNode.find(".treeNode-info").attr("data-folder-id", id);
		subNode.find(".treeNode-info").click(showSubDir);
		subNode.appendTo(subDirs);
	}
	// 因为ajax的原因，判断是否显示的逻辑应该在本函数内实现
	if (show) {
		toggleExistSubDir(dir);
	}
}

/*
 * 模态框取消按钮和x按钮绑定的事件处理函数
 */ 
function removeAddedMission() {
	// 移除所有可操作按钮绑定的事件
	$("#modal_btn_submit").off("click");
	$("#modal_btn_cancel").off("click");
	$("#modal_btn_close").off("click");
}

/*
 * 显示文件上传模态框
 */
function showFileUploadModal(files) {
	$("#dirbox_header_action").text("上传");
	$("#modal_btn_submit").text("开始上传");

	var firstFilename = files[0].name;
	var nums = files.length;
	// 显示要上传的文件信息和数量
	$("#modal_file_thumb").attr("src", getFileIcon(getSuffix(firstFilename)));
	$("#modal_file_name").text(firstFilename);
	if (nums == 1) {
		$("#modal_addtional_info").hide();
	} else {
		$("#modal_addtional_info span").text(nums);
		$("#modal_addtional_info").show();
	}
	$("#path_modal").modal("show");
	$("#modal_btn_cancel").click(removeAddedMission);
	$("#modal_btn_close").click(removeAddedMission);
	$("#modal_btn_submit").click(function() {
		$("#path_modal").modal("hide");
		// 上传按钮单击后移除所有绑定的事件
		$("#modal_btn_submit").off("click");
	});
}