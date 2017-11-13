package com.zheng.socket.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

/**
 * @Author zhenglian
 * @Date 2017/11/13 13:36
 */
public class EchoClient {
    
    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ByteBuf delimitor = Unpooled.copiedBuffer("$_".getBytes(StandardCharsets.UTF_8));
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimitor));
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new EchoClientHandler());
                    }
                });
        // 发起异步调用
        try {
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端链路链接关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
        new EchoClient().connect("localhost", 8080);
    }
}
