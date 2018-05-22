package com.yiren.server.commander.model;


import io.netty.channel.Channel;

public class DeviceChannel {
    public String deviceId;
    public String imei;
    public String oem;
    public String apkVersionCode;
    public Channel channel;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getApkVersionCode() {
        return apkVersionCode;
    }

    public void setApkVersionCode(String apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public DeviceChannel(String deviceId, String imei, String oem, String apkVersionCode) {
        this.deviceId = deviceId;
        this.imei = imei;
        this.oem = oem;
        this.apkVersionCode = apkVersionCode;
    }

    public DeviceChannel() {
    }

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
