package com.chimm.controller;

import com.alibaba.fastjson.JSON;
import com.chimm.domain.Login;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author huangshuai
 * @date 2019-04-23 10:16
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoControllerTest {

    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new DemoController()).build();
    @Autowired
    protected WebApplicationContext wac;


    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    public void testGetMapping() throws Exception{
        mockMvc.perform(get("/testGet?name=chimm&pwd=123"));
    }

    @Test
    public void testPostMapping1() throws Exception{
        Login login = new Login();
        login.setName("吃茫茫");
        login.setPwd("123456");
        mockMvc.perform(post("/testPost1/step1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(login)));
    }

    @Test
    public void testPostMapping2() throws Exception{
        Login login = new Login();
        login.setName("吃茫茫");
        login.setPwd("123456");
        mockMvc.perform(post("/testPost2?path=step1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(login)));
    }

}
