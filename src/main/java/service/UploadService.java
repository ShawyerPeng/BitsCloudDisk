package service;

import model.OriginFile;
import model.UserFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

/**
 * 文件上传相关
 */
public interface UploadService extends FileService {
    /**
     * 检查md5值对应的文件是否存在，若存在则插入、更新相应的数据，不再继续上传；否则继续上传
     */
    Map<String, Object> serveFirstPart(Part part, String fileMd5, UserFile userFile) throws IOException;

    /**
     * 保存最后一个文件块，并插入、更新相应的数据
     */
    Map<String, Object> serveLastPart(UserFile userFile, OriginFile originFile) throws IOException;

    /**
     * 保存文件块到文件系统
     */
    void savePart(Part part, String fileMd5) throws IOException;

    /**
     * 保存小文件
     */
    Map<String, Object> serveSmallFile(Part part, String fileMd5, UserFile userFile) throws IOException;

    /**
     * 取消上传，删除上传到一半的文件
     */
    void cancel(String fileMd5);

    /**
     * 获取上传到一半的文件的大小，返回给客户端以实现断点续传
     */
    Long resume(String fileMd5);
}