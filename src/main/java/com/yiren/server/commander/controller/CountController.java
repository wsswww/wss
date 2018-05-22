package com.yiren.server.commander.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CountController {

    @RequestMapping(value = "/instants")
    public ModelAndView returns(){
        ModelAndView modelAndView=new ModelAndView("/instant");
        return modelAndView;
    }
}
