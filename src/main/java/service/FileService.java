package service;

import model.UserFile;

import java.io.File;

/**
 * 文件上传、下载相关
 */
public interface FileService {
    /**
     * 服务端保存所有文件的根路径
     */
    String FILE_BASE = "D:/images" + File.separator;
    /**
     * 所有上传文件URL的根
     */
    String URL_ROOT = "http://localhost:8080/";

    default String getFullFilename(UserFile userFile) {
        return userFile.getFileName() + "." + userFile.getFileType();
    }
}