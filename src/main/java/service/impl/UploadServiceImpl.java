package service.impl;

import mapper.OriginFileMapper;
import mapper.UserFileMapper;
import mapper.UserMapper;
import model.OriginFile;
import model.User;
import model.UserFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import util.object.DTOConvertUtil;

import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传相关，文件以FILE_BASE
 */
@Service
public class UploadServiceImpl implements UploadService {
    private static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OriginFileMapper originFileMapper;
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private DTOConvertUtil convertor;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Map<String, Object> serveFirstPart(Part part, String fileMd5, UserFile userFile) throws IOException {
        OriginFile file = originFileMapper.getByFileMd5(fileMd5);
        if (file != null) {
            // 服务器存在该文件，执行秒传逻辑
            Map<String, Object> result = new HashMap<>();

            userFile.setOriginId(file.getOriginFileId());
            // 检查客户端上传路径下是否存在同名文件
            UserFile duplicate = userFileMapper.getByPath(userFile.getUserId()
                    , userFile.getParentId(), userFile.getFileName(), userFile.getFileType());
            while (duplicate != null) {
                /*
                 * 如果存在同名文件，且两文件对应的真实文件相同，则返回相应的业务状态码和消息，在客户端执行对应的操作
                 * 如果存在同名文件，但两文件对应的真实文件不同，则为上传文件的文件名添加数字下标，然后继续检查新文件名是否重名，直到不存在重名文件为止
                 */
                if (duplicate.getFileId() == userFile.getFileId()) {
                    result.put("status_code", 111);
                    result.put("msg", "file already exists");
                    return result;
                } else {
                    userFile.setFileName(resolveFileNameConflict(userFile.getFileName()));
                    duplicate = userFileMapper.getByPath(userFile.getUserId()
                            , userFile.getParentId(), userFile.getFileName(), userFile.getFileType());
                }
            }
            // 新建一行UserFile数据，并更新用户使用空间
            userFile.setCreateTime(new Date());
            userFile.setModifyTime(userFile.getCreateTime());
            userFileMapper.insert(userFile);

            result.put("status_code", 222);
            System.out.println("222");
            result.put("msg", "instant uploading");
            result.put("file", convertor.convertToDTO(userFile, file));
            result.put("user", convertor.convertToDTO(updateUserCap(userFile.getUserId(), file.getFileSize(), true)));
            return result;
        }
        // 服务器不存在该文件，开始文件上传
        savePart(part, fileMd5);
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Map<String, Object> serveLastPart(UserFile userFile, OriginFile file) throws IOException {
        // 保存新上传文件的信息
        file.setFileUrl(URL_ROOT + file.getFileMd5());
        file.setCreateTime(new Date());
        file.setModifyTime(file.getCreateTime());
        originFileMapper.insert(file);

        // 如果存在重名，则为文件名添加数字下标，直到无重名为止
        while (userFileMapper.getByPath(userFile.getUserId()
                , userFile.getParentId(), userFile.getFileName(), userFile.getFileType()) != null) {
            userFile.setFileName(resolveFileNameConflict(userFile.getFileName()));
        }
        // 新建一行UserFile数据，并更新用户使用空间
        userFile.setOriginId(file.getOriginFileId());
        userFile.setCreateTime(new Date());
        userFile.setModifyTime(userFile.getCreateTime());
        userFileMapper.insert(userFile);

        Map<String, Object> result = new HashMap<>();
        result.put("status_code", 333);
        System.out.println("333");
        result.put("msg", "Uploading accomplished");
        result.put("file", convertor.convertToDTO(userFile, file));
        result.put("user", convertor.convertToDTO(updateUserCap(userFile.getUserId(), file.getFileSize(), true)));
        return result;
    }

    /**
     * 保存上传的大文件的分段
     */
    @Override
    public void savePart(Part part, String fileMd5) throws IOException {
        File file = new File(FILE_BASE + fileMd5);
        FileOutputStream fos = new FileOutputStream(file, true);
        BufferedInputStream bis = new BufferedInputStream(part.getInputStream());
        byte[] buf = new byte[1024 * 1024];
        int hasRead;
        while ((hasRead = bis.read(buf)) != -1) {
            fos.write(buf, 0, hasRead);
        }
        bis.close();
        fos.close();
    }

    /**
     * 上传小文件
     */
    @Override
    @Transactional
    public Map<String, Object> serveSmallFile(Part part, String fileMd5, UserFile userFile) throws IOException {
        Map<String, Object> result = serveFirstPart(part, fileMd5, userFile);
        if (result == null) {
            OriginFile file = new OriginFile();
            file.setFileMd5(fileMd5);
            file.setFileType(part.getHeader("content-type"));
            file.setFileSize(part.getSize());
            return serveLastPart(userFile, file);
        } else {
            return result;
        }
    }

    /**
     * 取消上传文件
     */
    @Override
    @Transactional(readOnly = true)
    public synchronized void cancel(String fileMd5) {
        if (originFileMapper.getByFileMd5(fileMd5) == null) {
            File file = new File(FILE_BASE + fileMd5);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 继续上传文件
     */
    @Override
    public Long resume(String fileMd5) {
        File file = new File(FILE_BASE + fileMd5);
        if (file.exists()) {
            long uploadedBytes = file.length();
            System.out.println(uploadedBytes);
            return uploadedBytes;
        }
        return null;
    }

    /**
     * 当文件上传路径发生重名时，为文件名添加一个数字下标防止冲突
     *
     * @param fileName 原文件名
     * @return 原文件名加上一个数字下标的字符串
     */
    private String resolveFileNameConflict(String fileName) {
        if (fileName.length() > 2) {
            String end = fileName.substring(fileName.length() - 3);
            // 判断localName是否以形如“(n)”的字符串结尾
            if (end.matches("\\(\\d\\)") && end.charAt(1) != '0') {
                int i = Integer.parseInt(end.substring(1, 2));// 括号中间的数字
                i++;
                StringBuilder sb = new StringBuilder();
                sb.append(fileName.substring(0, fileName.length() - 3));
                sb.append('(').append(i).append(')');

                return sb.toString();
            }
        }
        return fileName + "(1)";
    }

    /**
     * 更新ID=userID的用户所使用的空间，更新量为size
     *
     * @param plus = true 增加使用空间，plus=false减少使用空间
     * @return 更新后的用户对象
     */
    private User updateUserCap(Integer userId, Long size, boolean plus) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (plus) {
            user.setUsedSize(user.getUsedSize() + size);
        } else {
            user.setUsedSize(user.getUsedSize() - size);
        }
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }
}