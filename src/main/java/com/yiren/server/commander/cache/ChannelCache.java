package com.yiren.server.commander.cache;

import com.yiren.server.commander.model.DeviceChannel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelCache {
    private static ConcurrentHashMap<String, DeviceChannel> mChannels = new ConcurrentHashMap<>(); // 设备id, 连接channel
    private static ConcurrentHashMap<String, List<DeviceChannel>> oemDevices = new ConcurrentHashMap<>(); // 厂商id,厂商设备列表

    public static ConcurrentHashMap<String, DeviceChannel> getmChannels() {
        return mChannels;
    }

    public static DeviceChannel getmChannelsByDeviceId(String deviceId) {
        return mChannels.get(deviceId);
    }

    public static ConcurrentHashMap<String, List<DeviceChannel>> getOemDevices() {
        return oemDevices;
    }

    public static List<DeviceChannel> getDeviceList(String oem) {
        return oemDevices.get(oem);
    }
}
