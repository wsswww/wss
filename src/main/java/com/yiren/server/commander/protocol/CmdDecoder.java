package com.yiren.server.commander.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class CmdDecoder extends LengthFieldBasedFrameDecoder {
    private static final int HEADER_SIZE = 6;

    public CmdDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf inn) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, inn);
        if (buf == null) {
            return null;
        }
        if (buf.readableBytes() < HEADER_SIZE) {
            return null;
        }
        buf.readByte();
        byte msgType = buf.readByte();
        int len = buf.readInt();

        if (buf.readableBytes() < len) {
            return null;
        }
        ByteBuf dataBuf = buf.readBytes(len);
        byte[] datas = new byte[dataBuf.readableBytes()];
        dataBuf.readBytes(datas);
        CmdMsg msg = new CmdMsg();
        msg.header = new CmdHeader(msgType, len);
        msg.body = new String(datas, "UTF-8");
        return msg;
    }
}

