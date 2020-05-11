package com.chimm.netty.example06_protobuf;

import com.chimm.netty.example06_protobuf.MyDataInfo.Cat;
import com.chimm.netty.example06_protobuf.MyDataInfo.Dog;
import com.chimm.netty.example06_protobuf.MyDataInfo.MyMessage;
import com.chimm.netty.example06_protobuf.MyDataInfo.MyMessage.DataType;
import com.chimm.netty.example06_protobuf.MyDataInfo.Person;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huangshuai
 * @date 2020/05/09
 */
public class TestServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        System.out.println("接收到消息...");

        DataType dataType = msg.getDataType();
        switch (dataType) {
            case PersonType:
                Person person = msg.getPerson();
                System.out.println(person.getName());
                System.out.println(person.getAge());
                System.out.println(person.getAddress());
                break;
            case DogType:
                Dog dog = msg.getDog();
                System.out.println(dog.getName());
                System.out.println(dog.getAge());
                break;
            case CatType:
                Cat cat = msg.getCat();
                System.out.println(cat.getName());
                System.out.println(cat.getCity());
                break;
            default:break;
        }
    }
}
