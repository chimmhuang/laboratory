package com.chimm.muxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@SpringBootApplication
@MapperScan(basePackages = "com.chimm.muxin.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
