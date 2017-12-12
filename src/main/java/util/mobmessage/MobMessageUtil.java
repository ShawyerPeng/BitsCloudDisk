package util.mobmessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class Person {
    private String action = "query";
    private String userid = null;//id
    private String account = null;// 账号
    private String password = null;
    private String mobile = null;// 用, 隔开 用户电话
    private String content = null;// 内容
    private String sendTime = "";// 发送时间 2010-10-24 09:08:10
    private String extno = "";// 扩展子号

    public Person(String userid, String account, String password) {
        this.userid = userid;
        this.account = account;
        this.password = password;
    }

    public String getAction() {
        return action;
    }

    public void set(String mobile, String content) {
        this.action = "send";
        this.mobile = mobile;
        this.content = content;
    }

    public void set(String mobile, String content, String sendTime, String extno) {
        this.action = "send";
        this.mobile = mobile;
        this.content = content;
        this.sendTime = sendTime;
        this.extno = extno;
    }

    @Override
    public String toString() {
        return "Person{" +
                "action='" + action + '\'' +
                ", userid='" + userid + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", extno='" + extno + '\'' +
                '}';
    }
}

public class MobMessageUtil {
    public static void main(String[] args) {
        MobMessageUtil send = new MobMessageUtil();
        System.out.println(send.getCon());
        System.out.println(send.getCon("1399261899*", "【Butanoler】" + "message", "", ""));
    }

    private static final String URLPATH = "~~~~";
    private Person person = new Person("****", "52319330*", "*****");// 余额
    private MobMessageUtil send = null;

    // 查询
    public String getCon() {
        return function();
    }

    // 立即发送
    public String getCon(String mobiler, String contentr) {
        person.set(mobiler, contentr);
        return function();
    }

    // 定时发送
    public String getCon(String mobiler, String contentr, String timer, String extnor) {
        person.set(mobiler, contentr, timer, extnor);
        return function();
    }

    //?userAccount=20134428&userPassword=dingcun123&userLogin.x=32&userlogin.y=11
    private String function() {
        try {
            URL url = null; // 发送 POST 请求
            System.out.println(person.getAction());
            if (person.getAction().equals("send")) {
                url = new URL(URLPATH + "sms.aspx");
            } else if (person.getAction().equals("query")) {
                url = new URL(URLPATH + "callApi.aspx");
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(person.toString());
            out.flush();
            out.close();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {  // 获取响应状态
                return "failed!";
            }
            String line, result = ""; // 获取响应内容体
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (Exception e) {
            return "error";
        }
    }
}