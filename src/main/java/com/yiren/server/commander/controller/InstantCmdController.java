package com.yiren.server.commander.controller;

import com.yiren.server.commander.handler.CommanderServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class InstantCmdController {


    @RequestMapping(value = "/instant", produces = "application/javascript;charset=UTF-8")
    @ResponseBody
    public ModelAndView getInstant(@RequestParam("facilityId") String facilityId, @RequestParam("clientId") String clientId, @RequestParam("ak") String ak) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("instant");
        if (facilityId == null || clientId == null || ak == null) {
            modelAndView.addObject("message", "{\"code\":501,\"msg\":\"params missing \"}");
        } else {
            CommanderServer commander = CommanderServer.getINSTANCE();
            if (commander.isDeviceOnline(facilityId)) {
                long seq = commander.cmdInstantPic(facilityId);
                int count = 0;
                while (count < 20) {
                    String data = CommanderServer.getINSTANCE().getPicData(seq);
                    if (data != null) {
                       return modelAndView.addObject("message", "{\"code\":0,\"data\":" + data + "}");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count++;
                }
                modelAndView.addObject("message", "{\"code\":403,\"msg\":\"can not get pic\"}");
            } else {
               modelAndView.addObject("message", "{\"code\":410,\"msg\":\"device offline\"}");
            }
        }
        return modelAndView;
    }
}
