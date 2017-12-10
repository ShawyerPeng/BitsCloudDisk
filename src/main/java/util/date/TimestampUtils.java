package util.date;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeStamp 工具类，提供 TimeStamp 与 String、Date 的转换
 */
public class TimestampUtils {
    /**
     * String 转换为 TimeStamp
     */
    public static Timestamp string2Timestamp(String value) {
        if (value == null && !"".equals(value.trim())) {
            return null;
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        ts = Timestamp.valueOf(value);
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

}
