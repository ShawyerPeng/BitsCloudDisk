package util.object;

import dto.UserFolderDTO;
import dto.UserDTO;
import dto.UserFileDTO;
import mapper.OriginFileMapper;
import model.OriginFile;
import model.User;
import model.UserFile;
import model.UserFolder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 将DO转换为DTO的工具类
 */
@Component
public class DTOConvertUtil {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OriginFileMapper originFileMapper;

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserFolderDTO convertToDTO(UserFolder entity) {
        return modelMapper.map(entity, UserFolderDTO.class);
    }


    @Transactional(readOnly = true)
    public UserFileDTO convertToDTO(UserFile localFileEntity, OriginFile fileEntity) {
        UserFileDTO dto = modelMapper.map(localFileEntity, UserFileDTO.class);
        if (fileEntity == null) {
            fileEntity = originFileMapper.selectByPrimaryKey(localFileEntity.getFileId());
        }
        dto.setFileSize(fileEntity.getFileSize());
        dto.setFileUrl(fileEntity.getFileUrl());
        return dto;
    }

    public List<UserFileDTO> convertFileList(List<UserFile> entityList) {
        if (entityList != null) {
            List<UserFileDTO> dtoList = new ArrayList<>();
            for (UserFile entity : entityList) {
                UserFileDTO dto = convertToDTO(entity, null);
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            return null;
        }
    }

    public List<UserFolderDTO> convertFolderList(List<UserFolder> entityList) {
        if (entityList != null) {
            List<UserFolderDTO> dtoList = new ArrayList<>();
            for (UserFolder entity : entityList) {
                UserFolderDTO dto = convertToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            return null;
        }
    }
}