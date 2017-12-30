package controller;

import common.ResponseResult;
import model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.ImageService;
import util.file.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/{md5}", method = RequestMethod.GET)
    public String getContent(@PathVariable("md5") String md5) {
        Image image = imageService.selectByMd5(md5);
        return image.getUrl();
    }

    @RequestMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile pictureFile) throws Exception {
        String FILEPATH = "D:/images/";
        String originalFilename = "";
        String newFileName = "";
        String md5 = null;

        // 进行图片的上传
        if (pictureFile != null && pictureFile.getOriginalFilename() != null && pictureFile.getOriginalFilename().length() > 0) {
            // 图片上传成功后，将图片的地址写到数据库
            originalFilename = pictureFile.getOriginalFilename();
            newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

            // 新文件
            File file = new File(FILEPATH + newFileName);
            // 将内存中的文件写入磁盘
            pictureFile.transferTo(file);

            md5 = FileUtil.getFileMD5(file);
            System.out.println("md5:                     !!!!!!!!!!!!!!!! " + md5);
        }

        imageService.insertImage(originalFilename, md5, "http://localhost:8080/images/" + newFileName);

        Map<String, Object> data = new HashMap<>();
        data.put("md5", md5);
        data.put("url", "http://localhost:8080/images/" + newFileName);

        return ResponseResult.builder().code(201).message("获取成功").data(data).build();
    }

    @RequestMapping("/uploadMultipart")
    public String uploadMultipart(@RequestParam("file") MultipartFile[] pictureFile) throws Exception {
        String FILEPATH = "D:/images/";
        String originalFilename = "";
        String newFileName = "";
        String md5 = null;

        // 进行图片的上传
        for (int i = 0; i < pictureFile.length; i++) {
            if (pictureFile[i] != null && pictureFile[i].getOriginalFilename() != null && pictureFile[i].getOriginalFilename().length() > 0) {
                originalFilename = pictureFile[i].getOriginalFilename();
                newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

                File file = new File(FILEPATH + newFileName);
                pictureFile[i].transferTo(file);

                md5 = FileUtil.getFileMD5(file);
            }
            imageService.insertImage(originalFilename, md5, "http://localhost:8080/images/" + newFileName);
        }

        return "index";
    }
}