package com.zheng.socket.netty;

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
    ByteBuf request;
    public TimeClinetHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes(StandardCharsets.UTF_8);
        request = Unpooled.buffer(req.length);
        request.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] response = new byte[buf.readableBytes()];
        buf.readBytes(response);
        String body = new String(response, StandardCharsets.UTF_8);
        System.out.println("now is: " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
