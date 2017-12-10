package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 处理 Properties 的常用方法
 */
public class PropertiesUtil {
    /**
     * 转换 Properties 为 Map 对象
     */
    public static Map<String, String> convertToMap(Properties prop) {
        if (prop == null) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>();
        for (Object eachKey : prop.keySet()) {
            if (eachKey == null) {
                continue;
            }

            String key = eachKey.toString();
            String value = (String) prop.get(key);
            if (value == null) {
                value = "";
            } else {
                value = value.trim();
            }

            result.put(key, value);
        }
        return result;
    }
}