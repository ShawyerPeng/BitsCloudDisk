package util.net;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 操作 FPT 工具类
 */
public class FtpUtil {
    private final static Logger LOG = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * 上传文件
     */
    public static boolean uploadFile(String ip, int port, String username, String password, String remotePath,
                                     String remoteFileName, InputStream input) {
        Long start = System.currentTimeMillis();
        boolean result = false;
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();

            // 连接 FTP 服务器, 如果采用默认端口，可以使用 ftp.connect(ip) 的方式直接连接
            ftp.connect(ip, port);

            // 登录
            ftp.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                return result;
            }

            // 更改目录
            ftp.changeWorkingDirectory(remotePath);

            // 设置文件类型（二进制）
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024000);
            ftp.storeFile(remoteFileName, input);

            result = true;
            Long end = System.currentTimeMillis();
            LOG.info("上传游戏图片，上传时间:" + (end - start));
        } catch (IOException e) {
            LOG.error("uploadFile failed : " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            if (ftp != null) {
                try {
                    ftp.logout();
                } catch (IOException e) {

                }
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {

                    }
                }
            }
        }
        return result;
    }
}