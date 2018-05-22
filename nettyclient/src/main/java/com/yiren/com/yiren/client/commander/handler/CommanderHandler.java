package com.yiren.com.yiren.client.commander.handler;

import com.yiren.com.yiren.client.commander.protocol.CmdHeader;
import com.yiren.com.yiren.client.commander.protocol.CmdMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;

import static com.yiren.com.yiren.client.commander.protocol.CmdHeader.HEART_COUNT;
import static com.yiren.com.yiren.client.commander.protocol.CmdHeader.MSG_TYPE_REG;

@ChannelHandler.Sharable
public class CommanderHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接收server端的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CmdMsg message = (CmdMsg) msg;
        System.out.print("收到服务器心跳+++++++++++");
        System.out.println(message.toString());
    }

    /**
     * 向server发送消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CmdMsg msg = new CmdMsg();
        msg.header = new CmdHeader(MSG_TYPE_REG, MSG_TYPE_REG * 4);
        msg.body = "21,22,23,24";
        System.out.println("发送得数据：" + msg.toString() + "\n");
        ctx.channel().writeAndFlush(msg);
        System.out.println("激活时间是：" + new Date());

//            ctx.fireChannelActive();
    }

    /**
     * 断线触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是：" + new Date());
        System.out.println("停止方法 channelInactive");
        System.out.println("断线重新连接");
//        new CommanderClient().connect();
        super.channelInactive(ctx);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("长期没收到服务器推送数据");
//                    new CommanderClient().connect();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("心跳 channelActive");
                //发送心跳包
                sendHeartbeat(ctx);
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("ALL");
            }
        }
    }

    /**
     * 发送心跳包
     *
     * @param ctx
     */
    private void sendHeartbeat(ChannelHandlerContext ctx) {
        CmdMsg cmdMsg = new CmdMsg();
        cmdMsg.body = "clientHeart";
        cmdMsg.header = new CmdHeader(HEART_COUNT);
        System.out.println("发送心跳出去");
        ctx.writeAndFlush(cmdMsg);
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
