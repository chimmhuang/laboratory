package com.rocketmq.demo03_TransactionExample.listener;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangshuai
 * @date 2019-04-16 15:43
 *
 * 事务消息监听对象，用于执行本地事务和消息回查
 */
public class TransactionListenerImpl implements TransactionListener {

    //存储对应事务的状态信息， key:事务id ， value:当前事务执行的状态
    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * 执行本地事务
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //事务id
        String transactionId = msg.getTransactionId();

        //0-执行中，状态未知 、 1-本地事务执行成功 、 2-本地事务执行失败
        localTrans.put(transactionId, 0);

        //业务执行，处理本地事务，service
        System.out.println("hello!----Demo----Transaction");

        try {
            System.out.println("正在执行本地事务---");
            Thread.sleep(12000);
            System.out.println("正在执行本地事务---成功");
            localTrans.put(transactionId, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            localTrans.put(transactionId, 2);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * 消息回查
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        //获取对应事务id的状态
        String transactionId = msg.getTransactionId();
        //获取事务id的执行状态
        Integer status = localTrans.get(transactionId);

        switch (status) {
            case 0:
                return LocalTransactionState.UNKNOW;
            case 1:
                return LocalTransactionState.COMMIT_MESSAGE;
            case 2:
                return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        return LocalTransactionState.UNKNOW;
    }
}
