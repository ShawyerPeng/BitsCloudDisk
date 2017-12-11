package service.impl;

import mapper.OriginFileMapper;
import mapper.UserFileMapper;
import mapper.UserFolderMapper;
import model.OriginFile;
import model.UserFile;
import model.UserFolder;
import service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class DownloadServiceImpl implements DownloadService {
    @Autowired
    private OriginFileMapper originFileMapper;
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private UserFolderMapper userFolderMapper;
    /**
     * key是文件在客户端存储的路径，value是文件在服务器存储的路径
     */
    private Map<String, String> filePathMap = new HashMap<>();
    /**
     * 空文件夹路径，用于压缩zip文件时生成空文件夹
     */
    private List<String> emptyFolderList = new ArrayList<>();

    @Override
    @Transactional(readOnly = true)
    public String generateZipFileName(List<Integer> files, List<Integer> folders) {
        StringBuilder filename = new StringBuilder();
        if (!folders.isEmpty()) {
            UserFolder userFolder = userFolderMapper.selectByPrimaryKey(folders.get(0));
            filename.append(userFolder.getFolderName());
        } else {
            UserFile userFile = userFileMapper.selectByPrimaryKey(files.get(0));
            filename.append(getFullFilename(userFile));
        }

        int size = files.size() + folders.size();
        if (size > 1) {
            filename.append("等" + size + "个文件");
        }

        filename.append(".zip");

        return filename.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public void download(Integer userId, List<Integer> files, List<Integer> folders, OutputStream out) throws IOException {
        /* 生成zip压缩文件内的目录结构 */
        String parentPath = "";// 初始路径为空
        for (Integer folderId : folders) {
            UserFolder folder = userFolderMapper.selectByPrimaryKey(folderId);
            generateFolderPath(userId, parentPath, folder);
        }
        for (Integer fileId : files) {
            UserFile file = userFileMapper.selectByPrimaryKey(fileId);
            generateFilePath(parentPath, file);
        }

        ZipOutputStream zos = null;
        FileInputStream fis = null;
        try {
            zos = new ZipOutputStream(out);
            for (Entry<String, String> entry : filePathMap.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                File file = new File(entry.getValue());
                try {
                    fis = new FileInputStream(file);
                    int hasRead;
                    byte[] buffer = new byte[102400];
                    while ((hasRead = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, hasRead);
                    }
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            // 往zip里添加空文件夹
            for (String emptyFolder : emptyFolderList) {
                zos.putNextEntry(new ZipEntry(emptyFolder));
            }
        } finally {
            zos.close();
        }
    }

    /**
     * 生成当前文件夹路径，若文件夹为空，将其路径加入emptyFolderList
     *
     * @param parentPath 上一级路径
     * @param folder     当前文件夹实体对象
     */
    private void generateFolderPath(Integer userId, String parentPath, UserFolder folder) {
        if (parentPath.length() > 0) {
            parentPath = parentPath + File.separator + folder.getFolderName();
        } else {
            parentPath = folder.getFolderName();
        }

        boolean emptyFolder = true;

        List<UserFolder> subfolders = userFolderMapper.listByParentId(userId, folder.getFolderId());
        for (UserFolder subfolder : subfolders) {
            emptyFolder = false;
            generateFolderPath(userId, parentPath, subfolder);
        }

        List<UserFile> subfiles = userFileMapper.listByParentId(userId, folder.getFolderId());
        for (UserFile subfile : subfiles) {
            emptyFolder = false;
            generateFilePath(parentPath, subfile);
        }

        if (emptyFolder) {
            parentPath += File.separator;// 需要在路径后面加一个分隔符才会生成空文件夹
            emptyFolderList.add(parentPath);
        }
    }

    /**
     * 生成当前文件的本地路径并获取文件的服务器路径
     * 把两个路径分别作为key（本地）,value（服务器）存入filePathMap中
     *
     * @param parentPath 上一级路径
     * @param userFile  当前文件实体对象
     */
    private void generateFilePath(String parentPath, UserFile userFile) {
        String filename = getFullFilename(userFile);
        String filePath;
        if (parentPath.length() > 0) {
            filePath = parentPath + File.separator + filename;
        } else {
            filePath = filename;
        }
        OriginFile file = originFileMapper.selectByPrimaryKey(userFile.getOriginId());
        filePathMap.put(filePath, FILE_BASE + file.getFileMd5());
    }
}
