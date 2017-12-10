package exception;

import po.ResponseResult;

import java.util.HashMap;
import java.util.Map;

public class ResponseResultGenerator {
    public static <T> ResponseResult<T> build() {
        return build(null);
    }

    public static <T> ResponseResult<T> build(T t) {
        return new ResponseResult.Builder<T>().setData(t).build();
    }

    public static void main(String[] args) {
        System.out.println(ResponseResultGenerator.build("hello"));

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", 5);
        System.out.println(new ResponseResult.Builder().setData(hashMap).setStatusCode(1024).setMessage("hello").build());
    }
}
