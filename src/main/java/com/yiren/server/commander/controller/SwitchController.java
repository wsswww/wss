package com.yiren.server.commander.controller;

import com.yiren.server.commander.handler.CommanderServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SwitchController {

    @RequestMapping(value = "/switch", produces = "application/javascript;charset=UTF-8")
    public ModelAndView cmdSwitch(@RequestParam("ak") String ak, @RequestParam("facility_id") String facilityId, @RequestParam("switch") String switc) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("switch");
        int io = Integer.MIN_VALUE;
        try {
            io = Integer.parseInt(switc);
        } catch (Exception e) {
        }
        if (ak == null || io == Integer.MIN_VALUE || facilityId == null) {
            return modelAndView.addObject("message", "{\"code\":501,\"msg\":\"params missing \"}");
        }
        CommanderServer commander = CommanderServer.getINSTANCE();
        if (commander.isDeviceOnline(facilityId)) {
            CommanderServer.getINSTANCE().cmdSwitch(ak, facilityId, io);
            return modelAndView.addObject("message", "{\"code\":0,\"msg\":\"success\",\"data\":[]}");
        } else {
            return modelAndView.addObject("message", "{\"code\":410,\"msg\":\"device offline\"}");
        }
    }
}
