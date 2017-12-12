package service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.dto.UserFolderDTO;
import service.dto.UserDTO;
import service.dto.UserFileDTO;
import mapper.OriginFileMapper;
import mapper.UserFileMapper;
import mapper.UserFolderMapper;
import mapper.UserMapper;
import model.OriginFile;
import model.User;
import model.UserFile;
import model.UserFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.DiskService;
import util.SortUtil;
import util.object.DTOConvertUtil;

import java.util.*;

@Transactional
@Service
public class DiskServiceImpl implements DiskService {
    private static Logger logger = LoggerFactory.getLogger(DiskServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OriginFileMapper originFileMapper;
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private UserFolderMapper userFolderMapper;
    @Autowired
    private SortUtil sorter;
    @Autowired
    private DTOConvertUtil convertor;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getMenuContents(Integer userId, String menu) {
        List<UserFile> files = null;
        // 默认按字母排序
        int sortType = 0;
        switch (menu) {
            case "recent":
                files = userFileMapper.listRecentFile(userId);
                // 按时间排序
                sortType = 1;
                break;
            case "doc":
                String[] docTypeArray = {"txt", "doc", "docx", "ppt", "xls"};
                files = userFileMapper.listByFileType(userId, docTypeArray);
                break;
            case "photo":
                String[] photoTypeArray = {"jpeg", "jpg", "png", "gif"};
                files = userFileMapper.listByFileType(userId, photoTypeArray);
                break;
            case "video":
                String[] videoTypeArray = {"mp4"};
                files = userFileMapper.listByFileType(userId, videoTypeArray);
                break;
            case "audio":
                String[] audioTypeArray = {"mp3"};
                files = userFileMapper.listByFileType(userId, audioTypeArray);
                break;
            case "disk":
                return getFolderContents(userId, 1, 0);
            case "recycle":
                return getFolderContents(userId, 3, 0);
            case "safebox":
                // TODO 功能未实现
                return getFolderContents(userId, 2, 0);
            case "share":
                // TODO 功能未实现
                break;
        }

        if (files == null) {
            throw new IllegalArgumentException("Illegal argument: " + menu);
        }

        List<UserFileDTO> fileDTOList = convertor.convertFileList(files);
        UserFileDTO[] fileDTOArray = fileDTOList.toArray(new UserFileDTO[fileDTOList.size()]);
        //sorter.sort(fileDTOArray, sortType);

        Map<String, Object> result = new HashMap<>();
        result.put("files", fileDTOArray);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFolderContents(Integer userId, Integer folderId, Integer sortType) {
        //// 设置查询参数
        //Map<String, Object> queryParam = new HashMap<>();
        //// 这三个文件夹ID为公用ID，分别代表网盘，保险箱，回收站
        //if (folderId == 1 || folderId == 2 || folderId == 3) {
        //    queryParam.put("userId", userId);
        //}
        //queryParam.put("parent", folderId);

        List<UserFolder> folderList = userFolderMapper.listByParentId(userId, folderId);
        //for (UserFolder folder:folderList) {
        //    logger.error("!!!!!!!!!!!!!!!!!!!!!!   ParentId:" + folder.getParentId() + "  folderId" + folder.getFolderId());
        //}
        List<UserFolderDTO> folderDTOList = convertor.convertFolderList(folderList);
        UserFolderDTO[] folderDTOArray = folderDTOList.toArray(new UserFolderDTO[folderDTOList.size()]);
        //sorter.sort(folderDTOArray, sortType);

        List<UserFile> fileList = userFileMapper.listByParentId(userId, folderId);
        //for (UserFile file:fileList) {
        //    logger.error("!!!!!!!!!!!!!!!!!!!!!!   " + file.getParentId()+ "  fileId" + file.getFileId());
        //}
        List<UserFileDTO> fileDTOList = convertor.convertFileList(fileList);
        UserFileDTO[] fileDTOArray = fileDTOList.toArray(new UserFileDTO[fileDTOList.size()]);
        //sorter.sort(fileDTOArray, sortType);

        Map<String, Object> result = new HashMap<>();
        result.put("folders", folderDTOArray);
        result.put("files", fileDTOArray);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> search(Integer userId, String input) {
        List<UserFolder> localFolderList = userFolderMapper.listByName(userId, input);
        List<UserFolderDTO> folderDTOList = convertor.convertFolderList(localFolderList);

        List<UserFile> localFileList = userFileMapper.listByName(userId, input);
        List<UserFileDTO> fileDTOList = convertor.convertFileList(localFileList);

        Map<String, Object> result = new HashMap<>();
        result.put("folders", folderDTOList);
        result.put("files", fileDTOList);
        return result;
    }

    @Override
    public Map<String, Object> move(List<Integer> folders, List<Integer> files, Integer dest) {
        List<UserFolder> localFolderList = new ArrayList<>();
        for (int i = 0; i < folders.size(); i++) {
            UserFolder localFolder = userFolderMapper.selectByPrimaryKey(folders.get(i));
            localFolder.setParentId(dest);
            localFolder.setModifyTime(new Date());
            userFolderMapper.updateByPrimaryKeySelective(localFolder);
            localFolderList.add(localFolder);
        }
        List<UserFolderDTO> folderDTOList = convertor.convertFolderList(localFolderList);

        List<UserFile> localFileList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            UserFile localFile = userFileMapper.selectByPrimaryKey(files.get(i));
            localFile.setParentId(dest);
            localFile.setModifyTime(new Date());
            userFileMapper.updateByPrimaryKeySelective(localFile);
            localFileList.add(localFile);
        }
        List<UserFileDTO> fileDTOList = convertor.convertFileList(localFileList);

        Map<String, Object> result = new HashMap<>();
        result.put("folders", folderDTOList);
        result.put("files", fileDTOList);
        return result;
    }

    @Override
    public UserFolderDTO renameFolder(Integer folderId, String fileName) {
        UserFolder folder = userFolderMapper.selectByPrimaryKey(folderId);
        folder.setFolderName(fileName);
        folder.setModifyTime(new Date());
        userFolderMapper.updateByPrimaryKeySelective(folder);

        return convertor.convertToDTO(folder);
    }

    @Override
    public UserFileDTO renameFile(Integer originFileId, String fileName, String fileType) {
        UserFile file = userFileMapper.selectByPrimaryKey(originFileId);
        file.setFileName(fileName);
        file.setFileType(fileType);
        file.setModifyTime(new Date());
        userFileMapper.updateByPrimaryKeySelective(file);

        return convertor.convertToDTO(file, null);
    }

    @Override
    public UserFolderDTO newFolder(UserFolder unsaved) {
        unsaved.setCreateTime(new Date());
        unsaved.setModifyTime(unsaved.getCreateTime());
        userFolderMapper.insert(unsaved);

        return convertor.convertToDTO(unsaved);
    }

    @Override
    public UserDTO shred(List<Integer> folders, List<Integer> files, Integer userId) {
        // 统计删除的总字节
        long removedCap = 0L;

        // 删除文件
        for (int i = 0; i < files.size(); i++) {
            UserFile localFile = userFileMapper.selectByPrimaryKey(files.get(i));
            OriginFile file = originFileMapper.selectByPrimaryKey(localFile.getFileId());
            removedCap += file.getFileSize();
            userFileMapper.deleteByPrimaryKey(localFile.getFileId());
        }

        // 删除文件夹和该文件夹内的所有子文件夹，以及它们包含的文件
        for (int i = 0; i < folders.size(); i++) {
            Queue<Integer> queue = new LinkedList<>();
            queue.add(folders.get(i));
            while (!queue.isEmpty()) {
                Integer parent = queue.poll();
                List<UserFile> localFileList = userFileMapper.listByParentId(userId, parent);
                for (UserFile localFile : localFileList) {
                    OriginFile file = originFileMapper.selectByPrimaryKey(localFile.getFileId());
                    removedCap += file.getFileSize();
                    userFileMapper.deleteByPrimaryKey(localFile.getFileId());
                }

                List<UserFolder> localFolderList = userFolderMapper.listByParentId(userId, parent);
                for (UserFolder localFolder : localFolderList) {
                    queue.add(localFolder.getFolderId());
                }
                userFolderMapper.deleteByPrimaryKey(userFolderMapper.selectByPrimaryKey(parent).getFolderId());
            }
        }

        // 更新用户已用空间信息
        User user = userMapper.selectByPrimaryKey(userId);
        user.setUsedSize(user.getUsedSize() - removedCap);
        userMapper.updateByPrimaryKey(user);
        return convertor.convertToDTO(user);
    }
}