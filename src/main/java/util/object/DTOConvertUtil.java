package util.object;

import org.springframework.beans.factory.annotation.Qualifier;
import service.dto.UserFolderDTO;
import service.dto.UserDTO;
import service.dto.UserFileDTO;
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
    @Qualifier(value = "modelMapper")
    private ModelMapper modelMapper;
    @Autowired
    private OriginFileMapper originFileMapper;

    /**
     * User -> UserDTO
     */
    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * UserFolder -> UserFolderDTO
     */
    public UserFolderDTO convertToDTO(UserFolder userFolder) {
        return modelMapper.map(userFolder, UserFolderDTO.class);
    }

    /**
     * UserFile、OriginFile -> UserFileDTO
     */
    @Transactional(readOnly = true)
    public UserFileDTO convertToDTO(UserFile userFile, OriginFile file) {
        UserFileDTO dto = modelMapper.map(userFile, UserFileDTO.class);
        if (file == null) {
            file = originFileMapper.selectByPrimaryKey(userFile.getOriginId());
        }
        dto.setFileSize(file.getFileSize());
        dto.setFileUrl(file.getFileUrl());
        return dto;
    }

    /**
     * List<UserFile> -> List<UserFileDTO>
     */
    public List<UserFileDTO> convertFileList(List<UserFile> userFileList) {
        if (userFileList != null) {
            List<UserFileDTO> dtoList = new ArrayList<>();
            for (UserFile entity : userFileList) {
                UserFileDTO dto = convertToDTO(entity, null);
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            return null;
        }
    }

    /**
     * List<UserFolder> -> List<UserFolderDTO>
     */
    public List<UserFolderDTO> convertFolderList(List<UserFolder> userFolderList) {
        if (userFolderList != null) {
            List<UserFolderDTO> dtoList = new ArrayList<>();
            for (UserFolder entity : userFolderList) {
                UserFolderDTO dto = convertToDTO(entity);
                dtoList.add(dto);
            }
            return dtoList;
        } else {
            return null;
        }
    }
}