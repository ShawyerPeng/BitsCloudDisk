package service.impl;

import entity.UserFile;
import mapper.OriginFileMapper;
import mapper.UserFileMapper;
import mapper.UserMapper;
import model.OriginFile;
import model.User;
import model.UserFile;
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

@Service
public class UploadServiceImpl implements UploadService {
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
    public synchronized Map<String, Object> serveFirstPart(Part part, String md5, UserFile localFile) throws IOException {
        OriginFile file = originFileMapper.getByFileMd5(md5);
        if (file != null) {
            /* 服务器存在该文件，执行秒传逻辑 */
            Map<String, Object> result = new HashMap<>();

            localFile.setFileId(file.getOriginFileId());
            /* 检查客户端上传路径下是否存在同名文件 */
            UserFile duplicate = userFileMapper.getByPath(localFile.getUserId()
                    , localFile.getParentId(), localFile.getFileName(), localFile.getFileType());
            while (duplicate != null) {
                /*
                 * 如果存在同名文件，且两文件对应的真实文件相同，则返回相应的业务状态码和消息，在客户端执行对应的操作
                 * 如果存在同名文件，但两文件对应的真实文件不同，则为上传文件的文件名添加数字下标，然后继续检查新文件名是否重名，直到不存在重名文件为止
                 */
                if (duplicate.getFileId() == localFile.getFileId()) {
                    result.put("status_code", 111);
                    System.out.println("111");
                    result.put("msg", "localfile already exists");
                    return result;
                } else {
                    localFile.setFileName(resolveLocalNameConflict(localFile.getFileName()));
                    duplicate = userFileMapper.getByPath(localFile.getUserId()
                            , localFile.getParentId(), localFile.getFileName(), localFile.getFileType());
                }
            }
            /* 新建一行LocalFile数据，并更新用户使用空间 */
            localFile.setCreateTime(new Date());
            localFile.setModifyTime(localFile.getCreateTime());
            userFileMapper.insert(localFile);

            result.put("status_code", 222);
            System.out.println("222");
            result.put("msg", "instant uploading");
            result.put("file", convertor.convertToDTO(localFile, file));
            result.put("user", convertor.convertToDTO(updateUserCap(localFile.getUserId(), file.getFileSize(), true)));
            return result;
        }
        /* 服务器不存在该文件，开始文件上传 */
        savePart(part, md5);
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized Map<String, Object> serveLastPart(UserFile localFile, OriginFile file) throws IOException {
        /* 保存新上传文件的信息 */
        file.setFileUrl(URL_ROOT + file.getFileMd5());
        file.setCreateTime(new Date());
        file.setModifyTime(file.getCreateTime());
        originFileMapper.insert(file);

        localFile.setFileId(file.getOriginFileId());
        /* 如果存在重名，则为文件名添加数字下标，直到无重名为止 */
        while (userFileMapper.getByPath(localFile.getUserId()
                , localFile.getParentId(), localFile.getFileName(), localFile.getFileType()) != null) {
            localFile.setFileName(resolveLocalNameConflict(localFile.getFileName()));
        }
        /* 新建一行LocalFile数据，并更新用户使用空间 */
        localFile.setCreateTime(new Date());
        localFile.setModifyTime(localFile.getCreateTime());
        userFileMapper.insert(localFile);

        Map<String, Object> result = new HashMap<>();
        result.put("status_code", 333);
        System.out.println("333");
        result.put("msg", "uploading accomplished");
        result.put("file", convertor.convertToDTO(localFile, file));
        result.put("user", convertor.convertToDTO(updateUserCap(localFile.getUserId(), file.getFileSize(), true)));
        return result;
    }

    @Override
    public void savePart(Part part, String md5) throws IOException {
        File file = new File(FILE_BASE + md5);
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

    @Override
    @Transactional
    public Map<String, Object> serveSmallFile(Part part, String md5, UserFile localFile) throws IOException {
        Map<String, Object> result = serveFirstPart(part, md5, localFile);
        if (result == null) {
            OriginFile file = new OriginFile();
            file.setFileMd5(md5);
            file.setFileType(part.getHeader("content-type"));
            file.setFileSize(part.getSize());
            return serveLastPart(localFile, file);
        } else {
            return result;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public synchronized void cancel(String md5) {
        if (originFileMapper.getByFileMd5(md5) == null) {
            File file = new File(FILE_BASE + md5);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public Integer resume(String md5) {
        File file = new File(FILE_BASE + md5);
        if (file.exists()) {
            Integer uploadedBytes = file.length();
            System.out.println(uploadedBytes);
            return uploadedBytes;
        }
        return null;
    }

    /**
     * 当文件上传路径发生重名时，为文件名添加一个数字下标防止冲突
     *
     * @param localName 原文件名
     * @return 原文件名加上一个数字下标的字符串
     */
    private String resolveLocalNameConflict(String localName) {
        if (localName.length() > 2) {
            String end = localName.substring(localName.length() - 3);
            if (end.matches("\\(\\d\\)") && end.charAt(1) != '0') {// 判断localName是否以形如“(n)”的字符串结尾
                int i = Integer.parseInt(end.substring(1, 2));// 括号中间的数字
                i++;
                StringBuilder sb = new StringBuilder();
                sb.append(localName.substring(0, localName.length() - 3));
                sb.append('(').append(i).append(')');

                return sb.toString();
            }
        }
        return localName + "(1)";
    }

    /**
     * 更新ID=userID的用户所使用的空间，更新量为size
     *
     * @param plus = true 增加使用空间，plus=false减少使用空间
     * @return 更新后的用户对象
     */
    private User updateUserCap(Integer userId, Integer size, boolean plus) {
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
