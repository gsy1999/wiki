package com.knowl.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${test.Hello:TEST}")  //冒号后面是默认值，如果没有扫描到配置文件的对应内容，则会显示默认值
    private String testHello;
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!"+ testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World! Post" +name;
    }
}
