package com.chimm.demo;

import com.alibaba.fastjson.JSON;
import com.chimm.domain.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author huangshuai
 * @date 2019-05-30
 */
@Component
public class DemoClient {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void getRedis() {
        String map = redisTemplate.opsForValue().get("demo_map");
        Demo demo = JSON.parseObject(map, Demo.class);
        System.err.println("从redis中接受到数据：" + demo.getId());
    }


    public void setRedis(Date date, String id) {
        Demo demo = new Demo();
        demo.setDate(date);
        demo.setId(id);
        redisTemplate.opsForValue().set("demo_map",JSON.toJSONString(demo));
        System.err.println("已成功写入redis");
    }


}
