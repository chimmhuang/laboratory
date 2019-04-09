package com.rocketmq.demo02_OrderExample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * @author huangshuai
 * @date 2019-04-08 21:57
 *
 * 订单发送消息示例代码
 */
public class OrderedProducer {

    public static void main(String[] args) throws Exception{
        //实例化生产者
        DefaultMQProducer producer = new DefaultMQProducer("demo02");

        //指定消息地址
        producer.setNamesrvAddr("172.16.120.132:9876");
        producer.setSendMsgTimeout(1000*60);

        //加载生产者
        producer.start();

        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {

            int orderId = i % 10;

            /**
             * 创建消息实例，指定topic，tag，和正文内容
             * @param topic
             * @param tags
             * @param body 消息内容
             */
            Message message = new Message("TopicTest",
                    tags[i % tags.length],
                    "KEY"+i,
                    ("你好RocketMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            },orderId);

            System.out.printf("%s%n",sendResult);
        }

        //关闭生产者
        producer.shutdown();
    }
}
