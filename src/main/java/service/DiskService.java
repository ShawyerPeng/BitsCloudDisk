package service;

import dto.UserDTO;
import dto.UserFileDTO;
import dto.UserFolderDTO;
import model.UserFolder;

import java.util.List;
import java.util.Map;

/**
 * 文件 I/O 操作相关
 */
public interface DiskService {
    Map<String, Object> getMenuContents(int userId, String menu);

    Map<String, Object> getFolderContents(int userId, int folderId, int sortType);

    Map<String, Object> search(int userId, String input);

    Map<String, Object> move(List<Integer> folders, List<Integer> files, int dest);

    UserFolderDTO renameFolder(int folderId, String fileName);

    UserFileDTO renameFile(int fileId, String fileName, String fileType);

    UserFolderDTO newFolder(UserFolder unsaved);

    UserDTO shred(List<Integer> folders, List<Integer> files, int userId);
}