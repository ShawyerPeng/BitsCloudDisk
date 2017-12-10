package service.impl;

import mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import po.Image;
import service.FileUploadService;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public List<Image> listFiles() {
        return new ArrayList<Image>();
    }

    @Override
    public Image getFile(Integer id) {
        return imageMapper.selectByPrimaryKey(id);
    }

    @Override
    public Image saveFile(Image image) {
        imageMapper.insert(image);
        return image;
    }
}
