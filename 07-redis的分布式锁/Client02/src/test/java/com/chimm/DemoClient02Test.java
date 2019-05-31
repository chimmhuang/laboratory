package com.chimm;

import com.chimm.client.Client02;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangshuai
 * @date 2019-05-31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoClient02Test {

    @Autowired
    private Client02 client;


    @Test
    public void testClient() {
        client.getId();
    }
}
