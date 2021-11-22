package com.aike.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource(value = "classpath:/properties/httpClient.properties")
public class HttpClientConfig {
    @Value("${http.maxTotal}")
    private Integer maxTotal;                        //最大连接数

    @Value("${http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;                //最大并发链接数

    @Value("${http.connectTimeout}")
    private Integer connectTimeout;                    //创建链接的最大时间

    @Value("${http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;        //链接获取超时时间

    @Value("${http.socketTimeout}")
    private Integer socketTimeout;                    //数据传输最长时间

    @Value("${http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;    //提交时检查链接是否可用

    //定义httpClient链接池
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        //设定最大链接数
        manager.setMaxTotal(maxTotal);
        //设定并发链接数
        manager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return manager;
    }

    //定义HttpClient

    /**
     * 实例化连接池，设置连接池管理器。
     * @param httpClientConnectionManager
     * @return
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        return httpClientBuilder;
    }

    /**
     * 注入连接池，用于获取httpClient
     * @param httpClientBuilder
     * @return
     */
    @Bean
    @Scope("prototype")
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * 设置builder的连接信息
     * @return
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled);
    }

    /**
     * 使用builder构建一个RequestConfig对象
     * @param builder
     * @return
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
        return builder.build();
    }
}
