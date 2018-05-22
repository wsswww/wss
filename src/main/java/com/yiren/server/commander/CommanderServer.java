package com.yiren.server.commander;

import com.yiren.server.commander.model.DeviceChannel;
import com.yiren.server.commander.ptotocol.CmdDecoder;
import com.yiren.server.commander.ptotocol.CmdEncoder;
import com.yiren.server.commander.ptotocol.CmdHeader;
import com.yiren.server.commander.ptotocol.CmdMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.yiren.server.commander.ptotocol.CmdHeader.HEART_COUNT;
import static com.yiren.server.commander.ptotocol.CmdHeader.MSG_TYPE_SYSTEM;

public class CommanderServer {
    private static final CommanderServer INSTANCE = new CommanderServer();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderServer.class.getSimpleName());

    public static CommanderServer getINSTANCE() {
        return INSTANCE;
    }

    private CommanderServer() {
    }

    public static final int PORT = 20021;
    private ConcurrentHashMap<String, DeviceChannel> mChannels = new ConcurrentHashMap<>(); // 设备id, 连接channel
    private ConcurrentHashMap<String, List<DeviceChannel>> oemDevices = new ConcurrentHashMap<>(); // 厂商id,厂商设备列表

    private ConcurrentHashMap<Long, String> picDatas = new ConcurrentHashMap<>();

    public String getPicData(long seq) {
        return picDatas.remove(seq);
    }


    public boolean isDeviceOnline(String deviceId) {
        DeviceChannel deviceChannel = mChannels.get(deviceId);
        if (deviceChannel == null) {
            return false;
        } else {
            return deviceChannel.channel.isActive();
        }
    }

    public String getDevicelist() {
        StringBuilder online = new StringBuilder();
        StringBuilder offline = new StringBuilder();
        for (Map.Entry<String, DeviceChannel> entry : mChannels.entrySet()) {
            if (entry.getValue().channel.isActive()) {
                online.append(entry.getValue().toString())
                        .append("\r\n");
            } else {
                offline.append(entry.getValue().toString())
                        .append("\r\n");
            }
        }
        return "online:\r\n" + online.toString() + "\r\noffline:\r\n" + offline.toString();
    }

    public long cmdInstantPic(String deviceId) {
        if (mChannels.containsKey(deviceId) && mChannels.get(deviceId).channel.isActive()) {
            long seqId = System.currentTimeMillis();
            Channel channel = mChannels.get(deviceId).channel;
            CmdMsg msg = new CmdMsg();
            msg.header = new CmdHeader(CmdHeader.MSG_TYPE_INSTANT_IMG);
            msg.body = seqId + "";
            channel.writeAndFlush(msg);
            LOGGER.info("sending down:\t" + msg.toString());
            return seqId;
        }
        return 0;
    }

    public void cmdPic(String deviceId, String seqId, long startTime, long endTime) {
        if (mChannels.containsKey(deviceId)) {
            Channel channel = mChannels.get(deviceId).channel;
            String cmd = seqId + "," + startTime + "," + endTime;
            CmdMsg msg = new CmdMsg();
            msg.header = new CmdHeader(CmdHeader.MSG_TYPE_PICS);
            msg.body = cmd;
            channel.writeAndFlush(msg);
            LOGGER.info("sending down:\t" + msg.toString());
        }
    }

    /**
     * @param ak
     * @param ioStatus
     */
    public void cmdSwitch(String ak, String deviceId, int ioStatus) {
        String oem = null;
        switch (ak) {
            case "befe6eff6ece49259065f02aa881b905":
                oem = "futong";
                break;
            case "eb2c3da8f666477bb48bb7100623c7ad":
                oem = "lingdu";
                break;
            default:
                break;
        }
        if (oem == null) {
            return;
        }
        if (oemDevices.containsKey(oem)) {
            String cmd = String.valueOf(ioStatus);
            CmdMsg msg = new CmdMsg();
            msg.header = new CmdHeader(MSG_TYPE_SYSTEM);
            msg.body = cmd;
            mChannels.get(deviceId).channel.writeAndFlush(msg);
            LOGGER.info("sending down to " + deviceId + ":\t" + msg.toString());
        }
    }

    public void cmdUpdate(int versionCode, String updateUrl) {
        for (Map.Entry<String, DeviceChannel> entry : mChannels.entrySet()) {
            String cmd = versionCode + "," + updateUrl;
            CmdMsg msg = new CmdMsg();
            msg.header = new CmdHeader(CmdHeader.MSG_TYPE_UPDATE);
            msg.body = cmd;
            entry.getValue().channel.writeAndFlush(msg);
            LOGGER.info("sending down:\t" + msg.toString() + "\t to " + entry.getKey());
        }
    }

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 2;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;


    public void run() {
        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new IdleStateHandler(25, 30, 0, TimeUnit.SECONDS));
                                ch.pipeline().addLast("decoder", new CmdDecoder(MAX_FRAME_LENGTH,
                                        LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                                ch.pipeline().addLast("encoder", new CmdEncoder());
                                ch.pipeline().addLast(new CommanderServerHandler());
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture f = b.bind(PORT).sync();
                LOGGER.info("Commander standing by");
                System.out.println();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }

    public class CommanderServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }


        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            LOGGER.info("server channelRead..");
            CmdMsg cmdMsg1 = (CmdMsg) msg;
            LOGGER.info("连接状况的channel状态:" + ctx.channel().isActive());
            LOGGER.info("server" + cmdMsg1.toString());
            if (msg instanceof CmdMsg) {
                CmdMsg cmdMsg = (CmdMsg) msg;
                LOGGER.info("receive cmd :\t" + cmdMsg.toString());
                LOGGER.info("receive cmd lenth:\t" + cmdMsg.header.len);
                if (CmdHeader.MSG_TYPE_REG == cmdMsg.header.msgType) {
                    //{$device_id=设备id},{$oem=厂商},{$imei=imei}
                    String[] data = cmdMsg.body.split(",");
                    if (data.length >= 4) {
                        DeviceChannel deviceChannel = new DeviceChannel();
                        deviceChannel.deviceId = data[0].trim();
                        deviceChannel.oem = data[1].trim();
                        deviceChannel.imei = data[2].trim();
                        deviceChannel.apkVersionCode = data[3].trim();
                        deviceChannel.channel = ctx.channel();
                        // put into channels
                        mChannels.put(deviceChannel.deviceId, deviceChannel);
                        // put into devices
                        LOGGER.info(deviceChannel.deviceId + "进入程序");
                        if (!oemDevices.containsKey(deviceChannel.oem)) {
                            oemDevices.put(deviceChannel.oem, new ArrayList<>());
                        }
                        oemDevices.get(deviceChannel.oem).add(deviceChannel);
                    }
                } else if (CmdHeader.MSG_TYPE_INSTANT_RECV == cmdMsg.header.msgType) {
                    // deviceId,seqId,图片url,图片大小(kb)
                    String[] data = cmdMsg.body.split(",");
                    if (data.length == 4) {
                        String deviceId = data[0];
                        long seqId = Long.parseLong(data[1]);
                        String url = data[2];
                        String length = data[3];
                        String result = "{\"time\": " + seqId + ",  \"size\": " + length + ", \"resource\": \"" + url + "\"}";
                        picDatas.put(seqId, result);
                    }
                } else if(HEART_COUNT==cmdMsg.header.msgType){
                    LOGGER.info("接收心跳的channel状态:" + ctx.channel().isActive());
                    LOGGER.info("接收到心跳:"+cmdMsg.toString());
//                    cmdMsg.body = "serverHeart";
                    cmdMsg.header = new CmdHeader();
                    ctx.channel().writeAndFlush(cmdMsg);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOGGER.error("device offline with exception:");
            cause.printStackTrace();
            System.out.println();
            ctx.close();
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
            if (obj instanceof IdleStateEvent) {
                IdleState state = ((IdleStateEvent) obj).state();
                if (state == IdleState.READER_IDLE) {
                    LOGGER.info("读取信息超时");
                    LOGGER.info("关闭之前的channel:" + ctx.channel().isActive());
                    ctx.channel().close();
                    LOGGER.info("关闭之后的channel:" + ctx.channel().isActive());
//                  getState(ctx);//查看集合中channel的状态
                } else if (state == IdleState.WRITER_IDLE) {
                    LOGGER.info("写出信息超时");
                }
            } else {
                super.userEventTriggered(ctx, obj);
            }
        }

        /**
         * 查看关闭之后集合中channel的状态
         * @param ctx
         */
//        private void getState(ChannelHandlerContext ctx){
//                for (Map.Entry<String, DeviceChannel> entry : mChannels.entrySet()) {
//                    DeviceChannel deviceChannel = entry.getValue();
//                    if (ctx.channel().id() == deviceChannel.channel.id()) {
//                        System.out.println("关闭之后的状态" + deviceChannel.channel.isActive());
//                    }
//                }
//                for (Map.Entry<String, List<DeviceChannel>> listEntry : oemDevices.entrySet()) {
//                    List<DeviceChannel> list = listEntry.getValue();
//                    for (DeviceChannel list1 : list) {
//                        if (ctx.channel().id() == list1.channel.id()) {
//                            System.out.println("关闭之后的状态" + list1.channel.isActive());
//                        }
//                    }
//                }
//            }
    }

    public static void main(String[] args) {
        new CommanderServer().run();
    }
}
