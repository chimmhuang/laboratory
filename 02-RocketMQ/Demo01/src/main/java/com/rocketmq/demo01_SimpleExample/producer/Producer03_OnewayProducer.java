package com.rocketmq.demo01_SimpleExample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author huangshuai
 * @date 2019-04-06 22:32
 *
 * 单向模式发送消息
 * 原理：单向（Oneway）发送特点为只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
 * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
 */
public class Producer03_OnewayProducer {

    public static void main(String[] args) throws Exception{
        //实例化一个生产者（可以自定义名称，默认：DEFAULT_PRODUCER）
        DefaultMQProducer producer = new DefaultMQProducer("demo01");

        //指定消息地址
        producer.setNamesrvAddr("172.16.120.132:9876");

        producer.setVipChannelEnabled(false);
        producer.setSendMsgTimeout(1000*60);

        //加载实例
        producer.start();

        for (int i = 0; i < 5; i++) {
            /**
             * 创建消息实例，指定topic，tag，和正文内容
             * @param topic
             * @param tags
             * @param body 消息内容
             */
            Message msg = new Message("TopicTest",
                    "TagA",
                    ("你好RocketMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(msg);
        }

        //关闭生产者
        producer.shutdown();
    }
}
