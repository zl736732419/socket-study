package com.zheng.socket.netty.echo.fixlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhenglian
 * @Date 2017/11/13 14:10
 */
public class EventClientHandler extends ChannelHandlerAdapter {

    private String ECHO_STR = "welcome to netty world";
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf req = Unpooled.copiedBuffer(ECHO_STR.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("recieve response: " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
