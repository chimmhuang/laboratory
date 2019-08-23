package com.chimm.lock;

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

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
