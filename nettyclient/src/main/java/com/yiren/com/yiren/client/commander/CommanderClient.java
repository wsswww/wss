package com.yiren.com.yiren.client.commander;

import com.yiren.com.yiren.client.commander.handler.CommanderClientChannelInitializer;
import com.yiren.com.yiren.client.commander.heart.ConnectionListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommanderClient {

    private static final Logger logger = LoggerFactory.getLogger(CommanderClient.class.getSimpleName());

    /**
     * 客户端连接
     */
    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new CommanderClientChannelInitializer());
        try {
            ChannelFuture future = bootstrap.connect(ClientAPP.HOST, ClientAPP.PORT).sync();
            future.addListener(new ConnectionListener());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new CommanderClient().connect();
    }
}
