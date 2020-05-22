package com.chimm.nio;

import java.nio.ByteBuffer;

/**
 * @author huangshuai
 * @date 2020/05/22
 */
public class NioTest7 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        ByteBuffer readonlyBuffer = buffer.asReadOnlyBuffer();

        readonlyBuffer.flip();

        while (readonlyBuffer.hasRemaining()) {
            System.out.println(readonlyBuffer.get());
        }

    }
}
