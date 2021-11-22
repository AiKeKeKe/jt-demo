package com.aike.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * url://www.jt.com?id=1&name=tomcat
 * 目的:发起请求获取服务器数据
 * 参数说明:
 * 1.url地址
 * 2.用户提交的参数使用Map封装
 * 3.指定编码格式
 * <p>
 * 步骤:
 * 1.校验字符集. 如果字符集为null 设定默认值
 * 2.校验params是否为null
 * null:表示用户get请求无需传参.
 * !null:需要传参,  get请求规则 url?key=value&key2=value2...
 * 3.发起http的get请求获取返回值结果
 */
@Service
public class HttpClientService {
    @Resource
    private CloseableHttpClient httpClient;

    @Resource
    private RequestConfig requestConfig;

    public String doGet(String url, Map<String, String> params, String charSet) {
        // 1.校验字符集
        if (StringUtils.isEmpty(charSet)) {
            charSet = "UTF-8";
        }
        /**
         * 2.校验参数是否为null url如何拼接????
         * url:www.jt.com?id=1&name=tomcat&....
         * Map<entry<k,v>>
         */
        if (params != null) {
            url += "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url = url + entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        //3.发起get请求
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        String result = null;
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), charSet);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    //重载方法.对方法进行扩充方便使用者调用
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

}
