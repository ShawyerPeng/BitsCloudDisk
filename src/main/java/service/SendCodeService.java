package service;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发送验证码，请在官网上服务器端 API 上查看，有一个计算 checkSum 的实例
 */
public class SendCodeService {
    // 发送验证码的请求路径 URL
    private static final String SERVER_URL = "https://api.netease.im/sms/sendcode.action";
    // 网易云信分配的账号，请替换你在管理后台应用下申请的 Appkey
    private static final String APP_KEY = "d234a693b0ed87c1e01cd44acae78284";
    // 网易云信分配的密钥，请替换你在管理后台应用下申请的 appSecret
    private static final String APP_SECRET = "520cc8c28684";
    // 随机数
    private static final String NONCE = "123456";
    // 短信模板 ID
    private static final String TEMPLATEID = "3135021";
    // 客户手机号
    private static final String MOBILE = "15850673601";
    // 验证码长度，范围 4～10，默认为 4
    private static final String CODELEN = "4";

    private static final CloseableHttpClient httpClient;

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public static void main(String[] args) throws Exception {
        HttpPost httpPost = new HttpPost(SERVER_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算 CheckSum 的 java 代码，在上述文档的参数列表中，有 CheckSum 的计算文档示例
          */
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

        // 设置请求的 header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody 参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1. 如果是模板短信，请注意参数 mobile 是有 s 的，详细参数配置请参考 “发送模板短信文档”
         * 2. 参数格式是 jsonArray 的格式，例如 "['13888888888','13666666666']"
         * 3.params 是根据你模板里面有几个参数，那里面的参数也是 jsonArray 格式
          */
        //nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", MOBILE));
        nvps.add(new BasicNameValuePair("codeLen", CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1. 打印执行结果，打印结果一般会 200、315、403、404、413、414、500
         * 2. 具体的 code 有问题的可以参考官网的 Code 状态表
          */
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(responseEntity);
        String yanzhengma = JSON.parseObject(responseEntity).getString("obj");
        System.out.println(yanzhengma);

    }
}
