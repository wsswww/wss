package com.yiren.server.commander.ptotocol;

public class CmdHeader {
    private static byte MAGIC_NUM = 44;

    public static byte MSG_TYPE_REG = 21;
    public static byte MSG_TYPE_INSTANT_IMG = 31;
    public static byte MSG_TYPE_INSTANT_RECV = 32;
    public static byte MSG_TYPE_PICS = 33;
    public static byte MSG_TYPE_UPDATE = 41;
    public static byte MSG_TYPE_SYSTEM = 42;
    public static byte HEART_COUNT = 1;//心跳判定
    public byte magic; // 魔数
    public byte msgType; // 消息类型
    public int len; // 长度

    public CmdHeader(byte msgType) {
        this.magic = MAGIC_NUM;
        this.msgType = msgType;
    }

    public CmdHeader(byte msgType, int len) {
        this.magic = MAGIC_NUM;
        this.msgType = msgType;
        this.len = len;
    }

    public CmdHeader() {

    }

    @Override
    public String toString() {
        return "[" + magic + "|" + msgType + "|" + len + "]";
    }
}
