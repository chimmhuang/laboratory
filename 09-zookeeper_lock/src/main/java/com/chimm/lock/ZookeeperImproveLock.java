package com.chimm.lock;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于zookeeper的分布式锁实现方式
 *
 * @author huangshuai
 * @date 2019-08-28
 */
@Slf4j
public class ZookeeperImproveLock implements Lock {

    /** zookeeper获取锁的跟路径 */
    private static final String LOCK_PATH = "/LOCK";
    /** zookeeper的地址及端口 */
//    private static final String ZOOKEEPER_IP_PORT = "172.16.120.140:2181";
    private static final String ZOOKEEPER_IP_PORT = "192.168.48.129";

    /** zookeeper客户端 */
    private ZkClient client = new ZkClient(ZOOKEEPER_IP_PORT, 100000, 100000, new SerializableSerializer());

    /**
     * 多线程计数器、调用await()方法阻塞等待，当countDown=0的时候，放行所有await()阻塞的线程
     */
    private CountDownLatch countDownLatch;

    /** 当前请求的节点前一个节点 */
    private String beforePath;
    /** 当前请求的节点 */
    private String currentPath;

    /** 判断有没有LOCK目录，没有则创建 */
    public ZookeeperImproveLock() {
        if (!this.client.exists(LOCK_PATH)) {
            this.client.createPersistent(LOCK_PATH);
        }
    }

    @Override
    public void lock() {
        //阻塞式加锁
        Statement statement = null;
        //尝试加锁
        if (tryLock()) {
            return;
        }
        //如果没有加上阻塞当前线程
        waitForLock();
        //递归调用再尝试加锁
        lock();
    }

    private void waitForLock() {
        //让线程休眠一段时间
        IZkDataListener listener = new IZkDataListener() {

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //捕获到当前节点被删除的事件后，发令抢countdown，让主线程停止等待
                log.info(Thread.currentThread().getName() + "捕获到DataDelete事件！");
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }

            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

        };

        //给排在前面的节点增加数据删除的watcher，本质是启动呢另外一个线程去监听前置节点
        this.client.subscribeDataChanges(beforePath, listener);

        if (this.client.exists(beforePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error("异常捕捉：{}", e.getMessage(), e);
            }
        }
        this.client.unsubscribeDataChanges(beforePath, listener);
    }

    @Override
    public boolean tryLock() {
        //非阻塞式加锁
        //如果currentPath为空则为第一次加锁，第一次加锁赋值currentPath
        if (currentPath == null || currentPath.length() <= 0) {
            //创建一个临时顺序节点
            currentPath = this.client.createEphemeralSequential(LOCK_PATH + "/", "lock");
            log.info("------------------------->" + currentPath);
        }
        //获取所有临时节点并排序，临时节点名称为自增长的字符串入：0000000400
        List<String> children = this.client.getChildren(LOCK_PATH);
        Collections.sort(children);

        if (currentPath.equals(LOCK_PATH + "/" + children.get(0))) {
            //如果当前节点在所有节点中排名第一则获取锁成功
            return true;
        } else {
            //如果当前节点在所有节点中排名不是第一个，则获取前面的节点名称，并赋值给beforePath
            int wz = Collections.binarySearch(children, currentPath.substring(6));
            beforePath = LOCK_PATH + "/" + children.get(wz - 1);
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //解锁
        //删除当前临时节点
        client.delete(currentPath);
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    @Override
    public void lockInterruptibly() throws InterruptedException {
        //可中断的
    }
}
