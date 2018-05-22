package com.yiren.com.yiren.client.commander.handler;

import com.yiren.com.yiren.client.commander.ClientAPP;
import com.yiren.com.yiren.client.commander.protocol.CmdDecoder;
import com.yiren.com.yiren.client.commander.protocol.CmdEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class CommanderClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("handler", new IdleStateHandler(30
                , 20, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast("decoder", new CmdDecoder(ClientAPP.MAX_FRAME_LENGTH,
                ClientAPP.LENGTH_FIELD_OFFSET, ClientAPP.LENGTH_FIELD_LENGTH, ClientAPP.LENGTH_ADJUSTMENT, ClientAPP.INITIAL_BYTES_TO_STRIP));
        ch.pipeline().addLast("encoder", new CmdEncoder());
        pipeline.addLast(new CommanderHandler());
    }
}
