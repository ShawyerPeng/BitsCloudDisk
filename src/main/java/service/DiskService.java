package service;

import service.dto.UserDTO;
import service.dto.UserFileDTO;
import service.dto.UserFolderDTO;
import model.UserFolder;

import java.util.List;
import java.util.Map;

/**
 * 文件 I/O 操作相关
 */
public interface DiskService {
    Map<String, Object> getMenuContents(Integer userId, String menu);

    Map<String, Object> getFolderContents(Integer userId, Integer folderId, Integer sortType);

    Map<String, Object> search(Integer userId, String input);

    Map<String, Object> move(List<Integer> folders, List<Integer> files, Integer dest);

    UserFolderDTO renameFolder(Integer folderId, String fileName);

    UserFileDTO renameFile(Integer fileId, String fileName, String fileType);

    UserFolderDTO newFolder(UserFolder unsaved);

    UserDTO shred(List<Integer> folders, List<Integer> files, Integer userId);
}