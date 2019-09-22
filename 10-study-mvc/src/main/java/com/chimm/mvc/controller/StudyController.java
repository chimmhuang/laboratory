package com.chimm.mvc.controller;

import com.chimm.mvc.annotation.Autowired;
import com.chimm.mvc.annotation.Controller;
import com.chimm.mvc.annotation.RequestMapping;
import com.chimm.mvc.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Controller
@RequestMapping("/study")
public class StudyController {

    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        System.err.println("进入了test方法");
        userService.printHelloWord();
    }
}
