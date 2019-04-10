package cn.html2pdf;

//import com.google.common.collect.Maps;
import cn.html2pdf.domain.Product;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
//import org.apache.commons.compress.utils.Lists;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo {

    public static void main(String[] args) throws IOException, TemplateException, com.lowagie.text.DocumentException, DocumentException {

        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\" />\n" +
                "    <title>Document</title>\n" +
                "    <style>\n" +
                "      @page {\n" +
                "        margin: 0;\n" +
                "      }\n" +
                "      body {\n" +
                "        margin: 0;\n" +
                "        font-family: SimSun;\n" +
                "        font-size:14px;\n" +
                "        padding: 0 20px;\n" +
                "      }\n" +
                "      .sheet {\n" +
                "        margin: 0;\n" +
                "        overflow: hidden;\n" +
                "        position: relative;\n" +
                "        box-sizing: border-box;\n" +
                "        page-break-after: always;\n" +
                "      }\n" +
                "\n" +
                "      /** Paper sizes **/\n" +
                "      body.A3 .sheet {\n" +
                "        /* width: 297mm; */\n" +
                "        height: 419mm;\n" +
                "      }\n" +
                "      body.A3.landscape .sheet {\n" +
                "        /* width: 420mm; */\n" +
                "        height: 296mm;\n" +
                "      }\n" +
                "      body.A4 .sheet {\n" +
                "        /* width: 210mm; */\n" +
                "        height: 296mm;\n" +
                "      }\n" +
                "      body.A4.landscape .sheet {\n" +
                "        /* width: 297mm; */\n" +
                "        height: 209mm;\n" +
                "      }\n" +
                "      body.A5 .sheet {\n" +
                "        /* width: 148mm; */\n" +
                "        height: 209mm;\n" +
                "      }\n" +
                "      body.A5.landscape .sheet {\n" +
                "        /* width: 210mm; */\n" +
                "        height: 147mm;\n" +
                "      }\n" +
                "\n" +
                "      /** Padding area **/\n" +
                "      .sheet.padding-10mm {\n" +
                "        padding: 10mm;\n" +
                "      }\n" +
                "      .sheet.padding-15mm {\n" +
                "        padding: 15mm;\n" +
                "      }\n" +
                "      .sheet.padding-20mm {\n" +
                "        padding: 20mm;\n" +
                "      }\n" +
                "      .sheet.padding-25mm {\n" +
                "        padding: 25mm;\n" +
                "      }\n" +
                "\n" +
                "      /** For screen preview **/\n" +
                "      @media screen {\n" +
                "        body {\n" +
                "          background: #e0e0e0;\n" +
                "        }\n" +
                "        .sheet {\n" +
                "          background: white;\n" +
                "          box-shadow: 0 0.5mm 2mm rgba(0, 0, 0, 0.3);\n" +
                "          margin: 5mm;\n" +
                "        }\n" +
                "      }\n" +
                "\n" +
                "      /** Fix for Chrome issue #273306 **/\n" +
                "      @media print {\n" +
                "        body.A3.landscape {\n" +
                "          /* width: 420mm; */\n" +
                "        }\n" +
                "        body.A3,\n" +
                "        body.A4.landscape {\n" +
                "          /* width: 297mm; */\n" +
                "        }\n" +
                "        body.A4,\n" +
                "        body.A5.landscape {\n" +
                "          /* width: 210mm; */\n" +
                "        }\n" +
                "        body.A5 {\n" +
                "          /* width: 148mm; */\n" +
                "        }\n" +
                "      }\n" +
                "\n" +
                "      @page {\n" +
                "        size: A4;\n" +
                "      }\n" +
                "      h2,\n" +
                "      table,\n" +
                "      th,\n" +
                "      td {\n" +
                "        padding: 0;\n" +
                "        margin: 0;\n" +
                "      }\n" +
                "\n" +
                "      table,\n" +
                "      th,\n" +
                "      td {\n" +
                "        border: 1px solid #eee;\n" +
                "        font-size: 14px;\n" +
                "      }\n" +
                "      table {\n" +
                "        text-align: center;\n" +
                "        border-collapse: collapse;\n" +
                "        margin: 10px 20px 0;\n" +
                "        width: 100%;\n" +
                "      }\n" +
                "      th {\n" +
                "        text-align: center;\n" +
                "        line-height: 40px;\n" +
                "        height: 40px;\n" +
                "      }\n" +
                "      td {\n" +
                "        text-align: center;\n" +
                "        height: 26px;\n" +
                "        min-width: 100px;\n" +
                "        word-wrap:break-word;\n" +
                "      }\n" +
                "      .title {\n" +
                "        text-align: center;\n" +
                "        line-height: 40px;\n" +
                "        font-size: 20px;\n" +
                "      }\n" +
                "      .container {\n" +
                "        margin: 0 auto;\n" +
                "        padding-top: 30px;\n" +
                "      }\n" +
                "      .contrctInfo {\n" +
                "        margin-left: auto;\n" +
                "        min-width: 240px;\n" +
                "        width: 32%;\n" +
                "        text-align: left;\n" +
                "        font-size: 14px;\n" +
                "      }\n" +
                "      .contrctInfo span {\n" +
                "        text-decoration: underline;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body class=\"A4\">\n" +
                "    <div class=\"container sheet\">\n" +
                "      <h2 class=\"title\">商业保理合同</h2>\n" +
                "      <div class=\"contrctInfo\">\n" +
                "        <p>合同编号：<span>${contractNo!''}</span></p>\n" +
                "        <p>合同编号：签订地点</p>\n" +
                "        <p>合同编号：<span>2019-02-12</span></p>\n" +
                "      </div>\n" +
                "      <table>\n" +
                "        <thead>\n" +
                "          <tr>\n" +
                "            <th>产品名称</th>\n" +
                "            <th>材质</th>\n" +
                "            <th>规格型号</th>\n" +
                "            <th>生产厂家</th>\n" +
                "            <th>数量（吨）</th>\n" +
                "            <th>单价（元/吨）</th>\n" +
                "            <th>总价（元）</th>\n" +
                "          </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "          <#if products?? && (products?size > 0) >\n" +
                "            <#list products as product>\n" +
                "              <tr>\n" +
                "                  <td>${product_index}</td>\n" +
                "                  <td>${product.name}</td>\n" +
                "                  <td>${product.name}</td>\n" +
                "                  <td>${product.name}</td>\n" +
                "                  <td>${product.longName}</td>\n" +
                "                  <td>${product.longName}</td>\n" +
                "                  <td>${product.longName}</td>\n" +
                "              </tr>\n" +
                "            </#list>\n" +
                "          <#else>\n" +
                "            <tr>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "                <td>/</td>\n" +
                "            </tr>\n" +
                "          </#if>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

        /**
         * 将模版放入装载程序
         * @param name 命名模版名称
         * @param templateContent 模版的源代码
         */
        stringTemplateLoader.putTemplate("DEMO_CONTRACT", html);

        //设置模版加载器
        configuration.setTemplateLoader(stringTemplateLoader);

        //设置模版异常处理器
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        //加载模版
        Template template = configuration.getTemplate("DEMO_CONTRACT");

        //设置数据
        Map<String, Object> map = new HashMap<>();
        map.put("contractNo", "JREIRE-534432");


        Product product = new Product();
        product.setName("张三");
        product.setLongName("李四张三");

        Product product2 = new Product();
        product2.setName("王Î五");
        product2.setLongName("王五孙六");
//        Map<String, Object> product = new HashMap<>();
//        map.put("name", "张三");
//        map.put("longName", "李四");
//
//        Map<String, Object> product2 = new HashMap<>();
//        map.put("name", "王五");
//        map.put("longName", "赵六");

        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);

        map.put("products", products);

        //输出流
        Writer out = new StringWriter(2048);
        template.process(map, out);
        System.out.println(out.toString());
        out.flush();

        OutputStream outputStream = new FileOutputStream("/Users/huangshuai/Desktop/test.pdf");
        savePdf(outputStream, out.toString().replaceAll("[\\n\\r]", ""));
    }

    public static void savePdf(OutputStream out, String html) throws IOException, com.lowagie.text.DocumentException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        ITextFontResolver fontResolver = renderer.getFontResolver();
//        fontResolver.addFont("/home/yunjie/lyj/SimSun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        fontResolver.addFont("/Users/huangshuai/IdeaProjects/实验室/03-freemarker/src/main/resources/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(out);
        out.flush();

    }

}
