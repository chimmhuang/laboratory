package com.rocketmq.spring.producer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huangshuai
 * @date 2019-04-16 19:58
 * @description
 */
@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        rocketMQTemplate.convertAndSend(
                "test-topic-1",
                "Hello, World!");
        rocketMQTemplate.send(
                "test-topic-1",
                MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        rocketMQTemplate.convertAndSend(
                "test-topic-2",
                new OrderPaidEvent("T_001",new BigDecimal(88.00)));
    }

    /*
        在发送客户端发送事务型消息并且实现回查Listener
        通过@RocketMQTransactionListener定义事务监听器
     */

    @Data
    @AllArgsConstructor
    public class OrderPaidEvent implements Serializable {
        private String orderId;
        private BigDecimal paidMoney;
    }
}
