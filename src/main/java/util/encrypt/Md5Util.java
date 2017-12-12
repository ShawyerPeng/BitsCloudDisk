package util.encrypt;

import java.security.MessageDigest;

public class Md5Util {
	public static String md5(String source) throws Exception{  
        String des = "";  
        MessageDigest md = MessageDigest.getInstance("MD5");  
        byte[] result = md.digest(source.getBytes());  
        StringBuilder buf = new StringBuilder();  
        for (int i=0;i<result.length;i++) {  
            byte b = result[i];  
            buf.append(String.format("%02X", b));  
        }  
        des = buf.toString().toLowerCase();  
        return des;  
    }  
    public static void main(String[] args) throws Exception {  
        System.out.println(Md5Util.md5("1ed4878c1-93d2-4d81-8ede-fa6fa7806612"));
        System.out.println(Md5Util.md5("pengxiaoye"));
    }  
}
