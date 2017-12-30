package service;

import model.Image;

public interface ImageService {
    int insertImage(String name, String url, String md5);

    Image selectByMd5(String md5);
}