package util.date;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化工具类
 */
public class DateFormatUtils {
    /**
     * yyyy: 年
     */
    public static final String DATE_YEAR = "yyyy";

    /**
     * MM：月
     */
    public static final String DATE_MONTH = "MM";

    /**
     * DD：日
     */
    public static final String DATE_DAY = "dd";

    /**
     * HH：时
     */
    public static final String DATE_HOUR = "HH";

    /**
     * mm：分
     */
    public static final String DATE_MINUTE = "mm";

    /**
     * ss：秒
     */
    public static final String DATE_SECONDES = "ss";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT1 = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static final String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd hh:mm:ss|SSS
     */
    public static final String TIME_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss|SSS";

    /**
     * yyyyMMdd
     */
    public static final String DATE_NOFUll_FORMAT = "yyyyMMdd";

    /**
     * yyyyMMddhhmmss
     */
    public static final String TIME_NOFUll_FORMAT = "yyyyMMddHHmmss";

    /**
     * 格式转换 <br>
     * yyyy-MM-dd hh:mm:ss 和 yyyyMMddhhmmss 相互转换 <br>
     * yyyy-mm-dd 和 yyyymmss 相互转换
     */
    public static String formatString(String value) {
        String sReturn = "";
        if (value == null || "".equals(value))
            return sReturn;
        if (value.length() == 14) {   // 长度为 14 格式转换成 yyyy-mm-dd hh:mm:ss
            sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " "
                    + value.substring(8, 10) + ":" + value.substring(10, 12) + ":" + value.substring(12, 14);
            return sReturn;
        }
        if (value.length() == 19) {   // 长度为 19 格式转换成 yyyymmddhhmmss
            sReturn = value.substring(0, 4) + value.substring(5, 7) + value.substring(8, 10) + value.substring(11, 13)
                    + value.substring(14, 16) + value.substring(17, 19);
            return sReturn;
        }
        if (value.length() == 10) {     // 长度为 10 格式转换成 yyyymmhh
            sReturn = value.substring(0, 4) + value.substring(5, 7) + value.substring(8, 10);
        }
        if (value.length() == 8) {      // 长度为 8 格式转化成 yyyy-mm-dd
            sReturn = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
        }
        return sReturn;
    }

    public static String formatDate(String date, String format) {
        if (date == null || "".equals(date)) {
            return "";
        }
        Date dt = null;
        SimpleDateFormat inFmt = null;
        SimpleDateFormat outFmt = null;
        ParsePosition pos = new ParsePosition(0);
        date = date.replace("-", "").replace(":", "");
        if ((date == null) || ("".equals(date.trim())))
            return "";
        try {
            if (Long.parseLong(date) == 0L)
                return "";
        } catch (Exception nume) {
            return date;
        }
        try {
            switch (date.trim().length()) {
                case 14:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                    break;
                case 12:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmm");
                    break;
                case 10:
                    inFmt = new SimpleDateFormat("yyyyMMddHH");
                    break;
                case 8:
                    inFmt = new SimpleDateFormat("yyyyMMdd");
                    break;
                case 6:
                    inFmt = new SimpleDateFormat("yyyyMM");
                    break;
                case 7:
                case 9:
                case 11:
                case 13:
                default:
                    return date;
            }
            if ((dt = inFmt.parse(date, pos)) == null)
                return date;
            if ((format == null) || ("".equals(format.trim()))) {
                outFmt = new SimpleDateFormat("yyyy 年 MM 月 dd 日 ");
            } else {
                outFmt = new SimpleDateFormat(format);
            }
            return outFmt.format(dt);
        } catch (Exception ex) {
        }
        return date;
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date date, String format) {
        return formatDate(DateUtils.date2String(date), format);
    }

    /**
     * 格式化是时间，采用默认格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDate(String value) {
        return getFormat(DATE_FORMAT2).format(DateUtils.string2Date(value, DATE_FORMAT2));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date value) {
        return formatDate(DateUtils.date2String(value));
    }

    /**
     * 获取日期显示格式，为空默认为 yyyy-mm-dd HH:mm:ss
     */
    protected static SimpleDateFormat getFormat(String format) {
        if (format == null || "".equals(format)) {
            format = DATE_FORMAT2;
        }
        return new SimpleDateFormat(format);
    }
}
