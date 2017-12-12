package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * https://www.cnblogs.com/shangxiaofei/p/5473937.html
 */
@Controller
@RequestMapping("/invite")
public class InviteController {
    @RequestMapping("/generateInivitationCode")
    public String generateInivitationCode() {
        return "";
    }

    @RequestMapping("/generateInivitationQRCode")
    public String generateInivitationQRCode() {
        return "";
    }

    /**
     * 生成二维码，返回到页面上
     *
     * @param response
     */
    @RequestMapping(value = "/getQRCode")
    public void getQRCode(HttpServletResponse response) {
        String url = "www.baidu.com";
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 200;
                int height = 200;
                stream = response.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}