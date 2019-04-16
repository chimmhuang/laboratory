package com.rocketmq.demo03_TransactionExample.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author huangshuai
 * @date 2019-04-06 19:32
 *
 * 事务消费者
 */
public class Consumer03 {

    public static void main(String[] args) throws Exception{
        //实例化一个消费者（可以自定义名称，默认：DEFAULT_CONSUMER）
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("demo03");

        //指定消息地址
        consumer.setNamesrvAddr("172.16.120.132:9876");

        /**
         * 订阅要消费的Topic
         * @param topic
         * @param subExpression 订阅表达式，如："tag1 || tag2"，如果为null或者为*，则表示订阅全部
         */

        consumer.subscribe("TopicTransactionTest","*");

        consumer.setVipChannelEnabled(false);

        //创建消息监听MessageListenner
        consumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    try {

                        //获取主题
                        String topic = msg.getTopic();

                        //获取标签
                        String tags = msg.getTags();

                        //获取信息
                        byte[] body = msg.getBody();
                        String result = new String(body, RemotingHelper.DEFAULT_CHARSET);

                        System.out.println("Transaction--Consumer消费信息---topic:" + topic + ",tags:" + tags + ",result:" + result);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                        //消息重试
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                }

                //成功消费消息
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        //开启消费者实例
        consumer.start();

        System.out.println("Consumer Started");
    }
}
