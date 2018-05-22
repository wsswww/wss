package com.yiren.server.commander.model;


import io.netty.channel.Channel;

public class DeviceChannel {
    public String deviceId;
    public String imei;
    public String oem;
    public String apkVersionCode;
    public Channel channel;

    @Override
    public String toString() {
        return new StringBuilder(deviceId)
                .append(",")
                .append(oem)
                .append(",")
                .append(apkVersionCode)
                .append(",")
                .append(channel != null ? channel.toString() : "")
                .toString();
    }
}
