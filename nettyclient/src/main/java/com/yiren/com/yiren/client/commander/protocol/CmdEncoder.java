package com.yiren.com.yiren.client.commander.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class CmdEncoder extends MessageToByteEncoder<CmdMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CmdMsg msg, ByteBuf byteBuf) throws Exception {

        if (msg == null | msg.header == null) {
            throw new Exception("The encode message is null");
        }
        byte[] bodyBytes = msg.body.getBytes(Charset.forName("utf-8"));
        int bodySize = bodyBytes.length;
        byteBuf.writeByte(msg.header.magic);
        byteBuf.writeByte(msg.header.msgType);
        byteBuf.writeInt(bodySize);
        byteBuf.writeBytes(bodyBytes);
    }
}
