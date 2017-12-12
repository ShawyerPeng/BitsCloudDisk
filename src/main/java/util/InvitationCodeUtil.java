package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://github.com/jooyu/invitation-code
 * 根据用户 id 生成与之对应的唯一邀请码 ，范围为‘0-9A-Z’
 * 验证码使用字母加数字的方式，字母以大写展示，并兼容小写，排除容易混淆的数字0和字母O、数字1和字母I后，使用剩下的32个字符（数字）
 */
public class InvitationCodeUtil {
    private static Logger logger = LoggerFactory.getLogger(InvitationCodeUtil.class);

    /**
     * 自定义进制 (0,1 没有加入, 容易与 o,l 混淆)
     * 'F'(0), 'L'(1), 'G'(2), 'W'(3), '5'(4), 'X'(5), 'C'(6), '3'(7), '9'(8), 'Z'(9), 'M'(10),
     * '6'(11), '7'(12), 'Y'(13), 'R'(14), 'T'(15), '2'(16), 'H'(17), 'S'(18), '8'(19), 'D'(20),
     * 'V'(21), 'E'(22), 'J'(23), '4'(24), 'K'(25), 'Q'(26), 'P'(27), 'U'(28), 'A'(29), 'N'(30), 'B'(31)
     */
    private static final char[] r = new char[]{'F', 'L', 'G', 'W', '5', 'X', 'C', '3', '9', 'Z', 'M', '6', '7', 'Y', 'R', 'T', '2', 'H', 'S', '8', 'D', 'V', 'E', 'J', '4', 'K', 'Q', 'P', 'U', 'A', 'N', 'B'};

    /**
     * 进制长度
     */
    private static final int binLen = r.length;

    //    private static final long startNumber = 1048576L;
    private static final long startNumber = 0L;

    /**
     * @param id ID
     * @return 随机码
     */
    public static String idToCode(long id, long costomStartNumber) {
        if (costomStartNumber < 0) {
            costomStartNumber = startNumber;
        }
        id += costomStartNumber;
        char[] buf = new char[32];
        int charPos = 32;

        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos] = r[ind];
            id /= binLen;
        }
        buf[--charPos] = r[(int) (id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str = new String(buf, charPos, (32 - charPos));
        return str.toUpperCase();
    }

    public static String idToCode(long idL) {
        return idToCode(idL, -1L);
    }

    public static String idToCode(String id) {
        long idL = Long.parseLong(id);
        return idToCode(idL, -1L);
    }

    public static String idToCode(String id, long costomStartNumber) {
        long idL = Long.parseLong(id);
        return idToCode(idL, costomStartNumber);
    }

    public static long codeToId(String code) {
        code = code.toUpperCase();
        char chs[] = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chs.length; i++) {
            int ind = 0;
            for (int j = 0; j < binLen; j++) {
                if (chs[i] == r[j]) {
                    ind = j;
                    break;
                }
            }
            if (i > 0) {
                res = res * binLen + ind;
            } else {
                res = ind;
            }
            //logger.debug(ind + "-->" + res);
        }
        res -= startNumber;
        return res;
    }

    public static void main(String[] args) {
        long id = 2134539L;
        String code = InvitationCodeUtil.idToCode(id);
        System.out.println("id(" + id + ") -> code:" + code);
        id = InvitationCodeUtil.codeToId(code);
        System.out.println("code(" + code + ") -> id:" + id);
    }
}