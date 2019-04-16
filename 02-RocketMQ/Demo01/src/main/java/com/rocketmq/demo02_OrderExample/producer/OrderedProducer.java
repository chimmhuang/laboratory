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
 * 顺序发送消息示例代码
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

        //String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        String[] tags = new String[]{"TagA"};
        for (int i = 0; i < 10; i++) {

            //int orderId = i % 10;

            /**
             * 创建消息实例，指定topic，tag，和正文内容
             * @param topic
             * @param tags
             * @param body 消息内容
             */
            Message message = new Message("TopicOrderTest",
                    tags[i % tags.length],
                    "KEY"+i,
                    ("你好RocketMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            /**
             * 顺序发送消息
             * 第一个参数：发送的消息信息
             * 第二个参数：选中指定的消息队列对象（会将所有消息队列传入进来）
             * 第三个参数：指定对应的队列下标
             */
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                /**
                 * 队列选择
                 * @param mqs 队列集合（如果是集群，那么就是集群总共的队列集合）
                 * @param msg 发送的消息
                 * @param arg 指定队列的下标
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    //获取队列的下标
                    Integer index = (Integer) arg;

                    //int index = id % mqs.size();

                    //获取对应下标的队列
                    return mqs.get(index);
                }
            },0);

            System.out.printf("%s%n",sendResult);
        }

        //关闭生产者
        producer.shutdown();
    }
}
