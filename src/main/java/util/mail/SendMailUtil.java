package util.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;


/**
 * 邮件工具类
 */
public class SendMailUtil {
    /**
     * 发送邮件
     */
    public static void sendMail(String to, String text) throws MessagingException {
        // 创建连接对象 连接到邮件服务器
        Properties properties = new Properties();
        // 设置发送邮件的基本参数
        // 发送邮件服务器
        properties.put("mail.smtp.host", "smtp.163.com");
        // 发送端口
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");

        // qq 必须加密 ssl
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 设置发送邮件的账号和密码
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 两个参数分别是发送邮件的账户和密码
                return new PasswordAuthentication("lgdafeng@163.com", "zrh131415");
            }
        });

        // 创建邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress("1002097607@qq.com"));
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // 设置主题
        message.setSubject("您的注册验证码为：");
        // 设置邮件正文  第二个参数是邮件发送的类型
        message.setContent(text, "text/html;charset=UTF-8");
        // 发送一封邮件
        Transport.send(message);
    }
}