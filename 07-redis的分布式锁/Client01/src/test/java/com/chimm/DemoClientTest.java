package com.chimm;

import com.chimm.client.Client01;
import com.chimm.demo.DemoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author huangshuai
 * @date 2019-05-30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoClientTest {

    @Autowired
    private DemoClient demoClient;
    @Autowired
    private Client01 client;

    @Test
    public void test() {
        demoClient.setRedis(new Date(),"1");
        demoClient.getRedis();
    }

    @Test
    public void testClient() {
        client.getId();
    }
}
