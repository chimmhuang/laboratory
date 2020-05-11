package com.chimm.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author huangshuai
 * @date 2020/05/11
 */
public class NioTest1 {

    public static void main(String[] args) {
        // 分配大小为10的缓存区
        IntBuffer buffer = IntBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            // SecureRandom 生成的随机数，更具有随机性
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }


        /*
            翻转
            上方是往 buffer 里面写入数据
            翻转过后，该 buffer 即可读出数据
         */
        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
