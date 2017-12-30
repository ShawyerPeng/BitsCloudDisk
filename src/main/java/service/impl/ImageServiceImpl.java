package service.impl;

import mapper.ImageMapper;
import model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public int insertImage(String name, String url, String md5) {
        Image image = new Image(name, url, md5);
        return imageMapper.insert(image);
    }

    @Override
    public Image selectByMd5(String md5) {
        return imageMapper.selectByMd5(md5);
    }
}