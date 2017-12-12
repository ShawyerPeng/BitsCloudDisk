package util.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeStamp 工具类，提供 TimeStamp 与 String、Date 的转换
 */
public class TimestampUtils {
    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * String 转换为 TimeStamp
     */
    public static Timestamp string2Timestamp(String dateStr) {
        if (dateStr == null && !"".equals(dateStr.trim())) {
            return null;
        }
        Timestamp ts = Timestamp.valueOf(dateStr);
        return ts;
    }

    /**
     * 将 Timestamp 转换为 String 类型，format 为 null 则使用默认格式 yyyy-MM-dd HH:mm:ss
     */
    public static String timestamp2String(Timestamp value, String format) {
        if (null == value) {
            return "";
        }
        SimpleDateFormat sdf = DateFormatUtils.getFormat(format);

        return sdf.format(value);
    }

    /**
     * Date 转换为 String
     */
    public static String date2String(Date date) {
        return sdf.format(date);
    }

    /**
     * String 转换为 Date
     */
    public static Date string2Date(String date) throws ParseException {
        return sdf.parse(date);
    }

    /**
     * Date 转换为 Timestamp
     */
    public static Timestamp date2Timestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     * Timestamp 转换为 Date
     */
    public static Date timestamp2Date(Timestamp time) {
        return time == null ? null : time;
    }

    public static void main(String[] args) {
        Timestamp ts1 = new Timestamp(System.currentTimeMillis());
        System.out.println(ts1);
        Date date1 = TimestampUtils.timestamp2Date(ts1);
        System.out.println(date1);
        Timestamp ts2 = TimestampUtils.date2Timestamp(date1);
        System.out.println(ts2);
    }
}
