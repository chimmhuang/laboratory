package com.chimm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chimm
 * @date 2019/8/18 0018
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.chimm.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
