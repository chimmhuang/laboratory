package com.chimm.demo;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Chimm Huang
 * @date 2020/7/29
 */
public class LuceneFirst {


    /**
     * 创建索引
     */
    @Test
    public void createIndex() throws IOException {
        // 1. 创建一个 Director 对象，指定索引库保存的位置
//        // 把索引保存到内存中
//        Directory directory = new RAMDirectory();
        Directory directory = FSDirectory.open(new File("./directory").toPath());

        // 2. 基于 Director 对象创建一个 IndexWriter 对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());

        // 3. 读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("./src/main/resources/searchsource");
        System.out.println(dir.getAbsolutePath());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            // 取文件名
            String fileName = file.getName();
            String filePath = file.getPath();
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            long fileSize = file.length();

            /*
                创建 field 域
                    参数1：域名称
                    参数2：域的内容
                    参数3：是否存储
             */
            Field fieldName = new TextField("name",fileName, Store.YES);
            Field fieldPath = new TextField("path", filePath, Store.YES);
            Field fieldContent = new TextField("content", fileContent, Store.YES);
            Field fieldSize = new TextField("size", fileSize + "", Store.YES);

            // 4. 向文档对象中添加域
            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);

            // 5. 把文档对象写入索引库
            indexWriter.addDocument(document);
        }

        // 6. 关闭 IndexWriter 对象
        indexWriter.close();
    }

    /**
     * 查询索引
     */
    @Test
    public void searchIndex() throws IOException {
        // 1. 创建一个 Director 对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("./directory").toPath());

        // 2. 创建一个 IndexReader 对象
        IndexReader indexReader = DirectoryReader.open(directory);

        // 3. 创建一个 IndexSearch 对象，构造方法中传入 indexReader 对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 4. 创建一个 Query对象， TermQuery
        Query query = new TermQuery(new Term("content","spring"));


        /*
            5. 执行查询，得到一个 TopDocs 对象
                参数1： 查询对象
                参数2： 返回的最大记录数
         */
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 6. 取查询结果的总记录数
        long totalHits = topDocs.totalHits;
        System.out.println("查询总记录数：" + totalHits);

        // 7. 取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取文档id
            int docId = scoreDoc.doc;

            // 根据id取文档对象
            Document document = indexSearcher.doc(docId);

            // 8. 打印文档中的内容
            System.out.println("name:" + document.get("name"));
            System.out.println("path:" + document.get("path"));
            System.out.println("size:" + document.get("size"));
//            System.out.println("content:" + document.get("content"));
            System.out.println("------------------------------------------------");
        }

        // 9. 关闭 IndexReader 对象
        indexReader.close();
    }

    /**
     * 标准分析器的使用
     */
    @Test
    public void testTokenStream() throws IOException {
        //1. 创建一个 `Analyzer` 对象（`StandardAnalyzer`对象）
        Analyzer analyzer = new StandardAnalyzer();

        //2. 使用分析器对象的 `tokenStream` 方法获取一个 `TokenStream` 对象
        TokenStream tokenStream = analyzer.tokenStream("", "Learn how to create a web page with Spring MVC.");

        //3. 获取 `TokenStream`对象中的一个引用，相当于一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        //4. 调用 `TokenStream` 对象的 `reset` 方法（将指针游标位置初始化）。如果不调用会抛异常
        tokenStream.reset();

        //5. 使用 `while` 循环，来遍历 `TokenStream` 对象
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }

        //6. 关闭 `TokenStream` 对象
        tokenStream.close();
    }
}
