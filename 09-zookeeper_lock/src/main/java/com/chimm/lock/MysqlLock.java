package com.chimm.lock;

import com.chimm.domain.TLock;
import com.chimm.mapper.TLockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁的实现———数据库实现方式
 * 继承juc的Lock类，重写方法
 *
 * @author huangshuai
 * @date 2019-08-22
 */
@Service
public class MysqlLock implements Lock {

    @Autowired
    private TLockMapper tLockMapper;


    //所有线程都往数据库插入主键值相同的数据
    private static final int LOCK_ID = 1;

    @Override
    public void lock() {
        //阻塞式加锁
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
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        //非阻塞式加锁
        try {
            tLockMapper.insert(TLock.builder().id(LOCK_ID).build());
        } catch (Exception e) {
            //插入失败，加锁失败
            return false;
        }
        //加锁成功
        return true;
    }

    @Override
    public void unlock() {
        //解锁
        tLockMapper.delete(TLock.builder().id(LOCK_ID).build());
    }

    //有条件的
    @Override
    public Condition newCondition() {
        return null;
    }

    //给时间进行加解锁的
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    //可中断的
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
