package com.yiren.server.commander.controller;


import com.yiren.server.commander.handler.CommanderServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@RestController
public class UpdateCmdController {


    @RequestMapping("/update")
    public ModelAndView updateCmd(@RequestParam("apkUrl") String apkUrl, @RequestParam("ver") String ve) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("update");
        int ver = 0;
        try {
            ver = Integer.parseInt(ve);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (apkUrl == null || ver == 0) {
            return null;
        }
        CommanderServer.getINSTANCE().cmdUpdate(ver, apkUrl);
        return modelAndView.addObject("message", "{\"code\":200,\"msg\":" + apkUrl + "}");
    }
}
