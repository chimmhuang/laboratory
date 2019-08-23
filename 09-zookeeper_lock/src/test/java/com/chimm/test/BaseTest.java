package com.chimm.test;

import com.chimm.domain.TLock;
import com.chimm.mapper.TLockMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangshuai
 * @date 2019-08-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    @Autowired
    private TLockMapper tLockMapper;

    @Test
    public void testInsert() {
        tLockMapper.insert(TLock.builder().id(1).build());
        System.err.println("数据插入成功");
    }
}
