package util.file;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import util.net.UserAgentUtil;
import util.date.DateUtils;
import util.math.RandomUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件上传下载
 */
public class FileUtil {
    private static final String REALNAME = "realName";
    private static final String STORENAME = "storeName";
    private static final String SIZE = "size";
    private static final String SUFFIX = "suffix";
    private static final String CONTENTTYPE = "contentType";
    private static final String CREATETIME = "createTime";
    private static final String UPLOADDIR = "uploadDir/";
    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 判断指定路径是否存在，如果不存在，根据参数决定是否新建
     */
    public static boolean isExist(String filePath, boolean isNew) {
        File file = new File(filePath);
        if (!file.exists() && isNew) {
            // 新建文件路径
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 获取文件名，构建结构为 prefix + yyyy-MM-dd HH:mm:ss + 10 位随机数 + suffix + .type
     */
    public static String getFileName(String type, String prefix, String suffix) {
        String date = DateUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");   // 当前时间
        String random = RandomUtils.generateNumberString(10);   //10 位随机数

        // 返回文件名
        return prefix + date + random + suffix + "." + type;
    }

    /**
     * 获取文件名，文件名构成: 当前时间 + 10 位随机数 + .type
     */
    public static String getFileName(String type) {
        return getFileName(type, "", "");
    }

    /**
     * 获取文件名，文件构成：当前时间 + 10 位随机数
     */
    public static String getFileName() {
        String date = DateUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");   // 当前时间
        String random = RandomUtils.generateNumberString(10);   //10 位随机数

        // 返回文件名
        return date + random;
    }

    /**
     * 获取指定文件的大小
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 将上传的文件进行重命名
     */
    private static String rename(String name) {
        Integer now = Integer.parseLong(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Integer random = (long) (Math.random() * now);
        String fileName = now + "" + random;
        if (name.indexOf(".") != -1) {
            fileName += name.substring(name.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * 文件重命名
     */
    public boolean renameDir(String oldPath, String newPath) {
        File oldFile = new File(oldPath);// 文件或目录
        File newFile = new File(newPath);// 文件或目录
        return oldFile.renameTo(newFile);// 重命名
    }

    /**
     * 压缩后的文件名
     */
    private static String zipName(String name) {
        String prefix = "";
        if (name.indexOf(".") != -1) {
            prefix = name.substring(0, name.lastIndexOf("."));
        } else {
            prefix = name;
        }
        return prefix + ".zip";
    }

    /**
     * 删除所有文件，包括文件夹
     */
    public void deleteAll(String dirpath) {
        File path = new File(dirpath);
        try {
            if (!path.exists())
                // 目录不存在退出
                return;
            if (path.isFile()) {
                // 如果是文件删除
                path.delete();
                return;
            }
            // 如果目录中有文件递归删除文件
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i].getAbsolutePath());
            }
            path.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件或者文件夹
     */
    public static void copy(File inputFile, File outputFile, boolean isOverWrite)
            throws IOException {
        if (!inputFile.exists()) {
            throw new RuntimeException(inputFile.getPath() + " 源目录不存在!");
        }
        copyPri(inputFile, outputFile, isOverWrite);
    }

    /**
     * 复制文件或者文件夹
     */
    private static void copyPri(File inputFile, File outputFile, boolean isOverwrite) throws IOException {
        if (inputFile.isFile()) {        // 文件
            copySimpleFile(inputFile, outputFile, isOverwrite);
        } else {
            if (!outputFile.exists()) {        // 文件夹
                outputFile.mkdirs();
            }
            // 循环子文件夹
            for (File child : inputFile.listFiles()) {
                copy(child, new File(outputFile.getPath() + "/" + child.getName()), isOverwrite);
            }
        }
    }

    /**
     * 复制单个文件
     */
    private static void copySimpleFile(File inputFile, File outputFile,
                                       boolean isOverWrite) throws IOException {
        if (outputFile.exists()) {
            if (isOverWrite) {        // 可以覆盖
                if (!outputFile.delete()) {
                    throw new RuntimeException(outputFile.getPath() + " 无法覆盖！");
                }
            } else {
                // 不允许覆盖
                return;
            }
        }
        InputStream in = new FileInputStream(inputFile);
        OutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }

    /**
     * 获取文件的 MD5
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件的后缀名
     */
    public static String getFileSuffix(String file) {
        if (file == null) {
            return null;
        }
        int extIndex = file.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        int folderIndex = file.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return file.substring(extIndex + 1);
    }

    /**
     * 文件上传
     */
    public static List<Map<String, Object>> upload(HttpServletRequest request, String[] params, Map<String, Object[]> values) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        String uploadDir = request.getSession().getServletContext().getRealPath("/") + FileUtil.UPLOADDIR;
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = null;
        int i = 0;
        for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); i++) {
            Map.Entry<String, MultipartFile> entry = it.next();
            MultipartFile mFile = entry.getValue();
            fileName = mFile.getOriginalFilename();
            String storeName = rename(fileName);
            String noZipName = uploadDir + storeName;
            String zipName = zipName(noZipName);
            // 上传成为压缩文件
            ZipOutputStream outputStream = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(zipName)));
            outputStream.putNextEntry(new ZipEntry(fileName));
            FileCopyUtils.copy(mFile.getInputStream(), outputStream);
            Map<String, Object> map = new HashMap<>();
            // 固定参数值对
            map.put(FileUtil.REALNAME, zipName(fileName));
            map.put(FileUtil.STORENAME, zipName(storeName));
            map.put(FileUtil.SIZE, new File(zipName).length());
            map.put(FileUtil.SUFFIX, "zip");
            map.put(FileUtil.CONTENTTYPE, "application/octet-stream");
            map.put(FileUtil.CREATETIME, new Date());
            // 自定义参数值对
            for (String param : params) {
                map.put(param, values.get(param)[i]);
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 文件下载
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String contentType,
                                String filePath, String fileName) throws IOException {
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long fileLength = new File(filePath).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment;" + UserAgentUtil.encodeFileName(request, fileName));
        response.setHeader("Content-Length", String.valueOf(fileLength));
        bis = new BufferedInputStream(new FileInputStream(filePath));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }
}