package com.chimm.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author huangshuai
 * @date 2020/05/22
 */
public class NioTest4 {

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();

            // 返回实际读取的字节个数，如果没有就为 -1
            int read = inputChannel.read(buffer);

            System.out.println("read: " + read);

            if (read == -1) {
                break;
            }

            buffer.flip();

            outputChannel.write(buffer);
        }

        outputChannel.close();
        inputChannel.close();
    }
}
