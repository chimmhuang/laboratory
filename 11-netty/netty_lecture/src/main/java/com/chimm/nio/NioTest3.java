package com.chimm.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author huangshuai
 * @date 2020/05/11
 */
public class NioTest3 {

    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byte[] messages = "Hello World nihao".getBytes();

        for (int i = 0; i < messages.length; i++) {
            byteBuffer.put(messages[i]);
        }

        byteBuffer.flip();

        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
