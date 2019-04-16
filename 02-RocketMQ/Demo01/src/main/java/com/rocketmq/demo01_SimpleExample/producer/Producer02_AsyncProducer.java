package com.rocketmq.demo01_SimpleExample.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author huangshuai
 * @date 2019-04-06 21:14
 *
 * 异步消息发送者
 * 异常消息传输，一般用于需要快速响应的业务场景中
 */
public class Producer02_AsyncProducer {

    public static void main(String[] args) throws Exception{
        //实例化一个生产者（可以自定义名称，默认：DEFAULT_PRODUCER）
        DefaultMQProducer producer = new DefaultMQProducer("demo01");

        //指定消息地址
        producer.setNamesrvAddr("172.16.120.132:9876");

        producer.setVipChannelEnabled(false);
        producer.setSendMsgTimeout(1000*60);

        //加载实例
        producer.start();

        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 5; i++) {
            final int index = i;

            /**
             * 创建消息实例，指定topic，tag，和正文内容
             * @param topic
             * @param tags
             * @param keys
             * @param body 消息内容
             */
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "你好RockeyMQ".getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(60000L);
        producer.shutdown();
    }
}
