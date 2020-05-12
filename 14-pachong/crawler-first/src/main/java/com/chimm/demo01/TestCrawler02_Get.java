package com.chimm.demo01;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * GET 请求
 *
 * @author huangshuai
 * @date 2020/05/11
 */
public class TestCrawler02_Get {

    public static void main(String[] args) throws IOException {
        // 创建 HttpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建 HttpGet 请求
        HttpGet httpGet = new HttpGet("http://www.itcast.cn/");

        CloseableHttpResponse response = null;

        try {
            // 使用 HttpClient 发起请求
            response = httpClient.execute(httpGet);

            // 判断响应状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 如果为 200 表示请求成功，获取返回数据
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 打印数据长度
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放链接
            if (response == null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpClient.close();
            }
        }
    }
}
