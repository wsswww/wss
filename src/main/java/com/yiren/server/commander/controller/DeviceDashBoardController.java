package com.yiren.server.commander.controller;


import com.yiren.server.commander.cache.ChannelCache;
import com.yiren.server.commander.model.DeviceChannel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DeviceDashBoardController {

    @RequestMapping(value = "/dashboard", produces = "application/json;charset=UTF-8")
    public ModelAndView getDeviceList() {
        StringBuilder online = new StringBuilder();
        StringBuilder offline = new StringBuilder();
        ModelAndView modelAndView=new ModelAndView();
        List<DeviceChannel> deviceChannelList = new ArrayList<>();
        for (Map.Entry<String, DeviceChannel> entry : ChannelCache.getmChannels().entrySet()) {
            deviceChannelList.add(entry.getValue());
        }
        deviceChannelList.add(new DeviceChannel("dd", "dd", "DD", "dd"));
        deviceChannelList.add(new DeviceChannel("dd", "dd", "DD", "dd"));
        deviceChannelList.add(new DeviceChannel("dd", "dd", "DD", "dd"));
        modelAndView.setViewName("dashboard");
        modelAndView.addObject("list",deviceChannelList);
        return modelAndView;
    }
}
