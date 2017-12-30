package common;

import java.io.Serializable;
import java.util.Map;

public class ResponseResult2<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 返回结果码
     * 0 成功，非 0 失败
     */
    private Integer code;
    /**
     * 操作结果信息
     */
    private String message;
    /**
     * 返回的数据
     */
    private Map<String, Object> data;

    public ResponseResult2() {
    }

    public ResponseResult2(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult2(Map<String, Object> data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return code;
    }

    public void setStatusCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseResult2{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}