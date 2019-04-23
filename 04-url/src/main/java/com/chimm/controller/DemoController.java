package com.chimm.controller;

import com.chimm.domain.Login;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangshuai
 * @date 2019-04-23 10:13
 * @description
 */
@RestController
public class DemoController {

    @GetMapping("/testGet")
    public void getMapping(@ModelAttribute Login login) {
        System.err.println("账号：" + login.getName());
        System.err.println("密码：" + login.getPwd());
    }

    @PostMapping("/testPost1/{path}")
    public void PostMapping1(@RequestBody Login login,@PathVariable String path) {
        System.err.println(login.getName());
        System.err.println(login.getPwd());
        System.err.println(path);
    }


    @PostMapping("/testPost2")
    public void PostMapping2(@RequestBody Login login,String path) {
        System.err.println(login.getName());
        System.err.println(login.getPwd());
        System.err.println(path);
    }
}
