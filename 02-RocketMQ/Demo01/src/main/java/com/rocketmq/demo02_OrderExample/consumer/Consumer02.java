package com.rocketmq.demo02_OrderExample.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author huangshuai
 * @date 2019-04-08 13:20
 *
 * 订阅消息示例代码
 */
public class Consumer02 {

    public static void main(String[] args) throws Exception{

        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("demo02");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //指定消息地址
        consumer.setNamesrvAddr("172.16.120.132:9876");
        /**
         * 订阅要消费的Topic
         * @param topic
         * @param subExpression 订阅表达式，如："tag1 || tag2"，如果为null或者为*，则表示订阅全部
         */

        consumer.subscribe("TopicTest","TagA || TagC || TagD");

        //注册消息监听器
        consumer.registerMessageListener(new MessageListenerOrderly() {

            AtomicLong consumeTimes = new AtomicLong(0);

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(false);
                byte[] body = msgs.get(0).getBody();
                String str = null;
                try {
                    str = new String(body, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.printf(Thread.currentThread().getName()+"：收到了新消息："+ str + "%n");

                //多线程计数器+1
                this.consumeTimes.incrementAndGet();

                if ((this.consumeTimes.get() % 2) == 0) {
                    return ConsumeOrderlyStatus.SUCCESS;
                } else if ((this.consumeTimes.get() % 3) == 0) {
                    return ConsumeOrderlyStatus.ROLLBACK;
                } else if ((this.consumeTimes.get() % 4) == 0) {
                    return ConsumeOrderlyStatus.COMMIT;
                } else if ((this.consumeTimes.get() % 5) == 0) {
                    context.setSuspendCurrentQueueTimeMillis(3000);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        System.out.println("Consumer Started");
    }
}
