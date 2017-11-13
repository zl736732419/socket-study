package com.zheng.socket.netty.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhenglian
 * @Date 2017/11/13 9:17
 */
public class TimeClinetHandler extends ChannelHandlerAdapter {
    private int counter = 0;
    private byte[] req;
    public TimeClinetHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            ByteBuf request = Unpooled.buffer(req.length);
            request.writeBytes(req);
            ctx.writeAndFlush(request);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] response = new byte[buf.readableBytes()];
//        buf.readBytes(response);
//        String body = new String(response, StandardCharsets.UTF_8);
        String body = (String) msg;
        System.out.println("now is: " + body + "; this counter is: " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
