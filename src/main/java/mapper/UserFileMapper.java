package mapper;

import model.UserFile;
import model.UserFileExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {
    int countByExample(UserFileExample example);

    int deleteByExample(UserFileExample example);

    int deleteByPrimaryKey(Integer fileId);

    int insert(UserFile record);

    int insertSelective(UserFile record);

    List<UserFile> selectByExample(UserFileExample example);

    UserFile selectByPrimaryKey(Integer fileId);

    int updateByExampleSelective(@Param("record") UserFile record, @Param("example") UserFileExample example);

    int updateByExample(@Param("record") UserFile record, @Param("example") UserFileExample example);

    int updateByPrimaryKeySelective(UserFile record);

    int updateByPrimaryKey(UserFile record);

    /**
     * 根据参数获取某个文件
     */
    UserFile getByPath(@Param("userId") Integer userId, @Param("parentId") Integer parentId,
                       @Param("fileName") String fileName, @Param("fileType") String fileType);

    /**
     * 根据目录ID列出文件列表
     */
    List<UserFile> listByFolderId(@Param("userId") Integer userId, @Param("folderId") Integer folderId);

    /**
     * 根据父级目录ID列出文件列表
     */
    List<UserFile> listByParentId(@Param("userId") Integer userId, @Param("parentId") Integer parentId);

    /**
     * 列出最近7天的文件列表
     */
    List<UserFile> listRecentFile(Integer userId);

    /**
     * 列出相似(like)名称的文件（搜索）
     */
    List<UserFile> listByName(@Param("userId") Integer userId, @Param("name") String name);

    /**
     * 列出所有文件类型在fileTypes范围内的文件
     */
    List<UserFile> listByFileType(@Param("userId") Integer userId, @Param("fileTypes") String[] fileTypes);
}