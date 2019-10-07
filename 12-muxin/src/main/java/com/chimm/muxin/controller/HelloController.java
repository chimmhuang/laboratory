package com.chimm.muxin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chimm
 * @date 2019/10/4 0004
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello muxin~~";
    }
}
