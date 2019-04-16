package com.rocketmq.demo03_TransactionExample.producer;

import com.rocketmq.demo03_TransactionExample.listener.TransactionListenerImpl;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author huangshuai
 * @date 2019-04-08 21:57
 *
 * 事务消息生产者
 */
public class TransactionProducer {

    public static void main(String[] args) throws Exception{
        //实例化事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("demo03");

        //指定消息地址
        producer.setNamesrvAddr("172.16.120.132:9876");
        producer.setSendMsgTimeout(1000*60);

        //指定消息监听对象，用于执行本地事务和消息回查
        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        //线程池
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(
                        2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        Thread thread = new Thread(runnable);
                        thread.setName("client-transaction-msg-check-thread");
                        return thread;
                    }
                }
        );
        producer.setExecutorService(executorService);

        //加载生产者
        producer.start();

        String[] tags = new String[]{"TagA"};

        /**
         * 创建消息实例，指定topic，tag，和正文内容
         * @param topic
         * @param tags
         * @param body 消息内容
         */
        Message message = new Message("TopicTransactionTest",
                "Tags",
                "KEY",
                ("你好RocketMQ事务模式").getBytes(RemotingHelper.DEFAULT_CHARSET));

        //发送事务消息
        TransactionSendResult result = producer.sendMessageInTransaction(message, "hello-transaction");

        System.out.println(result);

        //关闭生产者
        producer.shutdown();
    }
}
