package com.chimm;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author huangshuai
 * @date 2019-03-28 14:09
 *
 * 中文生成的md5是否一致
 */
public class demo01 {

    public static void main(String[] args) {
        String msg1 = "你好";
        String msg2 = "你好";
        String msg3 = "世界";
        String msg4 = "Hello world";

        System.out.println("mgs1："+ DigestUtils.md5Hex(msg1));
        System.out.println("mgs2："+ DigestUtils.md5Hex(msg2));
        System.out.println("mgs3："+ DigestUtils.md5Hex(msg3));
        System.out.println("mgs4："+ DigestUtils.md5Hex(msg4));
    }
}
