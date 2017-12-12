package controller;

import model.OriginFile;
import model.UserFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import controller.reqbody.UploadReqBody;
import service.DownloadService;
import service.UploadService;
import service.UserService;
import util.validation.ParamChecker;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@MultipartConfig
public class FileController {
    @Autowired
    private UserService userService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ParamChecker paramChecker;

    /**
     * 分块文件的大小，与前端保持一致
     */
    private static final long MAX_CHUNK_SIZE = 102400;

    /**
     * 功能：接收分块上传的文件块
     * 示例：POST users/admin/disk/files
     */
    @RequestMapping(value = "/users/{username}/disk/files", method = RequestMethod.POST)
    public Map<String, Object> upload(HttpServletRequest req, @RequestPart("files[]") Part part, @Valid UploadReqBody reqbody) throws IOException {
        // TODO 参数校验
        System.out.println(req.getHeader("content-range"));
        String contentRange = req.getHeader("content-range");
        
        // 检查云盘存储空间是否足够
        long size;
        if (contentRange != null) {
            size = Long.parseLong(contentRange.split("/")[1]);
        } else {
            size = part.getSize();
        }
        if (!paramChecker.isUserStorageEnough(reqbody.getUserId(), size)) {
            throw new IllegalArgumentException("Not enough storage to upload this file");
        }
        
        // 处理没有Content-Range请求头的小文件
        if (contentRange == null && part.getSize() <= MAX_CHUNK_SIZE) {
            UserFile userFile = modelMapper.map(reqbody, UserFile.class);
            return uploadService.serveSmallFile(part, reqbody.getFileMd5(), userFile);
        }

        if (isFirstPart(contentRange)) {
            UserFile localFile = modelMapper.map(reqbody, UserFile.class);
            return uploadService.serveFirstPart(part, reqbody.getFileMd5(), localFile);
        } else if (isLastPart(contentRange)) {
            OriginFile file = new OriginFile();
            file.setFileMd5(reqbody.getFileMd5());
            file.setFileType(part.getHeader("content-type"));
            file.setFileSize(size);
            UserFile userFile = modelMapper.map(reqbody, UserFile.class);
            uploadService.savePart(part, reqbody.getFileMd5());
            return uploadService.serveLastPart(userFile, file);
        } else {
            uploadService.savePart(part, reqbody.getFileMd5());
            return null;
        }
    }

    /**
     * 功能：取消上传
     * 示例：DELETE users/admin/disk/files?cancel=abcd，取消上传md5值为“abcd”的文件
     */
    @RequestMapping(value = "/users/{username}/disk/files", method = RequestMethod.DELETE, params = "cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelUpload(@RequestParam String cancel) throws InterruptedException {
        // TODO 参数校验
        uploadService.cancel(cancel);
    }

    /**
     * 功能：获取上传到一半的文件断点
     * 示例：GET users/admin/disk/files?resume=abcd，获取md5值为“abcd”的文件的断点
     */
    @RequestMapping(value = "/users/{username}/disk/files", method = RequestMethod.GET, params = "resume")
    public Long resumeUpload(@RequestParam String resume) {
        // TODO 参数校验
        return uploadService.resume(resume);
    }

    /**
     * 功能：下载
     * 示例：GET users/admin/disk/files?files=1,2&folders=3,4，打包下载ID=1,2的文件和ID=3,4的文件夹
     */
    @RequestMapping(value = "/users/{username}/disk/files", method = RequestMethod.GET, params = {"files", "folders"})
    public void download(@PathVariable String username, @RequestParam List<Integer> files, @RequestParam List<Integer> folders, HttpServletResponse response) throws IOException {
        int userId = userService.getUserByUsername(username).getUserId();
        // TODO 参数校验
        if (files.size() + folders.size() == 0) {
            throw new IllegalArgumentException("params must contain at least one file or folder");
        }

        String filename = downloadService.generateZipFileName(files, folders);
        filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        response.setContentType("application/octet-stream;");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);

        downloadService.download(userId, files, folders, response.getOutputStream());
    }


    /**
     * 根据Content-Range请求头(如：bytes 0-9999/312329)，判断文件块是否是首个块（chunk）
     */
    private boolean isFirstPart(String contentRange) {
        return contentRange.charAt(contentRange.indexOf(' ') + 1) == '0';
    }

    /**
     * 根据Content-Range请求头(如：bytes 310000-312328/312329)，判断文件块是否是最后一个块（chunk）
     */
    private boolean isLastPart(String contentRange) {
        String[] validPart = contentRange.substring(contentRange.indexOf('-') + 1).split("/");
        int current = Integer.parseInt(validPart[0]);
        int size = Integer.parseInt(validPart[1]);
        return size - 1 == current;
    }
}