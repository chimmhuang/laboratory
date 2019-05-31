package com.chimm.client;

import com.alibaba.fastjson.JSON;
import com.chimm.domain.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁的应用
 *
 * @author huangshuai
 * @date 2019-05-31
 */
@Component
public class Client02 {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;


    public void getId() {
        //获取锁
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("fin_lock", Thread.currentThread().toString(), 30L, TimeUnit.SECONDS);
        if (lock) {
            //获取成功
            String map = redisTemplate.opsForValue().get("demo_map");
            Demo demo = JSON.parseObject(map, Demo.class);
            Date date = demo.getDate();
            int redisDay = date.getDate();
            int today = new Date().getDate();
            if (today > redisDay) {
                //将demo设置为今天
                demo.setDate(new Date());
                demo.setId("100");
            }
            int id = Integer.parseInt(demo.getId());

            System.err.println("RK" + ++id);

            demo.setId(id + "");

            redisTemplate.opsForValue().set("demo_map", JSON.toJSONString(demo));

            //释放锁
            String lockValue = redisTemplate.opsForValue().get("fin_lodk");
            if (lockValue.equals(Thread.currentThread().toString())) {
                redisTemplate.delete("fin_lock");
            }
        } else {
            System.err.println("未能获取到锁");
        }
    }

}
