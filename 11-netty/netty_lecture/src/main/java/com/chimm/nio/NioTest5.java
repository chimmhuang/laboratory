package com.chimm.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 类型化的 Put 与 get 方法
 *
 * @author huangshuai
 * @date 2020/05/22
 */
public class NioTest5 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(15);
        buffer.putLong(5000000L);
        buffer.putDouble(14.123);
        buffer.putChar('你');
        buffer.putShort((short) 2);
        buffer.putChar('我');

        buffer.flip();

        // 顺序必须一致
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
    }
}
