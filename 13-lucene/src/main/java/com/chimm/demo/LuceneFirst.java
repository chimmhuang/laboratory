package com.chimm.demo;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
}
