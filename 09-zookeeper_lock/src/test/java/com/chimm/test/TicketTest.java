package com.chimm.test;

import com.chimm.Application;
import com.chimm.lock.ZookeeperImproveLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.locks.Lock;

/**
 * 火车票购买案列
 *
 * @author chimm
 * @date 2019/8/18 0018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TicketTest {

//    @Autowired
//    private Lock lock;

    /** 100张票 */
    private static Integer count = 100;

    @Test
    public void ticketTest() throws Exception {
        TicketRunnable tr = new TicketRunnable();
        // 四个线程对应四个窗口
        Thread t1 = new Thread(tr,"窗口A");
        Thread t2 = new Thread(tr,"窗口B");
        Thread t3 = new Thread(tr,"窗口C");
        Thread t4 = new Thread(tr,"窗口D");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // 该方法解释：主线程开启了新的线程，调用该方法，等待子线程执行完毕
        // 主线程等待子线程执行完毕
        Thread.currentThread().join();

    }

    public class TicketRunnable implements Runnable {

        @Override
        public void run() {
            while (count > 0) {
                Lock lock = new ZookeeperImproveLock();
                lock.lock();
                if (count > 0) {
                    System.out.println(Thread.currentThread().getName() + "售出第" + (count--) + "张票");
                }
                lock.unlock();

                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
