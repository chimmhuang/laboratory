package com.chimm.netty.example06_protobuf;

import com.chimm.netty.example06_protobuf.MyDataInfo.Cat;
import com.chimm.netty.example06_protobuf.MyDataInfo.Dog;
import com.chimm.netty.example06_protobuf.MyDataInfo.MyMessage;
import com.chimm.netty.example06_protobuf.MyDataInfo.MyMessage.DataType;
import com.chimm.netty.example06_protobuf.MyDataInfo.Person;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @author huangshuai
 * @date 2020/05/11
 */
public class TestClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Person> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Person msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接通道active...");


        int randomInt = new Random().nextInt(3);

        MyMessage myMessage = null;

        if (randomInt == 0) {

            Person person = Person.newBuilder()
                    .setName("张三")
                    .setAge(20)
                    .setAddress("成都")
                    .build();


            myMessage = MyMessage.newBuilder()
                    .setDataType(DataType.PersonType)
                    .setPerson(person)
                    .build();

        } else if (randomInt == 1) {
            Dog dog = Dog.newBuilder()
                    .setName("猪猪")
                    .setAge(1)
                    .build();
            myMessage = MyMessage.newBuilder()
                    .setDataType(DataType.DogType)
                    .setDog(dog)
                    .build();

        } else {
            Cat cat = Cat.newBuilder()
                    .setName("咪咪")
                    .setCity("成都")
                    .build();

            myMessage = MyMessage.newBuilder()
                    .setDataType(DataType.CatType)
                    .setCat(cat)
                    .build();
        }

        ctx.channel().writeAndFlush(myMessage);

    }
}
