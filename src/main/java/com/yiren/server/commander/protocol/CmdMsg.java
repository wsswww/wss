package com.yiren.server.commander.protocol;

public class CmdMsg {
    public CmdHeader header;
    /*
     * 注册命令  注册设备 `{$device_id=设备id},{$oem=厂商},{$imei=imei}` <br/>
     *
     *
     * 30 实时图片下发指令   ""
     * 31 实时图片接收指令   deviceId,seqId,图片url,图片大小(kb)
     * 32 图片指令      seqId(指令id),startTime(起始时间，毫秒),endTime(结束时间，毫秒)
     *
     * 41 更新插件命令    versionCode,updateUrl
     * 42 系统指令  int(> 0 启动 else 关闭)
     */
    public String body;

    @Override
    public String toString() {
        return header.toString() + "<" + body + ">";
    }
}
