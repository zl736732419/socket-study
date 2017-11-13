package com.zheng.socket.netty.echo.delimitor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhenglian
 * @Date 2017/11/13 11:58
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("server recieve command: " + body + "; the counter is: " + ++counter);
        body += "$_"; // 响应消息
        ByteBuf response = Unpooled.copiedBuffer(body.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close(); // 发生异常，关闭链路
    }
}
