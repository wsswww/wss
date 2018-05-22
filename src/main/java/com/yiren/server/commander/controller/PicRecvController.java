package com.yiren.server.commander.controller;

import com.yiren.server.commander.handler.CommanderServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

@RestController
public class PicRecvController {



    @RequestMapping(value = "/pic", produces = "application/json;charset=UTF-8")
    public ModelAndView cmdPic(@RequestParam("seqid") String seqid, @RequestParam("facility_id") String facility_id, @RequestParam("startTime") String start_time, @RequestParam("endTime") String end_time, @RequestParam("pic_num") String pic_num, @RequestParam("client_id") String client_id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pic");
        if (seqid == null || facility_id == null || start_time == null || end_time == null ||
                pic_num == null || client_id == null) {
            return modelAndView.addObject("message", "error");
        } else {
            CommanderServer commander = CommanderServer.getINSTANCE();
            if (commander.isDeviceOnline(facility_id)) {
                CommanderServer.getINSTANCE().cmdPic(facility_id, seqid, Long.parseLong(start_time), Long.parseLong(end_time));
                return modelAndView.addObject("message", "{\"code\":0,\"msg\":\"success\",\"data\":[]}");
            } else {
                return modelAndView.addObject("message", "{\"code\":410,\"msg\":\"device offline\",\"data\":[]}");
            }
        }
    }
}
