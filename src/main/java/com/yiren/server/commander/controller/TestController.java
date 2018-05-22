package com.yiren.server.commander.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController{

    @RequestMapping("/hello")
    public String helloSpringBoot(){
        System.out.println("Tomcat启动+++++++++++++++++++++++++++++");
        return "index";
    }


}
