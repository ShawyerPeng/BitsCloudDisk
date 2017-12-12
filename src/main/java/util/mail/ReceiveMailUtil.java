package util.mail;

import javax.mail.*;
import java.util.Properties;
import java.util.Scanner;

public class ReceiveMailUtil {
    // 连接 pop3 服务器的主机名、协议、用户名、密码
    private String pop3Server = "pop.163.com";
    private String protocol = "pop3";
    private String user = "Butanoler@163.com";
    private String pwd = "**********";
    private Properties props = null;
    private Session session = null;
    private Store store = null;
    private Folder folder = null;
    public Message[] messages = null;

    public ReceiveMailUtil() {
        // 创建一个有具体连接信息的 Properties 对象
        props = new Properties();
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.pop3.host", pop3Server);

        // 使用 Properties 对象获得 Session 对象
        session = Session.getInstance(props);
        session.setDebug(true);

        // 利用 Session 对象获得 Store 对象，并连接 pop3 服务器
        try {
            store = session.getStore();
            store.connect(pop3Server, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获得邮箱内的邮件夹 Folder 对象，以 "只读" 打开
        try {
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 获得邮件夹 Folder 内的所有邮件 Message 对象
        try {
            messages = folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void reading(Message message) throws Exception {
        ReciveOneMailUtil reciveOneMail = new ReciveOneMailUtil(message);
        System.out.println(" 邮件的时间：" + reciveOneMail.getSentDate());
        System.out.println(" 邮件的主题：" + reciveOneMail.getSubject());
        System.out.println(" 邮件的发件人地址：" + reciveOneMail.getFrom());
        System.out.println(" 邮件的内容 " + reciveOneMail.getBodyText());
        boolean iscont = reciveOneMail.isContainAttach(message);
        System.out.println(" 邮件是否含有附件 " + iscont);
        if (iscont) {
            reciveOneMail.saveAttachMent((Part) message);
        }
    }

    public void close() {
        try {
            folder.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ReceiveMailUtil rm = new ReceiveMailUtil();
        for (Message m : rm.messages) {
            rm.reading(m);
            new Scanner(System.in).nextLine();
        }
        rm.close();
    }
}