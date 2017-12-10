package common;

import java.util.Map;

public class RestResult {
    private Map<String, Object> data;
    private Integer statusCode;
    private String message;

    public RestResult() {
    }

    public RestResult(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public RestResult(Map<String, Object> data, Integer statusCode, String message) {
        this.data = data;
        this.statusCode = statusCode;
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
