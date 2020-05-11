package com.chimm.netty.protobuf;

import com.chimm.netty.protobuf.DataInfo.Student;

/**
 * @author huangshuai
 * @date 2020/05/09
 */
public class ProtoBufTest {

    public static void main(String[] args) throws Exception {

        // 将A机器上要传递的信息转换为二进制
        Student student = Student.newBuilder()
                .setName("张三")
                .setAge(20)
                .setAddress("成都")
                .build();
        byte[] student2ByteArray = student.toByteArray();

        // 在B机器可以进行解析（优点：不会涉及编程语言，A机器可以是用java，B机器可以是用python）
        Student student2 = Student.parseFrom(student2ByteArray);

        System.out.println(student2.toString());
    }
}
