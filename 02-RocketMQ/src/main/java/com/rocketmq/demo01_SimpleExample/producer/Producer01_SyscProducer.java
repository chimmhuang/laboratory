package com.rocketmq.demo01_SimpleExample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author huangshuai
 * @date 2019-04-06 18:52
 *
 * 同步发送消息
 * 同步消息消息传输一般的应用场景：重要的通知消息、短信通知、短信营销系统等
 */
public class Producer01_SyscProducer {

    public static void main(String[] args) throws Exception {
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
            Message msg = new Message("TopicTest","TagA",("你好RockeyMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            //向其中一个经纪人发送消息（轮询）
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n",sendResult);
        }

        //生产者不再时候后，关闭
        producer.shutdown();
    }
}
