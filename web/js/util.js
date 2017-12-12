/* 计算已用空间所占百分比 */
function getUsedPercentage(bytes, all) {
    // var percentage = Math.round(bytes / (1024 * 1024 * 10) * 100);// 10MB
    var percentage = Math.round(bytes / all * 100);// 10MB
    return percentage + "%";
}
/* 把字节数转为易读的单位 */
function getReadableSize(bytes) {
    if (Number(bytes) == 0) {
        return "0.0 B"
    }
    var s = ['B', 'K', 'M', 'G', 'T', 'P'];
    var e = Math.floor(Math.log(bytes) / Math.log(1024));
    return (bytes / Math.pow(1024, Math.floor(e))).toFixed(1) + " " + s[e];
}

function getFilenameWithoutSuffix(filename) {
    var pos = filename.lastIndexOf(".");
    if (pos > 0 && pos < filename.length - 1) {
        return filename.substring(0, pos);
    }
    return filename;
}

function getSuffix(filename) {
    var suffix = "";
    var pos = filename.lastIndexOf(".");
    if (pos > 0 && pos < filename.length - 1) {
        suffix = filename.substring(pos + 1);
    }
    return suffix;
}

function getFileIcon(suffix) {
    suffix = suffix.toLowerCase();
    var src = "img/icon/";
    switch (suffix) {
        case "jpg":
        case "png":
        case "gif":
        case "jpeg":
            src += "picture";
            break;
        case "doc":
        case "docx":
            src += "word";
            break;
        case "ppt":
            src += "ppt";
            break;
        case "xls":
            src += "xls";
            break;
        case "txt":
            src += "txt";
            break;
        case "mp4":
            src += "video";
            break;
        case "mp3":
            src += "music";
            break;
        default:
            src += "file";
    }
    src += ".png";
    return src;
}

function getFormattedDateTime(date) {
    function addZero(num) {
        if (num < 10) {
            num = "0" + num;
        }
        return num;
    }

    var dateObj = new Date(date);

    var year = dateObj.getFullYear();
    var month = addZero(dateObj.getMonth() + 1);
    var day = addZero(dateObj.getDate());
    var hour = addZero(dateObj.getHours());
    var minute = addZero(dateObj.getMinutes());

    var formattedString = year + "-" + month + "-" + day + " " + hour + ":" + minute;
    return formattedString;
}
/* 获取今天比输入的date晚了几天 */
function getGapDays(date) {
    var today = new Date();
    var dateObj = new Date(date);
    var gapDaysTime = today.getTime() - dateObj.getTime();
    var gapDays = Math.floor(gapDaysTime / (24 * 3600 * 1000));
    return gapDays;
}

function isPicture(fileType) {
    if ("JPG" == fileType.toUpperCase() || "PNG" == fileType.toUpperCase() || "GIF" == fileType.toUpperCase() || "JPEG" == fileType.toUpperCase()) {
        return true;
    } else {
        return false;
    }
}

/*
 * 测试函数
 * 打印js对象
 */
function printObj(obj) {
    var output = "";
    for (var i in obj) {
        var property = obj[i];
        output += i + " = " + property + "\n";
    }
    alert(output);
}