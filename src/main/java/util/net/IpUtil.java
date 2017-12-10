package util.net;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP 操作工具类
 */
public class IpUtil {
    /**
     * long 转为 ip
     */
    public static String getIP(Integer ipaddr) {
        long y = ipaddr % 256;
        long m = (ipaddr - y) / (256 * 256 * 256);
        long n = (ipaddr - 256 * 256 * 256 * m - y) / (256 * 256);
        long x = (ipaddr - 256 * 256 * 256 * m - 256 * 256 * n - y) / 256;
        return m + "." + n + "." + x + "." + y;
    }

    /**
     * IP 转 long
     */
    public static Integer setIP(String ipaddr) {
        if (!isIpv4(ipaddr)) {
            return 0L;
        }
        String ip[] = ipaddr.split("\\.");
        Integer ipLong = 256 * 256 * 256 * Integer.parseLong(ip[0]) + 256 * 256
                * Integer.parseLong(ip[1]) + 256 * Integer.parseLong(ip[2])
                + Integer.parseLong(ip[3]);
        return ipLong;
    }

    /**
     * 判断是否为合法 IP
     */
    public static boolean isIpv4(String ipAddress) {
        if (ipAddress == null) {
            return false;
        }
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 获取客户端 ip
     */
    public static String getClientAddress(HttpServletRequest request) {
        /*
         * request.getHeader("User-Agent"); // 就是取得客户端的系统版本
		 * request.getRemoteAddr(); // 取得客户端的 IP request.getRemoteHost();
		 * // 取得客户端的主机名 request.getRemotePort(); // 取得客户端的端口
		 * request.getRemoteUser(); // 取得客户端的用户 request.getLocalAddr(); // 取得本地 IP
		 * request.getLocalPort(); // 取得本地端口
		  */
        String ip = request.getHeader("x-forwarded-for");
        String localIP = "127.0.0.1";
        if ((ip == null) || (ip.length() == 0)
                || (ip.equalsIgnoreCase(localIP))
                || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0)
                || (ip.equalsIgnoreCase(localIP))
                || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0)
                || (ip.equalsIgnoreCase(localIP))
                || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if ((ip == null) || (ip.length() == 0)
                || (ip.equalsIgnoreCase(localIP))
                || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 查询 IP 是否在指定 ip 范围之内
     */
    public static boolean isInIpArray(String sourceIp, String[] ipArray) {
        Integer ip = setIP(sourceIp);
        if (Integer.parseLong(ipArray[0]) <= ip && Integer.parseLong(ipArray[1]) >= ip) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(IpUtil.setIP("127.0.0.1"));
        System.out.println(IpUtil.getIP(3232266495L));
    }
}