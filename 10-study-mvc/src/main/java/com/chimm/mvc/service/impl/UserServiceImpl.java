package com.chimm.mvc.service.impl;

import com.chimm.mvc.annotation.Service;
import com.chimm.mvc.service.UserService;

/**
 * @author chimm
 * @date 2019/9/21 0021
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public void printHelloWord() {
        System.err.println("Service调用成功");
    }
}
