package com.chimm.test.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.junit.Test;

/**
 * zookeeper的观察者测试
 *
 * @author huangshuai
 * @date 2019-08-27
 */
public class WatchTest {

    private static final String ZOOKEEPER_IP_PORT = "172.16.120.140";

    private ZkClient client = new ZkClient(ZOOKEEPER_IP_PORT, 10000, 10000, new SerializableSerializer());

    @Test
    public void watchTest() throws Exception {
        // 1.创建一个持久节点
        String path = "/watcher";
        client.createPersistent(path);

        // 2.实例化一个监听器
        IZkDataListener listener = new IZkDataListener() {
            //数据变化通知处理器
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            //数据删除通知处理器
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //捕获到节点被删除的事件
                System.out.println("收到节点被删除的事件，被删除的节点为：" + dataPath);
            }
        };

        // 3.给该节点订阅监听器
        this.client.subscribeDataChanges(path, listener);

        Thread.currentThread().join();
    }
}
