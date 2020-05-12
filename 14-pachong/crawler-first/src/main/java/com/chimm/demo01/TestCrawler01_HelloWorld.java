package com.chimm.demo01;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Hello World 程序
 *
 * @author huangshuai
 * @date 2020/05/11
 */
public class TestCrawler01_HelloWorld {

    public static void main(String[] args) throws Exception {
        // 使用默认值创建 CloseableHttpClient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://www.itcast.cn/");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(content);
        }
    }
}
