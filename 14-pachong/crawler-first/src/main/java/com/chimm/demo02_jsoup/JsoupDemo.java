package com.chimm.demo02_jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * @author huangshuai
 * @date 2020/05/11
 */
public class JsoupDemo {

    /**
     * 解析url
     */
    @Test
    public void testJsoupUrl() throws Exception {
        //    解析url地址
        Document document = Jsoup.parse(new URL("http://www.itcast.cn/"), 1000);

        //获取title的内容
        Element title = document.getElementsByTag("title").first();
        System.out.println(title.text());
    }

    /**
     * 解析字符串
     */
    @Test
    public void testJsoupString() throws Exception {
        //读取文件获取
        URL resource = this.getClass().getClassLoader().getResource("jsoup.html");
        String html = FileUtils.readFileToString(new File(resource.getFile()),"UTF-8");

        //    解析字符串
        Document document = Jsoup.parse(html);

        //获取title的内容
        Element title = document.getElementsByTag("title").first();
        System.out.println(title.text());
    }

    /**
     * 解析文件
     */
    @Test
    public void testJsoupHtml() throws Exception {
        //    解析文件
        URL resource = this.getClass().getClassLoader().getResource("jsoup.html");
        Document document = Jsoup.parse(new File(resource.getFile()),"UTF-8");

        //获取title的内容
        Element title = document.getElementsByTag("title").first();
        System.out.println(title.text());
    }
}
