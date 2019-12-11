package com.chimm.muxin;

import com.chimm.muxin.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@SpringBootApplication
@MapperScan(basePackages = "com.chimm.muxin.mapper")
@ComponentScan(basePackages = {"com.chimm.muxin","org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public SpringUtil getSpringUtil() {
        return new SpringUtil();
    }
}
