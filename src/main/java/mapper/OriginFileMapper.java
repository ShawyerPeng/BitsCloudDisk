package mapper;

import model.OriginFile;
import model.OriginFileExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OriginFileMapper {
    int countByExample(OriginFileExample example);

    int deleteByExample(OriginFileExample example);

    int deleteByPrimaryKey(Integer originFileId);

    int insert(OriginFile record);

    int insertSelective(OriginFile record);

    List<OriginFile> selectByExample(OriginFileExample example);

    OriginFile selectByPrimaryKey(Integer originFileId);

    int updateByExampleSelective(@Param("record") OriginFile record, @Param("example") OriginFileExample example);

    int updateByExample(@Param("record") OriginFile record, @Param("example") OriginFileExample example);

    int updateByPrimaryKeySelective(OriginFile record);

    int updateByPrimaryKey(OriginFile record);

    /**
     * 通过查询文件的MD5码得到OriginFile
     */
    OriginFile getByFileMd5(String fileMd5);
}