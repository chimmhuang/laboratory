package com.rocketmq.demo04_BroadcastingExample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangshuai
 * @date 2019-04-16 17:01
 *
 * 广播消息生产者（消息的批量发送）
 */
public class BoradcastingProducer {

    public static void main(String[] args) throws Exception {
        //实例化一个生产者（可以自定义名称，默认：DEFAULT_PRODUCER）
        DefaultMQProducer producer = new DefaultMQProducer("demo04");
        //指定消息地址
        producer.setNamesrvAddr("172.16.120.132:9876");

        producer.setVipChannelEnabled(false);
        producer.setSendMsgTimeout(1000*60);
        //加载实例
        producer.start();

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            /**
             * 创建消息实例，指定topic，tag，和正文内容
             * @param topic
             * @param tags
             * @param body 消息内容
             */
            Message msg = new Message(
                    "TopicBroadcastingTest", //主题
                    "TagA", //主要用于消息过滤
                    "Keys"+i, //消息的唯一值
                    ("你好RockeyMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            messages.add(msg);

        }

        //批量发送消息
        SendResult sendResult = producer.send(messages);
        System.out.println(sendResult);

        //生产者不再时候后，关闭
        producer.shutdown();
    }
}
